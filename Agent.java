import java.net.*;
import java.io.*;

public abstract class Agent implements Serializable
{
/*
	Megan McGill
	CISC 230
	Instructor: Dr. Jarvis
	May 5, 2013

	This class models an 'agent' object; an object that can arrive
	at its destination and self-execute following a protocol.

	Class Variables:

		serialVersionUID
			a private static long value

	Constructors:

		public Agent()
			there are no class variables in Agent, so this is an empty
			constructor.

	Methods:

		public Object readObjectFrom(Socket socket) throws IOException, ClassNotFoundException
			this method reads an object from the socket parameter's stream

		public Object readObjectFrom(InputStream in) throws IOException,ClassNotFoundException
			this method reads an object from the InputStream parameter

		public void writeMyselfTo(Socket socket) throws IOException
			this method calls the other writeMyselfTo method

		public void writeMyselfTo(OutputStream out) throws IOException
			this method writes the calling object to the stream parameter

		public void closeSocket(Socket socket)
			this method closes the socket

		public void closeEverything(Socket socket)
			thismethod closes the InputStream and OutputStream associated with
			the socket parameter before closing the socket itself

		public void closeInputStream(InputStream in)
			this method closes the InputStream parameter

		public void closeOutputStream(OutputStream out)
			this method closes the OutputStream parameter

		public InputStream getInputStreamFrom(Socket socket) throws IOException
			this method creates an InputStream for the socket parameter

		public OutputStream getOutputStreamFrom(Socket socket) throws IOException
			this method creates an OutputStream for the socket parameter


	Abstract Methods:

		public abstract void clientSideRun(Socket socket);

		public abstract void serverSideRun(Socket socket, LogWriterInterface logWriter);

*/
	//CLASS VARIABLES
	private final static long serialVersionUID = 1;

	//CONSTRUCTORS
	public Agent()
	{
		//nothing in this constructor.
	}

	//ADDITIONAL METHODS
	public Object readObjectFrom(Socket socket) throws IOException, ClassNotFoundException
	{
		//this method reads an object from the socket parameter's stream
		if(socket == null)
		{
			throw new RuntimeException("null was passed to as a socket:1");
		}
		return readObjectFrom(getInputStreamFrom(socket));
	}

	public Object readObjectFrom(InputStream in) throws IOException,ClassNotFoundException
	{
		//this method reads an object fromthe InputStream parameter

		//wrap the InputStream as an ObjectInputStream
		ObjectInputStream inStream;

		if(in!= null)
		{
			inStream = new ObjectInputStream(in);
		}
		else
		{
			throw new RuntimeException("null was passed: 2");
		}

		//return an object from the ObjectInputStream
		return inStream.readObject();
	}

	public void writeMyselfTo(Socket socket) throws IOException
	{
		//this method calls the other writeMyselfTo method
		if(socket != null)
		{
			writeMyselfTo(socket.getOutputStream());
		}
		else
		{
			throw new RuntimeException("null was passed: 3");
		}
	}

	public void writeMyselfTo(OutputStream out) throws IOException
	{
		//this method writes the calling object to the stream parameter

		ObjectOutputStream outStream;

		if(out != null)
		{
			outStream = new ObjectOutputStream(out);
			outStream.writeObject(this);
		}
		else
		{
			throw new RuntimeException("null was passed: 4");
		}
	}

	public void closeSocket(Socket socket)
	{
		//this method closes the socket
		try
		{
			socket.close();
		}
		catch(Exception e)
		{
			throw new RuntimeException("Could not close the socket.");
		}

	}

	public void closeEverything(Socket socket)
	{
		//thismethod closes the InputStream and OutputStream associated with
		//the socket parameter before closing the socket itself
		try
		{
			//close InputStream
			closeInputStream(getInputStreamFrom(socket));
		}
		catch(Exception a){}
		try
		{
			//close OutputStream
			closeOutputStream(getOutputStreamFrom(socket));
		}
		catch(Exception b){}
		try
		{
			//close socket parameter
			closeSocket(socket);
		}
		catch(Exception c){}
	}

	public void closeInputStream(InputStream in)
	{
		//this method closes the InputStream parameter
		try
		{
			in.close();
		}
		catch(IOException e)
		{
			throw new RuntimeException("Could not close the InputStream.");
		}
	}

	public void closeOutputStream(OutputStream out)
	{
		//this method closes the OutputStream parameter
		try
		{
			out.close();
		}
		catch(Exception e)
		{
			throw new RuntimeException("Could not close the OutputStream.");
		}
	}

	public InputStream getInputStreamFrom(Socket socket) throws IOException
	{
		//this method creates an InputStream for the socket parameter
		if(socket == null)
		{
			throw new RuntimeException("null was passed: 5");
		}
		return socket.getInputStream();
	}

	public OutputStream getOutputStreamFrom(Socket socket) throws IOException
	{
		//this method creates an OutputStream for the socket parameter
		if(socket == null)
		{
			throw new RuntimeException("null was passed: 6");
		}
		return socket.getOutputStream();
	}

	//ABSTRACT METHODS
	public abstract void clientSideRun(Socket socket);
	public abstract void serverSideRun(Socket socket, LogWriterInterface logWriter);


}