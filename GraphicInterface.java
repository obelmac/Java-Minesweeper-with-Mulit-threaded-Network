package finalProject;

import javax.swing.*;
import javax.swing.Timer;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class GraphicInterface extends JFrame {
	
	private static final long serialVersionUID = 1L;
	final static int GRID_SIZE = 16;
	final static int NUMBER_OF_MINES = 40;
	public static Timer timer;
	public static int numOfFlags = NUMBER_OF_MINES;
	public static JLabel flagsLabel = new JLabel("" + numOfFlags);
	public static JLabel scoreTimerLabel = new JLabel("1000");
	public static JLabel gameOverLabel = new JLabel("-----------");
	public static ImagePanel[][] gameArr = new ImagePanel[16][16]; //Holds the state of the board
	public static int winConditionClicks = (GRID_SIZE*GRID_SIZE) - NUMBER_OF_MINES;
	
	public GraphicInterface() {
		this.setSize(270, 340);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		this.setResizable(false);
		this.setLayout(new BorderLayout());
	
		//Create Menus
		setJMenuBar(createMenus());
		//Create Board and set mines randomly
		this.add(createGrid(), BorderLayout.CENTER);
		setMines();
		setNumOfAdjacentMines();
		//Create Status Panel
		this.add(createStatusBar(), BorderLayout.SOUTH);
		
		//start timer
		timer.start();	
	}
	
	private JMenuBar createMenus() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		
		class NewFileListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				GraphicInterface.gameOverProcedure();	
			}	
		}
		class OpenFileListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				GraphicInterface.loadSavedGame();				
			}	
		}
		class SaveFileListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				GraphicInterface.saveGame();				
			}	
		}
		class highScoreListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				GraphicInterface.getHighScores();			
			}	
		}
		class ExitFileListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);			
			}	
		}
		
		JMenuItem newG = new JMenuItem("New");
		newG.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		JMenuItem open = new JMenuItem("Open");
		open.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		JMenuItem save = new JMenuItem("Save");
		save.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		JMenuItem leaderScoreBoard = new JMenuItem("Leader Score Board");
		leaderScoreBoard.setAccelerator(KeyStroke.getKeyStroke('L', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));
		JMenuItem exit = new JMenuItem("Exit"); 
		exit.setAccelerator(KeyStroke.getKeyStroke('E', Toolkit.getDefaultToolkit ().getMenuShortcutKeyMask()));

		newG.addActionListener(new NewFileListener());
		open.addActionListener(new OpenFileListener());
		save.addActionListener(new SaveFileListener());
		leaderScoreBoard.addActionListener(new highScoreListener());	
		exit.addActionListener(new ExitFileListener());
		menu.add(newG);
		menu.add(open);
		menu.add(save);
		menu.add(leaderScoreBoard);
		menu.add(exit);
		menuBar.add(menu);
		return menuBar;		
	}
	
	public JPanel createGrid() {
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(GRID_SIZE,GRID_SIZE, 0, 0)); 
		
		class cellListener implements MouseListener{
			@Override
			public void mouseClicked(MouseEvent e) {
				Object temp1 = e.getSource();
				ImagePanel temp2 = (ImagePanel) temp1;
				
				 if(e.getButton() == MouseEvent.BUTTON1) {	 
		            //System.out.println("LeftClick");
		            temp2.leftClickHandler();   		            	
			     }
				 else if(e.getButton() == MouseEvent.BUTTON3) {
	        	    //System.out.println("RightClick");
	        	    temp2.rightClickHandler();
				 }			
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}	
		}
		
		ImagePanel temp;
		for(int i = 0; i < GRID_SIZE; i++) {
			for(int j = 0; j < GRID_SIZE; j++) {
				temp = new ImagePanel("10.png");
				gameArr[i][j] = temp; 
				temp.setCoordinates(i, j);
				temp.addMouseListener(new cellListener());
				gridPanel.add(temp);
			}
		}		
		return gridPanel;	
	}
	
	public JPanel createStatusBar() {
		JPanel statusPanel = new JPanel();
		JLabel scoreLabel = new JLabel("Score: ");
		JButton restartButton = new JButton("Restart");
		
		class playAgainButtonListener implements ActionListener {		
		    public void actionPerformed(ActionEvent event) {
		    	GraphicInterface.gameOverProcedure();//restart procedure       		     
		    }

		}
		
		class timerListener implements ActionListener {		
		    public void actionPerformed(ActionEvent event) {
		        scoreTimerLabel.setText((Integer.parseInt(scoreTimerLabel.getText()) - 1) + "");
		        //game over if zero
		        if((Integer.parseInt(scoreTimerLabel.getText())) <= 0){
		        	timer.stop();
		        	scoreTimerLabel.setText("0");
		        	ImagePanel.endGame(); //display mines 
		        	GraphicInterface.gameOverLabel.setText("Game Over");
		        }		     
		    }
		}
		
		restartButton.addActionListener(new playAgainButtonListener());
		timer = new Timer(1000, new timerListener());
		
		statusPanel.add(GraphicInterface.flagsLabel);
		statusPanel.add(restartButton);
		statusPanel.add(scoreLabel);	
		statusPanel.add(scoreTimerLabel);
		statusPanel.add(GraphicInterface.gameOverLabel);	
		return statusPanel;
	}
	
	public static void setMines() {
		Random rand = new Random();
		int iIndex = rand.nextInt(GRID_SIZE -1);
		int jIndex = rand.nextInt(GRID_SIZE -1);
		
	    int mineCount = 0;
	    //System.out.println("\nDeploying Mines at the following coordinates: ");
	    while (mineCount < NUMBER_OF_MINES) {
	    	
	    	iIndex = rand.nextInt(GRID_SIZE);
			jIndex = rand.nextInt(GRID_SIZE);
			//System.out.print("[" + iIndex + " " + jIndex + "]");
			
			if((gameArr[iIndex][jIndex]).isMine() == false) {
				(gameArr[iIndex][jIndex]).setMine();
				mineCount += 1;
			}
			else
				continue;
	    }	
	}
	
	public static void setNumOfAdjacentMines() {
		int numOfMinesAdjacent = 0;	
		for(int i = 0; i < GRID_SIZE; i++) {
			for(int j = 0; j < GRID_SIZE; j++) {
				numOfMinesAdjacent = 0;
				
				try {
					//check left cell
					if(gameArr[i][j - 1].isMine())
						numOfMinesAdjacent += 1;
				} catch (IndexOutOfBoundsException e) {}
				try {
					//check right
					if(gameArr[i][j + 1].isMine())
						numOfMinesAdjacent += 1;
				} catch (IndexOutOfBoundsException e) {}
				try {
					//check up
					if(gameArr[i - 1][j].isMine())
						numOfMinesAdjacent += 1;
				} catch (IndexOutOfBoundsException e) {}
				try {
					//check down
					if(gameArr[i + 1][j].isMine())
						numOfMinesAdjacent += 1;
				} catch (IndexOutOfBoundsException e) {}
				try {
					//check upper left
					if(gameArr[i - 1][j - 1].isMine())
						numOfMinesAdjacent += 1;
				} catch (IndexOutOfBoundsException e) {}
				try {
					//check upper right
					if(gameArr[i - 1][j + 1].isMine())
						numOfMinesAdjacent += 1;
				} catch (IndexOutOfBoundsException e) {}
				try {
					//check lower left
					if(gameArr[i + 1][j - 1].isMine())
						numOfMinesAdjacent += 1;
				} catch (IndexOutOfBoundsException e) {}
				try {
					//check lower right
					if(gameArr[i + 1][j + 1].isMine())
						numOfMinesAdjacent += 1;	
				} catch (IndexOutOfBoundsException e) {}
		
					gameArr[i][j].setNumOfAdjacentMines(numOfMinesAdjacent);				
				}
			}		
	}
	
	public static void gameOverProcedure() {	
		//reset all mines
		for(int i = 0; i < GRID_SIZE; i++) {
			for(int j = 0; j < GRID_SIZE; j++) {
				gameArr[i][j].paintDefaultImage();
				gameArr[i][j].isClicked = false;
				gameArr[i][j].isMine = false;
				gameArr[i][j].hasFlag = false;
				gameArr[i][j].numOfAdjacentMines = 0;
				gameArr[i][j].setCoordinates(i, j);	
				gameArr[i][j].hasBeenThroughRecursiveCheck = false;
			}
		}		
		setMines();
		setNumOfAdjacentMines();
		
		//reset Graphic Interface Variables
		GraphicInterface.numOfFlags = NUMBER_OF_MINES;
		GraphicInterface.flagsLabel.setText("" + numOfFlags);
		GraphicInterface.scoreTimerLabel.setText("1000");
		GraphicInterface.gameOverLabel.setText("-----------");
		GraphicInterface.timer.start();
		winConditionClicks = (GRID_SIZE*GRID_SIZE) - NUMBER_OF_MINES;
	}
	
	public static void loadSavedGame() {	
		//Set Graphic Interface Variables to saved game variables
		GraphicInterface.timer.stop();	
		try {
			MinesweeperClient getAllGameNamesClient;	
			getAllGameNamesClient = new MinesweeperClient();
			Object[] gameNameList = getAllGameNamesClient.openAndGetAllConnection();
			
			Icon icon = null;
			Component frame = null;
			String s = (String)JOptionPane.showInputDialog(
			                    frame,
			                    "Please select a game you would like to load:\n",
			                    "Load Game",
			                    JOptionPane.PLAIN_MESSAGE,
			                    icon,
			                    gameNameList,
			                    "");

			//If a string was returned, say so.
			if ((s != null) && (s.length() > 0)) {
			   //System.out.println("\nLoading the following saved game: " + s);
			   MinesweeperClient loadSavedGameClient;
			   loadSavedGameClient = new MinesweeperClient();
			   SavedGame gameToLoad = loadSavedGameClient.openAndLoadConnection(s);
			   System.out.println("LOADING After Recieving Data in Main:");
			   //Load returned game
			   for(int i = 0; i < GRID_SIZE; i++) {
					for(int j = 0; j < GRID_SIZE; j++) {
						GraphicInterface.gameArr[i][j].setImage(gameToLoad.gameArrData[i][j].currentImgPath); 
						GraphicInterface.gameArr[i][j].isClicked = gameToLoad.gameArrData[i][j].isClicked; 
						GraphicInterface.gameArr[i][j].isMine = gameToLoad.gameArrData[i][j].isMine; 
						GraphicInterface.gameArr[i][j].hasFlag = gameToLoad.gameArrData[i][j].hasFlag; 
						GraphicInterface.gameArr[i][j].numOfAdjacentMines = gameToLoad.gameArrData[i][j].numOfAdjacentMines;
						GraphicInterface.gameArr[i][j].iIndex = gameToLoad.gameArrData[i][j].iIndex;
						GraphicInterface.gameArr[i][j].jIndex = gameToLoad.gameArrData[i][j].jIndex;
						GraphicInterface.gameArr[i][j].hasBeenThroughRecursiveCheck = gameToLoad.gameArrData[i][j].hasBeenThroughRecursiveCheck;	
						
						//alternative
						//Corner Cases
						////////
						//GraphicInterface.gameArr[i][j].repaint();
						GraphicInterface.timer.start();//resume timer
					}
				}	
			   //macro variables set from loaded game
			   System.out.println("Returned Labels--------------------------------------");
			   
			   System.out.println("numOfFlags :" + gameToLoad.getNumOfFlags());
			   System.out.println("winConditionClicks :" + gameToLoad.getWinConditionClicks());
			   System.out.println("flagsLabel :" + gameToLoad.getflagsLabel());
			   System.out.println("scoreTimerLabel :" + gameToLoad.getscoreTimerLabel());
			   System.out.println("gameOverLabel :" + gameToLoad.getgameOverLabel());
			   
			   
			   GraphicInterface.numOfFlags = gameToLoad.getNumOfFlags();
			   GraphicInterface.winConditionClicks  = gameToLoad.getWinConditionClicks();
			   GraphicInterface.flagsLabel.setText("" + gameToLoad.getflagsLabel());
			   GraphicInterface.scoreTimerLabel.setText("" + gameToLoad.getscoreTimerLabel());
			   GraphicInterface.gameOverLabel.setText("" + gameToLoad.getgameOverLabel());
			   
			}
			else
			   System.out.println("No saved games exist");
		
		} catch (Exception e) {
			System.out.println("load game list failed");
		}
				
		GraphicInterface.timer.start();//resume timer
	}
	
	public static void getHighScores() {
		GraphicInterface.timer.stop();	
		try {
			MinesweeperClient getHighScoresClient;	
			getHighScoresClient = new MinesweeperClient();
			Object[] highScoreList = getHighScoresClient.getHighScoresConnection();
			
			JOptionPane.showMessageDialog(null, highScoreList[0], "HIGH SCORE TABLE", JOptionPane.INFORMATION_MESSAGE);			
		} catch (Exception e) {
			System.out.println("Get High Score list failed");
		}
				
		GraphicInterface.timer.start();//resume timer	
	}
	
	public static void saveGame() {	
		GraphicInterface.timer.stop();//resume timer
		//Set Graphic Interface Variables to saved game variables
		MinesweeperClient saveGameClient;
		
		//Prompt User for save game name
		Object[] possibilities = null;
		Icon icon = null;
		Component frame = null;
		
		String saveName = (String)JOptionPane.showInputDialog(
		                    frame,
		                    "Please enter a name for the saved game:\n",
		                    "Save Game",
		                    JOptionPane.PLAIN_MESSAGE,
		                    icon,
		                    possibilities,
		                    "");
		
		if(saveName == null) {
			GraphicInterface.timer.start();//resume timer
		}
		else {
			//Image Panel is non serializable so need to store data in separate entity
			ImagePanelData[][] imagePanelDataArr = new ImagePanelData[16][16];
			for(int i = 0; i < GRID_SIZE; i++) {
				for(int j = 0; j < GRID_SIZE; j++) {
					imagePanelDataArr[i][j] = new ImagePanelData(
																gameArr[i][j].currentImgPath,
																gameArr[i][j].isClicked,
																gameArr[i][j].isMine, 
																gameArr[i][j].hasFlag,
																gameArr[i][j].numOfAdjacentMines,
																gameArr[i][j].iIndex,
																gameArr[i][j].jIndex,
																gameArr[i][j].hasBeenThroughRecursiveCheck
																 );
				}
			}	
			
			SavedGame savedGame = new SavedGame(saveName, 
					imagePanelDataArr,
					/*GraphicInterface.gameArr,*/ 
					GraphicInterface.numOfFlags,
					GraphicInterface.flagsLabel.getText(),
					GraphicInterface.scoreTimerLabel.getText(),
					GraphicInterface.gameOverLabel.getText(),
					GraphicInterface.winConditionClicks);
			
			try {
				saveGameClient = new MinesweeperClient(savedGame);
				saveGameClient.openAndSaveConnection();
			} catch (Exception e) {
				System.out.println("save game failed");
			}
		} 
		
		GraphicInterface.timer.start();//resume timer
	}
	
	public static void saveHighScore(String nameAndScore) {	
		GraphicInterface.timer.stop();//resume timer
		//Set Graphic Interface Variables to saved game variables
		MinesweeperClient saveGameClient;
	
		ImagePanelData[][] imagePanelDataArr = new ImagePanelData[16][16];
		for(int i = 0; i < GRID_SIZE; i++) {
			for(int j = 0; j < GRID_SIZE; j++) {
				imagePanelDataArr[i][j] = new ImagePanelData(
															gameArr[i][j].currentImgPath,
															gameArr[i][j].isClicked,
															gameArr[i][j].isMine, 
															gameArr[i][j].hasFlag,
															gameArr[i][j].numOfAdjacentMines,
															gameArr[i][j].iIndex,
															gameArr[i][j].jIndex,
															gameArr[i][j].hasBeenThroughRecursiveCheck
															 );
			}
		}	
		
		SavedGame savedGame = new SavedGame(nameAndScore, 
				imagePanelDataArr,
				/*GraphicInterface.gameArr,*/ 
				GraphicInterface.numOfFlags,
				GraphicInterface.flagsLabel.getText(),
				GraphicInterface.scoreTimerLabel.getText(),
				GraphicInterface.gameOverLabel.getText(),
				GraphicInterface.winConditionClicks);
		
			try {
				saveGameClient = new MinesweeperClient(savedGame);
				saveGameClient.saveHighScoreConnection();
			} catch (Exception e) {
				System.out.println("save high score failed");
			}
			
		GraphicInterface.timer.start();//resume timer
	}

	public static void main(String[] args) {
		GraphicInterface test = new GraphicInterface();
		test.setVisible(true);

	}


}
