import sys
import os
import traceback
import re
import fileinput
import locale
#from datetime import datetime, date
import argparse
import time

if sys.version_info[0] > 2:
   xrange = range

#sys.stderr = sys.stdout
parser = argparse.ArgumentParser()
parser.add_argument( '-skiphdr', action='store_const', const=True, default=False, dest='skipHeader', help='skip header, default: false' )
parser.add_argument( '-separator', dest='sep', type=str, default=",", help='field separator. Default: ,', metavar="SYMBOL" )
parser.add_argument( '-plans', dest='plansFile', nargs=1, help='plans.csv input file', required=True, metavar="FILE" )
parser.add_argument( '-zips', dest='zipsFile', nargs=1, help='zips.csv input file', required=True, metavar="FILE" )
parser.add_argument( '-slcsps', dest='slcspsFile',nargs=1, help='slcsp.csv file', required=True, metavar="FILE" )
parser.add_argument( '-planLevel', dest='level', type=str, default='Silver', help='plan level, valid values: Gold, Silver, Bronze, Platinum. Default - Silver', metavar='STRING' )

locale.setlocale( locale.LC_ALL, '')

level_rx = None;

#==============================================================================
class SlcspStates(object):
    def __init__(self, statecodes ):
         self.statecodes = statecodes  # statecodes is set
    def __str__(self):
        return '%s ' % (self.statecodes);
    def __call__(self, rec ):
        return ( rec[1] in self.statecodes and is_level( rec ) );
        
#==============================================================================
class SlcspZips(object):
    def __init__(self, zipcodes ):
         self.zipcodes = zipcodes  # zipcodes is set
    def __str__(self):
        return '%s ' % (self.zipcodes);
    def __call__(self, rec ):
        return rec[0] in self.zipcodes;

#==============================================================================
def is_level( rec ):

   match  = re.findall( level_rx, rec[2], re.DOTALL | re.I )
   return ( match and len( match ) > 0 )

#==============================================================================
def create_rate_map( zip_code_data ):

    d = dict();  # rates map
    s = set();   # zips with multiple rate areas

    for r in zip_code_data:
        zc = r[0]            # zip code
        rc = r[4]            # rate code

        if( zc not in d ):
            d[zc] = ( r[1], rc )
        else:
            if( rc != d[zc] ):
                s.add( zc )

    for zc in s: del d[zc]
    return d;

#==============================================================================
def extract_zip_states( zips, zipcodes ):

    s = set();
    
    for r in zips:
        if( r[0] in zipcodes ): s.add( r[1] );
        
    return SlcspStates( s );

#==============================================================================
def filter_plans_by_zip( plans, zips ):

    out = [];
    
    #for r in plans:    #     if( r[1] in zips
#==============================================================================
def extract_record( data, sep, size ):

    rec = data.strip().split( sep );
    if( len( rec ) < size ):
        return [];
    return rec;

#==============================================================================
def get_records( file_name, separator, record_size, filtering_callback ):

    out = [];
    record_count = 0;

    sys.stderr.write( "\nFile {}\n".format( file_name ) )

    try:

        for line in fileinput.input( file_name ):

            record_count += 1;

            if( options.skipHeader and fileinput.isfirstline() ):
               continue

            r = extract_record( line, separator, record_size );

            if( record_count % 250 == 0 ):
                sys.stderr.write( "\rrecord count: " + '{:8}'.format( record_count ) )
            if( len( r ) < record_size ):
                sys.stderr.write( "\n\ninvalid record on line: " + str( record_count ) )
                continue

            if( filtering_callback != None ):

                if( filtering_callback( r ) ):
                    out.append( r );
            else:
                out.append( r );

        sys.stderr.write( "\rrecord count: " + '{:8}'.format( record_count ) )
        sys.stderr.write( "\n" )
    except Exception as e:
        print( e );
        print( "FAILED ON RECORD # %d" % ( record_count ));
        print( traceback.format_exc() )

    return out;

#==============================================================================
def search_plans( plans, idx, val ):
    min = 0
    max = len(plans) - 1
    while True:
        if max < min:
            return -1
        m = (min + max) // 2
        if plans[m][idx] < val:
            min = m + 1
        elif plans[m][idx] > val:
            max = m - 1
        else:
            return m
#==============================================================================
def get_rates_for_the_zip( zip, rate_map, plans ):

    out = [];
    
    if( zip in rate_map ):
        ( state, rate ) = rate_map[zip]
        i = search_plans( plans, 1, state )
        #print( "---> %s %s" % ( state, plans[i] ) )

        while( state == plans[i][1] ):

            plan = plans[i];
            #print( "-----> %s %s" % ( rate, plan[ 4 ] ) )
            if( rate == plan[ 4 ] ):
                out.append( float( plan[ 3 ] ) )
            i += 1

    return sorted( out );
#==============================================================================
if __name__ == '__main__':

    options = parser.parse_args()
    t0 = time.time()
    plans = [];
    slcsp_set = set();

    level_rx = r'%LEVEL%'.replace( '%LEVEL%', options.level )
    
    slcsp = get_records( options.slcspsFile, options.sep, 2, None )

    for r in slcsp: slcsp_set.add( r[0] );

    slcsp_filter = SlcspZips( slcsp_set );
    
    #-- 1. from zips file filter only those records that we need to check rates against
    #--
    zips  = get_records( options.zipsFile, options.sep, 5, slcsp_filter );
    
    #-- 2. from plans file filter only records with requested plan level: Silver, Gold etc
    #--    also filter specific states defined by zip codes
    #--
    state_filter = extract_zip_states( zips, slcsp_set );
    plans = sorted( get_records( options.plansFile, options.sep, 5, state_filter ), key=lambda x: x[1]+x[4] )
    
    #-- 3. create rate map in format zip: ( state_code: rate_code )
    #--    remove from processing all zip codes that have more than 1 plan rate asigned
    #--
    rate_map = create_rate_map( zips );

    for r in slcsp:
        zip = r[ 0 ]
        
        rates = get_rates_for_the_zip( zip, rate_map, plans )

        if( len( rates ) > 1 ):
            print( "%s,%s" % ( zip, rates[1] ) )
        else:
            print( '%s, ' % ( zip ) );
    
    t1 = time.time()

    sys.stderr.write( '\nTime: %d secs' % ( t1-t0 ) )

    '''
    #print( state_filter );
    for r in plans: print( "P=> %s" % r )
    zips.sort();
    for r in zips: print( "Z=> %s" % r )
    for k,v in rate_map.iteritems(): print( "R=> %s %s" % (k, v) );

    '''
    '''
    python slcsp.py -skiphdr -plans plans.csv -zips zips.csv -slcsps slcsp.csv
    '''