import java.io.*;
import java.net.*;
import java.util.*;

public class Customer
{
 /*
    Patrick L. Jarvis
    May 10, 2013

    CISC 230 project

    This is the client driver program

	Used by: Megan McGill
	May 15, 2013

 */

 private static Listener listener;

 public static void main(String[] args) throws IOException
 {
  Command    command;
  Thread     listenerThread;
  Properties systemProperties;

  //  load in the system properties file
  systemProperties = new Properties();
  systemProperties.loadFromXML(new FileInputStream("xmlFile.xml"));

  // create and launch the multicast socket to listen for JukeBox servers
  listener = new Listener( InetAddress.getByName(systemProperties.getProperty("MulticastIP")),
                           Integer.parseInt(systemProperties.getProperty("MulticastPort")) );
  listenerThread = new Thread(listener);
  listenerThread.start();



  //
  command = CommandFactory.getCommand("");
  while(!command.endProgram()) { command = command.run(); }
  command.run();


  System.out.println("End of Program");
 } // main




  private interface Command
  {
   public boolean endProgram();
   public String getDescription();
   public Command run() throws IOException;
  }  // interface

  static private class CommandFactory
  {
   private static Map<String, Command> commandTable = initializeTable();
   static public Command getCommand(String commandText)
   {
    //  Implement the Factory method pattern.
    //  Convert the user input stored in the parameter into a Command object
    Command commandObject;

    commandObject = commandTable.get(commandText.trim().toLowerCase());
    if(commandObject == null) { commandObject = commandTable.get(null); }
    return commandObject;
   }

   static private Map<String, Command> initializeTable()
   {
    HashMap<String, Command> table;

    table = new HashMap<String, Command>();
    table.put(null,      new UserInputError());
    table.put("",        new GetInputFromUser());
    table.put("?",       new Help());
    table.put("help",    new Help());
    table.put("quit",    new ShutDown());
    table.put("end",     new ShutDown());
    table.put("stop",    new ShutDown());
    table.put("server",  new ShowTheServers());
    table.put("servers", new ShowTheServers());
    table.put("random",  new PlayARandomSong());
    table.put("shuffle", new Shuffle());
    return table;
   } // initializeTable

   static public String[] getCommandText()
   {
    return commandTable.keySet().toArray(new String[0]);
   }
  }  // class CommandFactory

  static private class Shuffle extends CommandRequiringUserInput implements Command
  {
   public boolean endProgram()     { return false; }
   public String  getDescription() { return "Play a number of random songs"; }

   public Command run() throws IOException
   {
    String userInput;
    int    howManySongs;
    int    songCount;

    if(PlayList.getInstance().getAllServers().length < 1)
    {
     System.out.println("Can't shuffle music - no servers are available.");
    }
    else
    {
     userInput = super.readFromKeyboard("How many songs do you want to listen to? ");
     try
     {
      songCount = Integer.parseInt(userInput);
      if(songCount < 1) throw new NumberFormatException();
      for(int i=0; i< songCount; i++) { new PlayARandomSong().run(); }
     }
     catch(NumberFormatException nfe)
     {
      System.out.println("Error. You must enter an integer greater than zero.");
     }
     catch(Exception e)
     {
      System.out.println("Exception raised in shuffle - "+e.getMessage());
     }

    }  // if ... else
    return new GetInputFromUser();
   }
  } // class Shuffle


  static private class PlayARandomSong implements Command
  {
   //  pick a random server and request a random song
   public boolean endProgram()     { return false; }
   public String  getDescription() { return "Play a random song from a random server"; }

   public Command run() throws IOException
   {
    PlaySong       playSong;
    String[]       servers;
    String[]       parts;
    Socket         socket;

    try
    {
     servers = PlayList.getInstance().getAllServers();
     if(servers.length > 0)
     {
      parts = servers[(int)(Math.random()*servers.length)].split(":");
      socket = new Socket(parts[0], Integer.parseInt(parts[1]));
      playSong = new PlayRandomSong();
      playSong.writeMyselfTo(socket);
      playSong = (PlaySong) playSong.readObjectFrom(socket);
      playSong.clientSideRun(socket);
     }
     else
     {
      System.out.println("No servers are available so a random song cannot be played.");
     }
    }
    catch(Exception e)
    {e.printStackTrace();
     System.out.println("Problem in PlayRandomSong request: " + e.getMessage());
    }
   return new GetInputFromUser();
   }  // run

  }  //  classPlayRandomSong



