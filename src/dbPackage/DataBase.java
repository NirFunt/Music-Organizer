package dbPackage;

import java.sql.*;

public class DataBase {
    //mediator between database and business logic
private Connection conn;
private Statement queryStatement;
private PreparedStatement queryOneArtistStatement;
private PreparedStatement queryOneAlbumStatement;
private Statement queryRowsByObjectIDStatement;
private PreparedStatement insertIntoArtistsStatement;
private PreparedStatement insertIntoAlbumsStatement;
private PreparedStatement insertIntoSongsStatement;
private Statement updateNameStatement;
private Statement deleteStatement;
private PreparedStatement countSongs;
private static DataBase instance = new DataBase();

private DataBase() {
}

public static DataBase getInstance () {
    return  instance;
}

public boolean open () {
    try {
        conn = DriverManager.getConnection("jdbc:sqlite:C:\\Nir 2021\\Java 2021\\Project 1f\\Music Organizer\\music.db");
        queryStatement = conn.createStatement();
        queryOneArtistStatement = conn.prepareStatement("SELECT _id FROM artists WHERE name = ?");
        queryOneAlbumStatement = conn.prepareStatement("SELECT _id FROM albums WHERE name = ?");
        queryRowsByObjectIDStatement = conn.createStatement();
        insertIntoArtistsStatement = conn.prepareStatement("INSERT INTO artists (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
        insertIntoAlbumsStatement = conn.prepareStatement("INSERT INTO albums (name,artist) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
        insertIntoSongsStatement = conn.prepareStatement("INSERT INTO songs (track,title,album) VALUES (?,?,?)");
        updateNameStatement = conn.createStatement();
        deleteStatement = conn.createStatement();
        countSongs = conn.prepareStatement("Select COUNT (*) FROM songs");
        return true;
    } catch (SQLException e) {
        System.out.println("could not connect to data base file " +e.getMessage());
        e.printStackTrace();
        return false;
    }
}

public void close () {
    try {
        if (queryStatement != null) {
            queryStatement.close();
        }
        if (queryOneArtistStatement != null) {
            queryOneArtistStatement.close();
        }
        if (queryOneAlbumStatement != null) {
            queryOneAlbumStatement.close ();
        }
        if (queryRowsByObjectIDStatement != null) {
            queryRowsByObjectIDStatement.close();
        }
        if (insertIntoArtistsStatement != null) {
            insertIntoArtistsStatement.close();
        }
        if (insertIntoAlbumsStatement != null) {
            insertIntoAlbumsStatement.close();
        }
        if (insertIntoSongsStatement != null) {
            insertIntoSongsStatement.close();
        }
        if (updateNameStatement != null) {
            updateNameStatement.close();
        }
        if (deleteStatement != null) {
            deleteStatement.close();
        }
        if (conn != null) {
            conn.close();
        }
    } catch (SQLException e) {
        System.out.println("could not close connection to data base file " +e.getMessage());
        e.printStackTrace();
    }
}

    public ResultSet queryMethod(String tableName, String column, String addition) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e2){
            System.out.println("Sleep was inturrpted");
        }

        try {
          return queryStatement.executeQuery("SELECT * FROM " + tableName + " ORDER BY " + column +
                  " COLLATE NOCASE ASC " + addition);
        } catch (SQLException e) {
            System.out.println("Could not query " + tableName.substring(0,tableName.length()-1) + e.getMessage());
            return null;
        }
    }

    public ResultSet queryRowsByObjectIdMethod(int ID, String tableName, String column, String orderBy) {
        try {
           return queryRowsByObjectIDStatement.executeQuery("SELECT * FROM " + tableName + " WHERE " +
                   column + " = " + ID + " ORDER BY " + orderBy + " COLLATE NOCASE");
        } catch (SQLException e) {
            System.out.println("could not query Albums by Artist " + e.getMessage());
            return null;
        }
    }

    private int insertArtistMethod (String artistName) throws SQLException {
        queryOneArtistStatement.setString(1,artistName);
        ResultSet result = queryOneArtistStatement.executeQuery();
        if (result.next()) {
            return result.getInt(1);
        } else {
            insertIntoArtistsStatement.setString(1,artistName);
            int affactedRows = insertIntoArtistsStatement.executeUpdate();
            if (affactedRows != 1) {
                throw  new SQLException ("could not add artist");
            }
            ResultSet generatedKey = insertIntoArtistsStatement.getGeneratedKeys();
            if (generatedKey.next()) {
                return generatedKey.getInt(1);
            } else {
                throw new SQLDataException("could not get _id of artist");
            }
        }
    }

    private int insertAlbumMethod (String albumName, int artistID) throws SQLException{
    queryOneAlbumStatement.setString(1,albumName);
    ResultSet result = queryOneAlbumStatement.executeQuery();
    if (result.next()) {
        return result.getInt(1);
    } else {
        insertIntoAlbumsStatement.setString(1,albumName);
        insertIntoAlbumsStatement.setInt(2,artistID);
        int affactedRows = insertIntoAlbumsStatement.executeUpdate();
        if (affactedRows !=1) {
            throw new SQLDataException("could not add album");
        }
ResultSet GeneratedKey = insertIntoAlbumsStatement.getGeneratedKeys();
        if (GeneratedKey.next()) {
            return GeneratedKey.getInt(1);
        } else {
            throw new SQLDataException("could not recieve new album ID");
        }
    }
    }

    public void insertSongMethod (String title, String artist, String album, int track) {
        try {
        conn.setAutoCommit(false);
            int artistID = insertArtistMethod(artist);
            int albumID = insertAlbumMethod(album,artistID);
            insertIntoSongsStatement.setInt(1,track);
            insertIntoSongsStatement.setString(2,title);
            insertIntoSongsStatement.setInt(3,albumID);
            int affactedRows = insertIntoSongsStatement.executeUpdate();
            if (affactedRows == 1) {
                conn.commit();
            } else {
                throw new SQLException("the song insert failed");
            }
        } catch (SQLException e) {
            System.out.println("insert song excepetion " + e.getMessage());
            try {
                System.out.println("performing rollback");
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("could not roll back " + e2.getMessage());
            }
        } finally {
            try {
                System.out.println("setting default commit behavior");
                conn.setAutoCommit(true);
            } catch (SQLException e3) {
                System.out.println("couldn't reset auto commit" + e3.getMessage());
            }
        }
    }

    public boolean updateNameMethod(String tableName,String column, int id, String newName) {
    try {
        int affectedRows = updateNameStatement.executeUpdate("UPDATE " + tableName + " SET " + column + " = " +
                  "'"+ newName + "'" + " WHERE _id = " +id);
        return affectedRows == 1;
    } catch (SQLException e) {
        System.out.println("could not update the " + tableName.substring(0,tableName.length()-1) + " name" + e.getMessage());
        return false;
    }
    }

    public boolean deleteMethod(String tableName, int selectedID) {
    try {
        int affactedRows = deleteStatement.executeUpdate("DELETE FROM " +tableName + " WHERE _id = " +selectedID);
        return (affactedRows == 1);
    } catch (SQLException e) {
        System.out.println("could not delete " +tableName.substring(0,tableName.length()-1) + e.getMessage());
        return false;
    }
    }

    public int countSongsMethod() {
    int result = 0;
    try {
        ResultSet resultSet = countSongs.executeQuery();
        result = resultSet.getInt(1);
    } catch (SQLException e) {
        System.out.println("could not get songs count");
    }
    return result;
    }

}
