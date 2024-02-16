package betacraft.engine;

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
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import betacraft.utils.JLog;
import betacraft.utils.EMath;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

public class Display {
  JLog jl = new JLog();

  long m_frame;
  int m_width, m_height;
  String m_title;

  public Vector3f verts[] = {
      new Vector3f(0.0f, 0.5f, 0.0f),
      new Vector3f(0.5f, -0.5f, 0.0f),
      new Vector3f(-0.5f, -0.5f, 0.0f)
  };

  int m_vaoId;
  int m_vboId;

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

    m_frame = glfwCreateWindow(GetWidth(), GetHeight(), GetTitle(), NULL, NULL);
    if (m_frame == NULL)
      jl.Print("glfw failed to init!", JLog.TYPE.ERROR, false, new RuntimeException(
          "Unable to initialize the GLFW Window."));

    glfwSetKeyCallback(
        m_frame,
        (window, key, scancode, action, mods) -> {
          if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
            glfwSetWindowShouldClose(window, true);
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
    glfwSwapInterval(1);
    glfwShowWindow(m_frame);

    GL.createCapabilities();

    glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
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

  public void Update() {
    glViewport(0, 0, GetWidth(), GetHeight());
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    glBindVertexArray(m_vaoId);
    glDrawArrays(GL_TRIANGLES, 0, 3);
    glBindVertexArray(0);

    glfwSwapBuffers(m_frame);
    glfwPollEvents();
  }

  public boolean IsOpen() {
    return !glfwWindowShouldClose(m_frame);
  }

  public void Close() {
    glDeleteBuffers(m_vboId);
    glDeleteVertexArrays(m_vaoId);

    glfwFreeCallbacks(m_frame);
    glfwDestroyWindow(m_frame);

    glfwTerminate();
    glfwSetErrorCallback(null).free();
  }

  public void TestGraphics() {
    final IntBuffer vbo = BufferUtils.createIntBuffer(1);
    glGenBuffers(vbo);
    vbo.rewind();
    m_vboId = vbo.get();
    glBindBuffer(GL_ARRAY_BUFFER, m_vboId);

    glBufferData(GL_ARRAY_BUFFER, EMath.Indices.ToFloatArray(verts), GL_STATIC_DRAW);

    final IntBuffer vao = BufferUtils.createIntBuffer(1);
    glGenVertexArrays(vao);
    vao.rewind();
    m_vaoId = vao.get();
    glBindVertexArray(m_vaoId);

    glEnableVertexAttribArray(0);
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

    glBindVertexArray(0);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
  }
}
