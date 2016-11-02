/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.sun.istack.internal.NotNull;

/**
 *
 * @author user
 */
public class ClientListener implements MessageListener<Client> {
  
  private MainMenu mainMenu = null;
  
  public ClientListener(@NotNull MainMenu menu) {
      mainMenu = menu;
  }
  
  public void messageReceived(Client source, Message message) {
    if (message instanceof ProtocolMessage) {
      mainMenu.updateState((ProtocolMessage)message);
    }
  }
}
