package com.example.Services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import com.example.CommonUtility.MyException;
import com.example.Models.User;


public class SigninService {

   

    public void signOut() throws SQLException {
        Connection connection = DBService.getConnection();
        Session session = Session.getInstance();

        String deleteInvalidSession = "Delete from session where user_id=?";
        PreparedStatement ps = connection.prepareStatement(deleteInvalidSession);
        ps.setInt(1, session.getUserId());
        ps.executeUpdate();
        session.setUser(null);
    }

    public void signUp() throws SQLException {
        Connection connection = DBService.getConnection();
        Scanner sc =  new Scanner(System.in);
        System.out.print("User Name: ");
        String username = sc.next();
        System.out.print("User handleId(By default it's username): ");
        String handleId = sc.next();
        System.out.print("Email Id: ");
        String emialId = sc.next();
        System.out.print("Password: ");
        char[] passwd = System.console().readPassword();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        java.util.Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        String newUserId = "";
        String insertUser = "insert into user (name,handleid,emailid,passwd,creation_time) values(?,?,?,?,?); ";

        PreparedStatement ps = connection.prepareStatement(insertUser);
        ps.setString(1, username);
        ps.setString(2, handleId);
        ps.setString(3, emialId);
        ps.setString(4, new String(passwd));
        
        ps.setObject(5,formattedNow);
        ps.executeUpdate();
        

        // Statement stmt = connection.createStatement();
        // ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
        // rs.next();
        // int userid = rs.getInt(1);

        // System.out.println(userid);

        

        // User user = new User();
        // user.setUserId(userid);
        // user.setUsername(username);
        // user.setEmail(emialId);
        // user.setHandleId(handleId);
        // user.setPassword(passwd.toString());
        // user.setCreationTime(date);

        // Session session = Session.getInstance();
        // session.setCurrentUser(user);



    }

    public void signIn() throws SQLException, MyException {
       

        Connection connection = DBService.getConnection();
        Scanner sc =  new Scanner(System.in);
        
        System.out.print("User Email Id: ");
        String emailId = sc.next();
        System.out.print("Password: ");
        char[] passwd = System.console().readPassword();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        java.util.Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        String newUserId = "";
        String insertUser = "select * from user where emailid = ? and passwd = ?; ";

        PreparedStatement ps = connection.prepareStatement(insertUser);
       
        ps.setString(1, emailId);
        ps.setString(2, new String(passwd));
        System.out.println(new String(passwd));

        ResultSet rs = ps.executeQuery();

        

       
        User user  = new User();
        if(rs.next()){
            user.setUserId(rs.getInt("id"));
            user.setEmail(emailId);
            user.setPassword(new String(passwd));
            user.setUsername(rs.getString("name"));
            user.setHandleId(rs.getString("handleid"));
            user.setCreationTime(date);

            String checkUser = "select * from session where user_id = ?";
            String saveSesionInDB  = "Insert into session(user_id,last_modified) values (?,?);";
            ps=connection.prepareStatement(checkUser);
            ps.setInt(1, user.getUserId());
            ResultSet searchSessionResultSet = ps.executeQuery();
            if(searchSessionResultSet.next()){
                String updateQuery = "UPDATE session SET last_modified = ? WHERE user_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setString(1, formattedNow);
                preparedStatement.setInt(2, user.getUserId());
                int rowsUpdated = preparedStatement.executeUpdate();
                
            }else{

            ps = connection.prepareStatement(saveSesionInDB);
            ps.setInt(1, user.getUserId());
            ps.setObject(2, formattedNow);

            ps.executeUpdate();
            }

          }else{
            throw new MyException("invalid_credentials");
        }
        Session session = Session.getInstance();
        if(user.getEmail() != null){
           session.setUser(user);
        }
        
    }
    
}
