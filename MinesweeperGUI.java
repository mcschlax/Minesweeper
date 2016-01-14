/**
Mark Schlax
MinesweeperGUI
Originaly Made: 6/4/2013
Creates and displays a GUI containing the Minesweeper game
utilizing the mouse as input for selecting the tiles in the game

Updated: 1/13/2016
Updated code style
**/

import javax.swing.JFrame;
import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;

public class MinesweeperGUI{
	
	//Creates and displays an instance of the Minefield
	public static void main(String[] args){
		JFrame frame = new JFrame("Minesweeper");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JComponent minefield = new Minefield();
		frame.add(minefield);
		
		frame.pack();
		frame.setVisible(true);
	}
}

/*
The interactive component utilizing the mouse as input for the minesweeper game
*/
class Minefield extends JComponent{
	int difficulty = 1;
	Minesweeper mineField  = new Minesweeper(difficulty);
	
	public Minefield(){
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent event){
				if (event.isMetaDown())
					processClick((event.getY() + 20) * - 1, (event.getX() + 20) * - 1);
				else
					processClick(event.getY(), event.getX());
			}
		});
	}
	
	/*
	incoming parameters determine location of click
	the sign of incoming parameters determine left (positive) or right (negative) click
	
	depending on location of click, a tile is exposed/flagged or a new game is started
	regardless, an update was made so board is repainted
	*/
	public void processClick(int x, int y){
		//select a tile with right or left click
		if (x < 0)
			mineField.expose(x/20 + 5, y/20);
		else
			mineField.expose(x/20 - 5, y/20);
		
		//select new difficulty
		if (x >= 48 && x <= 61 && y >= 3 && y <= 63)
			mineField = new Minesweeper(difficulty = 1);
		else if (x >= 63 && x <= 76 && y >= 3 && y <= 78)
			mineField = new Minesweeper(difficulty = 2);
		else if (x >= 78 && x <= 91 && y >= 3 && y <= 48)
			mineField = new Minesweeper(difficulty = 3);
		
		//board was updated so repaint
		repaint();
	}
	/*
	returns the Dimension of the board size based on the difficulty
	*/
	public Dimension getBoardSize(){
		if (difficulty == 2)
			return new Dimension(320, 320);
		else if (difficulty == 3)
			return new Dimension(600, 320);
		else
			return new Dimension(160, 160);
	}
	
	/*
	used once to make the starting size of the GUI
	*/
	public Dimension getPreferredSize(){
		return new Dimension(161, 261);
	}
	
	/*
	paints the GUI
	*/
	protected void paintComponent(Graphics graphic){
		int[][] field = mineField.toArray();
		int width = (int)getBoardSize().getWidth();
		int height = (int)getBoardSize().getHeight();
		int check = mineField.checkBoard();
		
		graphic.setColor(new Color(238, 238, 238));
		graphic.fillRect(0, 0, 601, 601);
		
		graphic.setColor(new Color(245, 245, 220));
		graphic.fillRect(0, 0, 160, 100);
		graphic.fillRect(0, 100, width, height);
		
		graphic.setColor(Color.black);
		graphic.drawRect(0, 0, 160, 100);
		graphic.drawRect(0, 100, width, height);
		
		if (check == 1)
			graphic.drawString("Congratualtions! You Won!", 5, 15);
		else if (check == 0)
			graphic.drawString("Sorry. You Lost.", 5, 15);
		
		graphic.drawString("Choose An Option Below To", 5, 30);
		graphic.drawString("Restart, Or Begin A Game", 5, 42);
		graphic.drawString("Beginner", 5, 60);
		graphic.drawRect(3, 48, 60, 13);
		graphic.drawString("Intermediate", 5, 75);
		graphic.drawRect(3, 63, 75, 13);
		graphic.drawString("Expert", 5, 90);
		graphic.drawRect(3, 78, 45, 13);
		
		// draw each tile
		for (int row = 0; row < field.length; row++)
			for (int col = 0; col < field[row].length; col++){
				//draw the black border around the tile
				graphic.setColor(Color.black);
				graphic.drawLine(col * 20, (row + 5) * 20, col * 20, (row + 5) * 20 + 20);
				graphic.drawLine(col * 20, (row + 5) * 20, col * 20 + 20, (row + 5) * 20);
				
				String tile = "";
				switch(field[row][col]){
					case 0:
						graphic.setColor(Color.black);
						tile = "" + field[row][col];
						break;
					case 1:
						graphic.setColor(Color.blue);
						tile = "" + field[row][col];
						break;
					case 2:
						graphic.setColor(new Color(50, 205, 50));
						tile = "" + field[row][col];
						break;
					case 3:
						graphic.setColor(Color.red);
						tile = "" + field[row][col];
						break;
					case 4:
						graphic.setColor(new Color(159, 0, 197));
						tile = "" + field[row][col];
						break; 
					case 5:
						graphic.setColor(new Color(128, 0, 0));
						tile = "" + field[row][col];
						break;
					case 6:
						graphic.setColor(new Color(64, 224, 208));
						tile = "" + field[row][col];
						break;
					case 7:
						graphic.setColor(Color.black);
						tile = "" + field[row][col];
						break;
					case 8:
						graphic.setColor(new Color(84, 84, 84));
						tile = "" + field[row][col];
						break;
					case 10:
						graphic.setColor(new Color(245, 245, 220));
						tile = " ";
						break;
					case 11:
						graphic.setColor(Color.red);
						tile = "" + (char) 1635;
						break;
				}
				graphic.drawString(tile, col * 20 + 5, (row + 6) * 20 - 5);
			}
	}
}