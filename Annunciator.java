import java.net.*;
import java.io.*;

public class Annunciator extends Multicaster implements Runnable
{
/*
	Megan McGill
	CISC 230
	Instructor: Dr. Jarvis
	May 12, 2013

	This class announces messages to the MulticastSocket group.

	Class Variables:

		message
			a DatagramPackage containing a message

		sleepTimeBetweenSends
			an integer holding the amount of time between sleeps


	Constructors:

		public Annunciator(InetAddress group, int port, int sleepTimeBetweenSends, Serializable message) throws IOException
			a constructor that creates a MulticastSocket by calling the super class; initializes class variables

	Methods:

		synchronized private DatagramPacket getMessage()
			an accessor method for the class variable 'message'

		synchronized public void setMessage(Serializable message) throws IOException
			a mutator method for the class variable 'message'

		public void run()
			a method that sends messages to the group after every sleep interval, while
			the MulticastSocket is still broadcasting.
*/
	//CLASS VARIABLES
	private DatagramPacket 	message;
	private int				sleepTimeBetweenSends;

	//CONSTRUCTORS
	public Annunciator(InetAddress group, int port, int sleepTimeBetweenSends, Serializable message) throws IOException
	{
		//a constructor that creates a MulticastSocket by calling the super class; initializes class variables
		super(group, port);
		if(group == null || message == null) throw new IllegalArgumentException("null was passed to constructor in Annunciator");
		this.sleepTimeBetweenSends = sleepTimeBetweenSends;
		this.setMessage(message);
	}

	synchronized private DatagramPacket getMessage()
	{
		//an accessor method for the class variable 'message'
		return this.message;
	}

	synchronized public void setMessage(Serializable message) throws IOException
	{
		//a mutator method for the class variable 'message'
		if(message == null) throw new IllegalArgumentException("null was passed to setMessage() in Annunciator");
		this.message = super.putIntoDatagram(message);
	}

	public void run()
	{
		//a method that sends messages to the group after every sleep interval, while
		//the MulticastSocket is still broadcasting.

		while(!isClosed())
		{
			try
			{
				super.send(message);
				Thread.sleep(this.sleepTimeBetweenSends);

			}
			catch(Exception ioe){System.out.println(ioe.getMessage());}

		}
	}
}