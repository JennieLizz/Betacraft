package jlengine.model;

import java.util.HashMap;
import java.util.Map;

public class ModelManager {
    static final Map<String, RawModel> m_models = new HashMap<>();

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

    public static Map<String, RawModel> GetModels() {
        return m_models;
    }

    public static void DeleteModels() {
        m_models.forEach((key, value) -> value.SDelete());
        m_models.clear();
    }
}
