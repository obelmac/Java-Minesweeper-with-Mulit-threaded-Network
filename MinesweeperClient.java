package finalProject;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.*;


public class MinesweeperClient {

	  SavedGame gameToSave = null;
	
	  public MinesweeperClient(SavedGame gameToSave) {
		  this.gameToSave = gameToSave;
		  //openAndSaveConnection();	    
	  }
	  
	  public Object[] getHighScoresConnection() {
		  try {
			  //set packet string to GetAllRecords if want names of saved games
			  this.gameToSave = new SavedGame("GetHighScores");
			  // Establish connection with the server
			  Socket socket = new Socket("localhost", SaveServer.PORT);
			  System.out.println("Client connection started");
			  
			  // Create an input and output stream to the server
			  ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
			  ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
			  
			  //send packet
			  SavedGame game = this.gameToSave;
			  outStream.writeObject(game);
			  
			  //Receive Object Array [] with list of saved games
			  Object[] echoObject = (Object[])inStream.readObject();
			  socket.close();  
			  System.out.println("Client connection closed");
			  return echoObject;
		  }
		  catch(Exception e) {
			  System.out.print("client Exception: Client Closed");
		  }
		  
		Object[] noSavedGames = {""};
		return noSavedGames;
		  
	  }
	
	  public void openAndSaveConnection() {
	  
		  try {
			  // Establish connection with the server
			  Socket socket = new Socket("localhost", SaveServer.PORT);
			  System.out.println("Client connection started");
			  
			  // Create an input and output stream to the server
			  ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
			  ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
			  
			  //send packet
			  SavedGame game = this.gameToSave;
			  outStream.writeObject(game);
			  
			  //Receive echo
			  SavedGame echoObject = (SavedGame)inStream.readObject();
			  System.out.println("Client: " + echoObject.toString() + "\n");
			  JOptionPane.showMessageDialog(null, "saved game successfull");
			  
			  socket.close();
			  System.out.println("Client connection closed");	
		  }
		  catch(Exception e) {
			  System.out.print("client Exception: Client Closed");
		  }		  
	  }
	  
	  public SavedGame openAndLoadConnection(String loadGameName) {
		  
		  try {
			  // Establish connection with the server
			  Socket socket = new Socket("localhost", SaveServer.PORT);
			  System.out.println("Client connection started");
			  
			  // Create an input and output stream to the server
			  ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
			  ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
			  
			  //send packet
			  SavedGame game = new SavedGame("LoadSavedGame");///need name of game retrieving
			  game.gameToLoad = loadGameName;//game to be loaded save in variable saved game
			  outStream.writeObject(game);
			  
			  //Receive game to load
			  SavedGame echoObject = (SavedGame)inStream.readObject();
			  JOptionPane.showMessageDialog(null, "loaded game successfull");
			  socket.close();
			  System.out.println("Client connection closed");
			  return echoObject;		 	
		  }
		  catch(Exception e) {
			  System.out.print("client Exception: Client Closed");
		  }
		return null;		  
	  }
	  
	  public Object[] openAndGetAllConnection() {
		  
		  try {
			  //set packet string to GetAllRecords if want names of saved games
			  this.gameToSave = new SavedGame("GetAllRecords");
			  // Establish connection with the server
			  Socket socket = new Socket("localhost", SaveServer.PORT);
			  System.out.println("Client connection started");
			  
			  // Create an input and output stream to the server
			  ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
			  ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
			  
			  //send packet
			  SavedGame game = this.gameToSave;
			  outStream.writeObject(game);
			  
			  //Receive Object Array [] with list of saved games
			  Object[] echoObject = (Object[])inStream.readObject();
			  socket.close();  
			  System.out.println("Client connection closed");
			  return echoObject;
		  }
		  catch(Exception e) {
			  System.out.print("client Exception: Client Closed");
		  }
		  
		Object[] noSavedGames = {""};
		return noSavedGames;		  
	  }
	  
	  public MinesweeperClient() {
		    
	  }
	  
	public void saveHighScoreConnection() {
		try {
			  // Establish connection with the server
			  Socket socket = new Socket("localhost", SaveServer.PORT);
			  System.out.println("Client connection started");
			  
			  // Create an input and output stream to the server
			  ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
			  ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
			  
			  //send packet
			  SavedGame game = this.gameToSave;
			  this.gameToSave.gameToLoad = "saveHighScore";
			  //System.out.println("name: " + this.gameToSave.gameToLoad);
			  //System.out.println("name: " + this.gameToSave.name);
			  outStream.writeObject(game);
			  
			  //Receive echo
			  SavedGame echoObject = (SavedGame)inStream.readObject();
			  System.out.println("Client: " + echoObject.toString() + "\n");
			  JOptionPane.showMessageDialog(null, "saved high score successfull");
			  
			  socket.close();
			  System.out.println("Client connection closed");	
		  }
		  catch(Exception e) {
			  System.out.print("client Exception: Client Closed");
		  }	
		
	}
	  
	  
}
