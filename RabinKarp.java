package haoyu.webcrawler;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * String matching method using the Rabin-Karp algorithm
 * Rabin-Karp algorithm is based on hashing. A hash function for the pattern is computed.
 * A match in the text is searched by using the same hash function for each possible M-character substring of the text (M = length of the pattern).
 * If we find a text substring with the same hash value as the pattern, a match is confirmed by comparing the substring with the pattern character by character.
 * The hash function used in this program is a prime number modulus. Some manipulations in calculating the hash value are applied in search() and hash().
 * Created by Hao on 9/27/2016.
 * Updated by Hao on 10/31/2016
 */
public class RabinKarp extends StrMatcher{

    private int patLength;
    private int alphabetSize = 256;  //size of the complete ASCII table

    //Choose the type of the following variables to be long in case very large integers will be used during calculation
    private long prime;
    private long patHash;  //hash value of the pattern string
    private long leadMod; // alphabetSize^(patlength-1) % prime
    private String pattern;

    /*
       Constructor initiates the instance variables, calculates and hash value of the pattern string
     */
    public RabinKarp(String pattern)
    {
        this.pattern = pattern;
        patLength = pattern.length();
        //pick a random prime in this range for hash value calculation
        prime = randomPrime(10000, 20000);
        //calculate hash value for the pattern string
        patHash = hash(pattern, patLength);
        System.out.println("Prime is: "+ prime);   //print out the selected prime when an object is initialized
        leadMod = 1;
        //calculate alphabetSize^(patlength-1) % prime to be used to remove the leading digit
        for(int i = 1; i<patLength; i++)
            leadMod = (alphabetSize * leadMod)% prime;
    }

    /**
     * Rabin-karp algorithm
     * Compare pattern hash value to the hash values of substring of text with equal length
     * @param text
     * @param pattern
     * @return true if there is a match
     */
    @Override
    public boolean search(String text, String pattern)
    {
        int n = text.length();
        //System.out.println("Rabin-Karp is being used.");
        if(n < pattern.length()) return false;
        else {
            //calculate the hash value of the first pathLength characters of the text string
            long textHash = hash(text, patLength);
            //check if its a spurious hit
            if (patHash == textHash && check(0, text, pattern))
                return true;
            //for rest of the text string
            for (int i = 1; i < n - patLength + 1; i++) {
                //move the substring to the right by one character, calculate the new hash value based on the previous one
                //Note that an extra prime is added in the first equation to avoid negative value during calculation
                textHash = (textHash + prime - text.charAt(i - 1) * leadMod % prime) % prime;
                textHash = (textHash * alphabetSize + text.charAt(i + patLength - 1)) % prime;
                //check for spurious hit
                if (patHash == textHash) {
                    if (check(i, text, pattern)) return true;
                }
            }
            return false;
        }
    }

    /**
     * Calculate the hash value of a string with a fixed length
     * @param str
     * @param length
     * @return the hash value
     */
    private long hash(String str, int length){
        long hashValue = 0;
        //Horner's method applied to modular hashing
        for(int k = 0; k<length; k++)
            hashValue = (alphabetSize * hashValue + str.charAt(k))% prime;
        return  hashValue;
    }

    /**
     * Compare the substring of text at location i with pattern
     * @param i starting location of the substring
     * @param text
     * @param pat
     * @return true if there is at least a match
     */
    private boolean check(int i, String text, String pat){
        int j;
        int m = pat.length();
        if (text.length()< m) return false;
        for(j = 0; j < pat.length(); j++)
        {
            //check if the pattern matches a portion of the text character by character
            if(text.charAt(i+j) != pat.charAt(j)) break;
        }
        if(j == m) return true;
        return false;
    }

    /**
     * check if a number is prime
     * @param num
     * @return true if the number is prime
     */
    private boolean isPrime(int num) {
        if(num < 2)
            return false;

        if(num == 2 ) return true;

        if(num%2 == 0) return false;

        double sqrtNum = Math.sqrt(num);
        for(int i=3; i <= sqrtNum; i = i+2) {
            if(num % i == 0)    // If num is evenly divisible by i
                return false;   // then it isn't prime
        }
        return true;
    }

    /**
     * Generate random prime numbers in range lo to hi and store them in an arraylist
     * @param lo
     * @param hi
     * @return the arraylist with random primes
     */
    private int randomPrime(int lo, int hi){
        ArrayList<Integer> primeList = new ArrayList<>();
        for (int i = lo; i >= lo && i < hi; i++){
            if(isPrime(i)) primeList.add(i);
        }

        Random generator = new Random();
        return primeList.get(generator.nextInt(primeList.size()));
    }

    /**
     * Get the keyword (pattern) used in this class
     * @return the keyword
     */
    @Override
    public String getKeyWord()
    {
        return pattern;
    }

    /**
     * This main method is used to test this string matching method
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

        RabinKarp newSearch = new RabinKarp(pattern);
        boolean result = newSearch.search(text, pattern);
        if (result)
            System.out.println("There is at least one match.");
        else
            System.out.println("There is no match.");

        scanner.close();
    }
}
