package betacraft.graphics;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
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
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import betacraft.utils.JLog;

public class Shader extends ShaderManager {
  String m_name;
  int m_program;
  int m_vertexShader;
  int m_fragmentShader;

  JLog jl = new JLog();

  public Shader(String name, String vertexPath, String fragmentPath) {
    jl.showTime = true;
    jl.AllowSentFrom(this.getClass());

    if (!new File(vertexPath).exists() || !new File(fragmentPath).exists()) {
      jl.Print("Shader: " + name + " does not exist!", JLog.TYPE.ERROR, false, new Exception(
          "Shader: " + name + " does not exist!"));
      return;
    }

    m_name = name;
    m_vertexShader = LoadShader(vertexPath, GL_VERTEX_SHADER);
    m_fragmentShader = LoadShader(fragmentPath, GL_VERTEX_SHADER);
    m_program = glCreateProgram();

    glShaderSource(m_vertexShader, vertexPath);
    glCompileShader(m_vertexShader);
    glShaderSource(m_fragmentShader, fragmentPath);
    glCompileShader(m_fragmentShader);

    glAttachShader(m_program, m_vertexShader);
    glAttachShader(m_program, m_fragmentShader);
    glLinkProgram(m_program);
    glValidateProgram(m_program);

    super.AddShaderToManager(name, m_program, m_vertexShader, m_fragmentShader);
  }

  public void Use() {
    glUseProgram(m_program);
  }

  public void Delete() {
    glDetachShader(m_program, m_vertexShader);
    glDetachShader(m_program, m_fragmentShader);
    glDeleteShader(m_vertexShader);
    glDeleteShader(m_fragmentShader);
    glDeleteProgram(m_program);

    super.RemoveShaderFromManager(m_name);
  }

  private static int LoadShader(String file, int type) {
    StringBuilder shaderSource = new StringBuilder();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String line;
      while ((line = reader.readLine()) != null) {
        shaderSource.append(line).append("//\n");
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }
    int shaderID = glCreateShader(type);
    glShaderSource(shaderID, shaderSource);
    glCompileShader(shaderID);
    if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
      System.out.println(glGetShaderInfoLog(shaderID, 500));
      System.err.println("Could not compile shader!");
      System.exit(-1);
    }
    return shaderID;
  }

}
