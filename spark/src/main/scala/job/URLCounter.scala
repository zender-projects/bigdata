package job

import java.net.URL

import org.apache.spark.{Partitioner, SparkConf, SparkContext}

object URLCounter {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("UrlCounter").setMaster("local[2]")

    val sc = new SparkContext(conf)

    val rdd1 = sc.textFile("/datas/url/itcast.log").map( line => {
      // url, 1
      (line.split("\t")(1),1)
    })

    //相同url相加
    val rdd2 = rdd1.reduceByKey(_+_)

    val rdd3 = rdd2.map(t => {

      val url = t._1
      val host = new URL(url).getHost
      // host , url count
      (host, url, t._2)
    })

    val rdd4 = rdd3.groupBy(t => t._1).mapValues(iter => iter.toList.sortBy(_._3).reverse.take(3))

    rdd4.collect()



    //存在的问题：数据量很大时，iterator容易爆


    //java rdd
    val rdd5 = rdd3.filter(t => t._1.eq("java.itcast.cn"))

    rdd5.sortBy(t => t._3).take(3).foreach(t => println(t))

    /*rdd6.foreach(t => {
      println(t)
    })*/
  }

}


class MyPartitioner extends Partitioner {

  override def numPartitions = ???

  override def getPartition(key: Any) = ???

}
