package BusinessLogic;

import DataModel.SongAlbumArtistTrack;
import dbPackage.PlayListsFile;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import uiPackage.UpdateValueController;

import java.io.IOException;
import java.util.*;


public class PlayListHandler {

    public void addToPlayList (TableView<SongAlbumArtistTrack> saatTable, ListView <String> playListListView
    , TableView<SongAlbumArtistTrack> playListTable, Map<String, List<SongAlbumArtistTrack>> playListsMap) {

        SongAlbumArtistTrack songAlbumArtistTrack = saatTable.getSelectionModel().getSelectedItem();
        String selectedListName = playListListView.getSelectionModel().getSelectedItem();
        playListsMap.get(selectedListName).add(songAlbumArtistTrack);
        playListTable.getItems().add(songAlbumArtistTrack);
    }

    public void savePlayList (Map<String,List<SongAlbumArtistTrack>> playListsMap) {
        PlayListsFile.getInstance().savePlayLists(playListsMap);
    }

    public Map<String,List<SongAlbumArtistTrack>> loadPlayList (ListView<String> playListListView,
                                                                TableView<SongAlbumArtistTrack> playListTable) {
        Map<String,List<SongAlbumArtistTrack>> playListsMap;
        playListListView.getItems().clear();
        playListsMap = PlayListsFile.getInstance().loadPlayLists();
        List <String> playListNames = new ArrayList<>();
        for (String s : playListsMap.keySet()) {
            playListNames.add(s);
        }
        playListListView.getItems().addAll(playListNames);
        playListListView.getItems().sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        playListListView.getSelectionModel().selectFirst();
        String listName = playListListView.getSelectionModel().getSelectedItem();
        List <SongAlbumArtistTrack> listOfsaat = playListsMap.get(listName);
        if (listOfsaat != null) {
            playListTable.getItems().addAll(listOfsaat);
        }
    return playListsMap;
    }

    public void addPlayList (GridPane mainWindow, ListView<String> playListListView,
                             Map<String,List<SongAlbumArtistTrack>> playListsMap) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainWindow.getScene().getWindow());
        dialog.setHeaderText("Please pick playlist name");
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
            String playListName = updateValueController.receiveData();

            List <String> playListsNames = playListListView.getItems();
            if (playListsNames.contains(playListName)) {
                System.out.println("playlist name already used, please pick new playlist name");
                return;
            }
            playListListView.getItems().add(playListName);
            List <SongAlbumArtistTrack> list = new ArrayList<>();
            playListsMap.put(playListName, list);

        }
    }

    public void deletePlayList (ListView<String> playListListView, Map<String,List<SongAlbumArtistTrack>> playListsMap,
                                TableView<SongAlbumArtistTrack> playListTable) {
        String selectedList = playListListView.getSelectionModel().getSelectedItem();
        playListsMap.remove(selectedList);
        playListListView.getItems().remove(selectedList);

        playListListView.getSelectionModel().selectFirst();
        String listName = playListListView.getSelectionModel().getSelectedItem();
        List <SongAlbumArtistTrack> listOfsaat = playListsMap.get(listName);
        if (listOfsaat != null) {
            playListTable.getItems().addAll(listOfsaat);
        }
    }

public void removeSongFromPlayList (ListView<String> playListListView, TableView<SongAlbumArtistTrack> playListTable,
                                    Map<String,List<SongAlbumArtistTrack>> playListsMap) {
    String playListName = playListListView.getSelectionModel().getSelectedItem();
    playListsMap.get(playListName).remove(playListTable.getSelectionModel().getSelectedItem());
    playListTable.getItems().remove(playListTable.getSelectionModel().getSelectedItem());
}

}
