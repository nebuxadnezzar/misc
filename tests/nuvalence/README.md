Assignment given by Nuvalence
=============================

## Create algorithm detecting different positioning of rectangles.

Problem
--------

There are several rectangles on the plane. Detect whether they are overlapping, detached, side touching or contained within each other. Create tests as well.

Approach
--------

I created Rect object that would take 4 constructor parameters: x coordinate, y coordinate, height, width. Then I represented each side of a rectangle as set of coordinate points. Thus I calculated intersection, containment, detachment as relations between different coordinate point sets.