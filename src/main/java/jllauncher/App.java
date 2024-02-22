package jllauncher;

import jlengine.engine.Display;
import jlengine.engine.Engine;
import jlengine.engine.Game;
import jlengine.graphics.Shader;
import jlengine.model.RawModel;
import jlengine.utils.JLLog;

public class App implements Game {
    static JLLog jl;
    static Display d;

    final float[] vertices = {
            -1, 1, 0, // V0
            -1, -1, 0, // V1
            1, -1, 0, // V2
            1, 1, 0 // V3
    };
    final int[] indices = {
            0, 1, 3, // Top left triangle (V0,V1,V3)
            3, 1, 2 // Bottom right triangle (V3,V1,V2)
    };
    Shader s;

    public static void main(String[] args) {
        jl = new JLLog();
        jl.showTime = true;

        d = new Display(1280, 720, "JLE Test!", new String[]{"editor"});
        new Engine(d);
    }

    @Override
    public void Init() {
        new RawModel("test2", vertices, indices);
        s = new Shader("test", "src/main/resources/shders/Raymarchingtest/Raymarchingtest.vert", "src/main/resources/shaders/Raymarchingtest/Raymarchingtest.frag");
    }

    @Override
    public void Update() {

    }
}