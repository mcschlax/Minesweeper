/**
Mark Schlax
MinesweeperGUI
Originaly Made: 6/4/2013
A classic, remade.
A gird of tiles have mines or valeus hidden until clicked.
Selecting a tile with a mine ends the game.
Selecting on a tile without a mine will display a value representing how many adjacent, 
including diagnoal, mines are to that tile.

Updated: 1/13/2016
Updated code style
**/

public class Minesweeper{
	//three choices of difficulty
	private final int[] beginner = {8, 8, 10};
	private final int[] intermediate = {16, 16, 40};
	private final int[] expert = {16, 30, 99};
	
	/*
	dimensions will be set to a difficulty
	bombs mark the bomb locations
	exposed mark which tiles are exposed
	flagged mark which tiles are flagged
	sol marks the solution values, the value signifying adjacent bombs
	firstMove marks whether the first move has been made
	*/
	private int[] dimensions = {0, 0, 0};
	private boolean[][] bombs = new boolean[dimensions[0]][dimensions[1]];
	private boolean[][] exposed = new boolean[dimensions[0]][dimensions[1]];
	private boolean[][] flagged = new boolean[dimensions[0]][dimensions[1]];
	private int[][] sol = new int[dimensions[0]][dimensions[1]];
	private boolean firstMove = true;
	
	/*
	constructor
	using parameter difficulty, sets up the minefield
	*/
	public Minesweeper(int difficulty){
		switch (difficulty){
			case 1:
				dimensions = beginner;
				break;
			case 2:
				dimensions = intermediate;
				break;
			case 3:
				dimensions = expert;
				break;
		}
		
		resetMinefield();
	}
	
	/*
	resets the minefield to default values
	also used as inital helper in constructor
	*/
	private void resetMinefield(){
		sol = new int[dimensions[0]][dimensions[1]];
		bombs = new boolean[dimensions[0]][dimensions[1]];
		exposed = new boolean[dimensions[0]][dimensions[1]];
		flagged = new boolean[dimensions[0]][dimensions[1]];
		firstMove = true;
	}
	
	/*
	randomly places the bombs on the minefield
	called after user makes a move so first tile does not contain a bomb
	*/
	private void placeMines(int userRow, int userCol){
		for (int bombCount = 0, row = (int)(Math.random() * dimensions[0]), col = (int)(Math.random() * dimensions[1]); bombCount < dimensions[2];){
			//insures the first (row, col) selected by the user doesn't result in a mine
			if (row != userRow && col != userCol){
				bombs[row][col] = !bombs[row][col];
				if (bombs[row][col]) bombCount++;
				else bombCount--;
			}
			row = (int)(Math.random() * dimensions[0]);
			col = (int)(Math.random() * dimensions[1]);
		}
		//first move was already made, update firstMove
		firstMove = false;
		
		//minefield was made, it needs to be solved
		solveMinefield();
	}
	
	/*
	solves the minefield by determining the values of each tile
	*/
	private void solveMinefield(){
		//go through each tile
		for (int row = 0; row < dimensions[0]; row++)
			for (int col = 0; col < dimensions[1]; col++)
				// if there's a bomb in the tile
				if (bombs[row][col])
					//update the adjacent, including diagnoal, tiles' value
					for (int row2 = row - 1; row2 <= (row + 1) && row2 < dimensions[0]; row2++)
						for (int col2 = col - 1; col2 <= (col + 1) && col2 < dimensions[1]; col2++){
							//make sure to not go out of bounds
							if (row2 < 0)
								row2++;
							if (col2 < 0)
								col2++;
							
							//bombs have a value of 9 
							if (!bombs[row2][col2])
								sol[row2][col2]++;
							else sol[row2][col2] = 9;
						}
	}
	
