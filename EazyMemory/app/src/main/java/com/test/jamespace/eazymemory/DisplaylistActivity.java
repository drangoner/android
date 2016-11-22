package com.test.jamespace.eazymemory;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SymbolTable;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DialogTitle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import data.DataBaseHandler;
import model.MyMemory;

public class DisplaylistActivity extends Activity {

    private DataBaseHandler dba;
    private ArrayList<MyMemory> dbMemory = new ArrayList<>();
    private MemoryAdapter memoryAdapter;
    private ListView listView;
    private Button button;
    private AlertDialog.Builder dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaylist);

        listView = (ListView) findViewById(R.id.myListView);
        button = (Button) findViewById(R.id.create);
        refreshData();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplaylistActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
           moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void refreshData(){
        dbMemory.clear();
        dba = new DataBaseHandler(getApplicationContext());
        ArrayList<MyMemory> memFromDB = dba.getMemory();

        for(int i = 0; i < memFromDB.size(); i++){
            String title = memFromDB.get(i).getTitle();
            String content = memFromDB.get(i).getContent();
            String dateText = memFromDB.get(i).getRecordDate();
            int mid =memFromDB.get(i).getItemID();

            MyMemory myMemory = new MyMemory();
            myMemory.setTitle(title);
            myMemory.setContent(content);
            myMemory.setRecordDate(dateText);
            myMemory.setItemID(mid);
            dbMemory.add(myMemory);
        }
        dba.close();
        //when nothing to show
        if(dbMemory.size() == 0){
            Toast.makeText(getApplicationContext(), "You have not created any little memory...", Toast.LENGTH_LONG).show();

        }
        memoryAdapter = new MemoryAdapter(DisplaylistActivity.this, R.layout.memory_row, dbMemory);
        listView.setAdapter(memoryAdapter);
        memoryAdapter.notifyDataSetChanged();
    }

    public class MemoryAdapter extends ArrayAdapter<MyMemory>{

        Activity activity;
        int layoutResouse;
        MyMemory myMemory;
        ArrayList<MyMemory> mdata = new ArrayList<>();
        public MemoryAdapter(Activity atc, int resource, ArrayList<MyMemory> data) {
            super(atc, resource, data);
            activity = atc;
            layoutResouse = resource;
            mdata = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mdata.size();
        }
        @Nullable
        @Override
        public MyMemory getItem(int position) {
            return super.getItem(position);
        }

        @Override
        public int getPosition(MyMemory item) {
            return super.getPosition(item);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder = null;
            if(row == null || (row.getTag()) == null){
                LayoutInflater inflater = LayoutInflater.from(activity);
                row = inflater.inflate(layoutResouse,null);
                holder = new ViewHolder();
                holder.mTitle = (TextView) row.findViewById(R.id.name);
                holder.mdelete = (TextView) row.findViewById(R.id.delete_text);
                holder.mDate = (TextView) row.findViewById(R.id.dateText);
                holder.view = row;
                row.setTag(holder);
            }else{
                holder = (ViewHolder) row.getTag();
            }
            holder.myMemory = getItem(position);
            holder.mTitle.setText(holder.myMemory.getTitle());
            holder.mDate.setText(holder.myMemory.getRecordDate());
            final ViewHolder finalHolder = holder;
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text = finalHolder.myMemory.getContent().toString();
                    String dateText = finalHolder.myMemory.getRecordDate().toString();
                    String title = finalHolder.myMemory.getTitle().toString();
                    int mid = finalHolder.myMemory.getItemID();

                    Intent intent = new Intent(DisplaylistActivity.this, DetailActivity.class);
                    intent.putExtra("content", text);
                    intent.putExtra("date", dateText);
                    intent.putExtra("title", title);
                    intent.putExtra("id",mid);
                    startActivity(intent);
                }
            });
            holder.mdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = new AlertDialog.Builder(DisplaylistActivity.this);
                    dialog.setTitle("Warning!!!");
                    dialog.setMessage("Are you sure to delete it?");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DataBaseHandler dba = new DataBaseHandler(getApplicationContext());
                                    dba.deleteMemory(finalHolder.myMemory.getItemID());
                                    Toast.makeText(getApplicationContext(),"You have deleted it.",Toast.LENGTH_LONG).show();
                                    dba.close();
                                    startActivity(new Intent(DisplaylistActivity.this, DisplaylistActivity.class));
                                }
                            });
                    dialog.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog dialogD = dialog.create();
                    dialogD.show();
                }
            });
            return row;
        }

        class ViewHolder{
            View view;
            MyMemory myMemory;
            TextView mTitle;
            TextView mdelete;
            int mId;
            TextView mContext;
            TextView mDate;
        }
    }
}


