package edu.scala.ch19

class TypeParameterization {
  
  class SlowAppendQueue[T](elems: List[T]) {    // 효율적이지 않다.
    def head = elems.head                                            // 상수시간.
    def tail = new SlowAppendQueue(elems.tail)                       // 상수시간.
    def enqueue(x: T) = new SlowAppendQueue(elems ::: List(x))       // 크기에 비례.
  }
  
  // 리스트를 뒤집어 처리.
  class SlowHeadQueue[T](smele: List[T]) {
    def head = smele.last                                            // 크기에 비례.
    def tail = new SlowHeadQueue(smele.init)                         // 크기에 비례.
    def enqueue(x: T) = new SlowHeadQueue(x :: smele)                // 상수 시간.
  }
  
  // head, tail을 처리하는 각각의 리스트를 관리.
  class Queue[T] private (      // 주생성자 숨김.
    private val leading: List[T],
    private val trailing: List[T]
  ) {
    def this() = this(Nil, Nil)
    def this(elems: T*) = this(elems.toList, Nil)
    
    private def mirror = 
      if (leading.isEmpty)
//        new Queue(trailing.reverse, Nil)          // leading이 비어있는 경우에만 크기에 비례. 주생성자를 private처리하여 에러
//        new Queue(Array(trailing.reverse), Nil)     // 보조 생성자를 통해 생성.
        Queue(trailing.reverse, Nil)                // 동반객체를 통해 생성.
      else
        this
    
    def head = mirror.leading.head
    def tail = {
        val q = mirror
//        new Queue(q.leading.tail, q.trailing)      //  주생성자를 private처리하여 에러
//        new Queue(Array(q.leading.tail, q.trailing))  // 보조 생성자를 통해 생성.
        Queue(q.leading.tail, q.trailing)            // 동반객체를 통해 생성.
    }
    def enqueue(x: T) = 
//      new Queue(leading, x :: trailing)
//      new Queue(Array(leading, x :: trailing))
      Queue(leading, x :: trailing)
  }
  
  object Queue {
    def apply[T](xs: T*) = new Queue[T](xs.toList, Nil)
  }
}

object TypeParameterization {
    
  def main(args: Array[String]): Unit = {
    val queue = Queue("1", "2")
    Queue.doesNotCompile(queue)    // 에러 : Queue[T]는 공변적이지 않다. Queue[+T]로 해야 에러가 나지 않는다.
    
    val c1 = new Cell[String]("abc")
    val c2: Cell[Any] = c1
    c2.set(1)
    val s: String = c1.get
  }
}

// trait를 사용한 Queue구현
trait Queue[+T] {
  def head: T
  def tail: Queue[T]
//  def enqueue(x: T): Queue[T]      // 메서드 파라미터는 반공변이므로 공변적으로 만들수 없다. 하위바운드를 사용한다.
  def enqueue[U >: T](x: U): Queue[U]
}

object Queue {
  def apply[T](xs: T*): Queue[T] = {
    new QueueImpl[T](xs.toList, Nil)
  }
  
  private class QueueImpl[T] (
    private val leading: List[T],
    private val trailing: List[T]
  ) extends Queue[T] {
    def mirror = 
      if (leading.isEmpty)
        new QueueImpl(trailing.reverse, Nil)
      else
        this
    def head: T = mirror.leading.head
    def tail: QueueImpl[T] = {
      val q = mirror
      new QueueImpl(q.leading.tail, q.trailing)
    }
    def enqueue[U >: T](x: U): Queue[U] = {
      new QueueImpl[U](leading, x :: trailing)
    }
//    def enqueue(x: T) = 
//      new QueueImpl(leading, x :: trailing)
  }
  
  def doesNotCompile(q: Queue[AnyRef]) {
    println(s"queue : ${q}")
  }

}

class Cell[+T](init: T) {
  private[this] var current = init
  def get = current
//  def set(x: T) { current = x }
  def set[U >: T](x: U) { current = x.asInstanceOf[T] }
}

class Publication(val title: String)
class Book(title: String) extends Publication(title)
object Library {
  val books : Set[Book] = 
    Set(
        new Book("Programming in Scala"),
        new Book("Walden")
    )
    
  def printBookList(info: Book => AnyRef) {
    for (book <- books) println(info(book))
  }
}

object Customer extends App {
  def getTitle(p: Publication): String = p.title
  Library.printBookList { getTitle }
}

// leading이 비어 있는 경우 mirror 연산이 trailing 리스트를 leading리스트로 반복해서 복사하는 문제를 해결한 것.
// private[this]를 없애면 setter 메서드를 만들어야 하므로 T가 반공변적이여야하여 오류가 발생함.
class OptimizedQueue[+T] private (
  private[this] var leading: List[T],
  private[this] var trailing: List[T]
) {
  private def mirror() = 
    if (leading.isEmpty) {
      while (!trailing.isEmpty) {
        leading = trailing.head :: leading
        trailing = trailing.tail
      }
    }
  
  def head: T = {
    mirror()
    leading.head
  }
  
  def tail: OptimizedQueue[T] = {
    mirror()
    new OptimizedQueue(leading.tail, trailing)
  }
  
  def enqueue[U >: T](x: U) = new OptimizedQueue[U](leading, x :: trailing)
}

class Person(val firstName: String, val lastName: String) extends Ordered[Person] {
  def compare(that: Person) = {
    val lastNameComparison = lastName.compareToIgnoreCase(that.lastName)
    if (lastNameComparison != 0)
      lastNameComparison
    else
      firstName.compareToIgnoreCase(that.firstName)
  }
  
  override def toString = firstName + " " + lastName
}

object Person extends App {
  val robert = new Person("Robert", "Jones")
  val sally = new Person("Sally", "Smith")
  
  println(s"result : ${robert < sally}")    // Ordered 트레이트를 믹스인하면 < 연산자 사용 가능.
  
  // T는 Ordered를 믹스인해야 한다. 상위바운드 지정.
  def orderedMergeSort[T <: Ordered[T]](xs: List[T]): List[T] = {
    def merge(xs: List[T], ys: List[T]): List[T] = 
      (xs, ys) match {
      case (Nil, _) => ys
      case (_, Nil) => xs
      case (x :: xs1, y :: ys1) =>
        if (x < y) x :: merge(xs1, ys)
        else y :: merge(xs, ys1)
    }
    val n = xs.length / 2
    if (n == 0) xs
    else {
      val (ys, zs) = xs splitAt n
      merge(orderedMergeSort(ys), orderedMergeSort(zs))
    }
  }
  
  val people = List(
      new Person("Larry", "Wall"),
      new Person("Anders", "Hejlsberg"),
      new Person("Guide", "van Rossum"),
      new Person("Alan", "Kay"),
      new Person("Yukihiro", "Matsumoto")
  )
  
  println(s"source : $people")
  println(s"sort : ${orderedMergeSort(people)}")
  
//  val wontCompile = orderedMergeSort(List(3, 2, 1))  // Int는 Ordered[Int]의 서브 타입이 아니다. 암시적 파라미터와 뷰바운드 이용.
}

