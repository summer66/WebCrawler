package haoyu.webcrawler;

import java.util.Scanner;

/**
 * String matching method with finite automata
 * Created by Hao on 9/27/2016.
 */
public class FiniteAutomata extends StrMatcher{

    private String pattern;
    private int alphabetSize = 256;
    private int patternLength;
    private int[][] dfa;

    public FiniteAutomata(String pattern)
    {
        this.pattern = pattern;
        patternLength = pattern.length();
        dfa = new int[patternLength][alphabetSize];
    }

    @Override
    public boolean search(String text, String pattern)
    {
        int length = text.length();
        computeDFA(pattern, dfa);
        int j=0;
        for(int i=0; i<length; i++)
        {
            j= dfa[j][text.charAt(i)];
            if(j == patternLength) return true;
        }
        return false;
    }


    private void computeDFA(String pattern, int[][] dfa)
    {
        int currState;
        int column;
        for(currState = 0; currState < patternLength; currState++)
        {
            for(column=0; column<alphabetSize; column++)
            {
                dfa[currState][column] = getNextState(pattern, currState, column);
            }
        }
    }
    private int getNextState(String pattern, int currState, int lastChar)
    {
        //note that characters can be compared to their ASCII codes directly
        if(currState < patternLength && pattern.charAt(currState)== lastChar)
            return currState+1;
        int nextState;
        int i;

        for(nextState = currState; nextState>0; nextState--)
        {
            if(pattern.charAt(nextState-1) == lastChar)
            {
                for(i=0; i<nextState-1; i++)
                {
                    if(pattern.charAt(i) != pattern.charAt(currState-nextState+1+i))
                        break;
                }
                if(i== nextState-1) return nextState;
            }
        }
        return 0;
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

        FiniteAutomata newSearch = new  FiniteAutomata(pattern);
        boolean result = newSearch.search(text, pattern);
        if (result)
            System.out.println("There is at least one match.");
        else
            System.out.println("There is no match.");

        scanner.close();
    }
}
