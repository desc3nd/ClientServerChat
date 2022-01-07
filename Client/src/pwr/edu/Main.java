package pwr.edu;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 6666)) {
            String input = "";
            Scanner scanner = new Scanner(System.in);
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            Runnable run = new Runnable(socket);
            new Thread(run).start();
            while (true) {
                input = scanner.nextLine();
                output.println(input);
                if (input.equals("quit")) {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("client main: " + Arrays.toString(e.getStackTrace()));
        }
    }

}

