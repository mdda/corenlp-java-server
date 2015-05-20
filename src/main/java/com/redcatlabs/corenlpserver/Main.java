package com.redcatlabs.corenlpserver;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        // Initial connection will be on http://127.0.0.1:4567/hello
        get("/hello", (request, response) -> "Hello World!");
    }
    
}
