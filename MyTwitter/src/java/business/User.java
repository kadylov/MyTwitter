/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import murach.util.PasswordUtil;

/**
 *
 * @javabean for User Entity
 */
public class User implements Serializable {
    //define attributes fullname, ...

    //define set/get methods for all attributes.
    private int userId;
    private String fullName;
    private String username;
    private String emailAddress;
    private String birthDate;
    private String password;
    private int questionNo;
    private String answer;
    private String profilePicture;
    private String last_login_time;
    private String salt;

    public User() {
        userId = 0;
        fullName = "";
        username = "";
        emailAddress = "";
        birthDate = "";
        password = "";
        answer = "";
        profilePicture = "";
        last_login_time = "";
    }

    public User(String fromString) {
        String[] data = fromString.replace("[", "").split(",");
        this.setFullName(data[0]);
        this.setEmailAddress(data[1]);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullname) {
        this.fullName = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthdate(String birthdate) {
        this.birthDate = birthdate;
    }

    public String getPassword() {
       
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(int questionNo) {
        this.questionNo = questionNo;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getLastLoginTime() {
        return getLast_login_time();
    }

    public void setLastLoginTime(String newTime) {
        this.setLast_login_time(newTime);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[%s,%s]", this.getFullName(), this.getEmailAddress()));
        return sb.toString();
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return the last_login_time
     */
    public String getLast_login_time() {
        return last_login_time;
    }

    /**
     * @param last_login_time the last_login_time to set
     */
    public void setLast_login_time(String last_login_time) {
        this.last_login_time = last_login_time;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String newSalt) {
        salt = newSalt;
    }

}
