/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pietsgeneralsudoku;

import java.awt.Color;
import java.util.function.Function;

/**
 *
 * @author Sylvia
 */
public class SudokuConstants {
    
    public static void main(String... args) {
        System.out.println(GameStatus.valueOf("SETUP"));
    }
    enum GameType {NORMAL, KILLER, CALCU}
    enum GroupOperation {NORMAL, SUM, PROD}
    enum GroupType {ROW, COL, SQUARE, OTHER}
    enum GameStatus {SETUP, USERSOLVING, FINISHED}
    
    enum Colors {
        NORMAL (new Color(175,236,111)),
        SELECTED(new Color(255,255,231)),
        GROUPED(new Color(225, 255, 160)),
        BUTTONPANEL(Color.BLUE),
        KILLERCELLPANEL(Color.BLACK),
        MESSAGEPANEL(Color.LIGHT_GRAY),
        GROUPBORDER(new Color(0, 200, 0)),
        CURRENTCELL(Color.CYAN)
        ;
        
        final Color color;
        
        Colors(Color c) {
            color = c;
        }
    }
    
    enum Message {
        SETUP ("Press \"Set Up\" to start the set up"),
        USERSOLVING ("Press \"User Solving\" when done set up"),
        WRONGNUMBEROFCELLS ("nr of cells > size!"),
        NOTCONNECTED ("selected cells are not connected"),
        SOLVED ("Press \"give Solution\" for the solution"),
        NOTENOUGHGROUPS ("one or more cells have no group"),
        SUCCESS ("Success wih the solving"),
        NOTAVALIDGROUP ("selection is not a valid group"),
        INVALIDGROUPTOTAL ("Impossible total. Please enter a valid total"),
        CLEAR (""),
        STATUSMUSTBESETUP ("You need to be in SetUp to load/save a game"),
        STATUSMUSTBEUSERSOLVING ("You need to be in UserSolving to save a game"),
        UNABLETOSAVE ("Can't save file, unfortunately"),
        FILEDOESNOTEXIST ("File does not exist"),
        FILENOTOKAY ("Killerfile is not correct")
        ;
        
        final String message;
        Message(String s) {
            message = s;
        }
        
        @Override
        public String toString() {
            return message;
        }
    }
    
    enum Direction {
        GOLEFT (0, -1),
        GORIGHT (0, 1),
        GOUP (-1, 0),
        GODOWN (1, 0)
        ;
    
        int deltax;
        int deltay;
        
        Direction(int x, int y) {
            deltax = x;
            deltay = y;
        }
    }
    
    enum BorderSize {
        KILLERPANEL_BORDER (5),
        KILLERPANEL_SQUAREBORDER (2),
        KILLERPANEL_CELLBORDER (1),
        BUTTOPANEL_BORDER (5),
        BUTTONPANEL_GAP (5)
        ;
        final int width;
        BorderSize(int c) {
            width = c;
        }
    }
    
    enum CellSize {
        NORMAL (50);
        
        final int size;
        
        CellSize(int size) {
            this.size = size;
        }
    }
}
