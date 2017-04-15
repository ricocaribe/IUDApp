package com.lme.iudapp.utilidades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lme.iudapp.entidades.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Endpoints implements Serializable{

    private static final String GET_ALL = "http://hello-world.innocv.com/api/user/getall";
    private static final String GET_USER = "http://hello-world.innocv.com/api/user/get";
    private static final String CREATE_USER = "http://hello-world.innocv.com/api/user/create";
    private static final String REMOVE_USER = "http://hello-world.innocv.com/api/user/remove";
    private static final String UPDATE_USER = "http://hello-world.innocv.com/api/user/update";


    public static ArrayList<Usuario> getUsers() {

        try {
            URL url = new URL(GET_ALL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(100000);
            conn.setReadTimeout(100000);

            int HttpResult = conn.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

                StringBuilder sb = new StringBuilder();
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }

                conn.disconnect();

                Type listType = new TypeToken<ArrayList<Usuario>>() {
                }.getType();
                return new GsonBuilder().create().fromJson(sb.toString(), listType);
            } else return null;

        } catch (java.io.IOException e) {
            return null;
        }
    }


    public static Usuario getUser(int id){

        try {
            String stringBuilder = GET_USER + "?id=" +
                    URLEncoder.encode(String.valueOf(id), "UTF-8");

            URL url = new URL(stringBuilder);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(100000);
            conn.setReadTimeout(100000);

            int HttpResult = conn.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder sb = new StringBuilder();
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }

                conn.disconnect();

                Gson gson = new Gson();
                return gson.fromJson(sb.toString(), Usuario.class);
            } else return null;

        } catch (java.io.IOException e) {
            return null;
        }
    }


    public static Usuario updateUser(Usuario user){
        try {
            URL url = new URL(UPDATE_USER);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(100000);
            conn.setReadTimeout(100000);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", user.getName());
            jsonObject.put("birthdate", user.getBirthdate());

            OutputStream os = conn.getOutputStream();
            os.write(jsonObject.toString().getBytes("UTF-8"));
            //os.write(user.toString().getBytes("UTF-8"));
            os.close();

            StringBuilder sb = new StringBuilder();
            int HttpResult = conn.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();

                conn.disconnect();
                Gson gson = new Gson();

                return gson.fromJson(sb.toString(), Usuario.class);

            } else return null;

        }catch (java.io.IOException e) {
            return null;
        }catch (JSONException e) {
            return null;
        }
    }


    public static Usuario createUser(Usuario user){

        try {
            URL url = new URL(CREATE_USER);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(100000);
            conn.setReadTimeout(100000);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", user.getName());
            jsonObject.put("birthdate", user.getBirthdate());

            OutputStream os = conn.getOutputStream();
            os.write(jsonObject.toString().getBytes("UTF-8"));
            //os.write(user.toString().getBytes("UTF-8"));
            os.close();

            StringBuilder sb = new StringBuilder();
            int HttpResult = conn.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();

                conn.disconnect();

                Gson gson = new Gson();
                return gson.fromJson(sb.toString(), Usuario.class);
            } else return null;

        }catch (java.io.IOException e) {
            return null;
        }catch (JSONException e) {
            return null;
        }
    }


    public static void removeUser(int id){

        try{
            String stringBuilder = REMOVE_USER + "?id=" +
                    URLEncoder.encode(String.valueOf(id), "UTF-8");

            URL url = new URL(stringBuilder);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(100000);
            conn.setReadTimeout(100000);

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder sb = new StringBuilder();
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }

            conn.disconnect();

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
