#!/usr/bin/env /usr/bin/python3

import sys
import argparse
import sqlite3
import fileinput
import traceback

parser = argparse.ArgumentParser()

parser.add_argument( '-data', dest='dataFile', help='data file', required=True, metavar="FILE" )

def createdb(fields : list):
    s = "','".join(fields)
    sql = f"CREATE TABLE test('{s}')"
    # print(sql)
    dbname = ':memory:' #'hier.db'
    conn = sqlite3.connect(dbname, check_same_thread = False)
    conn.execute(sql)
    conn.commit()
    return conn

def hierachicalsort(path, sep):
    rows = []
    cnt = 0
    try:
        fieldcnt = 0
        for line in fileinput.input(path):
            l = line.strip('\n')
            cnt += 1
            if cnt < 2:
                fields = l.split(sep)
                fieldcnt = len(fields)
                cur = createdb(fields)
                continue
            else:
                rows.append(l.split(sep))
        valsql = "?," * fieldcnt
        inssql = f'insert into test values({valsql.rstrip(",")})'
        #print(inssql)
        #print(rows)
        cur.executemany(inssql, rows)
        cur.commit()
        idx = fields.index('net_sales')
        fldsnippet = ','.join([fields[x] for x in range(len(fields)) if x+1 < idx])
        srtsql = \
        f'''
        with t as
        (
            SELECT *, row_number() OVER (PARTITION BY {fldsnippet} ORDER BY {fldsnippet} asc,cast(net_sales as digital) desc) AS idx
            FROM test order by {fldsnippet}, idx asc
        )
        select {",".join(fields)} from t;
        '''
        #print(srtsql)

        recs = cur.execute(srtsql)
        print(sep.join(fields))
        for rec in recs:
            print(sep.join(rec))

    except Exception as e:
        print(f'Exception {e}')
        traceback.print_exception(*sys.exc_info())
    finally:
        if cur != None:
            cur.close()


def main(args):
    opts = parser.parse_args()
    hierachicalsort(opts.dataFile, '|')


#==============================================================================
if __name__ == '__main__':
    import sys
    sys.exit(main(sys.argv))

