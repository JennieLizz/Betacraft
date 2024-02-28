package jllauncher;

import jlengine.components.graphics.camera.Camera;
import jlengine.engine.Engine;
import jlengine.engine.Game;
import jlengine.graphics.Shader;
import jlengine.model.RawModel;
import jlengine.texture.Texture;
import jlengine.utils.JLFrames;
import jlengine.utils.JLLog;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class App implements Game {
    static JLLog jl = new JLLog();

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
        jl.showTime = true;

        new Engine(new App(), args);
    }

    @Override
    public void Init() {
        Shader le = new Shader("test", "src/main/resources/shaders/basic3D/sh.vert", "src/main/resources/shaders/basic3D/sh.frag");
        le.SetLayer("Troll");

        for (float i = -3.0f; i <= 3.0f; i += 0.5f) {
            RawModel troll = new RawModel(i + "", vertices, indices);
            troll.transform.SetPosition(new Vector3f(i, 0.0f, -2.0f));
            troll.SetLayer("Troll");
        }

        Texture trollFace = new Texture("TrollMan", "C:/Users/jenni/Downloads/trollBruh.png");

        le.SetVariable("image", trollFace.GetTexture());

        new Shader("Over", "src/main/resources/shaders/CoolColors/sh.vert", "src/main/resources/shaders/CoolColors/sh.frag")
                .SetLayer("Overlay");

        RawModel bruh = new RawModel("Overlay", vertices, indices);
        bruh.SetLayer("Overlay");
        bruh.transform.SetScale(new Vector3f(3, 3, 0));

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