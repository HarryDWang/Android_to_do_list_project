package com.example.harrywang.to_do_list_ml;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ArrayAdapter;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import android.content.Intent;
import android.widget.AdapterView;
import android.view.ContextMenu;
import java.util.Scanner;
import java.io.PrintWriter;


public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> arrayListToDo;
    ArrayAdapter<String> arrayAdapterToDo;
    String messageText;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("creating");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        arrayListToDo = new ArrayList<>();
        arrayAdapterToDo = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayListToDo);
        listView.setAdapter(arrayAdapterToDo);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id){
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,EditMessageClass.class);
                intent.putExtra(Intent_Constants.INTENT_MESSAGE_DATA,arrayListToDo.get(position).toString());
                intent.putExtra(Intent_Constants.INTENT_ITEM_POSITION,position);
                startActivityForResult(intent,Intent_Constants.INTENT_REQUEST_CODE_TWO);

            }
        });

        try{
            Scanner sc = new Scanner(openFileInput("Todo.txt"));
            while(sc.hasNextLine()){
                String data = sc.nextLine();
                arrayAdapterToDo.add(data);
            }
            sc.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,View v,ContextMenu.ContextMenuInfo menuInfo){
        menu.setHeaderTitle("What would you like to do?");
        String options[]={"Delete","Cancel"};
        for(String option : options){
            menu.add(option);
        }
    }

    @Override
    public void onBackPressed(){
        try{
            PrintWriter pw = new PrintWriter(openFileOutput("Todo.txt",android.content.Context.MODE_PRIVATE));
            for(String data : arrayListToDo){
                pw.println(data);
            }
            System.out.println("testingtesting");
            pw.close();
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        finish();
    }

    public void onClick(View v){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,Edit_FieldClass.class);
        startActivityForResult(intent,Intent_Constants.INTENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode==Intent_Constants.INTENT_REQUEST_CODE){
            messageText = data.getStringExtra(Intent_Constants.INTENT_MESSAGE_FIELD);
            arrayListToDo.add(messageText);
            arrayAdapterToDo.notifyDataSetChanged();
        }
        else if(resultCode==Intent_Constants.INTENT_RESULT_CODE_TWO){
            messageText = data.getStringExtra(Intent_Constants.INTENT_CHANGED_MESSAGE);
            position = data.getIntExtra(Intent_Constants.INTENT_ITEM_POSITION,-1);
            arrayListToDo.remove(position);
            arrayListToDo.add(position,messageText);
            arrayAdapterToDo.notifyDataSetChanged();
        }
    }




}
