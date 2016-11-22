package haoyu.webcrawler;
import java.util.Scanner;

/**
 * A super class for string matching methods
 * The two methods will be overriden by subclasses
 * Created by Hao on 10/28/2016.
 */
public  class StrMatcher {

    public boolean search(String text, String pattern){
        return false;
    }

    public String getKeyWord() {
        return null;
    }
}
