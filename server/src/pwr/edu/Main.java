package pwr.edu;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Main {


    public static void main(String[] args) throws IOException {
        ArrayList<Thread> threadList = new ArrayList<>();
        FileHandler handler = new FileHandler("default.log", true);
        Logger logger = Logger.getLogger("com.javacodegeeks.snippets.core");
        logger.addHandler(handler);
        try (ServerSocket serversocket = new ServerSocket(6666)) {
            while (true) {
                Socket socket = serversocket.accept();
                Thread serverThread = new Thread(threadList, socket, logger);
                threadList.add(serverThread);
                serverThread.start();
            }
        } catch (Exception exception) {
            System.out.println("Main error ocured: " + Arrays.toString(exception.getStackTrace()));
        }
    }
}
