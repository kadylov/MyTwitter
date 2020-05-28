/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataaccess;

import business.Twit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TwitDB {
    
    public static int insert(Twit twit) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        
        PreparedStatement ps = null;
        
        String query = "insert into twitterdb.twits(twitMessage, twitDate, twitOwner) values(?, ?, ?)";
        int result = 0;
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, twit.getTwitMessage());
            ps.setString(2, twit.getTwitDate());
            ps.setInt(3, twit.getOwnerID());
            
            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return result;
        
    }
    
    public static int update(Twit twit) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        
        PreparedStatement ps = null;
        
        String preparedSQL = "UPDATE twitterdb.twits SET "
                + "twitId=?, "
                + "twitMessage=?, "
                + "twitDate=?, "
                + "twitOwner=? "
                + "WHERE twitId=?";
        int result = 0;
        try {
            ps = connection.prepareStatement(preparedSQL);
            ps.setInt(1, twit.getTwitId());
            ps.setString(2, twit.getTwitMessage());
            ps.setString(3, twit.getTwitDate());
            ps.setInt(4, twit.getOwnerID());
            ps.setInt(5, twit.getTwitId());
            
            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return result;
        
    }
    
    public static int delete(int twitID) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        
        PreparedStatement ps = null;
        
        String query = "DELETE FROM twitterdb.twits WHERE twitId=?";
        int result = 0;
        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, twitID);
            
            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return result;
        
    }
    
    public static int countTwit(String username) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        
        String query = "Select count(username) as numb from twitterdb.vwTwit where username = ?";
        PreparedStatement ps = null;
        
        int count = 0;
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                
                count = rs.getInt("numb");
                return count;
            }
            
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return count;
    }
    
    public static Twit selectTwit(int twitOwner) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        
        String query = "Select * from twitterdb.twits where twitOwner = ?";
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1, twitOwner);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                
                Twit twit = new Twit();
                twit.setTwitId(rs.getInt("twitId"));
                twit.setTwitMessage(rs.getString("twitMessage"));
                twit.setTwitDate(rs.getString("twitDate"));
                twit.setOwnerID(rs.getInt("twitOwner"));
                
                return twit;
            }
            
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return null;
    }
    
    public static Twit searchTwit(Twit twit) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        
        String query = "Select * from twitterdb.twits where twitMessage = ?";
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, twit.getTwitMessage());
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                
                Twit t = new Twit();
                t.setTwitId(rs.getInt("twitId"));
                t.setTwitMessage(rs.getString("twitMessage"));
                t.setTwitDate(rs.getString("twitDate"));
                t.setOwnerID(rs.getInt("twitOwner"));
                
                return t;
            }
            
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return null;
    }
    
    public static ArrayList<Twit> selectTwitsByUsername(String username) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        
        String query = "Select * from twitterdb.vwTwit where username=?";
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            ArrayList<Twit> twitList = new ArrayList<Twit>();

            /*username	varchar(45)
                fullname	varchar(45)
                twitMessage	varchar(280)
                twitDate	varchar(45)
                twitOwner	int(11)*/
            while (rs.next()) {
                Twit t = new Twit();
                t.setTwitId(rs.getInt("twitId"));
                t.setTwitMessage(rs.getString("twitMessage"));
                t.setTwitDate(rs.getString("twitDate"));
                
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
    
    public static ArrayList<Twit> selectTwitsByUserID(int userId) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        
        String query = "Select * from twitterdb.twits where twitOwner=? order by twits.twitDate desc";
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
                t.setOwnerID(userId);
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
    
    public static Twit selectTwitByDate(String date) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        
        String query = "Select * from twitterdb.twits where twitDate = ?";
        PreparedStatement ps = null;
        
        try {
            ps = connection.prepareStatement(query);
            ps.setString(1, date);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                
                Twit twit = new Twit();
                twit.setTwitId(rs.getInt("twitId"));
                twit.setTwitMessage(rs.getString("twitMessage"));
                twit.setTwitDate(rs.getString("twitDate"));
                twit.setOwnerID(rs.getInt("twitOwner"));
                
                return twit;
            }
            
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return null;
    }
}
