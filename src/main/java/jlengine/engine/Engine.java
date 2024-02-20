package jlengine.engine;

import jllauncher.App;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class Engine {
    static final long m_startTime = System.currentTimeMillis();
    static Game m_game;
    static Display m_display;

    public Engine(Display display) {
        m_display = display;
        m_game = new App();

        m_game.Init();

        while (m_display.IsOpen()) {
            m_display.Update(m_game);
        }

        m_game.Close();
        m_display.Close();
    }

    public static void Close() {
        glfwSetWindowShouldClose(m_display.GetFrame(), true);
    }

    public static long GetStartTime() {
        return m_startTime;
    }
}
