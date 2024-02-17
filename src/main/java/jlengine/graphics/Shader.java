package jlengine.graphics;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.joml.Vector2f;
import org.joml.Vector3f;

import jlengine.utils.JLog;

public class Shader extends ShaderManager {
  String m_shaderLayer = "Default";
  String m_name;
  int m_program;
  int m_vertexShader;
  int m_fragmentShader;

  public Shader(String name, String vertexPath, String fragmentPath) {
    JLog jl = new JLog();
    jl.showTime = true;
    jl.AllowSentFrom(this.getClass());

    jl.Print("Shader Creation Started...", JLog.TYPE.INFO, false, null);

    if (!new File(vertexPath).exists() || !new File(fragmentPath).exists()) {
      jl.Print("Shader: " + name + " does not exist!", JLog.TYPE.ERROR, false, new Exception(
          "Shader: " + name + " does not exist!"));
      return;
    }

    jl.Print("Passed Exist Check...", JLog.TYPE.INFO, false, null);

    m_name = name;
    m_vertexShader = LoadShader(vertexPath, GL_VERTEX_SHADER);
    m_fragmentShader = LoadShader(fragmentPath, GL_FRAGMENT_SHADER);
    m_program = glCreateProgram();

    jl.Print("Loaded and created the Shader Program!", JLog.TYPE.INFO, false, null);

    glAttachShader(m_program, m_vertexShader);
    glAttachShader(m_program, m_fragmentShader);

    glLinkProgram(m_program);
    glValidateProgram(m_program);

    jl.Print("Compiled and linked!", JLog.TYPE.INFO, false, null);

    super.AddShaderToManager(this);

    jl.Print("Shader Creation Complete!", JLog.TYPE.INFO, false, null);
  }

  public void SetLayer(String layer) {
    if (layer.length() > 1) {
      m_shaderLayer = layer;
    }
  }

  public String GetLayer() {
    return m_shaderLayer;
  }

  public void SetInt(String name, int value) {
    glUniform1i(glGetUniformLocation(m_program, name), value);
  }

  public void SetFloat(String name, float value) {
    glUniform1f(glGetUniformLocation(m_program, name), value);
  }

  public void SetVector2f(String name, Vector2f vector) {
    glUniform2f(glGetUniformLocation(m_program, name), vector.x, vector.y);
  }

  public void SetVector3f(String name, Vector3f vector) {
    glUniform3f(glGetUniformLocation(m_program, name), vector.x, vector.y, vector.z);
  }

  public void Use() {
    glUseProgram(m_program);
  }

  private int LoadShader(String file, int type) {
    JLog jl = new JLog();
    jl.showTime = true;

    StringBuilder shaderSource = new StringBuilder();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String line;
      while ((line = reader.readLine()) != null) {
        shaderSource.append(line).append("//\n");
      }
      reader.close();
    } catch (IOException e) {
      jl.Print("Shader: " + file + " failed to load", JLog.TYPE.ERROR, false, e);
    }

    int shaderID = glCreateShader(type);
    glShaderSource(shaderID, shaderSource);
    glCompileShader(shaderID);
    if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
      jl.Print("Shader: " + file + " failed to compile", JLog.TYPE.ERROR, false, new Exception(
          glGetShaderInfoLog(shaderID, 500)));
    }
    return shaderID;
  }

  public void Delete() {
    glDetachShader(m_program, m_vertexShader);
    glDetachShader(m_program, m_fragmentShader);
    glDeleteShader(m_vertexShader);
    glDeleteShader(m_fragmentShader);
    glDeleteProgram(m_program);

    super.RemoveShaderFromManager(this);
  }

  void SDelete() {
    glDetachShader(m_program, m_vertexShader);
    glDetachShader(m_program, m_fragmentShader);
    glDeleteShader(m_vertexShader);
    glDeleteShader(m_fragmentShader);
    glDeleteProgram(m_program);
  }
}
