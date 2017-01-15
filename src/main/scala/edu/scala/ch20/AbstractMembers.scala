package edu.scala.ch20

import java.io.PrintWriter
import java.net.ServerSocket
import java.util.Date

class AbstractMembers {

  trait Abstract {
    type T
    def transform(x: T): T
    val initial: T
    var current: T
  }
  
  class Concrete extends Abstract {
    type T = String
    def transform(x: String) = x + x
    val initial = "hi"
    var current = initial
  }
  
  abstract class Fruit {
    val v: String
    def m: String
  }
  
  abstract class Apple extends Fruit {
    val v: String
    val m: String    // def를 val로 오버라이드 할 수 있다.
  }
  
  abstract class BadApple extends Fruit {
//    def v: String      // 'error' val은 def로 오버라이드할 수 없다.
    def m: String
  }
  
  trait AbsractTime {
    var hour: Int
    var minute: Int
  }
  
  // Abstract와 동일한 정의.
  trait SameAbstractTime {
    def hour: Int
    def hour_=(x: Int)
    def minute: Int
    def minute_=(x: Int)
  }
  
  // 추상 val 초기화 
  trait RationalTrait {
    val numerArg: Int
    val denomArg: Int
  }
  
  // 익명 클래스. new Rational(1, 2)와 같다.
  new RationalTrait {
    val numerArg = 1
    val denomArg = 2
  }
  
//  new Rational(expr1, expr2)    // Rational을 초기화하기 전에 expr1, expr2를 계산.
//  new RationalTrait {           // 초기화 전에 사용가능하지 않다.
//    val numberArg = expr1
//    val denomArg = expr2
//  }
  
  trait RationalTrait2 {
    val numerArg: Int
    val denomArg: Int
    require(denomArg != 0)    // denomArg은 항상 0이다.
    private val g = gcd(numerArg, denomArg)
    val numer = numerArg / g
    val denom = denomArg/ g
    
    private def gcd(a: Int, b: Int): Int = 
      if (b == 0) a else gcd(b, a % b)
      
    override def toString = numer + "/" + denom
  }
  
  // 필드를 미리 초기화하기.
  val x = 2
  new {
    val numerArg = 1 * x
    val denomArg = 2 * x
  } with RationalTrait2
  
  object twoThirds extends {
    val numerArg = 2
    val denomArg = 3
  } with RationalTrait2
  
  class RationalClass(n: Int, d: Int) extends {
    val numerArg = n
    val denomArg = d
  } with RationalTrait2 {
    def + (that: RationalClass) = new RationalClass(
        numer * that.denom + that.numer * denom,
        denom * that.denom
      )
  }
  
  // 지연 계산 val
  trait LazyRationalTrait {
    val numerArg: Int
    val denomArg: Int
    lazy val numer = numerArg / g
    lazy val denom = denomArg / g
    override def toString = numer + "/" + denom
    private lazy val g = {
      require(denomArg != 0)
      gcd(numerArg, denomArg)
    }
    
    private def gcd(a: Int, b: Int): Int =
      if (b == 0) a else gcd(b, a % b)
  }
  new LazyRationalTrait {    // 이제 미리 초기화할 필요 없다.
    val numerArg = 1 * x
    val denomArg = 2 * x
  }
  
  // 추상 타입
  class Food
  abstract class Animal {
    def eat(food: Food)
  }
  
  class Grass extends Food
//  class Cow extends Animal {
//    override def eat(food: Grass) {}    // 컴파일 할 수 없음.
//  }
  abstract class AbstractAnimal {
    type SuitableFood <: Food
    def eat(food: SuitableFood)
  }
  class Cow extends AbstractAnimal {
    type SuitableFood = Grass
    override def eat(food: SuitableFood) {}
  }
  
  
  // 구조적 서브타이핑
  //////////////////////////////////////////////////////////////////////
  class Animal2 { type SuitableFood = Grass }
  class Pasture {
    var animals: List[Animal { type SuitableFood = Grass }] = Nil
  }
  
  using(new PrintWriter("date.txt")) {
    writer => writer.println(new Date)
  }
  val serverSocket = new ServerSocket
  using(serverSocket.accept()) { socket =>
    socket.getOutputStream().write("hello, world\n".getBytes)
  }
  
