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

import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * class Tuple, modelled after the Tuple class in Scala.
 * The generic parameters K and V are arbitrary classes.
 * The getters return a reference to the two members,
 * it is the responsability of the user to ensure that
 * both members are immutable!
 * 
 * The types K and V are generic.
 *
 * @author Piet
 * The Hague, 2015-06-08
 */
public class Tuple<K, V> {
    
    //***************************************
    //  members
    //***************************************
    final private K _1;
    final private V _2;
    
    /****************************************
     * Constructor
     * @param k first parameter of type K
     * @param v second parameter of type V
     ****************************************/
    public Tuple(K k, V v) {
        if (k == null || v == null) {
            throw new IllegalArgumentException("arguments can not be null!");
        }
        this._1 = k;
        this._2 = v;
    }
    
    /****************************************************
     * @return the first element, of type K
     *****************************************************/
    public K getFirst() {
        return _1;
    }
    
    /****************************************************
     * @return the second element, of type V
     ****************************************************/
    public V getSecond() {
        return _2;
    }
    
    /****************************************************
     * @param other the object of which the equality is tested
     * @return true if this equals other, else false
     ****************************************************/
    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (! (other instanceof Tuple)) return false;
        Tuple t = (Tuple) other;
        return _1.equals(t._1) && _2.equals(t._2);
    }

    /**
     * 
     * @return hashcode
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this._1);
        hash = 59 * hash + Objects.hashCode(this._2);
        return hash;
    }
    
    @Override
    public String toString() {
        String s = "<%s, %s>";
        return String.format(s, _1.toString(), _2.toString());
    }
}

