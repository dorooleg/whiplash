/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.network.Message;
import com.jme3.network.Network;
import java.io.IOException;

/**
 *
 * @author user
 */
public class Server {

    private final static int PORT = 8100;
    private com.jme3.network.Server server;
    private ServerListener messageListener;
    private ServerConnectionListener connectionListener;

    public Server(MainMenu menu, StartScreen screen) throws IOException {
        server = Network.createServer(PORT);
        messageListener = new ServerListener(menu);
        connectionListener = new ServerConnectionListener(screen);
        server.addMessageListener(messageListener, ProtocolMessage.class);
        server.addConnectionListener(connectionListener);
        server.start();
    }
    
    public void broadcast(Message msg) {
        server.broadcast(msg);
    }
    
    public void close() {
        server.removeConnectionListener(connectionListener);
        server.removeMessageListener(messageListener);
        server.close();
    }
}
