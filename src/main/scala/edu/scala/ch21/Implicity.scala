package edu.scala.ch21

import javax.swing.JButton
import java.awt.event.ActionListener
import java.awt.event.ActionEvent

class Implicity {
  
  val button = new JButton
  button.addActionListener(
    new ActionListener {
      def actionPerformed(event: ActionEvent) {
        println("pressed!")
      }
    }
  )

  implicit def funtion2ActionListener(f: ActionEvent => Unit) = 
    new ActionListener {
      def actionPerformed(event: ActionEvent) = f(event)
    }

  button.addActionListener(
    (_: ActionEvent) => println("pressed!")
  )
  
  // 암시적 파라미터
  //////////////////////////////////////////////////////////////////
  class PreferredPrompt(val preference: String)
  class PreferredDrink(val preference: String)
  object Greeter {
    def greet(name: String)(implicit prompt: PreferredPrompt) {
      println("Welcome, " + name + ". The system is ready.")
      println(prompt.preference)
    }
    
    def greet2(name: String)(implicit prompt: PreferredPrompt, drink: PreferredDrink) {
      println("Welcome, " + name + ". The system is ready.")
      println("But while you work, ")
      println("why not enjoy a cup of " + drink.preference + "?")
      println(prompt.preference)
    }
  }
  object JoesPrefs {
    implicit val prompt = new PreferredPrompt("Yes, master>")
    implicit val drink = new PreferredDrink("tea")
  }
  import JoesPrefs._    // 동일 스코프에 없으면 에러
  Greeter.greet("Joe")
  Greeter.greet2("Joe")
  
  // 상위 바인드를 사용하여 T가 Ordered[T]의 서브타입이어야 한다는 사실을 알림.
  // Int는 Ordered[Int]의 서브타입이 아니므로 사용할 수 없음.
  def maxListUpBound[T <: Ordered[T]](elements: List[T]): T = 
    elements match {
      case List() => 
        throw new IllegalArgumentException("empty list!")
      case List(x) => x
      case x :: rest =>
        val maxRest = maxListUpBound(rest)
        if (x > maxRest) x
        else maxRest
  }
  
  // T를 Ordered[T]로 변환하는 함수를 두번째 인자로 추가하여 Int를 사용가능하게 변경
  def maxListImpParam[T](elements: List[T])(implicit orderer: T => Ordered[T]): T =
    elements match {
    case List() =>
      throw new IllegalArgumentException("empty list!")
    case List(x) => x
    case x :: rest =>
      val maxRest = maxListImpParam(rest)(orderer)
      if (orderer(x) > maxRest) x
      else maxRest
  }
  maxListImpParam(List(1, 5, 10, 3))
  maxListImpParam(List(1.5, 5.2, 10.7, 3.14159))
  maxListImpParam(List("one", "two", "three"))
  //////////////////////////////////////////////////////////////////
  
  // 뷰 바운드
  //////////////////////////////////////////////////////////////////
  def maxList[T](elements: List[T])(implicit orderer: T => Ordered[T]): T =
    elements match {
    case List() =>
      throw new IllegalArgumentException("empty list!")
    case List(x) => x
    case x :: rest =>
      val maxRest = maxList(rest)    // orderer를 암시적으로 사용.
      if (x > maxRest) x             // orderer(x)를 암시적으로 사용.
      else maxRest
  }
  def maxList2[T <% Ordered[T]](elements: List[T]): T = 
    elements match {
    case List() => 
      throw new IllegalArgumentException("empty list!")
    case List(x) => x
    case x :: rest =>
      val maxRest = maxList2(rest)
      if (x > maxRest) x
      else maxRest
  }
  //////////////////////////////////////////////////////////////////
}

