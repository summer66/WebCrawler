package haoyu.webcrawler;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.LinkedList;
import java.util.HashSet;

/**
 * Web crawler using breadth-first search
 * Breadth-first search is a graph traversing or searching algorithm. It starts at a node searving as the root of the search tree
 * visits all the adjacent nodes before moving to the next level nodes.
 * Created by Hao on 9/27/2016.
 */
public class BFSCrawler
{

    private int maxURLCrawled;

    private HashSet<String> crawled;
    private LinkedList<String> toCrawl;
    private LinkedList<String> matches;
    private HashSet<String> invalidURLs;

    /**
     * Constructor initiates the instance variables
     */
    public BFSCrawler()
    {

        maxURLCrawled = Main.getMaxNumOfURLs();
        //crawled set contains all the urls that have been crawled
        crawled = new HashSet<>();
        //toCrawl is a queue implemented with a linked list containing all the urls that are in line to be crawled
        toCrawl = new LinkedList<>();
        //matches is a linked list containing all the urls of the pages that contain keywords
        matches = new LinkedList<>();
        invalidURLs = new HashSet<>();

    }

    /**
     * Method to crawl a url with breadth-first search algorithm
     *
     * @param seedURL
     */
    public void crawl(String seedURL)
    {

        //Initialize the elements of the UI that will be updated
        Label status = Main.getStatus();
        Label totalCrawledCount = Main.getTotalCrawledCount();
        Label totalMatchCount = Main.getTotalMatchCount();
        TextArea textArea = Main.getMatchOutputTA();
        textArea.setEditable(true);

        //Handle the UI and background threads
        Platform.runLater(new Runnable()
        {
            public void run()
            {
                //Initialize the UI elements for every new search
                status.setText("Crawling");
                totalCrawledCount.setText("");
                totalMatchCount.setText("");
                textArea.setText("");
            }
        });
        //search thread
        Task<Void> task = new Task<Void>()
        {
            @Override
            public Void call() throws Exception
            {

                StringBuilder stringBuilder = new StringBuilder();
                //Initialize crawling with the start URL
                toCrawl.add(URLVerifier.normalizeURL(seedURL));
                PageParser pageParser = new PageParser(); //a new PageParser object to handle parsing the html page
                if (!pageParser.canCrawl(seedURL)) {
                    System.out.println("Error occured crawling the start URL.");
                } else {
                    LinkedList<String> linksOnCurrPage;  //a linked list to store all the linked on the page being crawled
                    KeyWordSearcher newSearch = new KeyWordSearcher(); // a new KeyWordSearcher object to handle keyword matching in page text

                    //When there is urls to crawl and the number of urls crawled is less than requested by the user
                    while (toCrawl.size() != 0 && crawled.size() < maxURLCrawled) {
                        //remove the first url in the toCrawl queue
                        String url = toCrawl.removeFirst();
                        //System.out.println("Size of toCrawl is: "+ toCrawl.size());
                        // System.out.println("Size of crawled is: "+ crawled.size());
                        //System.out.println("Next url to crawl is: "+ url);

                        boolean verified = URLVerifier.verifyURL(url);
                        if (!verified) {
                            System.out.println("Error occured verifying:" + url);
                            invalidURLs.add(url);
                            continue;
                        }
                        boolean parsable = pageParser.canCrawl(url);
                        if (!parsable) {
                            System.out.println("Error occured parsing:" + url);
                            invalidURLs.add(url);
                            continue;
                        }
                        //execute the following only when the current url can be crawled. Otherwise, go to next iteration of the while loop
                        crawled.add(url);   //add it to the crawled set
                        String input = pageParser.getPageText(url);
                        boolean searchResult = newSearch.searchKeyWords(input);
                        if (!searchResult)
                            System.out.println(url + ": " + "no match found.");
                        else {
                            System.out.println(url + ": " + "successful match!");
                            matches.addLast(url);        //add it to the matches queue if successfully matched

                            //Output the matched list of urls in the text area
                            stringBuilder.append(url + "\n");
                            String matchedPages = stringBuilder.toString();
                            textArea.setText(matchedPages);
                        }
                        //Collect all the links on current page no matter if the keywords are matched or not.
                        //Note that these links are absolute urls made sure by the PageParser's canCrawl method
                        linksOnCurrPage = pageParser.getLinks(url);
                        //for all the links on this page (breath-first)
                        for (String link : linksOnCurrPage) {
                            link = URLVerifier.normalizeURL(link);
                            //add the link to the toCrawl queue only when it's not crawled before and it's not already in the toCrawl queue
                            if (!crawled.contains(link) && !toCrawl.contains(link) && !invalidURLs.contains(url)) {
                                toCrawl.addLast(link);
                            }
                        }

                    }
                }
                textArea.setEditable(false);
                return null;
            }
        };
        //update the status in UI
        task.setOnSucceeded(e -> {
            Platform.runLater(new Runnable()
            {
                public void run()
                {
                    status.setText("Completed");
                    totalCrawledCount.setText(Integer.toString(crawled.size()));
                    totalMatchCount.setText(Integer.toString(matches.size()));
                }
            });
        });
        new Thread(task).start();
    }

}



