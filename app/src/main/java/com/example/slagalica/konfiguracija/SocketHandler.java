package com.example.slagalica.konfiguracija;

import android.util.Log;

import com.example.slagalica.BuildConfig;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketHandler {

    static Socket socket;

    public static void setSocket(){
        try {
            socket = IO.socket("http://" + "192.168.170.96" + ":3000");
        }catch (Exception e){
            Log.println(Log.ERROR, "Error while connecting to socket", e.getMessage());
        }
    }

    public static Socket getSocket(){
        return socket;
    }
}
