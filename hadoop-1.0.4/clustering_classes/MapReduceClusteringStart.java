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
 	
public class MapReduceClusteringStart {
 	public static ArrayList<CenteroidPair> center = new ArrayList<CenteroidPair>();

        private static final int k = 3;
	private static int counter = 0;

	//Gets starting centeroids
	public static class StartMap extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
 	        private Text word = new Text();
 	
 	        public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
 	        	String line = value.toString();
 	        	StringTokenizer tokenizer = new StringTokenizer(line);
 	          	word.set(tokenizer.nextToken());
			String values = "";
			ArrayList<String> adjacentNodes = new ArrayList<String>();
 	        	while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				values = values + " " + token;
				adjacentNodes.add(token);
 	       		} 	
			if (counter < k) {
				boolean add = true;
				for (int i = 0; i < center.size(); i++) {
					if (center.get(i).getValue().equals(adjacentNodes.size())) {
						add = false;
					}
				}
				if (add) {
					center.add(new CenteroidPair(word.toString(), evaluationFunction(word.toString(), adjacentNodes)));
					counter++;	
				}
			}          		
			output.collect(word, new Text(values));
 	     	}
 	 }
 	
	//Sets centeroids for each key to start
	public static class StartReduce extends MapReduceBase implements Reducer<Text, Text, Text, Text> {
 		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
			String centeroid = null;
			int difference = Integer.MAX_VALUE; 	        	
			String adjacenyList = values.next().toString();
 	        	StringTokenizer tokenizer = new StringTokenizer(adjacenyList);
			ArrayList<String> adjacentNodes = new ArrayList<String>();
 	        	while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				adjacentNodes.add(token);
 	       		} 
			for (int i = 0; i < center.size(); i++) {
				if (Math.abs(center.get(i).getValue() - evaluationFunction(key.toString(), adjacentNodes)) < difference) {
					centeroid = center.get(i).getId();
					difference = Math.abs(center.get(i).getValue() - evaluationFunction(key.toString(), adjacentNodes));
				}
			}
 	        	output.collect(key, new Text(centeroid + " " + adjacenyList));
		}
 	}

	public static int evaluationFunction(final String text, final ArrayList<String> adjacentNodes) {
		return adjacentNodes.size();
	}

	public static ArrayList<CenteroidPair> getCenteroids() {
		return center;
	}

	public static int getK() {
		return k;
	}
 }
 	
