// 중괄호는 안써도 된다.
//package a
//package b
//class c {
//  
//}
package edu.scala.ch13 {

  class PackageAndImport {
    // 접근 수식자
    class Outer {
      class Inner {
        private def f() { println("f") }
        class InnerMost {
          f()    // 문제 없음.
        }
      }
//      (new Inner).f()  // 오류.  자바에서는 OK?
    }
    
    // Protected Member Access
    class Super {
      protected def f() { println("f") }
    }
    class Sub extends Super {
      f()
    }
    class Other {
//      (new Super).f()    // 오류. 자바에서는 OK?
    }
  }

  package bobsrockets {
    package navigation {
      class Navigator {
        val map = new StarMap
      }
  
      class StarMap
      
      // 원본 소스 코드에 테스트 코드를 포함시키되 별도 패키지로 생성.
      package tests {
        class NavigatorSuite
      }
    }
  
    class Ship {
      val nav = new navigation.Navigator    // 하위 클래스는 상대경로로 참조.
    }
    
    package fleets {
      class Fleet {
        def addShip() { new Ship }    // 상위 패키지에 있는 클래스를 바로 참조. 명시적으로 패키징을 내포시킨 경우에만 가능.
      }
    }
  }
}

package launch {
  class Booster3
}

package bobsrokets {
  package navigation {
    package launch {
      class Booster1
    }
    
    class MissionControl {
      val booste1 = new launch.Booster1
      val booster2 = new bobsrokets.launch.Booster2
      val booster3 = new _root_.launch.Booster3
    }
  }
  
  package launch {
    class Booster2
  }
}

