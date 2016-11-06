/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.network.ErrorListener;

/**
 *
 * @author user
 */
public class ClientErrorListener implements ErrorListener {

    private StartScreen screen = null;

    public ClientErrorListener(StartScreen screen) {
        this.screen = screen;
    }

    public void handleError(Object source, Throwable t) {
        System.out.println("handle error");
        screen.cleanNetwork();
        screen.owner.clean();
        screen.nifty.gotoScreen("start");
    }
}
