package uiPackage;
import DataModel.SongAlbumArtistTrack;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class NewSongController {

    @FXML
    private  TextField songName;
    @FXML
    private  TextField albumName;
    @FXML
    private  TextField artistName;
    @FXML
    private  TextField trackNumber;

    public SongAlbumArtistTrack receiveData() {
        int track;
     try {
             track = Integer.parseInt(trackNumber.getText());
  } catch (NumberFormatException e) {
         track = 0;
     }
        SongAlbumArtistTrack songAlbumArtistTrack = new SongAlbumArtistTrack(songName.getText(),albumName.getText(),artistName.getText(),track);
        return songAlbumArtistTrack;
    }

    public Boolean areTextFieldsNotEmpty() {
        if ( (songName.getText().isEmpty()) == false && (artistName.getText().isEmpty()) == false
        && (albumName.getText().isEmpty() == false) && trackNumber.getText().isEmpty() == false ) {
            return true;
        }
        return false;
    }

}
