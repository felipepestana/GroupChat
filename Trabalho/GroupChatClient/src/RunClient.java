import java.io.ByteArrayInputStream;
import java.io.Console;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Date;
import java.util.Scanner;
import java.rmi.server.UnicastRemoteObject;

public class RunClient 
{
    public static void main(String args[]) 
    {
        final String terminate = "/exit";
        int registryPort = 0;
        String username = "";
        String rmiAddress = "";
        Scanner scan= new Scanner(System.in);
        
        try    
        {
            GetConfig getConfig = new GetConfig("config.properties");
            registryPort = Integer.parseInt(getConfig.GetProperties("registryPort"));
        }       
        catch (Exception ex) 
        { 
            System.out.println("Error when trying to get information from 'config.properties' file. Aborting...");
            ex.printStackTrace();
            System.exit((-1));
        }
        
        ServerInterface server = null;
        try    
        {
            LocateRegistry.getRegistry(registryPort);
            server = (ServerInterface) Naming.lookup("rmi://localhost:" + registryPort + "/Server");
            System.out.println("Found server address to connect.");
            System.out.println("Begin Running Client ");

        }
        catch (Exception ex)
        {
            System.out.println("Error when trying to connect to rmiRegistry. Aborting...");
            ex.printStackTrace();
            System.exit((-2));
        }

        ChatClient client = null;
        try    
        {
            System.out.println("Please enter an username to login: ");
            username = scan.nextLine();
            Date localDate = new Date();
            int id = server.logIn(username, localDate);
            client = new ChatClient();
            rmiAddress = "rmi://localhost:" + registryPort + "/Client" + String.format("%02d",id);
            Naming.rebind(rmiAddress, client);
            System.out.println("Connected. Type message '/exit' at any moment to exit chat room...");
			System.out.println("Begin chatting:");
        }
        catch (Exception ex)
        {
            System.out.println("Error when trying to execute login on Server and register client on RMI...");
            ex.printStackTrace();
            System.exit((-3));
        }

        while(true)
        {
            try
            {
				String messageBody = scan.nextLine();
				System.out.print("\033[1A"); // Move cursor up
				System.out.print("\033[2K"); // Erase current line

                if(messageBody.equals(terminate))
                {
                    break;
                }
                else
                {
                    Date sendDate = new Date();
                    Message msg = new Message(username, messageBody, sendDate, false);    
                    server.sendMessage(msg);
                }
            }
            catch(Exception ex)
            {
                System.out.println("[Server Message] Exception thrown for error found when trying to send message. Please try again...");
                continue;
            }
        }
        
        try
        {
            Date sendDate = new Date();
            Naming.unbind(rmiAddress);
            UnicastRemoteObject.unexportObject(client, false);
            server.logOff(username, sendDate);
			System.out.println("Exiting now...");
        }
        catch(Exception ex)
        {
			System.out.println("Error when trying to execute proper shutdown. Exiting anyway...");
		}
        System.exit(0);
    }    
}

