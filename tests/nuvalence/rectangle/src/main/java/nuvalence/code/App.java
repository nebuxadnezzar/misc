package nuvalence.code;

/**
 * java -cp target/rectangle-1.0-SNAPSHOT.jar nuvalence.code.App
 *
 *
 */
 
import nuvalence.code.dto.*;
import java.util.function.*;

public class App 
{
    public static void main( String[] args )
    {
        demo();
        
    }
    
    private static void demo()
    {
        Rect r1 = new Rect( 2, 2, 2, 1 ); // contained rect 
        Rect r2 = new Rect( 1, 1, 4, 4 ); // container rect
        Rect r3 = new Rect( 7, 2, 1, 3 ); // detached rect
        Rect r4 = new Rect( 2, 2, 3, 7 ); // overlapping rect
        Rect r5 = new Rect( 5, 1, 5, 7 ); // side touching
        
        System.out.println( App.isContained( r1, r2));
        System.out.println( App.isDetached( r3, r2));
        System.out.println( App.isOverlapped( r4, r2));
        System.out.println( App.isTouching( r5, r2));
        System.out.println(isAdjacent( r5, r2 )); 
    }
    
    public static Adjacent isAdjacent( Rect r1, Rect r2 )
    {
        if( ! isTouching( r1, r2 ) )
        { return Adjacent.NONE; }
        
        BiPredicate<Integer,Integer> predicate = (i1, i2) -> i1 <= i2;
        
        int [] ary1 = r1.toArray();
        int [] ary2 = r2.toArray();
        
        if( isLowerLeftPointWithin( r1, r2, predicate ) )
        {
            if( ary1[0] - ary2[0] + ary1[1] - ary2[1] + ary1[2] - ary2[2] == 0 )
              { return Adjacent.PROPER; }
            else
              { return Adjacent.PARTIAL; }
        }
        
        if( isUpperLeftPointWithin( r1, r2, predicate ) )
        {
            if( ary1[0] - ary2[0] + ary1[1] - ary2[1] + ary1[3] - ary2[3] == 0 )
              { return Adjacent.PROPER; }
            else
              { return Adjacent.PARTIAL; }
        }
        
        if( isUpperRightPointWithin( r1, r2, predicate ) )
        {
            if( ary1[0] - ary2[0] + ary1[2] - ary2[2] + ary1[3] - ary2[3] == 0 )
              { return Adjacent.PROPER; }
            else
              { return Adjacent.PARTIAL; }
        }
        
        if( isLowerRightPointWithin( r1, r2, predicate ) )
        {
            if( ary1[0] - ary2[0] + ary1[2] - ary2[2] + ary1[3] - ary2[3] == 0 )
              { return Adjacent.PROPER; }
            else
              { return Adjacent.PARTIAL; }
        }
        return Adjacent.NONE;
    }
    
    static void printa(int [] a )
    {
        for( int i = 0, k = a.length; i < k; i++ )
        {
            System.out.printf("%02d ", a[i] );
        }
        System.out.println();
    }
    /**
         * checks whether Rect r1 is contained inside of Rect r2
         */
    public static boolean isTouching( Rect r1, Rect r2 )
    {
        BiPredicate<Integer,Integer> predicate1 = (i1, i2) -> i1 <= i2;
        BiPredicate<Integer,Integer> predicate2 = (i1, i2) -> i1 < i2;
        return isOverlapped( r1, r2, predicate1 ) && 
              !isOverlapped( r1, r2, predicate2 );
    }

    public static boolean isContained( Rect r1, Rect r2 )
    {
        BiPredicate<Integer,Integer> predicate = (i1, i2) -> i1 <= i2;

        return isLowerLeftPointWithin( r1, r2, predicate ) &&
               isUpperLeftPointWithin( r1, r2, predicate ) &&
               isLowerRightPointWithin( r1, r2, predicate ) &&
               isUpperRightPointWithin( r1, r2, predicate );
    }

    public static boolean isOverlapped( Rect r1, Rect r2 )
    {
        BiPredicate<Integer,Integer> predicate = (i1, i2) -> i1 <= i2;
        return isOverlapped( r1, r2, predicate);
    }
    
    public static boolean isOverlapped( Rect r1, Rect r2, BiPredicate predicate )
    {
        return isLowerLeftPointWithin( r1, r2, predicate ) ||
               isUpperLeftPointWithin( r1, r2, predicate ) ||
               isLowerRightPointWithin( r1, r2, predicate ) ||
               isUpperRightPointWithin( r1, r2, predicate ); 
    }
    
    public static boolean isDetached( Rect r1, Rect r2 )
    {
        return ! ( isContained( r1, r2) || isOverlapped( r1, r2 ) );
    }
    /**
         * set of methods to check whether Rect r1's corner coordinates are inside of Rect r2
         */
    static boolean isUpperLeftPointWithin( Rect r1, Rect r2, BiPredicate predicate )
    {
        return predicate.test(r1.getY() + r1.getHeight(), r2.getY() + r2.getHeight()) &&
               predicate.test(r2.getY(), r1.getY() + r1.getHeight()) && 
               predicate.test(r2.getX(), r1.getX()) &&
               predicate.test(r1.getX(), r2.getX() + r2.getWidth());
    }
    
    static boolean isLowerLeftPointWithin( Rect r1, Rect r2, BiPredicate predicate)
    {
        return predicate.test(r1.getX(), r2.getX() + r2.getWidth()) &&
               predicate.test(r2.getX(), r1.getX()) &&
               predicate.test(r1.getY(), r2.getY() + r2.getHeight()) &&
               predicate.test(r2.getY(), r1.getY());
    }

    static boolean isUpperRightPointWithin( Rect r1, Rect r2, BiPredicate predicate )
    {
        return predicate.test(r1.getX() + r1.getWidth(), r2.getX() + r2.getWidth()) &&
               predicate.test(r2.getX(), r1.getX() + r1.getWidth()) &&
               predicate.test(r1.getY() + r1.getHeight(), r2.getY() + r2.getHeight()) &&
               predicate.test(r2.getY(), r1.getY());
    }

    static boolean isLowerRightPointWithin( Rect r1, Rect r2, BiPredicate predicate )
    {
        return predicate.test(r1.getX() + r1.getWidth(), r2.getX() + r2.getWidth()) &&
               predicate.test(r2.getX(), r1.getX() + r1.getWidth()) &&
               predicate.test(r1.getY(), r2.getY() + r2.getHeight()) &&
               predicate.test(r2.getY(), r1.getY());
    }

    public enum Adjacent
    {
        PROPER, PARTIAL, SUB, NONE;
    };
}
