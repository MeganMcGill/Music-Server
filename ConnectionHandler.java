import java.net.*;
import java.io.*;

public class ConnectionHandler
{
/*
	Megan McGill
	CISC 230
	Instructor: Dr. Jarvis
	April 23, 2013

	This class models a connection handler.

	Class Variables:

		clientConnection
			holds the client's socket

		logWriter
			a LogWriterInterface object

		serialNumber
			contains the serial number of the connection handler object

		serialNumberCounter
			holds the running total of serial numbers; a static long

	Constructors:

		public ConnectionHandler(Socket clientConnection, LogWriterInterface writer)
			this constructor initializes all class variables

	Methods:

		synchronized private void initializeSerialNumber()
			a method added to eliminate synchronization problems when setting serial numbers

		public void run()
			this method recieves an agent from an object inputStream and calls
			the serverSideRun method of the agent

		public long getSerialNumber()
			returns the serial number of the current object

*/
	//CLASS VARIABLES
	private Socket 				clientConnection;
	private long				serialNumber;
	private static long 		serialNumberCounter = 0;
	private LogWriterInterface 	logWriter;

	//CONSTRUCTORS
	public ConnectionHandler(Socket clientConnection, LogWriterInterface writer)
	{
		//this constructor initializes class variables
		this.clientConnection = clientConnection;
		initializeSerialNumber();
		this.logWriter = writer;
	}

	synchronized private void initializeSerialNumber()
	{
		//this method was added to avoid synchronization problems

	 	serialNumberCounter = serialNumberCounter + 1;
	 	this.serialNumber   = serialNumberCounter;
	}

	//ADDITIONAL METHODS
	public void run()
	{
		//this method recieves an agent from an object inputStream and calls
		//the serverSideRun method of the agent
		ObjectInputStream 		inStream;
		Agent					agent;
		try
		{
			inStream = new ObjectInputStream(this.clientConnection.getInputStream());
			agent = (Agent)inStream.readObject();
			agent.serverSideRun(this.clientConnection,this.logWriter);

			try
			{
				inStream.close();
			}
			catch(Exception a)
			{
				throw new RuntimeException("Input stream didn't close");
			}
			try
			{
				this.clientConnection.close();
			}
			catch(Exception b)
			{
				throw new RuntimeException("Client socket didn't close");
			}
		}
		catch(Exception e)
		{
			//shut up
		}
	}

	public long getSerialNumber()
	{
		//returns the serial number of the current object
		return this.serialNumber;
	}

}