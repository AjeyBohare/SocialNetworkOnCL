package com.example.Services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.example.CommonUtility.MyException;
import com.example.Models.Post;

public class UserService {

    public void shownewsfeed(String sortCriteria) throws SQLException {
        ArrayList<Post> feed = new ArrayList<>(); 
        Connection connection = DBService.getConnection();
        Session session = Session.getInstance();
        String fetchPostsOfFollowingUser = "select * from post p JOIN ( "+
            "select following_user_id from user_has_follower where user_id = ?) as following_list "+
            "ON following_list.following_user_id = p.user_id;";
    
        String scoreBasedPosts = "SELECT * from post order by (upvotes-downvotes) desc";
        String commnetBasedScore = " select * from post p JOIN ( "+
            "select p.id from Post p JOIN reply r ON r.post_id = p.id GROUP by p.id order by count(r.post_id) desc) as post_id_list "+
            " ON p.id = post_id_list.id ORDER by post_id_list.id";

        String timeStampBasedSort = "select * from post order by creation_time desc";

        

        if(sortCriteria.equals("commnetBasedScore")){
            PreparedStatement ps = connection.prepareStatement(commnetBasedScore);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                feed.add(Post.mapResultSetToPost(rs));

            }

             ps = connection.prepareStatement(fetchPostsOfFollowingUser);
             ps.setInt(1,session.getUserId());
             rs = ps.executeQuery();
            while(rs.next()){
                feed.add(Post.mapResultSetToPost(rs));

            }
            ps = connection.prepareStatement(scoreBasedPosts);
           
            rs = ps.executeQuery();
           while(rs.next()){
               feed.add(Post.mapResultSetToPost(rs));

           }
           ps = connection.prepareStatement(timeStampBasedSort);
          
           rs = ps.executeQuery();
          while(rs.next()){
              feed.add(Post.mapResultSetToPost(rs));

          }

        }else  if(sortCriteria.equals("fetchPostsOfFollowingUser")){
            PreparedStatement ps = connection.prepareStatement(fetchPostsOfFollowingUser);
            ps.setInt(1,session.getUserId());
            ResultSet rs = ps.executeQuery();
           while(rs.next()){
               feed.add(Post.mapResultSetToPost(rs));

           }

            ps = connection.prepareStatement(commnetBasedScore);
            rs = ps.executeQuery();
            while(rs.next()){
                feed.add(Post.mapResultSetToPost(rs));

            }

            
            ps = connection.prepareStatement(scoreBasedPosts);
           
            rs = ps.executeQuery();
           while(rs.next()){
               feed.add(Post.mapResultSetToPost(rs));

           }
           ps = connection.prepareStatement(timeStampBasedSort);
          
           rs = ps.executeQuery();
          while(rs.next()){
              feed.add(Post.mapResultSetToPost(rs));

          }

        }else  if(sortCriteria.equals("scoreBasedPosts")){
            PreparedStatement ps = connection.prepareStatement(scoreBasedPosts);
           
            ResultSet rs = ps.executeQuery();
           while(rs.next()){
               feed.add(Post.mapResultSetToPost(rs));

           }

            ps = connection.prepareStatement(commnetBasedScore);
            rs = ps.executeQuery();
            while(rs.next()){
                feed.add(Post.mapResultSetToPost(rs));

            }

            
            ps = connection.prepareStatement(fetchPostsOfFollowingUser);
            ps.setInt(1,session.getUserId());
            rs = ps.executeQuery();
           while(rs.next()){
               feed.add(Post.mapResultSetToPost(rs));

           }
           ps = connection.prepareStatement(timeStampBasedSort);
          
           rs = ps.executeQuery();
          while(rs.next()){
              feed.add(Post.mapResultSetToPost(rs));

          }

        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        java.util.Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        PreparedStatement ps = connection.prepareStatement(fetchPostsOfFollowingUser);
        

        ResultSet rs = ps.executeQuery();
        
        

       


        String updateSessionTime = "update session "+
        "set last_modified = ? WHERE user_id=?";
        PreparedStatement pStmt = connection.prepareStatement(updateSessionTime);
        pStmt.setObject(1, formattedNow);
        pStmt.setInt(2, session.getUserId());

        int updateRow = pStmt.executeUpdate();


    }

    

    public void follow(int userId) throws SQLException, MyException {
        Connection connection = DBService.getConnection();
        Session session = Session.getInstance();

        String fetchUser = "SELECT * FROM user where id= ?";


        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedNow = now.format(formatter);

        java.util.Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

        PreparedStatement ps = connection.prepareStatement(fetchUser);
        ps.setInt(1,userId);

        ResultSet rs = ps.executeQuery();
        
        if(!rs.next()){
       
            System.out.println("User id is not correct");
        }else{

        String selectFollower = "select * from user_has_follower where user_id = ? AND following_user_id = ?";
        PreparedStatement pStmt = connection.prepareStatement(selectFollower);
        pStmt.setInt(1, session.getUserId());
        pStmt.setInt(2, userId);
        ResultSet seachUserResultSet = pStmt.executeQuery();
        if(!seachUserResultSet.next()){
       
        String updateUpvote = "insert user_has_follower(user_id, following_user_id) values(?,?)";
        pStmt = connection.prepareStatement(updateUpvote);
        pStmt.setInt(1, session.getUserId());
        pStmt.setInt(2, userId);
        pStmt.executeUpdate();


        String updateSessionTime = "update session "+
        "set last_modified = ? WHERE user_id=?";
        pStmt = connection.prepareStatement(updateSessionTime);
        pStmt.setObject(1, formattedNow);
        pStmt.setInt(2, session.getUserId());

        int updateRow = pStmt.executeUpdate();

        if(updateRow == 2) throw new MyException("update_session_error");
        }else{
            System.out.println("User already follow this user.");
        }
        }


    

    }

}
