package pwr.edu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;

public class Runnable implements java.lang.Runnable {
    private final BufferedReader input;

    public Runnable(Socket socket) throws IOException {
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            while (true) {
                String response = input.readLine();
                System.out.println(response);
            }
        } catch (IOException exception) {
            System.out.println("Runnable loop error ocured: " + Arrays.toString(exception.getStackTrace()));
        } finally {
            try {
                input.close();
            } catch (Exception exception) {
                System.out.println("input close error ocured: " + Arrays.toString(exception.getStackTrace()));
            }
        }
    }
}


