package com.example.hackathon2019;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class ConnectActivity {

    private static Socket s = null;
    private String IP = "10.27.254.160"; //"10.27.248.205";//
    private int PORT = 53312;

    private String res = "NULL";

    public ConnectActivity(String name) {
        startConnection();
        Log.i("pitest", "Connected to server");

        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //(new Thread(new sendDataThread("balls"))).start();
        (new Thread(new waitForResponseThread())).start();

        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        (new Thread(new sendDataThread("name " + name))).start();
    }

    public class connectThread implements Runnable {

        @Override
        public void run() {
            connectToServer();
        }
    }

    public void connectToServer() {
        try {
            s = new Socket();
            s.connect(new InetSocketAddress(IP, PORT), 1000);
            //Send username
        } catch (IOException e) {

        }
    }

    public static class sendDataThread implements Runnable {
        private String data;
        public sendDataThread(String data) {
            this.data = data;
        }

        @Override
        public void run() {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendData(data);
        }
    }

    public static boolean sendData(String data) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));
            out.println(data);
            out.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public class waitForResponseThread implements Runnable {
        @Override
        public void run() {
            Log.i("pitest", "STARTING WAIT");
            while (true) {
                res = waitForResponse();
                Log.i("pitest", res);
            }
        }
    }

    public String getRes() {
        if (!res.equals("NULL")) {
            String ret = "" + res;
            res = "NULL";

            return ret;
        }

        return res;
    }

    public String waitForResponse() {
        try
        {
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            //out.println(json);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String response = in.readLine();

            //Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
            //socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e("pitest", "FAILED: DEFAULTING");
        return "NULL";
    }

    public void startConnection() {
        (new Thread(new connectThread())).start();
    }

}
