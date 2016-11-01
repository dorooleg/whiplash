/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.network.AbstractMessage;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class ProtocolMessage extends AbstractMessage  {
    enum STATE_LASH {
        ENABLE,
        DISABLE
    }
    
    private class Entry {
        private long identifier;
        private float x;
        private float y;
        private long color;
        private float rotation;
        private STATE_LASH state;
    }
    
    private ArrayList<Entry> entries;
}
