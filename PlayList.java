import java.util.*;
import java.net.*;
import java.net.InetSocketAddress;

public class PlayList
{
/*
		Megan McGill
		CISC 230
		Instructor: Dr. Jarvis
		May 10, 2013

		This class models a list of songs.

		Class Variables:

			playList
				a PlayList object that holds the instance of this class

			songTable
				a Hashtable whose keys are the InetAddresses of servers and whose values
				are SongList objects

		Constructors:

			private PlayList()
				this constructor initializes the class variable songTable


		Methods:

			public static PlayList getInstance()
				a method that calls newInstance()

			public static PlayList newInstance()
				a method that returns an instance of the 'playlist' class variable

			private String getKeyFrom(InetSocketAddress server)
				a method that returns the key for a server if passed the server's
				InetSocketAddress

			public String[] getAllServers()
				a method that returns the set of servers contained in the hashtable class variable
				the servers are the set of keys, which is a concatination of server and port number.

			public Song[] getAllSongs(String server)
				a method that returns a Song[] of the objects associated with a particular server;
				the parameter 'server' should be a string of the server's InetSocketAddress

			public Song[] getAllSongs(InetSocketAddress server)
				a method that returns a Song[] of the objects associated with a particular server

			public void loadSongsFrom(InetSocketAddress server, Song[] songs)
				a method used to load songs from a server to the hashtable

			public void removeSongsFrom(InetSocketAddress server)
				a method that removes the Hashtable key and value associated with a particular server

*/
	//CLASS VARIABLES
	private static final PlayList playList = new PlayList();
	private Hashtable<String,SongList> songTable;

	//CONSTRUCTORS
	private PlayList()
	{
		//this constructor initializes the class variable songTable
		songTable = new Hashtable<String,SongList>();
	}

	//ADDITIONAL METHODS

	public static PlayList getInstance()
	{
		//a method that calls newInstance()
		return newInstance();
	}

	public static PlayList newInstance()
	{
		//a method that returns an instance of the 'playlist' class variable
		return playList;
	}

	private String getKeyFrom(InetSocketAddress server)
	{
		//a method that returns the key for a server if passed the server's InetSocketAddress
		return server.getAddress().getHostAddress()+":"+server.getPort();
	}

	public String[] getAllServers()
	{
		//a method that returns the set of servers contained in the hashtable class variable
		//the servers are the set of keys, which is a concatination of server and port number.
		Object[] hold;
		String[] toReturn;

		//create Object[] and String[] that are the length of # of keys
		hold = new Object[this.songTable.keySet().toArray().length];
		toReturn = new String[this.songTable.keySet().toArray().length];

		//populate the Object[] with keys
		hold = this.songTable.keySet().toArray();

		//populate the String[] with the objects in the Object[]
		System.arraycopy(hold,0,toReturn,0,toReturn.length);
		return toReturn;
	}

	public Song[] getAllSongs(String server)
	{
		//a method that returns a Song[] of the objects associated with a particular server
		Song[]   songArray;
		SongList songList;

		songList = this.songTable.get(server);
		if(songList == null) { songArray = new Song[0]; } else { songArray = songList.getSongs();}

		return songArray;
	}

	public Song[] getAllSongs(InetSocketAddress server)
	{
		//a method that returns a Song[] of the objects associated with a particular server

		//call the other 'getAllSongs' method
		return getAllSongs(getKeyFrom(server));
	}

	public void loadSongsFrom(InetSocketAddress server, Song[] songs)
	{
		//a method used to load songs from a server to the hashtable; no error
		//checking is performed in this method. If a server already had a value
		//in the table, it will be replaced by a new value if the key is repeated.

		String 		key;
		SongList 	value;

		key = getKeyFrom(server); //create a key from the InetSocketAddress
		value = new SongList(songs); //create a value for the key

		this.songTable.put(key, value); //put the key and value into the hashtable
	}

	public void removeSongsFrom(InetSocketAddress server)
	{
		//a method that removes the Hashtable key and value associated with a particular server
		this.songTable.remove(getKeyFrom(server));
	}

	//INNER CLASS
	private class SongList
	{
	/*
		Megan McGill
		CISC 230
		Instructor: Dr. Jarvis
		May 10, 2013

		This class models a list of songs.

		Class Variables:

			songArray
				an array of Song objects

		Constructors:

			public SongList(Song[] songArray)
				this constructor initializes the class variable songArray


		Methods:

			public int getNumberOfSongs()
				this method returns the number of songs in the class variable

			public Song[] getSongs()
				this method returns the array of songs held in the class variable

			public Song getSongUsingSongId(String songId)
				this method returns a specific Song object from the class' Song[] variable

	*/
		//CLASS VARIABLES
		private Song[] songArray;

		//CONSTRUCTORS
		public SongList(Song[] songArray)
		{
			//this constructor initializes the class variable songArray
			this.songArray = new Song[songArray.length];
			System.arraycopy(songArray,0,this.songArray,0,songArray.length);

		}

		//ADDITIONAL METHODS
		public int getNumberOfSongs()
		{
			//this method returns the number of songs in the class variable
			return this.songArray.length;
		}

		public Song[] getSongs()
		{
			//this method returns the array of songs held in the class variable
			Song[] songs;

			songs = new Song[this.songArray.length];
		 	System.arraycopy(this.songArray,0,songs,0,this.songArray.length);
           	return songs;
		}

		public Song getSongUsingSongId(String songId)
		{
			//this method returns a specific Song object from the class' Song[] variable
			return this.songArray[Integer.parseInt(songId)-1];
		}
	}
}