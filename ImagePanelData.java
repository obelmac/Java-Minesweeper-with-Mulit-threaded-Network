package finalProject;

import java.awt.Image;
import java.io.Serializable;

public class ImagePanelData implements Serializable{
 
	private static final long serialVersionUID = 1L;
	//public Image img;
	public String currentImgPath = null;
	public Boolean isClicked = false;
	public Boolean isMine = false;
	public Boolean hasFlag = false;
	public int numOfAdjacentMines = 0;
	public int iIndex = 0;
	public int jIndex = 0;
	public Boolean hasBeenThroughRecursiveCheck = false;

	
	public ImagePanelData(String currentImgPath, Boolean isClicked, Boolean isMine,
						  Boolean hasFlag, int numOfAdjacentMines,
						  int iIndex, int jIndex, 
						  Boolean hasBeenThroughRecursiveCheck) {
		//this.img = img;
		this.currentImgPath = currentImgPath;
		this.isClicked = isClicked;
		this.isMine = isMine;
		this.hasFlag = hasFlag;
		this.numOfAdjacentMines = numOfAdjacentMines;
		this.iIndex = iIndex;
		this.jIndex = jIndex;
		this.hasBeenThroughRecursiveCheck = hasBeenThroughRecursiveCheck;	
	}

}
