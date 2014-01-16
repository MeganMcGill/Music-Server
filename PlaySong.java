import java.net.*;
import java.io.*;
import javax.sound.sampled.*;
import javax.sound.sampled.Line.Info;
import javax.sound.sampled.AudioSystem;

public abstract class PlaySong extends Agent
{
/*
	Megan McGill
	CISC 230
	Instructor: Dr. Jarvis
	May 5, 2013

	This class models an agent that plays music.

	Class Variables:

		serialVersionUID
			a static final long that has value 1; used for serialization

		error
			a String that holds any error messages that are generated

		song
			a Song object


	Constructors:

		public PlaySong()
			this constructor passes null to the other constructor

		public PlaySong(Song inSong)
			this constructor initializes the class variables

	Methods:

		public boolean errorWasGenerated()
			this method returns whether or not an error has been generated

		public Song getSong()
			this method returns a song

		public void setSong(Song song)
			this method sets a new Song object to the class song variable

		public void serverSideRun(Socket socket, LogWriterInterface logWriter)
			this method implements the server-side protocol for the PlaySong objects

		public void clientSideRun(Socket socket)
			this method implements the client-side protocol PlaySong objects

*/
	//CLASS VARIABLES
	private static final long	serialVersionUID = 1;
	private String				error;
	private Song				song;

	//Constructor
	public PlaySong()
	{
		//this class passes null to the other constructor
		this(null);
	}

	public PlaySong(Song song)
	{
		//this constructor initializes the class variables
		this.song = song;
		this.error = null;
	}

	public boolean errorWasGenerated()
	{
		//this method returns whether or not an error has been generated
		return this.error != null;
	}

	public Song getSong()
	{
		//this method returns a song
		return this.song;
	}

	public void setSong(Song song)
	{
		//this method sets a new Song object to the class song variable
		this.song = song;
	}

	public void serverSideRun(Socket socket, LogWriterInterface logWriter)
	{
		//this method implements the server-side protocol for the PlaySong objects
		byte[]			buffer;
		int 			bytesRead;
		InputStream 	inStream;
		OutputStream 	outStream;
		Song			serverSong;
		String 			songTitle;

		inStream = null;

		serverSong = getSong();

		if(serverSong == null)
		{
			this.error = "Could not find Song in media catalog.";
			songTitle = "unknown";
		}
		else
		{
			songTitle = serverSong.getSongTitle();

			try
			{
				inStream = serverSong.getInputStream();
			}
			catch(IOException e)
			{
				this.error = "Could not get input stream from Song";
			}
		}

		if(errorWasGenerated())
		{
			logWriter.writeLogMessage(this.error);
		}

		try
		{
			try{super.writeMyselfTo(socket);}catch(Exception e){this.error = "Could not write myself to socket";}

			if(!errorWasGenerated())
			{
				try
				{
					outStream = super.getOutputStreamFrom(socket);

					buffer = new byte[2048];
					bytesRead = inStream.read(buffer);

					while(bytesRead >= 0)
					{
						if(bytesRead > 0)
						{
							outStream.write(buffer, 0, bytesRead);
						}
						bytesRead = inStream.read(buffer);
					}
				}
				catch(IOException ioe)
				{
					this.error = ioe.getMessage();
					logWriter.writeLogMessage(this.error);
				}
			}
		}
		catch(Exception e)
		{
			this.error = "Could not communicate with socket";

		}
		super.closeEverything(socket);

	}

	public void clientSideRun(Socket socket)
	{
		//this method implements the client-side protocol PlaySong objects

		AudioInputStream	audioStream;
		byte[]				byteArray;
		int					bytesRead;
		BufferedInputStream bufferedInput;
		DataLine.Info		dataLine;
		AudioFormat			format;
		SourceDataLine		sourceDataLine;

		if(this.error == null)
		{
			System.out.println("Song Title: "+this.song.getSongTitle()+"\tArtist: "+this.song.getArtist());
			try
			{
				//get a BufferedInputStream using the socket's input stream
				bufferedInput = new BufferedInputStream(super.getInputStreamFrom(socket));

				//get an AudioInputStream using the BufferedInputStream
				audioStream = AudioSystem.getAudioInputStream(bufferedInput);

				//get the AudioFormat from AudioInputStream
				format = audioStream.getFormat();

				//create a Dataline.Info object from AudioFormat and open a SourceDataLine
				dataLine = new DataLine.Info(SourceDataLine.class,format);
				sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLine);

				//open the SourceDataLine
				sourceDataLine.open(format);

				//start the SourceDataLine
				sourceDataLine.start();

				//creating a byte array
				byteArray = new byte[1024];

				bytesRead = audioStream.read(byteArray,0,byteArray.length);

				while(bytesRead>=0)
				{
					if(bytesRead>0)
					{
						bytesRead = audioStream.read(byteArray);
						sourceDataLine.write(byteArray,0, byteArray.length);
					}
				}
				//read AudioInputStream and write the SouceDataLine

				//drain SouceDataLine
				sourceDataLine.drain();

				try
				{
					sourceDataLine.close();
				}
				catch(SecurityException watchDog)
				{
					System.out.println("Could not close the SourceDataLine");
				}

				try
				{
					audioStream.close();
				}
				catch(SecurityException guard)
				{
					System.out.println("Could not clsoe the AudioInputStream");
				}

				try
				{
					bufferedInput.close();
				}
				catch(IOException io)
				{
					System.out.println("Could not close BufferedInputStream");
				}
				finally
				{
					super.closeEverything(socket);
				}
			}
			catch(Exception ioe)
			{
				this.error = ioe.getMessage();
			}
		}
		if(this.error != null)
		{
			//this.error = "Can't play song.";
			System.out.println(this.error);
		}

	}
}