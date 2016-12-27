package edu.scala.ch12

class Trait {
  
  abstract class IntQueue {
    def get(): Int
    def put(x: Int): Unit
  }
  
  import scala.collection.mutable.ArrayBuffer
  class BasicIntQueue extends IntQueue {
    private val buf = new ArrayBuffer[Int]
    def get() = buf.remove(0)
    def put(x: Int): Unit = buf += x
  }
  
  // IntQueue가 슈퍼클래스이므로 IntQueue를 상속한 클래스만이 Doubling을 믹스인할 수 있다.
  trait Doubling extends IntQueue {
    abstract override def put(x: Int): Unit =  { super.put(2 * 2) }    // 동적 바인딩이므로 put메서드가 있는 트레이트나 클래스에 믹스인하면 문제없음.
                                                              // 컴파일러에게 의도적으로 super의 메서드를 호출했다는 사실을 알려주기 위해, absract override로 표시.
  }
  
  class MyQueue extends BasicIntQueue with Doubling
  val queue = new BasicIntQueue with Doubling          // 생성시에 믹스인 할 수도 있음.
  
  trait Incrementing extends IntQueue {
    abstract override def put(x: Int): Unit = { super.put(x + 1) }
  }
  
  trait Filtering extends IntQueue {
    abstract override def put(x: Int): Unit = { if (x >= 0) super.put(x) }
  }
}

object Trait {
  def main(args: Array[String]): Unit = {
    
  }
}