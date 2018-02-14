package t;

/*
    to compile javac -d . *.java
    to run     java t.DfsRouter
*/

import java.util.*;
import java.util.AbstractMap.*;

public class DfsRouter
{
    private static Map<String,Node> buildGraph()
    {
       Map<String,Node> graph = new TreeMap<String,Node>();

       for( String s : _conveyorData )
       {
            String [] ss = s.split( " " );
            int time = Integer.parseInt( ss[2] );
            Node n1 = getGraphNode( graph, ss[0] ),
                 n2 = getGraphNode( graph, ss[1] );
            n1.addLink( n2, time );
            n2.addLink( n1, time );
       }

       return graph;
    }
    //-------------------------------------------------------------------------
    private static Map<String,String> getStartAndDestination( String bagsString )
    {
        Map<String,String> m = new HashMap<String,String>();
        String [] ss = bagsString.split( " " );
        String flightId = ss[2];
        m.put( "bagId", ss[0] );
        m.put( "start", ss[1] );
        m.put( "end", ss[2] );

        for( String s: _departures )
        {
            if( s.startsWith( flightId ) )
            {
                m.put( "end", s.split( " " )[1] );
                break;
            }
        }

        return m;
    }
    //-------------------------------------------------------------------------
    private static Node getGraphNode( Map<String,Node> graph, String id )
    {
        Node n = graph.get( id );
        if( n == null )
        {
            n = new Node( id );
            graph.put( id, n );
        }
        return n;
    }
    //-------------------------------------------------------------------------
    public static void main( String [] args )
    {
        ArrayDeque<SimpleImmutableEntry<String,Integer>> st = new ArrayDeque<SimpleImmutableEntry<String,Integer>>();
        Map<String,Node> graph = buildGraph();
        //System.out.println( graph );

        StringBuilder sb = new StringBuilder();

        for( String s : _bagsData )
        {
            Map<String,String> m = getStartAndDestination( s );
            System.out.println( m );

            Node start = graph.get( m.get( "start" ) ),
                 end   = graph.get( m.get( "end" ) );

            start.findNode( end, start, 0, st );

            //System.out.println( st );

            int cnt = 0;

            sb.append( String.format( "%s ", m.get( "bagId" ) ) );

            while( ! st.isEmpty() )
            {
                SimpleImmutableEntry<String,Integer> e = st.poll();
                cnt += e.getValue();
                sb.append( String.format( "%s ", e.getKey() ) );
            }
            sb.append( String.format( " : %d\n", cnt ) );

            st.clear();
        }
        System.out.println( "=============================" );
        System.out.println( sb.toString());
    }
    //-------------------------------------------------------------------------
    private static String [] _bagsData     = new String[]
    {
        "0001 Concourse_A_Ticketing UA12",
        "0002 A5 UA17",
        "0003 A2 UA10",
        "0004 A8 UA18",
        "0005 A7 BaggageClaim"
    };

    private static String [] _departures   = new String[]
    {
        "UA10 A1 MIA 08:00",
        "UA11 A1 LAX 09:00",
        "UA12 A1 JFK 09:45",
        "UA13 A2 JFK 08:30",
        "UA14 A2 JFK 09:45",
        "UA15 A2 JFK 10:00",
        "UA16 A3 JFK 09:00",
        "UA17 A4 MHT 09:15",
        "UA18 A5 LAX 10:15"
    };
    private static String [] _conveyorData = new String[]
    { "Concourse_A_Ticketing A5 5", "A5 BaggageClaim 5",
      "A5 A10 4", "A5 A1 6","A1 A2 1", "A2 A3 1", "A3 A4 1",
      "A10 A9 1","A9 A8 1", "A8 A7 1", "A7 A6 1"
    };
}

