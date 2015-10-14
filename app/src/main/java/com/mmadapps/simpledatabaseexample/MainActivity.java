package com.mmadapps.simpledatabaseexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
 EditText e_name,e_id,e_marks;
    Button b_save;
    Helper helper;
    ArrayList<Bean> beanArrayList;
    boolean isInserted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e_name = (EditText) findViewById(R.id.V_name);
        e_id = (EditText) findViewById(R.id.V_id);
        e_marks = (EditText) findViewById(R.id.V_marks);
        b_save = (Button) findViewById(R.id.V_button);
        beanArrayList=new ArrayList<>();
        helper = new Helper(this);
        createDatabase();
        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bean bean1 = new Bean();
                bean1.setId(e_id.getText().toString());
                bean1.setName(e_name.getText().toString());
                bean1.setMarks(e_marks.getText().toString());
                beanArrayList.add(bean1);
                try {
                    helper.openDataBase();
                     isInserted = helper.insertvalData(beanArrayList);

                    helper.exportDatabse();
                    helper.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                if (isInserted = true) {
                    Toast.makeText(MainActivity.this, "inserted", Toast.LENGTH_LONG).show();
                    // getdata();


                } else {
                    Toast.makeText(MainActivity.this, "not inserted", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void createDatabase() {
        try {
            helper = new Helper(getApplicationContext());
            helper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
