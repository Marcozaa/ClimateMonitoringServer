package com.example.climatemonitoringserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientTest {

    void exec() throws IOException, ClassNotFoundException {
        InetAddress addr = InetAddress.getByName(null); // Localhost
        System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, 2345);

        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            String command = "";
            while (!command.equals("exit")) {
                System.out.println("Enter a command (insertAreaOfInterest, registerOperator, validateUser, exit): ");
                command = scanner.nextLine();
                switch (command) {
                    case "insertAreaOfInterest":
                        System.out.println("Enter the area name:");
                        String nome = scanner.nextLine();
                        System.out.println("Enter the state:");
                        String stato = scanner.nextLine();
                        System.out.println("Enter the latitude:");
                        double latitudine = scanner.nextDouble();
                        System.out.println("Enter the longitude:");
                        double longitudine = scanner.nextDouble();
                        scanner.nextLine(); // Consuma la newline rimasta dopo nextDouble()

                        out.writeObject("insertAreaOfInterest");
                        out.writeObject(nome);
                        out.writeObject(stato);
                        out.writeObject(latitudine);
                        out.writeObject(longitudine);

                        // Receiving server response
                        String response = (String) in.readObject();
                        System.out.println("Server response: " + response);
                        break;

                    case "registerOperator":
                        System.out.println("Enter the username:");
                        String username = scanner.nextLine();
                        System.out.println("Enter the password:");
                        String password = scanner.nextLine();

                        out.writeObject("registerOperator");
                        out.writeObject(username);
                        out.writeObject(password);

                        // Receiving server response
                        response = (String) in.readObject();
                        System.out.println("Server response: " + response);
                        break;

                    case "validateUser":
                        System.out.println("Enter the username:");
                        String user = scanner.nextLine();
                        System.out.println("Enter the password:");
                        String pass = scanner.nextLine();

                        out.writeObject("validateUser");
                        out.writeObject(user);
                        out.writeObject(pass);

                        // Receiving server response
                        boolean isValid = (boolean) in.readObject();
                        System.out.println("User validation result: " + isValid);
                        break;

                    case "exit":
                        System.out.println("Exiting the client.");
                        break;

                    default:
                        System.out.println("Unknown command. Please try again.");
                        break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            socket.close(); // Ensure the socket is closed
        }
    }

    public static void main(String[] args) {
        try {
            new ClientTest().exec();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
