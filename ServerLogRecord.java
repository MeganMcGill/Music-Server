public class ServerLogRecord extends LogRecord
{
/*
	Megan McGill
	CISC 230
	Instructor: Dr. Jarvis
	April 23, 2013

	This class models a log record for a server.

	Class Variables:

		MessageType
			an enum that stores both the name and numeric values for messages used in the
			server records. This enum has its own constructor, and a private message, getMessageCode(),
			that returns teh integer code associated with the message type.

	Constructors:

		public ServerLogRecord()
			calls the LogRecord super class and passes it the number of fields needed for a
			ServerLogRecord

	Methods:

		public void setServerName(String serverName)
			sets the server's name to a field in the log record

		public void setMessageType(MessageType messageType)
			sets the numeric value of the MessageType to a field in the log record

		public void setClientIPNumber(String clientIPNumber)
			sets an IP number to a field in the log record

		public void setClientPort(int clientPort)
			sets a port number to a field in the log record

		public void setMessage(String message)
			sets a message to a field in the log record

*/
	//CLASS VARIABLES
	public static enum MessageType {StatusMessage(1), RequestMessage(2);

			private int code;
			private MessageType(int code)
			{
				this.code = code;
			}
			private int getMessageCode()
			{
				return this.code;
			}
		}

	//CONSTRUCTORS
	public ServerLogRecord()
	{
		//this constructor calls the super class constructor in LogRecord
		super(5);
	}

	//ADDITIONAL METHODS
	public void setServerName(String serverName)
	{
		//this method sets field zero in super class LogRecord
		super.setField(0,serverName);
	}

	public void setMessageType(MessageType messageType)
	{
		//this method sets field one in super class LogRecord
		super.setField(1, messageType.getMessageCode()+"");
	}

	public void setClientIPNumber(String clientIPNumber)
	{
		//this method sets field two in super class LogRecord
		super.setField(2,clientIPNumber);
	}

	public void setClientPort(int clientPort)
	{
		//this method sets field three in super class LogRecord
		super.setField(3,""+clientPort);
	}

	public void setMessage(String message)
	{
		//this method sets field four in super class LogRecord
		super.setField(4,message);
	}

}