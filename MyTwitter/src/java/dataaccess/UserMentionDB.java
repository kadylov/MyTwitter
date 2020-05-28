/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import business.Twit;
import business.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author yushi
 */
public class UserMentionDB {

    public static void insert(int twitId, int userId) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        PreparedStatement ps = null;
        String query = "insert into twitterdb.userMentions(twitID, userID) values(?, ?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, twitId);
            ps.setInt(2, userId);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }

    }

    public static ArrayList<Twit> selectMentions(int userId) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        String query = "SELECT * FROM twitterdb.vwMention4 WHERE mentID=? ORDER BY vwMention4.twitDate DESC";
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            ArrayList<Twit> twitList = new ArrayList<Twit>();

            while (rs.next()) {
                Twit t = new Twit();
                t.setTwitId(rs.getInt("twitId"));
                t.setTwitMessage(rs.getString("twitMessage"));
                t.setTwitDate(rs.getString("twitDate"));
                t.setOwnerID(rs.getInt("ownerID"));
                t.setOwnerFullname(rs.getString("ownerFullname"));
                t.setOwnerUsername(rs.getString("ownerUsername"));
                t.setOwnerPicture(rs.getString("ownerPicture"));

                twitList.add(t);

            }
            return twitList;

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return null;
    }

    public static ArrayList<Twit> selectMentions(String username) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        String query = "SELECT * FROM twitterdb.vwMention4 WHERE mentUsername=? ORDER BY vwMention4.twitDate DESC";
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            ArrayList<Twit> twitList = new ArrayList<Twit>();

            while (rs.next()) {
                Twit t = new Twit();
                t.setTwitId(rs.getInt("twitId"));
                t.setTwitMessage(rs.getString("twitMessage"));
                t.setTwitDate(rs.getString("twitDate"));
                t.setOwnerID(rs.getInt("ownerID"));
                t.setOwnerFullname(rs.getString("ownerFullname"));
                t.setOwnerUsername(rs.getString("ownerUsername"));

                twitList.add(t);

            }
            return twitList;

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return null;
    }

    public static ArrayList<Twit> selectLastMentions(String username, String lastLoginTime) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        String query = "SELECT * FROM twitterdb.vwMentionList WHERE mUsername=? and twitDate>? ORDER BY vwMentionList.twitDate DESC";
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, lastLoginTime);
            ResultSet rs = ps.executeQuery();
            ArrayList<Twit> twitList = new ArrayList<Twit>();
            
            
            
            while (rs.next()) {
                Twit t = new Twit();
                t.setTwitId(rs.getInt("twitId"));
                t.setTwitMessage(rs.getString("twitMessage"));
                t.setTwitDate(rs.getString("twitDate"));
                t.setOwnerID(rs.getInt("twitOwner"));
                t.setOwnerFullname(rs.getString("oFullname"));
                t.setOwnerUsername(rs.getString("oUsername"));
                t.setOwnerPicture(rs.getString("oProfilePicture"));

                twitList.add(t);

            }
            return twitList;

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return null;
    }
    
//    get all twits from those the user follows + those the user is mentioned in + those posted by the user
    public static ArrayList<Twit> selectAllTwits(User user) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        String query = "SELECT * FROM twitterdb.vwFollMentPost WHERE mentID=? ORDER BY vwFollMentPost.twitDate DESC";
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, user.getUserId());
            ResultSet rs = ps.executeQuery();
            ArrayList<Twit> twitList = new ArrayList<Twit>();
            
            
            while (rs.next()) {
                Twit t = new Twit();
                t.setTwitId(rs.getInt("twitId"));
                t.setTwitMessage(rs.getString("twitMessage"));
                t.setTwitDate(rs.getString("twitDate"));
                t.setOwnerID(rs.getInt("tOwnerID"));
                t.setOwnerFullname(rs.getString("tOwnerFullname"));
                t.setOwnerUsername(rs.getString("tOwnerUsername"));
                t.setOwnerPicture(rs.getString("tOwnerPic"));

                twitList.add(t);

            }
            return twitList;

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return null;
    }
    
    

}
