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
 	
public class MapReduceClustering {
 	
	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
 		private final static IntWritable one = new IntWritable(1);
 	        private Text word = new Text();
 	
 	        public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
	 	       String line = value.toString();
	 	       StringTokenizer tokenizer = new StringTokenizer(line);
	 	       String first = tokenizer.nextToken();
		       tokenizer.nextToken();
		       String centeroid = null;
		       int difference = Integer.MAX_VALUE; 
		       ArrayList<String> adjacentNodes = new ArrayList<String>();	 
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				first = first + " " + token;
				adjacentNodes.add(token);			
			}       	
		       for (int i = 0; i < MapReduceClusteringStart.getCenteroids().size(); i++) {
				if (Math.abs(MapReduceClusteringStart.getCenteroids().get(i).getValue() - MapReduceClusteringStart.evaluationFunction(key.toString(), adjacentNodes)) < difference) {
					centeroid = MapReduceClusteringStart.getCenteroids().get(i).getId();
					difference = Math.abs(MapReduceClusteringStart.getCenteroids().get(i).getValue() - MapReduceClusteringStart.evaluationFunction(key.toString(), adjacentNodes));
				}
			}
	 	        output.collect(new Text(centeroid), new Text(first));
 	     	}
 	 }
 	
	public static class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, Text> {
 		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
			ArrayList<Text> newValues = new ArrayList<Text>();
 	        	int sum = 0;
			int count = 0;
 	        	while (values.hasNext()) {
				String currentString = values.next().toString();
 	        		StringTokenizer tokenizer = new StringTokenizer(currentString);
				String node = tokenizer.nextToken();
				String written = node;
				ArrayList<String> adjacentNodes = new ArrayList<String>();
	 	        	while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					adjacentNodes.add(token);
	 	       		}
 	         		sum += MapReduceClusteringStart.evaluationFunction(node, adjacentNodes);
				count++;
				newValues.add(new Text(currentString));
 	        	}
			float average = ((float) sum)/((float) count); 
			String currentCenteroid = null;
			float closest = (float) Integer.MAX_VALUE;
			Iterator<Text> valuesCopy = newValues.iterator();
			ArrayList<Text> toWrite = new ArrayList<Text>();
			while (valuesCopy.hasNext()) {
				String currentString = valuesCopy.next().toString();
 	        		StringTokenizer tokenizer = new StringTokenizer(currentString);
				String node = tokenizer.nextToken();
				String written = node;
				ArrayList<String> adjacentNodes = new ArrayList<String>();
	 	        	while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					written = written + " " + token;
					adjacentNodes.add(token);
	 	       		}
				toWrite.add(new Text(written));
				if (Math.abs(average - MapReduceClusteringStart.evaluationFunction(currentString, adjacentNodes)) < closest) {
					currentCenteroid = node;			
					closest = Math.abs(average - MapReduceClusteringStart.evaluationFunction(currentString, adjacentNodes));
				}
			}
			Iterator<Text> valuesCopy2 = toWrite.iterator();
			while (valuesCopy2.hasNext()) {
 	        		output.collect(new Text(currentCenteroid), valuesCopy2.next());
			}
		}
 	}
 	
	public static void main(String[] args) throws Exception {
		FileSystem fs = FileSystem.get(new Configuration());
		fs.delete(new Path(args[1]), true);
		fs.delete(new Path("temp1"), true);
		fs.delete(new Path("temp2"), true);

		JobConf conf = new JobConf(MapReduceClusteringStart.class);
	 	conf.setJobName("clusteringStart");
	 	
	 	conf.setOutputKeyClass(Text.class);
	 	conf.setOutputValueClass(Text.class);
	 	
	 	conf.setMapperClass(MapReduceClusteringStart.StartMap.class);
	 	conf.setReducerClass(MapReduceClusteringStart.StartReduce.class);
	 	
	 	conf.setInputFormat(TextInputFormat.class);
	 	conf.setOutputFormat(TextOutputFormat.class);
	 	
	 	FileInputFormat.setInputPaths(conf, new Path(args[0]));
	 	FileOutputFormat.setOutputPath(conf, new Path("temp1"));
	 	
	 	JobClient.runJob(conf);
		for (int i = 0; i < 5; i++) {
			conf = new JobConf(MapReduceClustering.class);
			conf.setJobName("clusteringLoop1");
		     
			conf.setOutputKeyClass(Text.class);
		 	conf.setOutputValueClass(Text.class);
		 	
		 	conf.setMapperClass(Map.class);
		 	conf.setReducerClass(Reduce.class);
		 	
		 	conf.setInputFormat(TextInputFormat.class);
		 	conf.setOutputFormat(TextOutputFormat.class);
		 	FileInputFormat.setInputPaths(conf, new Path("temp1"));
			fs.delete(new Path("temp2"), true);
		 	FileOutputFormat.setOutputPath(conf, new Path("temp2"));
		 	
			JobClient.runJob(conf);

			conf = new JobConf(MapReduceClusteringPart2.class);
			conf.setJobName("clusteringLoop2");
		     
			conf.setOutputKeyClass(Text.class);
		 	conf.setOutputValueClass(Text.class);
		 	
		 	conf.setMapperClass(MapReduceClusteringPart2.Map2.class);
		 	conf.setReducerClass(MapReduceClusteringPart2.Reduce2.class);
		 	
		 	conf.setInputFormat(TextInputFormat.class);
		 	conf.setOutputFormat(TextOutputFormat.class);
		 	FileInputFormat.setInputPaths(conf, new Path("temp2"));
			fs.delete(new Path("temp1"), true);
		 	FileOutputFormat.setOutputPath(conf, new Path("temp1"));
		 	
			JobClient.runJob(conf);
		}

		fs.delete(new Path(args[1]), true);

		//Starting the last one
			conf = new JobConf(MapReduceClustering.class);
			conf.setJobName("clusteringLoop1");
		     
			conf.setOutputKeyClass(Text.class);
		 	conf.setOutputValueClass(Text.class);
		 	
		 	conf.setMapperClass(Map.class);
		 	conf.setReducerClass(Reduce.class);
		 	
		 	conf.setInputFormat(TextInputFormat.class);
		 	conf.setOutputFormat(TextOutputFormat.class);
		 	FileInputFormat.setInputPaths(conf, new Path("temp1"));
			fs.delete(new Path("temp2"), true);
		 	FileOutputFormat.setOutputPath(conf, new Path("temp2"));
		 	
			JobClient.runJob(conf);

			conf = new JobConf(MapReduceClusteringPart2.class);
			conf.setJobName("clusteringLoop2");
		     
			conf.setOutputKeyClass(Text.class);
		 	conf.setOutputValueClass(Text.class);
		 	
		 	conf.setMapperClass(MapReduceClusteringPart2.Map2.class);
		 	conf.setReducerClass(MapReduceClusteringPart2.Reduce2.class);
		 	
		 	conf.setInputFormat(TextInputFormat.class);
		 	conf.setOutputFormat(TextOutputFormat.class);
		 	FileInputFormat.setInputPaths(conf, new Path("temp2"));
		 	FileOutputFormat.setOutputPath(conf, new Path(args[1]));
		 	
			JobClient.runJob(conf);
			fs.delete(new Path("temp2"), true);
			fs.delete(new Path("temp1"), true);
	 }
 }
 	
