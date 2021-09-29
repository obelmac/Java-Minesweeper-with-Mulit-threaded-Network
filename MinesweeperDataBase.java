package finalProject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

public class MinesweeperDataBase {
	
	private PreparedStatement getHighScoresStmt;
	private PreparedStatement getAllRecordsStmt;
	private PreparedStatement insertNewRecordStmt;
	private PreparedStatement getRecordStmt;
	private PreparedStatement insertHighScoreStmt;
	private Connection conn;
	
	public MinesweeperDataBase (){
		
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:minesweeperDatabase.db");
			conn.setAutoCommit(true);
			insertHighScoreStmt = conn.prepareStatement("INSERT INTO highscores (name, score, gamedata) VALUES (?, ?, ?)");
			getHighScoresStmt = conn.prepareStatement("SELECT name, score FROM highscores ORDER BY 2 Desc Limit 5");
			getAllRecordsStmt = conn.prepareStatement("SELECT * FROM savedgames ORDER BY 1 Asc");
			getRecordStmt = conn.prepareStatement("SELECT * FROM savedgames WHERE name = ?");
			insertNewRecordStmt = conn.prepareStatement("INSERT INTO savedgames (name, gamedata) VALUES (?, ?)");
			System.out.println("Connected succesfully to database");
		} catch (SQLException e) {
			System.err.println("Connection error: " + e);
			System.exit(1);
		}
		
			
	}
	
	public void insertSavedGame(SavedGame gameState) {
		String name = gameState.getName();
		System.out.println("PRINT NAME DB: " + gameState.toString());// does not print
		byte [] data;
		PreparedStatement database = insertNewRecordStmt;
				
		//convert object to byte array and store it in database as "BLOB"
		try {
			SavedGame obj = gameState;
		    ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
	        ObjectOutputStream oos = new ObjectOutputStream(bos);
	        oos.writeObject(obj);
	        oos.flush();
	        data = bos.toByteArray();
	        
	        data = bos.toByteArray();
	        //Blob blob = new SerialBlob(data);
	        
	        //Inserting values
	        database.setString(1, name); 
	        database.setBytes(2, data); //why does this not work?????????? setBytes or Blob
	        int resSet = database.executeUpdate();
	        
	        if(resSet > 0)
	        	System.out.println("DB: saved game succesfull");
	        else
	        	System.out.println("DB: saved game NOT succesfull");
		} catch (IOException e) {
			System.out.print("1 " + e.getMessage());
		} catch (SerialException e) {
			System.out.print("2 " + e.getMessage());
		} catch (SQLException e1) {
			System.out.print("3 " + e1.getMessage());
		}
		finally {
		    if (conn != null) {
		        try {
		            conn.close();
		            System.out.println("DB: database connection closed");
		        } catch (SQLException e) {
		        	/*ignore*/
		        }
		    }
		}
	}	
	
	public SavedGame getSavedGame(String saveName) {
		String name = saveName;
		PreparedStatement database = getRecordStmt;
		
		try {
			database.setString(1, name);
			ResultSet rset = database.executeQuery();
			
			byte [] data = (byte[]) rset.getObject(2);
			
			//convert from Byte back to Java Object
			ByteArrayInputStream baip = new ByteArrayInputStream(data);
	        ObjectInputStream ois = new ObjectInputStream(baip);
	        SavedGame dataobj = (SavedGame) ois.readObject();
	        return dataobj ;
			
		} catch (IOException e) {
			System.out.print("1 " + e.getMessage());	
		} catch (SQLException e) {
			System.out.print("1 " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.print("1 " + e.getMessage());
		}
		finally {
		    if (conn != null) {
		        try {
		            conn.close();
		            System.out.println("DB: database connection closed");
		        } catch (SQLException e) {
		        	/*ignore*/
		        }
		    }
		}
		return null;		
	}
	
	public Object[] getAllNames() {
		
		PreparedStatement database = getAllRecordsStmt;
		try {
			int count = 0;
			ResultSet rset = database.executeQuery();
			while (rset.next()) {		
				Object o = rset.getObject(1);
				count += 1;
			}

			ResultSet rset2 = database.executeQuery();
			Object[] savedGameNames = new Object[count];
			count = 0;
			while (rset.next()) {		
				String o = rset2.getString(1);
				System.out.println("Records: ");
				savedGameNames[count] = o;
				count++;
			}
			return savedGameNames;
		
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		Object[] noSavedGames = {""};
		return noSavedGames;		
	}
	
	public Object[] getHighScores() {
		
		try {
			PreparedStatement database = getHighScoresStmt;
			String o = "";
			ResultSet rset = database.executeQuery();
			Object[] namesAndHighScores = new Object[1];
			
			while (rset.next()) {		
				o += rset.getString(1) + "\n";
				System.out.println(o);
			}
			namesAndHighScores[0] = o;	
			return namesAndHighScores;
		
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		Object[] noHighScores = {"No Entries And So No High Scores!!"};
		return noHighScores;		
	}
	
	public void insertHighScore(SavedGame gameState) {
		String name = gameState.getName();
		String score = gameState.getscoreTimerLabel();
		byte [] data;
		PreparedStatement database = insertHighScoreStmt;
				
		//convert object to byte array and store it in database as "BLOB"
		try {
			SavedGame obj = gameState;
		    ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
	        ObjectOutputStream oos = new ObjectOutputStream(bos);
	        oos.writeObject(obj);
	        oos.flush();
	        data = bos.toByteArray();
	        
	        data = bos.toByteArray();
	        //Blob blob = new SerialBlob(data);
	        
	        //Inserting values
	        database.setString(1, name); 
	        database.setString(2, score);
	        database.setBytes(3, data); //why does this not work?????????? setBytes or Blob
	        int resSet = database.executeUpdate();
	        
	        if(resSet > 0)
	        	System.out.println("DB: saved game succesfull");
	        else
	        	System.out.println("DB: saved game NOT succesfull");
		} catch (IOException e) {
			System.out.print("1 " + e.getMessage());
		} catch (SerialException e) {
			System.out.print("2 " + e.getMessage());
		} catch (SQLException e1) {
			System.out.print("3 " + e1.getMessage());
		}
		finally {
		    if (conn != null) {
		        try {
		            conn.close();
		            System.out.println("DB: database connection closed");
		        } catch (SQLException e) {
		        	/*ignore*/
		        }
		    }
		}
	}	
}
/*
	finally {
	    if (rs != null) {
	        try {
	            rs.close();
	        } catch (SQLException e) {ignored }
	    }
	    if (ps != null) {
	        try {
	            ps.close();
	        } catch (SQLException e) { ignored }
	    }
	    if (conn != null) {
	        try {
	            conn.close();
	        } catch (SQLException e) { ignored}
	    }
	}
*/

