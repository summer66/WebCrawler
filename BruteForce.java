package haoyu.webcrawler;

import java.util.Scanner;

/**
 * The BruteForce class provides a simple string matching method that uses the naive string-matching algorithm.
 * Created by Hao on 9/30/2016.
 */
public class BruteForce extends StrMatcher{

    private String pattern;

    /**
     * The optional main method of this class prompts the user to enter a string and a pattern (in string),
     * then test if there is a match in the input string against the pattern.
     * Delete the wrapping comment symbols to use the main method.
     * @param
     */
   /*public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        String text = "";
        while(text.length()<1) {
            System.out.print("Please enter a string to be searched: ");
            text = scanner.nextLine().trim();
        }

        String pattern = "";
        while(pattern.length()<1) {
            System.out.print("Please enter a target pattern: ");
            pattern = scanner.nextLine().trim();
        }

        boolean result = BruteForce.bruteForce(text, pattern);
        if (result)
           System.out.println("There is at least one match.");
        else
            System.out.println("There is no match.");

        scanner.close();
    }*/

   public BruteForce(String pattern){
       this.pattern = pattern;
   }

    /**
     * This method is an iterative Brute Force string matching method.
     * @param text
     * @param pattern
     * @return true is there is at least one match.
     */
    @Override
    public boolean search(String text, String pattern)
    {
        int n = text.length();
        int m = pattern.length();
        int j;
        //System.out.println("BruteForce is being used.");
        if (n < m) return false;

        for(int i= 0; i< n-m +1; i++)
        {
            for(j = 0; j< m; j++)
            {
                //check if the pattern matches a portion of the text string character by character
               if(text.charAt(i+j) != pattern.charAt(j)) break;
            }
            //if the inner loop was able to finish without breaking, there is a match.
            if(j==m) return true;
        }
        //if the outloop was able to finish, there is no match.
        return false;
    }

    @Override
    public String getKeyWord()
    {
        return pattern;
    }

}
