package com.game.frame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GobangChessFrame extends JFrame implements MouseListener, Runnable {
    // get the width of screen
    int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    // get the height of screen
    int height = Toolkit.getDefaultToolkit().getScreenSize().height;
    // get background
    BufferedImage bgImage = null;
    // position of stone
    int x = 0;
    int y = 0;
    // save previouse index
    int[][] allChess = new int[15][15];
    // next stone status
    Boolean isBlack = true;
    // if game can continue
    Boolean canPlay = true;
    // info string
    String message = "Black turn";
    // save time remain(s)
    int maxTime = 0;
    Thread t = new Thread(this);
    int blackTime = 0;
    int whiteTime = 0;
    String blackMessage = "No limit";
    String whiteMessage = "No limit";

    public GobangChessFrame () {
        // set title
        this.setTitle("Gobang");
        // set size
        this.setSize(500, 520);
        // set location
        this.setLocation((width - 500) / 2, (height - 500) / 2);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // insert inspection
        this.addMouseListener(this);

        try {
            String fileName = GobangChessFrame.class.getResource("") + "Background.png";
            fileName = fileName.split(":")[1];
            bgImage = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setVisible(true);

        t.start();
        t.suspend();

        this.repaint();
    }

    public void paint(Graphics g) {
        // draw background
        g.drawImage(bgImage, 0, 20, this);
        g.setFont(new Font("", Font.BOLD, 30));
        g.drawString("Info: " + message, 220, 70);
        g.setFont(new Font("", 0, 16));
        g.drawString("Black Time: " + blackMessage, 30, 485);
        g.drawString("White Time: " + whiteMessage, 280, 485);

        // draw lines
        for (int i = 0; i < 15; i++) {
            g.drawLine(24, 84 + 25*i, 374, 84 + 25*i);
        }
        for (int i = 0; i < 15; i++) {
            g.drawLine(24 + 25*i, 84, 24 + 25*i, 434);
        }

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                // black stone
                if (allChess[i][j] == 1) {
                    g.setColor(Color.BLACK);
                    g.fillOval(16+i*25, 76+j*25, 16,16);
                }
                // white stone
                if (allChess[i][j] == 2) {
                    g.setColor(Color.BLACK);
                    g.fillOval(16+i*25, 76+j*25, 16,16);
                    g.setColor(Color.WHITE);
                    g.fillOval(17+i*25, 77+j*25, 14,14);
                }
            }
        }

    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (canPlay == true) {
            x = e.getX();
            y = e.getY();
            if (x > 16 && x < 382 && y > 76 && y < 442) {
                x = (x - 16) / 25;
                y = (y - 76) / 25;
                if (allChess[x][y] == 0) {
                    if (isBlack) {
                        allChess[x][y] = 1;
                        message = "White turn";
                    } else {
                        allChess[x][y] = 2;
                        message = "Black turn";
                    }
                    isBlack = !isBlack;

                    // check if win
                    boolean flag = this.checkWin();
                    if (flag == true) {
                        JOptionPane.showMessageDialog(this, (allChess[x][y]==1?"Black":"White") + " Win!");
                        canPlay = false;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Already have stone here!");
                }
                this.repaint();
            }
        }
        int tempX = e.getX();
        int tempY = e.getY();

        if (tempX >= 390 && tempX <= 485) {
            if (tempY >= 85 && tempY <= 117) {
                // start game
                int result = JOptionPane.showConfirmDialog(this, "Do you want to restart?");
                if (result == 0) {
                    allChess = new int[15][15];
                    message = "Black turn";
                    isBlack = true;
                    canPlay = true;
                    blackTime = maxTime;
                    whiteTime = maxTime;
                    if (maxTime == 0) {
                        blackMessage = "No limit!";
                        whiteMessage = "No limit!";
                    } else {
                        blackMessage = maxTime / 3600 + ":" + (maxTime % 3600) / 60 + ":" + (maxTime % 60);
                        whiteMessage = maxTime / 3600 + ":" + (maxTime % 3600) / 60 + ":" + (maxTime % 60);
                        t.resume();
                    }
                    this.repaint();
                }
            }
            if (tempY >= 138 && tempY <= 170) {
                // setting game
                String input = JOptionPane.showInputDialog("Please input the max time of this game(min):");
                try {
                    maxTime = Integer.parseInt(input) * 60;
                    if (maxTime < 0) {
                        JOptionPane.showMessageDialog(this, "Please enter valid info.");
                    } else {
                        int result = JOptionPane.showConfirmDialog(this, "Successfully set! Do you want to restart?");
                        if (result == 0) {
                            for (int i = 0; i < 15; i++) {
                                for (int j = 0; j < 15; j++) {
                                    allChess[i][j] = 0;
                                }
                            }
                            message = "Black turn";
                            isBlack = true;
                            blackTime = maxTime;
                            whiteTime = maxTime;
                            if (maxTime == 0) {
                                blackMessage = "No limit!";
                                whiteMessage = "No limit!";
                            } else {
                                blackMessage = maxTime / 3600 + ":" + (maxTime % 3600) / 60 + ":" + (maxTime % 60);
                                whiteMessage = maxTime / 3600 + ":" + (maxTime % 3600) / 60 + ":" + (maxTime % 60);
                                t.resume();
                            }
                            this.repaint();
                        }
                    }
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(this, "Please enter valid info.");
                }


            }
            if (tempY >= 190 && tempY <= 212) {
                // introduction game
                JOptionPane.showMessageDialog(this, "This territorial game has a grid of 15x15. \nPlayers alternate in placing a stone of their color (black or white) on an empty intersection. \nThe winner is the first player to get an unbroken row of fives stones horizontally, vertically, or diagonally.");
            }
            if (tempY >= 300 && tempY <= 332) {
                // give up
                int result = JOptionPane.showConfirmDialog(this, "Do you want to give up?");
                if (result == 0) {
                    if (isBlack) {
                        JOptionPane.showMessageDialog(this, "Black give up, White Win!");
                    } else {
                        JOptionPane.showMessageDialog(this, "White give up, Black Win!");
                    }
                    canPlay = false;
                }
            }
            if (tempY >= 352 && tempY <= 383) {
                // about
                JOptionPane.showMessageDialog(this, "Email: xichen.s@northeastern.edu");
            }
            if (tempY >= 406 && tempY <= 436) {
                // exit
                JOptionPane.showMessageDialog(this, "Game over");
                System.exit(0);
            }
        }
    }

    private boolean checkWin() {
        boolean flag = false;
        int count = 1;
        int color = allChess[x][y];

        count = this.checkCount(1,0,color);
        if (count >= 5) {
            return true;
        }
        count = this.checkCount(0,1,color);
        if (count >= 5) {
            return true;
        }
        count = this.checkCount(1,-1,color);
        if (count >= 5) {
            return true;
        }
        count = this.checkCount(1,1,color);
        if (count >= 5) {
            return true;
        }
        return flag;
    }

    private int checkCount(int xChange, int yChange, int color) {
        int count = 1;
        int tempX = xChange;
        int tempY = yChange;
        while (checkValid(x+xChange) && checkValid(y+yChange) && color == allChess[x+xChange][y+yChange]) {
            count++;
            if (xChange != 0) {
                if (xChange > 0) {
                    xChange++;
                } else {
                    xChange--;
                }
            }
            if (yChange != 0) {
                if (yChange > 0) {
                    yChange++;
                } else {
                    yChange--;
                }
            }
        }
        xChange = tempX;
        yChange = tempY;
        while (checkValid(x-xChange) && checkValid(y-yChange) && color == allChess[x-xChange][y-yChange]) {
            count++;
            if (xChange != 0) {
                if (xChange > 0) {
                    xChange++;
                } else {
                    xChange--;
                }
            }
            if (yChange != 0) {
                if (yChange != 0) {
                    if (yChange > 0) {
                        yChange++;
                    } else {
                        yChange--;
                    }
                }
            }
        }

        return count;
    }

    private boolean checkValid (int val) {
        boolean res = true;
        if (val < 0 || val >= 15) {
            return false;
        }
        return res;
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void run() {
        if (maxTime > 0) {
            while(true) {
                if (isBlack) {
                    blackTime--;
                    if (blackTime == 0) {
                        JOptionPane.showMessageDialog(this, "Black time out, game over.");
                        this.restart();
                    }
                } else {
                    whiteTime--;
                    if (whiteTime == 0) {
                        JOptionPane.showMessageDialog(this, "White time out, game over.");
                        this.restart();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                blackMessage = blackTime / 3600 + ":" + (blackTime % 3600) / 60 + ":" + (blackTime % 60);
                whiteMessage = whiteTime / 3600 + ":" + (whiteTime % 3600) / 60 + ":" + (whiteTime % 60);
                this.repaint();
            }
        }
    }

    public void restart(){
        allChess = new int[15][15];
        message = "Black turn";
        isBlack = true;
        canPlay = true;
        blackTime = maxTime;
        whiteTime = maxTime;
        if (maxTime == 0) {
            blackMessage = "No limit!";
            whiteMessage = "No limit!";
        } else {
            blackMessage = maxTime / 3600 + ":" + (maxTime % 3600) / 60 + ":" + (maxTime % 60);
            whiteMessage = maxTime / 3600 + ":" + (maxTime % 3600) / 60 + ":" + (maxTime % 60);
            t.resume();
        }
        this.repaint();
    }
}
