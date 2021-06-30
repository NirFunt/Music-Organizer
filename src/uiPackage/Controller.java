package uiPackage;

import BusinessLogic.*;
import DataModel.Song;
import DataModel.SongAlbumArtistTrack;
import DataModel.Album;
import DataModel.Artist;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.util.*;

public class Controller {
    //mediator between ui and business logic
    @FXML
    private TableView <Artist> artistTable;
    @FXML
    private TableView <Album> albumTable;
    @FXML
    private TableView <Song> songTable;
    @FXML
    private TableView <SongAlbumArtistTrack> saatTable;
    @FXML
    private TableView <SongAlbumArtistTrack> playListTable;
    @FXML
    private ListView<String> playListListView;
    @FXML
    private GridPane mainWindow;
    @FXML
    private ProgressBar progressBar;

    private  Map<String,List<SongAlbumArtistTrack>> playListsMap = new HashMap<>();

    private ObjectHandlerAbstract artistHandler = new ArtistsHandler();
    private ObjectHandlerAbstract albumsHandler = new AlbumsHandler();
    private ObjectHandlerAbstract songsHandler = new SongsHandler();
    private ViewHandler viewHandler = new ViewHandler();

    private PlayListHandler playListHandler = new PlayListHandler();
    private InitializeHandler initializeHandler = new InitializeHandler();

    public void getArtists() {
        artistHandler.get(artistTable,albumTable,songTable,saatTable,progressBar);
    }

    public void getAlbums() {
        albumsHandler.get(artistTable,albumTable,songTable,saatTable,progressBar);
    }

    public void getSongs() {
        songsHandler.get(artistTable,albumTable,songTable,saatTable,progressBar);
    }

    public void queryView () {
        viewHandler.view(artistTable,albumTable,songTable,saatTable,progressBar);
    }

    public void newSong () {
        songsHandler.newSong(mainWindow,artistTable,albumTable,songTable,saatTable,progressBar);
    }

    public void initialize () {
        loadPlayLists();

initializeHandler.initialize(artistTable,albumTable,songTable,saatTable,playListTable,playListListView,
                            progressBar,playListsMap,mainWindow);
    }

    public void savePlayLists () {
           playListHandler.savePlayList(playListsMap);
        }

    public void loadPlayLists () {
        playListsMap = playListHandler.loadPlayList(playListListView,playListTable);
    }

    public void addPlayList () {
   playListHandler.addPlayList(mainWindow,playListListView,playListsMap);
        }

    public void searchSong () {
        viewHandler.searchSong(mainWindow,saatTable,artistTable,albumTable,songTable,progressBar);
    }

    public void exit () {
        Alert alertType = new Alert(Alert.AlertType.WARNING);
        alertType.setContentText("Do You Want To Exit?");
        alertType.setHeaderText("");
        alertType.getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = alertType.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            Platform.exit();
        }
    }

}