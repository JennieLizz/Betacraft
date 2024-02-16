package betacraft.graphics;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class Shader extends ShaderManager {
  String m_name;
  int m_program;
  int m_vertexShader;
  int m_fragmentShader;

  public Shader(String name, String vertexPath, String fragmentPath) {
    m_name = name;
    m_vertexShader = glCreateShader(GL_VERTEX_SHADER);
    m_fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
    m_program = glCreateProgram();

    glShaderSource(m_vertexShader, vertexPath);
    glCompileShader(m_vertexShader);
    glShaderSource(m_fragmentShader, fragmentPath);
    glCompileShader(m_fragmentShader);

    glAttachShader(m_program, m_vertexShader);
    glAttachShader(m_program, m_fragmentShader);
    glLinkProgram(m_program);

    super.AddShaderToManager(name, m_program, m_vertexShader, m_fragmentShader);
  }

  public void Use() {
    glUseProgram(m_program);
  }

  public void Delete() {
    glDeleteProgram(m_program);
    glDeleteShader(m_vertexShader);
    glDeleteShader(m_fragmentShader);

    super.RemoveShaderFromManager(m_name);
  }
}
