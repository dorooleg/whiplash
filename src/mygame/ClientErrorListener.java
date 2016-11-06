package mygame;

import com.jme3.network.ErrorListener;

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
