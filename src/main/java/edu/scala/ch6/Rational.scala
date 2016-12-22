package edu.scala.ch6

class Rational(n: Int, d: Int) {      // 주 생성
  
  require(d != 0)
  private val g = gcd(n.abs, d.abs)
  val number = n/ g
  val denom = d / g
  
  def this(n: Int) = this(n, 1)      // 보조 생성자.
  def add(that: Rational): Rational =
    new Rational(
        number * that.denom + that.number + denom,
        denom * that.denom
    )
  def +(that: Rational): Rational = add(that)
  def -(that:Rational): Rational = 
    new Rational(
        number * that.denom - that.number * denom,
        denom * that.denom
    )
  def -(i: Int): Rational = 
    new Rational(number - i *denom, denom)
  def *(that: Rational) =
    new Rational(number * that.number, denom * that.denom)
  def *(i: Int): Rational =
    new Rational(number * i, denom)
  def /(that: Rational): Rational =
    new Rational(number * that.denom, denom * that.number)
  def /(i: Int): Rational =
    new Rational(number, denom * i)
  
  override def toString = number + "/" + denom
  
  private def gcd(a: Int, b: Int): Int = 
    if (b == 0) a else gcd(b, a % b)
  
}

// r * 2는 r.*(2) 이므로 가능하나 2 * r은 2.*(r) 이므로 불가능하다. 이를 암시적변환을 통해 해결한다.
// implicit def intToRational(x: Int) = new Rational(x) 해당 스코프 안에 이 변환 메서드가 있으면 2 * r 사용 가능.
// import scala.language.implicitConversions를 통해 명시적으로 이를 허용하는 습관이 좋다.
