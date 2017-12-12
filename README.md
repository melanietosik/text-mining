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

### Run

```
$ javac -d bin -sourcepath src src/pa/tfidf/Pipeline.java && java pa.tfidf.Pipeline data
```

### Output

```
$ javac -d bin -sourcepath src src/pa/tfidf/Pipeline.java && java pa.tfidf.Pipeline data 2> /dev/null
Processing documents...
C1
C10
C11
C12
C13
C14
C15
C2
C3
C4
C5
C6
C7
C8
C9
Sliding window...
Generating n-grams...
3
2
1
Calculating TF-IDF scores...
Generating topics...
Done
java pa.tfidf.Pipeline data 2> /dev/null  80.39s user 1.51s system 145% cpu 56.317 total
```

## K-Means clustering and document similarity

### Run

```
$ javac -d bin -sourcepath src src/pa/kmeans/Pipeline.java && java pa.kmeans.Pipeline
```

### Output

```
$ javac -d bin -sourcepath src src/pa/kmeans/Pipeline.java && java pa.kmeans.Pipeline
Document similarity...
C8_article01
C8_article05
C8_article10
C8_article04
C8_article08
Dimensionality reduction (SVD)...
K-Means...
Centroids [99, 35, 28, 112, 16, 72, 46, 111, 56, 7, 67, 44, 20, 84, 108]
Converged at epoch 12
[4, 4, 9, 9, 4, 4, 4, 4, 8, 8, 8, 8, 8, 8, 8, 8, 0, 4, 4, 4, 12, 12, 0, 12, 14, 12, 0, 0, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 14, 14, 14, 11, 11, 6, 6, 6, 6, 6, 6, 6, 6, 3, 3, 0, 0, 3, 0, 0, 0, 0, 0, 9, 7, 7, 10, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 13, 13, 13, 13, 0, 13, 13, 13, 2, 0, 2, 14, 2, 0, 0, 0, 2, 0, 14, 14, 14, 14, 14, 14, 14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
```

### Generate plots

```
$ cat resources/labels_kmeans.txt| awk -F,  '{print $1 "," $2}' | paste -d, - resources/labels.txt > resources/labels_original.txt
$ python plots/plot.py resources/labels_original.txt resources/labels_kmeans.txt
```

## K-nearest neighbors

### Run

```
$ javac -d bin -sourcepath src src/pa/knn/Pipeline.java && java pa.knn.Pipeline data_test/2/a8.txt data_test 2> /dev/null
```

### Output

```
Processing input document...
Computing 3-nearest neighbors/topics...
 C14_article03, predictive_analytics
 C14_article04, predictive_analytics
 C14_article05, predictive_analytics
Majority label: predictive_analytics
Cross-validation...
k=1
k=2
k=3
k=4
k=5
k=6
k=7
k=8
k=9
k=10
k=11
k=12
k=13
k=14
k=15
Average accuracies: {1=0.9666666666666666, 2=0.9416666666666667, 3=0.9333333333333333, 4=0.9333333333333333, 5=0.9250000000000002, 6=0.9083333333333334, 7=0.9250000000000002, 8=0.9166666666666667, 9=0.9, 10=0.8583333333333332, 11=0.8583333333333336, 12=0.8416666666666668, 13=0.8333333333333334, 14=0.825, 15=0.8166666666666668}
Best: k=1
Processing test data...
1
2
Generating confusion matrix...
   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0
   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0
   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0
   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0
   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0
   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0
   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0
   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0
   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0
   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0
   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0
   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0
   0   0   0   0   0   0   0   0   0   0   0   0   0   0   0
   0   0   0   0   0   0   0   0   0   0   0   0   0  10   0
   0   0   0   0   0   0   0   0   0   0   0   0   0   0  10
java pa.knn.Pipeline data_test/2/a8.txt data_test 2> /dev/null  24.32s user 1.02s system 161% cpu 15.716 total
```
