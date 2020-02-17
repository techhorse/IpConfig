package com.example.abhishek.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {

    private Button btnScan;
    private ListView listViewNI;

    ArrayList<String> niList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan = (Button) findViewById(R.id.scan);
        listViewNI = (ListView) findViewById(R.id.listviewni);


        niList = new ArrayList();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, niList);
        listViewNI.setAdapter(adapter);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ScanNITask().execute();
            }
        });
    }


    private class ScanNITask extends AsyncTask<Void, String, Void> {

        @Override
        protected void onPreExecute() {
            niList.clear();
            adapter.notifyDataSetInvalidated();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface thisInterface = networkInterfaces.nextElement();
                    Enumeration<InetAddress> inetAddresses = thisInterface.getInetAddresses();
                    String niInfo = "";
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress thisAddress = inetAddresses.nextElement();
                        niList.add(thisAddress.getCanonicalHostName());
                    }

                    publishProgress(niInfo);


                }
            } catch (SocketException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            adapter.notifyDataSetInvalidated();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_LONG).show();
        }
    }
}