package mygame;

import com.jme3.system.AppSettings;


public class Main {

  public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        //settings.setFullscreen(true);
        settings.setResolution(960,720);
        MainMenu app = new MainMenu();
        app.setShowSettings(false);
        app.setSettings(settings);
        app.start();
    }
};
