package main.java.learning_spark.rdd_broadcast_accumulator;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class RDDReview {
    public static void main(String[] args) {
        String filePath = "E:/data/hadoop/input";
        SparkConf conf = new SparkConf().setAppName("rdd").setMaster("local[2]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> textFile = sc.textFile(filePath);
        JavaPairRDD<String, Integer> reduce = textFile.flatMap(line -> Arrays.asList(line.split(" ")).iterator())
                .mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey((a, b) -> a + b);
        List<Tuple2<String, Integer>> collect = reduce.collect();
        System.out.println(collect);
    }
}
