# Text mining in Java

## Environment

```
$ mkdir bin
$ export CLASSPATH=bin/pa/tfidf/:bin/pa/kmeans/:bin:lib/*:.
```

## TF-IDF and document topics

```
$ javac -d bin -sourcepath src src/pa/tfidf/Pipeline.java && java pa.tfidf.Pipeline <data_folder> 2> /dev/null
```

## K-Means clustering and document similarity

```
$ javac -d bin -sourcepath src src/pa/kmeans/Pipeline.java && java pa.kmeans.Pipeline
```

