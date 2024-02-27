package jlengine.graphics;

import jlengine.components.graphics.camera.Camera;
import jlengine.engine.Display;
import jlengine.model.RawModel;
import jlengine.utils.JLLog;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class RenderManager {
    public static int gWidth, gHeight;
    public static Shader default3D;

    static JLLog jl = new JLLog();
    static Camera m_camera;

    static {
        default3D = new Shader("Default", "src/main/resources/shaders/basic3D/sh.vert", "src/main/resources/shaders/basic3D/sh.frag");
    }

    public static void SetUseCamera(Camera camera) {
        m_camera = camera;
    }

    public static void Render(Display display) {
        for (RawModel rm : RawModel.GetModels().values()) {
            ShaderManager.ClearShaders();
            ShaderManager.GetLayers().forEach(layer -> {
                if (rm.GetLayer().equals(layer)) {
                    ShaderManager.GetShaders().forEach(shader -> {
                        if (shader.GetLayer().equals(layer)) {
                            Matrix4f viewMat = m_camera.view.GetTransform();
                            Matrix4f projMat = m_camera.perspective.GetTransform();
                            Matrix4f modelMat = rm.transform.GetTransform();

                            Matrix4f[] mats = {viewMat, projMat, modelMat};

                            if (display.IsEditor())
                                shader.SendUniformVariables(gWidth, gHeight, mats);
                            else
                                shader.SendUniformVariables(display.GetWidth(), display.GetHeight(), mats);

                            shader.Use();
                            rm.Bind();
                        }
                    });
                }
            });
        }
    }

    public static class FrameBuffer {
        static int m_fbo;
        static int m_tex;
        static int m_rbo;

        public static void CopyFrameBuffer(int width, int height) {
            m_fbo = glGenFramebuffers();
            glBindFramebuffer(GL_FRAMEBUFFER, m_fbo);

            m_tex = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, m_tex);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, NULL);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, m_tex, 0);

            m_rbo = glGenRenderbuffers();
            glBindRenderbuffer(GL_RENDERBUFFER, m_rbo);
            glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, width, height);
            glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, m_rbo);

            if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
                jl.Print("Failed to create FrameBuffer!", JLLog.TYPE.WARNING, false, null);

            glBindFramebuffer(GL_FRAMEBUFFER, 0);
            glBindTexture(GL_TEXTURE_2D, 0);
            glBindRenderbuffer(GL_RENDERBUFFER, 0);
        }

        public static void DeleteFrameBuffer() {
            glDeleteFramebuffers(m_fbo);
            glDeleteTextures(m_tex);
            glDeleteRenderbuffers(m_rbo);
        }

        public static int GetFrameTexture() {
            return m_tex;
        }

        public static void RescaleFrameBuffer(int width, int height) {
            glBindTexture(GL_TEXTURE_2D, m_tex);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, NULL);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, m_tex, 0);

            glBindRenderbuffer(GL_RENDERBUFFER, m_rbo);
            glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, width, height);
            glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, m_rbo);
        }

        public static void BindFrameBuffer() {
            glBindFramebuffer(GL_FRAMEBUFFER, m_fbo);
        }

        public static void UnbindFrameBuffer() {
            glBindFramebuffer(GL_FRAMEBUFFER, 0);
        }
    }
}
