package jlengine;

import jlengine.engine.Display;
import jlengine.engine.Engine;
import jlengine.engine.Game;
import jlengine.engine.RawModel;
import jlengine.graphics.Shader;
import jlengine.utils.JLog;
import org.joml.Vector2f;

public class App implements Game {
  static JLog jl;
  static Display d;
  Shader s;

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

  public static void main(String[] args) {
    jl = new JLog();
    jl.showTime = true;

    d = new Display(1280, 720, "JLE Test!");
    new Engine(d);
  }

  @Override
  public void Init() {
    new RawModel("test2", vertices, indices);
    s = new Shader("test", "src\\main\\resources\\shaders\\Raymarchingtest\\Raymarchingtest.vert",
            "src\\main\\resources\\shaders\\Raymarchingtest\\Raymarchingtest.frag");
  }

  @Override
  public void Update() {
    s.SetVector2f("iResolution", new Vector2f(d.GetWidth(), d.GetHeight()));
    s.SetFloat("iTime", (System.currentTimeMillis() - Engine.GetStartTime()) * 0.001f);
  }
}
