import java.net.*;
import java.io.*;

public class Goodbye implements HelloGoodbyeInterface, Runnable, Serializable
{
/*
	Megan McGill
	CISC 230
	Instructor: Dr. Jarvis
	May 11, 2013

	This class models a 'Goodbye' message for exiting a multicast group.

	Class Variables:

		serialVersionUID
			a static final long with value 1; used in serialization

		serverLocation
			an InetSocketAddress that holds the server's location

		serverName
			a String that holds the server's name


	Constructors:

		public Goodbye(String serverName, InetSocketAddress serverLocation)
			a constructor that initializes class variables

	Methods:

		public void run()
			a method that removes songs from the exiting server

*/

	//CLASS VARIABLES
	private static final long serialVersionUID = 1;
	private InetSocketAddress serverLocation;
	private String serverName;

	public Goodbye(String serverName, InetSocketAddress serverLocation)
	{
		//a constructor that initializes class variables
		this.serverLocation = serverLocation;
		this.serverName = serverName;
	}

	public void run()
	{
		//a method that removes songs from the exiting server
		PlayList.getInstance().removeSongsFrom(this.serverLocation);
		System.out.println("Songs from "+this.serverName+" have been erased.");
	}
}