package com.redcatlabs.corenlpserver;

import java.io.IOException;
import java.io.CharArrayWriter;

import spark.ResponseTransformer;

//ResponseTransformer  :: To json
//import com.google.gson.Gson;

// See CoreNLP/src/edu/stanford/nlp/pipeline/StanfordCoreNLP.java :489
//import java.io.ByteArrayOutputStream;
//import edu.stanford.nlp.pipeline.JSONOutputter;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.StringUtils;

/* 
 * To make a clean Json response, we'll have to do our own serialization
 * or, rely on the JsonAnnotator that Stanford helpfully provides...
 *
 * See : http://www.javacreed.com/gson-serialiser-example/
 * and : https://github.com/perwendel/spark/blob/master/README.md
*/

public class JsonUtils {
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

}
