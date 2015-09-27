package com.iizrailevsky.hadoop.example.votetally;

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
 * Tallies the votes in a given election for collected ballots.
 *
 */
public class VoteTally {

    /**
     * Mapper class
     */
    public static class VoteTallyMap extends Mapper<LongWritable, Text, Text, IntWritable> {
        private static final IntWritable one = new IntWritable(1);
        private Text candidate = new Text();

        /**
         * Breaks up the given line into chosen candidate's first and last names
         * and maps each last name (key) to 1 vote (value).
         * @param key Line number
         * @param value Line contents
         * @param context Output context to write key-value pairs to
         */
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] name = line.split("[\\s]+");
            // grab chosen candidate's last name and count each vote
            if (name != null && name.length == 2) {
                String candidateLastName = name[1];
                candidate.set(candidateLastName);
                context.write(candidate, one);
            }
        }
    }

    /**
     * Reducer class
     */
    public static class VoteTallyReduce extends Reducer<Text, IntWritable, Text, IntWritable> {
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
            context.write(key, sumTotal);
        }
    }

    public static void main( String[] args ) throws IOException, ClassNotFoundException, InterruptedException {
        // setup job configuration
        Configuration conf = new Configuration();
        Job job = new Job(conf, "helloWorldVoteTally");

        // set mapper output key and value classes
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // set mapper and reducer classes
        job.setMapperClass(VoteTallyMap.class);
        job.setReducerClass(VoteTallyReduce.class);

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
