package betacraft.graphics;

import static org.lwjgl.opengl.GL20.glUseProgram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import betacraft.utils.JLog;

public class ShaderManager {
  static List<String> m_layers = new ArrayList<>(List.of("Default"));
  static Map<String, Shader> m_shaders = new HashMap<>();

  public static void ClearShaders() {
    glUseProgram(0);
  }

  public static void DeleteShader(Shader shader) {
    shader.Delete();
    RemoveShaderFromManager(shader);
  }

  static void AddShaderToManager(Shader shader) {
    m_shaders.put(shader.m_name, shader);
  }

  static void RemoveShaderFromManager(Shader shader) {
    m_shaders.remove(shader.m_name, shader);
  }

  public static void AddLayer(String name) {
    JLog jl = new JLog();
    jl.showTime = true;

    if (!m_layers.contains(name)) {
      m_layers.add(name);
      return;
    }

    jl.Print("Layer " + name + " already exists!", JLog.TYPE.WARNING, false, null);
  }

  public static List<String> GetLayers() {
    return m_layers;
  }

  public static void RemoveLayer(String name) {
    JLog jl = new JLog();
    jl.showTime = true;

    if (name.equals("Default")) {
      jl.Print("Cannot remove default layer!", JLog.TYPE.WARNING, false, null);
      return;
    }

    if (m_layers.contains(name)) {
      for (Shader s : m_shaders.values()) {
        if (s.GetLayer().equals(name)) {
          s.SetLayer("Default");
        }
      }

      m_layers.remove(name);
      return;
    }

    jl.Print("Layer " + name + " does not exist!", JLog.TYPE.WARNING, false, null);
  }

  public static Shader GetShader(String name) {
    JLog jl = new JLog();
    jl.showTime = true;

    for (Map.Entry<String, Shader> entry : m_shaders.entrySet()) {
      if (entry.getKey().equals(name)) {
          return entry.getValue();
      }
    }

    return null;
  }

  public static List<Shader> GetShaders() {
    return new ArrayList<>(m_shaders.values());
  }

  public static void DeleteShaders() {
    m_shaders.entrySet().stream().forEach(entry -> {
      entry.getValue().SDelete();
    });

    m_shaders.clear();
  }
}
