package com.example.sqlite;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ComputerAdapter extends BaseAdapter {
    private List<Computer> computerList;
    private Activity activity;

    public ComputerAdapter(List<Computer> computerList, Activity activity) {
        this.computerList = computerList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return computerList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = activity.getLayoutInflater();
        view = inflater.inflate(R.layout.activity_computer,null);
        TextView maPCAX = view.findViewById(R.id.idPCAX);
        TextView namePCAX = view.findViewById(R.id.namePCAX);
        Computer computer = computerList.get(i);
        maPCAX.setText(computer.getMaMT());
        namePCAX.setText(computer.getTen());
        return view;
    }
}
