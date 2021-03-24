INSTRUCTIONS FOR EXECUTION:
(requires a java compiler installed)
Run the following command in terminal and expect some sort of enviroment to be installed as the following commands show
java -version
$openjdk version "11.0.9.1" 2020-11-04
$OpenJDK Runtime Environment (build 11.0.9.1+1-Ubuntu-0ubuntu1.20.04)
$OpenJDK 64-Bit Server VM (build 11.0.9.1+1-Ubuntu-0ubuntu1.20.04, mixed mode, sharing)

#///CHECKPOINT 1:
1.  Open TWO terminals in the directory(coleliamcheckpoint1)

2.  First compile class files(type the following in the command line)
    javac Receiver.java & javac Sender.java

3.  Run the following command in EACH of the terminal windows(Receiver first)(remember capitals for Java files)
    [for Receiver accepted arguments]:
    java Receiver -f [filehere] [port#here]
    java Receiver [port#here]

    Examples:
    java Receiver -f example.txt 64321
    java Receiver 64321

    [for Sender accepted arguments]:
    java Sender -f [filehere] [ipaddresshere] [port#here]
    java Sender [ipaddresshere] [port#here]

    Examples:
    java Sender -f example.txt 127.0.0.1 64321
    java Sender 127.0.0.1 64321

4.  Either the file specified will be sent from Sender to Receiver or the user
    can type a message on the Sender side. That file contents or message will then
    by either displayed or written into a file on the Receiver side
    
    
///CHECKPOINT2:
#COMMAND FOR CHECKPOINT 2:
#This allows for running of tests via command line

#COMMAND1: (COMPILE)
javac -cp junit-4.12.jar:. AppTest.java Header.java Packet.java Receiver.java Sender.java 

#COMMAND2: (RUNTESTS)
java -cp junit-4.12.jar:hamcrest-core-1.3.jar:. org.junit.runner.JUnitCore AppTest.java

#COMMAND3: (CLEANUP UGLY .CLASS FILES)
rm *.class
