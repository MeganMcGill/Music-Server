import java.io.*;
import java.net.*;

public class Listener extends Multicaster implements Runnable
{
/*
	Megan McGill
	CISC 230
	Instructor: Dr. Jarvis
	May 12, 2013

	This class listens for messages from the MulticastSocket.

	Constructors:

		public Listener(InetAddress group, int port) throws IOException
			creates a MulticastSocket by calling the super class Multicaster

	Methods:

		public void run()
			a method that listens for messages while the Multicaster is broadcasting.

*/
	//CONSTRUCTOR
	public Listener(InetAddress group, int port) throws IOException
	{
		//creates a MulticastSocket by calling the super class Multicaster
		super(group, port);
	}

	public void run()
	{
		//a method that listens for messages while the Multicaster is still broadcasting
		Object 			inObject;

		while(!super.isClosed())
		{
			try
			{
				inObject = (HelloGoodbyeInterface) super.receive();
				new Thread((Runnable) inObject).start();
			}
			catch(Exception e){}
		}
	}
}