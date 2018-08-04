package iptransfer

import org.apache.spark.{SparkConf, SparkContext}

object IpLocationJob {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("IPLocation").setMaster("local[2]")

    val sc = new SparkContext(conf)

    // 加载ip库
    val ipResultRdd = sc.textFile("hdfs://hadoop01:9000/datas/iplocation/ip.txt")

    // 将ip库从executors收集到Driver
    val ipResultArray = ipResultRdd.collect()

    /*ipResultArray.foreach(line => {
      println(line)
    })*/

    // 将ip库广播到每个Executor节点
    val ipRulesBroadcast = sc.broadcast(ipResultArray)

    // 加载需要处理到数据
    val ipsRdd = sc.textFile("hdfs://hadoop01:9000/datas/iplocation/20090121000132.394251.http.format")
                      // 截取ip地址
                      .map(line => line.split("\\|")(1))

    // 将ip地址转换成坐标信息
    val result = ipsRdd.map(ip => {
        // 将ip转换成十进制
        val ipNumber = IpTransfer.ip2Long(ip)
        // 二分查找在ip库中到索引
        val index = IpTransfer.binarySearch(ipRulesBroadcast.value, ipNumber)
        // 返回ip库中到坐标信息
        val info = ipRulesBroadcast.value(index)
        info
    })

    //获取省份信息
    val proviceRdd = result.map(ipLocation => {
      (ipLocation.split("\\|")(6),1)
    }).reduceByKey(_+_).sortBy(x => RequestLocation(x._1, x._2))

    println(proviceRdd.collect().toBuffer)

    sc.stop()
  }

}


case class RequestLocation(province: String, requestNum: Int) extends Ordered[RequestLocation] with Serializable {
  override def compare(that: RequestLocation) = {
    that.requestNum - this.requestNum
  }
}