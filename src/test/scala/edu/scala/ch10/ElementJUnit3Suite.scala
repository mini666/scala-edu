package edu.scala.ch10

import org.scalatest.junit.JUnit3Suite
import Element.elem

// Junit을 사용하고 ScalaTest의 단언문법을 사용하고자 할 때.
class ElementJUnit3Suite extends JUnit3Suite {
  def testUnitformElement() {
    val ele = elem('x', 2, 3)
    assert(ele.width === 2)
    assertResult(3) { ele.height }    // expect(3) { ele.height }는 에러. expect가 없음. 라이브러리 버전업 되면서 사라진듯.
    intercept[IllegalArgumentException] {
      elem('x', -2, 3)
    }
  }
}