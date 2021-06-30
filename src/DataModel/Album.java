package DataModel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Album {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleIntegerProperty artistId;

    public Album (int id, String name, int artistID) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.artistId = new SimpleIntegerProperty(artistID);
    }

    public int getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public int getArtistId() {
        return artistId.get();
    }

    public void setName (String name) {
        this.name.set(name);
    }
}
