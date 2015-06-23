#! /usr/bin/bash


# Note the use of '\'' to create a single <'> inside the 'quoted' "string"

curl -X POST http://localhost:4567/ner                         \
  -d '{"doc":["Jack Sprat and Jill did n'\''t go up the hill .\nHowever , Jill fell down ."]}'

curl -X POST http://localhost:4567/ner                         \
  -d '{"doc":["Jack Sprat and Jill did n'\''t go up the hill .\nHowever , Jill fell down ."], 
       "props":{
         "annotators":"tokenize,ssplit,pos,lemma,ner",
         "tokenize.whitespace":"true",
         "ssplit.eolonly":"true",
         "ner.model":"edu/stanford/nlp/models/ner/english.conll.4class.distsim.crf.ser.gz"
       }
      }'

#         "annotators":"tokenize,ssplit,pos,lemma,ner,parse",





