/*
 * File: Connect4.java
 * Author: Sameer Farooq (sfarooq@bu.edu), Stephen Bahr (sbahr@bu.edu),
 * and Eric Park (ericpark@bu.edu)
 * Date: 4/29/13
 * 
 * Purpose: This class serves as the GUI for the Connect Four
 * game and listens for click events by the human players, then
 * calls the computer player to make its move and continues
 * until a winner is then found.
 * 
 * Platform: This was developed on Mac OS X 10.8.3 in Eclipse IDE
 * 
 * Associated Files: Player.java, JBox.java, JCanvas.java, JEventQueue.java
 */

import java.awt.Color;
import java.awt.Font;
import java.util.EventObject;
import java.awt.*;
import javax.swing.*;

public class Connect4
{
	private static JFrame frame = new JFrame("Connect 4");
	private static JPanel format = new JPanel();
	private static int[][] board;
	private static JPanel totalBoard;

	// Standard board size is 6 rows by 7 columns but AI will work with any size > 4
	public static int rows = 6;
	public static int cols = 7;

	private static JButton[][] input;

	/*
	 * makeInput makes the board of buttons all yellow
	 * and not clicked.
	 */

	private JPanel makeInput()
	{
		input = new JButton[rows][cols];
		GridLayout grid = new GridLayout(rows, cols);
		totalBoard = new JPanel(grid);
		for(int r = 0; r < rows; r++)
		{
			for(int c = 0; c < cols; c++)
			{
				input[r][c] = new JButton("");
				JBox.setSize(input[r][c],80,80);
				input[r][c].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
				input[r][c].setBackground(Color.YELLOW);
				input[r][c].setOpaque(true);
				totalBoard.add(input[r][c]);
			}
		}
		return totalBoard;
	}

	/*
	 * setBoard initializes the board to an empty board.
	 */
	private void setBoard()
	{
		board = new int[rows][cols];
	}

	/*
	 * update updates the board to show the integer
	 * in each index of each array to show either
	 * player 1 (red), player 2 (blue), or empty.
	 */
	private void update(JBox bottom)
	{
		format.remove(totalBoard);
		format.remove(bottom);
		format.add(totalBoard);
		format.add(bottom);
		frame.add(format);
		frame.setVisible(true);
	}

	/*
	 * empty empties the board to reset it.
	 */
	private void reset(JBox top, JBox bottom)
	{
		setBoard();
		format.remove(top);
		format.remove(totalBoard);
		format.remove(bottom);
		format.add(top);
		format.add(makeInput());
		format.add(bottom);
		frame.add(format);
		frame.setVisible(true);
	}


	/*
	 * TEST METHOD
	 * setupBoard takes the board, column, row, and player and
	 * updates the board to insert that 'piece' in the
	 * specified index.
	 */
	//	private void setupBoard(JBox top, JBox bottom, int[][] board, int row, int col, int player)
	//	{
	//		if(player == 1)
	//		{
	//			input[row][col].setBackground(Color.RED);
	//			board[row][col] = 1;
	//		}
	//		else if(player == 10)
	//		{
	//			input[row][col].setBackground(Color.BLUE);
	//			board[row][col] = 10;
	//		}
	//		update(top, bottom);
	//	}

