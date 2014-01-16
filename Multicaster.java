import java.net.*;
import java.io.*;

public class Multicaster
{
/*
	Megan McGill
	CISC 230
	Instructor: Dr. Jarvis
	May 15, 2013

	This class listens implements the multicasting portion of program.

	Class Variables:

		group
			an InetAddress that holds the multicast group

		isBroadcasting
			a boolean that returns whether or not the MulticastSocket is broadcasting

		multicastSocket
			a MulticastSocket object

		port
			an integer that holds the port of the multicast group


	Constructors:

		public Multicaster(InetAddress group, int port) throws IOException
			a constructor that initializes class variables

	Methods:

		public void close(Serializable lastMessage) throws IOException
			a method used to closse the class's MulticastSocket variable 'multicastSocket'

		public boolean isClosed()
			a method that returns whether or not a socket is closed (if it is broadcasting or not)

		public Object receive() throws IOException, ClassNotFoundException
			a method that calls the other receive() and passes it an integer payload size.

		public Object receive(int payloadSizeInBytes) throws IOException, ClassNotFoundException
			a method that creates a byte[] of the payloadSizeInBytes, creates a DatagramPacket
			for receiving a datagram, grabs a DatagramPacket from a client, returns the packet.

		public DatagramPacket putIntoDatagram(Serializable object) throws IOException
			a method that recieves an object as input, and packetizes it.

		public void send(DatagramPacket datagram) throws IOException
			a method that sends DatagramPackets
*/
	//CLASS VARIABLES
	private InetAddress			group;
	private boolean 			isBroadcasting;
	private MulticastSocket 	multicastSocket;
	private int					port;

	//CONSTRUCTORS
	public Multicaster(InetAddress group, int port) throws IOException
	{
		//a constructor that initializes the class variables

		if(group == null) throw new RuntimeException("null was passed to constructor in Multicaster");
		//create a new MulticastSocket
		this.multicastSocket = new MulticastSocket(port);
		this.multicastSocket.joinGroup(group);

		//continue instantiations
		this.group = group;
		this.port = port;

		//begin broadcasting
		this.isBroadcasting = true;
	}

	//ADDITIONAL METHODS
	public void close(Serializable lastMessage) throws IOException
	{
		//a method used to closse the class's MulticastSocket variable 'multicastSocket'

		if(!this.isClosed())
		{
			this.isBroadcasting = false;
			if(lastMessage != null) this.multicastSocket.send(putIntoDatagram(lastMessage));

			this.multicastSocket.leaveGroup(group);
			this.multicastSocket.close();
		}
	}

	public boolean isClosed()
	{
		//a method that returns whether or not a socket is closed (if it is broadcasting or not)
		return !this.isBroadcasting;
	}

	public Object receive() throws IOException, ClassNotFoundException
	{
		//a method that calls the other receive() and passes it an integer payload size.
		int payloadSizeInBytes;

		payloadSizeInBytes = 2048;
		return receive(payloadSizeInBytes);
	}

	public Object receive(int payloadSizeInBytes) throws IOException, ClassNotFoundException
	{
		//a method that creates a byte[] of the payloadSizeInBytes, creates a DatagramPacket
		//for receiving a datagram, grabs a DatagramPacket from a client, returns the packet.
		byte[] 					buffer;
		DatagramPacket 			packet;
		ByteArrayInputStream 	bis;

		if(payloadSizeInBytes <1) throw new RuntimeException("less than 1 byte passed to receive() in Multicaster.");

		buffer = new byte[payloadSizeInBytes];
		packet = new DatagramPacket(buffer, buffer.length);
		this.multicastSocket.receive(packet);
		bis = new ByteArrayInputStream(buffer);
		return new ObjectInputStream(bis).readObject();


	}

	public DatagramPacket putIntoDatagram(Serializable object) throws IOException
	{
		//a method that recieves an object as input, and packetizes it.
		DatagramPacket 		  packet;
		byte[]				  buffer;
		ByteArrayOutputStream bos;

		if(object == null) throw new IllegalArgumentException("null was passed to putIntoDatagram() in Multicaster");
		bos = new ByteArrayOutputStream();
		new ObjectOutputStream(bos).writeObject(object);
		buffer = bos.toByteArray();
		packet = new DatagramPacket(buffer, buffer.length);
		packet.setAddress(this.group);
		packet.setPort(this.port);
		return packet;
	}

	public void send(DatagramPacket datagram) throws IOException
	{
		//a method that sends DatagramPackets

		if(datagram == null) throw new IllegalArgumentException("null was passed to send() in Multicaster");
		multicastSocket.send(datagram);
	}

}