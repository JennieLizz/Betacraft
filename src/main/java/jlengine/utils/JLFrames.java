package jlengine.utils;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class JLFrames {
    static long m_startTime = System.currentTimeMillis();
    static double m_lastFrameTime;
    static int m_frameCount;
    static double m_frameRate;
    static float m_frameTimer;

    public static void UpdateFrameRate() {
        m_frameCount++;
        m_frameTimer += GetDeltaTimeF();
    }

    public static int GetFrameRate() {
        if (m_frameTimer >= 1.0) {
            m_frameRate = m_frameCount / GetDeltaTimeD();
            m_frameCount = 0;
            m_frameTimer = 0.0f;
            m_lastFrameTime = glfwGetTime();

            return (int) m_frameRate;
        }

        return (int) m_frameRate;
    }

    public static double GetDeltaTimeD() {
        return glfwGetTime() - m_lastFrameTime;
    }

    public static float GetDeltaTimeF() {
        return (float) (glfwGetTime() - m_lastFrameTime);
    }

    public static long GetStartTime() {
        return m_startTime;
    }

    public static double GetTimeSinceStartD() {
        return glfwGetTime();
    }

    public static float GetTimeSinceStartF() {
        return (float) glfwGetTime();
    }

    public static long GetTimeMillisecs() {
        return System.currentTimeMillis();
    }

    public static float GetShaderTime() {
        return (GetTimeMillisecs() - GetStartTime()) * 0.001f;
    }
}
