package nuvalence.code;

/**
 * java -cp target/rectangle-1.0-SNAPSHOT.jar nuvalence.code.App
 *
 *
 */
 
import nuvalence.code.dto.*;
import java.util.function.*;
import java.util.AbstractMap.*;
import java.util.*;

public class App 
{
    public static void main( String[] args )
    {
        demo1();
        
    }
    
    private static void demo1()
    {
        Rect r1 = new Rect( 2, 2, 2, 1 ); // contained rect 
        Rect r2 = new Rect( 1, 1, 4, 4 ); // container rect
        Rect r3 = new Rect( 7, 2, 1, 3 ); // detached rect
        Rect r4 = new Rect( 2, 2, 3, 7 ); // overlapping rect
        Rect r5 = new Rect( 5, 2, 1, 3 ); // touching rect

        //System.out.println("Perimeter coords: " + r2.getPerimeterCoordSet() );
        //System.out.println("Internal coords: " + r2.getAllCoordSet() );
        System.out.println("Is r2 and r4 intersect: " + isIntersection(r4, r2) );
         System.out.println("r2 and r4 overlapped: " + isOverlapped(r4, r2) );
        //System.out.println("r2 and r4 intersection: " + getIntersection(r4, r2) );
        System.out.println("Is r1 contained in r2: " + isContained(r1, r2) );
        //System.out.printf("r3 all coord set %s\n", r3.getAllCoordSet());
        System.out.println("Is r3 detached from r2: " + isDetached(r3, r2) );
        System.out.println("Is r1 detached from r2: " + isDetached(r1, r2) );
        
        System.out.println("Is r2 and r5 touching: " + isTouching(r5, r2) );
        System.out.println("Is r2 and r5 intersect: " + isIntersection(r5, r2) );
        System.out.println("r2 and r5 intersection: " + getIntersection(r5, r2) );
        System.out.println("r2 and r5 overlapped: " + isOverlapped(r5, r2) );
    }
    
    public static Set<SimpleImmutableEntry<Integer,Integer>> getIntersection( Rect r1, Rect r2 )
    {
        Set<SimpleImmutableEntry<Integer,Integer>> intersection = new HashSet<>();
        Set<SimpleImmutableEntry<Integer,Integer>> r2AllCoordSet = r2.getPerimeterCoordSet();
        
        for( SimpleImmutableEntry<Integer,Integer> e : r1.getPerimeterCoordSet() )
        {
            if( r2AllCoordSet.contains( e )){ intersection.add( e ); }
        }
        
        return intersection;
    }

    public static boolean isIntersection( Rect r1, Rect r2 )
    {
        Set<SimpleImmutableEntry<Integer,Integer>> intersection = new HashSet<>(r1.getPerimeterCoordSet());
        return intersection.retainAll(r2.getPerimeterCoordSet());
    }
    
    public static boolean isContained( Rect r1, Rect r2 )
    {
        Set<SimpleImmutableEntry<Integer,Integer>> intersection = new HashSet<>(r2.getAllCoordSet());
        return intersection.containsAll(r1.getAllCoordSet());
    }
    
    public static boolean isDetached( Rect r1, Rect r2 )
    {
        return ! (isContained( r1, r2 ) || getIntersection( r1, r2 ).size() > 0);
    }
    
    public static boolean isOverlapped( Rect r1, Rect r2 )
    {
        Set<SimpleImmutableEntry<Integer,Integer>> intersection = new HashSet<>(r1.getInternalCoordSet());
        return intersection.retainAll(r2.getInternalCoordSet());
    }
    
    public static boolean isTouching( Rect r1, Rect r2 )
    {
        return isIntersection( r1, r2 ) && ! isOverlapped( r1, r2 );
    }
         
}

/*
// set of methods to check whether Rect r1's corner coordinates are inside of Rect r2

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
*/