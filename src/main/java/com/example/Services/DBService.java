package com.example.Services;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBService {
    private static final String DB_NAME = "social_network.db";
    private static final String USER_TABLE = "user";
    private static final String FEED_TABLE = "feed";
    private static final String FOLLOW_TABLE = "follow";
    private static final String COMMENT_TABLE = "comment";
    private static final String UPVOTE_TABLE = "upvote";
    private static final String DOWNVOTE_TABLE = "downvote";
    private static Connection connection;

    private DBService() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
           // connection = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
        connection = DriverManager.getConnection("jdbc:mysql://localhost/social_netwrok?user=<username>&password=<passwd>");
        }
        return connection;
    }

    // public static void createTables() throws SQLException {
    //     Statement statement = getConnection().createStatement();
    //     statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + USER_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL UNIQUE, password TEXT NOT NULL)");
    //     statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + FEED_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER NOT NULL, content TEXT NOT NULL, timestamp TEXT NOT NULL, upvotes INTEGER NOT NULL DEFAULT 0, downvotes INTEGER NOT NULL DEFAULT 0)");
    //     statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + FOLLOW_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER NOT NULL, followed_user_id INTEGER NOT NULL)");
    //     statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + COMMENT_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, feed_id INTEGER NOT NULL, user_id INTEGER NOT NULL, content TEXT NOT NULL, timestamp TEXT NOT NULL)");
    //     statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + UPVOTE_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, feed_id INTEGER NOT NULL, user_id INTEGER NOT NULL)");
    //     statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + DOWNVOTE_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, feed_id INTEGER NOT NULL, user_id INTEGER NOT NULL)");
    //     statement.close();
    // }

}
