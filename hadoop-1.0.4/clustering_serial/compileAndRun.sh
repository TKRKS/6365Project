javac -d . ./CenteroidPair.java 

javac -classpath . -d . ./ClusteringSerialMain.java

java -Xmx1024m ClusteringSerial.ClusteringSerialMain ./input/links-simple-sorted.txt ./output/output.txt
