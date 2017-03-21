package com.testapp.geoffreycaven.sudoku;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private GridView sudokuGrid;
    private String[] items;
    private String[] startState;
    private String activeNum;
    private Button[] numpad = new Button[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //put all the numpad buttons in an array so we access them later
        numpad[0] = (Button)this.findViewById(R.id.button1);
        numpad[1] = (Button)this.findViewById(R.id.button2);
        numpad[2] = (Button)this.findViewById(R.id.button3);
        numpad[3] = (Button)this.findViewById(R.id.button4);
        numpad[4] = (Button)this.findViewById(R.id.button5);
        numpad[5] = (Button)this.findViewById(R.id.button6);
        numpad[6] = (Button)this.findViewById(R.id.button7);
        numpad[7] = (Button)this.findViewById(R.id.button8);
        numpad[8] = (Button)this.findViewById(R.id.button9);

        generateNewPuzzle();
    }

    public void generateNewPuzzle() {
        //get puzzle, save starting state
        this.items = generateBoard();
        this.startState = this.items.clone();
        //initialize puzzle
        sudokuGrid = (GridView) this.findViewById(R.id.sudokuGrid);
        SudokuGridAdapter gridAdapter = new SudokuGridAdapter(MainActivity.this, items);
        sudokuGrid.setAdapter(gridAdapter);
    }

    public void sudokuClick(View view) {
        Button b = (Button)view;
        //clear previously active button
        for (Button btn : numpad) {
            btn.getBackground().clearColorFilter();
        }
        //set button to active state
        b.getBackground().setColorFilter(Color.parseColor("#5C6BC0"), PorterDuff.Mode.MULTIPLY);
        activeNum = b.getText().toString();

        //listen for taps on game board
        sudokuGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //check if the move is valid, and not on a square that is filled at the start of the puzzle
                if (!checkValidMove(position) || !startState[position].equals("")) {
                    Log.d("", startState[position]);
                    return;
                }
                //se item, set grid
                items[position] = "" + activeNum;
                SudokuGridAdapter gridAdapter = new SudokuGridAdapter(MainActivity.this, items);
                sudokuGrid.setAdapter(gridAdapter);
                //victory triggered when grid is full
                if (checkSolved()) {
                    Toast toast = Toast.makeText(MainActivity.this, "Congrats, you solved the puzzle! Press 'New' to try another puzzle.", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    public boolean checkValidMove(int position) {
        int x = position % 9;
        int y = position / 9;
        //check row for conflicts
        int after = 8 - x;
        int before = 8 - after;
        int curr = position;
        for (int i = 1; i <= after; i++) {
            if (items[curr+i].equals(activeNum)) {
                //Log.d("Found invalid", "after row pos");
                return false;
            }
        }
        for (int i = 1; i <= before; i++) {
            if (items[curr-i].equals(activeNum)) {
                //Log.d("Found invalid", "before row pos");
                return false;
            }
        }
        //check column for conflicts
        after = 8 - y;
        before = 8 - after;
        for (int i = 1; i <= after; i++) {
            if (items[curr+(i*9)].equals(activeNum)) {
                //Log.d("Found invalid", "after col pos");
                return false;
            }
        }
        for (int i = 1; i <= before; i++) {
            if (items[curr-(i*9)].equals(activeNum)) {
                //Log.d("Found invalid", "before col pos");
                return false;
            }
        }
        //check local square for conflicts
        int super_grid_x = x/3;
        int super_grid_y = y/3;
        int y_in_local = y - (super_grid_y*3);
        int x_in_local = x - (super_grid_x*3);
        int local_pos = position - y_in_local*9 - x_in_local;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (items[local_pos+i+(j*9)].equals(activeNum)) {
                    //Log.d("Found invalid", "in local square @ " + i +", " + j);
                    return false;
                }
            }
        }
        //if not invalid, must be valid
        return true;
    }

    //check that grid is completely full
    //since user cannot enter invalid options, must be correct if full
    public boolean checkSolved() {
        for (String val : items) {
            if (val.equals("")) {
                return false;
            }
        }
        return true;
    }

    //on click 'New' button, replicate onCreate procedure
    public void newPuzzleClick(View view) {
        generateNewPuzzle();
    }

    //select from hardcoded puzzles, returns one at random as 2D array
    public String[] generateBoard() {
        ArrayList<String[][]> puzzles = new ArrayList<>();
        String[][] p1 = {
                {"", "3", "4", "", "", "", "1", "5", ""},
                {"2", "", "", "5", "", "4", "", "", "3"},
                {"6", "", "", "", "3", "", "", "", "8"},
                {"", "7", "", "1", "9", "3", "", "2", ""},
                {"", "", "5", "", "", "", "3", "", ""},
                {"", "8", " ", "7", "5", "2", "", "4", ""},
                {"8", "", "", "", "1", "", "", "", "4"},
                {"1", "", "", "9", "", "5", "", "", "6"},
                {"", "6", "7", "", "", "", "2", "9", ""}
        };
        String[][] p2 = {
                {"", "", "", "2", "6", "", "7", "", "1"},
                {"6", "8", "", "", "7", "", "", "9", ""},
                {"1", "9", "", "", "", "4", "5", "", ""},
                {"8", "2", "", "1", "", "", "", "4", ""},
                {"", "", "4", "6", "", "2", "9", "", ""},
                {"", "5", "", "", "", "3", "", "2", "8"},
                {"", "", "9", "3", "", "", "", "7", "4"},
                {"", "4", "", "", "5", "", "", "3", "6"},
                {"7", "", "3", "", "1", "8", "", "", ""}
        };
        String[][] p3 = {
                {"1", "", "", "4", "8", "9", "", "", "6"},
                {"7", "3", "", "", "", "", "", "4", ""},
                {"", "", "", "", "", "1", "2", "9", "5"},
                {"", "", "7", "1", "2", "", "6", "", ""},
                {"5", "", "", "7", "", "3", "", "", "8"},
                {"", "", "6", "", "9", "5", "7", "", ""},
                {"9", "1", "4", "6", "", "", "", "", ""},
                {"", "2", "", "", "", "", "", "3", "7"},
                {"8", "", "", "5", "1", "2", "", "", "4"}
        };
        String[][] p4 = {
                {"", "", "", "", "", "", "", "", ""},
                {"", "8", "9", "4", "1", "", "", "", ""},
                {"", "", "6", "7", "", "", "1", "9", "3"},
                {"2", "", "", "", "", "", "7", "", ""},
                {"3", "4", "", "6", "", "", "", "1", ""},
                {"", "", "", "9", "", "", "", "", "5"},
                {"", "", "", "", "2", "", "", "5", ""},
                {"6", "5", "", "", "4", "", "", "2", ""},
                {"7", "3", "", "1", "", "", "", "", ""}
        };
        String[][] p5 = {
                {"", "", "", "", "4", "", "", "", ""},
                {"5", "", "", "", "", "3", "", "8", ""},
                {"", "6", "", "", "", "", "5", "2", "7"},
                {"", "", "", "", "", "", "", "", ""},
                {"", "", "", "", "3", "6", "", "", ""},
                {"", "", "7", "", "2", "8", "", "6", "9"},
                {"", "2", "", "9", "8", "", "", "", ""},
                {"3", "", "", "", "1", "5", "", "4", ""},
                {"", "", "", "", "", "4", "", "7", "8"}
        };
        puzzles.add(p1);
        puzzles.add(p2);
        puzzles.add(p3);
        puzzles.add(p4);
        puzzles.add(p5);
        String[] flattenedItems = new String[81];
        int i = 0;
        Random rand = new Random();
        for (String[] row : puzzles.get(rand.nextInt(puzzles.size()))) {
            for (String val : row) {
                flattenedItems[i++] = val;
            }
        }
        return flattenedItems;
    }
}
