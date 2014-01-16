public interface LogWriterInterface
{
/*
	Megan McGill
	CISC 230
	Instructor: Dr. Jarvis
	May 3, 2013

	This interface allows messages to be written to logs from classes
	without direct access to Log.
*/
	public void writeLogMessage(String message);
}