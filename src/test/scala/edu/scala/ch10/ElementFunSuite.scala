package edu.scala.ch10

import org.scalatest.FunSuite
import Element.elem

// scalatest 사용.
// Scala 함수 스타일로 테스트케이스 작성.
class ElementFunSuite extends FunSuite {
  test("elem result should have passed width") {
    val ele = elem('x', 2, 3)
    
    assert(ele.width == 2)
  }
}