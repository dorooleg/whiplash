/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author user
 */


@Serializable
public class ProtocolMessage extends AbstractMessage  {
    
    enum STATE_LASH {
        ENABLE,
        DISABLE
    }
    
    void addEntry(@NotNull Entry entry) {
        entries.add(entry);
    }
    
    @Serializable    
    public static class Entry {
        public Entry() {
            
        }
        public long identifier;
        public float x;
        public float y;
        public ColorRGBA color;
        public Quaternion rotation;
        public ProtocolMessage.STATE_LASH state;
        public boolean  whip_heat;
        public float health_status;
    }
    
    
    @Override
    public String toString() {
        
        StringBuilder stringBuilder = new StringBuilder(); 
       
       Iterator<Entry> it = entries.iterator();
       
       while (it.hasNext()) {
        Entry entry = it.next();
        
        stringBuilder.append("identifier = " + entry.identifier 
                             + " x = " + entry.x + " y = " 
                             + entry.y + " color = " + entry.color 
                             + " rotation " + entry.rotation 
                             + " state = " + entry.state  + " whip_heat = "
                             + entry.whip_heat + " health_status = "
                             + entry.health_status  + "\n");
       }

       return stringBuilder.toString(); 
    }

    public ArrayList<Entry> entries = new ArrayList<Entry>();
}