  static private class Help implements Command
  {
   //  lists the available command
   public boolean endProgram()     { return false; }
   public String  getDescription() { return "List the commands"; }

   public Command run() throws IOException
   {
    //  close the multicast socket without send a final message
    Command  command;
    String[] commandText;
    String   description;
    int      maxLength;

    maxLength = 0;
    commandText = CommandFactory.getCommandText();
    for(int i=0; i<commandText.length; i++)
    {
     if(commandText[i] == null) { commandText[i] = ""; }
     maxLength = Math.max(maxLength, commandText[i].length());
    }

    for(int i=0; i<commandText.length; i++)
    {
     while(commandText[i].length() < maxLength) { commandText[i] = commandText[i] + " "; }
    }

    Arrays.sort(commandText);

    System.out.println("\n\n\n--- Command List ---");
    for(int i=0; i<commandText.length; i++)
    {
     if(commandText[i].trim().length() > 0)
     {
      command = CommandFactory.getCommand(commandText[i]);
      description = command.getDescription();
      if(description != null) { System.out.println("    " + commandText[i] + " " + description); }
     }

    }
    return new GetInputFromUser();
   }
  }



  static private class ShutDown implements Command
  {
   //  shuts down this client. server needs to be independently shut down
   public boolean endProgram()     { return true; }
   public String  getDescription() { return "End the program"; }
   public Command run() throws IOException
   {
    //  close the multicast socket without send a final message
    listener.close(null);
    return this;
   }
  }



  static private class ShowTheServers implements Command
  {
   // display a list of all the servers that have music in our play list
   public boolean endProgram() { return false; }
   public String  getDescription() { return "List the servers"; }
   public Command run()
   {
    String[] servers;
    int      songCount;

    servers = PlayList.getInstance().getAllServers();
    if(servers.length < 1)
    {
     System.out.println("\n\n--- The play list is empty.");
    }
    else
    {
     System.out.print("\n\n---\nThe play list contains music from " + servers.length + " server");
     if(servers.length != 1) { System.out.print("s"); }
     System.out.println(".");

     for(int i=0; i<servers.length; i++)
     {
      songCount = PlayList.getInstance().getAllSongs(servers[i]).length;
      System.out.print("   " + servers[i] + " - " + songCount + " song");
      if(songCount != 1) { System.out.print("s"); }
      System.out.println(".");
     }
     System.out.println("---");
    }
    return new GetInputFromUser();
   }
  }  // class ShowTheServers


  static private class UserInputError implements Command
  {
   public boolean endProgram() { return false; }
   public String  getDescription() { return null; }
   public Command run()
   {
    System.out.println("I don't understand that command. Try again.");
    return new GetInputFromUser();
   }
  }  // class UserInputError



  abstract static private class CommandRequiringUserInput
  {
   private BufferedReader keyboard;
   private String         userInput;

   public CommandRequiringUserInput()
   {
    keyboard       = new BufferedReader(new InputStreamReader(System.in));
    this.userInput = null;
   }

   public String readFromKeyboard(String prompt) throws IOException
   {
    System.out.print(prompt);
    this.userInput = keyboard.readLine().trim();
    return this.userInput;
   }

   public String getUserInput() { return this.userInput; }

  }  // CommandRequiringUserInput

  static private class GetInputFromUser extends CommandRequiringUserInput implements Command
  {

   public boolean endProgram() { return false; }
   public String  getDescription() { return null; }
   public Command run() throws IOException
   {
    //  ask the user to input a command
    do { super.readFromKeyboard ("\n\n>>>>>> Enter a command (? to get help): "); }
    while(super.getUserInput() == "");

    return CommandFactory.getCommand(super.getUserInput());
   }  // run

  }   // class GetInputFromuser




}  // class Customer