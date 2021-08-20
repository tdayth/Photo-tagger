package scene;

import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class SceneController implements Initializable {

    @FXML
    TreeView<String> treeView;
    @FXML
    TilePane tilePane;
    @FXML
    Button modifyBtn;
    @FXML
    Button moveBtn;
    @FXML
    Button chooseDir;
    @FXML
    Label pathOfFile;
    @FXML
    Button openDir;

    /**
     * The path of the selected photo.
     */
    static String selectedImagePath;
    private static String choiceDir;
    private String choice;

    //private Image computerIcon = new Image(getClass().getResourceAsStream("/img/computer.png"));

    public void initialize(URL url, ResourceBundle rb) {
        chooseDir.setOnAction(e -> {
            Stage stage = new Stage();
            DirectoryChooser dc = new DirectoryChooser();
            dc.setInitialDirectory(new File(System.getProperty("user.home")));
            File choice = dc.showDialog(stage);
            if (choice != null) {
                treeView.setRoot(DirectoryManager.getFiles(choice));
                choiceDir = choice.getParent();
                pathOfFile.setText(choice.getPath());
                openDir.setDisable(false);
            }
        });
    }

    /**
     * If the Modify tags button is selected, this method will open a new window where the user can edit the tags.
     */
    @FXML
    public void modifyTagsClicked() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Tags.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Modify tags");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.showAndWait();
            if (TagsController.pressed) {
                displayTilePane(choice);
            }
            modifyBtn.setDisable(true);
            moveBtn.setDisable(true);
        } catch (Exception e) {
            System.out.println("Can't modify");
        }
    }

    /**
     * Moves the selected image to the desired location when the move button is selected.
     */
    public void moveFile() {
        PhotoManager.movePhoto();
        if (PhotoManager.moved) {
            displayTilePane(choice);
        }
        modifyBtn.setDisable(true);
        moveBtn.setDisable(true);
    }

    /**
     * Opens interface with logs of each file name change.
     */
    @FXML
    public void logOfTags() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Log.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Log of tags");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.out.println("Can't modify");
        }
    }

    /**
     * Open the directory of a selected file or open a directory, both in the file viewer.
     */
    @FXML
    public void OpenDirectory() {
        DirectoryManager.openDir(pathOfFile.getText());
    }

    /**
     * Tracks when there is a mouse click on the TreeView.
     */
    public void getMouseClick() {
        TreeItem<String> select = treeView.getSelectionModel().getSelectedItem();
        if (select != null && !select.isLeaf()) {
            pathOfFile.setText(choiceDir + "/" + DirectoryManager.TruePath(select, select.getValue()));
            choice = choiceDir + "/" + DirectoryManager.TruePath(select, select.getValue());
            displayTilePane(choiceDir + "/" + DirectoryManager.TruePath(select, select.getValue()));
            //displayTilePane("/" + TruePath(select, select.getValue()));
        } else if (select != null && select.isLeaf()) {
            pathOfFile.setText(choiceDir + "/" + DirectoryManager.TruePath(select.getParent(), select.getParent().getValue()));
            choice = choiceDir + "/" + DirectoryManager.TruePath(select.getParent(), select.getParent().getValue());
            displayTilePane(choiceDir + "/" + DirectoryManager.TruePath(select.getParent(),
                    select.getParent().getValue()));
        }
    }

    /**
     * Displays clickable icons on the TilePane.
     *
     * @param selectedName the name of the selected file
     */
    private void displayTilePane(String selectedName) {
        try {
            File selectedDir = new File(selectedName);
            tilePane.getChildren().clear();
            getAllImages(selectedDir);
            for (Node item : tilePane.getChildren()) {
                VBox vbox = (VBox) item;
                vbox.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        String tileClicked = vbox.getId();
                        //Highlights clicked area and enables buttons since a photo is selected.
                        modifyBtn.setDisable(false);
                        moveBtn.setDisable(false);
                        vbox.requestFocus();
                        selectedImagePath = tileClicked;
                        pathOfFile.setText(tileClicked);
                    }
                });
                /*
                 * This code is from:
                 * https://stackoverflow.com/questions/33840436/
                 * javafx-highlight-vbox-on-mouse-click-and-change-color-when-unfocused
                 *
                 * by user: Uluk Biy
                 */
                final Background focused = new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY));
                final Background unFocus = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
                vbox.backgroundProperty().bind(Bindings
                        .when(vbox.focusedProperty())
                        .then(focused)
                        .otherwise(unFocus));
            }
        } catch (Exception e) {
            System.out.println("File is locked.");
        }
    }

    private void getAllImages(File rootFile) {
        File[] allFiles = rootFile.listFiles();
        if (allFiles != null) {
            for (File file : allFiles) {
                if (file.isFile() && !file.isHidden() && PhotoManager.isImage(file)) {
                    Label fileName = new Label(file.getName().replaceAll("............", "$0\n"));
                    fileName.setId(file.getName());
                    ImageView imageView = new ImageView("/img/icon-image.png");
                    VBox vbox = new VBox();
                    vbox.setId(file.getPath());
                    vbox.getChildren().addAll(imageView, fileName);
                    vbox.setAlignment(Pos.TOP_CENTER);
                    tilePane.getChildren().addAll(vbox);
                } else if (file.isDirectory() && !file.isHidden()) {
                    getAllImages(file);

                }
            }
        }
    }

    /**
     * Opens interface so user can enter a new tag.
     */
    @FXML
    public void addNewTag() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addTag.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add new tag");
            stage.setScene(new Scene(root1));
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println("Can't modify");
        }
    }
}
