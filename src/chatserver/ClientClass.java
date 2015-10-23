package chatserver;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientClass
{
    
    
    public static void main(String [] args)
    {
        
        try {
            String serverName = "localhost";
            int port = 5432;
            Socket s=new Socket(serverName, port);
            Thread t = new clientWrite(s);
            t.start();
            
            Thread t2 = new clientRead(s); //---------------------------------------------
            t2.start();
        } catch(Exception e) {
            //TODO
        }
        
    }
}




class clientWrite extends Thread {
    
    
    static Socket ClientSocket;
    static boolean isInit=false;
    static String id=null;
    
    public clientWrite(Socket s) {
        ClientSocket=s;
        
    }
    
    
    public void run()
    {
        try
        {
            //System.out.println("Connecting to " + serverName + " on port " + port);
            //Socket client = new Socket(serverName, port);
            
            
            Socket client =ClientSocket; 
            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            //Thread.sleep(5000);
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            
            if(!isInit) {
                System.out.println("Give Username:");
                Scanner kb=new Scanner(System.in);
                id=kb.next();
                out.writeUTF(id);
                isInit=true;
            }
            
            while(true) {
                //System.out.print(id+": ");
                Scanner sc=new Scanner(System.in);
                String str=sc.nextLine();
                
                if(str.equals("quit")){
                    client.close();
                    outToServer.close();
                    break;
                }
                
                out.writeUTF(str);
                //client.close();
            }
            
        }catch(Exception e)
        {
            e.printStackTrace();
         //System.out.println("Client Closed");
        }
    }
    
}




class clientRead extends Thread {
    
    
    static Socket ClientSocket;
    
    
    public clientRead(Socket s) {
        ClientSocket=s;
        
    }
    
    
    public void run()
    {
        try
        {
            InputStream inFromServer = ClientSocket.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            while(true) {
                //System.out.println("I AM AT ClientRead");
               System.out.println(in.readUTF());
               //Thread.currentThread().interrupt();
               //ClientSocket.close();
            }
        }catch(Exception SocketException)
        {
           // e.printStackTrace();
         System.err.println("Client Closed!!");
        }
    }
    
}