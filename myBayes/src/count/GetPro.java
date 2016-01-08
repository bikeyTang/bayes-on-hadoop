package count;

import count.MyFileInputFormat;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Spliterator;
import java.util.function.Consumer;

import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

public class GetPro extends Configuration implements Tool{
	public static class DocMap extends Mapper<Object,Text,Text,Text>{

		public void map(Object key,Text value,Context context) throws IOException,InterruptedException{

			InputSplit inputSplit=context.getInputSplit();
			String fileName=((FileSplit)inputSplit).getPath().toString();
			String[] str_arr=fileName.split("/");
			int str_length=str_arr.length;
			Text c=new Text(str_arr[str_length-2]);
			Text doc_id=new Text(str_arr[str_length-1]);
		
			context.write(c,doc_id);
		}
		
	}
	public static class DOCCombiner extends Reducer<Text,Text,Text,Text>{
		public void reduce (Text key,Iterable<Text> values,Context context) throws IOException ,InterruptedException{
			HashSet hs=new HashSet();
			for (Text val:values){
				hs.add(val);
			}
			context.write(key,new Text(String.valueOf(hs.size())));
		}
	}
	public static class DocReduce extends Reducer<Text ,Text,Text,IntWritable>{
		public final static IntWritable one=new IntWritable(1);
		
		public void reduce(Text key,Iterable<Text> values,Context context) throws IOException,InterruptedException{

			IntWritable  sum=new IntWritable(0);
			for(Text val:values){
				sum.set(Integer.parseInt(val.toString())+sum.get());
			}
			context.write(key,sum);
		}
	}
	public static void main(String[] args) throws Exception{
		int res=ToolRunner.run(new Configuration(), new GetPro(),args);
		System.exit(res);
		
	}
	public Configuration getConf() {
		// TODO Auto-generated method stub
		return null;
	}
	public void setConf(Configuration arg0) {
		// TODO Auto-generated method stub
		
	}
	public void forEach(Consumer<? super Entry<String, String>> action) {
		// TODO Auto-generated method stub
		
	}
	public Spliterator<Entry<String, String>> spliterator() {
		// TODO Auto-generated method stub
		return null;
	}
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf=new Configuration();
		conf.set("mapreduce.input.fileinputformat.input.dir.recursive","true");
		Path outputPath=new Path(args[1]);
		outputPath.getFileSystem(conf).delete(outputPath,true);
		Job job=Job.getInstance(conf,"getP");
		job.setJarByClass(GetPro.class);
		
		job.setMapperClass(DocMap.class);
		job.setCombinerClass(DOCCombiner.class);
		job.setReducerClass(DocReduce.class);
		job.setInputFormatClass(MyFileInputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job,new Path(args[1]));
		System.exit(job.waitForCompletion(true)?  0:1);
		return 0;
	}
}
	

