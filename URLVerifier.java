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
}
