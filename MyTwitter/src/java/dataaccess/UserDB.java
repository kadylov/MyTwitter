/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import business.User;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDB {

    public static int insert(User user) throws IOException {

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        PreparedStatement ps = null;

        String preparedSQL = "INSERT INTO twitterdb.user(fullname, username, emailAddress, birthdate, password, questionNo, answer, profilePicture, salt) VALUES (?,?,?,?,?,?,?,?,?)";

        int flag = 0;
        try {
            ps = connection.prepareStatement(preparedSQL);
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmailAddress());
            ps.setString(4, user.getBirthDate());
            ps.setString(5, user.getPassword());
            ps.setInt(6, user.getQuestionNo());
            ps.setString(7, user.getAnswer());
            ps.setString(8, user.getProfilePicture());
            ps.setString(9, user.getSalt());

            flag = ps.executeUpdate();

        } catch (SQLException e) {
            for (Throwable t : e) {
                t.printStackTrace();
            }

        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return flag;
    } // end of method insert

    public static int insertLoginTime(String loginTime, String username) throws IOException {

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        PreparedStatement ps = null;

        String preparedSQL = "UPDATE twitterdb.user SET "
                + "last_login_time=? "
                + "WHERE username=?";

        int flag = 0;
        try {
            ps = connection.prepareStatement(preparedSQL);
            ps.setString(1, loginTime);
            ps.setString(2, username);

            flag = ps.executeUpdate();

        } catch (SQLException e) {
            for (Throwable t : e) {
                t.printStackTrace();
            }

        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return flag;
    } // end of method insertLoginTime

    public static ArrayList<User> selectUsers() {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        String query = "Select * from twitterdb.user";
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            ArrayList<User> users = new ArrayList<User>();

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("userID"));
                user.setFullName(rs.getString("fullname"));
                user.setUsername(rs.getString("username"));
                user.setEmailAddress(rs.getString("emailAddress"));
                user.setBirthdate(rs.getString("birthdate"));
                user.setPassword(rs.getString("password"));
                user.setQuestionNo(rs.getInt("questionNo"));
                user.setAnswer(rs.getString("answer"));
                user.setProfilePicture(rs.getString("profilePicture"));
                user.setLastLoginTime(rs.getString("last_login_time"));

                users.add(user);

            }
            return users;

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return null;
    }

    public static User select(String usernameEmail) {

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        PreparedStatement ps = null;

        String query = "SELECT * from twitterdb.user WHERE username=? or emailAddress=?";
        User user = null;
        ResultSet rs = null;
        try {

            ps = connection.prepareStatement(query);
            ps.setString(1, usernameEmail);
            ps.setString(2, usernameEmail);
            rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("userID"));
                user.setFullName(rs.getString("fullname"));
                user.setUsername(rs.getString("username"));
                user.setEmailAddress(rs.getString("emailAddress"));
                user.setPassword(rs.getString("password"));
                user.setBirthdate(rs.getString("birthdate"));
                user.setQuestionNo(rs.getInt("questionNo"));
                user.setAnswer(rs.getString("answer"));
                user.setProfilePicture(rs.getString("profilePicture"));
                user.setLastLoginTime(rs.getString("last_login_time"));

            }

//                statement.close();
        } catch (SQLException e) {
            System.out.println(e);

        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }

        return user;
    } // end of method select
    
    
    
    public static String selectSalt(String usernameEmail) {

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        
        String salt="";

        PreparedStatement ps = null;

        String query = "SELECT salt from twitterdb.user WHERE username=? or emailAddress=?";
        User user = null;
        ResultSet rs = null;
        try {

            ps = connection.prepareStatement(query);
            ps.setString(1, usernameEmail);
            ps.setString(2, usernameEmail);
            rs = ps.executeQuery();

            if (rs.next()) {
               salt = rs.getString("salt");
            }

        } catch (SQLException e) {
            System.out.println(e);

        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }

        return salt;
    } // end of method select

    public static User search(String emailAddress) {

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        PreparedStatement ps = null;

        String query = "SELECT * from twitterdb.user WHERE emailAddress=?";
        User user = null;
        ResultSet rs = null;
        try {

            ps = connection.prepareStatement(query);
            ps.setString(1, emailAddress);
            rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("userID"));
                user.setFullName(rs.getString("fullname"));
                user.setUsername(rs.getString("username"));
                user.setEmailAddress(rs.getString("emailAddress"));
                user.setPassword(rs.getString("password"));
                user.setBirthdate(rs.getString("birthdate"));
                user.setQuestionNo(rs.getInt("questionNo"));
                user.setAnswer(rs.getString("answer"));
                user.setProfilePicture(rs.getString("profilePicture"));
                user.setLastLoginTime(rs.getString("last_login_time"));

            }

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return user;

    } // end of method search

    public static boolean update(User user) {
        boolean flag = false;

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        PreparedStatement ps = null;
        String preparedSQL = "UPDATE twitterdb.user SET "
                + "fullname=?, "
                + "birthdate=?, "
                + "password=?, "
                + "questionNo=?, "
                + "answer=?, "
                + "profilePicture=? "
                + "WHERE username=?";

        try {
            ps = connection.prepareStatement(preparedSQL);
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getBirthDate());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getQuestionNo());
            ps.setString(5, user.getAnswer());
            ps.setString(6, user.getProfilePicture());
            ps.setString(7, user.getUsername());

            ps.executeUpdate();

        } catch (SQLException e) {
            for (Throwable t : e) {
                t.printStackTrace();
            }
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return flag;
    } // end of method update

} // end of class UserDB
