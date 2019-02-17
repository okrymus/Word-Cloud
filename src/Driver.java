/*
 * Word cloud
 * The program enlarge size of the words which occurrence multiple time
 * Last Modified: 9/21/2016
 * Programmer: Panupong Leenawarat
 */

import java.io.*;
import java.util.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.text.Font;
import java.util.ArrayList;     // import arraylist
import javafx.geometry.Orientation;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;

/**
 *
 * @author Panupong_lee
 */
public class Driver extends Application {

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) throws IOException {
        HBox Pane = new HBox();
        Pane.setSpacing(10);
        Pane.setAlignment(Pos.CENTER);

        /**
         * Create an arrayList to hold the data
         */
        ArrayList<String> list = new ArrayList<>();

        Label lblFileName = new Label("File name: ");
        TextField tfFileName = new TextField();                    // To fill the file name

        Pane.getChildren().addAll(lblFileName, tfFileName);

        Button btCalc = new Button("process");
        btCalc.setOnAction(e -> {
            File fileInput = new File(tfFileName.getText());
            String text;

            // Create a TreeMap to hold words as key and count as value
            Map<String, Integer> map = new TreeMap<>();
            try (BufferedReader in = new BufferedReader(new FileReader(fileInput))) {
                // While the file is able to read
                while ((text = in.readLine()) != null) {

                    list.add(text);                                      // Add data to arrayList<String> list 
                    String[] words = text.split("[ \n\t\r.,;:!?(){]");   // Split text and hold in String array

                    // For loop through all the words which are in the array
                    for (int i = 0; i < words.length; i++) {
                        String key = words[i].toLowerCase();             // Change the word which allocates at i to lower case

                        if (key.length() > 0 && key.length() != 1) {
                            // If not, add an entry to the map with the word as the key and value 1.
                            if (!map.containsKey(key)) {
                                map.put(key, 1);
                            } // Increase the value for the word (key) by 1 in the map if the key is already add in the tree
                            else if (map.containsKey(key)) {
                                int value = map.get(key);

                                value++;
                                map.put(key, value);
                            }
                        }
                    }
                }

                // Create a new POP-UP windows
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(primaryStage);

                // FlowPane to display the word cloud
                FlowPane flow = new FlowPane(Orientation.HORIZONTAL);
                flow.getChildren().removeAll();

                Set<Map.Entry<String, Integer>> entrySet = map.entrySet();
                for (Map.Entry<String, Integer> entry : entrySet) {
                    if (!isSuperfluous(entry.getKey()) && entry.getValue() >= 1 && !hasPunctuation(entry.getKey()) && !isNumeric(entry.getKey())) {
                        Text text1 = new Text(entry.getKey() + " ");

                        text1.setFont(Font.font("Courier", FontWeight.BOLD,
                                FontPosture.ITALIC, 15 + entry.getValue() * 3));
                        flow.getChildren().addAll(text1);
                    }
                    // Print occurrences of words in console
                    System.out.println(entry.getValue() + "\t" + entry.getKey());
                }

                /*USE TO TEST PRINT WHOLE DATA WITH ENLARGED OCCERANCE WORDS
                // For loop to get data in the array list
                for (int i = 0; i <= list.size() - 1; i++) {
                    String[] plainWords = list.get(i).split(" ");
                    for (String wordsPane1 : plainWords) {
                        // Remove punctuation
                        String key = wordsPane1.replaceAll("\\s*\\p{Punct}+\\s*$", "").toLowerCase();
                        Text text1 = new Text(wordsPane1 + " ");

                        // If map is certain with the word, the word occured more than one time and the word is not superfluous, process it
                        if (map.containsKey(key) && map.get(key) > 1 && !isSuperfluous(key)) {
                            // Enlarge the word which occurrences multiple time by multiple 2 by the number of occurrences
                            text1.setFont(Font.font("Courier", FontWeight.BOLD,
                                    FontPosture.ITALIC, 15 + map.get(key) * 2));
                            flow.getChildren().addAll(text1);

                        } else {
                            text1.setFont(Font.font("Courier", FontWeight.BOLD,
                                    FontPosture.ITALIC, 15));
                            flow.getChildren().add(text1);

                        }
                    }
                } */
                Scene dialogScene = new Scene(flow, flow.getMaxWidth(), flow.getMinHeight());
                dialog.setTitle(tfFileName.getText());
                dialog.setScene(dialogScene);
                dialog.show();

            } catch (Exception ex) {
                // If the file is not exist 
                tfFileName.setText("File Not Found");
                System.out.println("File Not Found");
            }
        }
        );

        Pane.getChildren().addAll(btCalc);

        Scene scene = new Scene(Pane, 400, 100);
        primaryStage.setTitle("Words Cloud");   // Set the stage title
        primaryStage.setScene(scene);              // Place the scene in the stage
        primaryStage.show();
    }

    // Return true if the word has punctuation 
    private static boolean hasPunctuation(String str) {
        Pattern p = Pattern.compile("[\\p{Alpha}]*[\\p{Punct}][\\p{Alpha}]*");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    // Return true if String str is equal the words in the parentheses(...)
    // Return false if it is not match
    private static boolean isSuperfluous(String str) {

        return str.equals("is") || str.equals("are") || str.equals("am") || str.equals("and") || str.equals("in") || str.equals("on") || str.equals("out")
                || str.equals("there") || str.equals("those") || str.equals("this") || str.equals("the") || str.equals("that") || str.equals("we")
                || str.equals("i") || str.equals("I") || str.equals("he") || str.equals("she") || str.equals("it") || str.equals("they") || str.equals("us")
                || str.equals("him") || str.equals("me") || str.equals("her") || str.equals("their") || str.equals("its") || str.equals("hers")
                || str.equals("you") || str.equals("your") || str.equals("my") || str.equals("of") || str.equals("to") || str.equals("did") || str.equals("and");
    }

    // Return true if s is numeric
    public boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
