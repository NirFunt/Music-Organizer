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
import uiPackage.NewSongController;
import uiPackage.UpdateValueController;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SongsHandler extends ObjectHandlerAbstract {

    private ObjectHandlerAbstract artistsHandler = new ArtistsHandler();

    public void get (TableView artistTable, TableView albumTable,
                          TableView songTable, TableView saatTable,
                          ProgressBar progressBar) {

        List<Song> songs = new ArrayList<>();
        progressBar.setVisible(true);

        Dialog<ButtonType> dialog = new Dialog<>();
        int songsCount = DataBase.getInstance().countSongsMethod();
        dialog.setHeaderText("There are " + songsCount + " of songs, please pick starting song");
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

        UpdateValueController updateValueController = fxmlLoader.getController();
        String updatedValue = updateValueController.receiveData();
        if (updatedValue.equals("")) {
            updatedValue ="0";
        }
        int updatedValueInt = Integer.parseInt(updatedValue);

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {

                return DataBase.getInstance().queryMethod ("songs","title"," limit " +
                        updatedValueInt +",500");
            }
        };
        task.setOnSucceeded(e-> {
            artistTable.setVisible(false); songTable.setVisible(true); albumTable.setVisible(false); saatTable.setVisible(false);
            progressBar.setVisible(false);
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


    public void update (GridPane mainWindow, TableView songTable) {
        Song song = (Song) songTable.getSelectionModel().getSelectedItem();
        if (song == null) {
            System.out.println("Song was not selected");
            return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainWindow.getScene().getWindow());
        dialog.setHeaderText("Please pick new song name");
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

        if (result.isPresent() && result.get()==ButtonType.OK) {
            UpdateValueController updateValueController = fxmlLoader.getController();
            String updatedValue = updateValueController.receiveData();
            Task task = new Task() {
                @Override
                protected Object call() throws Exception {
                    return DataBase.getInstance().updateNameMethod("songs","title",song.getId(),updatedValue);
                }
            };
            new Thread(task).start();
            task.setOnSucceeded(e -> {
                song.setTitle(updatedValue);
                songTable.refresh();
            });
        }
    }



    public void delete (TableView songTable) {
        Song song = (Song) songTable.getSelectionModel().getSelectedItem();
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                return DataBase.getInstance().deleteMethod("songs",song.getId());
            }
        };
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Delete Song");
        alert.setContentText("Are you sure you want to delete " + song.getTitle() + " ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            new Thread (task).start();
        }
        task.setOnSucceeded(e-> {
            songTable.getItems().remove(song);
            songTable.refresh();
        });
    }

    @Override
    public void newSong(GridPane mainWindow, TableView<Artist> artistTable, TableView<Album> albumTable, TableView<Song> songTable, TableView<SongAlbumArtistTrack> saatTable, ProgressBar progressBar) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainWindow.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/uiPackage/NewSongDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Could not load new song dialog");
            e.printStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);

        NewSongController newSongController = fxmlLoader.getController();

        Task task = new Task() {
            @Override
            protected Object call() {
                while (isCancelled() == false) {
                    if (newSongController.areTextFieldsNotEmpty()) {
                        dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
                    } else {
                        dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                    }
                }
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.start();

        Optional<ButtonType> result = dialog.showAndWait();
        task.cancel();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            SongAlbumArtistTrack songAlbumArtistTrack = newSongController.receiveData();

            new Thread(new Task() {
                @Override
                protected Object call() throws Exception {
                    DataBase.getInstance().insertSongMethod(songAlbumArtistTrack.getSongName(), songAlbumArtistTrack.getArtistName()
                            , songAlbumArtistTrack.getAlbumName(), songAlbumArtistTrack.getTrackNumber());
                    return null;
                }
            }).start();

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                System.out.println("could not sleep");
            }

            artistsHandler.get(artistTable,albumTable,songTable,saatTable,progressBar);

        }
    }


}
