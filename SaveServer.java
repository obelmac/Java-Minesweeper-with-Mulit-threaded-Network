package finalProject;

import java.io.*;
import java.net.*;
import java.io.*;
import java.net.*;
import java.util.Date;
import javax.swing.*;

public class SaveServer extends JFrame implements Runnable {
	
	  public static final int PORT = 3191;  
	// Text area for displaying contents
	  private JTextArea ta;  
	  // Number a client
	  private int clientNo = 0;
	  
	  public static void main(String[] args) {
		    SaveServer mts = new SaveServer();
		    mts.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    mts.setVisible(true);
	  }
	  
	  public SaveServer() {
		  ta = new JTextArea(10,10);
		  JScrollPane sp = new JScrollPane(ta);
		  this.add(sp);
		  this.setTitle("MultiThreadServer");
		  this.setSize(400,200);
		  Thread t = new Thread(this);
		  t.start();
	  }

	  public void run() {
		  try {
	        // Create a server socket
	        ServerSocket serverSocket = new ServerSocket(PORT);
	        ta.append("MultiThreadServer started at " + new Date() + '\n');
	    
	        while (true) {
	          // Listen for a new connection request
	          Socket socket = serverSocket.accept();
	    
	          // Increment clientNo
	          clientNo++;
	          
	          ta.append("Starting thread for client " + clientNo + " at " + new Date() + '\n');

	          // Find the client's host name, and IP address
	          InetAddress inetAddress = socket.getInetAddress();
	          ta.append("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + "\n");
	          ta.append("Client " + clientNo + "'s IP Address is " + inetAddress.getHostAddress() + "\n");
	          
	          // Create and start a new thread for the connection
	          new Thread(new HandleAClient(socket, clientNo)).start();
	        }
	      }
	      catch(IOException ex) {
	        System.err.println(ex);
	      }
		    
	  }
	  
	  // Define the thread class for handling new connection
	  class HandleAClient implements Runnable {
		  private Socket socket; // A connected socket
		  private int clientNum;
	    
	    /** Construct a thread */
		  public HandleAClient(Socket socket, int clientNum) {
	      this.socket = socket;
	      this.clientNum = clientNum;
		  }

	    /** Run a thread */
	    public void run() {
	    	
			 try {		   
			   System.out.println("Server is running clientNum: "+ clientNum);
			   ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
			   ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());

			   SavedGame game = (SavedGame)inStream.readObject();
			   //System.out.println("Server: " + game.toString());
			   
			   //retrieve all records for load game list
			   if(game.getName().equals("GetAllRecords")) {
				   System.out.println("------------------" + game.getName());
				   MinesweeperDataBase dataBaseConnection = new MinesweeperDataBase();
				   Object [] listOfGameNames = (Object[]) dataBaseConnection.getAllNames();
				   outStream.writeObject(listOfGameNames);//send list back to client
				   this.socket.close();
			   }
			   //load game return object
			   else if (game.getName().equals("LoadSavedGame")) {
				   System.out.println("In load game Server");
				   MinesweeperDataBase dataBaseConnection = new MinesweeperDataBase();
				   SavedGame returnSavedGame = dataBaseConnection.getSavedGame(game.getGameToLoad());
				   outStream.writeObject(returnSavedGame);//return game to load	
				   this.socket.close();
			   }
			   //save won game score return object
			   else if (game.getGameToLoad().equals("saveHighScore")) {
				   outStream.writeObject(game);//echo
				   System.out.println("In highscore game Server");
				   MinesweeperDataBase dataBaseConnection = new MinesweeperDataBase();
				   dataBaseConnection.insertHighScore(game);
				   this.socket.close();
			   }
			   //get high scores return object
			   else if (game.getName().equals("GetHighScores")) {
				   System.out.println("In load game Server");
				   MinesweeperDataBase dataBaseConnection = new MinesweeperDataBase();
				   Object[] returnHighScores = dataBaseConnection.getHighScores();
				   outStream.writeObject(returnHighScores);//return high scores
				   this.socket.close();
			   }
			   //save game
			   else {
				   outStream.writeObject(game);//echo
				   System.out.println("In save game Server");
				   MinesweeperDataBase dataBaseConnection = new MinesweeperDataBase();
				   dataBaseConnection.insertSavedGame(game);
				   this.socket.close();
			   }
			   
			   this.socket.close();
			   System.out.println("Server Thread Closed");
			   
			 }catch (Exception e) {
				 System.out.println(e.getMessage());
				 System.out.println("Server Thread Closed Exception");
			 }
	    }  
	  }
	  
}





