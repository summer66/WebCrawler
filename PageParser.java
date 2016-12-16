package haoyu.webcrawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.LinkedList;
import java.io.IOException;

/**
 * This class parses an HTML page, outputs its text content and all the links on it
 * Created by Hao Yu on 10/28/2016.
 */
public class PageParser
{

    private Document htmlDocument;
    //The user agent information of my browser is found at http://www.whoishostingthis.com/tools/user-agent/
    //Please edit it when necessary
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36";
    private LinkedList<String> links = new LinkedList<>();
    private String pageText;

    /**
     * Check if the passed URL can be crawled. Also collect all the linked URLs from this page and extract its text content
     *
     * @param url
     * @return true if the passed url can be crawled
     */

    public boolean canCrawl(String url)
    {

        try {
            //test connection. Redirection is not allowed to avoid issues caused by redirecting.
            Connection.Response response = Jsoup.connect(url).userAgent(USER_AGENT).followRedirects(false).timeout(5000).execute();

            if (response.statusCode() == 200) {
                if (!response.contentType().contains("text/html")) {
                    System.out.println("Wrong content type: " + url);
                    return false;
                }
                //parse the page
                htmlDocument = response.parse();

                //get the text content of the html page
                pageText = htmlDocument.body().text().toLowerCase();
                Elements linksOnPage = htmlDocument.select("a[href]");
                if (linksOnPage != null) {
                    for (Element link : linksOnPage)
                        //Get the link's absolute url
                        this.links.add(link.absUrl("href"));
                }
                return true;
            }
            System.out.println("Connection failure: " + response.statusCode() + " " + url);
            return false;

        } catch (IOException e) {
            System.out.println("Other reasons caused failure when accessing " + url);
            return false;
        }
    }

    public String getPageText(String url)
    {
        if(htmlDocument == null) canCrawl(url);
        return pageText;
    }

    public LinkedList<String> getLinks(String url)
    {
        if(htmlDocument == null) canCrawl(url);
        return links;
    }


}