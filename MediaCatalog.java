import java.util.*;
import java.io.*;
import java.net.*;

public class MediaCatalog
{
/*
	Megan McGill
	CISC 230
	Instructor: Dr. Jarvis
	May 5, 2013

	This class models a media catalog.

	Class Variables:

		mediaCatalog
			the only instance of MediaCatalog is contained in this object

		catalog
			a Hashtable whose keys are songId's as Strings and whose values are Song
			objects


	Constructors:

		private MediaCatalog()
			a constructor that initializes hashtable


	Methods:

		synchronized public static MediaCatalog newInstance()
			this method returns the instance of the MediaCatalog object

		public static MediaCatalog getInstance()
			this method calls newInstance() to access the MediaCatalog object.

		public void loadSongsFromFile(File dataFile) throws IOException
			this method loads songs from a file to the MediaCatalog.

		public Song getSong(String songId)
			this method returns a song from the catalog

		public Song[] getSongs()
			this method returns the songs in the catalog as a Song[]

		public Song getRandomSong()
			this method returns a random song from the compedium of awesome tunes.


	Nested Classes:
		(Documentation for the following classes can be found within the classes themselves.)

			private class InvalidFieldDataException extends IllegalArgumentException
*/

	//CLASS VARIABLES
	private static MediaCatalog mediaCatalog = null;
	private Hashtable<String,Song> catalog;


	//CONSTRUCTORS
	private MediaCatalog()
	{
		//a constructor that initializes hashtable
		catalog = new Hashtable<String, Song>();
	}

	//ADDITIONAL METHODS
	synchronized public static MediaCatalog newInstance()
	{
		//this method returns the instance of the MediaCatalog object

		if(MediaCatalog.mediaCatalog == null)
		{
			//if a MediaCatalog object hasn't been created yet, create the instance of the class
			mediaCatalog = new MediaCatalog();
		}
		return mediaCatalog;
	}

	public static MediaCatalog getInstance()
	{
		//this method calls newInstance to access the MediaCatalog object.
		return newInstance();
	}

	public void loadSongsFromFile(File dataFile) throws IOException
	{
		//This method loads songs from a file to the MediaCatalog.

		//Variables
		int				counter;
		File			file;
		String[]		fields;
		BufferedReader 	reader;
		String			record;
		Song			song;
		Vector<String>	vector;

		//error checking for File
		if(dataFile == null)
		{
			throw new IllegalArgumentException("Null was passed for the file");
		}
		if(!dataFile.exists())
		{
			throw new IllegalArgumentException("File does not exist");
		}

		//initializing vector
		vector = new Vector<String>();

		try
		{
			//read info from file
			reader = new BufferedReader(new FileReader(dataFile));

			record = reader.readLine();
			while(record!=null)
			{
				vector.add(record);
				record = reader.readLine();
			}

			//close file
			reader.close();
		}
		catch(Exception e)
		{
			throw new IllegalArgumentException("Could not read from file.");
		}

		counter = 0;
		while(counter<vector.size())
		{
			fields = new String[4];
			fields = vector.elementAt(counter).split("\t",4);

			try
			{
				//testing for song title
				if(fields[0].trim() == null)
				{
					throw new IllegalArgumentException("record "+(counter+1)+" had a null song title");
				}
				if(fields[0].trim().isEmpty())
				{
					throw new IllegalArgumentException("record "+(counter+1)+" had an empty song title");
				}
				//testing for artist name
				if(fields[1].trim() == null)
				{
					throw new IllegalArgumentException("record "+(counter+1)+" had a null artist name");
				}
				if(fields[1].trim().isEmpty())
				{
					throw new IllegalArgumentException("record "+(counter+1)+" had an empty artist name");
				}
				//testing for encoding method
				if(fields[2].trim() == null)
				{
					throw new IllegalArgumentException("record "+(counter+1)+" had a null encoding method");
				}
				if(fields[2].trim().isEmpty())
				{
					throw new IllegalArgumentException("record "+(counter+1)+" had an empty encoding method");
				}
				//testing for file
				if(fields[3].trim() == null)
				{
					throw new IllegalArgumentException("record "+(counter+1)+" had a null file path");
				}
				if(fields[3].trim().isEmpty())
				{
					throw new IllegalArgumentException("record "+(counter+1)+" had an empty file path");
				}
				file = new File(fields[3].trim());
				if(!(file.isFile()))
				{
					throw new IllegalArgumentException("record "+(counter+1)+" has a non-existent file path");
				}
				//if all went according to plan...
				song = new Song(fields[0].trim(),fields[1].trim(),fields[2].trim(),file);

				catalog.put(song.getSongId(),song);
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}

			//increment while loop
			counter=counter+1;

		}//end while
	}//end loadSongsFromFile()

	public Song getSong(String songId)
	{
		//this method returns a song from the catalog

		//test if the song exists in the table
		if(catalog.get(songId) == null)
		{
			throw new IllegalArgumentException("song does not exist in media catalog.");
		}
		return catalog.get(songId);
	}

	public Song[] getSongs()
	{
		//this method returns the songs in the catalog as a Song[]
		return (Song[]) this.catalog.values().toArray(new Song[0]);
	}

	public Song getRandomSong()
	{
		//this method returns a random song from the compedium of awesome tunes.
		Song[] songs;
		songs = getSongs();
		return songs[(int)(Math.random()*getSongs().length)];
	}

	//NESTED CLASS
	private class InvalidFieldDataException extends IllegalArgumentException
	{
	/*
		Megan McGill
		CISC 230
		Instructor: Dr. Jarvis
		May 5, 2013

		This class models an exception for invalid field data.

		Constructors:

			public InvalidFieldDataException()
				this constructor calls its super class IllegalArgumentException

			public InvalidFieldDataException(String message)
				this constructor calls its super class IllegalArgumentException and passes it
				a message as a String.
	*/

		//CONSTRUCTORS
		public InvalidFieldDataException()
		{
			//calls the super class IllegalArgumentException
			super();
		}
		public InvalidFieldDataException(String message)
		{
			//calls the super class IllegalArgumentException and passes a String
			super(message);
		}
	}
}

