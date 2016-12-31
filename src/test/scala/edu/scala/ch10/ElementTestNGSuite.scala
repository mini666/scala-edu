package edu.scala.ch10

import org.scalatest.testng.TestNGSuite
import org.testng.annotations.Test
import Element.elem

class ElementTestNGSuite extends TestNGSuite {
  
  @Test def verifyUniformElement() {
    val ele = elem('x', 2, 3)
    assert(ele.width === 2)
    assertResult(3) { ele.height }    // expect(3) { ele.height }는 에러. expect가 없음. 라이브러리 버전업 되면서 사라진듯.
    intercept[IllegalArgumentException] {
      elem('x', -2, 3)
    }
  }
}