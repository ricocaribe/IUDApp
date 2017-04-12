package com.lme.iudapp.Utilidades;

import com.google.gson.Gson;
import com.lme.iudapp.entidades.ListaUsuarios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Endpoints implements Serializable{

    private static final String GETALL = "http://http://hello-world.innocv.com/api/user/getall";

    /**
     * vsdfsdag
     * @param
     * @return
     * @throws IOException en caso de que falle la llamada al servidor
     */
        public ListaUsuarios getUsers() throws IOException{

        URL url = new URL(GETALL);
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
        return gson.fromJson(sb.toString(), ListaUsuarios.class);
    }
}
