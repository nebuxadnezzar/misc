package t;

import java.util.*;
import java.util.AbstractMap.*;

//-----------------------------------------------------------------------------
class Node
{
    private List<Link> _links;
    private String     _id;

    public Node( String id )
    {
        _id = id;
        _links = new ArrayList<Link>();
    }

    public String getId(){ return _id; }

    public void addLink( Node n, int minutes )
    {
        if( ! ( _id.equals( n.getId() ) || _links.contains( n ) ) )
          { _links.add( new Link( n, minutes) ); }
    }

    public boolean findNode( Node n, Node prior, int time, ArrayDeque<SimpleImmutableEntry<String,Integer>> st )
    {
        if( n == null )
          { return false; }

        // direct match
        //
        if( _id.equals( n.getId() ) )
        {
            st.push( new SimpleImmutableEntry<String,Integer>( _id, 0 ) );
            return true;
        }

        // see if final destination point is already present in links
        //
        for( Link l : _links )
        {
            Node nn = l.getNode();
            if( nn.getId().equals( n.getId() ) )
            {
                st.push( new SimpleImmutableEntry<String,Integer>( nn.getId(), l.getTime() ) );
                st.push( new SimpleImmutableEntry<String,Integer>( _id, 0 ) );
                return true;
            }
        }

        // no direct connection with the end point, keep scanning
        //
        for( Link l : _links )
        {
            Node nn = l.getNode();
            if( nn.getId().equals( prior.getId() ) ) continue;

            if( nn.findNode( n, this, l.getTime(), st ) )
            {
                st.push( new SimpleImmutableEntry<String,Integer>( _id, l.getTime() ) );
                return true;
            }
        }

        return false;
    }
    public String toString()
    { return String.format( "{%s=%s} ", _id, _links ); }
}