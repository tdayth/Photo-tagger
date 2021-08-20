package scene;

import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.activation.MimetypesFileTypeMap;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class PhotoManager {

    static boolean moved = false;

    static void movePhoto() {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.home")));
        Stage stage = new Stage();
        File choices = dc.showDialog(stage);
        try { // Try to rename image to selected photo path.
            String pathOfChoice = choices.getPath();
            String[] eachDir = SceneController.selectedImagePath.split("/");
            String newLoc = pathOfChoice + "/" + eachDir[eachDir.length - 1];
            File correct = new File(newLoc);
            File fileMoved = new File(SceneController.selectedImagePath);
            moved = fileMoved.renameTo(correct);

        } catch (Exception e) {
            System.out.println("Couldn't open file");
        }
    }

    static boolean isImage(File file) {
        String mt= new MimetypesFileTypeMap().getContentType(file);
        String type = mt.split("/")[0];
        return (type.equals("image") || file.getName().toLowerCase().endsWith(".png"));
    }

    static boolean reName(TextField text) {

        /* Finds the path of changed image, and finds the extension of the previous file.*/
        File selectedFile = new File(SceneController.selectedImagePath);
        String selectedPath = selectedFile.getPath();
        String[] splitDir = selectedPath.split("/");
        String lastItem = splitDir[splitDir.length - 1];
        String[] atDot = lastItem.split("\\.");
        String newLoc = "/";

        for (int i = 0; i < splitDir.length - 1; i++) {
            newLoc = newLoc + "/" + splitDir[i];
        }
        // Adds @ to each name separated by a comma.
        String[] tagged = text.getText().split(",");
        String allTags = "";
        for (String item : tagged) {
            String trimmed = item.trim();
            allTags = allTags + "@" + trimmed + " ";

        }
        // Complete
        newLoc = newLoc + "/" + allTags.trim() + "." + atDot[atDot.length - 1];
        File newFile = new File(newLoc);
        boolean reNamed = selectedFile.renameTo(newFile);
        if (reNamed) {

            //Adds the changes to the log.
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/log.txt", true))) {
                bw.write(allTags.trim() + ",~," + atDot[0] + ",~," +
                        new java.util.Date() + "\n");
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return reNamed;
    }

    static boolean original(String string) {

        /* Finds the path of changed image, and finds the extension of the previous file.*/
        File selectedFile = new File(SceneController.selectedImagePath);
        String selectedPath = selectedFile.getPath();
        String[] splitDir = selectedPath.split("/");
        String lastItem = splitDir[splitDir.length - 1];
        String[] atDot = lastItem.split("\\.");
        String newLoc = "/";

        for (int i = 0; i < splitDir.length - 1; i++) {
            newLoc = newLoc + "/" + splitDir[i];
        }
        // Complete
        newLoc = newLoc + "/" + string + "." + atDot[atDot.length - 1];
        File newFile = new File(newLoc);
        boolean reNamed = selectedFile.renameTo(newFile);
        if (reNamed) {

            //Adds the changes to the log.
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/log.txt", true))) {
                bw.write(string + ",~," + atDot[0] + ",~," +
                        new java.util.Date() + "\n");
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return reNamed;
    }
}
