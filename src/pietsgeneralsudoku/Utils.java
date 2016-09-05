/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pietsgeneralsudoku;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Sylvia
 */
public class Utils {
    
    public static void main(String... args) {
        int aantal = 9;
        Map<NumberSum, BitSet> map = makeBitSets(aantal);
        System.out.println("map (" + aantal + ") size = " + map.size());
        System.out.println("*******************************");
        for (Map.Entry<NumberSum, BitSet> e: map.entrySet()) {
            System.out.println(e.getKey() + " :: " + e.getValue());
        }
    }
    
    
    //***************************************************************
    /**
     * determines if a collection of KillerCells is connected or not.
     * It is connected if, starting from a random cell in the collection,
     * all cells can be reached by going left, right, up or down.
     * @param cells a Collection of KillerCells
     * @return true if connected, false if not
     */
    public static boolean isConnected(Collection<KillerCell> cells) {
        if (cells.isEmpty()) return false;
        if (cells.size() == 1) return true;
        List<KillerCell> copy = new ArrayList<>(cells);
        LinkedList<KillerCell> queue = new LinkedList<>();
        Set<KillerCell> alreadySeen = new HashSet<>();
        queue.add(copy.get(0));
        alreadySeen.add(copy.get(0));
        while (!queue.isEmpty()) {
            KillerCell p = queue.removeFirst();
            List<KillerCell> neighbors = getNeighbors(p, copy);
            neighbors.removeAll(alreadySeen);
            alreadySeen.addAll(neighbors);
            queue.addAll(neighbors);
        }
        return alreadySeen.containsAll(copy);
    }
    
    //*********************************************************************
    /**
     * Given a collection of KillerCells and a KillerCell p, this method
     * gives a List of all the neighbors of p, belonging to that collection.
     * The neighbors can be on the left, right, up or down from p.
     * @param p the KillerCell
     * @param coll the collection to look for possible neighbors
     * @return a List of neighbors, can be an empty list
     */
    private static List<KillerCell> getNeighbors(KillerCell p, Collection<KillerCell> coll) {
        List<KillerCell> result = new ArrayList<>();
        result.add(new KillerCell(p.row - 1, p.col)); // top
        result.add(new KillerCell(p.row + 1, p.col)); // bottom
        result.add(new KillerCell(p.row, p.col - 1)); // left
        result.add(new KillerCell(p.row, p.col + 1)); // right
        result.retainAll(coll);
        return result;
    }
    
    //***********************************************************
    /**
     * Given a BitSet b, returns a list of the indices of all bits that are set
     * (i.e. true).
     * @param b the BitSet
     * @return a list of the indices of all the set bits in b
     */
    public static List<Integer> createList(BitSet b) {
        List<Integer> result = new ArrayList<>();
        if (b != null) {
            for (int i = b.nextSetBit(0); i >= 0; i = b.nextSetBit(i+1)) {
                result.add(i);
            }
        }
        return result;
    }
    
    //*************************************************************
    /**
     * Determines the squareroot of the size of the game. I.e. if we have
     * a sudoku of size 9x9, then this method returns 3, the size of a square
     * @param size the size of a sudoku game
     * @return the square root of that size
     */
    public static int getSquareSize(int size) {
        return (int) (Math.sqrt(size) + .1);
    }
    
