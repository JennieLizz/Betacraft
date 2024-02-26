package jlengine.engine;

public class Engine {
    static Game m_game;
    static Display m_display;

    public Engine(Display display, Game game) {
        m_display = display;
        m_game = game;

        m_game.Init();

        while (m_display.IsOpen()) {
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
