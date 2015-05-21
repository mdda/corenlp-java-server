# corenlp-java-server
Simple SparkJava wrapper for the Stanford CoreNLP parser

## Rationale

In order to 'play' with [CoreNLP](http://nlp.stanford.edu/software/corenlp.shtml) 
effectively, it makes sense to leave it running as a standalone process, 
and query it using a simple REST API.

One issue with the existing wrappers in other languages is that they 
don't seem to offer much control over the pipeline being used.  Often they
just launch a command-line instance of CoreNLP, and 'what you get is what you get'.

Therefore, this project is written in Java, so that the pipeline can
be customized per call, rather than per restart.

However, rather than get *involved* with writing extensively in Java, this project
takes the simplest approach possible to getting the REST server up-and-running
(using [SparkJava](http://sparkjava.com/documentation.html), 
not to be confused with [Apache-Spark](https://spark.apache.org/)).

## Goal

It should be simple to add to this project to create additional routes
as required, and still be able to 'natively' control the way in which CoreNLP
interacts with the text you supply.


## Running

This project is built using ```sbt``` - which I prefer due to my 
Scala tendencies.  Note, however, that it contains no scala code 
itself.  If necessary, it should be easy to create a ```pom.xml``` file 
for maven, etc.

To compile (which will take some time initially, since the CoreNLP 
class files must be downloaded, and they are ~150Mb) just :
```
sbt
### This is now an interactive session
> compile

### or, for on-file-change recompilation:
> ~ compile
```

Running is then simply (at the command line): 
```
sbt run
```

The server should be visible at [http://localhost:4567/ping](http://localhost:4567/ping).

And an example parse via [http://localhost:4567/test?txt="This is a test of the Stanford parser."](http://localhost:4567/test?txt="This is a test of the Stanford parser.") ,
where you may need to replace the spaces in the txt string as '%20'.

## Requirements

Needs ```java``` and ```sbt``` (unless someone else wants to suggest a ```pom.xml```).

On Fedora, these can be installed with : 

```
yum install java-1.8.0-openjdk-devel sbt
```


## License

Since this embeds the GPL 2(+) CoreNLP project, all modifications and 
extensions will also be GPL 2(+).

Note that simply using this as a REST API service doesn't mean a client 
using the HTTP API must be GPL.  If that were Stanford's intention, 
then the CoreNLP project itself would be (for instance) AGPL licensed.

