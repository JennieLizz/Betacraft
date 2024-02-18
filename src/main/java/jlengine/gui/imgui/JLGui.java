package jlengine.gui.imgui;

import imgui.ImGui;
import imgui.glfw.ImGuiImplGlfw;
import jlengine.engine.Display;

public class JLGui {
  ImGuiImplGlfw m_guiglfw = new ImGuiImplGlfw();
  long m_frame;

  public void InitGui(Display display) {
    m_frame = display.GetFrame();

    m_guiglfw.init(m_frame, true);
    ImGui.createContext();
  }
}
