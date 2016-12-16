package haoyu.webcrawler;

/**
 * Created by Hao on 10/31/2016.
 */
public class URLVerifier
{
    public static boolean verifyURL(String url)
    {
        // Only allow HTTP URLs.
        if (url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://"))
            return true;
        else return false;
    }

    /**
     * helper method to normalize url (only simply convert it to lower case and add a "/" at the end to avoid duplicates
     * @param url
     * @return the normlized the url
     */
    public static String normalizeURL(String url)
    {
        url = url.toLowerCase().trim();
        if(url.contains("#")) {
            int m = url.indexOf("#");
            url = url.substring(0, m);
        }
        if (!url.endsWith("/"))
            url = url + "/";

        return url;
    }

}