package dbPackage;

import DataModel.SongAlbumArtistTrack;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayListsFile {

    static PlayListsFile instance = new PlayListsFile();

    private PlayListsFile () {

    }

    public static PlayListsFile getInstance () {
        return instance;
    }

    public void savePlayLists (Map<String, List<SongAlbumArtistTrack>> playListsMap) {
        DataOutputStream playLists = null;
        try {
                playLists = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("playLists.dat")));
                playLists.writeInt(playListsMap.size());
                for (String s : playListsMap.keySet()) {
                    playLists.writeUTF(s);
                    playLists.writeInt(playListsMap.get(s).size());
                    for (SongAlbumArtistTrack saat : playListsMap.get(s)) {
                        playLists.writeUTF(saat.getSongName());
                        playLists.writeUTF(saat.getAlbumName());
                        playLists.writeUTF(saat.getArtistName());
                        playLists.writeInt(saat.getTrackNumber());
                    }
                }
        } catch (IOException e) {
            System.out.println("could not save playlists to file "  + e.getMessage());
        } finally {
            try {
                playLists.close();
            } catch (IOException e) {
                System.out.println("could not close the file " + e.getMessage());
            }
        }
    }

    public Map <String, List<SongAlbumArtistTrack>> loadPlayLists () {
Map <String, List<SongAlbumArtistTrack>> playListsMap = new HashMap<>();
        DataInputStream playlists = null;
try {
     playlists = new DataInputStream(new BufferedInputStream(new FileInputStream("playLists.dat")));
    int playListsMapSize = playlists.readInt();
    for (int i = 0; i<playListsMapSize; i++) {
        List <SongAlbumArtistTrack> list = new ArrayList<>();
        String playListName = playlists.readUTF();
        int playListSize = playlists.readInt();
        for (int j = 0; j<playListSize; j++ ) {
            SongAlbumArtistTrack saat = new SongAlbumArtistTrack(playlists.readUTF(), playlists.readUTF()
            ,playlists.readUTF(), playlists.readInt());
            list.add(saat);
        }
        playListsMap.put(playListName,list);
    }

} catch (IOException e) {
    System.out.println("could not load the playlists file " + e.getMessage());
} finally {
    try {
        playlists.close();
    } catch (IOException e) {
        System.out.println("could not close the file " + e.getMessage());
    }
}
return playListsMap;
    }

}
