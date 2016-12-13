package haoyu.webcrawler;

import java.util.Scanner;

/**
 * String matching method with finite automata
 * This algorithm builds a deterministic finite automaton (stored as a 2D table) based on the pattern and the alphabet before string matching.
 * Finding the longest prefix which is also a suffix is the key idea in building the DFA.
 * To use the DFA for string searching, the next state for a particular character is looked up in the DFA.
 * When the next state of a character equals to the length of the pattern, it indicates a match.
 * Created by Hao Yu on 9/27/2016.
 */
public class FiniteAutomata extends StrMatcher{

    private String pattern;
    private int alphabetSize = 256;   //the size of the complete ASCII table
    private int patternLength;
    private int[][] dfa;             //the deterministic finite automaton used for string matching

    public FiniteAutomata(String pattern)
    {
        this.pattern = pattern;
        patternLength = pattern.length();
        dfa = new int[patternLength][alphabetSize+1]; //the last column of the dfa is for non-ASCII characters
        computeDFA(pattern, dfa);          //build DFA from pattern
    }

    /**
     * Method to search for the pattern in given text by looking up the DFA
     * @param text
     * @param pattern
     * @return the search result as a boolean
     */
    @Override
    public boolean search(String text, String pattern)
    {
        int length = text.length();
        int j=0;   //state

        for(int i=0; i<length; i++)
        {
            //if the character is not an ASCII character, next state is 0
            if(text.charAt(i) > 255) j = 0;
            //look up the DFA to determine the next state
            else
               j= dfa[j][text.charAt(i)];
            //pattern is matched is the next state = the length of the pattern
            if(j == patternLength) return true;
        }
        return false;
    }

    /**
     * Method to build DFA
     * @param pattern
     * @param dfa
     */
    private void computeDFA(String pattern, int[][] dfa)
    {
        int currState;
        int column;   //the column number corresponds to an ASCII character or all the non-ASCII characters.

        //fill the DFA row by row
        for(currState = 0; currState < patternLength; currState++)
        {
            for(column=0; column<alphabetSize; column++)
            {
                dfa[currState][column] = getNextState(pattern, currState, column);
            }
            //fill the last column with 0 because
            //if a character is not in the ASCII table, the next state is 0
            dfa[currState][256] = 0;
        }
    }

    /**
     * Method to determine the next state based on the next character in the input(lastChar), knowing the pattern, the current state
     * Current state equals to the number of characters that have been matched in the pattern prior to the character being examined (lastChar)
     * @param pattern
     * @param currState
     * @param lastChar
     * @return the next state
     */
    private int getNextState(String pattern, int currState, int lastChar)
    {
        //if the current character is not in the ASCII table, the next state is 0
       // if (lastChar > 255)
            //return 0;

        //if the lastChar equals to the next character in the pattern (pattern.charAt(currState))
        if(pattern.charAt(currState)== lastChar)
            return currState+1;

        int nextState;
        int i;

        //start from the largest possible substring and stop when you find
        //a prefix which is also a suffix in the part of pattern that has been examined.
        //for example, if lastChar is a and the pattern is abab, then the largest possible substrings to be examined are aba and baa
        //and the longest prefix = suffix is a, next state is 1
        for(nextState = currState; nextState>0; nextState--)
        {
            //We know that lastChar (the character being examined) is always the last character of the suffix.
            //If a prefix = a suffix, the last character of the prefix must be the same as the lastChar.
            //From the second to the last character, we compare if that character is the same as lastChar
            if(pattern.charAt(nextState-1) == lastChar)
            {
                //If it is, we assume it has the potential to be the last character of a prefix that's equal to the suffix with the same length
                // We check if it's true by comparing every character in these prefix/suffix. If they don't match,
                // we go back to the outer loop to decrement the next state value by 1 and check again.
                for(i=0; i<nextState-1; i++)
                {
                    if(pattern.charAt(i) != pattern.charAt(currState-nextState+1+i))
                        break;
                }
                //return the nextState value when the prefix = suffix
                if(i== nextState-1) return nextState;
            }
        }
        //if no prefix is equal to any suffix, next state will be 0
        return 0;
    }

    @Override
    public String getKeyWord(){ return pattern;}

    /**
     * Test client for this string matching method.
     * @param args
     */
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

        FiniteAutomata newSearch = new  FiniteAutomata(pattern);
        boolean result = newSearch.search(text, pattern);

        if (result)
            System.out.println("There is at least one match.");
        else
            System.out.println("There is no match.");

        scanner.close();
    }
}
