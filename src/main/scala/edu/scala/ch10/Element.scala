package edu.scala.ch10

import Element.elem

abstract class Element {
  
  def contents: Array[String]
  def height: Int = contents.length
  def width: Int = if (height == 0) 0 else contents(0).length
  
  def above(that: Element): Element = {
    val this1 = this widen that.width
    val that1 = that widen this.width
    
    assert(this.width == that1.width)
    
    elem(this.contents ++ that.contents)
  }
  
  def beside(that: Element): Element = {
    // 명령형 스타일.
//    val contents = new Array[String](this.contents.length)
//    for (i <- 0 until this.contents.length)
//      contents(i) = this.contents(i) + that.contents(i)
//    elem(contents)
    
    // 함수형 스타일(축약형)
    val this1 = this heighten that.height
    val that1 = that heighten this.height
    elem(
        for (
            (line1, line2) <- this.contents zip that.contents
        ) yield line1 + line2
    )
  }
  
  def widen(w: Int): Element = {
    if (w <= width) this
    else {
      val left = elem(' ', (w - width) / 2, height)
      val right = elem(' ', w - width - left.width, height)
      left beside this beside right
    } ensuring(w <= _.width)
  }
  
  def heighten(h: Int): Element = {
    if (h <= height) this
    else {
      val top = elem(' ', width, (h - height) / 2)
      var bot = elem(' ', width, h - height - top.height)
      top above this above bot
    }
  }
  
  override def toString = contents mkString "\n"
  
  def demo() {
    println("Element's implementation invoked")
  }
}

object Element {
  private class ArrayElement(val contents: Array[String]) extends Element {
    override def demo() {
      println("ArrayElement's implementation invoked")
    }
  }

  private class LineElement(s: String) extends Element {
    val contents = Array(s)
    override def width = s.length
    override def height = 1

    override def demo() {
      println("LineElement's implementation invoked")
    }
  }

  private class UniformElement(
      ch: Char,
      override val width: Int,
      override val height: Int) extends Element {
    private val line = ch.toString * width
    def contents = Array.fill(height)(line)

    override def demo() {
      println("UniformElement's implementation invoked")
    }
  }
  
  def elem(contents: Array[String]): Element = {
    new ArrayElement(contents)
  }
  
  def elem(chr: Char, width: Int, height: Int): Element = {
    new UniformElement(chr, width, height)
  }
  
  def elem(line: String): Element = {
    new LineElement(line)
  }
}

object Spiral {
  val space = elem(" ")
  val corner = elem("+")
  
  def spiral(nEdges: Int, direction: Int): Element = {
    if (nEdges == 1) {
      elem("+")
    }
    else {
      val sp = spiral(nEdges - 1, (direction + 3) % 4)
      def verticalBar = elem('|', 1, sp.height)
      def horizontalBar = elem('-', sp.width, 1)    
      if (direction == 0)
        (corner beside horizontalBar) above (sp beside space)
      else if (direction == 1)
        (sp above space) beside (corner above verticalBar)
      else if (direction == 2)
        (space beside sp) above (horizontalBar beside corner)
      else
        (verticalBar above corner) beside (space above sp)
    }
  }
  
  def main(args: Array[String]): Unit = {
    val nSides = 11  //args(0).toInt
    println(spiral(nSides, 0))
  }
}

object ElementTest {
  def main(args: Array[String]): Unit = {
    val e1: Element = Element.elem(Array("hello", "world"))
    val ae: Element = Element.elem("hello")
    val e2: Element = ae
    val e3: Element = Element.elem('x', 2, 3)
    
    e1.demo()
    ae.demo()
    e2.demo()
    e3.demo()
    
    println(e1)
    println(ae)
    println(e2)
    println(e3)
    
    val value = "test"
    println(s"## : ${value.##}, hash : ${value.hashCode}")
  }
}