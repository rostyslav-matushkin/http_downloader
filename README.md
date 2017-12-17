## HTTP downloader

A console utility for downloading files by *HTTP* protocol

###Incoming parameters

Required:
```
-f
 
-o
```

Optional:
```
-n
 
-l 
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

Original technical task: [Console-downloader](https://github.com/Ecwid/new-job/blob/master/Console-downloader.md)
