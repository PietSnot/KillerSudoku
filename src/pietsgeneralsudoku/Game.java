/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pietsgeneralsudoku;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Sylvia
 */
public class Game {
    SudokuConstants.GameStatus status;
    
    int maxdigit;
    int squareSize;
    
    Map<NumberSum, BitSet> groupBitSets;
    
    boolean gameSolved;
    List<KillerCell> selection;
    KillerCell currentCell;
    
    // for buttonpanel
    JButton buttonSetUp, buttonUserSolving, buttonGiveHint, buttonGiveSolution;
    ActionListener alSetUp, alUserSolving, alGiveHint, alGiveSolution;
    
    // for KillerCellpanel
    KillerCell[][] bord;
    List<KillerGroup> groups;
    MouseAdapter maCellSetUp, maCellUserSolving;
    KeyListener klCellSetUp, klCellUserSolving;
    
    // for messagePanel
    JLabel messageLabel;
    
    // for dialog
    JDialog dialog;
    JPanel killerCellPanel;
    
    // for menubar
    Action actionSaveGame;
    JFileChooser fileChooser;
    JMenuItem menuItemSaveGame;
    
    //****************************************************************
    // constructors
    //****************************************************************
    public Game(JFrame owner, int maxdigit) {
        initialize(owner, maxdigit, true);
    }
    
    public Game(JFrame owner, File f) {
        initialize(owner, f);
    }
    
    //***************************************************************
    // gui stuff
    //***************************************************************
    
    private void createGui(JFrame owner) {
        JPanel buttonPanel = createButtonPanel();
        killerCellPanel = createKillerCellPanel();
        JPanel messagePanel = createMessagePanel();
        JMenuBar menuBar = createMenuBar();
        menuBar.setBackground(Color.LIGHT_GRAY);
        
        dialog = new JDialog(owner, "----- KillerSudoku ------");
        Container container = dialog.getContentPane();
        container.add(killerCellPanel, BorderLayout.PAGE_START);
        container.add(buttonPanel, BorderLayout.CENTER);
        container.add(messagePanel, BorderLayout.PAGE_END);
        
        dialog.setJMenuBar(menuBar);
        dialog.pack();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);
    }
    
    //==================================================
    private void createBord() {
        bord = new KillerCell[maxdigit][maxdigit];
        for (int r = 0; r < maxdigit; r++) {
            for (int c = 0; c < maxdigit; c++) {
                int rh = r / squareSize * squareSize+ c / squareSize;
                bord[r][c] = new KillerCell(r, c, maxdigit , SudokuConstants.CellSize.NORMAL.size);
            }
        }
    }
    
    //==================================================
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(SudokuConstants.Colors.BUTTONPANEL.color);
        int border = SudokuConstants.BorderSize.BUTTOPANEL_BORDER.width;
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(border, border, border, border));
        int gap = SudokuConstants.BorderSize.BUTTONPANEL_GAP.width;
        buttonPanel.setLayout(new GridLayout(0, 2, gap, gap));
        
        buttonUserSolving = new JButton("User Solving");
        buttonGiveHint = new JButton("Hint");
        buttonGiveSolution = new JButton("Solution");
        
        buttonPanel.add(new JLabel());
        buttonPanel.add(buttonUserSolving);
        buttonPanel.add(buttonGiveHint);
        buttonPanel.add(buttonGiveSolution);
        
        return buttonPanel;
    }
    
    //==================================================
    private JPanel createKillerCellPanel() {
        JPanel killerPanel = new JPanel();
        killerPanel.setBackground(SudokuConstants.Colors.KILLERCELLPANEL.color);
        int border = SudokuConstants.BorderSize.KILLERPANEL_BORDER.width;
        killerPanel.setBorder(BorderFactory.createEmptyBorder(border, border, border, border));
        border = SudokuConstants.BorderSize.KILLERPANEL_SQUAREBORDER.width;
        killerPanel.setLayout(new GridLayout(squareSize, squareSize, border, border));
        border = SudokuConstants.BorderSize.KILLERPANEL_CELLBORDER.width;
        JPanel[] squares = new JPanel[bord.length];
        for (int i = 0; i < bord.length; i++) {
            squares[i] = new JPanel(new GridLayout(squareSize, squareSize, border, border));
            squares[i].setBackground(SudokuConstants.Colors.KILLERCELLPANEL.color);
            killerPanel.add(squares[i]);
        }
        
        for (KillerCell[] row: bord) {
            for (KillerCell cell: row) {
                squares[cell.rh].add(cell);
            }
        }
        return killerPanel;
    }
    
    //==================================================
    private JPanel createMessagePanel() {
        JPanel messagePanel = new JPanel();
        messagePanel.setBackground(SudokuConstants.Colors.MESSAGEPANEL.color);
        messagePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        messageLabel = new JLabel(SudokuConstants.Message.SETUP.message);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        messagePanel.add(messageLabel);
        return messagePanel;
    }
    
    //==================================================
    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Game");
        menuItemSaveGame = new JMenuItem("save Game");
        menu.add(menuItemSaveGame);
        bar.add(menu);
        return bar;
    }
    
    //==================================================
    private void setButtons(boolean user, boolean hint, boolean solution) {
        buttonUserSolving.setEnabled(user);
        buttonGiveHint.setEnabled(hint);
        buttonGiveSolution.setEnabled(solution);
    }
    
    //==================================================
    private void setKillerCellColors(SudokuConstants.Colors color) {
        for (KillerCell[] row: bord) {
            for (KillerCell cell: row) {
                cell.setBackground(color.color);
            }
        }
    }
    
    //***************************************************************
    // public methods
    //***************************************************************
    
    public int getSize() {
        return maxdigit;
    }
    
    //***************************************************************
    // private methods
    //***************************************************************
    
    private void initialize(JFrame owner, int maxdigit, boolean setup) {
        squareSize = (int) (Math.sqrt(maxdigit) + .1);
        if (squareSize * squareSize != maxdigit) 
            throw new IllegalArgumentException("illegal size!");
        this.maxdigit = maxdigit;
        groups = new ArrayList<>();
        selection = new ArrayList<>();
        createBord();
        createGui(owner);
        createListenersAndAdapters();
        
        groupBitSets = Utils.makeBitSets(maxdigit);
       
        setKillerCellColors(SudokuConstants.Colors.NORMAL);
        addActionListenersToButtons(setup);
        addActionsToMenu();
        if (setup) { 
            changeStatusTo(SudokuConstants.GameStatus.SETUP);
            dialog.setVisible(true);
        }
    }
    
