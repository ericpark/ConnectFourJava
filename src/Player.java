/*
 * File: Player.java
 * Author: Sameer Farooq (sfarooq@bu.edu), Stephen Bahr (sbahr@bu.edu),
 * and Eric Park (ericpark@bu.edu)
 * Date: 4/29/13
 * 
 * Purpose: This class serves as a Player object that will
 * use a game tree to simulate possible future movements,
 * evaluate that board, and return the projected path in
 * order to get to a future win.
 * 
 * Alpha-Beta Pruning method provided by Wayne Snyder.
 * http://www.cs.bu.edu/fac/snyder/cs112/Lectures/CS112.Lecture.22.pdf
 * Slide #18, which was modfied for ints.
 * 
 * Platform: This was developed on Mac OS X 10.8.3 in Eclipse IDE
 * 
 * Associated Files: Connect4.java
 */

public class Player {

	/*
	 * move returns a column in which to make the move
	 * for the computer based on the enumerate value that
	 * is returned.
	 */

	private static int rows = Connect4.rows;
	private static int cols = Connect4.cols;

	public int move(int[][] board, int player){

		int colMove = -1;                        //represents the column move
		int value = Integer.MAX_VALUE * -1;

		for(int column = 0; column < cols; column++){

			if (board[0][column] == 0){           //if there can be a piece inserted

				//find the playable row
				int row = findPlayableRow(board, column);

				board[row][column] = 10;
				int e = enumerate(board, 1, Integer.MAX_VALUE * -1, Integer.MAX_VALUE);
				board[row][column] = 0;

				if (e >= value){
					colMove = column;
					value = e;
				}

			}
		}

		return colMove;

	}

	/*
	 * enumerate goes over all the possible plays on the board and returns 
	 * Code from Slide 18: 
	 * http://www.cs.bu.edu/fac/snyder/cs112/Lectures/CS112.Lecture.22.pdf
	 */
	public int enumerate(int[][] board, int move, int alpha, int beta){

		int pValue = Integer.MAX_VALUE * -1;            //player's value
		int cValue = Integer.MAX_VALUE;            //comp's value

		//see who's turn it is
		int player;
		if (move % 2 == 0){
			player = 10;
		}

		else
			player = 1;

		//evaluate the board value at this given instance
		int bValue = eval(board);

		//if at the depth we want, or either player wins, or no more moves
		if (isFull(board) || bValue == Integer.MAX_VALUE || bValue == Integer.MAX_VALUE * -1 || move == 7){

			if (bValue == Integer.MAX_VALUE)
				return bValue - move;          //return the board value as you cannot go deeper
			else if (bValue == Integer.MAX_VALUE * -1)
				return bValue + move;
			else
				return bValue;

		}

		else if (player == 10){         //max's turn

			for(int column = 0; column < cols; column++){         //each column in the board

				//if top is empty, column not full, means playable somewhere
				if(board[0][column] == 0){

					//find where it's playable
					int row = findPlayableRow(board, column);

					alpha = Math.max(alpha, pValue);    //update alpha with max

					if (beta < alpha){
						break;
					}

					board[row][column] = 10;             //WHOEVER PLAYS SECOND

					pValue = Math.max(pValue, enumerate(board, move + 1, alpha, beta));    //allows the bubbling up of leaf

					board[row][column] = 0;             //undo the move



				}
			}

			return pValue;

		}

		else{                     //min's turn

			for(int column = 0; column < cols; column++){         //each column in the board

				//if top is empty, column not full, means playable somewhere
				if(board[0][column] == 0){

					//find where it's playable
					int row = findPlayableRow(board, column);

					beta = Math.min(beta, cValue);    //update beta with min

					if (beta < alpha){
						break;
					}

					board[row][column] = 1;             //WHOEVER PLAYS FIRST

					cValue = Math.min(cValue, enumerate(board, move + 1, alpha, beta));    //allows the bubbling up of leaf

					board[row][column] = 0;             //undo the move


				}
			}

			return cValue;
		} 
	}

	/*
	 * findPlayableRow uses the board and column being searched to find
	 * the position of the row that is empty. This traverses the row
	 * from the bottom up.
	 */
	private int findPlayableRow(int[][] board, int column){

		//reverse traverse up the row in that column
		for(int row = rows - 1; row > -1; row--){

			//if the board at that row/column is empty
			if(board[row][column] == 0){
				return row;
			}
		}

		//should never get here
		return -1;
	}

