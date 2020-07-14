
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.SecureRandom;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Connor McNally
 */
public class MineSweeperWindow extends JFrame implements MouseListener {

    Tile[][] tiles;
    public int height;
    public int width;
    public int numOfBombs;
    public JMenuBar menuBar;
    public JMenu fileMenu;
    public JMenuItem newGameOption;
    public JMenuItem exitOption;
    public JMenuItem optionsOption;
    public int numUncovered;

    public MineSweeperWindow(int Height, int Width, int NumOfBombs) {

        super("MineSweeper");

        height = Height;
        width = Width;
        numOfBombs = NumOfBombs;
        numUncovered = 0;

        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        newGameOption = new JMenuItem();
        exitOption = new JMenuItem();
        optionsOption = new JMenuItem();

        fileMenu.setText("File");
        newGameOption.setText("New Game");
        exitOption.setText("Exit");
        optionsOption.setText("Options");

        newGameOption.addActionListener(e -> {

            restartGame();
        });

        optionsOption.addActionListener(e -> {

            NewGameMenu options = new NewGameMenu(this);
            options.setVisible(true);
        });

        exitOption.addActionListener(e -> {

            System.exit(0);
        });

        fileMenu.add(newGameOption);
        fileMenu.add(optionsOption);
        fileMenu.add(exitOption);
        menuBar.add(fileMenu);

        this.setJMenuBar(menuBar);
        menuBar.setVisible(true);
        fileMenu.setVisible(true);
        newGameOption.setVisible(true);

        this.setSize(width * 30, height * 30);

        this.setLayout(new GridLayout(height, width));

        // create tiles array based off current height and width and add them to frame
        addTiles();

        // lay numOfBombs mines randomly
        layMines();

        // calculate and set the number of adjacent bombs for each tile
        setTileNumbers();

        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    // calculate and set the number of adjacent bombs for each tile
    public void setTileNumbers() {

        // set Tile numbers
        for (int i = 0; i < tiles.length; ++i) {
            for (int j = 0; j < tiles[i].length; ++j) {

                if (tiles[i][j].hasBomb) {

                    if (i - 1 >= 0) {
                        ++tiles[i - 1][j].number;
                    }

                    if (i - 1 >= 0 && j - 1 >= 0) {
                        ++tiles[i - 1][j - 1].number;
                    }

                    if (i - 1 >= 0 && j + 1 < width) {
                        ++tiles[i - 1][j + 1].number;
                    }

                    if (j - 1 >= 0) {
                        ++tiles[i][j - 1].number;
                    }

                    if (j + 1 < width) {
                        ++tiles[i][j + 1].number;
                    }

                    if (i + 1 < height) {
                        ++tiles[i + 1][j].number;
                    }

                    if (i + 1 < height && j - 1 >= 0) {
                        ++tiles[i + 1][j - 1].number;
                    }

                    if (i + 1 < height && j + 1 < width) {
                        ++tiles[i + 1][j + 1].number;
                    }

                }

            }
        }
    }

    // lay numOfBombs mines. If tile already has a mine, skip and find another
    public void layMines() {

        SecureRandom rand = new SecureRandom();

        for (int i = 0; i < numOfBombs; i++) {

            int y = rand.nextInt(height);
            int x = rand.nextInt(width);

            if (!tiles[y][x].hasBomb) {
                tiles[y][x].hasBomb = true;

            } else {
                i--;

            }
        }
    }

    // recursive function that uncovers adjacent "0" tiles
    public void revealEmptyTiles(int i, int j) {

        // if it's a zero tile, reveal it, and check adjacent tiles
        if (tiles[i][j].number == 0 && !tiles[i][j].isSelected) {
            tiles[i][j].setText("");
            tiles[i][j].setBackground(Color.LIGHT_GRAY);
            tiles[i][j].isSelected = true;
            ++numUncovered;

            if (i - 1 >= 0) {
                revealEmptyTiles(i - 1, j);
            }

            if (i - 1 >= 0 && j - 1 >= 0) {
                revealEmptyTiles(i - 1, j - 1);
            }

            if (i - 1 >= 0 && j + 1 < width) {
                revealEmptyTiles(i - 1, j + 1);
            }

            if (j - 1 >= 0) {
                revealEmptyTiles(i, j - 1);
            }

            if (j + 1 < width) {
                revealEmptyTiles(i, j + 1);
            }

            if (i + 1 < height) {
                revealEmptyTiles(i + 1, j);
            }

            if (i + 1 < height && j - 1 >= 0) {
                revealEmptyTiles(i + 1, j - 1);
            }

            if (i + 1 < height && j + 1 < width) {
                revealEmptyTiles(i + 1, j + 1);
            }

            System.out.println(i + " " + j);

            // if it's a non-zero tile, reveal it, but dont reveal adjacent tiles
        } else if (!tiles[i][j].hasBomb && tiles[i][j].number != 0 && !tiles[i][j].isSelected) {

            tiles[i][j].setText(Integer.toString(tiles[i][j].number));
            tiles[i][j].setBackground(Color.LIGHT_GRAY);
            tiles[i][j].isSelected = true;
            ++numUncovered;

            System.out.println(i + " " + j);
        }

    }

    public void restartGame() {

        for (int i = 0; i < tiles.length; ++i) {
            for (int j = 0; j < tiles[i].length; ++j) {

                tiles[i][j].number = 0;
                tiles[i][j].isFlagged = false;
                tiles[i][j].isSelected = false;
                tiles[i][j].hasBomb = false;
                tiles[i][j].setBackground(new Color(220, 220, 220));
                tiles[i][j].setText("");

            }
        }

        layMines();
        setTileNumbers();
        numUncovered = 0;

    }

    public void addTiles() {

        tiles = new Tile[height][width];

        // create and add tiles
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {

                tiles[i][j] = new Tile(i, j);
                this.add(tiles[i][j]);
                tiles[i][j].setText("");
                tiles[i][j].setFont(new Font("Dialog", 1, 12));
                tiles[i][j].setBackground(new Color(220, 220, 220));
                tiles[i][j].setMargin(new Insets(1, 1, 1, 1));
                tiles[i][j].addMouseListener(this);
            }
        }

    }

