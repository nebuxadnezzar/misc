package nuvalence.code.dto;

import java.util.AbstractMap.*;
import java.util.*;

public class Rect
{
    private int x; // horizontal coordinate of lower left corner
    private int y; // vertical coordinate of lower left corner
    private int h; // hight
    private int w; // width
    
    public Rect( int x, int y, int height, int width )
    {
        validate(x, y, height, width);
        this.x = x;
        this.y = y;
        this.h = height;
        this.w = width;
    }
    
    public int getX(){ return x; }
    public int getY(){ return y; }
    public int getHeight(){ return h; }
    public int getWidth(){ return w; }
    public int[] toArray(){ return new int[]{x, y, x + w, y + h}; }
    
    /*
        x -> w, y; x, y -> h; x -> w, y + h; x + w, y -> h 
    */
    public Set<SimpleImmutableEntry<Integer,Integer>> getLeftHorizontalCoordSet()
    {
        Set<SimpleImmutableEntry<Integer,Integer>> coords = new LinkedHashSet<>();
        for( int i = x; i <= x + w; i++ )
           { coords.add( new SimpleImmutableEntry<Integer,Integer>(i, y) ); }
        return coords;   
        
    }
    
    public Set<SimpleImmutableEntry<Integer,Integer>> getLeftVerticalCoordSet()
    {
        Set<SimpleImmutableEntry<Integer,Integer>> coords = new LinkedHashSet<>();
        for( int i = y; i <= y + h; i++ )
           { coords.add( new SimpleImmutableEntry<Integer,Integer>(x, i) ); }
        return coords;   
    }
    
    public Set<SimpleImmutableEntry<Integer,Integer>> getRightHorizontalCoordSet()
    {
        Set<SimpleImmutableEntry<Integer,Integer>> coords = new LinkedHashSet<>();
        for( int i = x; i <= x + w; i++ )
           { coords.add( new SimpleImmutableEntry<Integer,Integer>(i, y + h) ); }
        return coords;   
        
    }
    
    public Set<SimpleImmutableEntry<Integer,Integer>> getRightVerticalCoordSet()
    {
        Set<SimpleImmutableEntry<Integer,Integer>> coords = new LinkedHashSet<>();
        for( int i = y; i <= y + h; i++ )
           { coords.add( new SimpleImmutableEntry<Integer,Integer>(x + w, i) ); }
        return coords;   
    }
    
    public Set<SimpleImmutableEntry<Integer,Integer>> getPerimeterCoordSet()
    {
        Set<SimpleImmutableEntry<Integer,Integer>> coords = new LinkedHashSet<>(getLeftHorizontalCoordSet());
        coords.addAll(getLeftVerticalCoordSet());
        coords.addAll(getRightHorizontalCoordSet());
        coords.addAll(getRightVerticalCoordSet());
        return coords;
        
    }
    
    public Set<SimpleImmutableEntry<Integer,Integer>> getAllCoordSet()
    {
        Set<SimpleImmutableEntry<Integer,Integer>> coords = new LinkedHashSet<>(getPerimeterCoordSet());
        coords.addAll(getInternalCoordSet());
        return coords;
        
    }

    public Set<SimpleImmutableEntry<Integer,Integer>> getInternalCoordSet()
    {
        Set<SimpleImmutableEntry<Integer,Integer>> coords = new LinkedHashSet<>();
        for( int i = x + 1, k = x + w; i < k; i++)
        {
            for( int ii = y + 1, kk = y + h; ii < kk; ii++)
               { coords.add( new SimpleImmutableEntry<>( i , ii)); }
        }
        return coords;
    }

    private void validate( int x, int y, int height, int width )
    {
        int [] ary = {x, y, height, width};
        
        for( int i : ary )
          { if( i < 0 ){ throw new IllegalArgumentException("Negative argument passed"); } }
    }
}