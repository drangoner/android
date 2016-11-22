package com.test.jamespace.eazymemory;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import data.DataBaseHandler;
import model.MyMemory;

public class EditActivity extends Activity {
    private EditText title, content;
    private Button cancleButton, saveButton;
    private DataBaseHandler dba;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        title =  (EditText) findViewById(R.id.edit_title);
        content = (EditText) findViewById(R.id.edit_content_text);
        cancleButton = (Button) findViewById(R.id.cancel_button);
        saveButton = (Button) findViewById(R.id.saveButton);
        dba = new DataBaseHandler(EditActivity.this);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            String titleText = extras.getString("title");
            String contentText = extras.getString("content");
            id = extras.getInt("id");
            title.setText(titleText);
            content.setText(contentText);
        }

        //cancle edit  and go to the list
        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditActivity.this, DisplaylistActivity.class);
                startActivity(intent);
            }
        });

        //save the edit content
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savetoDB();
            }
        });
    }


    //listen the back key to go back to the list
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            startActivity(new Intent(EditActivity.this, DisplaylistActivity.class));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void savetoDB(){
        MyMemory myMemory = new MyMemory();
        myMemory.setItemID(id);
        myMemory.setTitle(title.getText().toString().trim());
        myMemory.setContent(content.getText().toString());

        dba.updateMemory(myMemory);
        dba.close();
        Toast.makeText(getApplicationContext(),"Edit successfully",Toast.LENGTH_LONG).show();
        startActivity(new Intent(EditActivity.this, DisplaylistActivity.class));
    }

}
