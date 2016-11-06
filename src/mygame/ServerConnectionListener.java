/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Server;

/**
 *
 * @author user
 */
public class ServerConnectionListener implements ConnectionListener {
    private StartScreen screen = null;
  
    public ServerConnectionListener(StartScreen screen) {
        this.screen = screen;
    }
    public void connectionAdded(Server s, HostedConnection c) {
        //screen.owner.clean();
        screen.startGame("game");
    }
    
    public void connectionRemoved(Server s, HostedConnection c) {
        screen.cleanNetwork();
        screen.clearPlayers();
        screen.nifty.gotoScreen("start");
    }
    
}
