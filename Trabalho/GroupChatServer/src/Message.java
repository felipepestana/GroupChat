import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable
{
    public String username;
    public String body;
    public Date sendDate;
    public Date receiveDate;  
    public boolean isSystemMessage;
	
	public Message(String user, String messageText, Date createDate, boolean flag)
	{
		this.username = user;
		this.body = messageText;
		this.sendDate = createDate;
		this.isSystemMessage = flag;
	}
}
