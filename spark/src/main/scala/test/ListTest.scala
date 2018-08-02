package test

object ListTest {

   def main(args: Array[String]) : Unit = {
     val list = List[Int](1,2,3,4,5)

     list.::(3).iterator.foreach(i => {
       println(i)
     })
   }
}
