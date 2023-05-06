package com.example.Services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.example.CommonUtility.MyException;

public class ReplyService {
    public void reply(int feedId, String replyText,String commentOn) throws SQLException {
        Connection connection = DBService.getConnection();
        Session session = Session.getInstance();

        String insertPost = "INSERT INTO reply(user_id,description,upvotes,downvotes,creation_time,post_id,replyon) values "+
        "(?,?,?,?,?,?,?)";

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        java.util.Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        PreparedStatement ps = connection.prepareStatement(insertPost);
        ps.setInt(1,session.getUserId());
        ps.setString(2, replyText);
        ps.setInt(3, 0);
        ps.setInt(4, 0);
        ps.setObject(5,formattedNow);
        ps.setInt(6,feedId);
        ps.setObject(7, commentOn);


        int rs = ps.executeUpdate();
       

        String updateSessionTime = "update session "+
        "set last_modified = ? WHERE user_id=?";
        ps = connection.prepareStatement(updateSessionTime);
        ps.setObject(1, formattedNow);
        ps.setInt(2, session.getUserId());

        rs = ps.executeUpdate();

        // if(rs == 2) 
        // throw new MyException("update_session_error");

        

    }

    public void replydownvote(int replyId) throws SQLException, MyException {
        Connection connection = DBService.getConnection();
        Session session = Session.getInstance();

        String fetchPost = "SELECT * FROM reply where id= ?";


        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        java.util.Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        PreparedStatement ps = connection.prepareStatement(fetchPost);
        ps.setInt(1,replyId);

        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            int downvotes = rs.getInt("downvotes");
            downvotes++;
           
            String updateUpvote = "update post set downvotes=? where id = ?";
            PreparedStatement pStmt = connection.prepareStatement(updateUpvote);
            pStmt.setInt(1, downvotes<0 ? 0:downvotes);
            pStmt.setInt(2, replyId);
            pStmt.executeUpdate();


            String updateSessionTime = "update session "+
            "set last_modified = ? WHERE user_id=?";
            pStmt = connection.prepareStatement(updateSessionTime);
            pStmt.setObject(1, formattedNow);
            pStmt.setInt(2, session.getUserId());
    
            int updateRow = pStmt.executeUpdate();
    
            if(updateRow == 2) throw new MyException("update_session_error");
        }



        
    }

    public void replyupvote(int replyId) throws SQLException, MyException {
        Connection connection = DBService.getConnection();
        Session session = Session.getInstance();

        String fetchPost = "SELECT * FROM POST where id= ?";


        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        java.util.Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        PreparedStatement ps = connection.prepareStatement(fetchPost);
        ps.setInt(1,replyId);

        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            int upvotes = rs.getInt("upvotes");
            upvotes++;
            String updateUpvote = "update post set upvotes=? where id = ?";
            PreparedStatement pStmt = connection.prepareStatement(updateUpvote);
            pStmt.setInt(1, upvotes);
            pStmt.setInt(2, replyId);
            pStmt.executeUpdate();


            String updateSessionTime = "update session "+
            "set last_modified = ? WHERE user_id=?";
            pStmt = connection.prepareStatement(updateSessionTime);
            pStmt.setObject(1, formattedNow);
            pStmt.setInt(2, session.getUserId());
    
            int updateRow = pStmt.executeUpdate();
    
            if(updateRow == 2) throw new MyException("update_session_error");



        }
       

        
    }

    
}
