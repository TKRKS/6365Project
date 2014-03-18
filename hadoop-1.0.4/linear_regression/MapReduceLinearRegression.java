package mapReduce.linearRegression;

import java.io.IOException;
import java.util.*;
import java.lang.Math;
 	
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
 	
public class MapReduceLinearRegression {
 	
	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, DoubleWritable> {
 		private final static IntWritable one = new IntWritable(1);
 	        private Text word = new Text();
 	
 	        public void map(LongWritable key, Text value, OutputCollector<Text, DoubleWritable> output, Reporter reporter) throws IOException {
			String line = value.toString();
	 		StringTokenizer tokenizer = new StringTokenizer(line, ",");
	 		String predictedValueString = tokenizer.nextToken();
			double predictedValue = Double.parseDouble(predictedValueString);
			tokenizer.nextToken();
			String centeroid = null;
			double sum = 0.0;
			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				sum += Float.parseFloat(token);			
			}
			double difference = predictedValue - sum;
			double regressionValue = Math.pow(difference, 2.0);
			
			output.collect(new Text("Sqaured Difference"), new DoubleWritable(regressionValue));
 	 	}
	}
 	
	public static class Reduce extends MapReduceBase implements Reducer<Text, DoubleWritable, Text, DoubleWritable> {
 		public void reduce(Text key, Iterator<DoubleWritable> values, OutputCollector<Text, DoubleWritable> output, Reporter reporter) throws IOException {
			double finalValue = 0.0;
			while (values.hasNext()) {
				finalValue += values.next().get();
			}
			output.collect(new Text("Final Value:"), new DoubleWritable(finalValue));
 		}
	}
 	
	public static void main(String[] args) throws Exception {
		FileSystem fs = FileSystem.get(new Configuration());
		fs.delete(new Path(args[1]), true);

		JobConf conf = new JobConf(MapReduceLinearRegression.class);
	 	conf.setJobName("Linear Regression");
	 	
	 	conf.setOutputKeyClass(Text.class);
	 	conf.setOutputValueClass(DoubleWritable.class);
	 	
	 	conf.setMapperClass(MapReduceLinearRegression.Map.class);
	 	conf.setReducerClass(MapReduceLinearRegression.Reduce.class);
	 	
	 	conf.setInputFormat(TextInputFormat.class);
	 	conf.setOutputFormat(TextOutputFormat.class);
	 	
	 	FileInputFormat.setInputPaths(conf, new Path(args[0]));
	 	FileOutputFormat.setOutputPath(conf, new Path(args[1]));
	 	
	 	JobClient.runJob(conf);
	 }
 }
 	
