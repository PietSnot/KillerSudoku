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
public class GroupTotalAndCells extends Tuple<Integer, List<CellEssence>> {
    public GroupTotalAndCells(int grouptotal, List<CellEssence> list) {
        super(grouptotal, list);
    }
    
    public int getGrouptotal() {
        return this.getFirst();
    }
    
    public List<CellEssence> getListOfCellEssences() {
        return new ArrayList<>(this.getSecond());
    }
}
