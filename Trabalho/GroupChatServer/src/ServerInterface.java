import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;


public interface ServerInterface extends Remote
{    
    public void sendMessage(Message msg) throws RemoteException,IOException;    
    public int logIn(String username, Date sendDate) throws RemoteException,IOException;
    public void logOff(String username, Date sendDate) throws RemoteException,IOException;
}
