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
  class Queue[T] (
    private val leading: List[T],
    private val trailing: List[T]
  ) {
    private def mirror = 
      if (leading.isEmpty)
        new Queue(trailing.reverse, Nil)          // leading이 비어있는 경우에만 크기에 비례.
      else
        this
    
    def head = mirror.leading.head
    def tail = {
        val q = mirror
        new Queue(q.leading.tail, q.trailing)
    }
    def enqueue(x: T) = 
      new Queue(leading, x :: trailing)
  }
}