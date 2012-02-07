import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Board { 
	public static final int BOARD_WIDTH = 4;
	public static final int BOARD_HEIGHT = 4;
	private Random r;
	private Dictionary dict;
	private BoggleDie board[][];

	/**
	 * Creates a new board, given a dice file and dictionary.  The initial board should <b>not</b>
	 * be randomized, but based on the input file -- dice added from left-to-right and top-to-bottom, 
	 * with the initial side facing up
	 * @param diceFilename file containing a description of the dice
	 * @param dict The dictionary to use for checking word validity
	 * @throws IOException
	 */
	public Board(String diceFilename, Dictionary dict) throws IOException {
		this.dict = dict;
		board = new BoggleDie[BOARD_WIDTH][BOARD_HEIGHT];
		r = new Random();
		Scanner s = new Scanner(new File(diceFilename));
		for (int i = 0; i < BOARD_WIDTH; i++) {
			for (int j = 0; j < BOARD_HEIGHT; j++)
				board[i][j] = new BoggleDie(s.nextLine());
		}
	}

	/**
	 * Shuffles the board
	 */
	public void shuffle() {
		for (int i = 0; i < BOARD_WIDTH; i++) {
			for (int j = 0; j < BOARD_HEIGHT; j++)
				board[i][j].setFacing(r.nextInt(6));
		}
		for (int i = 0; i < BOARD_WIDTH; i++) {
			for (int j = 0; j < BOARD_HEIGHT; j++) {
				int swapX = r.nextInt(BOARD_WIDTH);
				int swapY = r.nextInt(BOARD_HEIGHT);
				BoggleDie tmp = board[i][j];
				board[i][j] = board[swapX][swapY];
				board[swapX][swapY] = tmp;
			}
		}        
	}

	/**
	 * Returns the character on the top of the piece at the x,y location
	 * @param x The x location of the piece to check
	 * @param y The y location of the piece to check
	 * @return The character at that location
	 */    
	public char pieceAt(int x, int y) {
		return board[x][y].getTop();
	}

	/**
	 * Returns true if the guess is valid (in the dictionary and on the board, length >= 3)
	 * @param guess The guess to check
	 * @return Returns true if the guess is valid
	 */
	public boolean validGuess(String guess) {
		if(guess.length() < 3 || !dict.check(guess))
			return false;

		guess = guess.toUpperCase();
		char firstChar = guess.charAt(0);

		for (int i = 0; i < BOARD_WIDTH; i++) {
			for (int j = 0; j < BOARD_HEIGHT; j++)	{
				if (firstChar == pieceAt(i, j) && onBoard(guess.substring(1, guess.length()), new boolean[BOARD_WIDTH][BOARD_HEIGHT], i, j))
					return true;
			}
		}

		return false;
	}


	private boolean onBoard(String guess, boolean[][] visited, int x, int y) {
		int length = guess.length();

		if(length == 0)			
			return true;

		visited[x][y] = true;

		char firstChar = guess.charAt(0);

		int minX = x > 0 ? x - 1 : 0; 
		int minY = y > 0 ? y - 1 : 0; 

		int maxX = x < 3 ? x + 1 : 3;
		int maxY = y < 3 ? y + 1 : 3;

		for (int i = minX; i <= maxX; i++){
			for (int j = minY; j <= maxY; j++){
				if(!visited[i][j] && firstChar == pieceAt(i, j))
					return onBoard(guess.substring(1, length), visited, i, j);
			}
		}

		return false;
	}


	/**
	 * Check a string of player moves, to see which ones are legal.  Returns the score
	 * for the move, and (in the output parameter playerMoves), a set of the legal
	 *  words from the player input
	 * @param playerInput An <b>input</b> parameter, passing in a string of words separated by "\n"
	 * that the player is guessing
	 * @param moves An <b>output</b> parameter, returning the legal words in the string playerInput
	 * @return The score for the move:  1 point for each 3-letter word, 2 points for each 4-letter word,
	 * 3 points for each 5-letter word, and so on.
	 */
	public int playerMove(String playerInput, Set<String> moves) {
		String[] input = playerInput.split("\n");
		int score = 0;
		moves.clear();
		for (int i = 0; i < input.length; i++) {
			if (input[i].length() > 0 && validGuess(input[i]) && !moves.contains(input[i])) {
				moves.add(input[i]);
				score = score + input[i].length() - 2;
			}
		}        
		return score;
	}


	void printBoard() {
		for (int i = 0; i < BOARD_HEIGHT; i++) {
			for (int j = 0; j < BOARD_WIDTH; j++)
				System.out.print(pieceAt(i, j));
			System.out.println();
		}
	}


	/**
	 * Returns the score for the computer move, and (in the output parameter computerMove)
	 * the set of moves that the computer makes.  The computer should find all valid words that
	 * are different from the player
	 * @param playerMove The set of all words in the players guess
	 * @param computerMove An output parameter, the moves made by the computer.  This should 
	 * be a set of all words that can be found in the board that are <b>not</b> in the set playerMove
	 * @return The score for the computer -- 1 point for each 3-letter word, 2 points for each 4 letter
	 * word, 3 points for each 5 letter word, and so on. 
	 */
	public int computerMove(Set<String> playerMove, Set<String> computerMove) {
		for (int i = 0; i < BOARD_WIDTH; i++) {
			for (int j = 0; j < BOARD_HEIGHT; j++){
				//System.out.println(dict.check("PO"));
				startPoint("" + pieceAt(i, j), new boolean[BOARD_WIDTH][BOARD_HEIGHT], i, j, computerMove);
				//startPoint("" + pieceAt(i, j), new boolean[BOARD_WIDTH][BOARD_HEIGHT], i, j, computerMove);
			}
		}

		computerMove.removeAll(playerMove);

		int points = 0;
		for (String s : computerMove)
			points += s.length() - 2;

		return points; 
	}



	private void startPoint(String string, boolean[][] visited, int x, int y, Set<String> computerMove){
		visited[x][y] = true;
		int minX = x > 0 ? x - 1 : 0; 
		int minY = y > 0 ? y - 1 : 0; 

		int maxX = x < 3 ? x + 1 : 3;
		int maxY = y < 3 ? y + 1 : 3;

		for (int i = minX; i <= maxX; i++){
			for (int j = minY; j <= maxY; j++){
				if(!visited[i][j]){
					boolean[][] clonevisited = new boolean[BOARD_WIDTH][BOARD_HEIGHT];
					for(int i1 = 0; i1 < 4; i1++){
						for (int j1 = 0; j1< 4; j1 ++){
							clonevisited[i1][j1] = visited[i1][j1];
						}
					}

					String newWord = string + pieceAt(i, j);


					/* ERRORS 
					 * Exception in thread "AWT-EventQueue-0" java.lang.ArrayIndexOutOfBoundsException: -17
					at Node.getChild(Node.java:25)
					at Dictionary.checkPrefix(Dictionary.java:94)
					at Dictionary.checkPrefix(Dictionary.java:39)
						at Board.startPoint(Board.java:201)*/
					
					
					
					if (newWord.length() >= 3 && dict.check(newWord)){
						computerMove.add(newWord);
						startPoint(newWord, clonevisited, i, j, computerMove);
					}
					else if (dict.checkPrefix(newWord)){
						startPoint(newWord, clonevisited, i, j, computerMove);

					}
				}
			}
		}
	}
}