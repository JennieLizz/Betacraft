package jlengine.utils;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class JLFrames {
    static double m_lastFrameTime = 0;
    static int m_frameCount = 0;
    static double m_frameRate = 0.0;

    public static int GetFrameRate() {
        double m_currentTime = glfwGetTime();
        double m_deltaTime = m_currentTime - m_lastFrameTime;

        m_frameCount++;

        if (m_deltaTime >= 1.0) {
            m_frameRate = m_frameCount / m_deltaTime;
            m_frameCount = 0;
            m_lastFrameTime = glfwGetTime();

            return (int) m_frameRate;
        }

        return (int) m_frameRate;
    }
}
