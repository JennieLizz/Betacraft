package betacraft.graphics;

import java.util.HashMap;
import java.util.Map;

import betacraft.utils.JLog;

public class ShaderManager {
  static Map<String, Shader> m_shaders = new HashMap<>();

  public static void Render() {
    m_shaders.entrySet().stream().forEach(entry -> {
      entry.getValue().Use();
    });
  }

  static void AddShaderToManager(Class<? extends Shader> shader) {
    JLog jl = new JLog();
    jl.showTime = true;

    try {
      Shader s = shader.getDeclaredConstructor().newInstance();
      m_shaders.put(shader.getSimpleName(), s);
    } catch (Exception e) {
      jl.Print(e.getMessage(), JLog.TYPE.ERROR, false, e);
    }
  }

  static void RemoveShaderFromManager(Class<? extends Shader> shader) {
    JLog jl = new JLog();
    jl.showTime = true;

    try {
      Shader s = shader.getDeclaredConstructor().newInstance();
      m_shaders.remove(shader.getSimpleName(), s);
    } catch (Exception e) {
      jl.Print(e.getMessage(), JLog.TYPE.ERROR, false, e);
    }
  }

  public static Shader GetShader(String name) {
    return m_shaders.get(name);
  }

  public static void DeleteShaders() {
    m_shaders.entrySet().stream().forEach(entry -> {
      entry.getValue().Delete();
    });

    m_shaders.clear();
  }
}
