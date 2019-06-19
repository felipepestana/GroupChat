import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ChatClient extends UnicastRemoteObject implements ClientInterface
{
    List<Message> msgList = Collections.synchronizedList(new ArrayList<Message>());
    
    public ChatClient() throws RemoteException {}
	
    public void printMessage(Message msg) throws RemoteException,IOException
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        String formattedMsg = "";
        msg.receiveDate = new Date();
        msgList.add(msg);
        
        try
        {
            Thread.sleep(300);
        } 
        catch(InterruptedException ex){}
        
        Message msgToShow = null;
        synchronized(msgList)
        {
            Collections.sort(msgList, new Comparator<Message>() {
                public int compare(Message msg1, Message msg2) 
                {
                    return msg1.sendDate.compareTo(msg2.sendDate);
                }
            });
            msgToShow = msgList.get(0);
            msgList.remove(0);
        }
        
        formattedMsg = "[" + dateFormat.format(msgToShow.sendDate) + "-" + dateFormat.format(msgToShow.receiveDate) + "]";
        if (msg.isSystemMessage)
        {
            formattedMsg =  formattedMsg + "Server Message: " + msgToShow.body;
        }
        else
        {
            formattedMsg = formattedMsg + msgToShow.username + ": " + msgToShow.body; 
        }
		
		synchronized(System.out)
		{
			System.out.println(formattedMsg);
		}
    }
}
