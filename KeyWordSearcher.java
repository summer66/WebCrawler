package haoyu.webcrawler;

/**
 * This helper class provides a keyword searcher to determine if the keywords entered by user occur in a certain webpage
 * Created by Hao Yu on 10/30/2016.
 */
public class KeyWordSearcher {

    private String strMethodName;
    private StrMatcher[] strMatchers;
    private String keyWords;

    /**
     * Constructor initiates the instance variables
     */
    public KeyWordSearcher()
    {
        keyWords = Main.getKeyWords();
        strMethodName = Main.getStringMatchMethod();

        String[] keyWordList = keyWords.split(" \\s*");  //split the keywords into individual words

        //create StrMatcher objects polymorphically (placed in StrMatcher[]) according to the string matching method user selected.
        //Each object is initialized with an individual keyword.
        strMatchers = new StrMatcher[keyWordList.length];
        for (int i = 0; i< keyWordList.length; i++) {
            strMatchers[i] = strMatcherCreator(keyWordList[i]);
        }
    }

    /**
     * Overload the searchKeyWords method using input as the only argument
     * @param input
     * @return true if searchKeyWords(String input, StrMatcher[] strMatchers) returns true
     * indicating the existence of at least one match.
     */
    public boolean searchKeyWords(String input){

        return searchKeyWords(input, strMatchers);
    }

    /**
     * Method to determine if the input string contains the keywords that user enetered.
     * @param input
     * @param strMatchers An array of StrMatcher objects
     * @return true if all the individual keywords occur in the input string
     */
    private boolean searchKeyWords(String input, StrMatcher[] strMatchers) {

        //iterate through all the StrMatcher objects and use each of them to search the input string.
        //Only all StrMatcher objects' search method returning true will result in true.
        for (StrMatcher strMatcher : strMatchers) {
            if (!strMatcher.search(input, strMatcher.getKeyWord())) return false;
        }
        return true;
    }

    /**
     * This method polymorphically creates StrMatcher objects with subclasses of StrMatcher class
     * according to the string matching method the user selected.
     * @param keyWord
     * @return
     */
    private StrMatcher strMatcherCreator(String keyWord) {

        switch (strMethodName) {
            case "Brute Force":
                StrMatcher strMatcher = new BruteForce(keyWord);
                return strMatcher;

            case "Rabin-Karp":
                StrMatcher strMatcher2 = new RabinKarp(keyWord);
                return strMatcher2;

            case "Finite Automata":
                StrMatcher strMatcher3 = new FiniteAutomata(keyWord);
                return strMatcher3;

            case "KMP":
                StrMatcher strMatcher4 = new KMP(keyWord);
                return strMatcher4;

            default:
                return null;
        }

    }

}
