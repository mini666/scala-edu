package edu.scala.ch10

import junit.framework.TestCase
import junit.framework.Assert.assertEquals
import junit.framework.Assert.fail
import Element.elem

// JUnit 사용.
class ElementTestCase extends TestCase {
  
  def testUnitformElement() {
    val ele = elem('x', 2, 3)
    
    assertEquals(2, ele.width)
    assertEquals(3, ele.height)
    try {
      elem('x', -2, 3)
      fail()
    } catch {
      case e: IllegalArgumentException => 
    }
  }
}