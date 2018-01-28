package job

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Scala WordCount.
  * @author zhangdong
  * */
object WordCount {

  def main(args: Array[String]): Unit = {

    val inPath = args(0)

    val outPath = args(1)
    val sparkConf = new SparkConf().setAppName("SparkWordCount")
    val sc = new SparkContext(sparkConf)
    val rowRdd = sc.textFile(inPath)
    rowRdd.flatMap(_.split(" "))
          .map((_, 1))
          .reduceByKey(_ + _)
          .saveAsObjectFile(outPath)
    sc.stop()
  }

}
