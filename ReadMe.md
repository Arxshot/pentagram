# Pentagram Generation

![alt text](https://raw.githubusercontent.com/Arxshot/pentagram/main/doc/PentagramDemo.gif)

Create the pentagram animation in Java Canvas like above demo shows.

Looks best if using prime numbers between 5 and 23. The image above is using 7 to feed the number of points. 

The rendering uses random to create a fuzzy red sand line. The sparkle effect is created with a velocity in the opposite direction to that of travel. There is about 1000 sparkle entities which are rendered to the canvas each time with an increasing transparency the longer they have existed so they fade out and naturally disappear. 