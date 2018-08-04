package job

import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  * 隐士转换
  * */
case class Girl2(faceValue: Int, age: Int) extends Serializable
object OrderContext {
  implicit object GirlOrdering extends Ordering[Girl2] {
    override def compare(x: Girl2, y: Girl2): Int = {
      if(x.faceValue > y.faceValue) {
        1
      }else if (x.faceValue == y.faceValue) {
        if(x.age > y.age) {
          -1
        }else {
          1
        }
      }else {
        -1
      }
    }
  }
}
/**
  * 自定义排序
  *
  * 先按faceValue排序，faceValue相等时再比较age,最后比较name
  * name/faceValue/age
  * */
object CustomSort {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("customsort").setMaster("local[2]")

    val sc = new SparkContext(conf)

    val rdd1 = sc.parallelize(List(("zhangsan", 90, 28), ("lisi", 88, 23), ("wangwu", 88, 21)))

    import OrderContext.GirlOrdering
    val rdd2 = rdd1.sortBy(x => new Girl2(x._2, x._3))

    println(rdd2.collect().toBuffer)

    sc.stop()

  }

}

/**
  * 第一种方式
  * */
class Girl(val faceValue: Int, val age: Int) extends Ordered[Girl] with Serializable
{
  override def compare(that: Girl) = {
    if (this.faceValue == that.faceValue) {
      that.age - this.age
    } else {
      this.faceValue - that.faceValue
    }
  }
}


