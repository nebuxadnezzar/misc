package t;

import java.io.*;

public class ToBinary
{
    public static void main(String [] args)
    {
        System.out.println( "\nTO BINARY " );
        System.out.println( toBinaryString(12) );
        System.out.println( toBinaryString(37) );
        System.out.println( toBinaryString(48) );
        
        System.out.println( toBinaryStringRecursive(12) );
        System.out.println( toBinaryStringRecursive(37) );
        System.out.println( toBinaryStringRecursive(48) );
    }
    
    public static String toBinaryString( int input )
    {
        int result = input;
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        
        while( result > 0 )
        {
            ba.write((result % 2) + 0x30);
            result = result / 2;
        }
        return reverse(new String( ba.toByteArray() ));
    }
    
    public static String toBinaryStringRecursive( int input )
    {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        recursiveHelper( input, ba );
        return reverse(new String( ba.toByteArray() ));
    }
    
    private static void recursiveHelper( int input, ByteArrayOutputStream ba )
    {
        if( input < 1 ) return;
        ba.write( ( input % 2 ) + 0x30 );
        recursiveHelper( input / 2, ba );
    }
    
    private static String reverse( String str )
    {
        return new StringBuilder(str).reverse().toString();
    }
}
