package mygame;

import com.jme3.system.AppSettings;

public class Main {

    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setVSync(true);
        settings.setSamples(16);
        
        settings.setResolution(814, 528);
        MainMenu app = new MainMenu();
        app.setShowSettings(false);
        
        app.setSettings(settings);
        app.start();
    }
};
