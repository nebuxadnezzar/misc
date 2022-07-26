package t;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
// Uncomment below classes to send network request if needed.
// import java.net.HttpURLConnection;
// import java.net.URL;

class Main {
    public static void main(String args[] ) throws Exception {
        String inputData = "";
        String thisLine = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while ((thisLine = br.readLine()) != null) {
            inputData += thisLine + "\n";
        }
        // Output the solution to the console
        System.out.println(codeHere(inputData));
    }
    
    public static String codeHere(String inputData) {
        // Use this function to write your solution;
        
        char [] ss = inputData.replaceAll("\n", "").toCharArray();
        
        int [] [] patterns = { {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, 
                               {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                               {0, 4, 8}, {2, 4, 6} 
                             };
                             
        for( int i = 0, k = patterns.length; i < k; i++ )
        {
        	int [] p = patterns[i];
        	char firstChar = ss[p[0]];
        	int n = firstChar - ss[p[1]] + firstChar - ss[p[2]];
        	if( n == 0 && firstChar != '-')
        	{
        		return String.valueOf(firstChar);
        	}
        }
        return "";
    }
    
}


/* 
Pattern pattern = Pattern.compile("([XO]{3}(\\b|$)|(X[^X]{2}(\\b|$))|([^X]X[^X](\\b|$)){3}|([^X]{2}X(\\b|$)){3})");
        Matcher match = pattern.matcher(s);
        */


