package com.example.sqlite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public SQLiteDatabase database;
    List<Computer> computerList;
    ComputerAdapter computerAdapter;
    ListView listView;
    Button insertBTAX, updateBTAX;
    int position = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doCreateDB();
//        doCreateTable("Category");
//        doCreateTable("Computer");
//
//        doInsertTable("Category", "1", "", "DELL");
//        doInsertTable("Category", "2", "", "ASUS");
//        doInsertTable("Category", "3", "", "MAC");
        doInsertTable("Computer", "1", "1", "DELL LATITUDE");
        doInsertTable("Computer", "3", "2", "MAC M1");
        doInsertTable("Computer", "1", "3", "DELL INSPIRON");
        doInsertTable("Computer", "2", "4", "ASUS VIVOBOOK");

//        doQueryTable("Computer","");
//        deleteDB("QuanLyMayTinh.db");
        computerList = new ArrayList<>();
        SetDataForListView();
        computerAdapter = new ComputerAdapter(computerList,MainActivity.this);
        listView = (ListView) findViewById(R.id.computerLV);
        listView.setAdapter(computerAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SetPositionOfItem(i);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Xoa may tinh");
                alertDialog.setIcon(R.mipmap.ic_launcher);
                alertDialog.setMessage("Ban co chac xoa mon nay");
                SetPositionOfItem(i);
                alertDialog.setPositiveButton("Co", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Computer computer = computerList.get(position);
//                        Đã xóa trong cơ sở dữ liệu
                        doDeleteTable(computer.getMaMT());
//                        Cách 1: Reset trong cơ sở dữ liệu
//                        SetDataForListView();
//                        Cách 2: Xóa trên giao diện
                        computerList.remove(position);
                        computerAdapter.notifyDataSetChanged();
                    }
                });
                alertDialog.setNegativeButton("Khong", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.show();
                return false;
            }
        });
        insertBTAX = (Button) findViewById(R.id.insertBT);
        insertBTAX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_dialog);
                dialog.show();
                TextView operationTVAX = dialog.findViewById(R.id.operationTV);
                operationTVAX.setText("Chèn ");

                EditText maMTTVAX = dialog.findViewById(R.id.maMTET);
                EditText tenTVAX = dialog.findViewById(R.id.tenET);
                EditText maDMTVAX = dialog.findViewById(R.id.maDMET);

                Button okBTAX = dialog.findViewById(R.id.okBT);
                Button cancelBTAX = dialog.findViewById(R.id.cancelBT);
                okBTAX.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String maDM = maDMTVAX.getText().toString();
                        String maMT = maMTTVAX.getText().toString();
                        String ten = tenTVAX.getText().toString();
                        doInsertTable("Computer",maDM,maMT, ten);
                        SetDataForListView();
                        computerAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                cancelBTAX.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
            }
        });
        updateBTAX = findViewById(R.id.updateBT);
        updateBTAX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_dialog);
                dialog.show();
                TextView operationTVAX = dialog.findViewById(R.id.operationTV);
                operationTVAX.setText("Cập nhật ");
                Computer computer = computerList.get(position);
                EditText maMTTVAX = dialog.findViewById(R.id.maMTET);
                EditText tenTVAX = dialog.findViewById(R.id.tenET);
                EditText maDMTVAX = dialog.findViewById(R.id.maDMET);
                maMTTVAX.setText(computer.getMaMT());
                tenTVAX.setText(computer.getTen());
                maDMTVAX.setText(computer.getMaDM());

                Button okBTAX = dialog.findViewById(R.id.okBT);
                okBTAX.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        doUpdateTable(maMTTVAX.getText().toString(),tenTVAX.getText().toString());
                        SetDataForListView();
                        computerAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                Button cancelBTAX = dialog.findViewById(R.id.cancelBT);
                cancelBTAX.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
            }
        });
    }

    public void SetDataForListView(){
        computerList.clear();
        Cursor c = database.query("Computer", null, null, null,null, null,null);
        c.moveToFirst();
        while (c.isAfterLast() == false){
            computerList.add(new Computer(c.getString(0),c.getString(1), c.getString(2)));
            c.moveToNext();
        }
    }
    public void SetPositionOfItem(int i){
        position = i;
    }
    public void doCreateDB(){
        database = openOrCreateDatabase("QuanLyMayTinh.db", MODE_PRIVATE,null);
        System.out.println("Create or Open Database Successful");
    }
    public void deleteDB(String DataBaseName){
        deleteDatabase(DataBaseName);
        System.out.println("Delete Database");
    }
    public void doCreateTable(String tableName){
        String sql = "";
        if(tableName.equals("Category")){
                    sql =   "Create table " + tableName
                    +       "("
                    +       "ma TEXT primary key,"
                    +       "ten TEXT)";
        }
        else
            if(tableName.equals("Computer")){
                        sql =   "Create table " + tableName + "("
                        +       "maMT TEXT primary key,"
                        +       "ten TEXT,"
                        +       "maDM TEXT not null constraint maDM "
                        +       "references Category(ma) on delete cascade)";
            }
        database.execSQL(sql);
        System.out.println("Create table " + tableName + "successful");
    }
    public void doInsertTable(String tableName, String maDM, String maMT, String ten){
        ContentValues values = new ContentValues();
        if(tableName.equals("Category")){
            values.put("ma", maDM);
            values.put("ten",ten);
        }
        else
            if(tableName.equals("Computer")){
                values.put("maMT",maMT);
                values.put("ten",ten);
                values.put("maDM",maDM);
            }
        if(database.insert(tableName,null,values) != 1);
            System.out.println("Insert " + tableName + " Successful");
    }
    public void doUpdateTable(String condition, String new_value){
        ContentValues values = new ContentValues();
        values.put("ten", new_value);
        int result = database.update("Computer",values,"maMT = ?", new String[]{condition});
        if(result != 0)
            System.out.println("Update " + "ComputerTable" + " Successful");
    }
    public void doDeleteTable(String condition){
        int result = database.delete("Computer", "maMT = ?",new String[]{condition});
        if(result != 0)
            System.out.println("Delete " + "ComputerTable" + " Successful");
    }
    public void doQueryTable(String tableName, String maDM){
        Cursor c;
        if(maDM.equals(""))
            c = database.query(tableName, null, null, null, null, null, null);
        else
            c = database.query(tableName, null, "maDM = ?", new String[]{maDM}, null, null, null);

        c.moveToFirst();
        if(c == null)
            System.out.println("Cursor is null");
        String data = "";
        while(c.isAfterLast() == false){
            data += c.getString(0) + "_" + c.getString(1) + "_";
            data += "\n";
            c.moveToNext();
        }
        if(data.equals(""))
            System.out.println("Không có dữ liệu đổ vào");
        System.out.println(data);
        c.close();
    }
}