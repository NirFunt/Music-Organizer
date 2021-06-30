package DataModel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Song {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty track;
    private SimpleStringProperty title;
    private SimpleIntegerProperty albumId;

    public Song (int id, int track, String title, int albumID) {
        this.id = new SimpleIntegerProperty(id);
        this.track = new SimpleIntegerProperty(track);
        this.title = new SimpleStringProperty(title);
        this.albumId = new SimpleIntegerProperty(albumID);
    }

    public int getId() {
        return id.get();
    }

    public int getTrack() {
        return track.get();
    }

    public String getTitle() {
        return title.get();
    }

    public int getAlbumId() {
        return albumId.get();
    }

    public void setTitle (String title) {
        this.title.set(title);
    }

}
