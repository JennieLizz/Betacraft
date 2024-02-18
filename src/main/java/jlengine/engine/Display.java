package jlengine.engine;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import jlengine.graphics.Shader;
import jlengine.graphics.ShaderManager;
import jlengine.utils.JLog;

public class Display {
  JLog jl = new JLog();

  long m_frame;
  int m_width, m_height;
  String m_title;

  long m_startTime = System.currentTimeMillis();

  float[] vertices = {
      -1, 1, 0, // V0
      -1, -1, 0, // V1
      1, -1, 0, // V2
      1, 1, 0 // V3
  };

  int[] indices = {
      0, 1, 3, // Top left triangle (V0,V1,V3)
      3, 1, 2 // Bottom right triangle (V3,V1,V2)
  };

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

    glfwSetWindowSizeCallback(m_frame, (window, r_width, r_height) -> {
      SetWidth(r_width);
      SetHeight(r_height);
    });

    try (MemoryStack stack = stackPush()) {
      final IntBuffer pWidth = stack.mallocInt((int) 1);
      final IntBuffer pHeight = stack.mallocInt((int) 1);

      glfwGetWindowSize(m_frame, pWidth, pHeight);

      final GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

      glfwSetWindowPos(
          m_frame,
          (vidmode.width() - pWidth.get(0)) / 2,
          (vidmode.height() - pHeight.get(0)) / 2);
    }

    glfwMakeContextCurrent(m_frame);
    glfwSwapInterval(0);
    glfwShowWindow(m_frame);

    GL.createCapabilities();

    glClearColor(1.0f, 0.0f, 0.0f, 0.0f);


    new RawModel("test2", vertices, indices);
    new Shader("test", "src\\main\\resources\\shaders\\Raymarchingtest\\Raymarchingtest.vert",
        "src\\main\\resources\\shaders\\Raymarchingtest\\Raymarchingtest.frag");
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

  public float GetStartTime() {
    return m_startTime;
  }

  public boolean IsOpen() {
    return !glfwWindowShouldClose(m_frame);
  }

  void Update() {
    glViewport(0, 0, GetWidth(), GetHeight());
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    ModelManager.Render();

    glfwSwapBuffers(m_frame);
    glfwPollEvents();
  }

  public void Close() {
    ModelManager.DeleteModels();
    ShaderManager.DeleteShaders();

    glfwFreeCallbacks(m_frame);
    glfwDestroyWindow(m_frame);

    glfwTerminate();
    glfwSetErrorCallback(null).free();
  }
}