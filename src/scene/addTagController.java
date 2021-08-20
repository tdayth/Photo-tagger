package scene;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


public class addTagController implements Initializable {

    @FXML
    Button cancelButton;
    @FXML
    TextField text;
    @FXML
    Button addBtn;
    @FXML
    ListView<String> addedTags;
    @FXML
    Button removeBtn;

    /**
     * Initializes the addTagController.
     *
     * @param location  the location of the URL
     * @param resources the resources of the URL
     */

    public void initialize(URL location, ResourceBundle resources) {

        displayListView();

        addedTags.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    removeBtn.setDisable(false);
                } catch (Exception e) {
                    System.out.println("No items");
                }
            }
        });

        text.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                int n = 0;

                if (event.getCode() == KeyCode.BACK_SPACE) {
                    n = 2;
                }
                if (text.getText().length() - n + 1 > 0) {
                    addBtn.setDisable(false);
                } else {
                    addBtn.setDisable(true);
                }

            }
        });
    }

    /**
     * Displays previous names in the list.
     */
    private void displayListView() {
        addedTags.setItems(TagManager.addedTags());
    }

    /**
     * When the OK button is clicked, it adds the text that was entered in the field to log.txt, which then can
     * be read to add to the previous tag list.
     */
    @FXML
    public void addButton() {
        TagManager.addTag(text);
        displayListView();
        text.clear();
        addBtn.setDisable(true);
    }

    @FXML
    public void removeTags() {
        TagManager.editTagRemove(addedTags.getSelectionModel().getSelectedItem(), text);
        removeBtn.setDisable(true);
        addBtn.setDisable(true);
        displayListView();
    }

    /**
     * Closes the interface.
     */
    @FXML
    public void cancelButton() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
