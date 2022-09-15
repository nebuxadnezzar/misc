package nuvalence.code.dto;

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
    
    private void validate( int x, int y, int height, int width )
    {
        int [] ary = {x, y, height, width};
        
        for( int i : ary )
          { if( i < 0 ){ throw new IllegalArgumentException("Negative argument passed"); } }
    }
}