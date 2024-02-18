package jlengine.engine;

public class Engine {
  Display m_display;

  public Engine(Display display) {
    m_display = display;

    while (m_display.IsOpen()) {
      AUpdate();
    }

    m_display.Close();
  }

  void AUpdate() {
    m_display.Update();
  }
}
