// 접근 지정자를 사용해 보호 영역을 유연하게 설정하기
package bobstrockets 

package navigation {
  private[bobstrockets] class Navigator {          // bobstrockets 패키지 내부에 있는 모든 객체와 클래스에서 Navigator에 접근 가능.
    protected[navigation] def useStarChart() {}    // navigation 패키지 내부에서 접근 가능하므로 자바의 protected와 동일.
    class LegOfJourney {
      private[Navigator] val distance = 100        // Navigator 클래스 내부에서 접근 가능.
    }
    private[this] var speed = 200                  // 이 정의를 포함하는 객체 내부에서만 접근 가능. 객체 전용. 같은 클래스의 다른 객체에서 접근할 수 없다.
  }
}

package launch {
  import navigation._
  object Vehicle {
    private[launch] val guide = new Navigator    // 
  }
}