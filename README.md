INSTRUCTIONS FOR EXECUTION:
(requires a java compiler installed)
Run the following command in terminal and expect some sort of enviroment to be installed as the following commands show
java -version
$openjdk version "11.0.9.1" 2020-11-04
$OpenJDK Runtime Environment (build 11.0.9.1+1-Ubuntu-0ubuntu1.20.04)
$OpenJDK 64-Bit Server VM (build 11.0.9.1+1-Ubuntu-0ubuntu1.20.04, mixed mode, sharing)

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
