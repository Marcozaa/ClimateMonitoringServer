package com.example.climatemonitoringserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static final int PORT = 2345;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter database host: ");
        String dbHost = scanner.nextLine();

        System.out.print("Enter database username: ");
        String dbUsername = scanner.nextLine();

        System.out.print("Enter database password: ");
        String dbPassword = scanner.nextLine();

        Database iDatabase = new Database(dbHost, dbUsername, dbPassword);
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(PORT);
            System.out.println("Server ready");
            while (true) {
                Socket cliSocket = ss.accept();
                new Thread(new ServerThread(cliSocket, iDatabase)).start();
                //cliSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ss != null) {
                    ss.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