	/*
	 * isFull returns true if all the columns on the top row are filled
	 * and returns true if they are. If there is an empty space, the board
	 * is not full, returns false.
	 */
	private boolean isFull(int[][] board){

		for(int i = 0; i < rows; i++){

			//if any top column on the board is empty, it's not full
			if(board[0][i] == 0){

				return false;

			}
		}

		return true;

	}

	/*
	 * endGame takes the board (2D int array) and looks at strings of 4 
	 * at a time horizontally, vertically, diagonally-down, and diagonally-up
	 * If there are four of the same piece (1 or 10) in a row, then it returns
	 * the locations of the spots. If not, it returns array of -1s
	 */
	public int[][] endGame(int[][] board) {
		
		int[][] winLocation = new int[4][2];

		int loopCount = 0;
		int redCount = 0;
		int blueCount = 0;

		// go through board horizontally
		for (int row = 0; row < rows; row++) { //board.length = number of rows
			for (int colStart = 0; colStart < cols - 3; colStart++) { //Start of the column comparison
				for (int colPos = colStart; colPos < colStart + 4; colPos++) { //End of the column comparison

					if (board[row][colPos] == 1)
						redCount++;
					else if (board[row][colPos] == 10)
						blueCount++;
					
					winLocation[loopCount][0] = row;
					winLocation[loopCount][1] = colPos;
					loopCount++;
				}

				if (redCount == 4 || blueCount == 4)
					return winLocation;

				loopCount = 0;
				redCount = 0;
				blueCount = 0;
			}
		}

		// go through board vertically
		for (int col = 0; col < cols; col++) { //board[0].length = number of columns
			for (int rowStart = 0; rowStart < rows - 3; rowStart++) {
				for (int rowPos = rowStart; rowPos < rowStart + 4; rowPos++) {
					if (board[rowPos][col] == 1)
						redCount++;
					else if (board[rowPos][col] == 10)
						blueCount++;

					winLocation[loopCount][0] = rowPos;
					winLocation[loopCount][1] = col;
					loopCount++;
				}

				if (redCount == 4 || blueCount == 4)
					return winLocation;


				loopCount = 0;
				redCount = 0;
				blueCount = 0;
			}
		}

		// go through board diagonally-down (nw-se)
		// do diagonals from column 0 (rows 0-2 in a typical board)
		for (int row = 0; row < rows - 3; row++) { // row to start in
			for (int colStart = 0; colStart < (rows - 3) - row; colStart++) { // column to start
				for (int colPos = colStart; colPos < colStart + 4; colPos++) {
					if (board[colPos + row][colPos] == 1)
						redCount++;
					else if (board[colPos + row][colPos] == 10)
						blueCount++;

					winLocation[loopCount][0] = colPos + row;
					winLocation[loopCount][1] = colPos;
					loopCount++;
					
				}

				if (redCount == 4 || blueCount == 4)
					return winLocation;


				loopCount = 0;
				redCount = 0;
				blueCount = 0;
			}
		}

		// do diagonals from row 0 (columns 1-3)
		for (int colPos = 1; colPos < cols - 3; colPos++) { // column to start in
			for (int colEnd = 0; colEnd < (cols - 3) - colPos; colEnd++) { // place to start
				for (int row = colEnd; row < colEnd + 4; row++) {
					if (board[row][row + colPos] == 1)
						redCount++;
					else if (board[row][row + colPos] == 10)
						blueCount++;

					winLocation[loopCount][0] = row;
					winLocation[loopCount][1] = row + colPos;
					loopCount++;
				}

				if (redCount == 4 || blueCount == 4)
					return winLocation;


				loopCount = 0;
				redCount = 0;
				blueCount = 0;
			}
		}

		// go through board diagonally-up (sw-ne)
		// do diagonals from column 0 (rows 3-6)
		for (int row = 3; row < rows; row++) { // row to start in
			for (int colStart = 0; colStart < row - 2; colStart++) { // place to start
				for (int colPosition = colStart; colPosition < colStart + 4; colPosition++) {
					if (board[row - colPosition][colPosition] == 1)
						redCount++;
					else if (board[row - colPosition][colPosition] == 10)
						blueCount++;

					winLocation[loopCount][0] = row - colPosition;
					winLocation[loopCount][1] = colPosition;
					loopCount++;
				}

				if (redCount == 4 || blueCount == 4)
					return winLocation;


				loopCount = 0;
				redCount = 0;
				blueCount = 0;
			}
		}

		// do diagonals from row 6 (columns 1-3)
		for (int colStart = 1; colStart < cols - 3; colStart++) { // column to start in
			for (int j = rows - 1; j > (rows - 5) + colStart; j--) { // place to start
				for (int k = j; k > j - 4; k--) {
					if (board[k][(rows - 1) + colStart - k] == 1)
						redCount++;
					else if (board[k][(rows - 1) + colStart - k] == 10)
						blueCount++;

					winLocation[loopCount][0] = k;
					winLocation[loopCount][1] = (rows - 1) + colStart - k;
					loopCount++;
				}

				if (redCount == 4 || blueCount == 4)
					return winLocation;

				loopCount = 0;
				redCount = 0;
				blueCount = 0;
			}
		}
		
		// if winLocation hasn't been returned yet, then there was no win
		// fill winLocation entirely with -1s
		for (int i = 0; i < winLocation.length; i++) {
			for (int j = 0; j < winLocation[0].length; j++) {
				winLocation[i][j] = -1;
			}
		}
		
		return winLocation;
	}

