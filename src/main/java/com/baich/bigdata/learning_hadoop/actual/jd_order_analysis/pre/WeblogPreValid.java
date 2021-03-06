package com.baich.bigdata.learning_hadoop.actual.jd_order_analysis.pre;

import com.baich.bigdata.learning_hadoop.actual.jd_order_analysis.mrbean.WebLogBean;
import com.baich.bigdata.learning_hadoop.actual.jd_order_analysis.mrbean.WebLogParser;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class WeblogPreValid {
    static class WeblogPreValidMapper extends Mapper<LongWritable, Text, Text, WebLogBean> {
        // 用来存储网络url分类数据
        Set<String> pages = new HashSet<String>();
        Text k = new Text();
        NullWritable v = NullWritable.get();

        /**
         * 从外部加载网络url分类数据
         */
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            pages.add("/about");
            pages.add("/black-ip-list/");
            pages.add("/cassandra-clustor/");
            pages.add("/finance-rhive-repurchase/");
            pages.add("/hadoop-family-roadmap/");
            pages.add("/hadoop-hive-intro/");
            pages.add("/hadoop-zookeeper-intro/");
            pages.add("/hadoop-mahout-roadmap/");
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            WebLogBean webLogBean = WebLogParser.parser(line);
            // 过滤js/图片/css等静态资源
            WebLogParser.filtStaticResource(webLogBean, pages);
            // 如果是标记为无效的数据，就不输出
            if (webLogBean.isValid()) {
                k.set(webLogBean.getRemote_addr());
                context.write(k, webLogBean);
            }
        }
    }

    static class WeblogPreValidReducer extends Reducer<Text, WebLogBean, NullWritable, WebLogBean> {
        @Override
        protected void reduce(Text key, Iterable<WebLogBean> values, Context context) throws IOException, InterruptedException {
            for (WebLogBean bean : values) {
                context.write(NullWritable.get(), bean);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJarByClass(WeblogPreValid.class);
        job.setMapperClass(WeblogPreValidMapper.class);
        job.setReducerClass(WeblogPreValidReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(WebLogBean.class);
        job.setOutputValueClass(WebLogBean.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(WebLogBean.class);
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
//        FileInputFormat.setInputPaths(job, new Path("E:/data/webLog/input/access.log.fensi"));
//        FileOutputFormat.setOutputPath(job, new Path("E:/data/webLog/valid_output/"));
        job.waitForCompletion(true);
    }
}
