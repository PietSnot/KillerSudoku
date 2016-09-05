/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pietsgeneralsudoku;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

/**
 *
 * @author Sylvia
 */
public class KillerGroup implements Group<KillerCell> {
    
    int nr;
    List<KillerCell> cells;
    int groupTotal;
    static Map<String, Border> borderMap = partialBorders(SudokuConstants.Colors.GROUPBORDER.color, 2);
    
    //================================================================
    //  private constructor
    //================================================================
    private KillerGroup(int nr, List<KillerCell> cells, int total) {
        this.nr = nr;
        this.cells = new ArrayList<>(cells);
        groupTotal = total;
    }
    
    //================================================================
    // factory
    //================================================================
    public static KillerGroup createGroup(int nr, List<KillerCell> cells, int gamesize, int grouptotal) {
        if (!isValidGroup(cells, gamesize)) return null;
        KillerGroup group = new KillerGroup(nr, cells, grouptotal);
        for (KillerCell cell: cells) {
            cell.setGroup(group);
            cell.setBackground(SudokuConstants.Colors.GROUPED.color);
        }
        KillerCell cell = group.getSmallestCell();
        cell.labelGrouptotal.setText("" + grouptotal);
        setGroupBorder(group);
        return group;
    }
    
    //================================================================
    public static boolean isValidGroup(List<KillerCell> cells, int gamesize) {
        if (cells.size() > gamesize) {
            return false;
        }
        if (!Utils.isConnected(cells)) return false;
        Set<KillerCell> set = new HashSet<>(cells);
        if (set.size() != cells.size()) return false;
        for (KillerCell cell: cells) if (cell.group != null) return false;
        return true;
    }

    //================================================================
    @Override
    public List<KillerCell> getCells() {
        return new ArrayList<>(cells);
    }

    //================================================================
    @Override
    public int getNr() {
        return nr;
    }

    //================================================================
    @Override
    public boolean isNumberPossible(int nr) {
        for (KillerCell cell: cells) if (cell.value == nr) return false;
        if (nr + getCurrentTotal() > groupTotal ) return false;
        return true;
    }

    //================================================================
    public int getCurrentTotal() {
        int sum = 0;
        for (KillerCell cell: cells) sum += cell.value;
        return sum;
    }
    
    //================================================================
    public int getRemainingTotal() {
        return groupTotal - getCurrentTotal();
    }
    
    //================================================================
    public int getNumberOfEmptyCells() {
        int result = 0;
        for (KillerCell cell: cells) if (cell.value == 0) result++;
        return result;
    }
    
    //================================================================
    public NumberSum getNumberSum() {
        return new NumberSum(getNumberOfEmptyCells(), getRemainingTotal());
    }

    //================================================================
    public List<KillerCell> getEmptyCells() {
        List<KillerCell> list = new ArrayList<>();
        for (KillerCell cell: cells) if (cell.value == 0) list.add(cell);
        return list;
    }

    //================================================================
    public KillerCell getSmallestCell() {
        Collections.sort(cells);
        return cells.get(0);
    }

    //================================================================
    private static void setGroupBorder(KillerGroup group) {
        List<KillerCell> cells = group.getCells();
        for (KillerCell cell: cells) {
            String borders = "";
            if (!cells.contains(new KillerCell(cell.row - 1, cell.col))) borders += "t";
            if (!cells.contains(new KillerCell(cell.row + 1, cell.col))) borders += "b";
            if (!cells.contains(new KillerCell(cell.row, cell.col - 1))) borders += "l";
            if (!cells.contains(new KillerCell(cell.row, cell.col + 1))) borders += "r";
            cell.setBorder(borderMap.get(getKey(borders)));
        }
    }
    
    //================================================================
    private static Map<String, Border> partialBorders(Color color, int pixels) {
        Map<String, Border> result = new HashMap<>();
        result.put("", BorderFactory.createEmptyBorder());
        result.put("L", new MatteBorder(0, pixels, 0, 0, color));
        result.put("T", new MatteBorder(pixels, 0, 0, 0, color));
        result.put("R", new MatteBorder(0, 0, 0, pixels, color));
        result.put("B", new MatteBorder(0, 0, pixels, 0, color));
        result.put("TL", new MatteBorder(pixels, pixels, 0, 0, color));
        result.put("TB", new MatteBorder(pixels, 0, pixels, 0, color));
        result.put("TR", new MatteBorder(pixels, 0, 0, pixels, color));
        result.put("LB", new MatteBorder(0, pixels, pixels, 0, color));
        result.put("LR", new MatteBorder(0, pixels, 0, pixels, color));
        result.put("BR", new MatteBorder(0, 0, pixels, pixels, color));
        result.put("TLB", new MatteBorder(pixels, pixels, pixels, 0, color));
        result.put("TLR", new MatteBorder(pixels, pixels, 0, pixels, color));
        result.put("TBR", new MatteBorder(pixels, 0, pixels, pixels, color));
        result.put("LBR", new MatteBorder(0, pixels, pixels, pixels, color));
        result.put("TLBR", new MatteBorder(pixels, pixels, pixels, pixels, color));
        return result;
    }
    
    //================================================================
    private static String getKey(String sides) {
        String s = sides.toUpperCase();
        String result = "";
        if (s.contains("T")) result += "T";
        if (s.contains("L")) result += "L";
        if (s.contains("B")) result += "B";
        if (s.contains("R")) result += "R";
        return result;
    }
    
    //================================================================
    // einde class
}
