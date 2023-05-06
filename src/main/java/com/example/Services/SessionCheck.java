package com.example.Services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class SessionCheck {

    public boolean isValidSession(Session session) throws SQLException {
        
        Connection connection = DBService.getConnection();
        String selecAllSession = "select user_id,"+
        "(case when (now()-last_modified)< 150 then 1 else 0 END ) as validity "+
        "from session where user_id=?";
        PreparedStatement ps = connection.prepareStatement(selecAllSession);
        ps.setInt(1, session.getUserId());
        ResultSet rs = ps.executeQuery(); 

            if (rs.next()) {
                int userId = rs.getInt("user_id");
                int valid = rs.getInt("validity");
                if(valid==1){
                    return true;
                }
                else{
                    //delete invalid session
                    String deleteInvalidSession = "Delete from session where user_id=?";
                    ps = connection.prepareStatement(deleteInvalidSession);
                    ps.setInt(1, session.getUserId());
                    ps.executeUpdate();

                }
               
            }else{
              return false;
            }

            

        return false;
    }
    
}
