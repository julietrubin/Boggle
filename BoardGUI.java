import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class BoardGUI extends JPanel
{
    private Board myBoard;
    private JTextArea playerInput;
    private JTextArea computerOutput;
    private BoardDisplay middlePanel;
    private JButton done;

    private int playerScore;
    private int computerScore;

    private class BoardDisplay extends JPanel
    {
        public static final int DIE_GRAPHIC_SIZE = 30;

        public BoardDisplay()
        {
            setPreferredSize(new Dimension(Board.BOARD_WIDTH * DIE_GRAPHIC_SIZE + 1,
                    Board.BOARD_HEIGHT * DIE_GRAPHIC_SIZE + 1));
        }

        /**
         * Paint the BoardDisplay panel
         *   Draw the Boggle board (an the ending score, if the game is over) 
         *   on the panel
         * @param g The graphics Object used to paint the component
         */
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            for (int i = 0; i < Board.BOARD_WIDTH; i++)
            {
                for (int j = 0; j < Board.BOARD_HEIGHT; j++)
                {
                    g.drawRect(j*DIE_GRAPHIC_SIZE, i*DIE_GRAPHIC_SIZE,DIE_GRAPHIC_SIZE, DIE_GRAPHIC_SIZE);
                    FontMetrics fm = g.getFontMetrics(g.getFont());
                    int  fontAscent = fm.getAscent();
                    int xCenter = j*DIE_GRAPHIC_SIZE + DIE_GRAPHIC_SIZE / 2;
                    int yCenter = i*DIE_GRAPHIC_SIZE + DIE_GRAPHIC_SIZE / 2;
                    String stringToDraw = "" + myBoard.pieceAt(i, j);
                    g.drawString(stringToDraw,
                            xCenter - fm.stringWidth(stringToDraw) / 2,
                            yCenter + fontAscent / 2);
                }
            }
            if (playerScore >= 0)
            {
                g.drawString("Player: " + playerScore, 5, Board.BOARD_HEIGHT * DIE_GRAPHIC_SIZE + 20);
                g.drawString("Computer: " + computerScore, 5, Board.BOARD_HEIGHT * DIE_GRAPHIC_SIZE + 45);
            }
        }
    }

    /**
     * Class to handle pressing the restart button
     *    Reset the textbox GUI elements
     *      (player & computer moves)
     *    Sets the player and computer scores to -1 
     *      (so they won't be displayed)
     *    Shuffle the board
     */
    private class Restart implements ActionListener
    {
        /**
         * Handle pressing the restart button
         * Reset the textbox GUI elements
         *      (player & computer moves)
         *    Sets the player and computer scores to -1 
         *      (so they won't be displayed)
         *    Shuffle the board
         *    @param arg0 Unused
         */
        public void actionPerformed(ActionEvent arg0)
        {
            playerInput.setText("");
            computerOutput.setText("");
            playerInput.setEditable(true);
            done.setEnabled(true);
            playerScore = -1;
            computerScore = -1;
            myBoard.shuffle();
            middlePanel.repaint();
        }
    }

    /**
     * Class to handle pressing the submit button
     *    Reset the textbox GUI elements
     *      (player & computer moves)
     *    Sets the player and computer scores to -1 
     *      (so they won't be displayed)
     *    Shuffle the board
     */
    private class SubmitButton implements ActionListener 
    {   
        /**
         * Convert a set of strings to a single string, by appending the
         * elements together, separated by a given separator string
         * @param set The set of strings to combine into the string
         * @param separator The separator to use
         * @return The string to return
         */
        public  String setToString(Set<String> set, String separator) {
            StringBuffer result = new StringBuffer();
            Iterator<String> it = set.iterator();
            if (it.hasNext()) {
                result.append(it.next());
                while (it.hasNext()) {
                    result.append(separator);
                    result.append(it.next());
                }
            }
            return result.toString();
        }

        /**
         * Class to handle pressing the submit button
         *    Reset the textbox GUI elements
         *      (player & computer moves)
         *    Sets the player and computer scores to -1 
         *      (so they won't be displayed)
         *    Shuffle the board
         *    @param arg0 Unused
         */
        public void actionPerformed(ActionEvent arg0)
        {
            HashSet<String> playerMove = new HashSet<String>();
            HashSet<String> computerMove = new HashSet<String>();
            playerScore = myBoard.playerMove(playerInput.getText(), playerMove);
            playerInput.setText(setToString(playerMove,"\n"));

            computerOutput.setText("Computing Player Move");

            computerScore = myBoard.computerMove(playerMove, computerMove);
            computerOutput.setText(setToString(computerMove,"\n"));       

            playerInput.setEditable(false);
            done.setEnabled(false);
            middlePanel.repaint();
        }
    }


    /**
     * Set up the Board GUI panel <br>
     *   Place a textbox on the left for player input. <br>
     *   Place the Boggle display panel in the middle. <br>
     *   Place the computer output on the left. <br> 
     *   Add buttons for submission of the user input and resetting the board <br>
     * @param b The Boggle board to display
     */
    public BoardGUI(Board b)  
    {
        playerScore = -1;
        computerScore = -1;
        myBoard = b;
        setLayout(new BorderLayout());

        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BorderLayout());

        playerInput = new JTextArea(15,10);
        JScrollPane playerScrollPane = new JScrollPane(playerInput); 
        playerInput.setEditable(true);
        userPanel.add(playerScrollPane, BorderLayout.NORTH);
        done = new JButton("Submit");
        done.addActionListener(new SubmitButton());
        userPanel.add(done, BorderLayout.SOUTH);

        JPanel computerPanel = new JPanel();
        computerPanel.setLayout(new BorderLayout());
        computerOutput = new JTextArea(15, 10);
        JScrollPane computerScrollPane = new JScrollPane(computerOutput); 
        computerOutput.setEditable(false);
        computerPanel.add(computerScrollPane, BorderLayout.NORTH);
        JButton reset = new JButton("Reset");
        reset.addActionListener(new Restart());
        computerPanel.add(reset, BorderLayout.SOUTH);

        middlePanel = new BoardDisplay();
        add(userPanel, BorderLayout.WEST);
        add(middlePanel, BorderLayout.CENTER);
        add(computerPanel, BorderLayout.EAST);
    }

    public static void main(String args[]) throws IOException
    {
        JFrame frame = new JFrame("Boggle");
        Dictionary dict = new Dictionary("words_ospd.txt");
        Board b = new Board("dice.txt", dict);
        frame.add(new BoardGUI(b));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
    }

}
