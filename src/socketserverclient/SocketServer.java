package socketserverclient;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SocketServer{
    public final static int PORT = 9000;
    private static ServerSocket server;
    private static Socket serverMessager;
    private static DataInputStream  clientReader;
    private static DataOutputStream clientWriter;
    private static String serverMessage = "";
    private static String clientMessage = "";
    
    private static void initialize(){
        try {
            server = new ServerSocket(PORT);
            System.out.println("Opening server : completed [on port:" + PORT + "]");
            serverMessager = server.accept();
            System.out.println("Accept client [local address:" + serverMessager.getLocalAddress() + "]");
            clientReader = new DataInputStream(serverMessager.getInputStream());
            clientWriter = new DataOutputStream(serverMessager.getOutputStream());
        } catch (IOException error) {
            System.err.println(error.getMessage());
        }
    }
    
    private static void reconnect(){
        try {
            serverMessager = server.accept();
            System.out.println("Accept client [local address:" + serverMessager.getLocalAddress() + "]");
            clientReader = new DataInputStream(serverMessager.getInputStream());
            clientWriter = new DataOutputStream(serverMessager.getOutputStream());
            System.out.println("Reconnect completed");
        } catch (IOException error) {
            System.err.println(error.getMessage());
        }
    }
    
    public static void sentMessage(String message){
        try {
            clientWriter.writeUTF(message);
            System.out.println("Massage to client : " + message);
        } catch (IOException error) {
            System.err.println(error.getMessage());
            reconnect();
        }
    }
    
    public static void main(String[] args) {
        initialize();
        Scanner messageScanner = new Scanner(System.in);
        try {
            while(!serverMessage.equals("Close")){
                try{
                    clientMessage = clientReader.readUTF();
                    System.out.println("Message from client : " + clientMessage);
                    System.out.print("Input message to client >> ");
                    serverMessage = messageScanner.nextLine();
                    sentMessage(serverMessage);
                } catch(IOException error){
                    System.err.println(error.getMessage());
                    reconnect();
                }
            }
            serverMessager.close();
            server.close();
        } catch (IOException error) {
            System.err.println(error.getMessage());
        }
    }
    
}
