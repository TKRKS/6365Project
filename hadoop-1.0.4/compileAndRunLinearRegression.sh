#The hadoopstuff folder is wherever you put your downloaded hadoop stuff

#Put the Java classes in the folder hadoopstuff/clustering_classes
#Put the input files in the folder hadoopstuff/clustering
#Run all these commands from the hadoopstuff folder

javac -classpath ./hadoop-core-1.0.4.jar:./linear_regression -d linear_regression/ ./linear_regression/MapReduceLinearRegression.java

jar -cvf ./linearRegression.jar -C linear_regression/ .

bin/hadoop jar ./linearRegression.jar mapReduce.linearRegression.MapReduceLinearRegression ./clustering/input/ ./clustering/output/

