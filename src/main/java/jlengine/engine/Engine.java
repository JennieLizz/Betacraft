package jlengine.engine;

import jleditor.gui.imgui.JLImGui;
import jlengine.components.graphics.camera.EditorCamera;
import jlengine.graphics.RenderManager;
import jlengine.graphics.ShaderManager;
import jlengine.model.ModelManager;
import jlengine.utils.JLFrames;

public class Engine {
    static Display m_display;
    static JLImGui m_gui;
    static EditorCamera m_editorCamera;

    public static void CreateEngine(Game game, String[] args) {
        m_display = new Display(1280, 720, "JLE Test!", args);
        m_gui = new JLImGui(m_display);
        m_gui.Init();

        game.Init();

        if (m_display.IsEditor()) {
            m_editorCamera = new EditorCamera();
        }

        while (m_display.IsOpen()) {
            RenderManager.FrameBuffer.BindFrameBuffer();
            m_display.SetTitle("JLE | " + JLFrames.GetFrameRate());

            m_display.PreUpdate();

            game.Update();

            RenderManager.Render(m_display);

            RenderManager.FrameBuffer.UnbindFrameBuffer();

            if (m_display.IsEditor()) {
                m_gui.sFrame();
                m_gui.Update();
                m_gui.eFrame();
            }

            m_display.PostUpdate();
        }

        game.Close();

        if (m_display.IsEditor()) {
            m_gui.Close();
        }

        RenderManager.FrameBuffer.DeleteFrameBuffer();

        ModelManager.DeleteModels();
        ShaderManager.DeleteShaders();

        m_display.Close();
    }

    public static Display GetDisplay() {
        return m_display;
    }

    public static JLImGui GetGui() {
        return m_gui;
    }

    public static void Close() {
        m_display.CloseEarly();
    }
}
