package BusinessLogic;

import DataModel.Album;
import DataModel.Artist;
import DataModel.Song;
import DataModel.SongAlbumArtistTrack;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;

public abstract class ObjectHandlerAbstract {

    public abstract void get(TableView tableView1, TableView tableView2, TableView tableView3,
                             TableView tableView4, ProgressBar progressBar);

    abstract void update (GridPane gridPane, TableView tableView);

    abstract void delete (TableView tableView);


    public void newSong(GridPane mainWindow, TableView<Artist> artistTable, TableView<Album> albumTable,
                        TableView<Song> songTable, TableView<SongAlbumArtistTrack> saatTable, ProgressBar progressBar) {

    }

    public void listAlbumsForArtist(TableView<Artist> artistTable, TableView<Album> albumTable, TableView<Song> songTable,
                                    TableView<SongAlbumArtistTrack> saatTable, ProgressBar progressBar) {

    }

    public void listSongsForAlbum(TableView<Artist> artistTable, TableView<Album> albumTable, TableView<Song> songTable,
                                  TableView<SongAlbumArtistTrack> saatTable, ProgressBar progressBar) {

    }


}
