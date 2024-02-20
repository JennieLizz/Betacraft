package jlengine.graphics;

import jlengine.engine.Display;
import jlengine.model.RawModel;
import jlengine.utils.JLLog;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class RenderManager {
    public static int cWidth, cHeight;

    public static void Render(Display display) {
        for (RawModel rm : RawModel.GetModels().values()) {
            ShaderManager.ClearShaders();
            ShaderManager.GetLayers().forEach(layer -> {
                if (rm.GetLayer().equals(layer)) {
                    ShaderManager.GetShaders().forEach(shader -> {
                        if (shader.GetLayer().equals(layer)) {
                            shader.Use();
                            if (display.IsEditor())
                                shader.SendUniformVariables(cWidth, cHeight);
                            else
                                shader.SendUniformVariables(display.GetWidth(), display.GetHeight());
                        }
                    });
                }
            });

            rm.Bind();
        }
    }

    public class FrameBuffer {
        static int m_fbo;
        static int m_tex;
        static int m_rbo;

        public static void CopyFrameBuffer(int width, int height) {
            JLLog jl = new JLLog();
            jl.showTime = true;

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
                jl.Print("Failed to create FrameBuffer!", JLLog.TYPE.ERROR, false, new RuntimeException("Failed to create FrameBuffer!"));

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
