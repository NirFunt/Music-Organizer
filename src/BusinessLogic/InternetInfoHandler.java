package BusinessLogic;

import DataModel.Artist;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import uiPackage.InternetInfoController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class InternetInfoHandler {


    public void getWebInfo (TableView table, GridPane mainWindow, IInternetInfoHandler iInternetInfoHandler) {
    String resolve = iInternetInfoHandler.getResolve(table);
    String title = iInternetInfoHandler.getDialogTitle();
    String address = iInternetInfoHandler.getAddress();

            try {
                URI wikipediaUri = new URI(address);
                URI bandUri = wikipediaUri.resolve(resolve);
                URL bandURL = bandUri.toURL();

                HttpURLConnection connection = (HttpURLConnection) bandURL.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Chrome");
                connection.setConnectTimeout(2000);
                connection.setReadTimeout(2000);

                int status = connection.getResponseCode();
                if (status > 299) {
                    System.out.println("Error loading web page");
                    return;
                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder sb = new StringBuilder();
                while (line != null) {
                    line = bufferedReader.readLine();
                    sb.append(line);
                }

                String computedString = iInternetInfoHandler.computeString(sb.toString());

                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.initOwner(mainWindow.getScene().getWindow());
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/uiPackage/InternetInfo.fxml"));
                try {
                    dialog.getDialogPane().setContent(fxmlLoader.load());
                } catch (IOException e) {
                    System.out.println("could not load information dialog");
                }
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                InternetInfoController internetInfoController = fxmlLoader.getController();
                internetInfoController.setTitle(title);
                internetInfoController.getInfo(computedString);
                dialog.showAndWait();

            } catch (URISyntaxException | MalformedURLException e) {
                System.out.println("error opening url " + e.getMessage());
            } catch (IOException e) {
                System.out.println("File Not Found " + e.getMessage());
            }
        }

    public void openBandWikipedia (TableView<Artist> artistTable) {
        String artistName = null;
        String artistNameNoSpace = null;
        Artist artist = artistTable.getSelectionModel().getSelectedItem();
        if (artist != null) {
            artistName = artist.getName();
            artistNameNoSpace = artistName.replaceAll(" ", "_");
            try {
                URI wikipediaUri = new URI("https://en.wikipedia.org/wiki/");
                URI bandUri = wikipediaUri.resolve(artistNameNoSpace);
                java.awt.Desktop.getDesktop().browse(bandUri);

            } catch (URISyntaxException | MalformedURLException e) {
                System.out.println("error opening url " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    }

