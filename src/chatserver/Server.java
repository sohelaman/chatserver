package chatserver;

import static chatserver.Server.ClientsTable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author Sohel Aman
 */
public class Server {
    
    /**
     * @param args the command line arguments
     */
    
    //static Client[] clientArray=new Client[10];
    //static String[] clientNameArray=new String[10];
    //static Socket[] clientSocketArray=new Socket[10];
    static Hashtable ClientsTable = new Hashtable(10);
    
    static int size=0;
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            ServerSocket serverSocket = null;
            serverSocket = new ServerSocket(5432);
            System.out.println("Server started at port 5432.");
            for (;;) {
                Socket clientSocket = null;
                clientSocket = serverSocket.accept();
                new Thread(new InputProcessor(clientSocket)).start();
                new Thread(new OutputProcessor(clientSocket)).start();
                //System.out.println(clientSocket+" connected.");
            }
          
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}

class InputProcessor implements Runnable {
    private final Socket clientSocket;
    //boolean isInit=false;
    private static String name;
    InputProcessor(Socket s) {
        clientSocket=s;
    }
    
    public void run() {
        
        try {
            
            String id=null;
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            id=in.readUTF();
            name=id;
            if(!ClientsTable.containsKey(id)) {
                //Scanner kb=new Scanner(System.in);
                //String tmp=in.readUTF().split("|")[1];
                
                ClientsTable.put(id,clientSocket);
                System.out.println(id+" joined.");
            } else {
                ClientsTable.put(id,clientSocket);
                System.out.println(id+" rejoined.");
            }
            
            
            while(true) {
                //DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                //DataOutputStream out=new DataOutputStream(clientSocket.getOutputStream());
                String temp=in.readUTF();
                if(!temp.contains(":")) {
                    System.out.println("Undirected Message from "+id+" ==> "+temp);
                } else {
                    //System.out.println(id+": "+temp);
                    //String[] to=temp.split(":");
                    //System.out.println(to[0] + " | "+to[1]);
                    String msg=null, to=null;
                    StringTokenizer st=new StringTokenizer(temp.trim(), ":");
                    while(st.hasMoreTokens()) {
                        to=st.nextToken();
                        msg=st.nextToken();
                        break;
                    }
                    //System.out.println(to+"|"+msg);
                    
                    if(ClientsTable.containsKey(to)) {
                        Socket soc=(Socket)ClientsTable.get(to);
                        if(!soc.isClosed()) {
                            DataOutputStream out=new DataOutputStream(soc.getOutputStream());
                            out.writeUTF(id+": "+msg);
                            System.out.println("Message sent to "+to);
                        } else {
                            System.out.println("User not connected.");
                            DataOutputStream out=new DataOutputStream(clientSocket.getOutputStream());
                            out.writeUTF("User not connected!");
                        }
                    } else {
                        System.out.println("Message not sent! Recipient not found.");
                        DataOutputStream out=new DataOutputStream(clientSocket.getOutputStream());
                        out.writeUTF("Message not sent! Recipient not found.");
                    }
                    
                }
                
                //out.writeUTF("Server Says GG!");
            }
            
        }catch(Exception EOFException) {
          System.err.println("Client Disconnected:"+ " " + name);
        }
        
        
    }
}


class OutputProcessor implements Runnable {
    private final Socket clientSocket;
    
    OutputProcessor(Socket s) {
        clientSocket=s;
    }
    
    public void run() {
        
        try {
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out=new DataOutputStream(clientSocket.getOutputStream());
            while(true) {
                //System.out.println("I AM AT SEVER OutputProcessor");
                //System.out.print("Server: ");
                java.util.Scanner sc=new java.util.Scanner(System.in);
                String str=sc.nextLine();
                
                out.writeUTF(str);
                
                //out.writeUTF("Server Says GG!");
            }
            
        } catch(IOException e) {
            e.printStackTrace();
            
        }
        
    }
}



