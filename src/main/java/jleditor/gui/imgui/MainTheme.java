package jleditor.gui.imgui;

import imgui.ImGui;
import imgui.ImVec4;
import imgui.flag.ImGuiStyleVar;

import static imgui.flag.ImGuiCol.*;

public class MainTheme {
    public static void Init() {
        ImVec4[] colors = new ImVec4[ModalWindowDimBg + 1];
        colors[Text] = new ImVec4(1.00f, 1.00f, 1.00f, 1.00f);
        colors[TextDisabled] = new ImVec4(0.50f, 0.50f, 0.50f, 1.00f);
        colors[WindowBg] = new ImVec4(0.06f, 0.06f, 0.06f, 0.94f);
        colors[ChildBg] = new ImVec4(0.00f, 0.00f, 0.00f, 0.00f);
        colors[PopupBg] = new ImVec4(0.08f, 0.08f, 0.08f, 0.94f);
        colors[Border] = new ImVec4(0.43f, 0.43f, 0.50f, 0.50f);
        colors[BorderShadow] = new ImVec4(0.00f, 0.00f, 0.00f, 0.00f);
        colors[FrameBg] = new ImVec4(0.39f, 0.29f, 0.20f, 0.54f);
        colors[FrameBgHovered] = new ImVec4(1.00f, 0.77f, 0.44f, 0.40f);
        colors[FrameBgActive] = new ImVec4(1.00f, 0.63f, 0.16f, 0.67f);
        colors[TitleBg] = new ImVec4(0.04f, 0.04f, 0.04f, 1.00f);
        colors[TitleBgActive] = new ImVec4(0.74f, 0.44f, 0.15f, 1.00f);
        colors[TitleBgCollapsed] = new ImVec4(0.00f, 0.00f, 0.00f, 0.51f);
        colors[MenuBarBg] = new ImVec4(0.14f, 0.14f, 0.14f, 1.00f);
        colors[ScrollbarBg] = new ImVec4(0.02f, 0.02f, 0.02f, 0.53f);
        colors[ScrollbarGrab] = new ImVec4(0.31f, 0.31f, 0.31f, 1.00f);
        colors[ScrollbarGrabHovered] = new ImVec4(0.41f, 0.41f, 0.41f, 1.00f);
        colors[ScrollbarGrabActive] = new ImVec4(0.51f, 0.51f, 0.51f, 1.00f);
        colors[CheckMark] = new ImVec4(1.00f, 0.64f, 0.16f, 0.67f);
        colors[SliderGrab] = new ImVec4(1.00f, 0.64f, 0.16f, 0.67f);
        colors[SliderGrabActive] = new ImVec4(1.00f, 0.64f, 0.16f, 0.67f);
        colors[Button] = new ImVec4(1.00f, 0.64f, 0.16f, 0.67f);
        colors[ButtonHovered] = new ImVec4(1.00f, 0.76f, 0.44f, 0.67f);
        colors[ButtonActive] = new ImVec4(1.00f, 0.64f, 0.16f, 0.67f);
        colors[Header] = new ImVec4(1.00f, 0.64f, 0.16f, 0.67f);
        colors[HeaderHovered] = new ImVec4(1.00f, 0.64f, 0.16f, 0.67f);
        colors[HeaderActive] = new ImVec4(1.00f, 0.64f, 0.16f, 0.67f);
        colors[Separator] = new ImVec4(0.43f, 0.43f, 0.50f, 0.50f);
        colors[SeparatorHovered] = new ImVec4(0.72f, 0.47f, 0.14f, 0.67f);
        colors[SeparatorActive] = new ImVec4(1.00f, 0.64f, 0.16f, 0.67f);
        colors[ResizeGrip] = new ImVec4(1.00f, 0.64f, 0.16f, 0.67f);
        colors[ResizeGripHovered] = new ImVec4(1.00f, 0.73f, 0.39f, 0.67f);
        colors[ResizeGripActive] = new ImVec4(1.00f, 0.64f, 0.16f, 0.67f);
        colors[Tab] = new ImVec4(0.77f, 0.53f, 0.22f, 0.67f);
        colors[TabHovered] = new ImVec4(1.00f, 0.75f, 0.41f, 0.67f);
        colors[TabActive] = new ImVec4(1.00f, 0.64f, 0.16f, 0.67f);
        colors[TabUnfocused] = new ImVec4(0.75f, 0.49f, 0.16f, 0.67f);
        colors[TabUnfocusedActive] = new ImVec4(0.82f, 0.58f, 0.26f, 0.67f);
        colors[DockingPreview] = new ImVec4(1.00f, 0.64f, 0.16f, 0.67f);
        colors[DockingEmptyBg] = new ImVec4(0.20f, 0.20f, 0.20f, 1.00f);
        colors[PlotLines] = new ImVec4(0.61f, 0.61f, 0.61f, 1.00f);
        colors[PlotLinesHovered] = new ImVec4(1.00f, 0.43f, 0.35f, 1.00f);
        colors[PlotHistogram] = new ImVec4(0.90f, 0.70f, 0.00f, 1.00f);
        colors[PlotHistogramHovered] = new ImVec4(1.00f, 0.60f, 0.00f, 1.00f);
        colors[TableHeaderBg] = new ImVec4(0.19f, 0.19f, 0.20f, 1.00f);
        colors[TableBorderStrong] = new ImVec4(0.31f, 0.31f, 0.35f, 1.00f);
        colors[TableBorderLight] = new ImVec4(0.23f, 0.23f, 0.25f, 1.00f);
        colors[TableRowBg] = new ImVec4(0.00f, 0.00f, 0.00f, 0.00f);
        colors[TableRowBgAlt] = new ImVec4(1.00f, 1.00f, 1.00f, 0.06f);
        colors[TextSelectedBg] = new ImVec4(0.78f, 0.60f, 0.37f, 0.67f);
        colors[DragDropTarget] = new ImVec4(1.00f, 1.00f, 0.00f, 0.90f);
        colors[NavHighlight] = new ImVec4(1.00f, 0.72f, 0.35f, 1.00f);
        colors[NavWindowingHighlight] = new ImVec4(1.00f, 1.00f, 1.00f, 0.70f);
        colors[NavWindowingDimBg] = new ImVec4(0.80f, 0.80f, 0.80f, 0.20f);
        colors[ModalWindowDimBg] = new ImVec4(0.80f, 0.80f, 0.80f, 0.35f);

        for (int i = 0; i < ModalWindowDimBg; i++) {
            ImGui.pushStyleColor(i, colors[i].x, colors[i].y, colors[i].z, colors[i].w);
        }

        ImGui.pushStyleVar(ImGuiStyleVar.FrameBorderSize, 1);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 6);
        ImGui.pushStyleVar(ImGuiStyleVar.ChildRounding, 6);
        ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, 4);
        ImGui.pushStyleVar(ImGuiStyleVar.PopupRounding, 4);
        ImGui.pushStyleVar(ImGuiStyleVar.ScrollbarRounding, 9);
        ImGui.pushStyleVar(ImGuiStyleVar.GrabRounding, 5);
        ImGui.pushStyleVar(ImGuiStyleVar.TabRounding, 4);
    }
}
