/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pietsgeneralsudoku;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.BitSet;
import javax.swing.JLabel;

/**
 *
 * @author Sylvia
 */
public class KillerCell extends JLabel implements Comparable<KillerCell> {
    int row, col, rh;
    int value;
    int maxdigit;
    BitSet possibleValues;
    KillerGroup group;
    int index;
    JLabel labelGrouptotal;
    
    public KillerCell(int row, int col, int maxdigit, int size) {
        super("", JLabel.CENTER);
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, (int) (size * .6));
        Font totalFont = new Font(Font.SANS_SERIF, Font.PLAIN, size / 5);
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.setFont(font);
        this.row = row;
        this.col = col;
        this.maxdigit = maxdigit;
        int sqr = (int) (Math.sqrt(maxdigit) + .1);
        rh = row / sqr * sqr + col / sqr;
        this.setPreferredSize(new Dimension(size, size));
        this.setOpaque(true);
        labelGrouptotal = new JLabel("23", JLabel.LEFT);
        labelGrouptotal.setFont(totalFont);
        this.add(labelGrouptotal);
        initialize();
    }
    
    
    /**
     * only for comparison purposes
     * @param row
     * @param col 
     */
    public KillerCell(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public void initialize() {
        group = null;
        possibleValues = null;
        labelGrouptotal.setText("");
        this.setText("");
        this.setBackground(SudokuConstants.Colors.NORMAL);
        this.setBorder(null);
    }
    
    public void setGroup(KillerGroup group) {
        this.group = group;
    }
    
    public void setBackground(SudokuConstants.Colors col) {
        super.setBackground(col.color);
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (this == other) return true;
        if (!(other instanceof KillerCell)) return false;
        KillerCell p = (KillerCell) other;
        return row == p.row && col == p.col;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.row;
        hash = 17 * hash + this.col;
        return hash;
    }
    
    public int compareTo(KillerCell other) {
        return row < other.row ? -1 :
               row > other.row ? 1 :
               col - other.col
        ;
    }
    
    public String toString() {
        String s = String.format("(%d, %d) val = %d posvals: %s", row, col, value, Utils.createList(possibleValues));
        return s;
    }
}
