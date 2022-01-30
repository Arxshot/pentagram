#!/bin/sh

echo "Encoding"

# Remove 5 seconds, crop the video 
ffmpeg -i PentagramDemo.mov -ss 5 -filter:v "crop=560:550:100:20" PentagramDemoCropped.mov

# Extract a pallete for png colors
ffmpeg -i PentagramDemoCropped.mov -filter_complex "scale=w=480:h=-1:flags=lanczos, palettegen=stats_mode=diff" PentagramDemoCropped.png

# Subsample every 20th frame to speed up gif
ffmpeg -i PentagramDemoCropped.mov -vf "select=not(mod(n\,20))" -vsync vfr -q:v 2 image_%03d.png

# Re-encode as a gif using palette
ffmpeg -framerate 20 -i image_%03d.png -i PentagramDemoCropped.png -filter_complex "[0]scale=w=400:h=-1[x];[x][1:v] paletteuse" -pix_fmt rgb24 PentagramDemo.gif


