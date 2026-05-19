import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;
public class RegionalSalesAnalysis {
    public static class RegionMapper extends Mapper<Object, Text, Text, Text> {
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString().trim();
            if (line.startsWith("order_id") || line.isEmpty()) return;
            String[] fields = line.split(",");
            if (fields.length < 7) return;
            try {
                double revenue = Integer.parseInt(fields[3].trim()) * Double.parseDouble(fields[4].trim());
                context.write(new Text(fields[5].trim()), new Text(revenue + ",1"));
            } catch (NumberFormatException e) {}
        }
    }
    public static class RegionReducer extends Reducer<Text, Text, Text, Text> {
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            double total = 0.0; int orders = 0;
            for (Text val : values) {
                String[] p = val.toString().split(",");
                total += Double.parseDouble(p[0]);
                orders += Integer.parseInt(p[1]);
            }
            context.write(key, new Text(String.format("Total=%.2f | Orders=%d | Avg=%.2f", total, orders, total/orders)));
        }
    }
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Regional Analysis");
        job.setJarByClass(RegionalSalesAnalysis.class);
        job.setMapperClass(RegionMapper.class);
        job.setReducerClass(RegionReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
