/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

/**
 *
 * @author yushi
 */
public class UserMention {
    
    private int userID;
    private int twitID;
    
    public UserMention(){
        userID=0;
        twitID=0;
    }

    /**
     * @return the userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @param userID the userID to set
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * @return the twitID
     */
    public int getTwitID() {
        return twitID;
    }

    /**
     * @param twitID the twitID to set
     */
    public void setTwitID(int twitID) {
        this.twitID = twitID;
    }
    
    
    
    
    
    
}
