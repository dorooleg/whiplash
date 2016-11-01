/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import de.lessvoid.nifty.Nifty;

/**
 *
 * @author user
 */
public class MainMenu extends SimpleApplication {

    private int health;
    private StartScreen startScreen;
  
    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);

        /**
         * Just some simple JME content to show it's really a JME app:
         */
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        Geometry geom = new Geometry("Box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);

        startScreen = new StartScreen();
        stateManager.attach(startScreen);

        /**
         * Åctivate the Nifty-JME integration: 
         */
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        nifty.fromXml("Interface/tutorial/screen3.xml", "start", startScreen);
        //nifty.setDebugOptionPanelColors(true);

        flyCam.setDragToRotate(true); // you need the mouse for clicking now  
    }
    
}
