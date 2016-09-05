/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pietsgeneralsudoku;

import java.util.BitSet;
import javax.swing.SwingUtilities;

/**
 *
 * @author Sylvia
 */
public class PietsGeneralSudoku {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StartFrame(" * * * Sudoku Sores * * * ");
            }
        });
    }
    
}
