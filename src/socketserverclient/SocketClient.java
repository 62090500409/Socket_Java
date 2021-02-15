package socketserverclient;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class SocketClient{
    private static BufferedReader input;
    private static final int TIME_OUT = 2000;
    private static final int DATA_RATE = 9600;
    public final static int PORT = 9000;
    
    private static Socket clientMessager;
    private static DataOutputStream serverWriter;
    private static DataInputStream serverReader;
    private static String serverMessage = "";
    private static String clientMessage;
    
    private static void initialize(){
        try{
            clientMessager = new Socket(InetAddress.getLocalHost(), PORT);
            serverWriter = new DataOutputStream(clientMessager.getOutputStream());
            serverReader = new DataInputStream(clientMessager.getInputStream());
        } catch(IOException error){
            System.err.println(error.getMessage());
            close();
        }
    }
    
    public static void sentMessage(String message) throws IOException{
        System.out.println("Massage to server : " + message);
        serverWriter.writeUTF(message);
        serverWriter.flush();
    }
    
    public static void close(){
        try {
            serverWriter.close();
            clientMessager.close();
        } catch (IOException error) {
            System.err.println(error.getMessage());
        }
    }
    
    public static void main(String[] args) {
        initialize();
        Scanner messageScanner = new Scanner(System.in);
        do{
            try{
                System.out.print("Input message to server >> ");
                clientMessage = messageScanner.nextLine();
                if(clientMessage.equals("Disconnect")) break;
                sentMessage(clientMessage);
                serverMessage = serverReader.readUTF();
                System.out.println("Massage from server : " + serverMessage);
            } catch(IOException error){
                System.err.println(error.getMessage());
                break;
            }
        }while(!serverMessage.equals("Close"));
        close();
    }
}
