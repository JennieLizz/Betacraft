package jlengine.graphics;

import jlengine.utils.JLLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL20.glUseProgram;

public sealed class ShaderManager permits Shader {
    static final List<String> m_layers = new ArrayList<>();
    static final Map<String, Shader> m_shaders = new HashMap<>();
    static JLLog jl = new JLLog();

    static {
        m_layers.addFirst("Default");
    }

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
        if (!m_layers.contains(name)) {
            m_layers.addLast(name);
            return;
        }

        jl.Print("Layer " + name + " already exists!", JLLog.TYPE.WARNING, false, null);
    }

    static void AddLayer(String name, int depth) {
        if (!m_layers.contains(name)) {
            m_layers.add(depth, name);
        }
    }

    public static List<String> GetLayers() {
        return m_layers;
    }

    public static void RemoveLayer(String name) {
        if (name.equals("Default")) {
            jl.Print("Cannot remove default layer!", JLLog.TYPE.WARNING, false, null);
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

        jl.Print("Layer " + name + " does not exist!", JLLog.TYPE.WARNING, false, null);
    }

    public static Shader GetShader(String name) {
        for (Map.Entry<String, Shader> entry : m_shaders.entrySet()) {
            if (entry.getKey().equals(name)) {
                return entry.getValue();
            }
        }

        jl.Print("Shader " + name + " does not exist!", JLLog.TYPE.ERROR, false, null);
        return null;
    }

    public static List<Shader> GetShaders() {
        return new ArrayList<>(m_shaders.values());
    }

    public static void DeleteShaders() {
        m_shaders.forEach((key, value) -> value.SDelete());

        m_shaders.clear();
    }
}