//==================================================================
    private void initialize(JFrame owner, File f) {
        LoadedData loadedData = Utils.loadFile2(f);
        if (loadedData == null) return;
        maxdigit = loadedData.getSize();
        initialize(owner, maxdigit, false);
        addGroups(loadedData.getListOfGroups());
        changeStatusTo(loadedData.getStatus());
        dialog.setVisible(true);
    }
    
    //==================================================
    private void setMessage(SudokuConstants.Message message) {
        messageLabel.setText(message.message);
    }
    
    //==================================================
    private void setMessage(String message) {
        messageLabel.setText(message);
    }
    
    //==================================================================
    private void addGroups(List<GroupTotalAndCells> list) {
        // tuple contains the grouptotal and a list of the accompanying cells
        int nr = 0;
        for (GroupTotalAndCells group: list) {
            List<KillerCell> temp = replaceCellsByBordCells(group.getListOfCellEssences());
            KillerGroup newgroup = KillerGroup.createGroup(nr, temp, maxdigit, group.getGrouptotal());
            groups.add(newgroup);
            nr++;
        }
    }
    
    //==================================================================
    /**
     * Note we have a list of Killercells, that we must turn into
     * groups. However, these cells are not the cells in our KillerPanel,
     * so we must replace each cell by the relevant KillerPanel cell,
     * and we have these in our 'bord' variable!
     * @param list
     * @return 
     */
    private List<KillerCell> replaceCellsByBordCells(List<CellEssence> list) {
        List<KillerCell> result = new ArrayList<>();
        for (CellEssence cell: list) {
            bord[cell.row][cell.col].value = cell.value;
            bord[cell.row][cell.col].setText(cell.text);
            result.add(bord[cell.row][cell.col]);
        }
        return result;
    }
    
    //==================================================
    private void addMouseListenersToKillerCellPanel(MouseListener m) {
        for (KillerCell[] row: bord) {
            for (KillerCell cell: row) {
                cell.addMouseListener(m);
            }
        }
    }
    
    //==================================================
    private void addKeyListenersToKillerCellPanel(KeyListener k) {
        for (KillerCell[] row: bord) {
            for (KillerCell cell: row) {
                cell.addKeyListener(k);
            }
        }
    }
    
    //==================================================
    private void addActionListenersToButtons(boolean setup) {
        buttonUserSolving.addActionListener(alUserSolving);
         buttonGiveHint.addActionListener(alGiveHint);
        buttonGiveSolution.addActionListener(alGiveSolution);
        if (setup) {
            setMessage(SudokuConstants.Message.SETUP);
        }
       
    }
    
    //==================================================
    private void createListenersAndAdapters() {
        maCellSetUp = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent m) {
                if (status != SudokuConstants.GameStatus.SETUP) return;
                if (m.getButton() == MouseEvent.BUTTON1) {
                    processSetUpMouseButtonLeft(m);
                }
                else {
                    processSetUpMouseButtonRight(m);
                }
            }
        };
        
        maCellUserSolving = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent m) {
                if (m.getButton() == MouseEvent.BUTTON1) {
                    processUserSolvingMouseButtonLeft(m);
                }
                else {
                    processUserSolvingMouseButtonRight(m);
                }
            }
        };
        
        alSetUp = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                if (status == SudokuConstants.GameStatus.SETUP) return;
                changeStatusTo(SudokuConstants.GameStatus.SETUP);
            }
        };
        
        alUserSolving = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (status != SudokuConstants.GameStatus.SETUP) return;
                if (!checkForAllGroups()) {
                    setMessage(SudokuConstants.Message.NOTENOUGHGROUPS);
                    return;
                }
                changeStatusTo(SudokuConstants.GameStatus.USERSOLVING);
                setMessage(SudokuConstants.Message.SUCCESS);
            }
        };
        
        alGiveHint = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (status != SudokuConstants.GameStatus.USERSOLVING) return;
                if (!gameSolved) return;
                currentCell.setText("" + currentCell.value);
            }
        };
        
        alGiveSolution = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (status != SudokuConstants.GameStatus.USERSOLVING) return;
                if (!gameSolved) return;
                for (KillerCell[] row: bord) {
                    for (KillerCell cell: row) {
                        cell.setText("" + cell.value);
                    }
                }
                changeStatusTo(SudokuConstants.GameStatus.FINISHED);
            }
        };
        
        klCellUserSolving = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent k) {
                if (status != SudokuConstants.GameStatus.USERSOLVING) return;
                switch (k.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_KP_LEFT:
                        setCurrentCell(SudokuConstants.Direction.GOLEFT);
                        break;
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_KP_RIGHT:
                        setCurrentCell(SudokuConstants.Direction.GORIGHT);
                        break;
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_KP_UP:
                        setCurrentCell(SudokuConstants.Direction.GOUP);
                        break;
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_KP_DOWN:
                        setCurrentCell(SudokuConstants.Direction.GODOWN);
                        break;
                    case KeyEvent.VK_DELETE:
                        currentCell.setText("");
                    default: 
                        break;
                }    
            }
            
            @Override
            public void keyReleased(KeyEvent k) {
                if (status != SudokuConstants.GameStatus.USERSOLVING) return;
                int c = k.getKeyChar() - '0';
                if (c >= 1 && c <= maxdigit ) {
                    currentCell.setText("" + c);
                }
            }
        };
        
        actionSaveGame = new AbstractAction("save Game") {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGame();
            }
        };
        
        fileChooser = new JFileChooser();
    }
    
    //==================================================================
    private void addActionsToMenu() {
        menuItemSaveGame.setAction(actionSaveGame);
    }
    
    //==================================================================
    private void saveGame() {
        System.out.println("In method \"saveGame\"");
//        if (status != SudokuConstants.GameStatus.USERSOLVING) {
//            setMessage(SudokuConstants.Message.STATUSMUSTBEUSERSOLVING);
//            return;
//        }
        int x = fileChooser.showSaveDialog(dialog);
        if (x != JFileChooser.APPROVE_OPTION) return;
        File f = fileChooser.getSelectedFile();
        saveFile(f);
    }
    
    //==================================================================
    private void saveFile(File f) {
        try (PrintWriter ps = new PrintWriter(f)) {
            ps.println("KillerSudoku -version 1.0");
            ps.println("size " + maxdigit);
            ps.println("status: " + status);
            ps.println("// cell data: row, col, rh, value, userfilled value");
            for (KillerGroup group: groups) {
                ps.println("startgroup: " + group.groupTotal);
                for (KillerCell cell: group.getCells()) {
                    ps.printf("    cell: %d, %d, %d, %d, %s %n", 
                            cell.row, cell.col, cell.rh, cell.value, cell.getText());
                }
                ps.println("endgroup");
            }
        }
        catch (FileNotFoundException e) {
            setMessage(SudokuConstants.Message.UNABLETOSAVE);
        }
    }
    
    //==================================================================
    private void processSetUpMouseButtonLeft(MouseEvent m) {
        KillerCell cell = (KillerCell) m.getSource();
        if (cell.group != null) return;
        if (selection.contains(cell)) {
            cell.setBackground(SudokuConstants.Colors.NORMAL.color);
            selection.remove(cell);
        }
        else {
            cell.setBackground(SudokuConstants.Colors.SELECTED.color);
            selection.add(cell);
        }
        setMessage(SudokuConstants.Message.USERSOLVING);
    }
    
    //==================================================
    private void processSetUpMouseButtonRight(MouseEvent m) {
        KillerCell cell = (KillerCell) m.getSource();
        if (cell.group != null) {
            if (userWantsToDeleteGroup()) {
                deleteGroup(cell.group);
            }
            return;
        }
        if (selection.isEmpty()) return;
        if (!KillerGroup.isValidGroup(selection, maxdigit)) {
            setMessage(SudokuConstants.Message.NOTAVALIDGROUP);
            return;
        }
        int total = getGroupTotal();
        if (total < 0) {
            setMessage(SudokuConstants.Message.INVALIDGROUPTOTAL);
            return;
        }
        NumberSum ns = new NumberSum(selection.size(), total);
        if (!groupBitSets.containsKey(ns)) {
            setMessage(SudokuConstants.Message.INVALIDGROUPTOTAL);
            return;
        }
        groups.add(KillerGroup.createGroup(groups.size(), selection, maxdigit, total));
        selection.clear();
        setMessage(SudokuConstants.Message.USERSOLVING);
    }
    
    //==================================================
    private void processUserSolvingMouseButtonLeft(MouseEvent m) {
        setCurrentCell((KillerCell) m.getSource());
    }
    
    //==================================================
    private boolean userWantsToDeleteGroup() {
        int x = JOptionPane.showConfirmDialog(
                null, 
                "Do you want to remove this group?", 
                "Remove Group?", 
                JOptionPane.YES_NO_CANCEL_OPTION)
        ;
        return x == JOptionPane.YES_OPTION;
    }
    
    //==================================================
    private void deleteGroup(KillerGroup group) {
        for (KillerCell cell: group.getCells()) {
            cell.initialize();
        }
        int nr = group.nr;
        Iterator<KillerGroup> iter = groups.iterator();
        while (iter.hasNext()) {
            KillerGroup p = iter.next();
            if (p.nr == nr) {
                iter.remove();
                break;
            }
        }
    }
    
    //==================================================
    private void processUserSolvingMouseButtonRight(MouseEvent m) {
        System.out.println("in processUserSolvingMouseButtonRight");
    }
    
    //==================================================
    private void changeStatusTo(SudokuConstants.GameStatus status) {
        switch(status) {
            case SETUP: 
                changeStatusToSetUp();
                break;
            case USERSOLVING:
                changeStatusToUserSolving();
                break;
            default:
                changeStatusToFinished();
                break;
        }
    };
    
    //==================================================
    private void changeStatusToSetUp() {
        status = SudokuConstants.GameStatus.SETUP;
        for (KillerCell[] row: bord) {
            for (KillerCell cell: row) {
                cell.addMouseListener(maCellSetUp);
            }
        }
        setButtons(true, false, false);
        setMessage(SudokuConstants.Message.USERSOLVING);
    }
    
    //==================================================
    private int getGroupTotal() {
        String sum = JOptionPane.showInputDialog(null, "Please enter groupsum...");
        try {
            int answer = Integer.parseInt(sum);
            return answer;
        }
        catch (IllegalArgumentException e) {
            return -1;
        }
    }
    
    //==================================================
    private void changeStatusToUserSolving() {
        if (!checkForAllGroups()) {
            setMessage(SudokuConstants.Message.NOTENOUGHGROUPS);
            return;
        }
        if (status == SudokuConstants.GameStatus.SETUP) {
            removeSetUpListeners();
        }
        status = SudokuConstants.GameStatus.USERSOLVING;
        addMouseListenersToKillerCellPanel(maCellUserSolving);
        addKeyListenersToKillerCellPanel(klCellUserSolving);
        setCurrentCell(bord[0][0]);
        setButtons(false, true, true);
        gameSolved = false;
        new Solver().execute();
    }
    
    //==================================================
    private void changeStatusToFinished() {
        status = SudokuConstants.GameStatus.FINISHED;
        setButtons(false, false, false);
        setMessage(SudokuConstants.Message.SETUP);
    }
    
    //==================================================
    private boolean checkForAllGroups() {
        for (KillerCell[] row: bord) {
            for (KillerCell cell: row) {
                if (cell.group == null) return false;
            }
        }
        return true;
    }
    
    //==================================================
    private void removeSetUpListeners() {
        for (KillerCell[] row: bord) {
            for (KillerCell cell: row) {
                cell.removeMouseListener(maCellSetUp);
            }
        }
    }
    
    //==================================================
    private void setCurrentCell(KillerCell cell) {
        if (cell.equals(currentCell)) return;
        if (currentCell != null) {
            currentCell.setBackground(SudokuConstants.Colors.GROUPED);
        }
        currentCell = cell;
        currentCell.setBackground(SudokuConstants.Colors.CURRENTCELL);
        currentCell.requestFocus();
    }
    
    //==================================================
    private void setCurrentCell(int row, int col) {
        if (row < 0 || row >= maxdigit || col < 0 || col >= maxdigit) return;
        setCurrentCell(bord[row][col]);
    }
    
    //==================================================
    private void setCurrentCell(SudokuConstants.Direction direction) {
        if (currentCell == null) {
            setCurrentCell(0, 0);
            return;
        }
        int row = currentCell.row + direction.deltax;
        int col = currentCell.col + direction.deltay;
        setCurrentCell(row, col);
    }
    
    //*****************************************************************
    // other classes
    //*****************************************************************
    
    class Solver extends SwingWorker<Boolean, Object> {
        
        boolean goingforward = true;
        BitSet[] rowSet = new BitSet[maxdigit];
        BitSet[] colSet = new BitSet[maxdigit];
        BitSet[] rhSet = new BitSet[maxdigit];
        BitSet[] groupSet = new BitSet[groups.size()];
        int index;  // index in the list of cells to be solved
        int cellsToSolve;
        PrintWriter pw;
        
        Solver() {
            String filename = "d:/ks_testoutput.txt";
            try {
                pw = new PrintWriter(filename);
            }
            catch (FileNotFoundException e) {
                System.out.println("Kan uitvoerbestand niet openen...");
                return;
            }
            for (int index = 0; index < maxdigit; index++) {
                rowSet[index] = new BitSet(maxdigit + 1);
                rowSet[index].flip(1, maxdigit + 1);
                colSet[index] = new BitSet(maxdigit + 1);
                colSet[index].flip(1, maxdigit + 1);
                rhSet[index] = new BitSet(maxdigit + 1);
                rhSet[index].flip(1, maxdigit + 1);
            }
            for (int index = 0; index < groups.size(); index++) {
                groupSet[index] = new BitSet(maxdigit + 1);
                groupSet[index].flip(1, maxdigit + 1);
            }
            a("initialized bitsets", true);
        } // end of constructor    
      
        @Override
        protected Boolean doInBackground() throws Exception {
            System.out.println("in 'doinbackground'");
            try {
                pw = new PrintWriter("d:\\ks_testoutput.txt");
            }
            catch (Exception e) {
                System.out.println("Can't output file");
            }
            System.out.println("Gaan we....");
            cellsToSolve = maxdigit * maxdigit;
            // first solving all groups with size 1
            
            for (KillerCell[] row: bord) {
                for (KillerCell cell: row) {
                    if (cell.group.cells.size() == 1) {
                        cell.value = cell.group.groupTotal;
                        rowSet[cell.row].clear(cell.value);
                        colSet[cell.col].clear(cell.value);
                        rhSet[cell.rh].clear(cell.value);
                        groupSet[cell.group.nr].clear(cell.value);
                        a(cell.toString() + "\nis gevuld, had maar één mogelijke waarde", true);
                        cellsToSolve--;
                        a("cells remaining: " + cellsToSolve, true);
                    }
                }
            }

            // with a little bit of luck are we done now...
            if (cellsToSolve == 0) {
                a("all cells solved", true);
                pw.close();
                return Boolean.TRUE; // should call the 'done' method
            }  
            
            // other smart ways are no doubt possible, but that's for later
            List<KillerCell> cellsToBeSolved = transformBordToList();
            index = 0;
            
            while (index >= 0 && index < cellsToBeSolved.size()) {
                KillerCell cell = cellsToBeSolved.get(index);
                if (goingforward) {
                    a("going forward with cell " + cell, true);
                    handleForward(cell);
                }
                else {
                    a("going backward with cell " + cell, true);
                    handleBackward(cell);
                }
            }
            boolean answer = false;
            if (index == cellsToBeSolved.size()) {
                a("solved it. End", true);
                answer = true;
            }
            else {
                a("No solution found....", true);
            }
            if (pw != null) pw.close();
            return answer;
        }
        
        //==============================================================
        @Override
        public void done() {
            boolean fout = false;
            try {
                gameSolved = get();
            } 
            catch (InterruptedException | ExecutionException e) {
                fout = true;
                gameSolved = false;
            }
            String s = gameSolved ? "Computer solved it" : 
                       fout ? "Error during solving!!!" :
                       "No solution found";
            setMessage(s);
        }
        
        //==============================================================
        private List<KillerCell> transformBordToList() {
            List<KillerCell> list = new ArrayList<>();
            for (KillerCell[] row: bord) {
                for (KillerCell cell: row) {
                    if (cell.value == 0) list.add(cell);
                }
            }
            return list;
        }
        
        //==============================================================
        private void handleForward(KillerCell cell) {
            cell.possibleValues = new BitSet(maxdigit + 1);
            cell.possibleValues.flip(1, maxdigit + 1);
            cell.possibleValues.and(rowSet[cell.row]);
            cell.possibleValues.and(colSet[cell.col]);
            cell.possibleValues.and(rhSet[cell.rh]);
            cell.possibleValues.and(groupSet[cell.group.nr]);
            NumberSum ns = cell.group.getNumberSum();
            cell.possibleValues.and(groupBitSets.get(ns));
            a("possible values: " + Utils.createList(cell.possibleValues), false);
            int trydigit = cell.possibleValues.nextSetBit(1);
            if (trydigit == -1) {
                // no values possible, so we do some backtracking
                a("no possible values, going backward", false);
                index--;
                goingforward = false;
            }
            else {
                a("trying " + trydigit + "; going forward", false);
                cell.value = trydigit;
                rowSet[cell.row].clear(trydigit);
                colSet[cell.col].clear(trydigit);
                rhSet[cell.rh].clear(trydigit);
                groupSet[cell.group.nr].clear(trydigit);
                index++;
                goingforward = true;
            }
        }
        
        //==============================================================
        private void handleBackward(KillerCell cell) {
            // so we're back with the backtracking. Let's try
            // another value
            // restore the number tried
            rowSet[cell.row].set(cell.value);
            colSet[cell.col].set(cell.value);
            rhSet[cell.rh].set(cell.value);
            groupSet[cell.group.nr].set(cell.value);
            int trydigit = cell.possibleValues.nextSetBit(cell.value + 1);
            if (trydigit == -1) {
                // no digits left. so we have to backtrack yet again
                a("no possible values left, going backward", false);
                cell.value = 0;
                index--;
                goingforward = false;
            }
            else {
                a("trying cell" + cell + " value = " + trydigit, false);
                cell.value = trydigit;
                rowSet[cell.row].clear(trydigit);
                colSet[cell.col].clear(trydigit);
                rhSet[cell.rh].clear(trydigit);
                groupSet[cell.group.nr].clear(trydigit);
                index++;
                goingforward = true;
            }
        }
        
        private void a(String s, boolean stars) {
//            if (stars) pw.println("*********************");
//            pw.println(s);
//            if (stars) pw.println("*********************\n");
        }
    }  // end of class Solver
}
