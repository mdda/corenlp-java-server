package com.redcatlabs.corenlpserver;

import java.io.IOException;
import java.util.Properties;
import java.io.CharArrayWriter;

import static spark.Spark.*;

import org.json.simple.JSONValue;
import org.json.simple.JSONObject;
//import org.json.simple.JSONArray;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.StringUtils;

public class Main {

    // See : http://sparkjava.com/documentation.html
    private static void runServer(final Properties props, final int server_port) {
        port(server_port);
        
        StanfordCoreNLP pipeline_cli = new StanfordCoreNLP(props);
        
        Properties props_ner = new Properties();
        // props_ner.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref"); // Works
        props_ner.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse"); // Must have 'parse' in it
        StanfordCoreNLP pipeline_ner = new StanfordCoreNLP(props_ner);
        
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
            pipeline_cli.annotate(document);
            
            final CharArrayWriter writer = new CharArrayWriter();
            pipeline_cli.jsonPrint(document, writer);
            
            return writer;
        });
  
/*            
            final CharArrayWriter writer = new CharArrayWriter();
            pipeline_cli.jsonPrint(document, writer);
            
            return writer;
        });
*/
        
        // ResponseTransformer  :: To json (for simple-to-serialize objects)
        // get("/hello", (request, response) -> new MyMessage("Hello World"), gson::toJson);

        // curl -X POST http://localhost:4567/ner -d '{"txt":"I went to London."}'
        post("/ner", (request, response) -> {  
            JSONObject json = (JSONObject) JSONValue.parse(request.body());
            
            String txt = (String) json.get("txt");
            if(txt == null) {
                txt = "Post a txt field to make this work.";
            }
            
            Annotation document = new Annotation(txt);
            pipeline_ner.annotate(document);
            
            final CharArrayWriter writer = new CharArrayWriter();
            pipeline_ner.jsonPrint(document, writer);
            
            return writer;
        });
  
        // We're assuming all json responses
        after((req, res) -> {
            res.type("application/json");
        });
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
