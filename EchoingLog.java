import java.io.*;

public class EchoingLog extends Log
{
/*
	Megan McGill
	CISC 230
	Instructor: Dr. Jarvis
	April 23, 2013

	This class models a log that is both written to a log file on disk and printed to the screen.

	Constructors:

		public EchoingLog(File logDirectory) throws IOException
			calls a constructor in the Log super class by passing the File

		public EchoingLog(File logDirectory, int sleepAmount) throws IOException
			calls a constructor in the Log super class by passing the File and sleepAmount integer

	Methods:

		public String writeLogRecord(LogRecord logRecord)
			overrides the writeLogRecord of the Log super class to provide the functionality to both
			write the log records to disk and to print the records to the screen.

*/
	//CONSTRUCTORS
	public EchoingLog(File logDirectory) throws IOException
	{
		//a constructor that calls the super class
		super(logDirectory);
	}
	public EchoingLog(File logDirectory, int sleepAmount) throws IOException
	{
		//a constructor that calls the super class
		super(logDirectory, sleepAmount);
	}

	//ADDITIONAL METHODS
	public String writeLogRecord(LogRecord logRecord)
	{
		//overrides the super class method in Log; adds the functionality to print log records to screen
		String fromSuper;

		fromSuper = super.writeLogRecord(logRecord);
		System.out.println(fromSuper);

		return fromSuper;
	}
}