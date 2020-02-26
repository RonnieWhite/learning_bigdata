package main.scala.learning_flink.course04_dataset

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala._

object DataSetDataSourceApp {
  def main(args: Array[String]): Unit = {
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //    fromCollection(env)
    //    readTextFile(env)
    readCsvFile(env)

  }

  def fromCollection(env: ExecutionEnvironment): Unit = {
    val data = 1 to 10
    env.fromCollection(data).print()

  }

  def readTextFile(env: ExecutionEnvironment): Unit = {
    env.readTextFile("E:/data/flink/input").print()
  }

  def readCsvFile(env: ExecutionEnvironment): Unit = {
    env.readCsvFile[(String, Int, String)]("E:/data/flink/input/person.csv", ignoreFirstLine = true).print()
  }
}
