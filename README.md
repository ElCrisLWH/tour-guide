This README will present the project, the code and how to run it.

## Tour Guide Robot

This code programs a Tour Guide Lego Mindstorms prototype, which follows and remembers a path drawn by a black line in a white font. Then it is capable of repeating the same path without the drawn path.

The robot is composed of a Lego Mindstorms NXT Brick with two Light Sensors at the front and two Motors as wheels.

![Lego Mindstorm Robot](/images/tour_guide.jpg)

![Path Example](/images/path.jpg)

## The code

This repository contains a `TourGuide.java` file, which when added to the NXT Brick begins a calibration of colors, then follows the black line until the end of the line identified by detecting black on both sensors.

To store and retrieve the moves in constant time but with unbounded memory, a linked-list stack is implemented. But in order to replicate the line, the stack is reversed before the replication. To avoid this, a queue might have been implemented, but in an array it would have had constant memory, and in a linked list either the queue or the dequeue would have taken linear time to get to the end of the queue. Prioritizing the running time performance, the linked-list stack implementation was preferred.

## How to run it

### Setup

Before modding, make sure to setup your environment and personalize your proyect.

1. Download [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/).
2. Download an IDE: [Visual Studio Code](https://code.visualstudio.com/) (or [Eclipse](https://www.eclipse.org/downloads/) or [IntelliJ](https://www.jetbrains.com/idea/download/#section=windows)).
3. Open the project from your IDE and download Java extensions.

Java basic knowledge might help understanding and developing.

### NXT Bricks

A full guide to program NXT Bricks can be found [here](https://lejos.sourceforge.io/nxt/nxj/tutorial/index.htm).
