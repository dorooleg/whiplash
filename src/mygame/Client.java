/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.network.Message;
import com.jme3.network.Network;

/**
 *
 * @author user
 */
public class Client {

    private com.jme3.network.Client client;
    private final static int PORT = 8100;
    private ClientListener messageListener;
    private ClientConnectionListener connectionListener;
    private ClientErrorListener errorListener;

    public Client(String ip, MainMenu menu, StartScreen screen) throws Exception {
        client = Network.connectToServer(ip, PORT);
        messageListener = new ClientListener(menu);
        client.addMessageListener(messageListener, ProtocolMessage.class);
        connectionListener = new ClientConnectionListener(screen);
        client.addClientStateListener(connectionListener);
        errorListener = new ClientErrorListener(screen);
        client.addErrorListener(errorListener);
        client.start();
    }
    
    public void send(Message msg) {
        client.send(msg);
    }
    
    public void close() {
        client.removeErrorListener(errorListener);
        client.removeMessageListener(messageListener);
        client.removeClientStateListener(connectionListener);
        client.close();
    }
}
