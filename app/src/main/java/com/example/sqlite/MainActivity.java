package com.example.sqlite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase database;
    List<Computer> computerList;
    ComputerAdapter computerAdapter;
    ListView listView;
    Button insertBTAX, updateBTAX;
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Xoa may tinh");
                alertDialog.setIcon(R.mipmap.ic_launcher);
                alertDialog.setMessage("Ban co chac xoa mon nay");
                int position = i;
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
            }
        });
        insertBTAX = (Button) findViewById(R.id.insertBT);
        updateBTAX = (Button) findViewById(R.id.updateBT);
        insertBTAX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    public void doCreateDB(){
        database = openOrCreateDatabase("QuanLyMayTinh.db",MODE_PRIVATE,null);
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
    public void SetDataForListView(){
        computerList.clear();
        Cursor c = database.query("Computer", null, null, null,null, null,null);
        c.moveToFirst();

        while (c.isAfterLast() == false){
            computerList.add(new Computer(c.getString(0),c.getString(1), c.getString(2)));
            c.moveToNext();
        }
    }
}