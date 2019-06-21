import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RunServer 
{
    public static InetAddress multi;
    public static MulticastSocket socket;
    public static int registryPort;
    
    public static void main(String args[]) 
    {
        final int MAX_LEN = 2048;
        String multiCastIP = "";
        int multiCastPort = 0;
        
        try    
        {
            GetConfig getConfig = new GetConfig("config.properties");
            multiCastIP = getConfig.GetProperties("multicastIP");
            multiCastPort = Integer.parseInt(getConfig.GetProperties("multicastPort"));
            registryPort = Integer.parseInt(getConfig.GetProperties("registryPort"));
        }       
        catch (Exception ex) 
        { 
            System.out.println("Error when trying to get information from 'config.properties' file. Aborting...");
            ex.printStackTrace();
            System.exit(-1);
        }

        try    
        {
            multi = InetAddress.getByName(multiCastIP);
            socket  = new MulticastSocket(multiCastPort);
            socket.joinGroup(multi);
            System.out.println("Server connected on address: " + multiCastIP + ":" + multiCastPort);
        }
        catch (Exception ex) 
        { 
            System.out.println("Error when trying to connect to a multiCast socket. Aborting...");
            ex.printStackTrace();
            System.exit(-2);
        }
        
        try    
        {
            LocateRegistry.getRegistry(registryPort);
            ChatServer server = new ChatServer(multi, socket, registryPort);
            Naming.rebind("rmi://localhost:" + registryPort + "/Server", server);
            System.out.println("Server registered on local RMI registry...");
        }
        catch (Exception ex)
        {
            System.out.println("Error when trying to connect to rmiRegistry. Aborting...");
            ex.printStackTrace();
            System.exit(-3);
        }
        
        // Proper Close to Server
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                System.out.println("");
                try
                {
                    socket.leaveGroup(multi);
                    Naming.unbind("rmi://localhost:" + registryPort + "/Server");
                    System.out.println("Sucessfully shutdown. Closing now...");
                }
                catch(Exception ex)
                {
                    System.out.println("Error when trying to execute proper shutdown. Closing anyway...");
                }
            }
        });
        
        System.out.println("Begin listening for messages on socket...");
        while(true)
        {
            try
            {
                byte[] buffer = new byte[MAX_LEN];
                DatagramPacket packet = new DatagramPacket(buffer, MAX_LEN, multi,multiCastPort);
                socket.receive(packet);
                ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
                ObjectInputStream ois = new ObjectInputStream(bais);
                Object obj = ois.readObject();
				
                if(obj instanceof Message)
                {
                    Message msg = (Message) obj;
                    Thread t = new Thread(new ListenThread(msg, registryPort));
                    t.start();
                }
                else
                {
                    System.out.println("Wrong message type received. Discarting package...");
                    continue;
                }
            }
            catch(IOException ex)
            {
                System.out.println("Connection error when trying to receive packet...");
                continue;
            }
            catch(Exception ex)
            {
                System.out.println("No object could be read from socket...");
                continue;
            }
        }  
    }    
}

class ListenThread implements Runnable
{
    private int registryPort;
    private Message msg;

    public ListenThread(Message receivedMsg, int regPort)
    {
        msg = receivedMsg;
        registryPort = regPort;
    }
	
	// Send a received message to all clients via RMI	
    @Override 
    public void run()
    {
        System.out.println("Received message via multicast Socket. Broadcasting via RMI...");
        try
        {
            Registry registry = LocateRegistry.getRegistry(registryPort);
            String[] entries = registry.list();
            for(String s:entries)
            {
                if(s.contains("Client"))
                {
                    int retry = 3;
                    while(retry > 0)
                    {   
                        try
                        {
                            ClientInterface client = null;
                            client = (ClientInterface) Naming.lookup("rmi://localhost:" + registryPort + "/" + s);
                            client.printMessage(msg);
                            break;
                        }
                        catch(Exception ex)
                        {
                            retry--;
                        }
                    }
                    if(retry <= 0)
                    {
                        System.out.println("Error when trying to send message via RMI to " + s);
                        continue;
                    }                        
                }                    
            }
        }        
        catch(Exception ex)
        {
            System.out.println("Error when trying to send message via RMI...");
            ex.printStackTrace();
        }
        finally
        {
            return;
        }
    }
}
