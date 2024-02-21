package jleditor.gui.imgui;

import imgui.ImGui;
import imgui.ImGuiViewport;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import jlengine.engine.Display;
import jlengine.engine.Engine;
import jlengine.graphics.RenderManager;
import jlengine.texture.Texture;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;

public class JLImGui {
    final ImGuiImplGlfw m_guiglfw = new ImGuiImplGlfw();
    final ImGuiImplGl3 m_guigl3 = new ImGuiImplGl3();
    String glslVersion = "#version 330 core";
    long m_frame;
    Display m_display;

    ImGuiViewport viewport;

    float m_sceneWidth, m_sceneHeight, m_sceneWidthOld, m_sceneHeightOld;

    boolean m_holding;

    Texture test;

    public void Init(Display display) {
        m_frame = display.GetFrame();
        m_display = display;

        RenderManager.FrameBuffer.CopyFrameBuffer(display.GetWidth(), display.GetHeight());

        ImGui.createContext();
        m_guiglfw.init(m_frame, true);
        m_guigl3.init(glslVersion);

        ImGui.getIO().setConfigFlags(ImGuiConfigFlags.DockingEnable);

        MainTheme.Init();

        test = new Texture("Test", "FolderIconS.png");

        viewport = ImGui.getMainViewport();
    }

    public void Update() {
        ImGui.dockSpaceOverViewport(viewport, ImGuiDockNodeFlags.PassthruCentralNode);

        if (ImGui.beginMainMenuBar()) {
            if (ImGui.beginMenu("File")) {
                if (ImGui.menuItem("Quit")) {
                    Engine.Close();
                }
                ImGui.endMenu();
            }
        }
        ImGui.endMainMenuBar();

        ImGui.pushStyleVar(ImGuiStyleVar.Alpha, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.WindowBg, 184, 98, 54, 255);
        if (ImGui.begin("BG", ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoBringToFrontOnFocus)) {
            ImGui.setWindowPos(0, 0);
            ImGui.setWindowSize(m_display.GetWidth(), m_display.GetHeight());
        }
        ImGui.end();
        ImGui.popStyleVar();
        ImGui.popStyleColor();

        if (ImGui.begin("Hierarchy")) {
            if (ImGui.beginListBox("##Hierarchy", ImGui.getContentRegionAvailX(), ImGui.getContentRegionAvailY())) {
                for (int i = 0; i < 10; i++) {
                    ImGui.selectable("Object " + i);
                }
            }
            ImGui.endListBox();
        }
        ImGui.end();

        if (ImGui.begin("Properties")) {
            if (ImGui.beginListBox("##Properties", ImGui.getContentRegionAvailX(), ImGui.getContentRegionAvailY())) {
                for (int i = 0; i < 10; i++) {
                    ImGui.selectable("Object " + i);
                }
            }
            ImGui.endListBox();
        }
        ImGui.end();

        int itemsEx = 50;
        if (ImGui.begin("Content Browser")) {
            int col = (int) ImGui.getWindowWidth() / 64;
            if (col > 3) {
                if (ImGui.beginTable("##Content Browser", col, ImGuiTableFlags.NoBordersInBody | ImGuiTableFlags.Sortable | ImGuiTableFlags.Reorderable | ImGuiTableFlags.SizingFixedFit, ImGui.getContentRegionAvailX(), ImGui.getContentRegionAvailY())) {
                    for (int i = 0; i <= itemsEx; i++) {
                        if (ImGui.tableGetColumnIndex() < col) {
                            ImGui.pushStyleColor(ImGuiCol.Button, 0, 0, 0, 0);
                            ImGui.imageButton(test.GetTexture(), 64, 64, 0, 1, 1, 0);
                            ImGui.popStyleColor();
                            ImGui.text("Object " + i);

                            ImGui.tableNextColumn();
                        } else {
                            ImGui.tableNextRow();
                        }
                    }
                }
                ImGui.endTable();
            } else {
                if (ImGui.beginListBox("##Content Browser", ImGui.getContentRegionAvailX(), ImGui.getContentRegionAvailY())) {
                    for (int i = 0; i <= itemsEx; i++) {
                        ImGui.selectable("Object " + i);
                    }
                }
                ImGui.endListBox();
            }
        }
        ImGui.end();

        if (ImGui.begin("Scene")) {
            if (ImGui.beginChild("Render")) {
                m_sceneWidth = ImGui.getWindowWidth();
                m_sceneHeight = ImGui.getWindowHeight();

                boolean resizing = m_sceneWidth != m_sceneWidthOld || m_sceneHeight != m_sceneHeightOld;

                UpdateSceneView();

                if (glfwGetMouseButton(m_frame, GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS && resizing) {
                    m_holding = true;
                    RenderManager.FrameBuffer.CopyFrameBuffer((int) m_sceneWidth, (int) m_sceneHeight);
                    m_sceneWidthOld = m_sceneWidth;
                    m_sceneHeightOld = m_sceneHeight;
                } else if (!m_holding && !resizing) {
                    ImGui.image(RenderManager.FrameBuffer.GetFrameTexture(), m_sceneWidth, m_sceneHeight, 0, 1, 1, 0);
                }

                if (m_holding && glfwGetMouseButton(m_frame, GLFW_MOUSE_BUTTON_LEFT) == GLFW_RELEASE) {
                    m_holding = false;
                }
            }
            ImGui.endChild();
        }
        ImGui.end();
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

    void UpdateSceneView() {
        glViewport(0, 0, (int) m_sceneWidth, (int) m_sceneHeight);
    }

    public int GetSceneWidth() {
        return (int) m_sceneWidth;
    }

    public int GetSceneHeight() {
        return (int) m_sceneHeight;
    }

    public void Close() {
        m_guigl3.dispose();
        m_guiglfw.dispose();
        ImGui.destroyContext();
    }
}
