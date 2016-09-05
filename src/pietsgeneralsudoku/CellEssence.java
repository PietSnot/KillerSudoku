/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pietsgeneralsudoku;

/**
 *
 * @author Sylvia
 */
public class CellEssence {
    final int row, col, value;
    final String text;
    
    public CellEssence(int r, int c, int v, String s) {
        row = r;
        col = c;
        value = v;
        text = s;
    }
}
