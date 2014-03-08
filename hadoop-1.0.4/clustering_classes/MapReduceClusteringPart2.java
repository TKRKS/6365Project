package org.myorg;

import java.io.IOException;
import java.util.*;
import java.lang.Math;
 	
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

import org.myorg.CenteroidPair;
import org.myorg.MapReduceClusteringStart;
 	
public class MapReduceClusteringPart2 {

        private static final int k = 3;

	//Gets starting centeroids
	public static class Map2 extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
 	
 	        public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
	 	       String line = value.toString();
	 	       StringTokenizer tokenizer = new StringTokenizer(line);
	 	       String first = tokenizer.nextToken();
		       String second = "";
	 	       while (tokenizer.hasMoreTokens()) {
				second = second + " " + tokenizer.nextToken();
	 	       }
	 	       output.collect(new Text(first), new Text(second));
 	     	}
 	 }
 	
	//Sets centeroids for each key to start
	public static class Reduce2 extends MapReduceBase implements Reducer<Text, Text, Text, Text> {
 		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
			while (values.hasNext()) {
				String second = values.next().toString();
				StringTokenizer tokenizer = new StringTokenizer(second);
				String node = tokenizer.nextToken();
				String adjacent = "";
				while (tokenizer.hasMoreTokens()) {
					adjacent = adjacent + " " + tokenizer.nextToken();
				}
				String outputString = key.toString() + " " + adjacent;
 	        		output.collect(new Text(node), new Text(outputString));
			}
		}
 	}
 }
 	
