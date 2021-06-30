package BusinessLogic;

import DataModel.SongAlbumArtistTrack;

import javafx.scene.control.TableView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SongLyricsProcessor implements IInternetInfoHandler {

    @Override
    public String getDialogTitle() {
        return "Song Lyrics";
    }

    @Override
    public String getAddress() {
        return "https://www.azlyrics.com/lyrics/";
    }

    @Override
    public String getResolve(TableView tableView) {
        String artistName = null;
        String artistNameNoSpace = null;
        String songName = null;
        String songNameNoSpace = null;

        SongAlbumArtistTrack songAlbumArtistTrack = (SongAlbumArtistTrack) tableView.getSelectionModel().getSelectedItem();
        if (songAlbumArtistTrack != null) {
            songName = songAlbumArtistTrack.getSongName().toLowerCase();
            artistName = songAlbumArtistTrack.getArtistName().toLowerCase();
            artistNameNoSpace = artistName.replaceAll(" ", "");
            songNameNoSpace = songName.replaceAll(" ", "");

        }
        return artistNameNoSpace +"/" + songNameNoSpace + ".html";
    }

    @Override
    public String computeString(String rawString) {
        Pattern pattern = Pattern.compile("(<br><br>.*<br><br>)");
        Matcher matcher = pattern.matcher(rawString);
        matcher.find();
        String bandIntroduction = matcher.group(1);
        String bandIntroductionEdited;
        String bandIntroductionEdited2;
        String bandIntroductionEdited3;
        String bandIntroductionEdited4;
        String bandIntroductionEdited5;
        String bandIntroductionEdited6;
        String bandIntroductionEdited7;

        bandIntroductionEdited = bandIntroduction;
        bandIntroductionEdited2 = bandIntroductionEdited.replaceAll("<br>", "\n");
        bandIntroductionEdited3 = bandIntroductionEdited2.replaceAll("<div>.*?-->", "");
        bandIntroductionEdited4 = bandIntroductionEdited3.replaceAll("<i>.*", "");
        bandIntroductionEdited5 = bandIntroductionEdited4.replaceAll("<!--.*", "");
        bandIntroductionEdited6 = bandIntroductionEdited5.replaceAll("&quot;", "");
        bandIntroductionEdited7 = bandIntroductionEdited6.replaceAll("</div>", "");
        return bandIntroductionEdited7;
    }

}
