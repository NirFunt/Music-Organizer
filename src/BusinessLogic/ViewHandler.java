package BusinessLogic;

import DataModel.Album;
import DataModel.Artist;
import DataModel.Song;
import DataModel.SongAlbumArtistTrack;
import YouTubePackage.Search;
import dbPackage.DataBase;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import uiPackage.UpdateValueController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ViewHandler {

    public void view (TableView<Artist> artistTable, TableView<Album> albumTable,
                           TableView<Song> songTable, TableView<SongAlbumArtistTrack> saatTable,
                           ProgressBar progressBar) {
        List<SongAlbumArtistTrack> list = new ArrayList<>();
        progressBar.setVisible(true);
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                return DataBase.getInstance().queryMethod("SAATVIEW","song","");
            }
        };
        task.setOnSucceeded(e-> {
            albumTable.setVisible(false); artistTable.setVisible(false); songTable.setVisible(false); saatTable.setVisible(true);
            progressBar.setVisible(false);
            ResultSet resultSet = (ResultSet) task.getValue();
            try {
                while (resultSet.next()) {
                    SongAlbumArtistTrack songAlbumArtistTrack = new SongAlbumArtistTrack(resultSet.getString(1)
                            , resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4));
                    list.add(songAlbumArtistTrack);
                }
            } catch (SQLException e1) {
                System.out.println("Could not Build View");
            }
            saatTable.getItems().clear();
            saatTable.getItems().addAll(list);
        });
        new Thread(task).start();
    }

    public void playSongYoutube (TableView<SongAlbumArtistTrack> saatTable) {
        SongAlbumArtistTrack songAlbumArtistTrack = saatTable.getSelectionModel().getSelectedItem();
        String songToSearch = songAlbumArtistTrack.getArtistName() + " " + songAlbumArtistTrack.getSongName();
        String videoCode = Search.searchYouTubeSong(songToSearch);

        try {
            URI youTubeSong = new URI("https://www.youtube.com/watch?v=" + videoCode);
            java.awt.Desktop.getDesktop().browse(youTubeSong);

        } catch (URISyntaxException | MalformedURLException e) {
            System.out.println("error opening url " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchSong (GridPane mainWindow, TableView<SongAlbumArtistTrack> saatTable, TableView<Artist> artistTable,
                            TableView<Album> albumTable, TableView<Song> songTable, ProgressBar progressBar) {
        if (saatTable.getItems().isEmpty()) {
            view(artistTable,albumTable,songTable,saatTable,progressBar);
        }
        albumTable.setVisible(false); artistTable.setVisible(false); songTable.setVisible(false); saatTable.setVisible(true);
        SongAlbumArtistTrack song = null;
        List<SongAlbumArtistTrack> list;
        list = saatTable.getItems();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainWindow.getScene().getWindow());
        dialog.setHeaderText("Please enter song name to search");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/uiPackage/UpdateValue.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("could not load update song name dialog");
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            UpdateValueController updateValueController = fxmlLoader.getController();
            String findSong = updateValueController.receiveData();
            for (SongAlbumArtistTrack saat : list) {
                if (saat.getSongName().equals(findSong)) {
                    song = saat;
                }
            }
            saatTable.getSelectionModel().select(song);
            saatTable.scrollTo(song);
        }
    }





}
