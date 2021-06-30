package BusinessLogic;

import DataModel.Album;
import DataModel.Artist;
import DataModel.Song;
import DataModel.SongAlbumArtistTrack;
import dbPackage.DataBase;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import uiPackage.UpdateValueController;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArtistsHandler extends ObjectHandlerAbstract {

    public void get (TableView artistTable, TableView albumTable,
                            TableView songTable, TableView saatTable, ProgressBar progressBar) {
        List<Artist> artists = new ArrayList<>();
        progressBar.setVisible(true);
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                return DataBase.getInstance().queryMethod("artists", "name" ,"");
            }
        };

        task.setOnSucceeded(e -> {
            artistTable.setVisible(true); songTable.setVisible(false); albumTable.setVisible(false); saatTable.setVisible(false);
            progressBar.setVisible(false);

            ResultSet resultSet = (ResultSet) task.getValue();
            try {
                while (resultSet.next()) {

                    Artist artist = new Artist(resultSet.getInt(1), resultSet.getString(2));
                    artists.add(artist);
                }

            } catch (SQLException e3) {
                System.out.println("Could not Bulid Artists");
            }
            artistTable.getItems().clear();
            artistTable.getItems().addAll(artists);
        });
        new Thread(task).start();
    }


    public void update (GridPane mainWindow, TableView artistTable) {
        Artist artist = (Artist) artistTable.getSelectionModel().getSelectedItem();
        if (artist == null) {
            System.out.println("Artist was not selected");
            return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainWindow.getScene().getWindow());
        dialog.setHeaderText("Please pick new artist name");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/uiPackage/UpdateValue.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("could not load update artist name dialog");
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get()==ButtonType.OK) {
            UpdateValueController updateValueController = fxmlLoader.getController();
            String updatedValue = updateValueController.receiveData();
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    return DataBase.getInstance().updateNameMethod("artists","name",artist.getId(),updatedValue);
                }
            };
            new Thread(task).start();
            task.setOnSucceeded(e -> {
                artist.setName(updatedValue);
                artistTable.refresh();
            });
        }
    }


    public void delete (TableView artistTable) {
        Artist artist = (Artist) artistTable.getSelectionModel().getSelectedItem();
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                return DataBase.getInstance().deleteMethod("artists",artist.getId());
            }
        };
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Are you sure you want to delete " + artist.getName() + " ?");
        alert.getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            new Thread(task).start();
        }
        task.setOnSucceeded(e -> {
            artistTable.getItems().remove(artist);
            artistTable.refresh();
        });
    }


    @Override
    public void listAlbumsForArtist(TableView<Artist> artistTable, TableView<Album> albumTable, TableView<Song> songTable,
                                    TableView<SongAlbumArtistTrack> saatTable, ProgressBar progressBar) {

        progressBar.setVisible(true);
        List <Album> albums = new ArrayList<>();
        Artist artist = artistTable.getSelectionModel().getSelectedItem();
        if (artist == null) {
            System.out.println("No Artist Selected, please pick an Artist");
            return;
        }
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                return DataBase.getInstance().queryRowsByObjectIdMethod(artist.getId(),"albums",
                        "artist", "name");
            }
        };
        task.setOnSucceeded(e -> {
            artistTable.setVisible(false); songTable.setVisible(false);albumTable.setVisible(true); saatTable.setVisible(false);
            progressBar.setVisible(false);
            ResultSet results = (ResultSet) task.getValue();
            try {
                while (results.next()) {
                    Album album = new Album(results.getInt(1),results.getString(2),artist.getId());
                    albums.add(album);
                }
            } catch (SQLException e1) {
                System.out.println("Could not build albums");
            }
            albumTable.getItems().clear();
            albumTable.getItems().addAll(albums);
        });
        new Thread(task).start();

    }


}
