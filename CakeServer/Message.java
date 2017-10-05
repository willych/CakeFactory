import java.io.*;

public class Message implements Serializable
{
    String name;
    String message;

    public Message(String strName,String strMessage)
    {
        name = strName;
        message = strMessage;
    }

    public String getName()
    {
        return name;
    }

    public String getMessage()
    {
        return message;
    }
}