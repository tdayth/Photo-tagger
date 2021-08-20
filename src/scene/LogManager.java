package scene;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;

public class LogManager {
    private SimpleStringProperty currName;
    private SimpleStringProperty prevName;
    private SimpleStringProperty date;

    /**
     * Returns the log of when the name is changed
     *
     * @param newName new name
     * @param oldName previous name
     * @param date    date
     */
    private LogManager(String newName, String oldName, String date) {
        super();
        this.currName = new SimpleStringProperty(newName);
        this.prevName = new SimpleStringProperty(oldName);
        this.date = new SimpleStringProperty(date);
    }

    static void addLog(ObservableList<LogManager> list) {
        try (BufferedReader br = new BufferedReader(new FileReader("src/log.txt"))) {
            String line = br.readLine();

            while (line != null) {
                String[] split = line.split(",~,");
                if (!split[0].equals(" ")) {
                    list.add(new LogManager(split[0].trim(), split[1].trim(), split[2].trim()));
                    line = br.readLine();
                } else {
                    line = br.readLine();
                }
            }
        } catch (Exception e) {
            System.out.println("nothing found");
        }
    }



    // All getters are required in TagsController, just not called explicitly.

    /**
     * Returns the current name
     *
     * @return String
     */
    public String getCurrName() {
        return currName.get();
    }

    /**
     * Returns the previous name
     *
     * @return String
     */
    public String getPrevName() {
        return prevName.get();
    }

    /**
     * Returns the date
     *
     * @return String
     */
    public String getDate() {
        return date.get();
    }

}
