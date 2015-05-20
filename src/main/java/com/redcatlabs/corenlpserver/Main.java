package com.redcatlabs.corenlpserver;

import static spark.Spark.*;

//ResponseTransformer  :: To json
import com.google.gson.Gson;

public class Main {

    // See : http://sparkjava.com/documentation.html
    public static void main(String[] args) {
        Gson gson = new Gson();

        // Initial connection will be on http://127.0.0.1:4567/hello
        get("/ping", (request, response) -> {
            return "pong";
        });
        
        post("/parse", (request, response) -> {
            return "NOT YET!";
        });
        
        // ResponseTransformer  :: To json
        // get("/hello", (request, response) -> new MyMessage("Hello World"), gson::toJson);
    }
    
}
