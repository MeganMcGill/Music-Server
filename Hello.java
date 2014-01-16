import java.net.*;
import java.io.*;
public class Hello extends Agent implements HelloGoodbyeInterface, Runnable, Serializable
{
/*
	Megan McGill
	CISC 230
	Instructor: Dr. Jarvis
	May 11, 2013

	This class models a 'Hello' message for entering a multicast group.

	Class Variables:

		serialVersionUID
			a static final long with value 1; used for serialization

		serverName
			a String that contains the server's name

		serverLocation
			an InetSocketAddress that holds the server's location


	Constructors:

		public Hello(String serverName, InetSocketAddress serverLocation)
			a constructor that initializes the class variables

	Methods:

		public void clientSideRun(Socket socket)
			a method that implements the client-side protocol for Hello objects

		public void serverSideRun(Socket socket, LogWriterInterface logWriter)
			a method that implements the server-side protocol for Hello objects

		public void run()
			a method that gets loads songs to client by using the protocol implemented in
			clientSideRun() and serverSideRun()
*/
	//CLASS VARIABLES
	private static final long serialVersionUID = 1;
	private String serverName;
	private InetSocketAddress serverLocation;

	//CONSTRUCTORS
	public Hello(String serverName, InetSocketAddress serverLocation)
	{
		//a constructor that initializes the class variables
		if(serverName == null) throw new IllegalArgumentException("null was passed as server name in Hello.");
		if(serverLocation == null) throw new IllegalArgumentException("null was passed as serverLocation in Hello.");
		this.serverName = serverName;
		this.serverLocation = serverLocation;
	}

	//ADDITIONAL METHODS
	public void clientSideRun(Socket socket)
	{
		//a method that implements the client-side protocol for Hello objects
		Song[] songArray;
		try
		{
			//read an object from the stream, cast the object as a Song[], add the songs to the PlayList
				songArray = (Song[]) super.readObjectFrom(socket);
				PlayList.getInstance().loadSongsFrom(this.serverLocation, songArray );
		}
		catch(Exception a)
		{a.printStackTrace();
			System.out.println("Unable to load songsfrom "+ this.serverLocation);
		}
		super.closeEverything(socket);
	}

	public void serverSideRun(Socket socket, LogWriterInterface logWriter)
	{
		//a method that implements the server-side protocol for Hello objects

		//Variables
		Song[] songs;
		ObjectOutputStream outStream;

		try
		{
			//grab Song[] from MediaCatalog
			songs = MediaCatalog.getInstance().getSongs();

			//write itself back to stream
			super.writeMyselfTo(socket);

			new ObjectOutputStream(getOutputStreamFrom(socket)).writeObject(songs);

			super.closeEverything(socket);

		}
		catch(IOException io)
		{
			logWriter.writeLogMessage("Write unsucessful in Hello "+io.getMessage());
		}
	}

	public void run()
	{
		//a method that gets loads songs to client by using the protocol implemented in
		//clientSideRun() and serverSideRun()

		//Variables
		Socket socket;
		Agent agent;

		if(PlayList.getInstance().getAllSongs(this.serverLocation).length < 1)
		{
			try
			{
				socket = new Socket(this.serverLocation.getAddress(),this.serverLocation.getPort());
				super.writeMyselfTo(socket);
                agent = (Agent) readObjectFrom(socket);
                agent.clientSideRun(socket);

			}catch(Exception ioe)
			{
				ioe.printStackTrace();System.out.println(ioe.getMessage());
			}
		}
	}
}