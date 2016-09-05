/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pietsgeneralsudoku;

import java.util.Collection;

/**
 *
 * @author Sylvia
 */
public class NumberSum extends Tuple<Integer, Integer> {
    NumberSum(int k, int v) {
        super(k, v);
    }
    
    NumberSum(Collection<Integer> col) {
        this(col.size(), Utils.sum(col));
    }
}
