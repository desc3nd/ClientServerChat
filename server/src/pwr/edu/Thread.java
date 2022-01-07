package pwr.edu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class Thread extends java.lang.Thread {
    private final ArrayList<Thread> threadList;
    private final Socket socket;
    private String channel;
    private String name;
    private final PrintWriter output;
    private final BufferedReader input;
    private List<String> channels = new ArrayList<>();
    private final Logger logger;

    public Thread(ArrayList<Thread> threads, Socket socket, Logger logger) throws IOException {
        this.threadList = threads;
        this.socket = socket;
        this.logger = logger;
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            setName();
            setUpChannel();
        } catch (IOException exception) {
            System.out.println("Setup channels and names error occurs: " + Arrays.toString(exception.getStackTrace()));
        }
        try {
            while (true) {
                String outputData = input.readLine();
                if (outputData.equals("quit")) {
                    socket.close();
                    threadList.remove(this);
                    logger.info("removed thread called " + name + " more info: " + this);
                    printToALlThreadsInChannel("server", name + " has left the channel");
                    break;
                }
                printToALlThreadsInChannel(name, outputData);
            }
        } catch (Exception exception) {
            System.out.println("Conversation error occur: " + Arrays.toString(exception.getStackTrace()));
            threadList.remove(this);
            logger.warning("removed thread in catch method: " + name + " more info: " + this);
            printToALlThreadsInChannel("server", name + " has left the channel");
        }
    }

    private void printToALlThreadsInChannel(String user, String outputData) {
        var threadsInChannel = threadList.stream().filter(x -> Objects.equals(x.channel, channel) && !Objects.equals(x.name, name)).toList();
        for (Thread thread : threadsInChannel) {
            thread.output.println(user + ": " + outputData);
        }
    }

    public void setUpChannel() throws IOException {
        output.println("Choose channel or create new one");
        setChannelsList();
        for (var distChannel : channels) {
            if (distChannel != null) {
                output.println("channel: " + distChannel);
                printNamesForChannel(distChannel);
            }
        }
        channel = input.readLine();
        logger.info(name + " join channel " + channel);
        printToALlThreadsInChannel("server", name + " join channel " + channel);
    }

    private void setChannelsList() {
        for (var thread : threadList) {
            channels.add(thread.channel);
        }
        channels = channels.stream().distinct().collect(Collectors.toList());
    }

    public void setName() throws IOException {
        output.println("Set name");
        getUniqueName();
        logger.info("Added new user called: " + name);
    }

    private void getUniqueName() throws IOException {
        var tempName = input.readLine();
        for (var thread : threadList) {
            if (Objects.equals(thread.name, tempName)) {
                output.println("Name already exists");
                getUniqueName();
                return;
            }
        }
        name = tempName;
    }

    private void printNamesForChannel(String distChannel) {
        output.println("Users in channel " + distChannel + ":");
        var threads = threadList.stream().filter(x -> Objects.equals(x.channel, distChannel)).toList();
        for (var thread : threads) {
            output.println(thread.name);
        }
        output.println("---------------");
    }

}
