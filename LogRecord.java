public class LogRecord
{
/*
	Megan McGill
	CISC 230
	Instructor: Dr. Jarvis
	April 23, 2013

	This class models a log record.

	Class Variables:

		fields
			a String[] containing the fields for the record

	Constructors:

		public LogRecord(int numOfFields)
			creates a LogRecord object and initializes the class variable: fields

	Methods:

		public void setField(int fieldNum, String record)
			sets a field in the current log record

		public String getFieldSeparator()
			returns the field separator used as a String

		public String getAsString()
			returns the record as a String

		public void writeToLog(Log log)
			the record writes itself to the current log (by calling the Log class)

*/
	//CLASS VARIABLES
	private String[] fields;

	//CONSTRUCTORS
	public LogRecord(int numOfFields)
	{
		//a constructor that initializes class variables
		this.fields = new String[numOfFields];
	}

	//MUTATORS
	public void setField(int fieldNum, String record)
	{
		//this method sets a field in the record
		if(fieldNum >=0 && fieldNum < fields.length)
		{
			this.fields[fieldNum] = record;
		}
		else
		{
			throw new IllegalArgumentException("Record cannot be set, field number exceeds length of String[]:\t"+fieldNum);
		}
	}

	//ACCESSORS
	public String getFieldSeparator()
	{
		//returns the field separator used in the record
		return "\t";
	}

	public String getAsString()
	{
		//returns the record as a String
		String toReturn;
		toReturn = "";

		for(int i = 0; i < this.fields.length; i++)
		{
			if(i != (this.fields.length - 1)) //while not the last index of array
			{
				toReturn = toReturn+ fields[i]+ getFieldSeparator();
			}
			else //for last index of array
			{
				toReturn = toReturn+fields[i];
			}
		}
		return toReturn;
	}

	//ADDITIONAL METHODS
	public void writeToLog(Log log)
	{
		//the LogRecord writes itself to the log file
		log.writeLogRecord(this);
	}
}