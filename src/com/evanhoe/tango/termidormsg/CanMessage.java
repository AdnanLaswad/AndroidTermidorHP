package com.evanhoe.tango.termidormsg;

public class CanMessage {
	
	
    public int identifier;
    public int dataLength;
    public int[] message;
    public int type;
    public String mac;
    public CanMessage(int identifier, int dataLength, int[] message, int type, String mac)
    {
        this.identifier = identifier;
        this.dataLength = dataLength;
        this.message = message;
        this.type = type;
        this.mac = mac;
    }
    public CanMessage()
    {

    }
    public CanMessage(String msg)
    {
        String delimeter = " ";
        String[] separated = msg.split(delimeter);
        int i = 0;
        if (separated[0].equalsIgnoreCase("M")){
        	
            dataLength = Integer.parseInt(separated[1].substring(2, 3));
            identifier = Integer.parseInt(separated[2], 16);
            message = new int[this.dataLength];
            for (int j=3;i < separated.length-3;j++)//(String s : separated.Skip(3))
            {
                this.message[i] = Integer.parseInt(separated[j], 16);
                i++;
            }
            this.type = 0;
        }
        if (separated[0].equalsIgnoreCase("I"))
        {
            if (separated.length == 6)
            {
                if (separated[1] == "Name:")
                {
                    this.mac = separated[5].substring(1, 13);
                    this.message = null;

                }
                else
                {
                    this.mac = null;
                }
            }
            this.type = 1;
        }
    }

    public CanMessage BuildMessage(String msg)
    {
        CanMessage thisMessage = new CanMessage();
        msg = msg.substring(1, msg.length() - 1);
        String delimeter = " ";
        String[] separated = msg.split(delimeter);
        int i = 0;
        if (separated[0].equalsIgnoreCase("M"))
        {
            thisMessage.dataLength = Integer.parseInt(separated[1].substring(2, 3));
            thisMessage.identifier = Integer.parseInt(separated[2], 16);
            thisMessage.message = new int[thisMessage.dataLength];
            for (int j=3;i < separated.length-3;j++)//(String s : separated.Skip(3))
            {
                thisMessage.message[i] = Integer.parseInt(separated[j], 16);
                i++;
            }
        }
        return thisMessage;
    }

    
    public String toString()
    {
        StringBuilder tmpString = new StringBuilder();
        tmpString.append("M ");
        tmpString.append("ED");
        tmpString.append(dataLength);
        tmpString.append(" ");
        tmpString.append(intToHex(identifier));            
        for (int i : message)
        {
            tmpString.append(" ");
            tmpString.append(intToHex(i));
        }
        return tmpString.toString();
    }
    private String intToHex(int num)
    {
        return Integer.toHexString(num);
    }


}
