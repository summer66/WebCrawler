<<<<<<< HEAD
package haoyu.webcrawler;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Web crawler using iterative deepening depth-first search
 * Iterative deepening depth-first search is a search strategy in which a depth-limited depth-first search is run
 * repeatedly with increasing depth limits in each iteration until the maximum number of searched URLs is reached.
 * IDDFS is a essentially a combination of breadth-first search and depth-first search. But it uses much less memory than BFS and
 * the depth of DFS is controlled so that the chance of finding matched webpages is increased compared to DFS.
 * <p>
 * A main feature of IDDFS is that every iteration of the algorithm starts at the root (seed URL). For those parsable and valid URLs, many of them will be visited repetitively but only searched once.
 * Therefore, the variable maxURLCrawled in this class represents the maximum number of urls that are searched.
 * On the contrary, for BFS crawler, a parsable and valid url is always visited(crawled) and searched once.
 * Created by Hao Yu on 9/27/2016.
 */
public class IDDFSCrawler
{
    private int maxURLCrawled;
    private int maxDepth;
    private HashSet<String> searched;
    private ArrayList<String> matches;
    private HashSet<String> invalidURLs;
    private TextArea textArea;

    /**
     * Constructor initiates the instance variables
     */
    public IDDFSCrawler()
    {
        maxDepth = 0;   //max depth of each iteration of the IDDFS search
        maxURLCrawled = Main.getMaxNumOfURLs();
        //searched set contains all the urls that have been searched
        //
        searched = new HashSet<>();
        //matches is an Arraylist containing all the urls of the pages that contain keywords
        matches = new ArrayList<>();
        invalidURLs = new HashSet<>();
        textArea = Main.getMatchOutputTA();
    }

    /**
     * Method to crawl the internet with the seed URL with IDDFS and update the UI
     *
     * @param seedURL
     */
    public void crawl(String seedURL)
    {

        //Initialize the elements of the UI that will be updated
        Label status = Main.getStatus();
        Label totalCrawledCount = Main.getTotalCrawledCount();
        Label totalMatchCount = Main.getTotalMatchCount();
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
                KeyWordSearcher newSearcher = new KeyWordSearcher();
                PageParser pageParser = new PageParser();
                //if the seed url can't be searched
                if (!pageParser.canCrawl(seedURL)) {
                    System.out.println("Error occured crawling:" + seedURL);
                } else {
                    while (searched.size() < maxURLCrawled) {
                        depthLimitedSearch(seedURL, maxDepth, newSearcher, pageParser, stringBuilder);
                        maxDepth++;
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
                    totalCrawledCount.setText(Integer.toString(searched.size()));
                    totalMatchCount.setText(Integer.toString(matches.size()));
                }
            });
        });
        new Thread(task).start();
    }

    /**
     * Method to perform a depth limited depth-first search
     *
     * @param url
     * @param maxDepth
     * @param newSearcher
     * @param pageParser
     */
    private void depthLimitedSearch(String url, int maxDepth, KeyWordSearcher newSearcher, PageParser pageParser, StringBuilder stringBuilder)
    {

        int depthOfCurrURL = 0;
        LinkedStack stack = new LinkedStack();  //use a stack to store URLs to be searched (together with their depths)

        stack.push(URLVerifier.normalizeURL(url), depthOfCurrURL);
        while (!stack.isEmpty() && searched.size() < maxURLCrawled && depthOfCurrURL <= maxDepth) {
            url = stack.peek();
            //obtain the depth of the current url
            depthOfCurrURL = stack.pop();
            boolean verified = URLVerifier.verifyURL(url);
            //if the url can't be verified, add it to the collection of invalid urls and go to next url in the stack
            if (!verified) {
                System.out.println("Error occured verifying:" + url);
                invalidURLs.add(url);
                continue;
            }
            boolean parsable = pageParser.canCrawl(url);
            //if the url can't be parsed, add it to the collection of invalid urls and go to next url in the stack
            if (!parsable) {
                System.out.println("Error occured parsing:" + url);
                invalidURLs.add(url);
                continue;
            }
            //Only when the url is not already searched, its content will be searched
            if (!searched.contains(url)) {
                searched.add(url);//add it to the searched set
                System.out.println("searched: " + url);
                String input = pageParser.getPageText(url);
                boolean searchResult = newSearcher.searchKeyWords(input);
                if (!searchResult)
                    System.out.println(url + ": " + "no match found.");
                else {
                    System.out.println(url + ": " + "successful match!");
                    matches.add(url); //add it to the matches queue if successfully matched
                    stringBuilder.append(url + "\n");
                    String matchedPages = stringBuilder.toString();
                    textArea.setText(matchedPages);
                }
            }
            //if number of searched urls has reached the maximum number the user indicated, stop crawling
            if (searched.size() >= maxURLCrawled) break;
            //if the depth of the webpage is less than the max depth for this iteration, collect the links on it
            //if the depth of the webpage is equal to max depth, go back to while loop
            if (depthOfCurrURL < maxDepth) {
                LinkedList<String> linksOnCurrPage;
                linksOnCurrPage = pageParser.getLinks(url);
                for (String link : linksOnCurrPage) {
                    link = URLVerifier.normalizeURL(link);
                    //If the url is not already known to be not parsable or invalid, we push it onto the stack
                    //Note that the urls that are already in the searched set and in the stack are still added
                    //because their depths are different
                    if (!invalidURLs.contains(link))
                        //push the link on to the stack with its depth 1 greater than the current url.
                        //Note that depthOfCurrURL is not incremented here. The value of this variable is updated above when the stack is popped
                        stack.push(link, depthOfCurrURL + 1);

                }
            }
        }

    }

}
