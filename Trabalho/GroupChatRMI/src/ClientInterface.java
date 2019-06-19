import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface ClientInterface extends Remote
{    
    public void printMessage(Message msg) throws RemoteException,IOException;   
}