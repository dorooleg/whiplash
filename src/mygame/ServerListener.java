/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.sun.istack.internal.NotNull;

public class ServerListener implements MessageListener<HostedConnection> {
    
    private MainMenu mainMenu = null;
  
    public ServerListener(@NotNull MainMenu menu) {
        mainMenu = menu;
    }
  
    public void messageReceived(HostedConnection source, Message message) {
        if (message instanceof ProtocolMessage) {
            mainMenu.updateState((ProtocolMessage)message);
            // do something with the message
            ProtocolMessage helloMessage = (ProtocolMessage) message;
            System.out.println("Server received" +helloMessage.toString() +"' from client #"+source.getId() );
        } // else....
    }
}
