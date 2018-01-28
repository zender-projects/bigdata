package job

import java.util.stream.Collectors

import org.apache.spark.{SparkConf, SparkContext}


/**
  * 常用的 转换 操作
  *
  * @author zhangdong
  * */
object TransformOperations {

  def main(args: Array[String]): Unit = {

      val sparkConf = new SparkConf().setAppName("TransformOperation")
      val sparkContext = new SparkContext(sparkConf)

      //1.map()
      val input = sparkContext.parallelize(List(1,2,3,4))
      val result = input.map(x => x * x )
      print(result.collect().mkString(","))  //逗号分割 打印结果:1,4,9,16


      //2.flatMap()
      val input2 = sparkContext.parallelize(List("this is a dog", "this is pig"))
      val result2 = input2.flatMap(x => x.split(" "))
      val str = result2.collect().mkString(",")
      print(str)   //this,is,a,dog,this,is,pig


      /* 类集合操作 */
      //3.distinct  该操作为宽依赖，会触发shuffle
      val input3 = sparkContext.parallelize(List("a", "b", "a", "c", "c"))
      val result3 = input3.distinct()
      print(result3.collect().mkString(","))   //a,b,c


      //4.union  并集
      val input4 = sparkContext.parallelize(List("a","c","d"))
      val input5 = (sparkContext).parallelize(List("a","d","e"))
      val reuslt5 = input4.union(input5)
      print(reuslt5.collect().mkString(","))  //a,c,d,a,d,e


      //5.intersection()  交集 该操作会触发shuffle
      val input6 = sparkContext.parallelize(List("a", "b", "c"))
      val input7 = sparkContext.parallelize(List("c", "d", "e"))
      val result6 = input7.intersection(input7)
      print(result6.collect().mkString(","))        //c

      //6.subtract()   //差集  该操作会触发shuffle
      val input8 = sparkContext.parallelize(List("a","b","c"))
      val input9 = sparkContext.parallelize(List("a", "c"))
      val result7 = input8.subtract(input9)
      print(result7.collect().mkString(","))   //b  



  }

}
