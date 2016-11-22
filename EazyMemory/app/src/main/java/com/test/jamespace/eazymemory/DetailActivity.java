package com.test.jamespace.eazymemory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import data.DataBaseHandler;

public class DetailActivity extends AppCompatActivity {
    private TextView title, date, content, goBack, edit;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title = (TextView) findViewById(R.id.detail_title);
        date = (TextView) findViewById(R.id.detail_date);
        content = (TextView) findViewById(R.id.detail_content_text);
        deleteButton = (Button) findViewById(R.id.delet_button);
        goBack = (TextView) findViewById(R.id.goBackText);
        edit = (TextView) findViewById(R.id.editText);

        //go back to the list
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, DisplaylistActivity.class);
                startActivity(intent);
            }
        });

        //show the detail
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            title.setText(extras.getString("title"));
            date.setText("Created: " + extras.getString("date"));
            content.setText("  " + extras.getString("content"));

            final int id = extras.getInt("id");
            //delete the memory and go back
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataBaseHandler dba = new DataBaseHandler(getApplicationContext());
                    dba.deleteMemory(id);
                    Toast.makeText(getApplicationContext(),"You have deleted it.",Toast.LENGTH_LONG).show();
                    dba.close();
                    startActivity(new Intent(DetailActivity.this, DisplaylistActivity.class));
                }
            });

            //go to edit activity
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailActivity.this, EditActivity.class);
                    intent.putExtra("title",title.getText().toString());
                    intent.putExtra("content",content.getText().toString());
                    intent.putExtra("id",id);
                    startActivity(intent);
                }
            });

        }



    }
}
