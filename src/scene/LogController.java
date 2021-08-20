package scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LogController implements Initializable {
    @FXML
    TableView<LogManager> tableView;
    @FXML
    TableColumn<LogManager, String> currName;
    @FXML
    TableColumn<LogManager, String> prevName;
    @FXML
    TableColumn<LogManager, String> date;
    @FXML
    Button cancelButton;

    /**
     * Collection of all name changes
     */
    private ObservableList<LogManager> list = FXCollections.observableArrayList();


    /**
     * Initializes the log
     *
     * @param url the URL to initialize
     * @param rb  the resources for the URL
     */
    public void initialize(URL url, ResourceBundle rb) {
        currName.setCellValueFactory(new PropertyValueFactory<>("currName"));
        prevName.setCellValueFactory(new PropertyValueFactory<>("prevName"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));

        tableView.setItems(list);

        LogManager.addLog(list);
    }

    /**
     * Closes the log.
     */
    @FXML
    public void cancelLog() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }
}
