package t;

//-----------------------------------------------------------------------------
class Link
{
    private Node _n;
    private int  _t;
    public Link( Node n, int minutes )
    { _n = n; _t = minutes; }

    public Node getNode(){ return _n; }
    public int getTime(){ return _t; }

    public String toString()
    { return String.format( "<%s %d> ", _n.getId(), _t ); }
}
