package jlengine.gui.imgui;

import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import jlengine.engine.Display;

import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

public class JLImGui {
  final ImGuiImplGlfw m_guiglfw = new ImGuiImplGlfw();
  final ImGuiImplGl3 m_guigl3 = new ImGuiImplGl3();
  String glslVersion = "#version 330 core";
  long m_frame;

  public void Init(Display display) {
    m_frame = display.GetFrame();

    ImGui.createContext();
    m_guiglfw.init(m_frame, true);
    m_guigl3.init(glslVersion);
  }

  public void Update() {
    ImGui.text("Hello, World!");
    if (ImGui.button("Save")) {
    }
    ImGui.sameLine();
    ImGui.separator();
    ImGui.text("Extra");
  }

  public void sFrame() {
    m_guiglfw.newFrame();
    ImGui.newFrame();
  }

  public void eFrame() {
    ImGui.render();
    m_guigl3.renderDrawData(ImGui.getDrawData());

    if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
      final long bkFramePtr = glfwGetCurrentContext();
      ImGui.updatePlatformWindows();
      ImGui.renderPlatformWindowsDefault();
      glfwMakeContextCurrent(bkFramePtr);
    }
  }

  public void Close() {
    m_guigl3.dispose();
    m_guiglfw.dispose();
    ImGui.destroyContext();
  }
}
