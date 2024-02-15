package betacraft;

import org.lwjgl.Version;

import betacraft.engine.Display;

import static jlog.JLog.*;

public class App {
	public static void main(String[] args) {
		Print("Betacraft - " + Version.getVersion(), TYPE.INFO);
		Print("Starting Engine...", TYPE.INFO);
		
		Display d = new Display(1280, 720, "Betacraft");
		d.TestGraphics();
		Print("Display Setup - Complete...", TYPE.INFO);
		while (d.IsOpen())
			d.Update();
		
		Print("Shutting Down...", TYPE.INFO);
		
		d.Close();
		
		Print("Complete. Program Finished!", TYPE.INFO);
		System.exit(0);
	}
}