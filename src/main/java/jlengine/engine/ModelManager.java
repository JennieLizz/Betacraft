package jlengine.engine;

import jlengine.graphics.ShaderManager;

import java.util.HashMap;
import java.util.Map;

public class ModelManager {
  static final Map<String, RawModel> m_models = new HashMap<>();

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
    m_models.forEach((key, value) -> {
        if (value.GetLayer().equals(oLayer)) {
            value.SetLayer(nLayer);
        }
    });
  }

  public static RawModel GetModel(String name) {
    return m_models.get(name);
  }

  public static void DeleteModels() {
    m_models.forEach((key, value) -> value.SDelete());

    m_models.clear();
  }
}
