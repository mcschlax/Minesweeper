//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//Mark Schlax
//MinesweeperGUI
//6/4/2013
//The interactive JComponent that accepts input as a mouse through MouseListener, and is painted through java.awt.Graphic's paintCompoent method.
//The source code's length without comments was 103 lines long; not that it matters, but I like it efficient and short
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//++++++++++++++++++++++++++++++++++
//Importing only the necesary classes
//to promote memory efficiency
//++++++++++++++++++++++++++++++++++
import javax.swing.JFrame;
import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
public class MinesweeperGUI
{
 public static void main(String[] args)
 {
  //+++++++++++++++++++++++++++++++++++++++++++++++++++
  //The creation of a standard JFrame, which has a JComponent:
  //mineFieldComponent, which is an instance of the Minefield class,
  //and is added and packed to the JFrame which is made visible.
  //+++++++++++++++++++++++++++++++++++++++++++++++++++
  JFrame frame = new JFrame("Minesweeper");
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  JComponent minefieldComponent = new Minefield();
  frame.add(minefieldComponent);
  frame.pack();
  frame.setVisible(true);
 }
}
class Minefield extends JComponent
{
 //+++++++++++++++++
 //The only two instance datas are:
 //the difficulty, and mineField, which is an instance of the Minesweeper class.
 //It should be known that the default difficulty is beginner mode.
 //+++++++++++++++++
 int difficulty = 1;
 Minesweeper mineField  = new Minesweeper(difficulty);
 public Minefield()
 {
  //+++++++++++++++++++++++++++++++++
  //The Constructor calls the addMouseListener method from the MouseListener interface
  //by adding a new object from the MouseAdapter class in the formal parameters.
  //By overriding the interface's mousePressed method, when the mouse listener receives
  //a mouse event that matches the mousePressed event, the method is called.
  //Inside this method is the .isMetaDown() method which return a boolean on wither it's
  //a right(false) or left(true) click.
  //The left click signifies a flag being placed because it will call processClick with
  //negative row, and col integers. Otherwise processClick is called with positive integers.
  //+++++++++++++++++++++++++++++++++
  addMouseListener(new MouseAdapter()
   {
    public void mousePressed(MouseEvent event)
    {
     if (event.isMetaDown()) processClick((event.getY() + 20) * - 1, (event.getX() + 20) * - 1);
     else processClick(event.getY(), event.getX());
    }
   }
  );
 }
 public void processClick(int row, int col)
 {
  //+++++++++++++++++++++++++++++++++++++++
  //Because the minesweeper field in the gui is shifted down 100 pixels,
  //it's necessary to set the proper row and col depending on wither it was a right
  //or left click. This is further explained in the minesweeper class.
  //For now, know that the shifts ensure proper coordinates calculation and
  //that the other if and else-if statements are the proportions of the rectangles
  //surrounding the words: "Beginner," "Intermediate," and "Expert," and will 
  //start a new game depending on which rectangle the mouse event occurred in.
  //+++++++++++++++++++++++++++++++++++++++
  if (row < 0) mineField.expose(row/20 + 5, col/20);
  else mineField.expose(row/20 - 5, col/20);
  if (row >= 48 && row <= 61 && col >= 3 && col <= 63) mineField = new Minesweeper(difficulty = 1);
  else if (row >= 63 && row <= 76 && col >= 3 && col <= 78) mineField = new Minesweeper(difficulty = 2);
  else if (row >= 78 && row <= 91 && col >= 3 && col <= 48) mineField = new Minesweeper(difficulty = 3);
  repaint();
 }
 public Dimension getBoardSize()
 {
  //++++++++++++++++++++++++++++
  //Pretty straight forward, the method
  //returns the dimensions for
  //the corresponding difficulty;
  //1 is beginner. 2 is intermediate
  //and 3 is expert, defualt is beginner
  //++++++++++++++++++++++++++++
  if (difficulty == 1) return new Dimension(160, 160);
  else if (difficulty == 2) return new Dimension(320, 320);
  else if (difficulty == 3) return new Dimension(600, 320);
  else return new Dimension(160, 160);
 }
 public Dimension getPreferredSize()
 {
  //++++++++++++++++++++++++++++++++
  //This is a method override the default 
  //JComponent size when it first appears
  //++++++++++++++++++++++++++++++++
  return new Dimension(161, 261);
 }
 protected void paintComponent(Graphics graphic)
 {
  //++++++++++++++++++++++++++++++++
  //Pretty self explanitory.
  //The array field is made to promote efficiency
  //instead of constantly calling the toArray() method;
  //the same is for check, but with the checkBoard() method.
  //The use of a switch statement is simply because 11
  //if and else if statements were ugly
  //(row + 5) creates a shift down of 100 pixels in the gui
  //The boxes are 20 by 20 pixels with the colored
  //number shifted to appear to be center inside the box.
  //++++++++++++++++++++++++++++++++
  int[][] field = mineField.toArray();
  int width = (int)getBoardSize().getWidth(), height = (int)getBoardSize().getHeight(), check = mineField.checkBoard();
  graphic.setColor(new Color(238, 238, 238));
  graphic.fillRect(0, 0, 601, 601);
  graphic.setColor(new Color(245, 245, 220));
  graphic.fillRect(0, 0, 160, 100);
  graphic.fillRect(0, 100, width, height);
  graphic.setColor(Color.black);
  graphic.drawRect(0, 0, 160, 100);
  graphic.drawRect(0, 100, width, height);
  if (check == 1) graphic.drawString("Congratualtions! You Won!", 5, 15);
  else if (check == 0) graphic.drawString("Sorry. You Lost.", 5, 15);
  graphic.drawString("Choose An Option Below To", 5, 30);
  graphic.drawString("Restart, Or Begin A Game", 5, 42);
  graphic.drawString("Beginner", 5, 60);
  graphic.drawRect(3, 48, 60, 13);
  graphic.drawString("Intermediate", 5, 75);
  graphic.drawRect(3, 63, 75, 13);
  graphic.drawString("Expert", 5, 90);
  graphic.drawRect(3, 78, 45, 13);
  for (int row = 0; row < field.length; row++)
   for (int col = 0; col < field[row].length; col++)
   {
    graphic.setColor(Color.black);
    graphic.drawLine(col * 20, (row + 5) * 20, col * 20, (row + 5) * 20 + 20);
    graphic.drawLine(col * 20, (row + 5) * 20, col * 20 + 20, (row + 5) * 20);
    String temp = "";
    switch(field[row][col])
    {
     case 0: graphic.setColor(Color.black); temp = "" + field[row][col]; break;
     case 1: graphic.setColor(Color.blue); temp = "" + field[row][col]; break;
     case 2: graphic.setColor(new Color(50, 205, 50)); temp = "" + field[row][col]; break;
     case 3: graphic.setColor(Color.red); temp = "" + field[row][col]; break;
     case 4: graphic.setColor(new Color(159, 0, 197)); temp = "" + field[row][col]; break; 
     case 5: graphic.setColor(new Color(128, 0, 0)); temp = "" + field[row][col]; break;
     case 6: graphic.setColor(new Color(64, 224, 208)); temp = "" + field[row][col]; break;
     case 7: graphic.setColor(Color.black); temp = "" + field[row][col]; break;
     case 8: graphic.setColor(new Color(84, 84, 84)); temp = "" + field[row][col]; break;
     case 10: graphic.setColor(new Color(245, 245, 220)); temp = " "; break;
     case 11: graphic.setColor(Color.red); temp = "" + (char) 1635; break;
    }
    graphic.drawString(temp, col * 20 + 5, (row + 6) * 20 - 5);
   }
 }
}