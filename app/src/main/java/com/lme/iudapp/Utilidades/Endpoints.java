package com.lme.iudapp.Utilidades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lme.iudapp.entidades.ListaUsuarios;
import com.lme.iudapp.entidades.Usuario;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Endpoints implements Serializable{

    private static final String GET_ALL = "http://hello-world.innocv.com/api/user/getall";
    private static final String GET_USER = "http://hello-world.innocv.com/api/user/get";
    private static final String CREATE_USER = "http://hello-world.innocv.com/api/user/create";
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


    public static Usuario getUser(int id){

        try{
            StringBuilder stringBuilder = new StringBuilder(GET_USER);
            stringBuilder.append("?id=");
            stringBuilder.append(URLEncoder.encode(String.valueOf(id), "UTF-8"));

            URL url = new URL(stringBuilder.toString());
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

    public static Usuario updateUser(Usuario user){
        return null;
    }


    public static Usuario createUser(Usuario user){

        try{
            URL url = new URL(CREATE_USER);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name" , user.getName());
            jsonObject.put("birthdate", user.getBirthdate());

            OutputStream os = conn.getOutputStream();
            os.write(jsonObject.toString().getBytes("UTF-8"));
            //os.write(user.toString().getBytes("UTF-8"));
            os.close();

            StringBuilder sb = new StringBuilder();
            int HttpResult =conn.getResponseCode();
            if(HttpResult ==HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(),"utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                System.out.println(""+sb.toString());

            }else{
                System.out.println(conn.getResponseMessage());
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
            StringBuilder stringBuilder = new StringBuilder(REMOVE_USER);
            stringBuilder.append("?id=");
            stringBuilder.append(URLEncoder.encode(String.valueOf(id), "UTF-8"));

            URL url = new URL(stringBuilder.toString());
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

    private static String getPostDataString(HashMap<String, Integer> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, Integer> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
        }

        return result.toString();
    }


    private static String getPostDataUser(HashMap<String, Usuario> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, Usuario> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
        }

        return result.toString();
    }
}
