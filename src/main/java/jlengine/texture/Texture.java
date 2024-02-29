package jlengine.texture;

import jlengine.utils.JLLog;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.BufferUtils.createByteBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.stb.STBImageResize.stbir_resize_uint8_srgb;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.memFree;
import static org.lwjgl.system.MemoryUtil.memSlice;

public class Texture extends TextureManager {
    static JLLog jl = new JLLog();
    String m_name;
    String m_path;
    ByteBuffer m_imageBuffer;
    int m_textureID;
    int m_width;
    int m_height;
    int m_comp;

    int m_MAG_FILTER = GL_LINEAR;
    int m_MIN_FILTER = GL_LINEAR;
    int m_WRAP_S = GL_REPEAT;
    int m_WRAP_T = GL_REPEAT;

    public Texture(String name, String path) {
        jl.showTime = true;
        jl.AllowSentFrom(this.getClass());

        m_name = name;
        m_path = path;

        if (!path.startsWith("http")) {
            if (Files.notExists(Path.of(path))) {
                GenerateMissingTexture();
                return;
            }
        }

        try {
            LoadTextureToBuffer(name, path);
        } catch (Exception e) {
            jl.Print("Texture: Failed to read information! : " + name, JLLog.TYPE.ERROR, false, new Exception(
                    "Texture: Failed to read information! : " + name, e));

            GenerateMissingTexture();
            return;
        }

        m_textureID = glGenTextures();
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, m_textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, m_MAG_FILTER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, m_MIN_FILTER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, m_WRAP_S);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, m_WRAP_T);

        int format;
        if (m_comp == 3) {
            if ((m_width & 3) != 0)
                glPixelStorei(GL_UNPACK_ALIGNMENT, 2 - (m_width & 1));

            format = GL_RGB;
        } else {
            PremultiplyAlpha();

            glEnable(GL_BLEND);
            glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

            format = GL_RGBA;
        }

        glTexImage2D(GL_TEXTURE_2D, 0, m_comp, m_width, m_height, 0, format, GL_UNSIGNED_BYTE, m_imageBuffer);

        ByteBuffer inputPixels = m_imageBuffer;
        int inputWidth = m_width;
        int inputHeight = m_height;
        int mipmapLevel = 0;
        while (1 < inputWidth || 1 < inputHeight) {
            int outputWidth = Math.max(1, inputWidth >> 1);
            int outputHeight = Math.max(1, inputHeight >> 1);
            ByteBuffer outputPixels = createByteBuffer(outputWidth * outputHeight * m_comp);

            stbir_resize_uint8_srgb(
                    inputPixels, inputWidth, inputHeight, inputWidth * m_comp,
                    outputPixels, outputWidth, outputHeight, outputWidth * m_comp, 3, 4, 0);

            glTexImage2D(GL_TEXTURE_2D, ++mipmapLevel, format, outputWidth, outputHeight, 0, format, GL_UNSIGNED_BYTE, outputPixels);

            if (mipmapLevel == 0)
                stbi_image_free(m_imageBuffer);
            else
                memFree(inputPixels);

            inputPixels = outputPixels;
            inputWidth = outputWidth;
            inputHeight = outputHeight;
        }

        if (mipmapLevel == 0)
            stbi_image_free(m_imageBuffer);
        else
            memFree(inputPixels);

        glBindTexture(GL_TEXTURE_2D, 0);

        AddTextureToManager(this);
    }


    ByteBuffer IOResourceToByteBuffer(String resource) throws IOException {
        ByteBuffer buffer;
        Path path = resource.startsWith("http") ? null : Paths.get(resource);
        if (path != null && Files.isReadable(path)) {
            buffer = ReadFromPath(path);
        } else {
            buffer = ReadFromStream(resource);
        }
        buffer.flip();
        return memSlice(buffer);
    }

    ByteBuffer ReadFromPath(Path path) throws IOException {
        ByteBuffer buffer;
        try (SeekableByteChannel fc = Files.newByteChannel(path)) {
            buffer = createByteBuffer((int) fc.size() + 1);
            while (fc.read(buffer) != -1) {
            }
        }
        return buffer;
    }

    ByteBuffer ReadFromStream(String resource) throws IOException {
        ByteBuffer buffer;
        try (InputStream source = resource.startsWith("http") ?
                URI.create(resource).toURL().openStream() :
                Texture.class.getClassLoader().getResourceAsStream(resource)) {
            assert source != null;

            try (ReadableByteChannel rbc = Channels.newChannel(source)) {
                buffer = createByteBuffer(8 * 1024);
                while (true) {
                    int bytes = rbc.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    if (buffer.remaining() == 0) {
                        buffer = ResizeBuffer(buffer, buffer.capacity() * 3 / 2); // 50%
                    }
                }
            }
        }
        return buffer;
    }

    ByteBuffer ResizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    void LoadTextureToBuffer(String name, String path) throws IOException {
        m_imageBuffer = IOResourceToByteBuffer(path);

        try (MemoryStack stack = stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            if (!stbi_info_from_memory(m_imageBuffer, w, h, comp)) {
                jl.Print("Texture: Failed to read information! : " + name, JLLog.TYPE.ERROR, false, new Exception(
                        "Texture: Failed to read information! : " + name));
            }

            stbi_set_flip_vertically_on_load(true);

            m_imageBuffer = stbi_load_from_memory(m_imageBuffer, w, h, comp, 0);
            if (m_imageBuffer == null)
                jl.Print("Texture: Failed to load image! : " + name, JLLog.TYPE.ERROR, false, new Exception(
                        stbi_failure_reason()));

            m_width = w.get(0);
            m_height = h.get(0);
            m_comp = comp.get(0);
        }
    }

    void GenerateMissingTexture() {
        m_width = 1;
        m_height = 1;
        memFree(m_imageBuffer);
        m_textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, m_textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        try (MemoryStack stack = stackPush()) {
            ByteBuffer texBuffer = stack.malloc(1024);
            texBuffer.put((byte) 255);
            texBuffer.put((byte) 0);
            texBuffer.put((byte) 255);
            texBuffer.flip();

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, m_width, m_height, 0, GL_RGB, GL_UNSIGNED_BYTE, texBuffer);
        }

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    void PremultiplyAlpha() {
        int stride = m_width * 4;
        for (int y = 0; y < m_height; y++) {
            for (int x = 0; x < m_width; x++) {
                int pixel = y * stride + x * 4;
                byte alpha = m_imageBuffer.get(pixel + 3);
                m_imageBuffer.put(pixel, (byte) ((m_imageBuffer.get(pixel) & 0xFF) * (alpha & 0xFF) / 255));
                m_imageBuffer.put(pixel + 1, (byte) ((m_imageBuffer.get(pixel + 1) & 0xFF) * (alpha & 0xFF) / 255));
                m_imageBuffer.put(pixel + 2, (byte) ((m_imageBuffer.get(pixel + 2) & 0xFF) * (alpha & 0xFF) / 255));
            }
        }
    }

    public int GetTexture() {
        return m_textureID;
    }

    public void Bind() {
        glBindTexture(GL_TEXTURE_2D, m_textureID);
    }
}