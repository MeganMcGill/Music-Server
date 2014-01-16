import java.io.*;
import java.text.*;
import java.util.*;

public class Log
{
/*
	Megan McGill
	CISC 230
	Instructor: Dr. Jarvis
	April 23, 2013

	This class models a log.

	Class Variables:

		isClosed
			a boolean that says whether or not the log is still open
		logFile
			a PrintWriter object used to write to log files
		recordNumber
			the running total of records in the log

	Constructors:

		public Log(File directory) throws IOException
			calls the other constructor by passing directory and a sleepAmount of 500 milliseconds

		public Log(File directory, int sleepAmount) throws IOException
			creates a Log object and initializes the class variables

	Methods:

		private String getNow()
			this method grabs the current date and time and formats it as a DATE_FORMAT

		public void close()
			this method closes the logFile

		public boolean isClosed()
			this method returns whether or not the logFile is still open

		public String writeLogRecord(LogRecord logRecord)
			this method writes a LogRecord to the opened logFile

	Nested Classes:
		(Documentation for the following classes can be found within the classes themselves.)

			public class LogFlusher implements Runnable

*/
	//CLASS VARIABLES
	private static final SimpleDateFormat 	DATE_FORMAT= new SimpleDateFormat("yyyyMMdd HHmmssSSS");
	private boolean 						isClosed;
	private PrintWriter 					logFile;
	private long							recordNumber;


	//CONSTRUCTORS
	public Log(File directory) throws IOException
	{
		//this constructor calls the other constructor
		this(directory,500);

	}

	public Log(File directory, int sleepAmount) throws IOException
	{
		//this constructor initializes all class variables

		String newFile;

		if(directory != null && directory.exists() && directory.isDirectory())
		{
			newFile = getNow().substring(0,8)+getNow().substring(9)+".txt";

			this.logFile = new PrintWriter(new File(directory, newFile));
			this.recordNumber =1;
			this.isClosed = false;

			if(sleepAmount > 0)
			{
				new Thread(new LogFlusher(sleepAmount)).start();
			}
		}
		else
		{
			throw new IllegalArgumentException("Invalid directory input");
		}
	}

	//ADDITIONAL METHODS
	private String getNow()
	{
		//this method grabs the current date and time
		return DATE_FORMAT.format(Calendar.getInstance().getTime());
	}

	public void close()
	{
		//this method closes the logFile
		this.logFile.close();
		this.isClosed = true;
	}

	public boolean isClosed()
	{
		//this method returns whether or not the logFile is still open
		return this.isClosed;
	}

	synchronized public String writeLogRecord(LogRecord logRecord)
	{
		//this method writes a LogRecord to the logFile
		String newString;

		newString = new String(this.recordNumber+"\t"+getNow()+"\t"+logRecord.getAsString());
		this.recordNumber = recordNumber +1;
		logFile.println(newString);
		return newString;
	}


	//INNER CLASS CODE
	private class LogFlusher implements Runnable
	{
	/*
		Megan McGill
		CISC 230
		Instructor: Dr. Jarvis
		April 23, 2013

		This class flushes data to the current Log file on disk.

		Class Variables:

			recordCount
				a long that holds the current number of records (since the last time this class was called)

			sleepAmount
				an int that holds the number of milliseconds to sleep

		Constructors:

			public LogFlusher(int sleepAmount)
				creates a LogFlusher object and initializes the class variables

		Methods:

			public void run()
				if records have been written to the log file since this class was last called, flush the
				log file to disk, update recordCount to the current recordNumber, and sleep.


	*/
		//CLASS VARIABLES
		private long	recordCount;
		private int 	sleepAmount;

		//CONSTRUCTORS
		public LogFlusher(int sleepAmount)
		{
			this.sleepAmount = sleepAmount;
		}

		//ADDITIONAL METHODS
		public void run()
		{
			//if records have been written to the log file since this class was last called, flush the
			//log file to disk, update recordCount to the current recordNumber, and sleep.
			while(!isClosed)
			{
				if(this.recordCount!= recordNumber)
				{
					logFile.flush();
					this.recordCount = recordNumber;
				}

				try
				{
					Thread.sleep(this.sleepAmount);
				}
				catch(Exception ie)
				{
					//shut up
				}
			}
		}
	}
}