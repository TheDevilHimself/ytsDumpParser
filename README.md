# ytsDumpParser
A simple Java project to navigate the dump of yts before its closing around the 20/10/15

I won't provide this dump so don't ask. I found a copy of it in the comment section of a torrentFreak's article about popcorntime
The file structure is expected to be

yts_movie.json

-torrents (folder containing torrents)

Generate the jar file and drop it into the same folder as yts_movie.json. Run it, press load films list and wait (should be fast).
Double clicking on a film will recover its data and show it on the right. Clicking open torrent should launch your default
torrent application. The interface it's bad, resize isn't working etc etc, but the basic functionality, search included, is here.

This project use the org.json library



