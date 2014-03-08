#The hadoopstuff folder is wherever you put your downloaded hadoop stuff

#Put the Java classes in the folder hadoopstuff/clustering_classes
#Put the input files in the folder hadoopstuff/clustering
#Run all these commands from the hadoopstuff folder

javac -classpath ./hadoop-core-1.0.4.jar -d clustering_classes/ ./clustering_classes/CenteroidPair.java

javac -classpath ./hadoop-core-1.0.4.jar:./clustering_classes -d clustering_classes/ ./clustering_classes/MapReduceClusteringStart.java

javac -classpath ./hadoop-core-1.0.4.jar:./clustering_classes -d clustering_classes/ ./clustering_classes/MapReduceClusteringPart2.java

javac -classpath ./hadoop-core-1.0.4.jar:./clustering_classes -d clustering_classes/ ./clustering_classes/MapReduceClustering.java

jar -cvf ./clustering.jar -C clustering_classes/ .

bin/hadoop jar ./clustering.jar org.myorg.MapReduceClustering ./clustering/input/ ./clustering/output/