	/*
	 * eval evaluates the board and returns the 'score' for that board.
	 * 1 = red, 10 = blue (our player), 0 = empty
	 */
	private int eval(int[][] board) {
		int myCurrentLines = currentLines(board, 10);

		if (myCurrentLines == Integer.MAX_VALUE)
			return myCurrentLines;

		int opponentCurrentLines = currentLines(board, 1);

		if (opponentCurrentLines == Integer.MAX_VALUE)
			return opponentCurrentLines * -1;

		int evalScore = myCurrentLines - opponentCurrentLines;

		return evalScore;

	}

	/*
	 * currentLines takes the board (2D int array) and the player (10 or 1). 
	 * It looks at strings of 4 at a time horizontally, vertically, 
	 * diagonally-down, and diagonally-up. It counts the number of current 
	 * lines of 1, 2, 3, and 4 based on empty spaces
	 */
	private int currentLines(int[][] board, int player) {

		int pieceCount = 0;
		int emptyCount = 0;
		int lineOf1Count = 0;
		int lineOf2Count = 0;
		int lineOf3Count = 0;
		int lineOf4Count = 0;

		// go through board horizontally
		for (int row = 0; row < rows; row++) { //board.length = number of rows
			for (int colStart = 0; colStart < cols - 3; colStart++) { //Start of the column comparison
				for (int colPos = colStart; colPos < colStart + 4; colPos++) { //End of the column comparison
					if (board[row][colPos] == 0)
						emptyCount++;
					else if (board[row][colPos] == player)
						pieceCount++;
				}

				if (pieceCount > 0 && (pieceCount + emptyCount == 4)) {
					if (pieceCount == 4)
						lineOf4Count++;
					else if (pieceCount == 3)
						lineOf3Count++;
					else if (pieceCount == 2)
						lineOf2Count++;
					else                // pieceCount == 1 (only option left)
						lineOf1Count++;
				}

				if (lineOf4Count > 0)
					return Integer.MAX_VALUE; // 2^31 - 1

				pieceCount = 0;
				emptyCount = 0;
			}
		}

		// go through board vertically
		for (int col = 0; col < cols; col++) { //board[0].length = number of columns
			for (int rowStart = 0; rowStart < rows - 3; rowStart++) {
				for (int rowPos = rowStart; rowPos < rowStart + 4; rowPos++) {
					if (board[rowPos][col] == 0)
						emptyCount++;
					else if (board[rowPos][col] == player)
						pieceCount++;
				}

				if (pieceCount > 0 && (pieceCount + emptyCount == 4)) {
					if (pieceCount == 4)
						lineOf4Count++;
					else if (pieceCount == 3)
						lineOf3Count++;
					else if (pieceCount == 2)
						lineOf2Count++;
					else // pieceCount == 1 (only option left)
						lineOf1Count++;
				}

				if (lineOf4Count > 0)
					return Integer.MAX_VALUE; // 2^31 - 1

				pieceCount = 0;
				emptyCount = 0;
			}
		}

		// go through board diagonally-down (nw-se)
		// do diagonals from column 0 (rows 0-2 in a typical board)
		for (int row = 0; row < rows - 3; row++) { // row to start in
			for (int colStart = 0; colStart < (rows - 3) - row; colStart++) { // column to start
				for (int colPos = colStart; colPos < colStart + 4; colPos++) {
					if (board[colPos + row][colPos] == 0)
						emptyCount++;
					else if (board[colPos + row][colPos] == player)
						pieceCount++;
				}

				if (pieceCount > 0 && (pieceCount + emptyCount == 4)) {
					if (pieceCount == 4)
						lineOf4Count++;
					else if (pieceCount == 3)
						lineOf3Count++;
					else if (pieceCount == 2)
						lineOf2Count++;
					else // pieceCount == 1 (only option left)
						lineOf1Count++;
				}

				if (lineOf4Count > 0)
					return Integer.MAX_VALUE; // 2^31 - 1

				pieceCount = 0;
				emptyCount = 0;
			}
		}

		// do diagonals from row 0 (columns 1-3)
		for (int colPos = 1; colPos < cols - 3; colPos++) { // column to start in
			for (int colEnd = 0; colEnd < (cols - 3) - colPos; colEnd++) { // place to start
				for (int row = colEnd; row < colEnd + 4; row++) {
					if (board[row][row + colPos] == 0)
						emptyCount++;
					else if (board[row][row + colPos] == player)
						pieceCount++;
				}

				if (pieceCount > 0 && (pieceCount + emptyCount == 4)) {
					if (pieceCount == 4)
						lineOf4Count++;
					else if (pieceCount == 3)
						lineOf3Count++;
					else if (pieceCount == 2)
						lineOf2Count++;
					else // pieceCount == 1 (only option left)
						lineOf1Count++;
				}

				if (lineOf4Count > 0)
					return Integer.MAX_VALUE; // 2^31 - 1

				pieceCount = 0;
				emptyCount = 0;
			}
		}

		// go through board diagonally-up (sw-ne)
		// do diagonals from column 0 (rows 3-6)
		for (int row = 3; row < rows; row++) { // row to start in
			for (int colStart = 0; colStart < row - 2; colStart++) { // place to start
				for (int colPosition = colStart; colPosition < colStart + 4; colPosition++) {
					if (board[row - colPosition][colPosition] == 0)
						emptyCount++;
					else if (board[row - colPosition][colPosition] == player)
						pieceCount++;
				}

				if (pieceCount > 0 && (pieceCount + emptyCount == 4)) {
					if (pieceCount == 4)
						lineOf4Count++;
					else if (pieceCount == 3)
						lineOf3Count++;
					else if (pieceCount == 2)
						lineOf2Count++;
					else // pieceCount == 1 (only option left)
						lineOf1Count++;
				}

				if (lineOf4Count > 0)
					return Integer.MAX_VALUE; // 2^31 - 1

				pieceCount = 0;
				emptyCount = 0;
			}
		}

		// do diagonals from row 6 (columns 1-3)
		for (int colStart = 1; colStart < cols - 3; colStart++) { // column to start in
			for (int j = rows - 1; j > (rows - 5) + colStart; j--) { // place to start
				for (int k = j; k > j - 4; k--) {
					if (board[k][(rows - 1) + colStart - k] == 0) // [7 + i - k]
						emptyCount++;
					else if (board[k][(rows - 1) + colStart - k] == player) // [7 + i - k]
						pieceCount++;
				}

				if (pieceCount > 0 && (pieceCount + emptyCount == 4)) {
					if (pieceCount == 4)
						lineOf4Count++;
					else if (pieceCount == 3)
						lineOf3Count++;
					else if (pieceCount == 2)
						lineOf2Count++;
					else // pieceCount == 1 (only option left)
						lineOf1Count++;
				}

				if (lineOf4Count > 0)
					return Integer.MAX_VALUE; // 2^31 - 1

				pieceCount = 0;
				emptyCount = 0;
			}
		}


		// lineOf4Count isn't included because if lineOf4Count > 0, Integer.MAX_VALUE should have returned
		int currentLinesScore = 1000000*lineOf3Count + 1000*lineOf2Count + 1*lineOf1Count;

		return currentLinesScore;
	}
}