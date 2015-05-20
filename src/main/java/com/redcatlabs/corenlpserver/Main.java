package com.redcatlabs.corenlpserver;

import static spark.Spark.*;

//ResponseTransformer  :: To json
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Properties;
import java.io.ByteArrayOutputStream;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.StringUtils;

public class Main {

    // See : http://sparkjava.com/documentation.html
    private static void runServer(final Properties props, final int server_port) {
        port(server_port);
        
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        
        Gson gson = new Gson();

        // Initial connection will be on http://127.0.0.1:4567/hello
        get("/ping", (request, response) -> {
            return "pong";
        });
        
        get("/parse", (request, response) -> {  // for testing
        //post("/parse", (request, response) -> {
            String text = "This is a test of the parser.";
            
            Annotation document = new Annotation(text);
            pipeline.annotate(document);
            
            //final ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            //pipeline.jsonPrint(document, ostream);
            
            //return response.body(ostream);
            //return document;
            
            return document;
        }, gson::toJson);
        
        // ResponseTransformer  :: To json
        // get("/hello", (request, response) -> new MyMessage("Hello World"), gson::toJson);
        
        // CoreNLP also has : public void jsonPrint(Annotation annotation, Writer w) throws IOException

    }
    
    public static void main(String[] args) throws IOException {
        Properties props = null;
        
        int server_port = 4567;  // This is SparkJava's default

        if (args.length > 0) {
            props = StringUtils.argsToProperties(args);
            
            final String portString = props.getProperty("port");
            if (portString != null) {
                server_port = Integer.parseInt(portString);
            }
        }
        runServer(props, server_port);
    }
}
