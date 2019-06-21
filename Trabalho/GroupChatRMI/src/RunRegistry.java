import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RunRegistry 
{
    public static Registry reg = null;
    public static void main(String args[]) 
    {
        int registryPort = 0;

        System.out.println("Starting local RMI Registry...");
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
		
        // Proper Close to Registry
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                System.out.println("");
                try
                {
                    UnicastRemoteObject.unexportObject(reg,true);
                    System.out.println("Successfully shutdown. Closing now...");
                }
                catch(Exception ex)
                {
                    System.out.println("Error when trying to execute proper shutdown. Closing anyway...");
                }
            }
        });		

        try    
        {
            reg = LocateRegistry.createRegistry(registryPort);
            if(reg != null)
            {
                System.out.println("Successfully started local registry. Running...");
            }
			
            // Run until application is closed
            while(true);

        }
        catch (Exception ex)
        {
            System.out.println("Error when trying to run local RMI Registry. Aborting...");
            ex.printStackTrace();
            System.exit((-2));
        }
    }
}