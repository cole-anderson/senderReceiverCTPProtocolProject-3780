#!/bin/bash

javac -cp lib/junit-4.12.jar:. Header.java Packet.java Receiver.java Sender.java AppTest.java
java -cp lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar:. org.junit.runner.JUnitCore AppTest
#rm *.class

# scuffed makefile 
