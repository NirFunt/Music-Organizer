package BusinessLogic;

import DataModel.Album;
import DataModel.Artist;
import DataModel.Song;
import DataModel.SongAlbumArtistTrack;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Map;

public class InitializeHandler {

    private ObjectHandlerAbstract artistHandler = new ArtistsHandler();
    private ObjectHandlerAbstract albumsHandler = new AlbumsHandler();
    private ObjectHandlerAbstract songsHandler = new SongsHandler();
    private ViewHandler viewHandler = new ViewHandler();

    private PlayListHandler playListHandler = new PlayListHandler();

    private IInternetInfoHandler artistInfoProcessor = new ArtistInfoProcessor();
    private IInternetInfoHandler songLyricsProccecor = new SongLyricsProcessor();
    private InternetInfoHandler internetInfoHandler = new InternetInfoHandler();

    public void initialize(TableView<Artist> artistTable, TableView<Album> albumTable, TableView<Song> songTable,
                           TableView<SongAlbumArtistTrack> saatTable, TableView<SongAlbumArtistTrack> playListTable
                           , ListView<String> playListListView, ProgressBar progressBar, Map<String,
                            List<SongAlbumArtistTrack>> playListsMap, GridPane mainWindow) {

        ContextMenu contextMenuArtist = new ContextMenu();
        MenuItem listAlbums = new MenuItem("List Albums");
        MenuItem deleteArtist = new MenuItem("Delete");
        MenuItem editArtist = new MenuItem ("Edit");
        MenuItem artistInfo = new MenuItem("Show Information");
        MenuItem artistInfoWikipedia = new MenuItem("Wikipedia");

        listAlbums.setOnAction(actionEvent -> artistHandler.listAlbumsForArtist(artistTable,albumTable,songTable,saatTable,progressBar));
        deleteArtist.setOnAction(actionEvent -> artistHandler.delete(artistTable));
        editArtist.setOnAction(actionEvent -> artistHandler.update(mainWindow,artistTable));
        artistInfo.setOnAction(actionEvent -> internetInfoHandler.getWebInfo(artistTable,mainWindow, artistInfoProcessor));
        artistInfoWikipedia.setOnAction(actionEvent -> internetInfoHandler.openBandWikipedia(artistTable));

        contextMenuArtist.getItems().add(listAlbums);
        contextMenuArtist.getItems().add(editArtist);
        contextMenuArtist.getItems().add(deleteArtist);
        contextMenuArtist.getItems().add(artistInfo);
        contextMenuArtist.getItems().add(artistInfoWikipedia);
        artistTable.setContextMenu(contextMenuArtist);


        ContextMenu contextMenuAlbum = new ContextMenu();
        MenuItem listSongs = new MenuItem("List Songs");
        MenuItem deleteAlbum = new MenuItem("Delete");
        MenuItem editAlbum = new MenuItem ("Edit");

        listSongs.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                albumsHandler.listSongsForAlbum(artistTable,albumTable,songTable,saatTable,progressBar);
            }
        });

        deleteAlbum.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                albumsHandler.delete(albumTable);
            }
        });

        editAlbum.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                albumsHandler.update(mainWindow,albumTable);
            }
        });
        contextMenuAlbum.getItems().add(listSongs);
        contextMenuAlbum.getItems().add(editAlbum);
        contextMenuAlbum.getItems().add(deleteAlbum);
        albumTable.setContextMenu(contextMenuAlbum);


        ContextMenu contextMenuSong = new ContextMenu();
        MenuItem deleteSong = new MenuItem("Delete");
        MenuItem editSong = new MenuItem ("Edit");

        deleteSong.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                songsHandler.delete(songTable);
            }
        });

        editSong.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                songsHandler.update(mainWindow,songTable);
            }
        });
        contextMenuSong.getItems().add(editSong);
        contextMenuSong.getItems().add(deleteSong);
        songTable.setContextMenu(contextMenuSong);


        ContextMenu contextMenuSAAT = new ContextMenu();
        MenuItem addToPlayList = new MenuItem("Add to Playlist");
        MenuItem playYoutube = new MenuItem("Play on Youtube");
        MenuItem showLyrics = new MenuItem("Show Song Lyrics");
        addToPlayList.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                playListHandler.addToPlayList(saatTable,playListListView,playListTable,playListsMap);
            }
        });
        playYoutube.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                viewHandler.playSongYoutube(saatTable);
            }
        });
        showLyrics.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                internetInfoHandler.getWebInfo(saatTable,mainWindow,songLyricsProccecor);
            }
        });
        contextMenuSAAT.getItems().add(addToPlayList);
        contextMenuSAAT.getItems().add(playYoutube);
        contextMenuSAAT.getItems().add(showLyrics);
        saatTable.setContextMenu(contextMenuSAAT);


        ContextMenu contextMenuPlayListsView = new ContextMenu();
        MenuItem deleteList = new MenuItem("Delete");
        deleteList.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                playListHandler.deletePlayList(playListListView,playListsMap,playListTable);
            }
        });
        contextMenuPlayListsView.getItems().add(deleteList);
        playListListView.setContextMenu(contextMenuPlayListsView);


        ContextMenu contextMenuPlayListSongs = new ContextMenu();
        MenuItem removePlayListSong = new MenuItem("Remove");
        removePlayListSong.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                playListHandler.removeSongFromPlayList(playListListView,playListTable,playListsMap);
            }
        });
        contextMenuPlayListSongs.getItems().add(removePlayListSong);
        playListTable.setContextMenu(contextMenuPlayListSongs);


        playListListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                playListTable.getItems().clear();
                String listName = playListListView.getSelectionModel().getSelectedItem();
                List<SongAlbumArtistTrack> listOfsaat = playListsMap.get(listName);
                if (listOfsaat != null) {
                    playListTable.getItems().addAll(listOfsaat);
                }
            }
        });
    }

}
