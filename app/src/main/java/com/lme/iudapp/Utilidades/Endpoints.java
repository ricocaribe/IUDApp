package com.lme.iudapp.Utilidades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lme.iudapp.entidades.ListaUsuarios;
import com.lme.iudapp.entidades.Usuario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Endpoints implements Serializable{

    private static final String GET_ALL = "http://hello-world.innocv.com/api/user/getall";
    private static final String GET_USER = "http://hello-world.innocv.com/api/user/get";
    private static final String REMOVE_USER = "http://hello-world.innocv.com/api/user/remove";


    public static ArrayList<Usuario> getUsers(){

        try{
            URL url = new URL(GET_ALL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            StringBuilder sb = new StringBuilder();
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }

            conn.disconnect();

            Type listType = new TypeToken<ArrayList<Usuario>>(){}.getType();
            return new GsonBuilder().create().fromJson(sb.toString(), listType);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static Usuario alterUser(int id){

        try{
            URL url = new URL(GET_USER + "{" + id + "}");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            StringBuilder sb = new StringBuilder();
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }

            conn.disconnect();

            Gson gson = new Gson();
            return gson.fromJson(sb.toString(), Usuario.class);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static Usuario removeUser(int id){

        try{
            URL url = new URL(REMOVE_USER + "/{" + id + "}");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            StringBuilder sb = new StringBuilder();
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }

            conn.disconnect();

            Gson gson = new Gson();
            return gson.fromJson(sb.toString(), Usuario.class);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
