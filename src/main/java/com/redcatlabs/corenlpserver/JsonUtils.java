package com.redcatlabs.corenlpserver;

import java.io.IOException;
import java.io.CharArrayWriter;
import java.util.stream.Collectors;

import spark.ResponseTransformer;


import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.Annotation;

import edu.stanford.nlp.ling.CoreAnnotations;

//ResponseTransformer  :: To json
//import com.google.gson.Gson;

/* 
 * To make a clean Json response, we'll have to do our own serialization
 * or, rely on the JsonAnnotator that Stanford helpfully provides...
 *
 * See : http://www.javacreed.com/gson-serialiser-example/
 * and : https://github.com/perwendel/spark/blob/master/README.md
*/

public class JsonUtils {
    // See also : CoreNLP/src/edu/stanford/nlp/pipeline/StanfordCoreNLP.java :489
    //   import java.io.ByteArrayOutputStream;
    //   import edu.stanford.nlp.pipeline.JSONOutputter;

    public static CharArrayWriter toJsonStanford(StanfordCoreNLP pipeline, Annotation document) throws IOException {
        final CharArrayWriter writer = new CharArrayWriter();
        pipeline.jsonPrint(document, writer);
        return writer;
    }
    
    public static String toJson(Object o) throws IOException {
        return "Not implemented";
        //return new Gson().toJson(object);
    }
 
    public static ResponseTransformer json() {
        return JsonUtils::toJson;
    }

    // CoreNLP/src/edu/stanford/nlp/pipeline/JSONOutputter.java
    // https://github.com/stanfordnlp/CoreNLP/blob/eafbd9a9ddd6b06a0ed2a1b979b60f532b9be3cc/src/edu/stanford/nlp/pipeline/JSONOutputter.java
    public static String toJsonNERonly(StanfordCoreNLP pipeline, Annotation document) throws IOException {
        String as_str = "";
        if (document.get(CoreAnnotations.TokensAnnotation.class) != null) {
            as_str = document.get(CoreAnnotations.TokensAnnotation.class).stream()
                .map(token -> {
                    return "["+token.ner()+"]";
                }).collect(Collectors.joining(","));
/*                
                .map(token -> {
                    try {
                        writer.write("["+token.ner()+"]");
                    } catch(IOException e) {}
*/
        }
        //pipeline.jsonPrint(document, writer);
        return as_str;
    }
}
