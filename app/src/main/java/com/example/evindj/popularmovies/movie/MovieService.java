package com.example.evindj.popularmovies.movie;

import com.facebook.stetho.json.annotation.JsonValue;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by evindj on 12/3/15.
 */
public  class MovieService {
    private String themoApiKey;
    private String sortOrder;
    private String baseUrl ;
    private static  MovieService singleService = new MovieService();
    private String queryUrl;
    private MovieService(){
        themoApiKey ="ENTER YOUR KEY HERE";
        sortOrder = "popularity.desc";
        baseUrl = "http://api.themoviedb.org/3/discover/movie?";
    }
    private String buildUrl(){
        return  queryUrl;
    }

    private String sendGet() throws IOException{
        URL url = new URL(buildUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if(responseCode==HttpURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            inputLine = in.readLine();
            while(inputLine !=null){
                response.append(inputLine);
                response.append("\n");
                inputLine = in.readLine();
            }
            in.close();
            return  response.toString();
        }
        else{
            //request failed.
            return  "";
        }
    }


    private ArrayList<Movie> parseJSON(String jsonString){
        JsonObject jsonObject ;
        try{
            JsonElement jsonElement =  new JsonParser().parse(jsonString);
            jsonObject = jsonElement.getAsJsonObject();
            JsonArray results = jsonObject.getAsJsonArray("results");
            ArrayList<Movie> moviesList = new ArrayList<>();
            for(int i =0; i<results.size();i++){
                JsonElement childElt = results.get(i);
                JsonObject child = childElt.getAsJsonObject();

                Movie movie = new Movie();
                movie.setId(child.get("id").getAsInt());
                movie.setTitle(child.get("original_title").getAsString());
                if(!child.get("poster_path").isJsonNull())
                    movie.setThumbnail(child.get("poster_path").getAsString());
                else
                    movie.setThumbnail("/orH9Zw6EXeOkNoWQqUwAj28Zfz2.jpg");
                movie.setRating(child.get("vote_average").getAsInt());
                if(!child.get("overview").isJsonNull())
                    movie.setPlotAnalysis(child.get("overview").getAsString());
                else
                    movie.setPlotAnalysis("no Analysis availlable");
                if(!child.get("release_date").isJsonNull())
                    movie.setReleaseDate(child.get("release_date").getAsString());
                else
                    movie.setReleaseDate("0000-00-00");
                moviesList.add(movie);
            }
            return  moviesList;
        }
        catch (Exception e){
            e.getMessage();
            e.printStackTrace();
            return  null;
        }

    }
    public static  MovieService getInstance(){
        return  singleService;
    }

    public  List getMovies( SortOrder sortOrder){
        queryUrl = baseUrl+"sort_by="+this.sortOrder+"&api_key="+themoApiKey;
        if(sortOrder == SortOrder.HIGHESTRATED)
            this.sortOrder ="vote_average.desc";
        else
            this.sortOrder="popularity.desc";
        try{
            String response = sendGet();
           ArrayList<Movie> ret = parseJSON(response);
            return  ret;

        }
        catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }

}
