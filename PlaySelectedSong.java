import java.net.*;

public class PlaySelectedSong extends PlaySong
{
/*
	Megan McGill
	CISC 230
	Instructor: Dr. Jarvis
	May 15, 2013

	This class models an agent that plays a selected song.

	Class Variables:

		serialVersionUID
			this is a static final long with value 1; used in serialization

	Constructors:

		public PlaySelectedSong(Song song)
			this constructor passes the Song object to the PlaySong super class

	Methods:

		public void serverSideRun(Socket socket, LogWriterInterface logWriter)
			a method that gets a specific song from the MediaCatalog and calls
			the super class's serverSideRun()
*/
	//CLASS VARIABLES
	private static final long serialVersionUID = 1;

	public PlaySelectedSong(Song song)
	{
		//this constructor passes the Song object to the PlaySong super class
		super(song);
	}

	public void serverSideRun(Socket socket, LogWriterInterface logWriter)
	{
		//a method that gets a specific song from the MediaCatalog and calls
		//the super class's serverSideRun()
		String 	string;
		Song 	song;

		string = super.getSong().getSongId();
		song = MediaCatalog.getInstance().getSong(string);
		super.setSong(song);
		super.serverSideRun(socket, logWriter);
	}
}