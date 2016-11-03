/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import java.util.Random;

/**
 *
 * @author dmitry
 */
public class Sound {
    private AudioNode music;
    private AudioNode[] whips ;
    private AudioNode[] moans ;
    private AssetManager assetManager;

    public Sound(AssetManager assetManager) {
        this.assetManager = assetManager; 
        whips = new AudioNode[4];
        moans = new AudioNode[3];
        loadSounds();
    }

    private void loadSounds() {
//        
        
        
        music = new AudioNode(assetManager, "Sounds/7-sins-OST-East-Wing.wav");
        music.setPositional(false);
        music.setReverbEnabled(false);
        music.setLooping(true);
        
        for (int i = 0; i < 1; i++) {
            whips[i] = new AudioNode(assetManager,
                    "Sounds/whip-"+(i+1)+".wav");
            whips[i].setPositional(false);
            whips[i].setReverbEnabled(false);
            whips[i].setLooping(false);
        }
        
        for (int i = 0; i < 2; i++) {
            moans[i] = new AudioNode(assetManager,
                    "Sounds/moan"+(i+1)+".wav");
            moans[i].setPositional(false);
            moans[i].setReverbEnabled(false);
            moans[i].setLooping(false);
        }

        
    }
    

    public void startMusic() {
        music.play();
    }

    
    public void whip() {
        whips[new Random().nextInt(1)].playInstance();
    }
    
    public void moan() {
        moans[new Random().nextInt(2)].playInstance();
    }

    
}
