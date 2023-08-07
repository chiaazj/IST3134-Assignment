import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class ReviewDriver {

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println("Usage: ReviewDriver <input path> <review output path> <wordcount output path>");
            System.exit(-1);
        }

        Job reviewJob = Job.getInstance();
        reviewJob.setJarByClass(ReviewDriver.class);
        reviewJob.setJobName("Review extraction");

        FileInputFormat.addInputPath(reviewJob, new Path(args[0]));
        FileOutputFormat.setOutputPath(reviewJob, new Path(args[1]));

        reviewJob.setMapperClass(ReviewMapper.class);
        reviewJob.setReducerClass(ReviewReducer.class);

        reviewJob.setOutputKeyClass(Text.class);
        reviewJob.setOutputValueClass(Text.class);

        boolean reviewJobSuccessful = reviewJob.waitForCompletion(true);

    }
}

