package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * test
 * @author normenhansen
 */
public class Main {

    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setResolution(640, 640);
        MainMenu app = new MainMenu();
        app.setShowSettings(false); // splashscreen
        app.setSettings(settings);
        app.start();
        /*
        JFrame frame = new JFrame();
        JPanel panel = new JPanel(new BorderLayout());
   
        JButton serverBtn = new JButton("Servier");
        JButton clientBtn = new JButton("Client");
        
        serverBtn.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
                ServerMain app = new ServerMain();
                app.start();
            }
        });
        
        clientBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ClientMain app = new ClientMain();
                app.start();    
            }
        });
        
        
        frame.getContentPane().add(panel); 
        frame.setVisible(true);
        frame.setPreferredSize(new Dimension(500, 400));
          
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        * */
    }
    
}
