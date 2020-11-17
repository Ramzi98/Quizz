package com.example.quizz.Model.DataBase;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

//Class converters pour converter arraylist des proposition vers des objets Json et le sens invers
public class Converters {
    @TypeConverter
    public static ArrayList<String> fromString(String value)
    {
        //récuperation d'objet Arraylist et le transformer en Json puis le retourner
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
    @TypeConverter public static String fromArrayList(ArrayList<String> list)
    {
        //récuperation d'objet Json et le transformer en String puis le retourner
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}

