/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pietsgeneralsudoku;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sylvia
 */
public class SudokuCellTest {
    
    public SudokuCellTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getPosition method, of class SudokuCell.
     */
    @Test
    public void testGetPosition() {
        System.out.println("getPosition");
        SudokuCell instance = null;
        Position expResult = null;
        Position result = instance.getPosition();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of initialize method, of class SudokuCell.
     */
    @Test
    public void testInitialize() {
        System.out.println("initialize");
        SudokuCell instance = null;
        instance.initialize();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getValue method, of class SudokuCell.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        SudokuCell instance = null;
        int expResult = 0;
        int result = instance.getValue();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addGroup method, of class SudokuCell.
     */
    @Test
    public void testAddGroup() {
        System.out.println("addGroup");
        Group group = null;
        SudokuCell instance = null;
        instance.addGroup(group);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of possibleValues method, of class SudokuCell.
     */
    @Test
    public void testPossibleValues() {
        System.out.println("possibleValues");
        SudokuCell instance = null;
        List<Integer> expResult = null;
        List<Integer> result = instance.possibleValues();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class SudokuCell.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object other = null;
        SudokuCell instance = null;
        boolean expResult = false;
        boolean result = instance.equals(other);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hashCode method, of class SudokuCell.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        SudokuCell instance = null;
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of compareTo method, of class SudokuCell.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        SudokuCell other = null;
        SudokuCell instance = null;
        int expResult = 0;
        int result = instance.compareTo(other);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
