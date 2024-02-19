package jlengine.engine;

import jlengine.App;

public class Engine {
  final Game m_game;
  final Display m_display;

  static final long m_startTime = System.currentTimeMillis();

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

  public static long GetStartTime() {
      return m_startTime;
  }
}
