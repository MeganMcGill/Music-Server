import java.net.*;
import java.io.*;

public class Server implements Runnable
{
/*
	Megan McGill
	CISC 230
	Instructor: Dr. Jarvis
	April 23, 2013

	This class models a server.

	Class Variables:

		portNumber
			an integer containing the port number of the server socket.

		serverIsRunning
			a boolean stating whether or not the server is running

		serverLog
			a Log object holding a reference to the Log in which the server's records are to be written.

		serverName
			a String object that holds the name of the server.

	Constructors:

		Server(int port, EchoingLog log)
			constructs a Server and initializes all class variables

	Methods:

		public void startServer() throws IOException
			this method is called by the run method to start-up the server; this method threads processes off to handlers
			and makes connections to clients while the server is running; this method also makes the appropriate calls to
			classes to keep a log of all events on the server.

		public void run()
			this method calls the startServer() method to begin running the server.

		public boolean serverIsRunning()
			returns a boolean stating whether or not the server is still running.

		public void stopServer()
			this method stops the server.

		public int getPort()
			this method returns the port number assoicated with the socket in use

	Nested Classes:
		(Documentation for the following classes can be found within the classes themselves.)

			private class Handler implements Runnable,LogWriterInterface

			private class RequestLogRecord extends ServerLogRecord

*/

	//CLASS VARIABLES
	private int 				portNumber;
	private boolean 			serverIsRunning;
	private Log					serverLog;
	private String				serverName;

	//CONSTRUCTORS
	public Server(int port, Log log)
	{
		//the constructor initializes all class variables

		if(port>=0)
		{
			this.portNumber = port;
			this.serverName = new String("server @ " + this.portNumber);
			this.serverIsRunning = false;
			this.serverLog = log;
		}
		else
		{
			throw new IllegalArgumentException("Invalid port input");
		}
	}

	//ADDITIONAL METHODS
	public void startServer() throws IOException
	{
		//This method begins the server.

		//Variables
		Handler handler;
		ServerLogRecord		logRecord;
		Socket 				client;
		ServerSocket 		serverSocket;

		//Create start-up record for server's log
		logRecord = new ServerLogRecord();
		logRecord.setServerName(this.serverName);
		logRecord.setClientIPNumber(InetAddress.getLocalHost().getHostAddress()+"");
		logRecord.setClientPort(this.portNumber);
		logRecord.setMessage("\tAbout to create server socket.");
		logRecord.setMessageType(ServerLogRecord.MessageType.StatusMessage);

		//write first record to the server's log
		logRecord.writeToLog(this.serverLog);

		//officially start the server.
		//if zero was passed as port, the socket will select a port
		serverSocket = new ServerSocket(this.portNumber);
		this.serverIsRunning = true;
		if(this.portNumber==0)
		{
			//update the port number to actual number in use
			this.portNumber = serverSocket.getLocalPort();
			this.serverName = new String("server @ " + this.portNumber);

			logRecord.setServerName(this.serverName);
			logRecord.setClientPort(this.portNumber);
		}


		logRecord.setMessage("\tServer started.");
		logRecord.setMessageType(ServerLogRecord.MessageType.StatusMessage);
		logRecord.writeToLog(this.serverLog);


		while(this.serverIsRunning())
		{
			//accept a client and thread client to a handler
			client = serverSocket.accept();
			if(this.serverIsRunning())
			{
				handler = new Handler(client);
				new Thread(handler).start();
			}
		}

		//update the server's log with exit info and close serverLog.
		logRecord.setMessage("\tServer stopped");
		logRecord.setMessageType(ServerLogRecord.MessageType.StatusMessage);
		logRecord.setClientPort(this.portNumber);
		logRecord.writeToLog(this.serverLog);

		this.serverLog.close();

	}

	public void run()
	{
		//This method calls the startServer() method to begin running the server.
		try
		{
			this.startServer();
		}
		catch(Exception ioe)
		{
			throw new RuntimeException(ioe.getMessage());
		}
	}

	public boolean serverIsRunning()
	{
		//returns whether or not the server is currently running
		return this.serverIsRunning;
	}

	public void stopServer()
	{
		//this method is used to stop the server
		if(this.serverIsRunning == true)
		{
			Socket newSocket;

			this.serverIsRunning = false;
			try
			{
				//attempt one last connect to server to shut it down
				newSocket = new Socket(InetAddress.getLocalHost(),this.portNumber);
			}
			catch(Exception e)
			{
				//throw exception at user.
				throw new RuntimeException("Server will not close:\t"+e.getMessage());
			}
		}
	}

