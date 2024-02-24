package jllauncher;

import jlengine.components.graphics.camera.Camera;
import jlengine.engine.Display;
import jlengine.engine.Engine;
import jlengine.engine.Game;
import jlengine.graphics.Shader;
import jlengine.graphics.ShaderManager;
import jlengine.model.RawModel;
import jlengine.utils.JLLog;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import static jlengine.components.math.Quaternion.EulerToQuaternion;

public class App implements Game {
    static JLLog jl;
    static Display d;

    final int[] indices = {
            0, 1, 2
    };
    float[] vertices = {
            0.5f, -0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.0f, 0.5f, 0.0f
    };

    float[] vertices2 = {
            0.5f, 0.5f, 0.0f,  // top right
            0.5f, -0.5f, 0.0f,  // bottom right
            -0.5f, -0.5f, 0.0f,  // bottom left
            -0.5f, 0.5f, 0.0f   // top left
    };
    int[] indices2 = {  // note that we start from 0!
            0, 1, 3,   // first triangle
            1, 2, 3    // second triangle
    };

    Camera cam;
    RawModel rm;
    RawModel bg;

    public static void main(String[] args) {
        jl = new JLLog();
        jl.showTime = true;

        d = new Display(1280, 720, "JLE Test!", new String[]{});
        new Engine(d);
    }

    @Override
    public void Init() {
        rm = new RawModel("test2", vertices, indices);
        rm.transform.SetPosition(new Vector3f(0.0f, 0.0f, -2.0f));
        bg = new RawModel("bg", vertices2, indices2);
        bg.SetLayer("bg");
        bg.transform.SetPosition(new Vector3f(0.0f, 0.0f, -4.0f));
        //bg.transform.SetScale(new Vector3f(50.0f, 50.0f, 0));
        new Shader("test", "src/main/resources/shaders/basic3D/sh.vert", "src/main/resources/shaders/basic3D/sh.frag");
        new Shader("bg", "src/main/resources/shaders/CoolColors/sh.vert", "src/main/resources/shaders/CoolColors/sh.frag")
                .SetLayer("bg");
        ShaderManager.AddLayer("bg");
        cam = new Camera();
    }

    @Override
    public void Update() {
        float rotationSpeed = 1.0f;

        Vector3f rot = new Vector3f(rotationSpeed, 0.0f, 0.0f);
        Quaternionf toQuat = EulerToQuaternion(rot);

        rm.transform.SetRotation(toQuat);
    }
}