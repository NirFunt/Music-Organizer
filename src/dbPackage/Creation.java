package dbPackage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Creation {

// create the database commends should be written here, this class should have psvm to run for one time in order to create
// the desired database with its tables etc..

    private static PreparedStatement createArtistsTable;
    private static PreparedStatement createAlbumsTable;
    private static PreparedStatement createSongsTable;
    private static PreparedStatement createSAATView;

    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:C:\\Nir 2021\\Java 2021\\Project 1b\\Draft 39 - Before Youtube\\music5.db");
        } catch (SQLException e) {
            System.out.println("could not connect to database");
            e.printStackTrace();
        }

        try {
            createArtistsTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS artists(_id INTEGER PRIMARY KEY, name TEXT NOT NULL)");
            createAlbumsTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS albums (_id INTEGER PRIMARY KEY, name TEXT NOT NULL, artist INTEGER)");
            createSongsTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS songs (_id INTEGER PRIMARY KEY, track INTEGER, title TEXT NOT NULL, album INTEGER)");
            createSAATView = conn.prepareStatement("CREATE VIEW IF NOT EXISTS SAATVIEW AS SELECT songs.title AS song," +
                    " albums.name AS album, artists.name AS artist, songs.track AS track FROM songs INNER JOIN albums ON albums._id = songs.album" +
                    " INNER JOIN artists ON artists._id = albums.artist ORDER BY songs.title, albums.name, artists.name COLLATE NOCASE");

            createArtistsTable.execute();
            createAlbumsTable.execute();
            createSongsTable.execute();
            createSAATView.execute();

        } catch (SQLException e) {
            System.out.println("could not create Tables and Views " + e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("could not close connection " + e.getMessage());
            }
        }
    }
}
