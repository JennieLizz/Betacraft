package jlengine;

import org.joml.Vector2f;
import org.lwjgl.Version;

import jlengine.engine.Display;
import jlengine.engine.Engine;
import jlengine.engine.Game;
import jlengine.graphics.Shader;
import jlengine.utils.JLog;

public class App implements Game {
  static Display d;
  Shader s;

  public static void main(String[] args) {
    JLog jl = new JLog();
    jl.showTime = true;

    d = new Display(1280, 720, "JLE Test!");
    new Engine(d);
  }

  @Override
  public void Init() {
    s = new Shader("Default", "shaders/vertex.glsl", "shaders/fragment.glsl");
  }

  @Override
  public void Update() {
    s.SetVector2f("iResolution", new Vector2f(d.GetWidth(), d.GetHeight()));
    s.SetFloat("iTime", (System.currentTimeMillis() - d.GetStartTime()) * 0.001f);
  }
}
