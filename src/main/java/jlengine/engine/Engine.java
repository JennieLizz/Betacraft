package jlengine.engine;

import jlengine.utils.JLFrames;

public class Engine {
    static Game m_game;
    static Display m_display;

    public static void CreateEngine(Game game, String[] args) {
        m_display = new Display(1280, 720, "JLE Test!", args);
        m_game = game;

        m_game.Init();

        while (m_display.IsOpen()) {
            m_display.SetTitle("JLE | " + JLFrames.GetFrameRate());

            m_display.Update(m_game);
        }

        m_game.Close();
        m_display.Close();
    }

    public static Display GetDisplay() {
        return m_display;
    }

    public static void Close() {
        m_display.CloseEarly();
    }
}
