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
public class Hashtag {

    private int hashtagID;
    private String hashtagText;
    private int hashtagCount;

    public Hashtag() {
        hashtagID = 0;
        hashtagText = "";
        hashtagCount = 0;
    }

    /**
     * @return the hashtagID
     */
    public int getHashtagID() {
        return hashtagID;
    }

    /**
     * @param hashtagID the hashtagID to set
     */
    public void setHashtagID(int hashtagID) {
        this.hashtagID = hashtagID;
    }

    /**
     * @return the hashtagText
     */
    public String getHashtagText() {
        return hashtagText;
    }

    /**
     * @param hashtagText the hashtagText to set
     */
    public void setHashtagText(String hashtagText) {
        this.hashtagText = hashtagText;
    }

    /**
     * @return the hashtagCount
     */
    public int getHashtagCount() {
        return hashtagCount;
    }

    /**
     * @param hashtagCount the hashtagCount to set
     */
    public void setHashtagCount(int hashtagCount) {
        this.hashtagCount = hashtagCount;
    }
    

}
