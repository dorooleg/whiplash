/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Server;
import com.sun.istack.internal.NotNull;

/**
 *
 * @author user
 */
public class ServerConnectionListener implements ConnectionListener {
    private StartScreen screen = null;
  
    public ServerConnectionListener(@NotNull StartScreen screen) {
        this.screen = screen;
    }
    public void connectionAdded(Server s, HostedConnection c) {
        screen.startGame("game");
    }
    
    public void connectionRemoved(Server s, HostedConnection c) {
        screen.clean();
        screen.clearPlayers();
        screen.nifty.gotoScreen("start");
    }
    
}