  def using[T, S](obj: T)(operation: T => S) = {
    val result = operation(obj)
//    obj.close()      // 타입 오류
    result
  }
  def using2[T <: { def close(): Unit }, S](obj: T)(operation: T => S) = {
    val result = operation(obj)
    obj.close()
    result
  }
  //////////////////////////////////////////////////////////////////////
  
  // 열거형
  //////////////////////////////////////////////////////////////////////
  object Color extends Enumeration {
    val Red, Green, Blue = Value
  }
  
  object Direction extends Enumeration {
    val North = Value("North")
    val East = Value("East")
    val South = Value("South")
    val West = Value("West")
  }
  
  for (d <- Direction.values) print(d + " ")
  Direction.East.id   // = 1
  Direction(1)        // = East
  //////////////////////////////////////////////////////////////////////
  
  // Currency 클래스 설계
  /////////////////////////////////////////////////////////////////////
  // 처음으로 (잘못) 설계한 통화 클래스
  abstract class Currency {
    val amount: Long
    def designation: String
    override def toString = amount + " " + designation
//    def + (that: Currency): Currency = {...}    // not yet implement
//    def * (x: Double): Currency = {...}    // not yet implement
  }
  
  // 두번째(여전히 완전하지는 않은) Currency 클래스 설계
  abstract class AbstractCurrency {
    type Currency <: AbstractCurrency    // 추상 타입
    val amount: Long
    def designation: String
    override def toString = amount + " " + designation
//    def + (that: Currency): Currency = ...  // not yet implement
//    def * (x: Double): Currency = ...       // not yet implement
    
    def make(amount: Long): Currency    // 팩토리 메서드
  }
  
  // 추상타입과 팩토리 메서드를 분리
  abstract class CurrencyZone {
    type Currency <: AbstractCurrency
    def make(x: Long): Currency
    abstract class AbstractCurrency {
      val amount: Long
      def designation: String
      override def toString = ((amount.toDouble / CurrencyUnit.amount.toDouble)
        formatted ("%." + decimals(CurrencyUnit.amount) + "f") + " " + designation)

      def + (that: Currency): Currency = make(this.amount + that.amount)
      def * (x: Double): Currency = make((this.amount * x).toLong)
      def - (that: Currency): Currency = make(this.amount - that.amount)
      def / (that: Double) = make((this.amount / that).toLong)
      def from(other: CurrencyZone#AbstractCurrency): Currency = 
        make(math.round(
            other.amount.toDouble * Converter.exchangeRate
              (other.designation)(this.designation)))
      
      private def decimals(n: Long): Int = 
        if (n < 10) 0 else 1 + decimals(n / 10)
    }
    val CurrencyUnit: Currency
  }
  
  object US extends CurrencyZone {
    abstract class Dollar extends AbstractCurrency {
      def designation = "USD"
    }
    type Currency = Dollar
    def make(cents: Long) = new Dollar {
      val amount = cents
    }
    val Cent = make(1)
    val Dollar = make(100)
    val CurrencyUnit = Dollar
  }
  
  object Europe extends CurrencyZone {
    abstract class Euro extends AbstractCurrency {
      def designation = "EUR"
    }
    type Currency = Euro
    def make(cents: Long) = new Euro {
      val amount = cents
    }
    val Cent = make(1)
    val Euro = make(100)
    val CurrencyUnit = Euro
  }
  
  object Japan extends CurrencyZone {
    abstract class Yen extends AbstractCurrency {
      def designation = "JPY"
    }
    type Currency = Yen
    def make(yen: Long) = new Yen {
      val amount = yen
    }
    
    val Yen = make(1)
    val CurrencyUnit = Yen
  }
  
  object Converter {
    val exchangeRate = Map (
        "USD" -> Map("USD" -> 1.0, "EUR" -> 0.7596,
                     "JPY" -> 1.211, "CHF" -> 1.223),
        "EUR" -> Map("USD" -> 1.316, "EUR" -> 1.0,
                     "JPY" -> 1.594, "CHF" -> 1.623),
        "JPY" -> Map("USD" -> 0.8257, "EUR" -> 0.6272,
                     "JPY" -> 1.0, "CHF" -> 1.018),
        "CHF" -> Map("USD" -> 0.8108, "EUR" -> 0.6160,
                     "JPY" -> 0.982, "CHF" -> 1.0)
    )
  }
  /////////////////////////////////////////////////////////////////////
}