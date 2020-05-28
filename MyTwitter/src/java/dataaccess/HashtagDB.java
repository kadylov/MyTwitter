/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import business.Hashtag;
import business.Twit;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author yushi
 */
public class HashtagDB {

    public static int insert(Hashtag hashtag) throws IOException {

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        PreparedStatement ps = null;
        // # hashtagID, hashtagText, hashtagCount

        String preparedSQL = "INSERT INTO twitterdb.hashtag(hashtagText, hashtagCount) VALUES (?,?)";

        int flag = 0;
        try {
            ps = connection.prepareStatement(preparedSQL);
            ps.setString(1, hashtag.getHashtagText());
            ps.setInt(2, hashtag.getHashtagCount());

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

    public static ArrayList<Twit> selectTwitsWithHashtags(Hashtag hashtag) {

        ArrayList<Twit> twits = new ArrayList<>();

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        String query = "select * from twitterdb.vwTwit, twitterdb.tweetHashtag\n"
                + "where tweetHashtag.hashtagID = ? and tweetHashtag.twitID=vwTwit.twitId";
        PreparedStatement ps = null;

        try {
            
            
            ps = connection.prepareStatement(query);
            ps.setInt(1, hashtag.getHashtagID());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Twit t = new Twit();
                t.setTwitId(rs.getInt("twitId"));
                t.setTwitMessage(rs.getString("twitMessage"));
                t.setTwitDate(rs.getString("twitDate"));
                t.setOwnerFullname(rs.getString("fullname"));
                t.setOwnerUsername(rs.getString("username"));
                t.setOwnerPicture(rs.getString("profilePicture"));
                twits.add(t);
            }

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }

        return twits;
    }

    public static int insertTwitHashtag(Hashtag hashtag, Twit twit) throws IOException {

        hashtag = search(hashtag.getHashtagText());

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        PreparedStatement ps = null;
        // # hashtagID, hashtagText, hashtagCount

        String preparedSQL = "INSERT INTO twitterdb.tweetHashtag(twitID, hashtagID) VALUES (?,?)";

        int flag = 0;
        try {
            ps = connection.prepareStatement(preparedSQL);
            ps.setInt(1, twit.getTwitId());
            ps.setInt(2, hashtag.getHashtagID());

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

    public static ArrayList<Hashtag> selectHashtags() {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        String query = "Select * from twitterdb.hashtag";
        PreparedStatement ps = null;
        ArrayList<Hashtag> hashtags = new ArrayList<>();

        try {
            ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Hashtag h = new Hashtag();
                h.setHashtagID(rs.getInt("hashtagID"));
                h.setHashtagText(rs.getString("hashtagText"));
                h.setHashtagCount(rs.getInt("hashtagCount"));

                hashtags.add(h);

            }

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return hashtags;
    }

    public static Hashtag search(String message) {

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        PreparedStatement ps = null;

        String query = "SELECT * from twitterdb.hashtag WHERE hashtagText=?";
        Hashtag hashtag = null;
        ResultSet rs = null;
        try {

            ps = connection.prepareStatement(query);
            ps.setString(1, message);
            rs = ps.executeQuery();

            if (rs.next()) {

                hashtag = new Hashtag();
                hashtag.setHashtagID(rs.getInt("hashtagID"));
                hashtag.setHashtagText(rs.getString("hashtagText"));
                hashtag.setHashtagCount(rs.getInt("hashtagCount"));
            }

        } catch (SQLException e) {
            System.out.println(e);

        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }

        return hashtag;
    } // end of method search

    public static boolean updateCount(Hashtag hashtag) {
        boolean flag = false;

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        PreparedStatement ps = null;
        String preparedSQL = "UPDATE twitterdb.hashtag SET "
                + "hashtagCount=? WHERE hashtagID=?";

        try {
            ps = connection.prepareStatement(preparedSQL);
            ps.setInt(1, hashtag.getHashtagCount());
            ps.setInt(2, hashtag.getHashtagID());

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
    
    
     // select 10 topics with the most count
     public static ArrayList<String> selectTrends() {

        ArrayList<String> trends = new ArrayList<>();

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        
        String query = "select hashtagText from twitterdb.hashtag order by hashtagCount desc limit 10";
        PreparedStatement ps = null;

        try {
            
            
            ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String trend = rs.getString("hashtagText");
                trends.add(trend);
            }

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }

        return trends;
    }

}
