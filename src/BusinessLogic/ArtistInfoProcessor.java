package BusinessLogic;

import DataModel.Artist;

import javafx.scene.control.TableView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArtistInfoProcessor implements IInternetInfoHandler {

    @Override
    public String getDialogTitle() {
        return "Information on Artist";
    }

    @Override
    public String getAddress() {
        return "https://en.wikipedia.org/wiki/";
    }

    @Override
    public String getResolve(TableView tableView) {
        String artistName = null;
        String artistNameNoSpace = null;
        Artist artist = (Artist) tableView.getSelectionModel().getSelectedItem();
        if (artist != null) {
            artistName = artist.getName();
             artistNameNoSpace = artistName.replaceAll(" ", "_");
        }
        return artistNameNoSpace;
    }

    @Override
    public String computeString(String rawString) {

        Pattern pattern = Pattern.compile("(</table>.*?</p><div)");
        Matcher matcher = pattern.matcher(rawString);
        matcher.find();
        String bandIntroduction = matcher.group(1);
        String bandIntroductionEdited;
        String bandIntroductionEdited2;
        String bandIntroductionEdited3;
        String bandIntroductionEdited4;
        String bandIntroductionEdited5;

        bandIntroductionEdited = bandIntroduction.replaceAll("\\<.*?\\>", "");
        bandIntroductionEdited2 = bandIntroductionEdited.replaceAll("&#[0-9]{2};[0-9]{2}&#[0-9]{2};", "");
        bandIntroductionEdited3 = bandIntroductionEdited2.replaceAll("&#[0-9]{2};[0-9]{1}&#[0-9]{2};", "");
        bandIntroductionEdited4 = bandIntroductionEdited3.replaceAll("<div", "");
        bandIntroductionEdited5 = bandIntroductionEdited4.replaceAll("&#[0-9]+;", "");
        return bandIntroductionEdited5;
    }

}
