# Text mining in Java

## Environment

### Java

```
$ mkdir bin
$ export CLASSPATH=bin/pa/tfidf/:bin/pa/kmeans/:bin/pa/knn/:bin:lib/*:.
```

### Python

```
$ virtualenv -p python3.6 env/
$ source env/bin/activate
$ pip install -r plots/requirements.txt
```

## TF-IDF and document topics

```
$ javac -d bin -sourcepath src src/pa/tfidf/Pipeline.java && java pa.tfidf.Pipeline <data_folder> 2> /dev/null
```

## K-Means clustering and document similarity

```
$ javac -d bin -sourcepath src src/pa/kmeans/Pipeline.java && java pa.kmeans.Pipeline
```

### Generate plots

```
$ cat resources/labels_kmeans.txt| awk -F,  '{print $1 "," $2}' | paste -d, - resources/labels.txt > resources/labels_original.txt
$ python plots/plot.py resources/labels_original.txt resources/labels_kmeans.txt
```

## K-nearest neighbors

```
$ javac -d bin -sourcepath src src/pa/knn/Pipeline.java && java pa.knn.Pipeline 2> /dev/null
```





