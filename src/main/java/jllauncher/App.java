package jllauncher;

import jlengine.components.graphics.camera.Camera;
import jlengine.engine.Display;
import jlengine.engine.Engine;
import jlengine.engine.Game;
import jlengine.graphics.Shader;
import jlengine.graphics.ShaderManager;
import jlengine.model.ModelManager;
import jlengine.model.RawModel;
import jlengine.texture.Texture;
import jlengine.utils.JLFrames;
import jlengine.utils.JLLog;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Arrays;

import static org.lwjgl.opengl.GL13.*;

public class App implements Game {
    static JLLog jl;
    static Display d;

    float[] vertices = {
            // positions          // colors           // texture coords
            0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, // top right
            0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, // bottom right
            -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, // bottom left
            -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f  // top left
    };

    int[] indices = {
            0, 1, 3, // first triangle
            1, 2, 3  // second triangle
    };

    Camera cam;

    public static void main(String[] args) {
        jl = new JLLog();
        jl.showTime = true;

        d = new Display(1280, 720, "JLE Test!", args);
        new Engine(d);
    }

    @Override
    public void Init() {
        /*new Shader("bg", "src/main/resources/shaders/basic3D/sh.vert", "src/main/resources/shaders/basic3D/sh.frag")
                .SetLayer("bg", 0);
        */

        Shader le = new Shader("test", "src/main/resources/shaders/basic3D/sh.vert", "src/main/resources/shaders/basic3D/sh.frag");
        le.SetLayer("Troll");

        RawModel troll = new RawModel(1 + "", vertices, indices);
        troll.transform.SetPosition(new Vector3f(0.0f, 0.0f, -2.0f));
        troll.SetLayer("Troll");

        Texture trollFace = new Texture("TrollMan", "C:/Users/jenni/Downloads/trollBruh.png");
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, trollFace.GetTexture());

        le.SetVariable("image", trollFace.GetTexture());

        new Shader("Over", "src/main/resources/shaders/CoolColors/sh.vert", "src/main/resources/shaders/CoolColors/sh.frag")
                .SetLayer("Overlay", 0);

        RawModel bruh = new RawModel("Overlay", vertices, indices);
        bruh.SetLayer("Overlay");
        bruh.transform.SetScale(new Vector3f(3, 3, 0));

        JLLog.SFPrint(Arrays.toString(ShaderManager.GetLayers().toArray()));
        JLLog.SFPrint(Arrays.toString(ModelManager.GetModels().keySet().toArray()));

        cam = new Camera();
        cam.view.SetTransform(new Matrix4f().setLookAt(
                0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, -3.0f,
                0.0f, 1.0f, 0.0f
        ));
    }

    @Override
    public void Update() {
        float radius = 3.0f;
        float camX = Math.sin(JLFrames.GetTimeSinceStartF()) * radius;
        float camZ = Math.cos(JLFrames.GetTimeSinceStartF()) * radius;
        cam.view.SetTransform(new Matrix4f().setLookAt(
                camX, 0.0f, camZ,
                0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f
        ));
    }
}