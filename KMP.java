package haoyu.webcrawler;

import java.util.Scanner;

/**
 * String matching method using the Knuth-Morris-Pratt algorithm
 * Created by Hao on 9/27/2016.
 */
public class KMP extends StrMatcher{
    private String pattern;
    private int[] LPSArray;
    private int patternLength;

    public KMP(String pattern)
    {
        this.pattern = pattern;
        patternLength = pattern.length();
        LPSArray = new int[patternLength];
    }

    @Override
    public boolean search(String text, String pattern)
    {
        computeLPSArray(pattern);
        int length = text.length();
        int i = 0;
        int j = 0;
        while (i<length)
        {
            if(text.charAt(i) == pattern.charAt(j))
            {
                i++;
                j++;
                if(j == patternLength)
                    return true;
            }
            else
            {
                if(i>=length) break;
                if(j == 0)
                i++;
                else
                   j = LPSArray[j-1];
            }
        }
         return false;
    }

    private int[] computeLPSArray(String pattern)
    {
        int i=1;
        int indexLPS = 0;
        LPSArray[0] = 0;

        while(i<patternLength)
        {
            if(pattern.charAt(i) == pattern.charAt(indexLPS))
            {
                indexLPS++;
                i++;
                LPSArray[i] = indexLPS;
            }
            else
            {
                if(indexLPS != 0)
                    indexLPS = LPSArray[indexLPS-1];

                else
                {
                    LPSArray[i] = 0;
                    i++;
                }
            }
        }
        return LPSArray;
    }

    @Override
    public String getKeyWord(){ return pattern;}

    public static void main(String[] args)
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

        KMP newSearch = new  KMP(pattern);
        boolean result = newSearch.search(text, pattern);
        if (result)
            System.out.println("There is at least one match.");
        else
            System.out.println("There is no match.");

        scanner.close();
    }

}
