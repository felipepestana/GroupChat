import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;


public class ChatServer extends UnicastRemoteObject implements ServerInterface
{
    private InetAddress multiIp;
    private MulticastSocket multiSocket;
    private Integer registryPort;
    
    public ChatServer(InetAddress ip, MulticastSocket socket, Integer port) throws RemoteException
    {
        multiIp = ip;
        multiSocket = socket;
        registryPort = port;
    }
    
    @Override
    public void sendMessage(Message msg) throws RemoteException,IOException
    {
        System.out.println("Received message from username " + msg.username);
        msg.isSystemMessage = false;
        BroadcastSocket(msg);
    }
    
    @Override
    public synchronized int logIn(String username, Date sendDate) throws RemoteException,IOException
    {
        String body = "The user '" + username + "' has just connected to the Chat";
		Message msg = new Message(username, body, sendDate, true);    
        System.out.println("Received login request from user " + username);
		
        Registry registry = LocateRegistry.getRegistry(registryPort);
        String[] entries = registry.list();
        int indexToReturn = -1;
        // Check for a number that is not being currently used  
        for (int i = 0; ; i++)
        {
            boolean check = true;
            for(String s:entries)
            {
                if(s.endsWith("Client" + String.format("%02d",i)))
                {
                    check = false;
                    break;
                }                    
            }
            
            if(check)
            {
                indexToReturn = i;
                break;
            }
        }
        
        if(indexToReturn >= 0)
        {
            BroadcastSocket(msg);
        }
        return indexToReturn;
    }
    
    @Override
    public void logOff(String username, Date sendDate) throws RemoteException,IOException
    {
        System.out.println("Received logoff request from user " + username);
        String body = "The user '" + username + "' has just disconnected";
		Message msg = new Message(username, body, sendDate, true);       
        BroadcastSocket(msg);
    }
    
    private void BroadcastSocket(Message msg) throws IOException
    {
        int retry = 3;
        while(retry > 0)
        {
            try
            {                
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(msg);
                byte[] byteArray = baos.toByteArray();
                DatagramPacket packet = new DatagramPacket(byteArray, byteArray.length, multiIp, multiSocket.getLocalPort());
                multiSocket.send(packet);
                break;
            }
            catch (IOException ex)
            {
                retry--;                
                System.out.println("Exception thrown when trying to broadcast message.Retries left: " + retry);
            }                
        }
        
        if(retry == 0)
            throw new IOException("Failed to broadcast message");
    }
    
}
