/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;

/**
 *
 * @author user
 */
public class ClientConnectionListener implements ClientStateListener {

    private StartScreen screen = null;

    public ClientConnectionListener(StartScreen screen) {
        this.screen = screen;
    }

    public void clientConnected(Client c) {
        screen.startGame("game");
    }

    public void clientDisconnected(Client c, DisconnectInfo info) {
        System.out.println("Disconnected");
        screen.cleanNetwork();
        screen.owner.clean();
        screen.nifty.gotoScreen("start");
    }
}
