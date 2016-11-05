/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

public class ServerListener implements MessageListener<HostedConnection> {
    
    private MainMenu mainMenu = null;
  
    public ServerListener(MainMenu menu) {
        mainMenu = menu;
    }
  
    public void messageReceived(HostedConnection source, Message message) {
        if (message instanceof ProtocolMessage) {
            mainMenu.updateState((ProtocolMessage)message);
        }
    }
}
