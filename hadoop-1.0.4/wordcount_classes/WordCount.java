 	package org.myorg;
 	
 	import java.io.IOException;
 	import java.util.*;
 	
 	import org.apache.hadoop.fs.*;
 	import org.apache.hadoop.conf.*;
 	import org.apache.hadoop.io.*;
	import org.apache.hadoop.mapred.*;
 	import org.apache.hadoop.util.*;
 	
 	public class WordCount {
 	
 	   public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
 	     private final static IntWritable one = new IntWritable(1);
 	     private Text word = new Text();
 	
 	     public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
 	       String line = value.toString();
 	       StringTokenizer tokenizer = new StringTokenizer(line);
 	       while (tokenizer.hasMoreTokens()) {
 	         word.set(tokenizer.nextToken());
 	         output.collect(word, one);
 	       }
 	     }
 	   }
 	
 	   public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
 	     public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
 	       int sum = 0;
 	       while (values.hasNext()) {
 	         sum += values.next().get();
 	       }
 	       output.collect(key, new IntWritable(sum));
 	     }
 	   }
 	
 	   public static void main(String[] args) throws Exception {
		// configuration should contain reference to your namenode
		FileSystem fs = FileSystem.get(new Configuration());
		// true stands for recursively deleting the folder you gave
		fs.delete(new Path(args[1]), true);
 	     JobConf conf = new JobConf(WordCount.class);
 	     conf.setJobName("wordcount");
 	
 	     conf.setOutputKeyClass(Text.class);
 	     conf.setOutputValueClass(IntWritable.class);
 	
 	     conf.setMapperClass(Map.class);
 	     conf.setCombinerClass(Reduce.class);
 	     conf.setReducerClass(Reduce.class);
 	
 	     conf.setInputFormat(TextInputFormat.class);
 	     conf.setOutputFormat(TextOutputFormat.class);
 	
 	     FileInputFormat.setInputPaths(conf, new Path(args[0]));
 	     FileOutputFormat.setOutputPath(conf, new Path("temp1"));
 	
 	     JobClient.runJob(conf);
		boolean temp1Input = true;
		for (int i = 0; i < 6; i++) {
	 	     conf = new JobConf(WordCount.class);
	 	     conf.setJobName("wordcount");
	 	
	 	     conf.setOutputKeyClass(Text.class);
	 	     conf.setOutputValueClass(IntWritable.class);
	 	
	 	     conf.setMapperClass(Map.class);
	 	     conf.setCombinerClass(Reduce.class);
	 	     conf.setReducerClass(Reduce.class);
	 	
	 	     conf.setInputFormat(TextInputFormat.class);
	 	     conf.setOutputFormat(TextOutputFormat.class);
	 		if (temp1Input) {
	 	     		FileInputFormat.setInputPaths(conf, new Path("temp1"));
				fs.delete(new Path("temp2"), true);
	 	     		FileOutputFormat.setOutputPath(conf, new Path("temp2"));
				temp1Input = false;
			} else {
	 	     		FileInputFormat.setInputPaths(conf, new Path("temp2"));
				fs.delete(new Path("temp1"), true);
	 	     		FileOutputFormat.setOutputPath(conf, new Path("temp1"));
				temp1Input = true;
			}
	 	
	 	     JobClient.runJob(conf);
		}

		// true stands for recursively deleting the folder you gave
		fs.delete(new Path(args[1]), true);

	     //Starting the second one
	     conf = new JobConf(WordCount.class);
 	     conf.setJobName("wordcountEnd");
 	
 	     conf.setOutputKeyClass(Text.class);
 	     conf.setOutputValueClass(IntWritable.class);
 	
 	     conf.setMapperClass(Map.class);
 	     conf.setCombinerClass(Reduce.class);
 	     conf.setReducerClass(Reduce.class);
 	
 	     conf.setInputFormat(TextInputFormat.class);
 	     conf.setOutputFormat(TextOutputFormat.class);
 	     if (temp1Input) {
 	     		FileInputFormat.setInputPaths(conf, new Path("temp1"));
		} else {
		 	FileInputFormat.setInputPaths(conf, new Path("temp2"));
		}
 	     FileOutputFormat.setOutputPath(conf, new Path(args[1]));
 	     JobClient.runJob(conf);
		fs.delete(new Path("temp1"), true);
		fs.delete(new Path("temp2"), true);
 	   }
 	}
 	
