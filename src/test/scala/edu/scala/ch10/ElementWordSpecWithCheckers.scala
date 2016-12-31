package edu.scala.ch10

import org.scalatest.WordSpec
import org.scalatest.prop.Checkers
import org.scalacheck.Prop._
import Element.elem

class ElementWordSpecWithCheckers extends WordSpec with Checkers {
  
  "elem result" must {
    "have passed width" in {
      check((w: Int) => w > 0 ==> (elem('x', w, 3).width == w))      // ==> : 왼쪽이 참이면 오른쪽도 참이여야 함.
    }
    "have passed height" in {
      check((h: Int) => h > 0 ==> (elem('x', 2, h).height == h))
    }
  }
}