//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//Mark Schlax
//Minesweeper
//6/4/2013
//The classic game re-made in a simple yet efficient class called Minesweeper. Don't worry, I'll try to explain what's happening
//The source code's length without comments was 120 lines long; not that it matters, but I like it efficient and short.
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
public class Minesweeper
{
 //+++++++++++++++++++++++++++++++
 //The first three arrays are the dimensions for beginner, intermediate, and expert; they're made final to prevent any possible change.
 //The array dimensions has the following indexes: [0] is the number of rows, [1] is the number of cols, and [2] number of bombs
 //The three boolean arrays: bombs, exposed, and flagged hold special information about wither and index is a bomb, or if it's flagged or exposed,
 //although dimensions is originally {0, 0, 0}, the three arrays are set to the dimensions to express the idea that later they will be set again to new dimensions.
 //Fun Fact: originally I had the three arrays part of a three-dimensional array, but they seemed confusing to people when I tried to explain the three-dimensional array.
 //The sol array will hold the "solution" of the game, in reality it jut holds the numbers that appear on screen
 //Boolean firstMove wasn't intended to be in the final version, but without it, many bugs arose and it's easier and more efficient to keep; against my wishes however...
 //+++++++++++++++++++++++++++++++
 private final int[] beginner = {8, 8, 10}, intermediate = {16, 16, 40}, expert = {16, 30, 99};
 private int[] dimensions = {0, 0, 0};
 private boolean[][] bombs = new boolean[dimensions[0]][dimensions[1]], exposed = new boolean[dimensions[0]][dimensions[1]], flagged = new boolean[dimensions[0]][dimensions[1]];
 private int[][] sol = new int[dimensions[0]][dimensions[1]];
 private boolean firstMove = true;
 public Minesweeper(int difficulty)
 {
  //+++++++++++++++++++++++++++++++
  //The Constructer sets the dimensions corresponding to the difficulty entered,
  //then calls the resetMinefield() method
  //+++++++++++++++++++++++++++++++
  if (difficulty == 1) dimensions = beginner;
  else if (difficulty == 2) dimensions = intermediate;
  else if (difficulty == 3) dimensions = expert;
  resetMinefield();
 }
 private void resetMinefield()
 {
  //+++++++++++++++++++++++++
  //With the dimensions reset to the corresponding difficulty,
  //the arrays are reset to have the proper dimensions,
  //and the stupid, lucky to be here, firstMove is reset to true...
  //+++++++++++++++++++++++++
  sol = new int[dimensions[0]][dimensions[1]];
  bombs = new boolean[dimensions[0]][dimensions[1]];
  exposed = new boolean[dimensions[0]][dimensions[1]];
  flagged = new boolean[dimensions[0]][dimensions[1]];
  firstMove = true;
 }
 private void placeMines(int userRow, int userCol)
 {
  //++++++++++++++++++++++++++++++++++++++++++++++
  //That no-good firstMove variable is set to false...ha take that...
  //Before explaining what's happening in the for-loop, one must notice that there's no afterthought section in the for-loop, it's actually at the bottom of the loop's body
  //In the initialization part of the for-loop, the variables bombCount is set to 0, and row and col are set to random numbers between the value of 0 and number of rows/cols.
  //The condition is simply, keep going when there are less bombs than dimensions[2], which is the number of bombs that should be on the field.
  //The body is slightly more complicated.
  //The first if statement insures that the row and col combination don't cause the user to lose on the first click.
  //If the combination doesn't match, the boolean array at the random indexes row, col is set to the opposite of itself.
  //Then an if statement is run incrementing bombCount if bombs at random indexes row, col is true, if not then bombCount is decremented;
  //this is because there's a probability that the random indexes chooses the same index multiple times and must adjust bombCount to adapt for the change.
  //As stated before the afterthought part of the for-loop is at the bottom of the body, where row and col are set to new random values.
  //++++++++++++++++++++++++++++++++++++++++++++++
  firstMove = false;
  for (int bombCount = 0, row = (int)(Math.random() * dimensions[0]), col = (int)(Math.random() * dimensions[1]); bombCount < dimensions[2];)
  {
   if (row != userRow && col != userCol)
   {
    bombs[row][col] = !bombs[row][col];
    if (bombs[row][col]) bombCount++;
    else bombCount--;
   }
   row = (int)(Math.random() * dimensions[0]);
   col = (int)(Math.random() * dimensions[1]);
  }
  solveMinefield();
 }
 private void solveMinefield()
 {
  //++++++++++++++++++++++++++
  //Okay! Don't freak out! It's a-lot simpler than it seems.
  //The two first for-loops iterate their way through the field.
  //When they reach a bomb, every space, in a 3 by 3 fashion with the bomb being the center, around the bomb is incremented one, except if it's a bomb, then it's set equal to 9.
  //The first two if statements in the 4th for-loop insure that row2 and col2 are never negative, because that would cause an exception.
  //++++++++++++++++++++++++++
  for (int row = 0; row < dimensions[0]; row++)
   for (int col = 0; col < dimensions[1]; col++)
    if (bombs[row][col])
     for (int row2 = row - 1; row2 <= (row + 1) && row2 < dimensions[0]; row2++)
      for (int col2 = col - 1; col2 <= (col + 1) && col2 < dimensions[1]; col2++)
      {
       if (row2 < 0) row2++;
       if (col2 < 0) col2++;
       if (!bombs[row2][col2]) sol[row2][col2]++;
       else sol[row2][col2] = 9;
      }
 }
 public void expose(int row, int col)
 {
  //+++++++++++++++++++++++++++++++++
  //To put it in english, the first if statement decodes the incoming row and col into whether it's a right or left click due to the values being negative;
  //it also prevents any ArrayOutOfBound exceptions from occurring.
  //If the values are positive, and the stupid firstMove variable is true, then the mines are placed.
  //Still positive if the index at row and col is not already exposed or flagged, the further most inside body is executed.
  //Still positive and not flagged or exposed, if the index in sol is 0, the recursive method exposeZeros is called and then exposeAroundZeros() is called,
  //otherwise, if the index in sol isn't zero, that index is made true in the exposed array.
  //If the index is negative, the flag at the proper positive indexes is toggled between true and false.
  //+++++++++++++++++++++++++++++++++
  if (row >= 0 && row < dimensions[0] && col >= 0 && col < dimensions[1])
  {
   if (firstMove) placeMines(row, col);
   if (!flagged[row][col] && !exposed[row][col])
    if (sol[row][col] == 0)
    {
     exposeZeros(row, col);
     exposeAroundZeros();
    }
    else exposed[row][col] = true;
  }
  else if (row <= -1 && row >= dimensions[0] * - 1 && col <= -1 && col >= dimensions[1] * -1) flagged[row * -1 - 1][col * -1 - 1] = !flagged[row * -1 - 1][col * -1 - 1];
 }
 private void exposeZeros(int row, int col)
 {
  //+++++++++++++++++++++++++++++++++++++++
  //Welcome to the recursive method used to uncover adjacent 0s.
  //The only solid base-case is the field's dimensions; otherwise the only other thing stopping the method is if there are no more adjacent 0s.
  //It's quite simple, first the index is exposed, then if any adjacent indexes are 0s in the sol array, the method calls itself and repeats the process.
  //+++++++++++++++++++++++++++++++++++++++
  exposed[row][col] = true;
  if (col + 1 < dimensions[1] && sol[row][col + 1] == 0 && !bombs[row][col + 1] && !exposed[row][col + 1]) exposeZeros(row,col + 1);
  if (row + 1 < dimensions[0] && sol[row + 1][col] == 0 && !bombs[row + 1][col] && !exposed[row + 1][col]) exposeZeros(row + 1,col);
  if (col - 1 >=  0 && sol[row][col - 1] == 0 && !bombs[row][col - 1] && !exposed[row][col - 1]) exposeZeros(row,col - 1);
  if (row - 1 >= 0 && sol[row - 1][col] == 0 && !bombs[row - 1][col] && !exposed[row - 1][col]) exposeZeros(row - 1, col);
 }
 private void exposeAroundZeros()
 {
  //+++++++++++++++++++++++++++++
  //Called after the recursive method, this method follows a similar pattern to the solveMineField() method.
  //But rather than having the inner for-loops run when there's a bomb, they're executed when an index is 0 and exposed.
  //And rather than incrementing the sol value at index, the index is exposed.
  //+++++++++++++++++++++++++++++
  for (int row = 0; row < dimensions[0]; row++)
   for (int col = 0; col < dimensions[1]; col++)
    if (exposed[row][col] && sol[row][col] == 0)
     for (int row2 = row - 1; row2 <= (row + 1) && row2 < dimensions[0]; row2++)
      for (int col2 = col - 1; col2 <= (col + 1) && col2 < dimensions[1]; col2++)
      {
       if (row2 < 0) row2++;
       if (col2 < 0) col2++;
       if (!exposed[row2][col2])exposed[row2][col2] = true;
      }
 }
 public int checkBoard()
 {
  //++++++++++++++++++++
  //The local variable count is made to count the number of exposed indexes in the field
  //A standard for loop iterates through the field and the if statements’' conditions are evaluated;
  //if an index is exposed and has a bomb, then the method returns a 0, indicating a loss,
  //otherwise if the index is exposed, then count is incremented; it should be noted that flagged doesn't increment count.
  //If the number of exposed is equal to the number of indexes - the number of bombs, i.e. the number of possible exposed spaces, then the method returns a 1; indicating a victory.
  //Otherwise, the method returns a 2; indicating the game is in progress.
  //++++++++++++++++++++
  int count = 0;
  for (int row = 0; row < dimensions[0]; row++)
   for (int col = 0; col < dimensions[1]; col++)
    if (exposed[row][col] && bombs[row][col]) return 0;
    else if (exposed[row][col]) count++;
  if (count == dimensions[0] * dimensions[1] - dimensions[2]) return 1;
  return 2;
 }
 public int[][] toArray()
 {
  //+++++++++++++++++++++
  //By combining all four arrays together, the toArray() returns an in[][] array.
  //First, a two dimensional array is created to later be returned, also an integer variable is set to the value of checkBoard, to promote efficiency.
  //A standard for loop iterates through the indexes and then if statements will determine what the output will consist of.
  //If the game is in progress, then the array has it's index set to the value of sol if the index is exposed; if not, then it's value is set to 10, if it's a bomb, then the value is set to 9, if it's flagged, the value is set to 11.
  //If the game is over, then the array has its index set to the value of sol, and all indexes of bombs are set to 11(which will cause it to appear flagged).
  //+++++++++++++++++++++
  int[][] arry = new int[dimensions[0]][dimensions[1]];
  int check  = checkBoard();
  for (int row = 0; row < dimensions[0]; row++)
   for (int col = 0; col < dimensions[1]; col++)
    if (check == 2)
    {
     if (exposed[row][col])
     {
      if (bombs[row][col]) arry[row][col] = 9;
      else arry[row][col] = sol[row][col];
     }
     else if (flagged[row][col]) arry[row][col] = 11;
     else arry[row][col] = 10;
    }
    else
     if (bombs[row][col]) arry[row][col] = 11;
     else arry[row][col] = sol[row][col];
  return arry;
 }
}