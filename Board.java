import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


/**
 * <h3>Created by James on 12/03/2016.</h3>
 * <p>
 * <p>The Board object groups and manages the cells in the game, providing methods of querying the status of cells</p>
 */
public class Board implements Serializable {

    private int boardX;
    private int boardY;
    private int numberOfMines;
    private Cell[][] cells;

    /**
     * Constructor for the board object, generates a board based on the Difficulty given
     *
     * @param difficulty Difficulty enum (see GameManager) for the difficulty
     */
    public Board(GameManager.Difficulty difficulty) {
        setBoardDifficulty(difficulty);
        generateNewBoard();
    }

    /**
     * Accessor method for the numberOfMines field
     *
     * @return int describing the number of mines
     */
    public int getNumberOfMines() {
        return numberOfMines;
    }

    /**
     * Accesses the cell at the coordinates given
     *
     * @param x x position of the desired cell
     * @param y y position of the desired cell
     *
     * @return Cell object at the given x and y positions
     */
    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    /**
     * Sets the board's difficulty by changing the the size and number of mines of the board
     *
     * @param d Difficulty enum (see GameManager) for the difficulty
     */
    public void setBoardDifficulty(GameManager.Difficulty d) {
        switch (d) {
            case BEGINNER:
                boardX = 9;
                boardY = 9;
                numberOfMines = 10;
                break;
            case MEDIUM:
                boardX = 16;
                boardY = 16;
                numberOfMines = 40;
                break;
            case EXPERT:
                boardX = 30;
                boardY = 16;
                numberOfMines = 99;
                break;
            /* To be implemented
            case CUSTOM:
                boardX = ;
                boardY = ;
                numberOfMines = ;*/
        }
        generateNewBoard();
    }

    /**
     * Accessor method for the boardX field
     *
     * @return int of the boardX field
     */
    public int getBoardX() {
        return boardX;
    }

    /**
     * Accessor method for the boardY field
     *
     * @return int of the boardY filed
     */
    public int getBoardY() {
        return boardY;
    }

    /**
     * Return all cells that are adjacent to the given cell
     *
     * @param x x position of cell
     * @param y y position of cell
     *
     * @return ArrayList<Cell> of all cell references adjacent to the current cell
     */
    public ArrayList<Cell> getAdjacentCells(int x, int y) {

        ArrayList<Cell> list = new ArrayList<>();

        int rowStart;
        int columnStart;
        int columnLimit;
        int rowLimit;

        if (x < 1) {
            rowStart = x;
        } else {
            rowStart = x - 1;
        }
        if (y < 1) {
            columnStart = y;
        } else {
            columnStart = y - 1;
        }
        if (x == boardX - 1) {
            rowLimit = x;
        } else {
            rowLimit = x + 1;
        }
        if (y == boardY - 1) {
            columnLimit = y;
        } else {
            columnLimit = y + 1;
        }

        for (int i = rowStart; i <= rowLimit; i++) {
            for (int j = columnStart; j <= columnLimit; j++) {
                list.add(cells[i][j]);
            }
        }

        return list;
    }

    /**
     * Generates and new board with default cells based on the X and Y of the board
     */
    public void generateNewBoard() {
        cells = new Cell[boardX][boardY];

        for (int i = 0; i < boardX; i++) {
            for (int j = 0; j < boardY; j++) {
                cells[i][j] = new Cell(i, j);
            }
        }
    }

    /**
     * Sets the cell coordinate and all adjacent cell to be safe cells
     *
     * @param x x position of the cell
     * @param y y position of the cell
     */
    public void setSafeCells(int x, int y) {
        for (Cell c : getAdjacentCells(x, y)) {
            c.setSafe(true);
        }
    }

    /**
     * Returns the number of mines adjacent cells
     *
     * @param x x position of the cell
     * @param y y position of the cell
     *
     * @return int of the number of the mines adjacent to the given cell
     */
    public int getAdjacentMines(int x, int y) {
        int count = 0;
        for (Cell c : getAdjacentCells(x, y)) {
            if (c.isMine()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Sets up the board with mines. Cell will not become a mine if it already is a mine and it is not a safe cell
     */
    public void generateMines() {
        Random rand = new Random();
        int count = numberOfMines;

        while (count > 0) {
            int x = rand.nextInt(boardX);
            int y = rand.nextInt(boardY);

            if (!getCell(x, y).isSafe() && !getCell(x, y).isMine()) {
                getCell(x, y).setMine(true);
                count--;
            }
        }
    }

    /**
     * Returns the number of unrevealed cells that are not mines
     *
     * @return int Returns the number of unrevealed cells that are not mines
     */
    public int nonMinesLeft() {
        int count = 0;

        for (Cell[] cellA : cells) {
            for (Cell c : cellA) {
                if (!c.isMine() && !c.isRevealed()) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Returns the number of potential mines left on the board.
     *
     * @return int Returns the number of potential mines left on the board.
     */
    public int minesLeft() {
        int count = 0;

        for (Cell[] cellA : cells) {
            for (Cell c : cellA) {
                if (c.isFlagged()) {
                    count++;
                }
            }
        }
        return numberOfMines - count;
    }
}
