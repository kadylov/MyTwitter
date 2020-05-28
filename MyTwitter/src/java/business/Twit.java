package business;

public class Twit {

    private int twitId;
    private String twitMessage;
    private String twitDate;
    private int ownerID;
    private String ownerUsername;
    private String ownerFullname;
    private String ownerPicture;

    public Twit() {
        twitId = 0;
        twitMessage = "";
        twitDate = "";
        ownerID = 0;
        ownerUsername = "";
        ownerFullname = "";
        ownerPicture="";
    }

    
    
    
    /**
     * @return the user
     */
    public int getOwnerID() {
        return ownerID;
    }

    /**
     * @param ownerID the user to set
     */
    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    /**
     * @return the twitMessage
     */
    public String getTwitMessage() {
        return twitMessage;
    }

    /**
     * @param twitMessage the twitMessage to set
     */
    public void setTwitMessage(String twitMessage) {
        this.twitMessage = twitMessage;
    }

    /**
     * @return the twitDate
     */
    public String getTwitDate() {
        return twitDate;
    }

    /**
     * @param twitDate the twitDate to set
     */
    public void setTwitDate(String twitDate) {
        this.twitDate = twitDate;
    }

    /**
     * @return the ownerUsername
     */
    public String getOwnerUsername() {
        return ownerUsername;
    }

    /**
     * @param ownerUsername the ownerUsername to set
     */
    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    /**
     * @return the ownerFullname
     */
    public String getOwnerFullname() {
        return ownerFullname;
    }

    /**
     * @param ownerFullname the ownerFullname to set
     */
    public void setOwnerFullname(String ownerFullname) {
        this.ownerFullname = ownerFullname;
    }

    /**
     * @return the twitId
     */
    public int getTwitId() {
        return twitId;
    }

    /**
     * @param twitId the twitId to set
     */
    public void setTwitId(int twitId) {
        this.twitId = twitId;
    }

    /**
     * @return the ownerPicture
     */
    public String getOwnerPicture() {
        return ownerPicture;
    }

    /**
     * @param ownerPicture the ownerPicture to set
     */
    public void setOwnerPicture(String ownerPicture) {
        this.ownerPicture = ownerPicture;
    }

}
