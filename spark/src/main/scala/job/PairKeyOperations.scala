package job

import org.apache.spark.{SparkConf, SparkContext}

object PairKeyOperations {

  def main(args: Array[String]): Unit = {


    //key-value 类型RDD到操作
    val sparkConf = new SparkConf()
    val sparkContext = new SparkContext(sparkConf)

    val input = sparkContext.parallelize(List((1,2), (3, 4), (3,6)))

    //1.reduceByKey
    val rdd1 = input.reduceByKey(_ + _)
    println(rdd1.collect())     //Array((1,3), (3,10))


    //2.groupByKey
    val rdd2 = input.groupByKey()
    println(rdd2.collect())   //Array((1,CompactBuffer(3)), (3,CompactBuffer(4, 6)))

    //3.mapValues
    val rdd3 = input.mapValues(_ + 1)
    println(rdd3.collect())   //Array((1,4), (3,5), (3,7))

    //4.flatMapValues
    val rdd4 = input.flatMapValues(_ to 10)
    println(rdd4.collect())   //Array((1,3), (1,4), (1,5), (1,6), (1,7), (1,8), (1,9), (1,10), (3,4), (3,5), (3,6), (3,7), (3,8), (3,9), (3,10), (3,6), (3,7), (3,8), (3,9), (3,10))

    //5.keys
    val rdd5 = input.keys
    println(rdd5.collect())  //Array(1, 3, 3)

    //6.values
    val rdd6 = input.values
    println(rdd6.collect())   //Array(3, 4, 6)

    //7.sortByKey
    val rdd7 = input.sortByKey()
    println(rdd7.collect())    //Array((1,3), (3,4), (3,6))


    val input2 = sparkContext.parallelize(List((1,2), (3,4), (3,6)))
    val input3 = sparkContext.parallelize(List((3,9)))

    //8.subtractByKey  根据key取差集
    print(input2.subtract(input3).collect()) //Array((1,2))

    //9.join  按照key做内链接
    val rdd8 = input2.join(input3)
    println(rdd8.collect())  //Array((3,(4,9)), (3,(6,9)))

    //10.leftOuterJoin   左链接
    val rdd9 = input2.leftOuterJoin(input3)
    println(rdd9.collect())

    //11.rightOuterJoin  右链接
    val rdd10 = input2.rightOuterJoin(input3)
    println(rdd10.collect())    //Array((3,(Some(4),9)), (3,(Some(6),9)))

    //12.cogroup 将两个rdd中key相同到值整合在一起
    val rdd11 = input2.cogroup(input3)
    println(rdd11.collect())
    //Array((1,(CompactBuffer(2),CompactBuffer())), (3,(CompactBuffer(4, 6),CompactBuffer(9))))

    val input4 = sparkContext.parallelize(List(("panda",0),("pink",3),("pirate",3),("panda",1),("pink",1)))

    //计算平均数   mapValue -> reduceByKey
    val rdd12 = input4.mapValues((_, 1)).reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))
    println(rdd12.collect()) //Array((panda,(1,2)), (pirate,(3,1)), (pink,(4,2)))


  }

}
