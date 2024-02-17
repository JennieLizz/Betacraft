package betacraft;

import betacraft.engine.Display;
import betacraft.utils.JLog;
import org.lwjgl.Version;

public class App {
  public static void main(String[] args) {
    JLog jl = new JLog();
    jl.showTime = true;

    jl.Print("Betacraft - " + Version.getVersion(), JLog.TYPE.INFO, false, null);
    jl.Print("Starting Engine...", JLog.TYPE.INFO, false, null);

    Display d = new Display(1280, 720, "Betacraft");
    jl.Print("Display Setup - Complete!", JLog.TYPE.INFO, false, null);
    while (d.IsOpen())
      d.Update();

    jl.Print("Shutting Down...", JLog.TYPE.INFO, false, null);

    d.Close();

    jl.Print("Complete. Program Finished!", JLog.TYPE.INFO, false, null);
    System.exit(0);
  }
}
