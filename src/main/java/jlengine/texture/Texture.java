package jlengine.texture;

import jlengine.utils.JLLog;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.stbi_info_from_memory;

public class Texture extends TextureManager {
    int m_width;
    int m_height;
    int m_textureID;

    public Texture(String name, String path) {
        JLLog jl = new JLLog();
        jl.showTime = true;
        jl.AllowSentFrom(this.getClass());

        if (!new File(path).exists()) {
            jl.Print("Texture: " + name + " does not exist!", JLLog.TYPE.ERROR, false, new Exception(
                    "Texture: " + name + " does not exist!"));
            return;
        }

        ByteBuffer imageBuffer = null;
        try {
            //imageBuffer = ioResourceToByteBuffer(path, 8 * 1024);
        } catch (Exception e) {
            jl.Print("Texture: Failed to read information! : " + name, JLLog.TYPE.ERROR, false, new Exception(
                    "Texture: Failed to read information! : " + name, e));
        }

        IntBuffer w = IntBuffer.allocate(1);
        IntBuffer h = IntBuffer.allocate(1);
        IntBuffer comp = IntBuffer.allocate(1);

        if (!stbi_info_from_memory(imageBuffer, w, h, comp)) {
            jl.Print("Texture: Failed to read information! : " + name, JLLog.TYPE.ERROR, false, new Exception(
                    "Texture: Failed to read information! : " + name));
        }
    }
}
