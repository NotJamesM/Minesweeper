import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;

/**
 * <h3>Created by James Merrington on 12/03/2016.</h3>
 * <p>
 * GUI object that expands JFrame to display the game logic.
 * </p>
 */
public class GUI extends JFrame {

    /**
     * Final GameManger to manage the game
     */
    private final GameManager gm;
    /**
     * Board object that contains the cells and manages them
     */
    private Board board;
    /**
     * Multiple view elements, these are inner classes
     */
    private BoardView boardView;
    private StatusView statusView;
    private InfoView infoView;

    /**
     * Constructor for the GUI.
     *
     * @param board the current board logic object
     * @param gm    the GameManager object
     */
    public GUI(Board board, GameManager gm) {
        this.board = board;
        this.gm = gm;
        build();
        setVisible(true);
    }

    /**
     * Builds the main GUI window and adds listeners
     */
    private void build() {
        //Set title
        setTitle("Minesweeper V2");
        //Window Listener
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                gm.save();
                System.exit(0);
            }
        });
        //Container
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //Menu Bar
        //File Menu
        //Creating items
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem loadItem = new JMenuItem("Load");
        JMenuItem settingsItem = new JMenuItem("Settings");
        JMenuItem exitItem = new JMenuItem("Exit");
        //Adding to menu
        fileMenu.add(newItem);
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.add(new JSeparator());
        fileMenu.add(settingsItem);
        fileMenu.add(new JSeparator());
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        //Setting listeners
        newItem.addActionListener(e -> gm.newGameConfirm());
        saveItem.addActionListener(e -> gm.save());
        loadItem.addActionListener(e -> gm.loadSave(false));
        settingsItem.addActionListener(e -> new OptionsDialog());
        exitItem.addActionListener(e -> gm.exitGame());
        //Set hotkeys
        newItem.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        saveItem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        loadItem.setAccelerator(KeyStroke.getKeyStroke('L', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        settingsItem.setAccelerator(KeyStroke.getKeyStroke('K', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        exitItem.setAccelerator(KeyStroke.getKeyStroke('W', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        //Debug menu
        JMenu debugMenu = new JMenu("Debug");
        JMenuItem winItem = new JMenuItem("Win");
        JMenuItem loseItem = new JMenuItem("Lose");
        JMenuItem overlayItem = new JMenuItem("Mine Overlay");
        winItem.addActionListener(e -> gm.gameWin());
        loseItem.addActionListener(e -> gm.gameLose());
        overlayItem.addActionListener(e -> {
            if (gm.isDebug()) {
                gm.setDebug(false);
            } else {
                gm.setDebug(true);
            }
            updateGUI();
        });
        debugMenu.add(winItem);
        debugMenu.add(loseItem);
        debugMenu.add(overlayItem);
        menuBar.add(debugMenu);
        //Info View
        container.add(infoView = new InfoView(), BorderLayout.EAST);
        //Status View
        container.add(statusView = new StatusView(), BorderLayout.SOUTH);
        //Sizing
        setSize(50 * board.getBoardX(), 50 * board.getBoardY());
        //Board View
        boardView = new BoardView();
        container.add(boardView, BorderLayout.CENTER);
        //packing
        pack();
    }

    /**
     * Updates the values of labels and redraws the board view
     */
    public void updateGUI() {
        statusView.update();
        infoView.update();
        repaint();
    }

    /**
     * Displays a popup with win text
     */
    public void showWin() {
        JOptionPane.showMessageDialog(this, generateWinText(), "Winner!", JOptionPane.PLAIN_MESSAGE);

    }

    /**
     * Generates the text for a the showWin() method
     *
     * @return a string with session wins and lifetime wins
     */
    private String generateWinText() {
        return "You win, your session wins are: " + gm.getSessionWins() + "\nYour lifetime wins are: " + GameManager.getLifetimeWins();
    }

    /**
     * Generates the text for a the showWin() method
     *
     * @return a string with session loses and lifetime loses
     */
    private String generateLossText() {
        return "You lose, your session losses are: " + gm.getSessionLosses() + "\nYour lifetime losses are: " + GameManager.getLifetimeLosses();
    }

    /**
     * Displays a popup with loss text
     */
    public void showLoss() {
        JOptionPane.showMessageDialog(this, generateLossText(), "Game Over!", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Displays a confirmation popup and returns the choice
     *
     * @return boolean value based on the choice of the user
     */
    public boolean confirm() {
        return JOptionPane.showConfirmDialog(this, "Restart game?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == 0;
    }

    /**
     * Sets the current board logic object
     *
     * @param board a board logic object
     */
    public void setBoard(Board board) {
        this.board = board;
        updateGUI();
    }

    /**
     * Displays a confirmation popup that returns the user choice
     *
     * @param difficulty Difficulty enum (see GameManger class) of which difficulty to confirm
     *
     * @return boolean value based on choice of the user
     */
    public boolean confirmDifficulty(GameManager.Difficulty difficulty) {
        return JOptionPane.showConfirmDialog(this, "Change difficulty to: " + difficulty.name() + "\nRestart game?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == 0;
    }

    /**
     * Returns the current GUI scale (only affects the BoardView)
     *
     * @return int of the scale
     */
    public int getScale() {
        return boardView.getScale();
    }

    /**
     * Sets the scale of the GUI (only affects the BoardView)
     *
     * @param scale int of the new scale to be set
     */
    public void setScale(int scale) {
        boardView.setScale(scale);
    }

    /**
     * BoardView inner class that implements MouseListener.
     * The function of this class is to generate a user interface for displaying the board object.
     * It also has functionality for determining which cell of the board has been click by the mouse.
     */
    private class BoardView extends JPanel implements MouseListener {

        /**
         * int of the current scale, used for painting the cells
         */
        private int scale = 25;

        /**
         * Constructor of the BoardView, only adds a MouseListener
         */
        public BoardView() {
            this.addMouseListener(this);
        }

        /**
         * Overridden paint method that paints the board object.
         *
         * @param g JPanel Graphics object
         */
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D g2d = (Graphics2D) g;
            g.setFont(new Font("Arial", Font.PLAIN, scale / 2));
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            for (int row = 0; row < board.getBoardX(); row++) {
                for (int column = 0; column < board.getBoardY(); column++) {
                    Cell currentCell = board.getCell(row, column);
                    if (currentCell.isMine() && gm.isDebug()) {
                        drawMine(g, row, column);
                    } else if (currentCell.isMine() && currentCell.isRevealed()) {
                        drawMine(g, row, column);
                    } else if (currentCell.isRevealed()) {
                        drawRevealed(g, row, column);
                    } else if (currentCell.isFlagged()) {
                        drawFlagged(g, row, column);
                    } else {
                        drawUnrevealed(g, row, column);
                    }
                }
            }
        }

        /**
         * Overridden getPreferredSize method, layout mangers call this to know the preferred size of the panel
         *
         * @return Dimension object for a layout manager.
         */
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(board.getBoardX() * scale, board.getBoardY() * scale);
        }

        /**
         * Paint method template of a unrevealed cell
         *
         * @param g      Graphic object to draw to
         * @param row    int of which row to draw to
         * @param column int of which column to draw to
         */
        private void drawUnrevealed(Graphics g, int row, int column) {
            g.setColor(Color.GRAY);
            g.fillRect(row * scale, column * scale, scale, scale);
            g.setColor(Color.BLACK);
            g.drawRect(row * scale, column * scale, scale, scale);
        }

        /**
         * Paint method template of a flagged cell
         *
         * @param g      Graphic object to draw to
         * @param row    int of which row to draw to
         * @param column int of which column to draw to
         */
        private void drawFlagged(Graphics g, int row, int column) {
            g.setColor(Color.ORANGE);
            g.fillRect(row * scale, column * scale, scale, scale);
            g.setColor(Color.BLACK);
            g.drawRect(row * scale, column * scale, scale, scale);
        }

        /**
         * Paint method template of a mine (used for debugging and showing a losing mine)
         *
         * @param g      Graphic object to draw to
         * @param row    int of which row to draw to
         * @param column int of which column to draw to
         */
        private void drawMine(Graphics g, int row, int column) {
            g.setColor(Color.RED);
            g.fillRect(row * scale, column * scale, scale, scale);
            g.setColor(Color.BLACK);
            g.drawRect(row * scale, column * scale, scale, scale);
        }

        /**
         * Paint method template of a revealed cell
         * Will add label to the cell based on the number of adjacent mines
         *
         * @param g      Graphic object to draw to
         * @param row    int of which row to draw to
         * @param column int of which column to draw to
         */
        private void drawRevealed(Graphics g, int row, int column) {
            String label;
            if (board.getAdjacentMines(row, column) == 0) {
                label = "";
            } else {
                label = Integer.toString(board.getAdjacentMines(row, column));
            }
            g.drawString(label, ((row * scale) + scale / 2) - 3, ((column * scale) + scale / 2) + 5);
            g.setColor(Color.BLACK);
            g.drawRect(row * scale, column * scale, scale, scale);
        }

        /**
         * Determines if the given x and y are within the board limits
         *
         * @param x x value to check
         * @param y y value to check
         *
         * @return boolean value if x and y are within the board limits
         */
        private boolean isInBoard(int x, int y) {
            return x / scale <= board.getBoardX() && y / scale <= board.getBoardY();
        }

        /**
         * Unused interface method, does nothing
         *
         * @param e MouseEvent
         */
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        /**
         * Only method of the MouseListener interface that used
         * Determines which mouse button was press (left or right click) and determines the cell to affect
         *
         * @param e MouseEvent
         */
        @Override
        public void mousePressed(MouseEvent e) {
            if (isInBoard(e.getX(), e.getY())) {
                Cell c = board.getCell(e.getX() / scale, e.getY() / scale);
                //Right Click
                if (e.getButton() == 3) {
                    if (c.isFlagged() && !c.isRevealed()) {
                        c.setFlagged(false);
                        gm.nextTurn(c, e.getButton());
                    } else if (!c.isRevealed()) {
                        c.setFlagged(true);
                        gm.nextTurn(c, e.getButton());
                    }
                    //Left Click
                } else if (e.getButton() == 1) {
                    if (c.isFlagged()) {
                        return;
                    }
                    c.setRevealed(true);
                    c.setFlagged(false);
                    gm.nextTurn(c, e.getButton());
                }
            }
        }

        /**
         * Unused interface method, does nothing
         *
         * @param e MouseEvent
         */
        @Override
        public void mouseReleased(MouseEvent e) {

        }

        /**
         * Unused interface method, does nothing
         *
         * @param e MouseEvent
         */
        @Override
        public void mouseEntered(MouseEvent e) {

        }

        /**
         * Unused interface method, does nothing
         *
         * @param e MouseEvent
         */
        @Override
        public void mouseExited(MouseEvent e) {

        }

        /**
         * Accessor method for the scale of the BoardView
         *
         * @return int of the current scale
         */
        public int getScale() {
            return scale;
        }

        /**
         * Mutator method for the scale field for the BoardView
         *
         * @param scale
         */
        public void setScale(int scale) {
            this.scale = scale;
            updateGUI();
            pack();
        }
    }

    /**
     * OptionDialog's function is to provide a way of changing GameManger and Board settings via a GUI.
     */
    private class OptionsDialog extends JDialog {

        /**
         * Radio button group of the difficulty options
         */
        private ButtonGroup difficulty;

        /**
         * Constructor for the dialog
         */
        public OptionsDialog() {
            buildOptions();
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            pack();
            setVisible(true);
        }

        /**
         * Builds the options dialog with a GridBag layout and adds listeners when required.
         */
        private void buildOptions() {
            setTitle("Settings");
            //Container
            //Container pane = getContentPane();
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(3, 3, 3, 3);
            //Radio buttons
            JRadioButton begRadio = new JRadioButton("Beginner", difficultySelected(GameManager.Difficulty.BEGINNER));
            JRadioButton interRadio = new JRadioButton("Intermediate", difficultySelected(GameManager.Difficulty.MEDIUM));
            JRadioButton expertRadio = new JRadioButton("Expert", difficultySelected(GameManager.Difficulty.EXPERT));
            JRadioButton customRadio = new JRadioButton("Custom", difficultySelected(GameManager.Difficulty.CUSTOM));
            //Listeners
            begRadio.addActionListener(e -> gm.setDifficulty(GameManager.Difficulty.BEGINNER));
            interRadio.addActionListener(e -> gm.setDifficulty(GameManager.Difficulty.MEDIUM));
            expertRadio.addActionListener(e -> gm.setDifficulty(GameManager.Difficulty.EXPERT));
            customRadio.addActionListener(e -> gm.setDifficulty(GameManager.Difficulty.CUSTOM));
            //Button Group
            difficulty = new ButtonGroup();
            difficulty.add(begRadio);
            difficulty.add(interRadio);
            difficulty.add(expertRadio);
            difficulty.add(customRadio);
            //Custom Text Boxes
            NumberFormat intFormat = NumberFormat.getNumberInstance();
            intFormat.setMaximumFractionDigits(0);
            intFormat.setMaximumIntegerDigits(2);
            JFormattedTextField customX = new JFormattedTextField(intFormat);
            JFormattedTextField customY = new JFormattedTextField(intFormat);
            JFormattedTextField customMines = new JFormattedTextField(intFormat);
            //Scale Change
            JFormattedTextField scaleText = new JFormattedTextField(boardView.getScale());
            JButton scaleButton = new JButton("Set Scale");
            scaleButton.addActionListener(e -> boardView.setScale(Integer.valueOf(scaleText.getText())));
            //adding to pane
            c.anchor = GridBagConstraints.NORTHWEST;
            add(new JLabel("Difficulty"));
            c.gridx = 0;
            c.gridy = 1;
            add(begRadio, c);
            c.gridy++;
            add(interRadio, c);
            c.gridy++;
            add(expertRadio, c);
            c.gridy++;
            add(customRadio, c);
            c.gridy = 0;
            c.gridx++;
            add(new JLabel("Width"), c);
            c.gridx++;
            add(new JLabel("Height"), c);
            c.gridx++;
            add(new JLabel("Mines"), c);
            c.gridx = 1;
            c.gridy++;
            add(new JLabel("9"), c);
            c.gridx++;
            add(new JLabel("9"), c);
            c.gridx++;
            add(new JLabel("10"), c);
            c.gridy++;
            c.gridx = 1;
            add(new JLabel("16"), c);
            c.gridx++;
            add(new JLabel("16"), c);
            c.gridx++;
            add(new JLabel("40"), c);
            c.gridy++;
            c.gridx = 1;
            add(new JLabel("30"), c);
            c.gridx++;
            add(new JLabel("16"), c);
            c.gridx++;
            add(new JLabel("99"), c);
            c.gridy++;
            c.gridx = 1;
            add(customX, c);
            c.gridx++;
            add(customY, c);
            c.gridx++;
            add(customMines, c);
            c.gridx = 0;
            c.gridy = 5;
            add(new JLabel("Scale:"), c);
            c.gridx++;
            add(scaleText, c);
            c.gridx++;
            add(scaleButton, c);
        }

        /**
         * Compares the current selected difficulty to the difficulty given
         *
         * @param d Difficulty enum to compare
         *
         * @return boolean based on whether the current difficulty matches the parameter
         */
        private boolean difficultySelected(GameManager.Difficulty d) {
            return gm.getDifficulty() == d;
        }
    }

    /**
     * StatusView is a JPanel that displays information about the current game i.e. the number of mines left
     */
    private class StatusView extends JPanel {

        /**
         * JLabel showing the number of mines left
         */
        private JLabel minesLeftLabel;

        /**
         * Constructor for the StatusView, only calls the build method
         */
        public StatusView() {
            build();
        }

        /**
         * Builds the StatusView elements
         */
        private void build() {
            //Labels
            minesLeftLabel = new JLabel(Integer.toString(board.minesLeft()));
            add(new JLabel("Mines Left: "));
            add(minesLeftLabel);
        }

        /**
         * Updates the labels
         */
        public void update() {
            minesLeftLabel.setText(Integer.toString(board.minesLeft()));
        }
    }

    /**
     * InfoView is a JPanel that displays user stats i.e. session and lifetime win and loss record.
     */
    private class InfoView extends JPanel {

        /**
         * Splitting lifetime and session to their own panels
         */
        private LifetimePanel lifeTimePanel;
        private SessionPanel sessionPanel;

        /**
         * Constructor for the InfoView, only calls build
         */
        public InfoView() {
            build();
        }

        /**
         * Builds the InfoView elements
         */
        private void build() {
            //Layout
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(3, 3, 3, 3);
            c.anchor = GridBagConstraints.NORTHWEST;
            c.weightx = 0.1;
            c.weighty = 0.1;
            c.gridx = 0;
            c.gridy = 0;
            //Labels
            c.gridy++;
            add(lifeTimePanel = new LifetimePanel(), c);
            c.gridy++;
            add(sessionPanel = new SessionPanel(), c);

        }

        /**
         * Calls the update method for both Lifetime and Session panels
         */
        public void update() {
            lifeTimePanel.update();
            sessionPanel.update();
        }

        /**
         * JPanel showing the lifetime stats i.e. wins and losses
         */
        private class LifetimePanel extends JPanel {

            /**
             * Wins and losses labels
             */
            private JLabel winsLabel;
            private JLabel lossesLabel;

            /**
             * Constructor for the LifetimePanel, only calls build
             */
            public LifetimePanel() {
                build();
            }

            /**
             * Builds the LifetimePanel elements
             */
            private void build() {
                setBorder(BorderFactory.createTitledBorder("Lifetime"));
                setLayout(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();
                c.insets = new Insets(3, 3, 3, 3);
                c.gridx = 0;
                c.gridy = 0;
                add(new JLabel("Wins: "), c);
                c.gridx++;
                add(winsLabel = new JLabel(Integer.toString(GameManager.getLifetimeWins())), c);
                c.gridx = 0;
                c.gridy++;
                add(new JLabel("Losses: "), c);
                c.gridx++;
                add(lossesLabel = new JLabel((Integer.toString(GameManager.getLifetimeLosses()))), c);
            }

            /**
             * Updates the win and loss labels
             */
            public void update() {
                winsLabel.setText(Integer.toString(GameManager.getLifetimeWins()));
                lossesLabel.setText(Integer.toString(GameManager.getLifetimeLosses()));
            }
        }

        /**
         * JPanel showing the session stats i.e. wins and losses
         */
        private class SessionPanel extends JPanel {

            /**
             * Wins and losses labels
             */
            private JLabel winsLabel;
            private JLabel lossesLabel;

            /**
             * Constructor for the SessionPanel, only calls build
             */
            public SessionPanel() {
                build();
            }

            /**
             * Builds the SessionPanel elements
             */
            private void build() {
                setBorder(BorderFactory.createTitledBorder("Session"));
                setLayout(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();
                c.insets = new Insets(3, 3, 3, 3);
                c.gridy = 0;
                c.gridx = 0;
                add(new JLabel("Wins: "), c);
                c.gridx++;
                add(winsLabel = new JLabel(Integer.toString(gm.getSessionWins())), c);
                c.gridx = 0;
                c.gridy++;
                add(new JLabel(("Losses: ")), c);
                c.gridx++;
                add(lossesLabel = new JLabel(Integer.toString(gm.getSessionLosses())), c);
            }

            /**
             * Updates the win and loss labels
             */
            public void update() {
                winsLabel.setText(Integer.toString(gm.getSessionWins()));
                lossesLabel.setText(Integer.toString(gm.getSessionLosses()));
            }
        }
    }
}
