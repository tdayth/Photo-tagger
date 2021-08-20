package scene;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class TagsController extends SceneController implements Initializable {
    @FXML
    Button applyChanges;
    @FXML
    Button cancelButton;
    @FXML
    ImageView imgView;
    @FXML
    TextField text;
    @FXML
    ListView<String> listView;
    @FXML
    Button removeBtn;
    @FXML
    public Button originalBtn;

    /* Keeps track if the apply button is selected, so when it is selected, we can refresh the TilePane. */
    static Boolean pressed = false;


    /**
     * Initializes TagsController.
     *
     * @param url URL
     * @param rb  Resources
     */
    public void initialize(URL url, ResourceBundle rb) {

        displayListView();
        text.clear();
        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    text.appendText(listView.getSelectionModel().getSelectedItem());
                    applyChanges.setDisable(false);
                    removeBtn.setDisable(false);
                } catch (Exception e) {
                    System.out.println("No items");
                }
            }
        });

        File selectedFile = new File(SceneController.selectedImagePath);
        try {
            BufferedImage bufferedImage = ImageIO.read(selectedFile);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imgView.setImage(image);
        } catch (IOException ex) {
            System.out.println("No image to display");
        }
        text.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                int n = 0;

                if (event.getCode() == KeyCode.BACK_SPACE) {
                    n = 2;
                }
                if (text.getText().length() - n + 1 > 0) {
                    applyChanges.setDisable(false);
                } else {
                    applyChanges.setDisable(true);
                }
            }
        });
    }

    /**
     * Displays previous names in the list.
     */
    private void displayListView() {
        listView.setItems(TagManager.allTags());
    }


    /**
     * If the user selects a tag, then clicks the remove button then the tag will be removed from the list.
     */
    @FXML
    public void removeTags() {
        TagManager.removeTag(listView.getSelectionModel().getSelectedItem(), text);
        removeBtn.setDisable(true);
        applyChanges.setDisable(true);
        displayListView();
    }

    /**
     * Closes the modify tag interface when clicked.
     */
    @FXML
    public void cancelTags() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * When selected, this method edits the name of the file to the tags entered, beginning with @ for each name.
     * This also updates the log with new name, current name, and date.
     */
    @FXML
    public void applyChanges() {
        boolean renamed = PhotoManager.reName(text);
        if (renamed) {
            Stage stage = (Stage) applyChanges.getScene().getWindow();
            stage.close();
            pressed = true;
        }

    }
    @FXML
    public void originalName() {
        boolean renamed = PhotoManager.original(TagManager.originalName);
        if (renamed) {
            Stage stage = (Stage) applyChanges.getScene().getWindow();
            stage.close();
            pressed = true;
        }
    }
}
