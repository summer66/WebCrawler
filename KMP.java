package haoyu.webcrawler;

import java.util.Scanner;

/**
 * String matching method using the Knuth-Morris-Pratt algorithm
 * The basic idea of KMP algorithm is similar to finite automata.
 * The prefix/suffix function for a pattern encapsulates knowledge about how the pattern matches against shifts of itself.
 * The longest prefix/suffix information of the pattern is stored in a LPSArray. We could use this information to avoid testing useless shifts
 * in the Brute Force algorithm.
 * The difference of KMP from finite automata is that it avoids precomputing the full DFA table.
 *
 * Created by Hao Yu on 9/27/2016.
 */
public class KMP extends StrMatcher
{

    private String pattern;
    private int[] LPSArray;
    private int patternLength;

    public KMP(String pattern)
    {
        this.pattern = pattern;
        patternLength = pattern.length();
        LPSArray = new int[patternLength];
        computeLPSArray(pattern);   //build the longest-prefix-suffix array
    }

    /**
     * Method to search for the pattern in given text using the longest-prefix-suffix array
     * @param text
     * @param pattern
     * @return if there is a match (true) or not(false)
     */
    @Override
    public boolean search(String text, String pattern)
    {
        int length = text.length();
        int i = 0;
        int j = 0;
        while (i < length) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == patternLength)
                    return true;
            } else {
                if (i >= length) break;
                //if the current character of the text does not match the first character of the pattern, simply move on to the next character of the text
                if (j == 0)
                    i++;
                //otherwise, the position of the character in the pattern to be compared in the next loop is the length of the previous longest prefix/suffix
                else
                    j = LPSArray[j - 1];   //note that the current i will be used in the next loop again
            }
        }
        return false;
    }

    /**
     * Method to build the longest-prefix-suffix array
     * The LPS array stores the length of the longest prefix/suffix that are equal to each other.
     * @param pattern
     * @return return the LPSArray[]
     */
    private void computeLPSArray(String pattern)
    {
        //front points to the last character of the suffix in the longest prefix/suffix pair. The length of the prefix/suffix stores in LPSArray[front]
        int front = 1;
        //back always points to the last character of the prefix in the longest prefix/suffix pair. The length of the prefix/suffix = back +1 or 0
        int back = 0;
        //the first element is always 0
        LPSArray[0] = 0;

        while (front < patternLength) {
            //if the two characters at the two pointers are equal
            if (pattern.charAt(front) == pattern.charAt(back)) {
                LPSArray[front] = back + 1;  //store the length of the prefix/suffix
                front++;
                back++; //the back pointer only increments when there is a match
            }
            //if not match
            else {
                //if the back pointer is not at the beginning of the array, it goes back to the last character of the previous longest prefix
                //front point does not move.
                //The next loop will check if the last character of the previous longest prefix matches pattern.charAt(front)
                if (back != 0)
                    back = LPSArray[back - 1];

                    //if the back pointer is at the beginning of the array, simply move the front pointer forward and store the length 0
                else {
                    LPSArray[front] = 0;
                    front++;
                }
            }
        }
    }

    @Override
    public String getKeyWord()
    {
        return pattern;
    }

    /**
     * Test client for the KMP string matching method
     * @param args
     */
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        String text = "";
        while (text.length() < 1) {
            System.out.print("Please enter a string to be searched: ");
            text = scanner.nextLine().trim();
        }

        String pattern = "";
        while (pattern.length() < 1) {
            System.out.print("Please enter a target pattern: ");
            pattern = scanner.nextLine().trim();
        }

        KMP newSearch = new KMP(pattern);
        boolean result = newSearch.search(text, pattern);
        if (result)
            System.out.println("There is at least one match.");
        else
            System.out.println("There is no match.");

        scanner.close();
    }

}
