package com.redcatlabs.corenlpserver;

import java.io.CharArrayWriter;

//ResponseTransformer  :: To json
//import com.google.gson.Gson;

// See CoreNLP/src/edu/stanford/nlp/pipeline/StanfordCoreNLP.java :489
//import java.io.ByteArrayOutputStream;
//import edu.stanford.nlp.pipeline.JSONOutputter;

import edu.stanford.nlp.pipeline.Annotation;

/* 
 * To make a clean Json response, we'll have to do our own serialization
 * or, rely on the JsonAnnotator that Stanford helpfully provides...
 *
 * See : http://www.javacreed.com/gson-serialiser-example/
 * and : https://github.com/perwendel/spark/blob/master/README.md
*/

public class JsonUtil {
 
  //public static String toJsonUsingStanfordJsonAnnotator(Annotation doc) {
  public static CharArrayWriter toJsonUsingStanfordJsonAnnotator(Annotation doc) {
    final CharArrayWriter writer = new CharArrayWriter();
    pipeline_cli.jsonPrint(document, writer);
    
    return writer;
    
    //return new Gson().toJson(object);
  }
 
  public static ResponseTransformer json_stanford() {
    return JsonUtil::toJsonUsingStanfordJsonAnnotator;
  }
}
