package com.lme.iudapp.utilidades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lme.iudapp.entidades.Usuario;
import com.lme.iudapp.fragmentos.FragmentoUsuarios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Endpoints{

    private static final String GET_ALL = "http://hello-world.innocv.com/api/user/getall";
    private static final String GET_USER = "http://hello-world.innocv.com/api/user/get";
    private static final String CREATE_USER = "http://hello-world.innocv.com/api/user/create";
    private static final String REMOVE_USER = "http://hello-world.innocv.com/api/user/remove";
    private static final String UPDATE_USER = "http://hello-world.innocv.com/api/user/update";

    private static final int CONNECTED_TO = 10000;
    private static final int READ_TO = 10000;

    public static ArrayList<Usuario> getUsers(FragmentoUsuarios fragmentoUsuarios) {

        try {
            URL url = new URL(GET_ALL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(CONNECTED_TO);
            conn.setReadTimeout(READ_TO);

            int HttpResult = conn.getResponseCode();
            switch (HttpResult){
                case 200:
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            conn.getInputStream(), "utf-8"));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    br.close();

                    conn.disconnect();
                    Type listType = new TypeToken<ArrayList<Usuario>>() {}.getType();
                    return new GsonBuilder().create().fromJson(sb.toString(), listType);
                case 404:
                    fragmentoUsuarios.showAlert(404);
                    break;
                case 500:
                    fragmentoUsuarios.showAlert(500);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            fragmentoUsuarios.showAlert(-1);
        }
        return null;
    }


    public static Usuario getUser(int id, FragmentoUsuarios fragmentoUsuarios){

        try {
            String stringBuilder = GET_USER + "?id=" +
                    URLEncoder.encode(String.valueOf(id), "UTF-8");

            URL url = new URL(stringBuilder);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(CONNECTED_TO);
            conn.setReadTimeout(READ_TO);

            int HttpResult = conn.getResponseCode();
            switch (HttpResult){
                case 200:
                    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    StringBuilder sb = new StringBuilder();
                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        sb.append(inputLine);
                    }

                    conn.disconnect();

                    if(!sb.toString().equals("null")){
                        Gson gson = new Gson();
                        return gson.fromJson(sb.toString(), Usuario.class);
                    }
                    else {
                        fragmentoUsuarios.showAlert(404);
                        break;
                    }
                case 404:
                    fragmentoUsuarios.showAlert(404);
                    break;
                case 500:
                    fragmentoUsuarios.showAlert(500);
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
            fragmentoUsuarios.showAlert(-1);
        }
        return null;
    }


    public static Usuario updateUser(Usuario user, FragmentoUsuarios fragmentoUsuarios){
        try {
            URL url = new URL(UPDATE_USER);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(CONNECTED_TO);
            conn.setReadTimeout(READ_TO);

            Gson gson = new Gson();
            OutputStream os = conn.getOutputStream();
            os.write(gson.toJson(user).getBytes("UTF-8"));
            os.close();

            StringBuilder sb = new StringBuilder();
            int HttpResult = conn.getResponseCode();
            switch (HttpResult){
                case 200:
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            conn.getInputStream(), "utf-8"));
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    br.close();

                    conn.disconnect();
                    return gson.fromJson(sb.toString(), Usuario.class);
                case 404:
                    fragmentoUsuarios.showAlert(404);
                    break;
                case 500:
                    fragmentoUsuarios.showAlert(500);
                    break;
            }

        }catch (IOException e) {
            e.printStackTrace();
            fragmentoUsuarios.showAlert(-1);
        }
        return null;
    }


    public static Usuario createUser(Usuario user, FragmentoUsuarios fragmentoUsuarios){

        try {
            URL url = new URL(CREATE_USER);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(CONNECTED_TO);
            conn.setReadTimeout(READ_TO);

            Gson gson = new Gson();
            OutputStream os = conn.getOutputStream();
            os.write(gson.toJson(user).getBytes("UTF-8"));
            os.close();

            StringBuilder sb = new StringBuilder();
            int HttpResult = conn.getResponseCode();
            switch (HttpResult){
                case 200:
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            conn.getInputStream(), "utf-8"));
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    br.close();

                    conn.disconnect();
                    return gson.fromJson(sb.toString(), Usuario.class);
                case 404:
                    fragmentoUsuarios.showAlert(404);
                    break;
                case 500:
                    fragmentoUsuarios.showAlert(500);
                    break;
            }

        }catch (IOException e) {
            e.printStackTrace();
            fragmentoUsuarios.showAlert(-1);
        }

        return null;
    }


    public static void removeUser(int id, FragmentoUsuarios fragmentoUsuarios){

        try{
            String stringBuilder = REMOVE_USER + "?id=" +
                    URLEncoder.encode(String.valueOf(id), "UTF-8");

            URL url = new URL(stringBuilder);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(CONNECTED_TO);
            conn.setReadTimeout(READ_TO);

            int HttpResult = conn.getResponseCode();
            switch (HttpResult){
                case 200:
                    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    StringBuilder sb = new StringBuilder();
                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        sb.append(inputLine);
                    }

                    conn.disconnect();
                    break;
                case 404:
                    fragmentoUsuarios.showAlert(404);
                    break;
                case 500:
                    fragmentoUsuarios.showAlert(500);
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
            fragmentoUsuarios.showAlert(-1);
        }
    }
}
