package finalProject;

import java.io.Serializable;
import javax.swing.JLabel;

public class SavedGame implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public String gameToLoad = "";
	public String name;	
	private int numOfFlags;
	private String flagsLabel;
	private String scoreTimerLabel;
	private String gameOverLabel;
	//private ImagePanel[][] gameArr;
	public ImagePanelData[][] gameArrData;
	private int winConditionClicks;

	public SavedGame(String name, ImagePanelData[][] gameArrData, /*ImagePanel[][] gameArr,*/ int numOfFlags, String flagsLabel, 
					 String scoreTimerLabel, String gameOverLabel, int winConditionClicks) {	
		this.name = name;
		this.gameArrData = gameArrData;
		//this.gameArr = gameArr;
		this.numOfFlags =numOfFlags;
		this.flagsLabel = flagsLabel;
		this.scoreTimerLabel = scoreTimerLabel;
		this.gameOverLabel = gameOverLabel;
		this.winConditionClicks = winConditionClicks;
	}
	
	public SavedGame(String name) {
		this.name = name;
	}
	
	public String getGameToLoad(){return this.gameToLoad;}
	public String getName(){return this.name;}
	public ImagePanelData[][] getGameArrData(){return this.gameArrData;}
	//public ImagePanel[][] getGameArr(){return this.gameArr;}
	public int getNumOfFlags(){return this.numOfFlags;}
	public String getflagsLabel(){return this.flagsLabel;}
	public String getscoreTimerLabel(){return this.scoreTimerLabel;}
	public String getgameOverLabel(){return this.gameOverLabel;}
	public int getWinConditionClicks(){return this.winConditionClicks;}
	
	public String toString() { return "this SavedGame Object " + getName() + " contains the follwing items: "
								+ "\n[\ngameDataArr(),\n" + "NumOfFlags(),\n" + "flagsLabel(),\n"
								+ "getscoreTimerLabel(),\n" + "getscoreTimerLabel(),\n" 
								+ "getgameOverLabel(),\n" + "getWinConditionClicks(),\n]";
	}

}