    // used when options changed
    public void rebuildMinefield() {

        // remove all current tiles
        for (int i = 0; i < tiles.length; ++i) {
            for (int j = 0; j < tiles[i].length; ++j) {

                this.remove(tiles[i][j]);

            }
        }

        this.setSize(width * 30, height * 30);
        this.setLayout(new GridLayout(height, width));

        addTiles();
        layMines();
        setTileNumbers();
        numUncovered = 0;

        this.repaint();

    }

    public void checkWin() {

        if ((numUncovered + numOfBombs) == (height * width)) {

            for (int i = 0; i < tiles.length; ++i) {
                for (int j = 0; j < tiles[i].length; ++j) {

                    if (tiles[i][j].hasBomb) {

                        tiles[i][j].setText("💣");
                        tiles[i][j].setBackground(Color.green);
                    }
                }
            }

            JOptionPane.showMessageDialog(null, "You Win!");

        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        Tile tile = (Tile) e.getComponent();

        // if tile left clicked
        if (isLeftMouseButton(e)) {

            if (!tile.isSelected && !tile.isFlagged) {

                // blow shit up
                if (tile.hasBomb) {

                    // reveal all tiles and mines
                    for (int i = 0; i < tiles.length; ++i) {
                        for (int j = 0; j < tiles[i].length; ++j) {

                            if (!tiles[i][j].isSelected) {

                                tiles[i][j].isSelected = true;

                                tiles[i][j].setText(Integer.toString(tiles[i][j].number));
                                tiles[i][j].setBackground(Color.LIGHT_GRAY);

                                if (tiles[i][j].hasBomb) {
                                    tiles[i][j].setText("💣");
                                    tiles[i][j].setBackground(Color.red);
                                }
                                if (tiles[i][j].getText().equals("0")) {
                                    tiles[i][j].setText("");
                                }
                                if (tiles[i][j].isFlagged) {
                                    tiles[i][j].setBackground(Color.yellow);
                                }
                                if (tiles[i][j].hasBomb && tiles[i][j].isFlagged) {
                                    tiles[i][j].setBackground(Color.orange);
                                }
                            }
                        }
                    }

                    tile.setText("💥");
                    this.repaint();

                    // normal non-zero tile
                } else if (tile.number != 0) {

                    tile.isSelected = true;
                    ++numUncovered;
                    tile.setBackground(Color.LIGHT_GRAY);
                    tile.setText(Integer.toString(tile.number));
                    checkWin();

                    // zero tile
                } else if (tile.number == 0) {

                    revealEmptyTiles(tile.i, tile.j);

                    // Comment this out for a cool effect when clearing empty tiles, otherwise
                    // they all get cleared at the same time
                    // this.repaint();
                    checkWin();

                }
            }
            // on right click (for setting flags)
        } else if (isRightMouseButton(e)) {

            if (!tile.isSelected && !tile.isFlagged) {

                tile.isFlagged = true;
                tile.setBackground(Color.yellow);

            } else if (tile.isFlagged) {

                tile.isFlagged = false;
                tile.setBackground(new Color(220, 220, 220));

            }

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change
        // body of generated methods, choose Tools | Templates.
    }

    public static void main(String[] args) {

        MineSweeperWindow window = new MineSweeperWindow(30, 30, 50);
        window.setVisible(true);

    }

}
