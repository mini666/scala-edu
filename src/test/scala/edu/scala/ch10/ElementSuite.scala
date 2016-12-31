package edu.scala.ch10

import org.scalatest.Suite
import Element.elem

// scalatest 사용.
class ElementSuite extends Suite {
  def testUnitformElement(): Unit = {
    val ele = elem('x', 2, 3)
    assert(ele.width == 2)
  }
}
