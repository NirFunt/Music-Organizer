package YouTubePackage;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Search {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final long NUMBER_OF_VIDEOS_RETURNED = 1;
    private static YouTube youtube;

    public static String searchYouTubeSong (String song) {

        String videoIDString3 = null;

        try {
            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {}
            }).setApplicationName("youtube-cmdline-search-sample").build();

            List<String> listIdSnippet = new ArrayList<>();
            listIdSnippet.add("id");
            listIdSnippet.add("snippet");

            YouTube.Search.List search = youtube.search().list(listIdSnippet);

            search.setKey("AIzaSyAKwr9TBCz-rA1NOUbslW5YHmLYG2IZXpg");
            search.setQ(song);

            List <String> listVideo = new ArrayList<>();
            listVideo.add("video");

            search.setType(listVideo);

            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
            SearchListResponse searchResponse = search.execute();

            List<SearchResult> searchResultList = searchResponse.getItems();
            ResourceId videoID = searchResultList.get(0).getId();
            String videoIDString = videoID.toString();
            String videoIDString2 = videoIDString.replaceAll("\\{\"kind\":\"youtube#video\",\"videoId\":\"", "");
            videoIDString3 = videoIDString2.replaceAll("\"}", "");

        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return videoIDString3;
    }

}
