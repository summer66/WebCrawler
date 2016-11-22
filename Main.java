package haoyu.webcrawler;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;

/**Web Crawler Policy:
 *
 *
 *
 *
 * The main class of the web crawler.
 * It creates a user interface that allows the user to
 * choose the type of crawler and string matching method, enter the starting URL,
 * choose the maximum number of URLs to crawl and enter the string to match the webpages against.
 * The crawler results will be displayed as well.
 * Created by Hao on 9/27/2016.
 * Updated by Hao on 10/31/2016
 */
public class Main extends Application {

    private Stage window;
    //The five values obtained from user input
    private static String crawlerType;   //will be used in PA3
    private static String strMatchMed;
    private static String startingURL;
    private static int maxNumOfURLs ;
    private static String keyWords;
    //Four values to be updated during and after crawling
    private static TextArea matchOutputTA;
    private static Label status;
    private static Label totalMatchCount;
    private static Label totalCrawledCount;


    public static void main(String[] args) {
        launch(args);
    }


    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Web Crawler");

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10, 10, 10, 10));

        GridPane topStack = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHalignment(HPos.RIGHT);
        topStack.getColumnConstraints().add(column1);
        topStack.setHgap(10);
        topStack.setVgap(12);
        topStack.setPadding(new Insets(5));

        Label chooseCrawlerLb = new Label("Type of crawler: ");
        Label chooseMatchLb = new Label("String matching method: ");
        Label inputURLLb = new Label("Start URL:");
        Label inputURLNote = new Label("(starting with \"http://\" )");
        Label maxURLsLb = new Label("Max URLs to crawl: ");
        Label maxURLsNote = new Label("(No more than 200)");
        Label searchStrLb = new Label("Search string: ");
        Label multiKeyWordsLb = new Label("(Multiple keywords allowed. Please seperate them with spaces)");

        TextField startURLTF = new TextField();
        TextField strInputTF = new TextField();

        final ToggleGroup group1 = new ToggleGroup();

        RadioButton rb1 = new RadioButton("BFS");
        rb1.setToggleGroup(group1);

        RadioButton rb2 = new RadioButton("DFS");
        rb2.setToggleGroup(group1);

        rb1.setUserData("BFS");
        rb2.setUserData("DFS");

        HBox crawlerRBs = new HBox();
        crawlerRBs.setSpacing(10);
        crawlerRBs.getChildren().addAll(rb1, rb2);

        final ToggleGroup group2 = new ToggleGroup();

        RadioButton rbG2_1 = new RadioButton("Brute Force");
        rbG2_1.setToggleGroup(group2);

        RadioButton rbG2_2 = new RadioButton("Rabin-Karp");
        rbG2_2.setToggleGroup(group2);

        RadioButton rbG2_3 = new RadioButton("Finite Automata");
        rbG2_3.setToggleGroup(group2);

        RadioButton rbG2_4 = new RadioButton("KMP");
        rbG2_4.setToggleGroup(group2);

        rbG2_1.setUserData("Brute Force");
        rbG2_2.setUserData("Rabin-Karp");
        rbG2_3.setUserData("Finite Automata");
        rbG2_4.setUserData("KMP");

        HBox strMatchRBs = new HBox();
        strMatchRBs.setSpacing(10);
        strMatchRBs.getChildren().addAll(rbG2_1, rbG2_2, rbG2_3, rbG2_4);

        ComboBox<String> numOfURLCB = new ComboBox<>();
        numOfURLCB.setEditable(true);
        numOfURLCB.setPrefSize(80, 22);
        numOfURLCB.getItems().addAll("50", "100", "200");
        HBox maxURLsBox = new HBox();
        maxURLsBox.getChildren().addAll(numOfURLCB,maxURLsNote);
        maxURLsBox.setSpacing(10);
        maxURLsBox.setAlignment(Pos.CENTER_LEFT);

        topStack.add(chooseCrawlerLb, 0, 0);
        topStack.add(crawlerRBs, 1, 0);
        topStack.add(chooseMatchLb, 0, 1);
        topStack.add(strMatchRBs, 1, 1);
        topStack.add(inputURLLb, 0, 2);
        topStack.add(startURLTF, 1, 2);
        topStack.add(maxURLsLb,0,3);
        topStack.add(maxURLsBox, 1, 3);
        topStack.add(inputURLNote, 2,2);
        topStack.add(searchStrLb, 0, 4);
        topStack.add(strInputTF, 1, 4);
        topStack.add(multiKeyWordsLb, 1, 5);

        Button searchButton = new Button("Search");
        searchButton.setAlignment(Pos.CENTER);
        topStack.add(searchButton, 2, 4);
        searchButton.setPrefSize(80, 15);

        //pressing the search button to start crawling the web
        searchButton.setOnAction(e -> {
            while (true) {
                //a series of alerts for various invalid user actions.
                if (group1.getSelectedToggle() == null) {
                    AlertBox.display("Alert", "Please select a crawler.");
                    break;
                }
                if (group2.getSelectedToggle() == null) {
                    AlertBox.display("Alert", "Please select a string matching method.");
                    break;
                }
                if (startURLTF.getText().length() < 1 || !URLVerifier.verifyURL(startURLTF.getText())) {
                    AlertBox.display("Alert", "Please enter a valid start URL.");
                    break;
                }

                if (!verifyMaxNumURL(numOfURLCB)) {
                    AlertBox.display("Alert", "Please select or enter a valid maximum number of URLs to crawl.");
                    break;
                }
                if (strInputTF.getText().length() < 1) {
                    AlertBox.display("Alert", "Please enter the keywords.");
                    break;
                }
                //Extract user input values
                crawlerType = group1.getSelectedToggle().getUserData().toString();
                strMatchMed = group2.getSelectedToggle().getUserData().toString();
                startingURL = startURLTF.getText().trim().toLowerCase();
                maxNumOfURLs = Integer.parseInt(numOfURLCB.getValue());
                keyWords = strInputTF.getText().trim().toLowerCase();

                /*
                  For PA2, only the BFS crawler is implemented
                  Choosing either BFS or DFS will invoke the BFS crawler
                */
                BFSCrawler crawler = new BFSCrawler();
                crawler.bfsCrawl(startingURL);
                break;

            }
        });
        pane.setTop(topStack);

        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);

        Label statusLabel = new Label("Status:");
        status = new Label();
        Label crawledCountLb = new Label("Total crawled pages:");
        totalCrawledCount = new Label();
        Label matchCountLb = new Label("Total matched pages:");
        totalMatchCount = new Label();

        HBox statusBox = new HBox();
        statusBox.setPadding(new Insets(5));
        statusBox.setSpacing(10);
        statusBox.getChildren().addAll(statusLabel, status);

        HBox crawledCountBox = new HBox();
        crawledCountBox .setPadding(new Insets(5));
        crawledCountBox .setSpacing(10);
        crawledCountBox .getChildren().addAll(crawledCountLb, totalCrawledCount);

        HBox matchCountBox = new HBox();
        matchCountBox.setPadding(new Insets(5));
        matchCountBox.setSpacing(10);
        matchCountBox.getChildren().addAll(matchCountLb, totalMatchCount);

        Label matchLabel = new Label("Matches: ");
        matchOutputTA = new TextArea();
        matchOutputTA.setPrefSize(400, 250);
        matchOutputTA.setEditable(false);

        VBox matchBox = new VBox();
        matchBox.setSpacing(3);
        matchBox.setPadding(new Insets(5));
        matchBox.getChildren().addAll(separator, statusBox, crawledCountBox, matchCountBox, matchLabel, matchOutputTA);
        pane.setCenter(matchBox);

        Scene scene = new Scene(pane, 700, 550);
        window.setScene(scene);
        window.show();
    }


    /**
     * Private method to send a alert message when the user tries to write in the output text area.
     */
    //private void matchOutputAreaAlert(){
        //AlertBox.display("Alert", "This area is for output only.");
    //}

    /**
     * Private method to verify user's input for the maximum number of URLs to crawl.
     * @param comboBox
     * @return true if the input is a integer between 1 and 200.
     */
    private boolean verifyMaxNumURL(ComboBox<String> comboBox)
    {
        if (comboBox.getValue() == null) return false;
        else if(comboBox.getValue().matches("\\d+"))
        {
            int value = Integer.parseInt(comboBox.getValue());
            if (value <1 || value >200)
                return false;
            else return true;
        }
        else
            return false;
    }

    //accessors to get the values of instance variables
    public static String getStringMatchMethod(){
        return strMatchMed;
    }

    public static String getStartURL(){
        return startingURL;
    }

    public static int getMaxNumOfURLs(){
        return maxNumOfURLs;
    }

    public static String getKeyWords(){
        return keyWords;
    }

    public static TextArea getMatchOutputTA(){
        return matchOutputTA;
    }

    public static Label getStatus(){
        return status;
    }

    public static Label getTotalCrawledCount() { return totalCrawledCount; }

    public static Label getTotalMatchCount(){
        return totalMatchCount;
    }

}