	public int getPort()
	{
		//returns the port number associated with the server socket.
		return this.portNumber;
	}

	//INNER CLASS
	private class Handler implements Runnable, LogWriterInterface
	{
	/*
		Megan McGill
		CISC 230
		Instructor: Dr. Jarvis
		April 23, 2013

		This class models a handler.

		Class Variables:

			connectionHandler
				a ConnectionHandler object

			connectionHandlerMessageCount
				a long containing the number of the messages that have come through

			connectionHandlerSerialNumber
				a long containing the serial number associated to the object as assigned
				by ConnectionHandler

			requestLogRecord
				a RequestLogRecord object

		Constructors:

			public Handler(Socket clientSocket)
				creates a Handler object and instantiates all class variables

		Methods:

			public void run()
				runs the connectionHandler object.

			public void writeLogMessage(String string)
				writes messages to the server's log record.

		Nested Classes:
			(Documentation for the following classes can be found within the classes themselves.)

				private class RequestLogRecord extends ServerLogRecord

	*/

		//CLASS VARIABLES
		private ConnectionHandler 	connectionHandler;
		private RequestLogRecord 	requestLogRecord;
		private long 				connectionHandlerMessageCount;
		private long				connectionHandlerSerialNumber;

		//CONSTRUCTORS
		public Handler(Socket clientSocket)
		{
			//this constructor initializes all class variables

			this.connectionHandler = new ConnectionHandler(clientSocket, this);
			this.connectionHandlerSerialNumber = this.connectionHandler.getSerialNumber();
			this.requestLogRecord = new RequestLogRecord(clientSocket);
			this.connectionHandlerMessageCount = 1;
		}

		//ADDITIONAL METHODS
		public void run()
		{
			//this method begins running the ConnectionHandler

			this.requestLogRecord.setMessage("begin");

			//write to log file
			this.requestLogRecord.writeToLog(Server.this.serverLog);

			//handle the connection with the socket
			this.connectionHandler.run();

			//finish with client
			this.requestLogRecord.setDateTimeStampToNow();
			this.requestLogRecord.setMessage("end");
			this.requestLogRecord.writeToLog(Server.this.serverLog);
		}

		public void writeLogMessage(String string)
		{
			//this method writes messages to the server's log record

			this.requestLogRecord.setMessage(""+this.connectionHandlerMessageCount+"   "+string);
			this.requestLogRecord.writeToLog(Server.this.serverLog);
			this.connectionHandlerMessageCount = connectionHandlerMessageCount+1;
		}

		//INNER CLASS
		private class RequestLogRecord extends ServerLogRecord
		{
		/*
			Megan McGill
			CISC 230
			Instructor: Dr. Jarvis
			April 23, 2013

			This class models a special type of log record used in requests to the server.

			Class Variables:

				timeInMilliseconds
					a long containing

			Constructors:

				public RequestLogRecord(Socket clientSocket)
					creates a RequestLogRecord object and calls setDateTimeStampToNow()

			Methods:

				public void setMessage(String message)
					overrides the setMessage method in the super class ServerLogRecord with a
					specialized message for the log file.

				public void setDateTimeStampToNow()
					mutator for the class variable timeInMilliseconds

		*/
			//CLASS VARIABLES
			private long timeInMilliseconds;

			//CONSTRUCTORS
			public RequestLogRecord(Socket clientSocket)
			{
				//this constructor initializes all class variables and
				//updates the info in the server's log record.

				this.setDateTimeStampToNow();
				this.setClientIPNumber(clientSocket.getInetAddress().getHostAddress()+"");
				this.setClientPort(clientSocket.getPort());
				this.setMessage("Connection Recieved");
				this.setMessageType(ServerLogRecord.MessageType.RequestMessage);
				this.setServerName(serverName);
			}

			//ADDITIONAL METHODS
			public void setMessage(String message)
			{
				//this method sets the message in the ServerLogRecord
				super.setMessage(Handler.this.connectionHandlerSerialNumber+"\t"+message+"\t\t"+this.timeInMilliseconds);
			}

			public void setDateTimeStampToNow()
			{
				//this method returns the current time in milliseconds
				this.timeInMilliseconds = System.currentTimeMillis();
			}

		} //end RequestLogRecord

	}//end Handler

}//end Server
