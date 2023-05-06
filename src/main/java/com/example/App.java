package com.example;

import java.lang.reflect.Member;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.type.MirroredTypeException;

import org.apache.commons.cli.*;

import com.example.Services.DBService;
import com.example.Services.PostService;
import com.example.Services.ReplyService;
import com.example.Services.Session;
import com.example.Services.SessionCheck;
import com.example.Services.SigninService;
import com.example.Services.UserService;
import com.example.Services.replyOn;
import com.example.CommonUtility.MyException;
import com.example.Models.User;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {

      
        //parseArgs(args);

        HashSet<String> menuSet = new HashSet<>();
        menuSet.add("signup");
        menuSet.add("signin");
        menuSet.add("post");
        menuSet.add("follow");
        menuSet.add("reply");
        menuSet.add("upvote");
        menuSet.add("downvote");
        menuSet.add("commentoncommnet");
        menuSet.add("commnetlike");
        menuSet.add("commnetdislike");
        menuSet.add("showsnewsfeed");
        menuSet.add("signout");
        menuSet.add("exit");
        ArrayList<String> params = new ArrayList<>();

       
            SessionCheck sessionCheck = new SessionCheck();
            Session session = Session.getInstance();
            //DBService.createTables();
           
        while(true){
            String command = commandMenu(menuSet,params);
            if(command.equals("exit")){
                return;
            }else{
                
                try{
                    if(command.equals("signup")) {

                    if(session.getUser() != null && sessionCheck.isValidSession(session)){
                        System.out.println("User already Signed in. First Signout to this termimal or start a new session in other terminal.");
                        throw new MyException("User Already Signed in");
                    }else{
                        signup();
                    }
                    System.out.println("Signup command invoked");
                    }else  if(command.equals("signin")) {

                        if(session.getUser() != null && sessionCheck.isValidSession(session)){
                            System.out.println("User already Signed in. First Signout to this termimal or start a new session in other terminal.");
                            throw new MyException("User Already Signed in");
                        }else{
                            signin();
                        }
        
                        System.out.println("Login command invoked");
                    }else  if (command.equals("post")) {
                
                        
                        if(session.getUser() != null && sessionCheck.isValidSession(session) && !params.get(0).isEmpty() && params.get(0) != null){
                            post(params.get(0));
                            
                        }else{
                            System.out.println("Not a valid Session. Please Signin again");
                            //throw new MyException("Invalid Session");
                        }
        
        
                        System.out.println("Post command invoked with post text: " + params.get(0));
                    }else  if (command.equals("upvote")) {
                
                        int feedId = Integer.parseInt(params.get(0));
                        if(session != null && sessionCheck.isValidSession(session)){
                            upvote(feedId);
                            
                        }else{
                            System.out.println("Not a valid Session. Please Signin again");
                            throw new MyException("Invalid Session");
                        }
                        System.out.println("Upvote command invoked for feed id: " + feedId);
                    }
        
                    else if (command.equals("downvote")) {
                       
                        int feedId = Integer.parseInt(params.get(0));
                        if(session != null && sessionCheck.isValidSession(session)){
                            downvote(feedId);
                            
                        }else{
                            System.out.println("Not a valid Session. Please Signin again");
                            throw new MyException("Invalid Session");
                        }
                        System.out.println("Downvote command invoked for feed id: " + feedId);
                    }else if (command.equals("follow")) {
                
                        int userId = Integer.parseInt(params.get(0));
                        if(session != null && sessionCheck.isValidSession(session)){
                            if(userId == session.getUserId()){
                                System.out.println("You can not follow yourself.");
                            }else{
                            follow(userId);
                            }
                            
                        }else{
                            System.out.println("Not a valid Session. Please Signin again");
                            throw new MyException("Invalid Session");
                        }
                        System.out.println("Follow command invoked for user: " + userId);
                    }else if (command.equals("shownewsfeed")) {

                        if(session != null && sessionCheck.isValidSession(session)){
                            shownewsfeed(params.get(0));
                            
                        }else{
                            System.out.println("Not a valid Session. Please Signin again");
                            throw new MyException("Invalid Session");
                        }
                        System.out.println("Show news feed command invoked");
                    }else if (command.equals("reply")) {

                        if(session != null && sessionCheck.isValidSession(session)){
                            reply(Integer.parseInt(params.get(0)),params.get(1));
                            
                        }else{
                            System.out.println("Not a valid Session. Please Signin again");
                            throw new MyException("Invalid Session");
                        }
                        System.out.println("reply on post command invoked");
                    }else if (command.equals("commentoncommnet")) {

                        if(session != null && sessionCheck.isValidSession(session)){
                            replyonreply(Integer.parseInt(params.get(0)),params.get(1));
                            
                        }else{
                            System.out.println("Not a valid Session. Please Signin again");
                            throw new MyException("Invalid Session");
                        }
                        System.out.println("reply on post command invoked");
                    }
                    else if (command.equals("commnetlike")) {

                        if(session != null && sessionCheck.isValidSession(session)){
                            replyupvote(Integer.parseInt(params.get(0)));
                            
                        }else{
                            System.out.println("Not a valid Session. Please Signin again");
                            throw new MyException("Invalid Session");
                        }
                        System.out.println("Upvote on reply command invoked");
                    }else if (command.equals("commnetdislike")) {

                        if(session != null && sessionCheck.isValidSession(session)){
                            replydownvote(Integer.parseInt(params.get(0)));
                            
                        }else{
                            System.out.println("Not a valid Session. Please Signin again");
                            throw new MyException("Invalid Session");
                        }
                        System.out.println("Downvote on reply command invoked");
                    }
                
                } catch (MyException m){
                    System.out.println(m.getMessage());
                } catch(SQLException e){
                    System.out.println("Error in DB"+e);
                }
                finally{
                
                    //destroy session
                    //delete DB
                    

                    
                }
            }
        }

      
    
       

       
       
        
    }


    private static String commandMenu(HashSet menuSet,ArrayList<String> params){
        Scanner sc = new Scanner(System.in);

        String inputCommand=sc.nextLine();

        Iterator<String> menuIterator  = menuSet.iterator();
        
        while(menuIterator.hasNext()){
            String menuOptions = menuIterator.next();
            
            if(inputCommand.contains(menuOptions)  ){
                if(menuOptions.equals("post")){               
                    Pattern pattern = Pattern.compile("(?:-\\s|-)post\\(\"([^\"]*)\"\\)$");
                    Matcher matcher = pattern.matcher(inputCommand);
                    String text;
                    params.clear();
                    if (matcher.matches()) {
                        text = matcher.group(1);
                        params.add(text);
                        //System.out.println("Text: " + text);
                    } else {
                        System.out.println("Input does not match the command pattern.");
                        //throw new MyException("invalid_command_options");
                    }
    
                }else  if(menuOptions.equals("upvote")){               
                    Pattern pattern = Pattern.compile("(?:-\\s|-)upvote\\(\"([^\"]*)\"\\)$");
                    Matcher matcher = pattern.matcher(inputCommand);
                    String postId;
                    params.clear();
                    if (matcher.matches()) {
                        postId = matcher.group(1);
                        params.add(postId);
                        //System.out.println("Text: " + text);
                    } else {
                        System.out.println("Input does not match the command pattern.");
                        //throw new MyException("invalid_command_options");
                    }
    
                }else  if(menuOptions.equals("downvote")){               
                    Pattern pattern = Pattern.compile("(?:-\\s|-)downvote\\(\"([^\"]*)\"\\)$");
                    Matcher matcher = pattern.matcher(inputCommand);
                    String postId;
                    params.clear();
                    if (matcher.matches()) {
                        postId = matcher.group(1);
                        params.add(postId);
                        //System.out.println("Text: " + text);
                    } else {
                        System.out.println("Input does not match the command pattern.");
                        //throw new MyException("invalid_command_options");
                    }
    
                }else  if(menuOptions.equals("follow")){               
                    Pattern pattern = Pattern.compile("(?:-\\s|-)follow\\(\"([^\"]*)\"\\)$");
                    Matcher matcher = pattern.matcher(inputCommand);
                    String param1;
                    params.clear();
                    if (matcher.matches()) {
                        param1 = matcher.group(1);
                        params.add(param1);
                        //System.out.println("Text: " + text);
                    } else {
                        System.out.println("Input does not match the command pattern.");
                        //throw new MyException("invalid_command_options");
                    }
    
                }else  if(menuOptions.equals("showsnewsfeed")){               
                    Pattern pattern = Pattern.compile("(?:-\\s|-)showsnewsfeed\\(\"([^\"]*)\"\\)$");
                    Matcher matcher = pattern.matcher(inputCommand);
                    String param1;
                    params.clear();
                    if (matcher.matches()) {
                        param1 = matcher.group(1);
                        params.add(param1);
                        //System.out.println("Text: " + text);
                    } else {
                        System.out.println("Input does not match the command pattern.");
                        //throw new MyException("invalid_command_options");
                    }
    
                }
                else  if(menuOptions.equals("reply")){               
                    Pattern pattern = Pattern.compile("(?:-\\s|-)reply\\(\"([^\"]*)\",\"([^\"]*)\"\\)$");
                    Matcher matcher = pattern.matcher(inputCommand);
                    String param1;
                    String param2;
                    params.clear();
                    if (matcher.matches()) {
                        param1 = matcher.group(1);
                        param2 = matcher.group(2);
                        params.add(param1);
                        params.add(param2);
                        //System.out.println("Text: " + text);
                    } else {
                        System.out.println("Input does not match the command pattern.");
                        //throw new MyException("invalid_command_options");
                    }
    
                }else  if(menuOptions.equals("commentoncommnet")){               
                    Pattern pattern = Pattern.compile("(?:-\\s|-)commentoncommnet\\(\"([^\"]*)\",\"([^\"]*)\"\\)$");
                    Matcher matcher = pattern.matcher(inputCommand);
                    String param1;
                    String param2;
                    params.clear();
                    if (matcher.matches()) {
                        param1 = matcher.group(1);
                        param2 = matcher.group(2);
                        params.add(param1);
                        params.add(param2);
                        //System.out.println("Text: " + text);
                    } else {
                        System.out.println("Input does not match the command pattern.");
                        //throw new MyException("invalid_command_options");
                    }
    
                }
                else  if(menuOptions.equals("commnetlike")){               
                    Pattern pattern = Pattern.compile("(?:-\\s|-)commnetlike\\(\"([^\"]*)\"\\)$");
                    Matcher matcher = pattern.matcher(inputCommand);
                    String replyId;
                    params.clear();
                    if (matcher.matches()) {
                        replyId = matcher.group(1);
                        params.add(replyId);
                        //System.out.println("Text: " + text);
                    } else {
                        System.out.println("Input does not match the command pattern.");
                        //throw new MyException("invalid_command_options");
                    }
    
                }else  if(menuOptions.equals("commnetdislike")){               
                    Pattern pattern = Pattern.compile("(?:-\\s|-)commnetdislike\\(\"([^\"]*)\"\\)$");
                    Matcher matcher = pattern.matcher(inputCommand);
                    String replyId;
                    params.clear();
                    if (matcher.matches()) {
                        replyId = matcher.group(1);
                        params.add(replyId);
                        //System.out.println("Text: " + text);
                    } else {
                        System.out.println("Input does not match the command pattern.");
                        //throw new MyException("invalid_command_options");
                    }
    
                }


                return menuOptions;
            }
        }
 
        return "invalidCommand";

    }



    

    private static void signout() throws SQLException {
        SigninService signinService = new SigninService();
        signinService.signOut();
    }

    private static void shownewsfeed(String sortCriteria) throws SQLException {
        UserService userService = new UserService();
        userService.shownewsfeed(sortCriteria);
    }

    private static void downvote(int feedId) throws SQLException, MyException {
        PostService postService = new PostService();
        postService.downvote(feedId);
    }

    private static void upvote(int feedId) throws SQLException, MyException {
        PostService postService = new PostService();
        postService.upvote(feedId);
    }

    private static void reply(int feedId, String replyText) throws SQLException {
        ReplyService replyService = new ReplyService();
        replyService.reply(feedId,replyText,"P");
    }

    private static void replydownvote(int feedId) throws SQLException, MyException {
        ReplyService replyService = new ReplyService();
        replyService.replydownvote(feedId);
    }

    private static void replyupvote(int feedId) throws SQLException, MyException {
        ReplyService replyService = new ReplyService();
        replyService.replyupvote(feedId);
    }

    private static void replyonreply(int feedId, String replyText) throws SQLException {
        ReplyService replyService = new ReplyService();
        replyService.reply(feedId,replyText,"C");
    }

    private static void follow(int userHandle) throws SQLException, MyException {
        UserService userService = new UserService();
        userService.follow(userHandle);
    }

    private static void post( String description) throws SQLException, MyException {
        PostService postService = new PostService();
        postService.addPost(description);
    }

    private static void signin() throws SQLException, MyException {
        SigninService signinService = new SigninService();
      
        signinService.signIn();
    }
    

    private static void signup() throws SQLException {
        SigninService signinService = new SigninService();
        signinService.signUp();
    }
    
    
    
}
