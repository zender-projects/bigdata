package job

import org.apache.spark.SparkContext

/**
  * k-v operations
  * partitionBy
  * mapValues
  * foldBykey
  * groupByKey
  * reduceByKeyLocally -> Map
  * reduceByKey -> RDD
  *
  *
  * */
object SparkOperations {

  def main(args: Array[String]): Unit = {

    //k-v transform operation
    var sc = new SparkContext()

    var  rdd1 = sc.makeRDD(Array((1, "A"), (2, "B"), (3,"C"), (4, "D")), 2)

    // show elements in every partition
    rdd1.mapPartitionsWithIndex( (idx, iter) => {
      var partMap = scala.collection.mutable.Map[String, List[(Int, String)]]()

      while (iter.hasNext) {
        var part_name = "part_" + idx
        var element = iter.next()

        if(partMap.contains(part_name)) {
          var elems = partMap(part_name)
          elems ::= element
          partMap(part_name) = elems
        } else {
          partMap(part_name) = List[(Int, String)]{element}
        }
      }
      partMap.iterator
    }).collect()


    var rdd2 = rdd1.partitionBy(new org.apache.spark.HashPartitioner(2))

    rdd2.mapPartitionsWithIndex( (idx, iter) => {
      var partMap = scala.collection.mutable.Map[String, List[(Int, String)]]()

      while (iter.hasNext) {
        var part_name = "part_" + idx
        var element = iter.next()

        if(partMap.contains(part_name)) {
          var elems = partMap(part_name)
          elems ::= element
          partMap(part_name) = elems
        } else {
          partMap(part_name) = List[(Int, String)]{element}
        }
      }
      partMap.iterator
    }).collect()

    //
    var rdd3 = sc.makeRDD(Array((1, "A"), (2, "B"), (3,"C"), (4, "D")), 2)
    rdd3.mapValues( x => {x + "_"}).collect()

    //combineByKey
    var rdd4 = sc.makeRDD(Array(("A", 1), ("A", 2), ("B", 1), ("B",2), ("C", 1)))

    rdd4.combineByKey(
      (v: Int) => v + "_",
      (c: String, v: Int) => c + "@" + v,
      (c1: String, c2: String) => c1 + "$" + c2
    ).collect()

    rdd4.foldByKey(2)(_+_).collect()
    rdd4.foldByKey(0)(_*_).collect()

    rdd4.groupByKey(2).collect()

    var rdd5 = sc.makeRDD(Array(("A", 0), ("A", 2), ("B", 1), ("B", 2), ("C", 1)))
    rdd5.reduceByKey((x, y) => x + y).collect()
    // -> Map
    rdd5.reduceByKeyLocally((x, y) => x + y)
  }

}
