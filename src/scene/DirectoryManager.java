package scene;

import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.File;
import java.io.IOException;

class DirectoryManager {


    /**
     * Gets all the files under the user's home.
     *
     * @param homeDirectory The user's home directory.
     * @return The tree of all files on the computer.
     */
    static TreeItem<String> getFiles(File homeDirectory) {
        TreeItem<String> root = new TreeItem<>(homeDirectory.getName(), new ImageView("/img/folder.png"));

        File[] allFiles = homeDirectory.listFiles();
        if (allFiles != null) {
            for (File file : allFiles) {
                if (file.isDirectory() && !file.isHidden()) {
                    root.getChildren().add(getFiles(file));
                } else if (file.isFile() && !file.isHidden() && PhotoManager.isImage(file)) {
                    root.getChildren().add(new TreeItem<>(file.getName(), new ImageView("/img/image.png")));
                }
            }
        }
        return root;

    }

    static void openDir(String path) {
        Desktop desktop = Desktop.getDesktop();
        File ToOpen;
        try {
            ToOpen = new File(path);
            if (ToOpen.isDirectory()) {
                desktop.open(ToOpen);
            } else {
                String parent = ToOpen.getParent();
                desktop.open(new File(parent));
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Finds the correct path from where the item is located to the root.
     *
     * @param item   The tree item that was selected
     * @param string Name of selected item
     * @return String of the path
     *
     */
    static String TruePath(TreeItem<String> item, String string) {
        if (item.getParent() == null) {
            return string;
        } else {
            String dir = item.getParent().getValue();
            dir = dir + "/" + string;
            return TruePath(item.getParent(), dir);
        }
    }
}

