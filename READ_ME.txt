TO RUN MUSIC SERVER:

1)	Create and place music into a folder called "Music" within the directory containing java files. 
	Music files must be .WAV files.

2)	Modify your SONGLIST.TXT to match the songs you have provided in your "Music" folder. Input should
	be provided in the following format:
			Song Title <TAB> Artist Name <TAB> .WAV <TAB> File Path
	I have included samples of how the text should be presented. Take care not to include extra characters
	or spaces. 
	
3)	Create a folder called "Log_Files" within the directory containing all the java files.

3)	Modify XMLFILE.XML
		* Replace the path in "LogFileDirectoryPath" to the file path for the Log_Files folder.
			Example: <entry key="LogFileDirectoryPath">C:YOUR_PATH\Music_Server\Log_Files</entry>
		* Choose whether or not you would like your server to echo log information to the terminal
		  by modifying the <entry key="EchoLog">true</entry> line. Specifying "true" will cause echoing;
		  specifying "false" will supress the output.
		* Modify the interval at which you'd like output written to the log with this line
		  <entry key="LogFlushInterval">5000</entry>. It is currently set to write every 5 seconds.
		* Make sure all users are using the same multicast IP and port if using in conjunction with
		  other music servers with these lines
		  	<entry key="MulticastIP">227.226.225.224</entry>
			<entry key="MulticastPort">22322</entry>
	
4)	Compile and Run CUSTOMER.JAVA (The client program)

5)	Compile and Run JUKEBOX.JAVA (Your Server)

6)	Enjoy! Interact with the customer terminal to play your tunes.


TO EXIT MUSIC SERVER:
1)	Press Enter in the JUKEBOX.JAVA terminal and then press "y"
2)	Select option "stop" in CLINET.JAVA terminal

