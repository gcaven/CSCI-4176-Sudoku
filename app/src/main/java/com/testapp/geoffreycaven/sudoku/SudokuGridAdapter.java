package com.testapp.geoffreycaven.sudoku;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by geoffreycaven on 2017-02-17.
 */

public class SudokuGridAdapter extends BaseAdapter{

    private Context context;
    private String[] items;
    LayoutInflater inflater;

    public SudokuGridAdapter(Context context, String[] items) {
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.sudoku_cell, null);
        }
        TextView t = (TextView)convertView.findViewById(R.id.grid_item);
        t.setText(items[position]);

        int x = position%9;
        int y = position/9;

        //add borders based on cell position in board
        if (x == 2 || x == 5) {
            if (y == 3 || y == 6) {
                t.setBackgroundResource(R.drawable.border_top_right);
            } else {
                t.setBackgroundResource(R.drawable.border_right);
            }
        }
        if((y == 3 || y == 6) && !(x == 2 || x == 5)) {
            t.setBackgroundResource(R.drawable.border_top);
        }
        return convertView;
    }

    @Override
    public int getCount() { return items.length; }

    @Override
    public Object getItem(int position) { return items[position]; }

    @Override
    public long getItemId(int position) { return position; }
}
