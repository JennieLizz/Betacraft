package jlengine.engine;

import jleditor.gui.imgui.JLImGui;
import jlengine.components.ComponentBase;
import jlengine.graphics.RenderManager;
import jlengine.graphics.ShaderManager;
import jlengine.model.ModelManager;
import jlengine.utils.JLFrames;
import jlengine.utils.JLLog;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.Objects;
import java.util.Random;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Display {
    static JLLog jl = new JLLog();

    long m_frame;
    int m_width, m_height;
    String m_title;

    ComponentBase m_cb;

    JLImGui m_gui;

    boolean m_editor;

    public Display(int width, int height, String title, String[] args) {
        m_width = width;
        m_height = height;
        m_title = title;

        jl.showTime = true;
        jl.AllowSentFrom(this.getClass());

        if (args != null && args.length > 0) {
            if (args[0].equals("editor")) {
                m_editor = true;
            }
        }

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            jl.Print("glfw failed to init!", JLLog.TYPE.ERROR, false, new IllegalStateException(
                    "Unable to initialize GLFW."));

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        m_frame = glfwCreateWindow(width, height, title, NULL, NULL);
        if (m_frame == NULL)
            jl.Print("glfw failed to init!", JLLog.TYPE.ERROR, false, new RuntimeException(
                    "Unable to initialize the GLFW Window."));

        /*glfwSetKeyCallback(m_frame, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
        });*/

        try {
            glfwSetWindowSizeCallback(m_frame, (window, r_width, r_height) -> {
                SetWidth(r_width);
                SetHeight(r_height);

                glfwSwapBuffers(m_frame);

                if (!m_editor)
                    glViewport(0, 0, r_width, r_height);
            });
        } catch (NullPointerException e) {
            jl.Print("Failed to set window size callback!", JLLog.TYPE.ERROR, false, e);
        }

        try (MemoryStack stack = stackPush()) {
            final IntBuffer pWidth = stack.mallocInt(1);
            final IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(m_frame, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            assert vidmode != null;
            glfwSetWindowPos(
                    m_frame,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(m_frame);
        glfwSwapInterval(1);
        glfwShowWindow(m_frame);

        GL.createCapabilities();

        glClearColor(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 1.0f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_ALPHA_TEST);

        //glEnable(GL_CULL_FACE);
        glFrontFace(GL_CW);

        if (m_editor) {
            m_gui = new JLImGui(this);
            m_gui.Init();
        }
    }

    public int GetWidth() {
        return m_width;
    }

    public void SetWidth(final int width) {
        m_width = width;
    }

    public int GetHeight() {
        return m_height;
    }

    public void SetHeight(final int height) {
        m_height = height;
    }

    public String GetTitle() {
        return m_title;
    }

    public void SetTitle(String title) {
        glfwSetWindowTitle(m_frame, title);
        m_title = title;
    }

    public long GetFrame() {
        return m_frame;
    }

    public boolean IsOpen() {
        return !glfwWindowShouldClose(m_frame);
    }

    public void CloseEarly() {
        glfwSetWindowShouldClose(m_frame, true);
    }

    public boolean IsEditor() {
        return m_editor;
    }

    public JLImGui GetGui() {
        return m_gui;
    }

    void Update(Game game) {
        RenderManager.FrameBuffer.BindFrameBuffer();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if (m_editor) {
            RenderManager.gWidth = m_gui.GetSceneWidth();
            RenderManager.gHeight = m_gui.GetSceneHeight();
        }

        game.Update();

        RenderManager.Render(this);

        RenderManager.FrameBuffer.UnbindFrameBuffer();

        if (m_editor) {
            m_gui.sFrame();
            m_gui.Update();
            m_gui.eFrame();
        }

        JLFrames.UpdateFrameRate();

        glfwSwapBuffers(m_frame);
        glfwPollEvents();
    }

    public void Close() {
        if (m_editor) {
            m_gui.Close();
        }

        RenderManager.FrameBuffer.DeleteFrameBuffer();

        ModelManager.DeleteModels();
        ShaderManager.DeleteShaders();

        glfwFreeCallbacks(m_frame);
        glfwDestroyWindow(m_frame);

        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }
}