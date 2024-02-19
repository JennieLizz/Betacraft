package jlengine.engine;

import imgui.ImGui;
import jlengine.graphics.ShaderManager;
import jlengine.gui.imgui.JLImGui;
import jlengine.utils.JLog;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Display {
  final JLog jl = new JLog();

  final long m_frame;
  int m_width, m_height;
  String m_title;

  JLImGui gui;


  public Display(final int width, final int height, final String title) {
    SetWidth(width);
    SetHeight(height);
    SetTitle(title);

    jl.showTime = true;
    jl.AllowSentFrom(this.getClass());

    GLFWErrorCallback.createPrint(System.err).set();

    if (!glfwInit())
      jl.Print("glfw failed to init!", JLog.TYPE.ERROR, false, new IllegalStateException(
          "Unable to initialize GLFW."));

    glfwDefaultWindowHints();
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

    m_frame = glfwCreateWindow(m_width, m_height, m_title, NULL, NULL);
    if (m_frame == NULL)
      jl.Print("glfw failed to init!", JLog.TYPE.ERROR, false, new RuntimeException(
          "Unable to initialize the GLFW Window."));

    glfwSetKeyCallback(m_frame, (window, key, scancode, action, mods) -> {
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
        glfwSetWindowShouldClose(window, true);
    });

    //noinspection resource
    glfwSetWindowSizeCallback(m_frame, (window, r_width, r_height) -> {
      SetWidth(r_width);
      SetHeight(r_height);
    });

    try (MemoryStack stack = stackPush()) {
      final IntBuffer pWidth = stack.mallocInt(1);
      final IntBuffer pHeight = stack.mallocInt(1);

      glfwGetWindowSize(m_frame, pWidth, pHeight);

      final GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        assert vidmode != null;
        glfwSetWindowPos(
          m_frame,
          (vidmode.width() - pWidth.get(0)) / 2,
          (vidmode.height() - pHeight.get(0)) / 2
      );
    }

    glfwMakeContextCurrent(m_frame);
    glfwSwapInterval(0);
    glfwShowWindow(m_frame);

    GL.createCapabilities();

    glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

    gui = new JLImGui();
    gui.Init(this);
  }

  public int GetWidth() {
    return m_width;
  }

  public void SetWidth(final int width) {
    this.m_width = width;
  }

  public int GetHeight() {
    return m_height;
  }

  public void SetHeight(final int height) {
    this.m_height = height;
  }

  public String GetTitle() {
    return m_title;
  }

  public void SetTitle(final String title) {
    this.m_title = title;
  }

  public long GetFrame() {
    return m_frame;
  }

  public boolean IsOpen() {
    return !glfwWindowShouldClose(m_frame);
  }

  void Update(Game game) {
    glViewport(0, 0, GetWidth(), GetHeight());
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    game.Update();

    ModelManager.Render();

    gui.sFrame();

    ImGui.begin("Bruh");

    ImGui.text("HELLOOO");

    ImGui.end();

    gui.eFrame();

    glfwSwapBuffers(m_frame);
    glfwPollEvents();
  }

  public void Close() {
    gui.Close();

    ModelManager.DeleteModels();
    ShaderManager.DeleteShaders();

    glfwFreeCallbacks(m_frame);
    glfwDestroyWindow(m_frame);

    glfwTerminate();
    Objects.requireNonNull(glfwSetErrorCallback(null)).free();
  }
}