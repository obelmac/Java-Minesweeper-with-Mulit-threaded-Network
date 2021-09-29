package finalProject;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ImagePanel extends JPanel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	final String path0 = "0.png";
	final String path1 = "1.png";
	final String path2 = "2.png";
	final String path3 = "3.png";
	final String path4 = "4.png";
	final String path5 = "5.png";
	final String path6 = "6.png";
	final String path7 = "7.png";
	final String path8 = "8.png";
	final String path9 = "9.png";
	final String path10 = "10.png";
	final String path11 = "11.png";
	final String path12 = "12.png";
	
	public Image cell1Img = new ImageIcon(path1).getImage();
	public Image cell2Img = new ImageIcon(path2).getImage();
	public Image cell3Img = new ImageIcon(path3).getImage();
	public Image cell4Img = new ImageIcon(path4).getImage();
	public Image cell5Img = new ImageIcon(path5).getImage();
	public Image cell6Img = new ImageIcon(path6).getImage();
	public Image cell7Img = new ImageIcon(path7).getImage();
	public Image cell8Img = new ImageIcon(path8).getImage();
	public Image defaultCellImg = new ImageIcon(path10).getImage();
	public Image emptyCellImg = new ImageIcon(path0).getImage();
	public Image mineImg = new ImageIcon(path9).getImage();
	public Image flagImg = new ImageIcon(path11).getImage();
	public Image crossedFlagImg = new ImageIcon(path12).getImage();
	
	
	public String currentImgPath;
	public Boolean isClicked = false;
	public Boolean isMine = false;
	public Boolean hasFlag = false;
	public int numOfAdjacentMines = 0;
	public int iIndex = 0;
	public int jIndex = 0;
	public Boolean hasBeenThroughRecursiveCheck = false;
	public Image img;

		
	public ImagePanel(String img) {
		this(new ImageIcon(img).getImage());
		this.currentImgPath = path10;
	}
	
	public ImagePanel(Image img) {
		this.img = img;
		this.currentImgPath = path10;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setLayout(null); 
	}
	
	@Override
    public void paintComponent(Graphics g) {
       g.drawImage(img, 0, 0, null);
    } 
	
	public void rightClickHandler(){
		
		if(this.isClicked == true) {
			//do nothing
		}
		else if(GraphicInterface.numOfFlags == 0 && this.hasFlag == false) {
			//do nothing
		}
		else if(this.hasFlag == true) {
  		  this.hasFlag = false;
  		  this.img = defaultCellImg;
  		  this.repaint();
  		  currentImgPath = path10;
  		  GraphicInterface.numOfFlags += 1;
  		  GraphicInterface.flagsLabel.setText("" + GraphicInterface.numOfFlags);
  	  	}
		else {
      	  this.hasFlag = true;
      	  this.img = flagImg;
      	  this.repaint();
      	  currentImgPath = path11;
      	 GraphicInterface.numOfFlags -= 1;
      	 GraphicInterface.flagsLabel.setText("" + GraphicInterface.numOfFlags);
  	  }
	}
	
	public void leftClickHandler(){
		
		if(this.isClicked == true) {
			//do nothing
		}
		else if(this.hasFlag == true) {
			//do nothing
  	  	}
		else if(this.isMine) {
			//end game and reveal all mines and crossed flags 
			this.isClicked = true;
			endGame();
			GraphicInterface.gameOverLabel.setText("Game Over");
			GraphicInterface.scoreTimerLabel.setText("0");
			//JOptionPane.showMessageDialog(null, "Game Over, Start Again?");
			//GraphicInterface.gameOverProcedure();
		}
  	  	else {
  	  		this.isClicked = true; 		
  	  		//if no adjacent mines just paint empty or do recursive check for empty cells
  	  		if(numOfAdjacentMines < 1) {
  	  			this.recursiveCheck();
  	  		}
  	  		else {
  	  			this.paintNumOfMinesImg(this.numOfAdjacentMines); 
  	  			GraphicInterface.winConditionClicks -= 1;
  	  		}
  	  	}
		
		//check win condition after click
		if(GraphicInterface.winConditionClicks < 1) {
			GraphicInterface.timer.stop();
			Object[] winnerNameAndScore = null;
			String saveName = (String)JOptionPane.showInputDialog(
                    null,
                    "Congratulations You Won!, Your Score is: \n Please enter your name for the leader Board",
                    "You Won",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    winnerNameAndScore,
                    "");
			
			String result = saveName + ": " + GraphicInterface.scoreTimerLabel.getText();
			
			for(int i = 0; i < GraphicInterface.GRID_SIZE; i++) {
				for(int j = 0; j < GraphicInterface.GRID_SIZE; j++) {			
					GraphicInterface.gameArr[i][j].isClicked = true;
				}
			}
			GraphicInterface.saveHighScore(result);
		}
	}
	
	public static void endGame() {
		GraphicInterface.timer.stop();
		for(int i = 0; i < GraphicInterface.gameArr.length; i++) {
			for(int j = 0; j < GraphicInterface.gameArr[0].length; j++) {
				
				GraphicInterface.gameArr[i][j].isClicked = true;
				
				if(GraphicInterface.gameArr[i][j].isMine() != true &&
				   GraphicInterface.gameArr[i][j].hasFlag() == true) {
					//if flag was on spot with no mine print crossed flag
					GraphicInterface.gameArr[i][j].paintCrossedFlagImage();
				}
				else if(GraphicInterface.gameArr[i][j].isMine() == true &&
						GraphicInterface.gameArr[i][j].hasFlag() == true) {
					//if mine and flag print flag
					GraphicInterface.gameArr[i][j].paintFlagImage();
				}	
				else if(GraphicInterface.gameArr[i][j].isMine() == true) {
					//if just a mine then print the mine
					GraphicInterface.gameArr[i][j].paintMineImage();
				}									
			}			
		}			
	}
	
	public void paintNumOfMinesImg(int x) {
		if(x == 1) {
			this.img = cell1Img; 
			this.currentImgPath = path1;
			this.repaint();
		}
		if(x == 2) {
			this.img = cell2Img;
			this.currentImgPath = path2;
			this.repaint();
		}
		if(x == 3) {
			this.img = cell3Img;
			this.currentImgPath = path3;
			this.repaint();
		}
		if(x == 4) {
			this.img = cell4Img;
			this.currentImgPath = path4;
			this.repaint();
		}
		if(x == 5) {
			this.img = cell5Img; 
			this.currentImgPath = path5;
			this.repaint();
		}
		if(x == 6) {
			this.img = cell6Img;
			this.currentImgPath = path6;
			this.repaint();
		}
		if(x == 7) {
			this.img = cell7Img; 
			this.currentImgPath = path7;
			this.repaint();
		}
		if(x == 8) {
			this.img = cell8Img; 
			this.currentImgPath = path8;
			this.repaint();
		}
	}
	
	public void recursiveCheck() {	
		if(this.hasBeenThroughRecursiveCheck == true) {
			//end cycle
		}
		else if(this.numOfAdjacentMines >= 1) {
			paintNumOfMinesImg(this.numOfAdjacentMines);
			hasBeenThroughRecursiveCheck = true;
			this.isClicked = true;
			GraphicInterface.winConditionClicks -= 1;
		}
		else {
			paintEmptyCellImg();
			this.isClicked = true;
			this.hasBeenThroughRecursiveCheck = true;
			GraphicInterface.winConditionClicks -= 1;
			//then call recursive check on all adjacent cells with try catch in case if border cell
			try {
				//check left cell
				GraphicInterface.gameArr[iIndex][jIndex - 1].recursiveCheck();				
			} catch (IndexOutOfBoundsException e) {}
			try {
				//check right
				GraphicInterface.gameArr[iIndex][jIndex + 1].recursiveCheck();
			} catch (IndexOutOfBoundsException e) {}
			try {
				//check up
				GraphicInterface.gameArr[iIndex - 1][jIndex].recursiveCheck();
			} catch (IndexOutOfBoundsException e) {}
			try {
				//check down
				GraphicInterface.gameArr[iIndex + 1][jIndex].recursiveCheck();
			} catch (IndexOutOfBoundsException e) {}
			try {
				//check upper left
				GraphicInterface.gameArr[iIndex - 1][jIndex - 1].recursiveCheck();
			} catch (IndexOutOfBoundsException e) {}
			try {
				//check upper right
				GraphicInterface.gameArr[iIndex - 1][jIndex + 1].recursiveCheck();
			} catch (IndexOutOfBoundsException e) {}
			try {
				//check lower left
				GraphicInterface.gameArr[iIndex + 1][jIndex - 1].recursiveCheck();
			} catch (IndexOutOfBoundsException e) {}
			try {
				//check lower right
				GraphicInterface.gameArr[iIndex + 1][jIndex + 1].recursiveCheck();	
			} catch (IndexOutOfBoundsException e) {}
		}
	}
	
	public void setCoordinates(int i, int j) {this.iIndex = i; this.jIndex = j;}
	public void setMine() {this.isMine = true;}
	public void setNumOfAdjacentMines(int x) {this.numOfAdjacentMines = x;}
	public boolean isMine() {return isMine;}
	public boolean hasFlag() {return hasFlag;}
	public void paintMineImage() {this.img = mineImg; this.repaint(); this.currentImgPath = path9;}
	public void paintFlagImage() {this.img = flagImg; this.repaint(); this.currentImgPath = path11;}
	public void paintEmptyCellImg() {this.img = emptyCellImg; this.repaint(); this.currentImgPath = path0;}
	public void paintCrossedFlagImage() {this.img = crossedFlagImg; this.repaint(); this.currentImgPath = path12;}
	public void paintDefaultImage() {this.img = defaultCellImg; this.repaint(); this.currentImgPath = path10;}
	public void setImage(String img) {this.img = new ImageIcon(img).getImage(); this.repaint();}
	
}
