## HTTP downloader

A console utility for downloading files by *HTTP* protocol

### Incoming parameters

Required:
```
-f file path with links (details below)
-o destination folder path for downloaded files
```

Optional:
```
-n threads quantity for simultaneous download (without this parameter will be used max available threads of the processor)
-l speed limit for one thread (details below)
```

Examples:
```
-f C:\folder\links.txt -o C:\files
-f C:\folder\links.txt -o C:\files -n 10 -l k=1024
```

File format with links:
```
<HTTP link><whitespace><file name what should be saved (optional)>
*if filenames aren't filled they will be '1', '2', '3' etc.
```

Examples:
```
with filenames:
http://example.com/archive.zip my_archive.zip
http://example.com/image.jpg picture.jpg
 
without filenames:
http://example.com/archive.zip
http://example.com/image.jpg
```

Speed limit:
```
dimension - byte/second, suffixes 'k', 'm' can be used (uppercase also)
k - kilobyte
m - megabyte
```

Examples:
```
k=1024 - downloading speed will be 1024 kilobytes per second
m=5 - downloading speed will be 5120 kilobytes or 5 megabytes per second
```

Original technical task (russian): [Console-downloader](https://github.com/Ecwid/new-job/blob/master/Console-downloader.md)
