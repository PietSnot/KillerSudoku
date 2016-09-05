/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pietsgeneralsudoku;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sylvia
 */
public class LoadedData {
    private int size;
    private SudokuConstants.GameStatus status;
    private List<GroupTotalAndCells> list = new ArrayList<>();
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setStatus(SudokuConstants.GameStatus status) {
        this.status = status;
    }
    
    public SudokuConstants.GameStatus getStatus() {
        return status;
    }
    
    public void addGroupTotalAndCells(int grouptotal, List<CellEssence> cells) {
        list.add(new GroupTotalAndCells(grouptotal, cells));
    }
    
    public List<GroupTotalAndCells> getListOfGroups() {
        return new ArrayList<>(list);
    }
}
