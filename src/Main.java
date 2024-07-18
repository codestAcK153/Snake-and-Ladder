import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

class FDemo extends JFrame {
    JPDemo jp1;

    FDemo() {
        super.setTitle("Snake & Ladder");
        jp1 = new JPDemo();
        add(jp1);
    }

    public static void main(String[] args) {
        FDemo f = new FDemo();
        f.setVisible(true);
        f.setBounds(200, 100, 1000, 1000);
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}

class JPDemo extends JPanel implements ActionListener {
    ImageIcon img1, img2, img3, img4, img5, newIcon;
    public int px1 = 305, py1 = 784;
    public int px2 = 305, py2 = 784;
    int currentPlayer = 1;
    int pos1 = 0, pos2 = 0;
    int ygap = 74, xgap = 79;

    Image board, start;
    Image player1, player2, dice;
    JButton b1, b2, b3, b4;
    JTextField tx1, tx2, tx3;
    Map<Integer, Integer> snake_ladders = new HashMap<>();




    JPDemo() {
        setBackground(Color.white);
        img1 = new ImageIcon("images/board.png");
        img2 = new ImageIcon("images/start.png");
        img3 = new ImageIcon("images/Player1.png");
        img4 = new ImageIcon("images/Player2.png");
        img5 = new ImageIcon("images/1.png");
        board = img1.getImage().getScaledInstance(800, 800, Image.SCALE_DEFAULT);
        player1 = img3.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        player2 = img4.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        dice = img5.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT);
        setLayout(null);

        b1 = new JButton("About");
        b1.setBounds(50, 120, 200, 80);
        add(b1);

        b2 = new JButton("Reset");
        b2.setBounds(50, 220, 200, 80);
        add(b2);
        b2.addActionListener(this);

        b3 = new JButton("Roll");
        b3.setBounds(100, 520, 100, 70);
        add(b3);
        b3.setForeground(Color.RED);
        b3.addActionListener(this);

        Image img = img2.getImage();
        Image newimg = img.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH);
        newIcon = new ImageIcon(newimg);

        b4 = new JButton(newIcon);
        b4.setBounds(50, 620, 200, 80);
        add(b4);
        b4.setBackground(Color.WHITE);

        Font f = new Font("Bauhaus 93", Font.ITALIC, 20);

        tx1 = new JTextField("START GAME");
        tx1.setBounds(50, 350, 150, 35);
        add(tx1);
        tx1.setBackground(Color.BLACK);
        tx1.setForeground(Color.GREEN);
        tx1.setFont(f);

        tx2 = new JTextField("    Player 1");
        tx2.setBounds(50, 390, 150, 35);
        add(tx2);
        tx2.setBackground(Color.BLACK);
        tx2.setForeground(Color.GREEN);
        tx2.setFont(f);

        tx3 = new JTextField("    Player 2");
        tx3.setBounds(50, 430, 150, 35);
        add(tx3);
        tx3.setBackground(Color.BLACK);
        tx3.setForeground(Color.GREEN);
        tx3.setFont(f);

        //snakes
        snake_ladders.put(17,5);
        snake_ladders.put(28,6);
        snake_ladders.put(60,15);
        snake_ladders.put(71,46);
        snake_ladders.put(95,75);

        //ladders
        snake_ladders.put(18,37);
        snake_ladders.put(15,53);
        snake_ladders.put(8,13);
        snake_ladders.put(14,81);
        snake_ladders.put(49,90);
        snake_ladders.put(73,86);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(board, 300, 20, this);
        g.drawImage(player1, 20, 392, this);
        g.drawImage(player2, 20, 432, this);
        g.drawImage(player1, px1, py1, this);
        g.drawImage(player2, px2+10, py2, this);
        g.drawImage(dice, 27, 520, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==b3){
            int roll = (int) Math.round(Math.random()*5 +1);
            updateDiceImage(roll);
            movePlayer(roll);
            repaint();
            System.out.println(roll+ " "+ pos1 + " "+ pos2);
        }
        if(e.getSource()==b2){
            resetGame();
        }
    }

    private void updateDiceImage(int roll) {
        img5 = new ImageIcon("images/" + roll + ".png");
        dice = img5.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT);
    }

    private void movePlayer(int roll) {
        if (currentPlayer == 1) {
            int newPos = pos1 + roll;
            if (newPos == 99) {
                pos1 = 99;
                int[] newPosition = calculateNewPosition(pos1);
                px1 = newPosition[0];
                py1 = newPosition[1];
                displayWinMessage("Player 1");
            } else if (newPos < 99) {
                pos1 = newPos;
                if (snake_ladders.containsKey(pos1)) {
                    pos1 = snake_ladders.get(pos1);
                }
                int[] newPosition = calculateNewPosition(pos1);
                px1 = newPosition[0];
                py1 = newPosition[1];
                switchPlayer();
            } else {
                // If newPos > 100, the player does not move and the turn switches
                switchPlayer();
            }
        } else {
            int newPos = pos2 + roll;
            if (newPos == 99) {
                pos2 = 99;
                int[] newPosition = calculateNewPosition(pos2);
                px2 = newPosition[0];
                py2 = newPosition[1];
                displayWinMessage("Player 2");
            } else if (newPos < 99) {
                pos2 = newPos;
                if (snake_ladders.containsKey(pos2)) {
                    pos2 = snake_ladders.get(pos2);
                }
                int[] newPosition = calculateNewPosition(pos2);
                px2 = newPosition[0];
                py2 = newPosition[1];
                switchPlayer();
            } else {
                // If newPos > 100, the player does not move and the turn switches
                switchPlayer();
            }
        }
    }



    private int[] calculateNewPosition(int currentPosition) {
        int startX = 305; // Starting x-coordinate
        int startY = 784; // Starting y-coordinate
        int xgap = 80; // Width of each cell
        int ygap = 80; // Height of each cell
        int boardSize = 10; // Size of the board (10x10)

        // Calculate the new position on the board
        int newPos = currentPosition;

        // Calculate the new coordinates
        int newX = startX;
        int newY = startY;

        if (newPos < boardSize * boardSize) {
            int newRow = newPos / boardSize;
            int newCol = newPos % boardSize;

            // Rows with alternating directions
            if (newRow % 2 == 0) {
                newX = startX + newCol * xgap;
            } else {
                newX = startX + (boardSize - 1 - newCol) * xgap;
            }

            newY = startY - newRow * ygap;
        } else {
            // If the new position exceeds the board, stay in place
            newX = startX + (boardSize - 1) * xgap;
            newY = startY - (boardSize - 1) * ygap;
        }

        return new int[]{newX, newY};
    }
    private void displayWinMessage(String player) {
        JOptionPane.showMessageDialog(this, player + " wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        resetGame();
    }
    private void resetGame() {
        pos1 = 0;
        pos2 = 0;
        px1 = 305;
        py1 = 784;
        px2 = 305;
        py2 = 784;
        currentPlayer = 1;
        tx1.setText("Player 1's Turn");
        repaint();
    }

    private void switchPlayer() {
        currentPlayer = currentPlayer == 1 ? 2 : 1;
        tx1.setText("Player " + currentPlayer + "'s Turn");
    }
}