    //**************************************************************
    /**
     * Given the size of a sudoku game, (for instance: if the game = 9x9,
     * then the gamesize = 9), returns a BitSet for every possible KillerGroup
     * of Cells. For instance: given a KillerGroup of 4 cells, this map
     * gives a BitSet of every possible index for which there is a group of 4
     * such that the sum of those four indices equals the given GroupTotal.
     * For instance, if we are given a group of length 1, with a total of 3,
     * then the corresponding BitSet has only bit 3 set.
     * Or, given that the group has 5 cells, with a sum of 19, we get this
     * BitSet: {1, 2, 3, 4, 5, 6, 7, 8, 9}, meaning that there are combinations
     * of 5 elements with a sum of 19 (f.i: {1, 2, 3, 4, 9}, {1, 2, 3, 5, 8})
     * 
     * The idea is that given a group, given the empty cells and the remaining
     * sum, we have a BitSet giving the possible numbers. However, this does
     * not take care of numbers already filled in the group. For instance, 
     * if remaing sum = 6 and there are two empy cells left, 1 and 5 are in the
     * bitset, even thoufgh 1 and 5 may already be present in the group.
     * For each group, however, it is maintained separately what numbers are
     * possible, so to get the real possible numbers, we simply do
     * bitsetOfUnusedNumbers.and(BitSetOfPossibleNumbers).
     * 
     * Note that for a KillerSudoku size of 9x9, the map contains a
     * mere 129 map-entries, very acceptable
     * 
     * @param sizeOfGame size of the sudoku
     * @return a Map in the form Map<NumberSum, BitSet>
     */
    public static Map<NumberSum, BitSet> makeBitSets(int sizeOfGame) {
        List<Integer> list = new ArrayList<>();
        for (int x = 1; x <= sizeOfGame; x++) list.add(x);
        List<List<Integer>> subsets = subsets(list);
        ListIterator iter = subsets.listIterator();
        while (iter.hasNext()) {
            List<Integer> temp = (List<Integer>) iter.next();
            if (temp.isEmpty()) {
                iter.remove();
                break;
            }
        }
        Map<NumberSum, List<List<Integer>>> map = new HashMap<>();
        for (List<Integer> temp: subsets) {
            NumberSum ns = new NumberSum(temp);
            List<List<Integer>> ints = map.get(ns);
            if (ints == null) ints = new ArrayList<>();
            ints.add(temp);
            map.put(ns, ints);
        }
        
        Map<NumberSum, BitSet> map2 = new HashMap<>();
        for (Map.Entry<NumberSum, List<List<Integer>>> e: map.entrySet()) {
            map2.put(e.getKey(), makeBitSet(e.getValue()));
        }
        return map2;
    }
    
    //****************************************************************
    /**
     * Given a List<List<Integer>> (for instance all non-empty subsets
     * of a List<Integer>), returns a BitSet where all digits of this list
     * have their corresponding bit set in BitSet
     * @param list a List<List<Integer>>
     * @return a BitSet where all bits with index present in list are set.
     */
    private static BitSet makeBitSet(List<List<Integer>> list) {
          Set<Integer> set = new HashSet<>() ;
          for (List<Integer> temp: list) set.addAll(temp);
          BitSet bs = new BitSet();
          for (int i: set) bs.set(i);
          return bs;
    }
    
