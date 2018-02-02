package partitions

import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

object GetPartitions {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf()
    val sparkContext = new SparkContext(sparkConf)

    val pairs = sparkContext.parallelize(List((1,1),(2,2),(3,3)))
    //默认没有进行分区
    val partitoner = pairs.partitioner  //=> None

    val partitioned = pairs.partitionBy(new HashPartitioner(2));
    //
    val partitionedP = partitioned.partitioner  //=>Some(org.apache.spark.HashPartitioner@2)



  }

}
