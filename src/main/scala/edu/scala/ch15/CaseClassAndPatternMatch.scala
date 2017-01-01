package edu.scala.ch15

class CaseClassAndPatternMatch {
  
  abstract class Expr
//  sealed abstract class Expr
  case class Var(name: String) extends Expr
  case class Number(num: Double) extends Expr
  case class UnOp(operator: String, arg: Expr) extends Expr
  case class BinOp(operator: String, left: Expr, right: Expr) extends Expr
  
  def simplifyTop(expr: Expr): Expr = expr match {
    case UnOp("-", UnOp("-", e)) => e
    case BinOp("+", e, Number(0)) => e
    case BinOp("*", e, Number(1)) => e
    case UnOp("abs", e @ UnOp("abs", _)) => e    // 변수 바인딩.
    case _ => expr
  }
  
  // 상수 패턴.
  def describe(x: Any) = x match {
    case 5 => "file"
    case true => "truth"
    case "hello" => "hi!"
    case Nil => "the empty list"
    case _ => "something else"
  }
  
  def describe(e: Expr): String = (e: @unchecked) match {
    case Number(_) => "a number"
    case Var(_) => "a variable"
//    case _ => throw new RuntimeException    // 불필한 코드. 대안으로 unchecked annotation 사용.
  }
  
  def generalSize(x:Any): Int = x match {
    case s: String => s.length
    case m: Map[_, _] => m.size
    case _ => -1
  }
  
  def isIntIntMap(x: Any) = x match {
    case m: Map[Int, Int] => true      // Erasure에 의하 타입이 소거되어 항상 true가 나온다.
    case _ => false
  }
  
  def isStringArray(x: Any) = x match {
    case a: Array[String] => "yes"    // 배열은 타입이 소거되지 않으므로 패턴 매치가 가능하다.
    case _ => "no"
  }
  
  def simplifyAdd(e: Expr) = e match {
    case BinOp("+", x, y) if x == y => BinOp("*", x, Number(2))    // 패턴 가드.
    case _ => e
  }
  
  def simplifyAll(expr: Expr): Expr = expr match {
    case UnOp("-", UnOp("-", e)) => simplifyAll(e)     // -를 두번 적용하는 경우
    case BinOp("+", e, Number(0)) => simplifyAll(e)    // 0을 더하는 경우
    case BinOp("*", e, Number(1)) => simplifyAll(e)    // 1을 곱하는 경우
    case UnOp(op, e) => UnOp(op, simplifyAll(e))       // 모든 단항연산자를 처리하므로 UnOp("-", UnOp("-", e)) => simplifyAll(e) 보다 뒤에 있어야 한다.
    case BinOp(op, l, r) => BinOp(op, simplifyAll(l), simplifyAll(r))    // 모든 이항 연산자를 처리하므로 뒤에 있어야 한다.
    case _ => expr
  }
  
  def usePatternInForExpression1(): Unit = {
    val capitals = Map("seoul" -> "korea", "pyungyang" -> "north korea", "washington" -> "america")
    for ((city, country) <- capitals) {
      println(s"The capital of ${country} is $city")
    }
  }
  
  def usePatternInForExprocession2(): Unit = {
    val results = List(Some("apple"), None, Some("orange"))
    for  (Some(fruit) <- results) println(fruit)
  }

  class ExprFormatter {
    // 연산자를 우선순위가 커지는 순서로 나열한 배열.  
    private val opGroups =
      Array(
        Set("|", "||"),
        Set("&", "&&"),
        Set("^"),
        Set("==", "!="),
        Set("<", "<=", ">", ">="),
        Set("+", "-"),
        Set("*", "%"))

    // 연산자와 우선순위 간이 맵
    private val precedence = {
      val assocs =
        for {
          i <- 0 until opGroups.length
          op <- opGroups(i)
        } yield op -> i
      assocs.toMap
    }

    private val unaryPrecedence = opGroups.length
    private val fractionPrecedence = -1

    import edu.scala.ch10.Element
    import edu.scala.ch10.Element._
//    import CaseClassAndPatternMatch._

    private val test = CaseClassAndPatternMatch

    private def format(e: Expr, enclPrec: Int): Element = {
      e match {
        case Var(name) => elem(name)
        case Number(num) =>
          def stripDot(s: String) =
            if (s endsWith ".0") s.substring(0, s.length - 2)
            else s
          elem(stripDot(num.toString))
        case UnOp(op, arg) =>
          elem(op) beside format(arg, unaryPrecedence)
        case BinOp("/", left, right) =>
          val top = format(left, fractionPrecedence)
          val bot = format(right, fractionPrecedence)
          val line = elem('-', top.width max bot.width, 1)
          val frac = top above line above bot

          if (enclPrec != fractionPrecedence) frac
          else elem(" ") beside frac beside elem(" ")
        case BinOp(op, left, right) =>
          val opPrec = precedence(op)
          val l = format(left, opPrec)
          val r = format(right, opPrec + 1)
          val oper = l beside elem(" " + op + " ") beside r
          if (enclPrec <= opPrec) oper
          else elem("(") beside oper beside elem(")")
      }
    }
  
    def format(e: Expr): Element = format(e, 0)
  }
}

object CaseClassAndPatternMatch {
  
  
  def main(args: Array[String]): Unit = {
    val test = new CaseClassAndPatternMatch
    
    import test._
    val sample = BinOp("+", Number(4d), Number(0))
    val result = simplifyTop(sample);
    
    println(s"result : $result")
    
    // 변수 정의에 패턴 사용하기.
    val exp = new BinOp("*", Number(5), Number(1))
    val BinOp(op, left, right) = exp
    val withDefault: Option[Int] => Int = {
      case Some(x) => x
      case None => 0
    }
    
    val f = new ExprFormatter
    val e1 = BinOp("*", BinOp("/", Number(1), Number(2)),
                        BinOp("+", Var("x"), Number(1)))
    val e2 = BinOp("+", BinOp("/", Var("x"), Number(2)),
                        BinOp("/", Number(15), Var("x")))
    val e3 = BinOp("/", e1, e2)
    
    def show(e: Expr) = println(f.format _ + "\n\n")
    
    for (e <- Array(e1, e2, e3)) show _
  }
}
