package betacraft.engine;

import java.util.HashMap;
import java.util.Map;

import betacraft.utils.JLog;

public class ModelManager {
  static Map<String, RawModel> m_models = new HashMap<>();

  public static void Render() {
    m_models.entrySet().stream().forEach(entry -> {
      entry.getValue().Bind();
    });
  }

  static void AddModelToManager(Class<? extends RawModel> rModel) {
    JLog jl = new JLog();
    jl.showTime = true;

    try {
      RawModel rm = rModel.getDeclaredConstructor().newInstance();
      m_models.put(rModel.getSimpleName(), rm);
    } catch (Exception e) {
      jl.Print(e.getMessage(), JLog.TYPE.ERROR, false, e);
    }
  }

  static void RemoveModelFromManager(Class<? extends RawModel> rModel) {
    JLog jl = new JLog();
    jl.showTime = true;

    try {
      RawModel rm = rModel.getDeclaredConstructor().newInstance();
      m_models.remove(rModel.getSimpleName(), rm);
    } catch (Exception e) {
      jl.Print(e.getMessage(), JLog.TYPE.ERROR, false, e);
    }
  }

  public static RawModel GetModel(String name) {
    return m_models.get(name);
  }

  public static void DeleteModels() {
    m_models.entrySet().stream().forEach(entry -> {
      entry.getValue().Delete();
    });

    m_models.clear();
  }
}
