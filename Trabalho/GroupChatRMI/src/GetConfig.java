import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class GetConfig 
{
    Properties configFile;
    
    public GetConfig(String fileName)
    {
        configFile = new Properties();
        
        try
        {
           ClassLoader classLoader = this.getClass().getClassLoader();
           InputStream stream = classLoader.getResourceAsStream(fileName);
           if(stream == null)
           {
               throw new FileNotFoundException("Could not find config file " + fileName);
           }
           
           configFile.load(stream);
           stream.close();
        }
        catch(FileNotFoundException ex)
        {
            System.out.println("Exception thrown: " + ex);
        }
        catch(Exception ex)
        {
            System.out.println("Unknown exception thrown when trying to read file " + fileName);
            ex.printStackTrace();
        }
    }
    
    public String GetProperties(String key)
    {
        String value = configFile.getProperty(key);
        return value;
    }        
}
