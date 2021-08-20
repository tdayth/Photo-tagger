package scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class TagManager {

    static String originalName = "";


    static ObservableList<String> allTags() {
        ObservableList<String> lst = FXCollections.observableArrayList();
        try {
            /*
             * The idea of using a stack to read a while backwards was from:
             * https://stackoverflow.com/questions/8664705/
             * how-to-read-file-from-end-to-start-in-reverse-order-in-java
             *
             * by user: A4L
             */

            BufferedReader br;
            br = new BufferedReader(new FileReader("src/log.txt"));
            Stack<String> lines = new Stack<>();
            String line = br.readLine();
            while (line != null) {
                lines.push(line);
                line = br.readLine();
            }
            // Splits string to find the file name, by knowing the path.
            String currImage = SceneController.selectedImagePath;
            String[] splitCurr = currImage.split("/");

            // Removes file extension.
            String[] noExt = splitCurr[splitCurr.length - 1].split("\\.");

            // Converts from the file name of tags, to how the user initially entered it.
            String finalName = noExt[0].substring(1).replace(" @", ", ");
            //ObservableList<String> lst = FXCollections.observableArrayList();
            while (!lines.empty()) {
                String pp = lines.pop();
                String[] each = pp.split(",~,");

                // Converts from the file name of tags, to how the user initially entered it.
                String noAt = each[1];
                if (each[1].startsWith("@")) {
                    noAt = each[1].substring(1).replace(" @", ", ");
                }
                String no = each[0].substring(1).replace(" @", ", ");
                //Filters the log to find out previous names, and to avoid removed from list tags.
                if (finalName.equals(no) && !(lst.contains(noAt))) {
                    if (each.length < 4) {
                        lst.add(noAt);
                        originalName = noAt;
                    }
                    finalName = noAt;
                } else if (each[0].equals(" ") && !(lst.contains(noAt))) {
                    if (each.length < 4) {
                        lst.add(noAt);
                    }
                }
            }
            if (!originalName.equals("")) {
                lst.remove(originalName);
            }
        } catch (Exception e) {
            System.out.println("couldn't list");

        }
        return lst;
    }

    static ObservableList<String> addedTags() {
        ObservableList<String> lst = FXCollections.observableArrayList();
        BufferedReader br;
        try {
            /*
             * The idea of using a stack to read a while backwards was from:
             * https://stackoverflow.com/questions/8664705/
             * how-to-read-file-from-end-to-start-in-reverse-order-in-java
             *
             * by user: A4L
             */
            br = new BufferedReader(new FileReader("src/log.txt"));
            Stack<String> lines = new Stack<>();
            String line = br.readLine();
            while (line != null) {
                lines.push(line);
                line = br.readLine();
            }
            while (!lines.empty()) {
                String pp = lines.pop();
                String[] each = pp.split(",~,");

                // Converts from the file name of tags, to how the user initially entered it.
                String noAt = each[1].substring(1).replace(" @", ", ");
                if (!each[1].startsWith("@")) {
                    noAt = each[1].replace(" @", ", ");
                }
                if (each[0].equals(" ") && !(lst.contains(noAt))) {
                    if (each.length < 4) {
                        lst.add(noAt);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Couldn't list");
        }
        return lst;
    }

    static void removeTag(String string, TextField text) {
        try {
            /*
            * The idea in order to edit lines in a file was from:
            * https://stackoverflow.com/questions/8563294/modifying-existing-file-content-in-java
            *
            * by user maltesmann
             */
            List<String> newLines = new ArrayList<>();
            for (String line : Files.readAllLines(Paths.get("src/log.txt"), StandardCharsets.UTF_8)) {
                String[] prevNames = line.split(",~,");

                /* If the selected item to remove matches a previous tag, then it edits the log.txt to say it is
                 * removed.
                 */
                if (string.equals(prevNames[1].substring(1).replace(" @", ", "))) {
                    newLines.add(line.replace(line, line + ",~," + "removed"));
                    text.clear();
                } else {
                    newLines.add(line);
                }
            }
            Files.write(Paths.get("src/log.txt"), newLines, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("could not remove");
        }
    }

    static void editTagRemove(String string, TextField text) {
        try {
            /*
            * The idea in order to edit lines in a file was from:
            * https://stackoverflow.com/questions/8563294/modifying-existing-file-content-in-java
            *
            * by user maltesmann
             */
            List<String> newLines = new ArrayList<>();
            for (String line : Files.readAllLines(Paths.get("src/log.txt"), StandardCharsets.UTF_8)) {
                String[] prevNames = line.split(",~,");

                /* If the selected item to remove matches a previous tag, then it edits the log.txt to say it is
                 * removed.
                 */
                if (string.equals(prevNames[1].substring(1).replace(" @", ", "))) {
                    newLines.add(line.replace(line, line + ",~," + "removed"));
                    text.clear();
                } else {
                    newLines.add(line);
                }
            }
            Files.write(Paths.get("src/log.txt"), newLines, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("could not remove");
        }
    }

    static void addTag(TextField text) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/log.txt", true))) {
            String[] tagged = text.getText().split(",");
            StringBuilder allTags = new StringBuilder();
            for (String item : tagged) {
                String trimmed = item.trim();
                allTags.append("@");
                allTags.append(trimmed);
                allTags.append(" ");

            }
            bw.write(" " + ",~," + allTags.toString().trim() + ",~," + new java.util.Date() + "\n");
            bw.close();
        } catch (IOException e) {
            System.out.println("Could not write to file");

        }
    }

}
