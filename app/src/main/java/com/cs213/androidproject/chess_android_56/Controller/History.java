package com.cs213.androidproject.chess_android_56.Controller;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.os.Bundle;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Context;

import java.util.Collections;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


import com.cs213.androidproject.chess_android_56.R;


public class History extends AppCompatActivity {
    private ListView historyListView;
    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> gameTitles;
    private ArrayList<String> gameDates;
    private ArrayList<String> gameDisplay;
    String ret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyListView = (ListView) findViewById(R.id.history);
        listAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        historyListView.setAdapter(listAdapter);
        gameTitles = new ArrayList<String>();
        gameDates = new ArrayList<String>();
        gameDisplay = new ArrayList<String>();
        Context context = this.getApplicationContext();
        historyListView.setSelected(true);


        try {

            InputStream inputStream = context.openFileInput("gameHistory.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
                System.out.println(ret);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (ret == null || ret.length() == 0 || ret.equals("")) {
            backtoMain();
        }
        gameTitles = getTitles();
        gameDates = getDates();
//        for (int i = 0; i < gameTitles.size(); i++) {
//            gameTitles.get(i).trim();
//            System.out.println(gameTitles.get(i));
//            gameDisplay.add(gameTitles.get(i) + "," + gameDates.get(i));
//        }
        listAdapter.addAll(gameDisplay);
        historyListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startReplay(position);

            }
        });
        sortByDate(this.historyListView);

    }


    public ArrayList<String> getTitles() {
        ArrayList<String> g = new ArrayList<String>();
        String current = "";
        boolean title = true;
        if (ret == null) {
            return null;
        }
        for (int i = 0; i < ret.length(); i++) {
            char a = ret.charAt(i);
            if (a == '+' && title == true) {
                title = false;
                g.add(current);
                current = "";
            } else if (a == ':' && title == false) {
                i = i + 2;
                if (i >= ret.length()) {
                    break;
                }
                title = true;
                a = ret.charAt(i);
            }

            if (title == true) {
                current += a;
            }
        }

        return g;
    }

    public ArrayList<String> getDates() {
        ArrayList<String> g = new ArrayList<String>();
        String current = "";
        boolean date = false;
        if (ret == null) {
            return null;
        }
        for (int i = 0; i < ret.length(); i++) {
            char a = ret.charAt(i);
            if (a == '+' && date == false) {
                date = true;
                i++;
                a = ret.charAt(i);
            } else if (a == '~' && date == true) {
                date = false;
                g.add(current);
                current = "";
            }

            if (date == true) {
                current += a;
            }
        }

        return g;

    }

    public void startReplay(int position) {
        AlertDialog.Builder builder;
        final Intent intent = new Intent(this, Replay.class);
        Replay.gameName = gameTitles.get(position);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(History.this);


        alertDialogBuilder.setTitle("Watch " + gameTitles.get(position) + "?");

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }

    public void sortByDate(View v) {
        for (int i = 0; i < gameDates.size(); i++) {
            if (i == gameDates.size() - 1) {
                break;
            }
            String m = "";
            String d = "";
            m += gameDates.get(i).charAt(5);
            m += gameDates.get(i).charAt(6);
            d += gameDates.get(i).charAt(8);
            d += gameDates.get(i).charAt(9);
            int mi = Integer.parseInt(m);
            int di = Integer.parseInt(d);
            String m2 = "";
            String d2 = "";
            m2 += gameDates.get(i + 1).charAt(5);
            m2 += gameDates.get(i + 1).charAt(6);
            d2 += gameDates.get(i + 1).charAt(8);
            d2 += gameDates.get(i + 1).charAt(9);
            int mi2 = Integer.parseInt(m2);
            int di2 = Integer.parseInt(d2);
            System.out.println(di + "  " + di2);

            if (mi > mi2) {
                Collections.swap(gameDates, i, i + 1);
                Collections.swap(gameTitles, i, i + 1);
                i = 0;
            } else if (mi < mi2) {
            } else if (mi == mi2) {
                if (di > di2) {
                    Collections.swap(gameDates, i, i + 1);
                    Collections.swap(gameTitles, i, i + 1);
                    System.out.println("hello");
                    i = 0;
                } else if (di < di2) {
                } else {

                }

            }
        }
        gameDisplay.clear();
        for (int i = 0; i < gameTitles.size(); i++) {
            gameDisplay.add(gameTitles.get(i) + "," + gameDates.get(i));
            System.out.println(gameDates.get(i));
        }
        listAdapter.clear();
        listAdapter.addAll(gameDisplay);
        listAdapter.notifyDataSetChanged();
    }

    public void sortByTitle(View v) {
        boolean seti = false;
        System.out.println(gameTitles.size());
        for (int i = 0; i < gameTitles.size(); i++) {
            if (seti == true) {
                i = 0;
                seti = false;
            }
            if (i == gameTitles.size() - 1) {
                break;
            }
            if (gameTitles.get(i).compareToIgnoreCase(gameTitles.get(i + 1)) > 0) {
                System.out.println("check");
                System.out.println("switching:" + gameTitles.get(i) + " and " + gameTitles.get(i + 1));
                Collections.swap(gameDates, i, i + 1);
                Collections.swap(gameTitles, i, i + 1);
                seti = true;
            }
        }

//            if(gameTitles.get(0).compareToIgnoreCase(gameTitles.get(1))>0){
//                System.out.println("check");
//                System.out.println("switching:"+gameTitles.get(0)+" and "+gameTitles.get(1));
//                Collections.swap(gameDates, 0,1);
//                Collections.swap(gameTitles,0,1);
//
//            }
        gameDisplay.clear();
        for (int i = 0; i < gameTitles.size(); i++) {
            gameDisplay.add(gameTitles.get(i) + "," + gameDates.get(i));
        }
        listAdapter.clear();
        listAdapter.addAll(gameDisplay);
        listAdapter.notifyDataSetChanged();


    }

    public void backtoMain() {
        Context context = this.getApplicationContext();
        CharSequence text = "No games are saved yet!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }


}
