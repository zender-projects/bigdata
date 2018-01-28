package job

import org.apache.spark.{SparkConf, SparkContext}

object Counter {

  def main(args: Array[String]): Unit = {

    val inPath = args(0)
    val outPath = args(1)

    val sparkConf = new SparkConf().setAppName("CounterTestJob")//.setMaster("")
    val sc = new SparkContext(sparkConf);

    val inRDD = sc.textFile(inPath)
    var counter_total = sc.accumulator(0L,"total_counter");
    val resultRDD = inRDD.flatMap(_.split(" ")).map(x => {
      counter_total+=1
      (x, 1)
    }).reduceByKey(_ + _)

    resultRDD.saveAsTextFile(outPath)

    sc.stop()
  }

}
