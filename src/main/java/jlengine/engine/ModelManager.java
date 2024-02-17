package jlengine.engine;

import java.util.HashMap;
import java.util.Map;

import jlengine.graphics.ShaderManager;

public class ModelManager {
  static Map<String, RawModel> m_models = new HashMap<>();

  public static void Render() {
    for (RawModel rm : m_models.values()) {
      ShaderManager.ClearShaders();
      ShaderManager.GetLayers().forEach(layer -> {
        if (rm.GetLayer().equals(layer)) {
          ShaderManager.GetShaders().forEach(shader -> {
            if (shader.GetLayer().equals(layer)) {
              shader.Use();
            }
          });
        }
      });

      rm.Bind();
    }
  }

  static void AddModelToManager(RawModel rModel) {
    m_models.put(rModel.m_name, rModel);
  }

  static void RemoveModelFromManager(RawModel rModel) {
    m_models.remove(rModel.m_name);
  }

  public static void SetModelLayer(String oLayer, String nLayer) {
    m_models.entrySet().stream().forEach(entry -> {
      if (entry.getValue().GetLayer().equals(oLayer)) {
        entry.getValue().SetLayer(nLayer);
      }
    });
  }

  public static RawModel GetModel(String name) {
    return m_models.get(name);
  }

  public static void DeleteModels() {
    m_models.entrySet().stream().forEach(entry -> {
      entry.getValue().SDelete();
    });

    m_models.clear();
  }
}
