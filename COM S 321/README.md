# COM S 321 - Intro to Computer Architexture and Machine-Level Programming

There was not a lot of coding assignments during this course. Included are the only 2 coding assignments. One assignment in Assembly, specifically Legv8 subset, and one assignment converting compiled binary code to assembly, my partner and I chose to do this assignment in C.

Outside of these 2 assignments, there were many written homeword assignments and exams testing us on the Assembly (Legv8) language. These assignments are not included.

## Assignment 1

This assignment was to write a Selection Sorting algorithm using Legv8 Assembly code. This assignment is compiled using the provided emulator legv8emul, created by course instructor. 

In order to run this code (must be in linux os or wsl) execute this command:

`./legv8emul assignment1.legv8asm`

The output of this command will display the registers and data stored in memory and end of execution followed by the code ran, and extra data.

## Assignment 2

On the flip of the previous assignment, this assignment took compiled binary and the goal was to convert the binary to Legv8 Assembly. This assignment was not perfect and was done before my partner and I had learned the C language.

In order to run this code (runs in linux or wsl) execute these command:
- `./build.sh`
- `./run.sh <binary file name>` | for provided file: `./run.sh testCase.leg8asm.machine`

The provided `testCase.legv8asm.machine` file is a compiled test code that contains all assembly commands required for the assignment.