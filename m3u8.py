## m3u8.py will parse an m3u8 playlist and remove files which aren't referenced in that playlist.

import os
import sys
import time
import csv

def main():
    print 'm3u8.py running\n'
    time.sleep(60)
    www = '/var/www'
    live = www + '/live'
    m3u8 ='/var/www/live.m3u8'
    old_playlist = []
    while True:
       loc = os.path.abspath(live)
        segments = []
      
        for files in os.listdir(loc):
            if files.endswith('TS') or files.endswith('ts'):
                segments.append(files)

        with open(m3u8,'rb') as rawFile:
            #Read in CSV object
            newData = csv.reader(rawFile)
            dataList = []
            dataList.extend(newData)
            playlist = []
            for rows in dataList:
                data = rows
                if rows[0][-3:] == '.ts':
                    data  = rows[0][5:]
                    #print data
                    #print ' '
                    playlist.append(data)
            # print playlist

        if old_playlist == playlist:
            print 'stream failed'
            if segments:
                for files in segments:
                    segment = live + '/' + files
                    try:
                        os.remove(segment)
                    except OSError:
                        print 'Cannot remove ' + files
                try:
                    os.remove(m3u8)
                except OSError:
                    print 'cannot remove playlist'
            sys.exit()

        for files in segments:
            if not files in playlist:
                segment = live + '/' + files
                try:
                    os.remove(segment)
                except OSError:
                    print 'OSError'

        old_playlist = playlist
        time.sleep(60)

if __name__ == "__main__":
    main()