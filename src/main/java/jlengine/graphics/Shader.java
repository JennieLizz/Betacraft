package jlengine.graphics;

import jlengine.engine.Engine;
import jlengine.utils.JLLog;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader extends ShaderManager {
    String m_shaderLayer = "Default";
    String m_name;
    int m_program;
    int m_vertexShader;
    int m_fragmentShader;

    public Shader(String name, String vertexPath, String fragmentPath) {
        JLLog jl = new JLLog();
        jl.showTime = true;
        jl.AllowSentFrom(this.getClass());

        m_name = name;

        m_vertexShader = LoadShader(vertexPath, GL_VERTEX_SHADER);
        m_fragmentShader = LoadShader(fragmentPath, GL_FRAGMENT_SHADER);

        m_program = glCreateProgram();

        glAttachShader(m_program, m_vertexShader);
        glAttachShader(m_program, m_fragmentShader);

        glLinkProgram(m_program);
        glValidateProgram(m_program);

        AddShaderToManager(this);
    }

    int LoadMissingShader(int shaderType) {
        JLLog jl = new JLLog();
        jl.showTime = true;
        jl.AllowSentFrom(this.getClass());
        String eShaderType = shaderType == GL_VERTEX_SHADER ? "Vertex" : "Fragment";
        jl.Print(eShaderType + " shader has failed to load! Loaded backup shader!", JLLog.TYPE.ERROR, false, new Exception("Shader has failed to load!"));

        String fullVertPath = "src/main/resources/shaders/backupShaders/bk.vert";
        String fullFragPath = "src/main/resources/shaders/backupShaders/bk.frag";
        String simpleVertPath = "shaders/backupShaders/bk.vert";
        String simpleFragPath = "shaders/backupShaders/bk.frag";

        if (shaderType == GL_VERTEX_SHADER) {
            String path = Files.exists(Path.of(fullVertPath)) ? fullVertPath : simpleVertPath;
            return LoadShader(path, GL_VERTEX_SHADER);
        } else if (shaderType == GL_FRAGMENT_SHADER) {
            String path = Files.exists(Path.of(fullFragPath)) ? fullFragPath : simpleFragPath;
            return LoadShader(path, GL_FRAGMENT_SHADER);
        }

        return 0;
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

    public void SetMatrix4f(String name, Matrix4f matrix) {
        float[] buffer = new float[16];
        buffer = matrix.get(buffer);
        glUniformMatrix4fv(glGetUniformLocation(m_program, name), false, buffer);
    }

    public void Use() {
        glUseProgram(m_program);
    }

    public void SendUniformVariables(int width, int height) {
        SetVector2f("iResolution", new Vector2f(width, height));
        SetFloat("iTime", (System.currentTimeMillis() - Engine.GetStartTime()) * 0.001f);
    }

    int LoadShader(String file, int type) {
        JLLog jl = new JLLog();
        jl.showTime = true;

        if (Files.notExists(Path.of(file)))
            return LoadMissingShader(type);

        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        } catch (IOException e) {
            jl.Print("Shader: " + file + " failed to load", JLLog.TYPE.ERROR, false, e);
        }

        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            jl.Print("Shader: " + file + " failed to compile", JLLog.TYPE.ERROR, false, new Exception(
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

        RemoveShaderFromManager(this);
    }

    void SDelete() {
        glDetachShader(m_program, m_vertexShader);
        glDetachShader(m_program, m_fragmentShader);
        glDeleteShader(m_vertexShader);
        glDeleteShader(m_fragmentShader);
        glDeleteProgram(m_program);
    }
}
