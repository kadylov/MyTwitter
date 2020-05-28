/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import business.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author yushi
 */
public class FolowDB {

    public static int insert(User user, User followedUser, String followDate) throws IOException {

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        PreparedStatement ps = null;

        String preparedSQL = "INSERT INTO twitterdb.follow(userID,followedUserID,followDate) VALUES (?,?,?)";

        int flag = 0;
        try {
            ps = connection.prepareStatement(preparedSQL);
            ps.setInt(1, user.getUserId());
            ps.setInt(2, followedUser.getUserId());
            ps.setString(3, followDate);

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

    
    
    public static ArrayList<User> selectFolowers(User currUser) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

//        String query = "Select * from twitterdb.vwFolow where cUserID=?";
        String query = "SELECT  * FROM twitterdb.vwFollowers WHERE followedUserID=?";

        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, currUser.getUserId());

            ResultSet rs = ps.executeQuery();
            ArrayList<User> users = new ArrayList<>();

            while (rs.next()) {

                User user = new User();
                user.setUserId(rs.getInt("fingUserID"));
                user.setFullName(rs.getString("fingUserFullname"));
                user.setUsername(rs.getString("fingUserUsername"));
                user.setEmailAddress(rs.getString("fingUserEmail"));
                user.setBirthdate(rs.getString("fingUserBirth"));
                user.setPassword(rs.getString("fingUserPass"));
                user.setQuestionNo(rs.getInt("fingUserQuestion"));
                user.setAnswer(rs.getString("fingUserAnswer"));
                user.setProfilePicture(rs.getString("fingUserPic"));
                user.setLastLoginTime(rs.getString("followDate"));              //last login time attribute is used as followDate
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

    public static HashMap<String, User> selectFolowedUsers1(User currUser) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

//        String query = "Select * from twitterdb.vwFolow where cUserID=?";
        String query = "SELECT  * FROM twitterdb.follow, twitterdb.user WHERE follow.followedUserID = user.userID and follow.userID=?";

        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, currUser.getUserId());

            ResultSet rs = ps.executeQuery();
            HashMap<String, User> users = new HashMap<String, User>();

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
                user.setLastLoginTime(rs.getString("followDate"));
                users.put(user.getUsername(), user);

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

    public static boolean isFollowedUserInserted(User currUser, User followedUser) {

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        PreparedStatement ps = null;
        boolean found = false;

        String query = "SELECT * from twitterdb.follow WHERE userID=? and followedUserID=?";
        ResultSet rs = null;
        try {

            ps = connection.prepareStatement(query);
            ps.setInt(1, currUser.getUserId());
            ps.setInt(2, followedUser.getUserId());
            rs = ps.executeQuery();

            if (rs.next()) {
                found = true;

            }

        } catch (SQLException e) {
            System.out.println(e);

        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }

        return found;
    } // end of method select

    public static int countFollowing(User currUser) {

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        PreparedStatement ps = null;
        int count = 0;

        String query = "Select count(fingUserID) as following from twitterdb.vwFollowing where fingUserID = ?";
        ResultSet rs = null;
        try {

            ps = connection.prepareStatement(query);
            ps.setInt(1, currUser.getUserId());
            rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt("following");

            }

        } catch (SQLException e) {
            System.out.println(e);

        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }

        return count;
    } // end of method select

    public static int countFollowers(User currUser) {

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        PreparedStatement ps = null;
        int count = 0;

        String query = "Select count(fingUserID) as followers from twitterdb.vwFollowers where followedUserID = ?";
        ResultSet rs = null;
        try {

            ps = connection.prepareStatement(query);
            ps.setInt(1, currUser.getUserId());
            rs = ps.executeQuery();

            if (rs.next()) {
                count = rs.getInt("followers");

            }

        } catch (SQLException e) {
            System.out.println(e);

        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }

        return count;
    } // end of method select

    public static int delete(User user) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        PreparedStatement ps = null;

        String query = "DELETE FROM twitterdb.follow WHERE followedUserID=?";
        int result = 0;
        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, user.getUserId());

            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return result;
    } // end of method delete

}
