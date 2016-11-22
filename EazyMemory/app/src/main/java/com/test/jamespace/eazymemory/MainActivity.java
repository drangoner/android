package com.test.jamespace.eazymemory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import data.DataBaseHandler;
import model.MyMemory;

public class MainActivity extends AppCompatActivity {
    private EditText title;
    private EditText content;
    private Button submit;
    private Button listButton;
    private DataBaseHandler dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbh = new DataBaseHandler(MainActivity.this);
        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content_text);
        submit = (Button) findViewById(R.id.first_button);
        listButton = (Button) findViewById(R.id.toListButton);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDB();
            }
        });

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplaylistActivity.class);
                startActivity(intent);
            }
        });

    }

    private void saveToDB(){
        MyMemory myMemory = new MyMemory();
        myMemory.setTitle(title.getText().toString().trim());
        myMemory.setContent(content.getText().toString().trim());
        dbh.addMemory(myMemory);
        dbh.close();

        //clear
        title.setText("");
        content.setText("");

        Intent intent = new Intent(MainActivity.this,DisplaylistActivity.class);
        startActivity(intent);
    }
}
