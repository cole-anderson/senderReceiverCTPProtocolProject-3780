INSTRUCTIONS FOR EXECUTION:

1.  Open TWO terminals in the directory(coleliamcheckpoint1)

2.  First compile class files(type the following in the command line)
    javac Receiver.java
    javac Sender.java

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
