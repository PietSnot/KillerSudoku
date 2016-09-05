/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pietsgeneralsudoku;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Sylvia
 */
interface Group<T> {
    List<T> getCells();
    int getNr();
    boolean isNumberPossible(int nr);
    
}

interface ExtendedGroup<T> extends Group<T> {
    SudokuConstants.GroupType getType();
    int getTotal();
    int getCurrentTotal();
    List<T> getEmptyCells();
    T getSmallestCell();
}
