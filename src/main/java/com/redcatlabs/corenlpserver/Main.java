package com.redcatlabs.corenlpserver;

import java.io.IOException;
import java.util.Properties;
import java.io.CharArrayWriter;

import static spark.Spark.*;

//ResponseTransformer  :: To json
import com.google.gson.Gson;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.StringUtils;

// See CoreNLP/src/edu/stanford/nlp/pipeline/StanfordCoreNLP.java :489
//import java.io.ByteArrayOutputStream;
//import edu.stanford.nlp.pipeline.JSONOutputter;

public class Main {

    // See : http://sparkjava.com/documentation.html
    private static void runServer(final Properties props, final int server_port) {
        port(server_port);
        
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        
        // Test the basic server operation with http://localhost:4567/ping
        get("/ping", (request, response) -> {
            return "pong";
        });
        
        // Test the parser server with :
        // http://localhost:4567/test?txt="This%20is%20a%20test%20of%20the%20Stanford%20parser."
        get("/test", (request, response) -> {  // for testing
            String txt = request.queryParams("txt");
            if(txt == null) {
                txt = "Hello world.";
            }
            
            Annotation document = new Annotation(txt);
            pipeline.annotate(document);
            
            final CharArrayWriter writer = new CharArrayWriter();
            pipeline.jsonPrint(document, writer);
            
            return writer;
        });
  
        // ResponseTransformer  :: To json (for simple-to-serialize objects)
        // get("/hello", (request, response) -> new MyMessage("Hello World"), gson::toJson);

/*
 * To make this formulation work, looks like we'll have to do our own serialization : 
 * See : http://www.javacreed.com/gson-serialiser-example/
 * and : https://github.com/perwendel/spark/blob/master/README.md
 * 
        Gson gson = new Gson();

        get("/test-json-direct", (request, response) -> {  // for testing
            String text = "This is a test of the Stanford University parser.";
            
            Annotation document = new Annotation(text);
            pipeline.annotate(document);
            
            return document;
        }, json());
*/

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