	/*
	given a row and colummn, will either
	place the mines, if it's the first move
	expose the tile, if it's not flagged
	toggle the flag, remember that negative (row,col) is what's used to signal flag
	*/
	public void expose(int row, int col){
		if (row >= 0 && row < dimensions[0] && col >= 0 && col < dimensions[1]){
			//first move, place mines
			if (firstMove)
				placeMines(row, col);
			//expose the tile if it's not flagged
			if (!flagged[row][col] && !exposed[row][col])
				if (sol[row][col] == 0){
					exposeZeros(row, col);
					exposeAroundZeros();
				}
				else
					exposed[row][col] = true;
		}
		else if (row <= -1 && row >= dimensions[0] * - 1 && col <= -1 && col >= dimensions[1] * -1)
			//toggle the flag, remember that negative (row,col) is what's used to signal flag
			flagged[row * -1 - 1][col * -1 - 1] = !flagged[row * -1 - 1][col * -1 - 1];
	}
	
	/*
	recursively exposes surrounding zeros if the tile is a zero
	*/
	private void exposeZeros(int row, int col){
		exposed[row][col] = true;
		if (col + 1 < dimensions[1] && sol[row][col + 1] == 0 && !bombs[row][col + 1] && !exposed[row][col + 1])
			exposeZeros(row,col + 1);
		if (row + 1 < dimensions[0] && sol[row + 1][col] == 0 && !bombs[row + 1][col] && !exposed[row + 1][col])
			exposeZeros(row + 1,col);
		if (col - 1 >=  0 && sol[row][col - 1] == 0 && !bombs[row][col - 1] && !exposed[row][col - 1])
			exposeZeros(row,col - 1);
		if (row - 1 >= 0 && sol[row - 1][col] == 0 && !bombs[row - 1][col] && !exposed[row - 1][col])
			exposeZeros(row - 1, col);
	}
	
	/*
	since a zero has no bombs around it, expose all the tiles a around a zero
	*/
	private void exposeAroundZeros(){
		for (int row = 0; row < dimensions[0]; row++)
			for (int col = 0; col < dimensions[1]; col++)
				if (exposed[row][col] && sol[row][col] == 0)
					for (int row2 = row - 1; row2 <= (row + 1) && row2 < dimensions[0]; row2++)
						for (int col2 = col - 1; col2 <= (col + 1) && col2 < dimensions[1]; col2++){
							//make sure to not go out of bounds
							if (row2 < 0)
								row2++;
							if (col2 < 0)
								col2++;
							
							if (!exposed[row2][col2])
								exposed[row2][col2] = true;
						}
	}
	
	/*
	returns the state of the game
	0 is loss
	1 is win
	2 is in progress
	*/
	public int checkBoard(){
		int count = 0;
		for (int row = 0; row < dimensions[0]; row++)
			for (int col = 0; col < dimensions[1]; col++)
				if (exposed[row][col] && bombs[row][col])
					//bomb is exposed, loss
					return 0;
				else if (exposed[row][col])
					count++;
		if (count == dimensions[0] * dimensions[1] - dimensions[2])
			//number of tiles that are not bombs == number of exposed tiles, win
			return 1;
		//in progress
		return 2;
	}
	
	/*
	converts the minefield into a double array of
	non-exposed, exposed, flagged, tiles such that
	0 - 8 is value of a tile
	9 is a bomb
	11 is a flag
	10 is unexposed
	*/
	public int[][] toArray(){
		int[][] arry = new int[dimensions[0]][dimensions[1]];
		int check  = checkBoard();
		
		for (int row = 0; row < dimensions[0]; row++)
			for (int col = 0; col < dimensions[1]; col++)
				if (check == 2){
					if (exposed[row][col]){
						if (bombs[row][col])
							//9 is a bomb
							arry[row][col] = 9;
						else
							// 0-8 is value of tile
							arry[row][col] = sol[row][col];
					}
					// 11 is flag
					else if (flagged[row][col])
						arry[row][col] = 11;
					// 10 is unexposed
					else
						arry[row][col] = 10;
				}
				else
					//if the game is over, bombs tiles are 11
					//every other tile is just their value
					if (bombs[row][col])
						arry[row][col] = 11;
					else
						arry[row][col] = sol[row][col];
		return arry;
	}
}