    //*********************************************************************
    /**
     * this method creates a list of all the subsets of the input list 'list'.
     * This list of subsets includes the empty list, and so the returned list
     * has cardinality (number of elements) equal to 2 ^ list.size().
     * @param <E> the type of the input list
     * @param list the list of which all subsets are to be created. Of course,
     * all implementations of List are allowed as parameter here
     * @return a list of all the subsets of the input list.
     */
    public static <E> List<List<E>> subsets(List<E> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("inputlist cannot be null or empty!");
        }
        List<List<E>> mainlist = new ArrayList<>();
        recursion(list.size(), 0, mainlist, new ArrayList<E>(), list);
        return mainlist;
    }
    
    //*********************************************************************
    /**
     * a help function that creates the subsets, by using recursion.
     * @param <E> the type of the elements
     * @param length the size of the input list
     * @param level
     * @param mainlist
     * @param templist
     * @param original 
     */
    private static <E> void recursion(
                int length, int level, List<List<E>> mainlist, 
                List<E> templist, List<E> original
               ) 
    {
        if (level == length) {
            mainlist.add(templist);
            return;
        }
        List<E> temp = new ArrayList<>(templist);
        recursion(length, level + 1, mainlist, templist, original);
        templist = temp;
        templist.add(original.get(level));
        recursion(length, level + 1, mainlist, templist, original);
    }
    
    //*********************************************************************
    /**
     * Given a Collection of Integers, returns the sum
     * @param col the collection of integers
     * @return the sum of all the integers in col
     */
    public static int sum(Collection<Integer> col) {
        int result = 0;
        for (int x: col) result += x;
        return result;
    }
    
    //=============================================================
    public static boolean killerfileIsOkay(File f) {
        if (!f.canRead()) return false;
        try (Scanner scanner = new Scanner(f)) {
            String s = scanner.nextLine();
            if (!s.startsWith("Killer")) return false;
            int size = 0;
            int groups = 0;
            int cells = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isEmpty()) {
                    if (line.toLowerCase().contains("size")) {
                        String[] temp = line.split(" ");
                        size = Integer.parseInt(temp[1]);
                    }
                    if (line.toLowerCase().contains("startgroup")) groups++;
                    if (line.toLowerCase().contains("cell")) cells++;
                }
            }
            return groups >= size && cells == size * size;
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public static Tuple<Tuple<Integer, String>, List<Tuple<Integer, List<KillerCell>>>> loadFile(File f) {
        // return new Tuple<>(4, null);
        List<KillerCell> cells = new ArrayList<>();
        List<Tuple<Integer, List<KillerCell>>> listOfTuples = new ArrayList<>();
        int size = 0, groupnr = 0, grouptotal = 0;
        String status = "";
        try (Scanner scan = new Scanner(f)) {
            String firstline = scan.nextLine().toLowerCase();
            if (!firstline.contains("killer")) return null;
            while (scan.hasNextLine()) {
                String s = scan.nextLine().toLowerCase().trim();
                if (s.startsWith("//")) continue;
                else if (s.startsWith("status")) {
                    String[] temp = s.split(" ");
                    status = temp[1];
                }
                else if (s.startsWith("size")) {
                    String[] temp = s.split(" ");
                    size = Integer.parseInt(temp[1]);
                }
                else if (s.startsWith("startgroup")) {
                    String[] temp = s.split(" ");
                    grouptotal = Integer.parseInt(temp[1]);
                    cells = new ArrayList<>();
                }
                else if (s.startsWith("cell")) {
                    String[] temp = s.split("[:,]");
                    int row = Integer.parseInt(temp[1].trim());
                    int col = Integer.parseInt(temp[2].trim());
                    KillerCell cell = new KillerCell(row, col, size, grouptotal);
                    cell.rh = Integer.parseInt(temp[3].trim());
                    cell.value = Integer.parseInt(temp[4].trim());
                    cell.setText(temp.length > 5 ? temp[5] : "");
                    cells.add(cell);
                }
                else if (s.startsWith("endgroup")) {
                    listOfTuples.add(new Tuple<>(grouptotal, cells));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }  
        return new Tuple<>(new Tuple<>(size, status), listOfTuples);
    }
    
    public static LoadedData loadFile2(File f) {
        LoadedData loadedData = new LoadedData();
        List<CellEssence> cells = new ArrayList<>();
        List<GroupTotalAndCells> groupies = new ArrayList<>();
        int size = 0, groupnr = 0, grouptotal = 0;
        String status = "";
        try (Scanner scan = new Scanner(f)) {
            String firstline = scan.nextLine().toLowerCase();
            if (!firstline.contains("killer")) return null;
            while (scan.hasNextLine()) {
                String s = scan.nextLine().toLowerCase().trim();
                if (s.startsWith("//")) continue;
                else if (s.startsWith("status")) {
                    String[] temp = s.split(" ");
                    status = temp[1].toUpperCase();
                    loadedData.setStatus(SudokuConstants.GameStatus.valueOf(status));
                }
                else if (s.startsWith("size")) {
                    String[] temp = s.split(" ");
                    size = Integer.parseInt(temp[1]);
                    loadedData.setSize(size);
                }
                else if (s.startsWith("startgroup")) {
                    String[] temp = s.split(" ");
                    grouptotal = Integer.parseInt(temp[1]);
                    cells = new ArrayList<>();
                }
                else if (s.startsWith("cell")) {
                    String[] temp = s.split("[:,]");
                    int row = Integer.parseInt(temp[1].trim());
                    int col = Integer.parseInt(temp[2].trim());
                    int value = Integer.parseInt(temp[4].trim());
                    String text = temp.length > 5 ? temp[5] : "";
                    cells.add(new CellEssence(row, col, value, text));
                }
                else if (s.startsWith("endgroup")) {
                    loadedData.addGroupTotalAndCells(grouptotal, cells);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }  
        return loadedData;
    }
}
