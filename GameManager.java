import java.io.*;

/**
 * <h3>Created by James on 12/03/2016.</h3>
 * <p>
 * <p>The GameManager runs the whole game and tracks the users stats</p>
 */
public class GameManager implements Serializable {

    private static int lifetimeWins;
    private static int lifetimeLosses;
    private boolean isDebug = false;
    private GUI gui;
    private Board board;
    private boolean firstClick;
    private Difficulty difficulty;
    private int scale;
    private int sessionWins;
    private int sessionLosses;

    /**
     * Constructor for the GameManager. This will set the game up with default settings.
     */
    public GameManager() {
        if (checkForSave()) {
            loadSave(true);
        } else {
            difficulty = Difficulty.BEGINNER;
            board = new Board(difficulty);
            gui = new GUI(board, this);
            firstClick = true;
        }
    }

    /**
     * Accessor for the lifeTimeWins field
     *
     * @return int of the lifeTimeWins
     */
    public static int getLifetimeLosses() {
        return lifetimeLosses;
    }

    /**
     * Accessor for the lifeTimeLosses field
     *
     * @return int of the lifeTimeLosses field
     */
    public static int getLifetimeWins() {
        return lifetimeWins;
    }

    /**
     * Main method to launch the program
     *
     * @param args
     */
    public static void main(String[] args) {
        new GameManager();
    }

    /**
     * Issues a next turn command to update the state of the game
     *
     * @param c      Reference of the cell that has just been revealed
     * @param button int of which mouse button was used (left = 1)
     */
    public void nextTurn(Cell c, int button) {
        int y = c.getY();
        int x = c.getX();
        if (firstClick) {
            board.setSafeCells(x, y);
            board.generateMines();
            firstClick = false;
        }
        setZeroReveal(x, y);
        if (c.isMine() && !c.isFlagged() && button == 1) {
            gui.updateGUI();
            gameLose();
        }
        if (board.nonMinesLeft() == 0) {
            gui.updateGUI();
            gameWin();
        }
        gui.updateGUI();
    }

    /**
     * Accessor fot the sessionWins field
     *
     * @return int of the session wins
     */
    public int getSessionWins() {
        return sessionWins;
    }

    /**
     * Accessor for the sessionLoses field
     *
     * @return int of the session losses
     */
    public int getSessionLosses() {
        return sessionLosses;
    }

    /**
     * Reveals cells adjacent to any cell that has zero mines adjacent to it
     *
     * @param x x position of the cell
     * @param y y position of the cell
     */
    private void setZeroReveal(int x, int y) {
        if (board.getAdjacentMines(x, y) == 0) {
            board.getCell(x, y).setRevealed(true);
            for (Cell c : board.getAdjacentCells(x, y)) {
                c.setRevealed(true);
                c.setFlagged(false);
                if (board.getAdjacentMines(c.getX(), c.getY()) == 0 && !c.isZeroReveal()) {
                    c.setZeroReveal(true);
                    setZeroReveal(c.getX(), c.getY());
                }
            }
        }
    }

    /**
     * Checks if the user already has a save file
     *
     * @return boolean depending if there is a save file
     */
    private boolean checkForSave() {
        File f = new File("game.data");
        return f.exists() && !f.isDirectory();
    }

    /**
     * Loads the save file
     *
     * @param loadGUI boolean depending where there is already a GUI object
     */
    public void loadSave(boolean loadGUI) {
        try (FileInputStream fs = new FileInputStream("game.data")) {
            ObjectInputStream os = new ObjectInputStream(fs);
            Object gamesWon = os.readObject();
            Object gamesLost = os.readObject();
            Object board = os.readObject();
            Object firstClick = os.readObject();
            Object difficulty = os.readObject();
            Object scale = os.readObject();

            GameManager.lifetimeWins = (int) gamesWon;
            GameManager.lifetimeLosses = (int) gamesLost;
            this.board = (Board) board;
            this.firstClick = (boolean) firstClick;
            this.difficulty = (Difficulty) difficulty;
            setScale((int) scale);

            if (loadGUI) {
                gui = new GUI(this.board, this);
            } else {
                gui.setBoard(this.board);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current session
     */
    public void save() {
        try (FileOutputStream fs = new FileOutputStream("game.data")) {
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(lifetimeWins);
            os.writeObject(lifetimeLosses);
            os.writeObject(board);
            os.writeObject(firstClick);
            os.writeObject(difficulty);
            os.writeObject(scale);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the BoardView scale
     *
     * @return int of the BoardView scale
     */
    public int getScale() {
        return scale;
    }

    /**
     * Sets the scale of the BoardView
     *
     * @param scale int of the scale
     */
    public void setScale(int scale) {
        this.scale = scale;
    }

    /**
     * Called when the user wins to reset the game and update scores
     */
    public void gameWin() {
        lifetimeWins++;
        sessionWins++;
        gui.showWin();
        newGame();
    }

    /**
     * Called when the user loses to reset the game and update scores
     */
    public void gameLose() {
        lifetimeLosses++;
        sessionLosses++;
        gui.showLoss();
        newGame();
    }

    /**
     * Resets the game and updates the GUI
     */
    private void newGame() {
        firstClick = true;
        board.generateNewBoard();
        gui.updateGUI();
        gui.pack();
    }

    /**
     * Prompts the user to confirm starting a new game
     */
    public void newGameConfirm() {
        if (gui.confirm()) {
            newGame();
        }
    }

    /**
     * Called when the game closes
     */
    public void exitGame() {
        save();
        System.exit(0);
    }

    /**
     * Returns the isDebug field
     *
     * @return boolean of the isDebug field
     */
    public boolean isDebug() {
        return isDebug;
    }

    /**
     * Mutator of the isDebug field
     *
     * @param debug boolean to set the isDebug field to
     */
    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    /**
     * Accessor for the difficulty field
     *
     * @return Difficulty enum of the difficulty field
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Mutator of the difficulty field
     *
     * @param d Difficulty to set to
     */
    public void setDifficulty(Difficulty d) {
        if (!(difficulty == d) && gui.confirmDifficulty(d)) {
            difficulty = d;
            board.setBoardDifficulty(difficulty);
            gui.updateGUI();
            gui.pack();
        }
    }

    /**
     * Enums for the default difficulty settings. CUSTOM has yet to be implemented.
     */
    public enum Difficulty {
        BEGINNER, MEDIUM, EXPERT, CUSTOM
    }

}