package betacraft.graphics;

import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;

import java.util.HashMap;
import java.util.Map;

public class ShaderManager {
  static Map<String, Integer> m_vertex = new HashMap<>();
  static Map<String, Integer> m_fragment = new HashMap<>();
  static Map<String, Integer> m_program = new HashMap<>();

  static void AddShaderToManager(String name, int program, int vertex, int fragment) {
    m_vertex.put(name, vertex);
    m_fragment.put(name, fragment);
    m_program.put(name, program);
  }

  static void RemoveShaderFromManager(String name) {
    m_vertex.remove(name);
    m_fragment.remove(name);
    m_program.remove(name);
  }

  public static int GetVertexShader(String name) {
    return m_vertex.get(name);
  }

  public static int GetFragmentShader(String name) {
    return m_fragment.get(name);
  }

  public static int GetProgram(String name) {
    return m_program.get(name);
  }

  public static void DeleteShaders() {
    m_vertex.entrySet().stream().forEach(entry -> {
      glDeleteShader(GetVertexShader(entry.getKey()));
    });
    m_fragment.entrySet().stream().forEach(entry -> {
      glDeleteShader(GetFragmentShader(entry.getKey()));
    });
    m_program.entrySet().stream().forEach(entry -> {
      glDeleteProgram(GetProgram(entry.getKey()));
    });

    m_vertex.clear();
    m_fragment.clear();
    m_program.clear();
  }
}