	/*
	 * gravity simulates the fall of pieces. Loops through until
	 * there are no empty spaces below it and just makes the piece
	 * fall 1 row down at a time.
	 */
	private void gravity(int col, JBox b, Color c)
	{
		int row = 0;
		input[row][col].setBackground(c);
		//update(b);
		while(row < rows - 1 && board[row + 1][col] == 0)
		{
			try {                                         // this will pause execution for 90 milliseconds (0.1 sec)
				Thread.sleep(90);                         
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			input[row][col].setBackground(Color.YELLOW);
			row++;
			input[row][col].setBackground(c);
			update(b);
		}
		if(c.equals(Color.RED))
		{
			board[row][col] = 1;
		}
		else
		{
			board[row][col] = 10;
		}
		//frame.setVisible(false);
		//frame.setVisible(true);
		SwingUtilities.updateComponentTreeUI(frame);

	}

	/*
	 * main method that contains the GUI and main
	 * actions needed to start and end the game.
	 */
	public static void main(String[] args)
	{
		Connect4 con = new Connect4();
		Player p = new Player();
		con.setBoard();
		con.makeInput();
		int[][] winLocation;

		frame.setSize(Math.max(rows * 100, 500) , Math.max(cols * 100, 500));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		//JPanel totalBoard = con.makeInput();

		//create label
		JLabel label1 = new JLabel("Connect 4");
		label1.setFont(new Font("Times", Font.BOLD, 28));
		JLabel label2 = new JLabel("Playing...");
		label2.setFont(new Font("Times", Font.PLAIN, 20)); //changed

		//create buttons
		JButton button1 = new JButton("Quit"); 
		JButton button2 = new JButton("Reset"); 

		JBox top = JBox.vbox(JBox.vspace(20),
				JBox.hbox(JBox.hspace(340), label1, JBox.hspace(340)));

		// This is what it was previously.
		//  JBox bottom = 
		//           JBox.hbox(JBox.hspace(150), button1, JBox.hspace(150), button2, JBox.hspace(150), label2, JBox.hspace(150));


		JBox bottom = JBox.vbox(
				JBox.hbox(JBox.hspace(150), button1, JBox.hspace(150), button2, JBox.hspace(150), label2, JBox.hspace(150)),
				JBox.vglue(),
				JBox.hglue(), JBox.hglue()
		);

		//  con.setupBoard(top, bottom, board, 7, 4, 10);
		//  con.setupBoard(top, bottom, board, 7, 4, 10);
		//  con.setupBoard(top, bottom, board, 6, 3, 1);
		//  con.setupBoard(top, bottom, board, 5, 3, 10);
		//  con.setupBoard(top, bottom, board, 7, 2, 1);
		//  con.setupBoard(top, bottom, board, 6, 2, 10);

		format.add(top);
		format.add(totalBoard);
		format.add(bottom);

		frame.add(format);
		frame.setVisible(true);

		JEventQueue events = new JEventQueue();
		events.listenTo(button1,"Quit");
		events.listenTo(button2,"Reset");
		for(int r = 0; r < rows; r++)
		{
			for(int c = 0; c < cols; c++)
			{
				events.listenTo(input[r][c], "" + c);
			}
		}


		while(true)
		{
			EventObject event = events.waitEvent();
			String name;
			name = JEventQueue.getName(event);
			switch (name) {
				case "Quit":
					System.exit(100);
				case "Reset":
					con.reset(top, bottom);
					for (int r = 0; r < rows; r++) {
						for (int c = 0; c < cols; c++) {
							events.listenTo(input[r][c], "" + c);
						}
					}
					label2.setText("Playing...");
					break;
				default:
					if ((label2.getText()).equals("Playing...")) {
						int playerMove = Integer.parseInt(name);
						while (isFull(playerMove)) {
							event = events.waitEvent();
							name = JEventQueue.getName(event);
							playerMove = Integer.parseInt(name);
						}

						con.gravity(playerMove, bottom, Color.RED);
					}

					//printArray(board);

					winLocation = p.endGame(board);

					if (winLocation[0][0] == -1)
						label2.setText("Playing...");
					else {
						if (board[winLocation[0][0]][winLocation[0][1]] == 1)
							label2.setText("Player Red wins!");
					/* TODO
					 * Highlight the squares red at
					 * board[winLocation[0][0]][winLocation[0][1]]
					 * board[winLocation[1][0]][winLocation[1][1]]
					 * board[winLocation[2][0]][winLocation[2][1]]
					 * board[winLocation[3][0]][winLocation[3][1]]
					 */
						else if (board[winLocation[0][0]][winLocation[0][1]] == 10)
							label2.setText("Player Blue wins!");
					/*
					 * Highlight the squares blue at
					 * board[winLocation[0][0]][winLocation[0][1]]
					 * board[winLocation[1][0]][winLocation[1][1]]
					 * board[winLocation[2][0]][winLocation[2][1]]
					 * board[winLocation[3][0]][winLocation[3][1]]
					 */
					}

					if ((label2.getText()).equals("Playing...")) {
						int nextMove = p.move(board, 10);
						while (isFull(nextMove)) {
							nextMove = p.move(board, 10);
						}

						con.gravity(nextMove, bottom, Color.BLUE);
					}

					//printArray(board);

					winLocation = p.endGame(board);

					if (winLocation[0][0] == -1)
						label2.setText("Playing...");
					else {
						if (board[winLocation[0][0]][winLocation[0][1]] == 1)
							label2.setText("Player Red wins!");
						/*
						 * Highlight the squares red at
						 * board[winLocation[0][0]][winLocation[0][1]]
						 * board[winLocation[1][0]][winLocation[1][1]]
						 * board[winLocation[2][0]][winLocation[2][1]]
						 * board[winLocation[3][0]][winLocation[3][1]]
						 */
						else if (board[winLocation[0][0]][winLocation[0][1]] == 10)
							label2.setText("Player Blue wins!");
						/*
						 * Highlight the squares blue at
						 * board[winLocation[0][0]][winLocation[0][1]]
						 * board[winLocation[1][0]][winLocation[1][1]]
						 * board[winLocation[2][0]][winLocation[2][1]]
						 * board[winLocation[3][0]][winLocation[3][1]]
						 */
					}
					break;
			}
		}
	}

	/*
	 * isFull returns true if the specified col parameter
	 * is full. Else returns false.
	 */
	private static boolean isFull(int col)
	{
		return (board[0][col] != 0);
	}

	/*
	 * TEST METHOD
	 * printArray prints out the array after each move.
	 */
	//	private static void printArray(int[][] board){
	//		System.out.println();
	//		for(int i = 0; i < 8; i++){
	//			for(int j = 0; j < 8; j++){
	//				System.out.print("[" + board[r][j] + "] ");
	//			}
	//			System.out.println();
	//		}
	//		System.out.println();
	//	}
}