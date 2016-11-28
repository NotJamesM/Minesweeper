import java.io.Serializable;

/**
 * <h3>Created by James on 12/03/2016.</h3>
 * <p>
 * <p><b>Cell</b> object that allows the <b>Board</b> and <b>GameManger</b> to query each cell individually</p>
 */
public class Cell implements Serializable {

    private final int x;
    private final int y;
    private boolean isMine;
    private boolean isFlagged;
    private boolean isRevealed;
    private boolean isSafe;
    private boolean zeroReveal;

    /**
     * Constructor for the Cell, sets the cell to it's default status of not a mine, not flagged and not revealed.
     *
     * @param x x position of the cell
     * @param y y position of the cell
     */
    public Cell(int x, int y) {
        this.isMine = false;
        this.isFlagged = false;
        this.isRevealed = false;
        this.x = x;
        this.y = y;
    }

    /**
     * Accessor method for the x position
     *
     * @return int of the x position
     */
    public int getX() {
        return x;
    }

    /**
     * Accessor method for the y position
     *
     * @return int of the y position
     */
    public int getY() {
        return y;
    }

    /**
     * Accessor method for the isMine field
     *
     * @return boolean whether the cell is a mine
     */
    public boolean isMine() {
        return isMine;
    }

    /**
     * Mutator method for the isMine field
     *
     * @param mine boolean whether the cell is a mine
     */
    public void setMine(boolean mine) {
        isMine = mine;
    }

    /**
     * Accessor method for the isFlagged field
     *
     * @return boolean whether the cell is flagged
     */

    public boolean isFlagged() {
        return isFlagged;
    }

    /**
     * Mutator method for the isFlagged field
     *
     * @param flagged boolean whether the cell is flagged
     */

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    /**
     * Accessor method for the isRevealed field
     *
     * @return boolean value whether the cell is revealed.
     */
    public boolean isRevealed() {
        return isRevealed;
    }

    /**
     * Mutator method for the isRevealed field
     *
     * @param revealed boolean value whether the cell is revealed
     */
    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    /**
     * Accessor method for the isSafe field
     *
     * @return boolean value whether the cell is safe (used for mine generation, meaning you can't lose on the first click)
     */
    public boolean isSafe() {
        return isSafe;
    }

    /**
     * Mutator method for the isSafe field
     *
     * @param safe boolean value whether the cell is safe
     */
    public void setSafe(boolean safe) {
        isSafe = safe;
    }

    /**
     * Accessor method for the zeroReveal field. This is used as a flag for the recursive method of revealing cells that have no adjacent mines
     *
     * @return boolean value of whether zeroReveal has tested this cell
     */
    public boolean isZeroReveal() {
        return zeroReveal;
    }

    /**
     * Accessor method for the zeroReveal field. This is used as a flag for the recursive method of revealing cells that have no adjacent mines
     *
     * @param zeroReveal boolean value of whether zeroReveal has tested this cell
     */
    public void setZeroReveal(boolean zeroReveal) {
        this.zeroReveal = zeroReveal;
    }
}

