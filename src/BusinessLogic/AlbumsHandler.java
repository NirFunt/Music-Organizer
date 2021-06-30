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

public class AlbumsHandler extends ObjectHandlerAbstract {


    public void get (TableView artistTable, TableView albumTable,
                           TableView songTable, TableView saatTable, ProgressBar progressBar) {
        List <Album> albums = new ArrayList<>();
        progressBar.setVisible(true);

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                return DataBase.getInstance().queryMethod("albums", "name","");
            }
        };

        task.setOnSucceeded(e -> {
            artistTable.setVisible(false); songTable.setVisible(false); albumTable.setVisible(true); saatTable.setVisible(false);
            progressBar.setVisible(false);

            ResultSet resultSet = (ResultSet) task.getValue();

            try {
                while (resultSet.next()) {
                    Album album = new Album(resultSet.getInt(1),resultSet.getString(2),resultSet.getInt(3));
                    albums.add(album);
                }
            } catch (SQLException e2) {
                System.out.println("Could not build albums");
            }
            albumTable.getItems().clear();
            albumTable.getItems().addAll(albums);

        });
        new Thread(task).start();
    }


    public void update (GridPane mainWindow, TableView albumTable) {
        Album album = (Album) albumTable.getSelectionModel().getSelectedItem();
        if (album == null) {
            System.out.println("Album was not selected");
            return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainWindow.getScene().getWindow());
        dialog.setHeaderText("Please pick new album name");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/uiPackage/UpdateValue.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("could not load update album name dialog");
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
                    return DataBase.getInstance().updateNameMethod("albums","name",album.getId(),updatedValue);
                }
            };
            new Thread(task).start();
            task.setOnSucceeded(e -> {
                album.setName(updatedValue);
                albumTable.refresh();
            });
        }
    }





    public void delete (TableView albumTable) {
        Album album = (Album) albumTable.getSelectionModel().getSelectedItem();
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                return DataBase.getInstance().deleteMethod("albums",album.getId());
            }
        };
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.getButtonTypes().add(ButtonType.CANCEL);
        alert.getButtonTypes().add(ButtonType.OK);
        alert.setHeaderText("Delete Album");
        alert.setContentText("Are you sure you want to delete " + album.getName() + " ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            new Thread(task).start();
        }
        task.setOnSucceeded(e -> {
            albumTable.getItems().remove(album);
            albumTable.refresh();
        });
    }


    @Override
    public void listSongsForAlbum(TableView<Artist> artistTable, TableView<Album> albumTable, TableView<Song> songTable,
                                  TableView<SongAlbumArtistTrack> saatTable, ProgressBar progressBar) {
        List<Song> songs = new ArrayList<>();
        progressBar.setVisible(true);
        Album album = albumTable.getSelectionModel().getSelectedItem();
        if (album == null) {
            System.out.println("album was not selected");
        }

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                return DataBase.getInstance().queryRowsByObjectIdMethod(album.getId(),"songs",
                        "album", "track");
            }
        };

        task.setOnSucceeded(e -> {
            albumTable.setVisible(false);artistTable.setVisible(false);songTable.setVisible(true);
            saatTable.setVisible(false);progressBar.setVisible(false);
            ResultSet resultSet = (ResultSet) task.getValue();
            try {
                while (resultSet.next()) {
                    Song song = new Song(resultSet.getInt(1),resultSet.getInt(2),
                            resultSet.getString(3),resultSet.getInt(4));
                    songs.add(song);
                }
            } catch (SQLException e1) {
                System.out.println("Could not build songs");
            }
            songTable.getItems().clear();
            songTable.getItems().addAll(songs);
        });
        new Thread(task).start();
    }


}
