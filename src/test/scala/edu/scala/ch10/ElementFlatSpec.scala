package edu.scala.ch10

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import Element.elem

class ElementFlatSpec extends FlatSpec with Matchers {
  
  "A UniformElement" should "hae a width equal to the passed value" in {
      val ele = elem('x', 2, 3)
      ele.width should be (2)
  }
  
  it should "have a height equal to the passed value" in {
    val ele = elem('x', 2, 3)
    ele.height should be (3)
  }
  
//  it should "throw an IAE if passed a negative width" in {
//    evaluating {
//      elem('x', -2, 3)
//    } should produce [IllegalArgumentException]
//  }
  an [IllegalArgumentException] should be thrownBy elem('x', -2, 3)
}