#! /usr/bin/bash

curl -X POST http://localhost:4567/ner                         \
  -d '{"doc":["Jack and Jill did nt go up the hill .\nHowever , Jill fell down ."], 
       "props":{
         "annotators":"tokenize,ssplit,pos,lemma,ner",
         "tokenize.whitespace":"true",
         "ssplit.eolonly":"true"
       }
      }'


#         "annotators":"tokenize,ssplit,pos,lemma,ner,parse",
#         "annotators":"tokenize,ssplit,pos,lemma,ner",
# \nHowever , Jill fell down .


