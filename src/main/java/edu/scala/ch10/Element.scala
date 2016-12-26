package edu.scala.ch10

abstract class Element {
  
  def contents: Array[String]
  def height: Int = contents.length
  def width: Int = if (height == 0) 0 else contents(0).length
  
  def above(that: Element): Element = new ArrayElement(this.contents ++ that.contents)
  
  def beside(that: Element): Element = {
    // 명령형 스타일.
    val contents = new Array[String](this.contents.length)
    for (i <- 0 until this.contents.length)
      contents(i) = this.contents(i) + that.contents(i)
    new ArrayElement(contents)
    
    // 함수형 스타일(축약형)
    new ArrayElement(
        for (
            (line1, line2) <- this.contents zip that.contents
        ) yield line1 + line2
    )
  }
  
  override def toString = contents mkString "\n"
  
  def demo() {
    println("Element's implementation invoked")
  }
}

class ArrayElement(val contents: Array[String]) extends Element {
  override def demo() {
    println("ArrayElement's implementation invoked")
  }
}

class LineElement(s: String) extends Element {
  val contents = Array(s)
  override def width = s.length
  override def height = 1
  
  override def demo() {
    println("LineElement's implementation invoked")
  }
}

class UniformElement(
    ch: Char,
    override val width: Int,
    override val height: Int
) extends Element {
  private val line = ch.toString * width
  def contents = Array.fill(height)(line)
  
  override def demo() {
    println("UniformElement's implementation invoked")
  }
}

object ElementTest {
  def main(args: Array[String]): Unit = {
    val e1: Element = new ArrayElement(Array("hello", "world"))
    val ae: Element = new LineElement("hello")
    val e2: Element = ae
    val e3: Element = new UniformElement('x', 2, 3)
    
    e1.demo()
    ae.demo()
    e2.demo()
    e3.demo()
    
    println(e1)
    println(ae)
    println(e2)
    println(e3)
  }
}