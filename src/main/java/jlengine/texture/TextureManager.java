package jlengine.texture;

import java.util.HashMap;
import java.util.Map;

public class TextureManager {
    static final Map<String, Texture> m_textures = new HashMap<>();

    static void AddTextureToManager(Texture texture) {
        m_textures.put(texture.m_name, texture);
    }

    static void RemoveTextureFromManager(Texture texture) {
        m_textures.remove(texture.m_name);
    }

    public static Texture GetTexture(String name) {
        return m_textures.get(name);
    }

    public static Map<String, Texture> GetTextures() {
        return m_textures;
    }
}
