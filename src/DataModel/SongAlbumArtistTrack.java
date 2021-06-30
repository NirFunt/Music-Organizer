package DataModel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SongAlbumArtistTrack {
    private SimpleStringProperty songName;
    private SimpleStringProperty albumName;
    private SimpleStringProperty artistName;
    private SimpleIntegerProperty trackNumber;

    public SongAlbumArtistTrack(String songName, String albumName, String artistName, int trackNumber) {
        this.songName = new SimpleStringProperty(songName);
        this.artistName = new SimpleStringProperty(artistName);
        this.albumName = new SimpleStringProperty(albumName);
        this.trackNumber = new SimpleIntegerProperty(trackNumber);
    }

    public String getSongName() {
        return songName.get();
    }

    public String getArtistName() {
        return artistName.get();
    }

    public String getAlbumName() {
        return albumName.get();
    }

    public int getTrackNumber() {
        return trackNumber.get();
    }
}
