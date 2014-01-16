import java.io.*;

public class Song implements Serializable
{
/*
	Megan McGill
	CISC 230
	Instructor: Dr. Jarvis
	May 5, 2013

	This class models a media catalog.

	Class Variables:

		serialNumber
			a static integer serial number that begins at zero

		serialVersionUID
			a static final long value 1; used for serialization

		artistName
			a String containing the artist's name of a particular song

		encodingMethod
			a String containing the encoding method of a song

		filePath
			a File object whose path is to the song

		songId
			an integer holding the ID number of the song

		songTitle
			a Sting holding the title of a song

	Constructors:

		public Song(String songTitle, String artistName, String encodingMethod, File filePath)
			This method is the class constructor. It initializes all class variables through assignments
			in this class and by calling the private method initializeSongId.

	Methods:

		synchronized private void initializeSongId()
			This method ensures that assigning of serialNumbers and subsequent songId's
			is synchronized.

		public String getArtist()
			This method returns the name of the artist associated with this Song object as a String.

		public String getSongTitle()
			This method returns the song title associated with this Song object as a String.

		public String getSongId()
			This method returns the songId associated with the Song object as a String

		public String getEncodingMethod()
			This method returns the encoding method associated with this Song object as a String.

		public InputStream getInputStream() throws FileNotFoundException
			This method returns an input stream for the File associated with this Song object.

*/
	//CLASS VARIABLES
	private static int 			serialNumber = 0;
	private static final long 	serialVersionUID = 1;

	private String 				artistName;
	private String				encodingMethod;
	private transient File		filePath;
	private int					songId;
	private String				songTitle;

	//CONSTRUCTORS
	public Song(String songTitle, String artistName, String encodingMethod, File filePath)
	{
		//This method is the class constructor. It initializes all class variables through assignments
		//in this class and by calling the private method initializeSongId.

		//error checking for the parameters
		if(songTitle == null) throw new IllegalArgumentException("Song title parameter is null");
		if(artistName == null) throw new IllegalArgumentException("Artist name parameter is null");
		if(encodingMethod == null) throw new IllegalArgumentException("Encoding method parameter is null");
		if(filePath == null) throw new IllegalArgumentException("File path parameter is null");

		//trim parameters
		songTitle = songTitle.trim();
		artistName = artistName.trim();
		encodingMethod = encodingMethod.trim();

		//more parameter checking
		if(songTitle.length() < 1) throw new IllegalArgumentException("Song title parameter contains no characters");
		if(artistName.length() < 1) throw new IllegalArgumentException("Artist name parameter contains no characters");
		if(encodingMethod.length() < 1) throw new IllegalArgumentException("Encoding method parameter contains no characters");


		//initialize the class varialbes
		if(filePath != null && filePath.exists())
		{
			this.filePath = filePath;
		}
		else
		{
			throw new IllegalStateException("File path is either null or does not exist.");
		}
		this.songTitle      = songTitle;
		this.encodingMethod = encodingMethod;
		this.artistName     = artistName;

		//finish initializations through method call
		initializeSongId();
	}

	synchronized private void initializeSongId()
	{
		//This method ensures that assigning of serialNumbers and subsequent songId's
		//is synchronized.
		serialNumber = serialNumber + 1;
		this.songId = serialNumber;
	}

	//ACCESSORS
	public String getArtist()
	{
		//This method returns the name of the artist associated with this Song object as a String.
		return this.artistName;
	}

	public String getSongTitle()
	{
		//This method returns the song title associated with this Song object as a String.
		return this.songTitle;
	}

	public String getSongId()
	{
		//This method returns the songId associated with the Song object as a String
		return ""+this.songId;
	}

	public String getEncodingMethod()
	{
		//This method returns the encoding method associated with this Song object as a String.
		return this.encodingMethod;
	}

	public InputStream getInputStream() throws FileNotFoundException
	{
		//This method returns an input stream for the File associated with this Song object.
		return new FileInputStream(this.filePath);
	}

}