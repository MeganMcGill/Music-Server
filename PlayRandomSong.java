import java.net.*;

public class PlayRandomSong extends PlaySong
{
/*
	Megan McGill
	CISC 230
	Instructor: Dr. Jarvis
	May 5, 2013

	This class models an agent that plays a random song.

	Class Variables:

		serialVersionUID
			this is a static final long with value 1; used in serialization

	Constructors:

		public PlayRandomSong()
			this is an empty constructor

	Methods:

		public void serverSideRun(Socket socket,LogWriterInterface logWriter)
			This method overrides a method in the PlaySong super class. This serverSideRun()
			works by snagging a random song from the MediaCatalog, passing that random song
			to the PlaySong's method setSong, and then calling the PlaySong serverSideRun().

*/
	//CLASS VARIABLE
	private static final long serialVersionUID = 1;

	public PlayRandomSong()
	{
		//empty
	}

	public void serverSideRun(Socket socket,LogWriterInterface logWriter)
	{
		//This method overrides a method in the PlaySong super class. This serverSideRun()
		//works by snagging a random song from the MediaCatalog, passing that random song
		//to the PlaySong's method setSong, and then calling the PlaySong serverSideRun().

		//Variables
		MediaCatalog 	mediaCatalog;
		Song			song;

		mediaCatalog = MediaCatalog.newInstance();
		song = mediaCatalog.getRandomSong();
		super.setSong(song);
		super.serverSideRun(socket, logWriter);
	}
}