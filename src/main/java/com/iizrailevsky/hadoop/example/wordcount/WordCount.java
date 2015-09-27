package com.iizrailevsky.hadoop.example.wordcount;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/**
 * Hello world! Count words in given text files.
 *
 */
public class WordCount
{
    /**
     * Mapper class
     */
	public static class WordCountMap extends Mapper<LongWritable, Text, Text, IntWritable> {
		private static final IntWritable one = new IntWritable(1);
		private Text word = new Text();

		/**
		 * Breaks up the given line into words and maps each word (key) to 1 (value).
		 * @param key Line number
		 * @param value Line contents
		 * @param context Output context to write key-value pairs to
		 */
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			String[] words = line.split("[\\p{Punct}\\s]+");
			for (String w: words) {
				word.set(w);
				context.write(word, one);
			}
		}
	}

	/**
	 * Reducer class
	 */
	public static class WordCountReduce extends Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable sumTotal = new IntWritable();

		/**
		 * Aggregates values (all ones) by each word (key) and writes out sum total to context.
		 * @param key Key - word
		 * @param values Iterator over the values - ones
		 * @param context Output context to write aggregates by key
		 */
		public void reduce(Text key, Iterable<IntWritable> values, Context context)  throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable value : values) {
				sum += value.get();
			}
			sumTotal.set(sum);
			context.write(key,  sumTotal);
		}
	}


	public static void main( String[] args ) throws IOException, ClassNotFoundException, InterruptedException {
		// setup job configuration
		Configuration conf = new Configuration();
		Job job = new Job(conf, "helloWorldWordCount");

		// set mapper output key and value classes
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		// set mapper and reducer classes
		job.setMapperClass(WordCountMap.class);
		job.setReducerClass(WordCountReduce.class);

		// set input and output file classes
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		// load input and output files
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		// run hadoop job!
		job.waitForCompletion(true);

        System.out.println( "Hello World!" );
    }
}
