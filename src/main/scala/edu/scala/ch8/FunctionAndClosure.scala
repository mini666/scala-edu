package edu.scala.ch8

class FunctionAndClosure {
  
  def functionLiteral() : Unit = {
    val someNumbers = List(-11, -10, -5, 0, 5, 10)
    
    someNumbers.foreach((x: Int) => println(x))
    someNumbers.foreach(println(_))
    someNumbers.foreach(println _)
    someNumbers.foreach(println)
    
    someNumbers.filter((x: Int) => x > 0)
    someNumbers.filter(x => x > 0)      // 인자가 하나일때 괄호 제거 가능
    someNumbers.filter(_ > 0)           // 간편 표기.
  }
  
  // 부분 함수.
  def partialFunction() : Unit = {
    def sum(a: Int, b: Int, c: Int) = a + b + c      // only declarations allowed here
    
    sum(1, 2, 3)
    
    val a = sum _
    a(1,2,3)
    a.apply(1,2,3)    // 스칼라가 컴파일할 때 자동으로 인자 3개인 apply메서드를 만듦.
    
    val b = sum(1, _: Int, 3)
    b(2)    // result = 6
  }
  
  def callFunction() : Unit = {
    // 반복 파라미터. 
    // 인자의 마지막을 반복 파라미터로 만들수 있음. args는 Array[String] 타입.
    def echo(args: String*) = for (arg <- args) println(arg)
    val arr = Array("What's", "up", "doc?")
//    echo(arr)  // 오류
    echo(arr: _*) // OK
    
    //Named Parameter
    def spped(distance: Float, time: Float): Float = distance / time
    spped(100, 10)
    spped(time = 10, distance = 100)
    
    // default parameter
    def printTime(out: java.io.PrintStream = Console.out) = out.println("time = " + System.currentTimeMillis())
    printTime()
    printTime(Console.err)
  }
  
  def tailRecursize() : Unit = {
    def isGoodEnough(guess: Double): Boolean = true
    def improve(guess: Double): Double = guess * 0.1
    
    def approximate(guess: Double): Double = {
      if (isGoodEnough(guess)) guess
      else approximate(improve(guess))
    }
    
    def approximateLoop(initialGuess: Double): Double = {
      var guess = initialGuess
      while (!isGoodEnough(guess))
        guess = improve(guess)
      guess
    }
    
    // approximate와 approximateLoop는 동일한 바이트 코드를 만들어 냄.
    // 재귀 호출마다 새로운 스택을 만들지 않고 같은 스택 프레임을 재활용함.
    def boom(x: Int): Int = {
      if (x == 0) throw new Exception("boom!")
      else boom(x - 1) + 1    // +1 때문에 꼬리 최적화가 아니다. boom(3)을 호출하면 스택이 여러개 나온다.
                              // boom(x - 1) 만 있어야 꼬리 최적화이고 boom(n)을 호출하면 스택이 하나만 나온다. 스택 재활용.
    }
  }
}


object FunctionAndClosure {
  
  def main(args: Array[String]): Unit = {
    val functionAndClosure = new FunctionAndClosure()
    functionAndClosure.tailRecursize()
  }
}