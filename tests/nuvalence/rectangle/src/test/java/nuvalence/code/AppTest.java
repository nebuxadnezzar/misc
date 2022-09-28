package nuvalence.code;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import nuvalence.code.*;
import nuvalence.code.dto.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        System.out.println("Running tests");
        Rect r1 = new Rect( 2, 2, 2, 1 ); // contained rect 
        Rect r2 = new Rect( 1, 1, 4, 4 ); // container rect
        Rect r3 = new Rect( 7, 2, 1, 3 ); // detached rect
        Rect r4 = new Rect( 2, 2, 3, 7 ); // overlapping rect
        Rect r5 = new Rect( 5, 1, 5, 7 ); // side touching
        
        try
        {
            Rect bad = new Rect( -1, -1, -1, -1);
        } catch( Exception e ) {
            assertTrue( e instanceof IllegalArgumentException );
        }
        assertTrue( App.isContained( r1, r2));
        assertTrue( App.isDetached( r3, r2));
        assertTrue( App.isOverlapped( r4, r2));
    }
}
