package com.redcatlabs.corenlpserver;

import java.io.IOException;
import java.io.CharArrayWriter;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.stream.Collectors;

import static spark.Spark.*;

import org.json.simple.JSONValue;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.StringUtils;

public class Main {

    // See : http://sparkjava.com/documentation.html
    private static void runServer(final Properties props_cli, final int server_port) {
        port(server_port);
        
        Map<Properties, StanfordCoreNLP> pipelines = new HashMap<Properties, StanfordCoreNLP>();
        // Loaded by default :
        // props_cli.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,dcoref"); // standard
        // props_cli.setProperty("ner.model", "edu/stanford/nlp/models/ner/english.all.3class.distsim.crf.ser.gz");
        // props_cli.setProperty("ner.model", "edu/stanford/nlp/models/ner/english.muc.7class.distsim.crf.ser.gz");
        // props_cli.setProperty("ner.model", "edu/stanford/nlp/models/ner/english.conll.4class.distsim.crf.ser.gz");
        props_cli.setProperty("ner.model","edu/stanford/nlp/models/ner/english.conll.4class.distsim.crf.ser.gz");
        
        pipelines.put(props_cli, new StanfordCoreNLP(props_cli));  // This map includes the 'cli' props pipeline
        
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
            
            StanfordCoreNLP pipeline_cli = pipelines.get(props_cli);
            Annotation document = new Annotation(txt);
            pipeline_cli.annotate(document);
            
            return JsonUtils.toJsonStanford(pipeline_cli, document); 
        });
        
        // ResponseTransformer  :: To json (for simple-to-serialize objects)
        // get("/hello", (request, response) -> new MyMessage("Hello World"), gson::toJson);

        // See : http://stackoverflow.com/questions/29807175/how-to-ner-and-pos-tag-a-pre-tokenized-text-with-stanford-corenlp
        Properties props_ner = new Properties();
        props_ner.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
        props_ner.setProperty("tokenize.whitespace", "true");
        props_ner.setProperty("ssplit.eolonly", "true");
        // Add something here, or it will load all three default models
        props_ner.setProperty("ner.model","edu/stanford/nlp/models/ner/english.conll.4class.distsim.crf.ser.gz");

        pipelines.put(props_ner, new StanfordCoreNLP(props_ner));  
        
/*
        curl -X POST http://localhost:4567/ner                       \
          -d '{"doc":["Jack and Jill did n'\''t go up the hill .\nHowever , Jill fell down ."],
               "props":{
                 "annotators":"tokenize,ssplit,pos,lemma,ner",
                 "tokenize.whitespace":"true",
                 "ssplit.eolonly":"true"
               }
              }'
*/
        post("/ner", (request, response) -> {  
            JSONObject json = (JSONObject) JSONValue.parse(request.body());
            
            Properties props = props_ner; // Default
            if(json.containsKey("props")) {  // Test here for 'props' hash in POSTed JSON
                props = JsonUtils.jsonToProperties((JSONObject)json.get("props"));
                if(!pipelines.containsKey(props)) {
                    System.out.println("Got new props - adding to cache");
                    pipelines.put(props, new StanfordCoreNLP(props));
                }
            }
            
            StanfordCoreNLP pipeline = pipelines.get(props);
            
            return ((JSONArray)json.get("doc")).stream()
                .map(doc -> {
                    Annotation document = new Annotation((String)doc);
                    pipeline.annotate(document);
                    return JsonUtils.toJsonNERonly(pipeline, document); 
                })
                .collect(Collectors.joining(",\n", "{\"ner\":[\n", "\n]}"));
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
        System.out.println("Processed args");
        runServer(props, server_port);
    }
}
