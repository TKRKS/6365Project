The included files for our project:
4-ProjectReport.pptx - A copy of our project report powerpoint
code.zip - All of our code (CentroidPair.java, MapReduceClustering.java, MapReduceClustering2.java, MapReduceClusteringStart.java, compileAndRun.sh and resize.py)
data.zip - All of our included data (testFile01.txt, testFile02.txt and twitterSSSSS.out)
README.txt - This explaination document
project.zip - Everything together in a hadoop folder ready to run


Here is a list of the important files included in project.zip:

./4-ProjectReport.pptx - The Project Report for our project
./bin/hadoop - The hadoop executable
./clustering/input - This folder contains all the input for running hadoop
./clustering_classes - The folder with our actual search code
./clustering_classes/CentroidPair.java - Helper class to store centeroid information
./clustering_classes/MapReduceClustering.java - Has the main function in addition to the first part of the 2 part MapReduce loop
./clustering_classes/MapReduceClustering2.java - The second part of the 2 part MapReduce loop
./clustering_classes/MapReduceClusteringStart.java - The first MapReduce that is run to set the starting clusters and read the first input
./data/testFile01 - The first test file we created to run our code on (used in combination with testFile02)
./data/testFile02 - The second test file we created to run our code on (used in combination with testFile01)
./data/twitterSSSSS.out - The 662.9 MB file of preprocessed Twitter Data
./hadoop-core-1.0.4.jar - The jar used to run the Hadoop code
./README.txt - This file with helpful info
./resize.py - Code used to preproces the original data file
./compileAndRun.sh - A script to compile all the java files and run the code.  It assumes the following layout:
  hadoop
	bin
		hadoop
	clustering
		input
			(whatever files are to be input to the MapReduce Clustering algorithm)
	clustering_classes
		CentroidPair.java
		MapReduceClustering.java
		MapReduceClustering2.java
		MapReduceClusteringStart.java
	compileAndRun.sh
	hadoop-core-1.0.4.jar
	resize.py

If this is the layout of the items (as it is in the hadoop-1.0.4 folder in project.zip), do this to run the code:

1.  Copy input of choice from ./data to ./clustering/input
2.  Run ./compileAndRun.sh
3.  View output in ./clustering/output

The output format is:

node clusterCentroid spacedListOfOutEdges

So if a line say:

Node1 Node1 Node2 Node3

It means Node1 is in the Node1 cluster and has the outedges Node2 and Node3



Here is how to run the preprocessor:

python resize.py [inputfile] [outputfile] [max_connection_count] [optional:min_connection_count]


Because the base Twitter data is so big (about 26 GB), we are turning it in sepearately to the professor.  We have included one of the smaller preprocessed files (./data/twitterSSSSS.out) for testing purpose.


If hadoop is not running correctly, try following the instructions here:

http://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/SingleCluster.html
