package edu.scala.ch9

import java.io.PrintWriter
import java.io.File

class FlowControlAbstraction {
  
  object FileMatcher {
    private def filesHere = (new java.io.File(".")).listFiles
    def filesEnding(query: String) = 
      for (file <- filesHere; if file.getName.endsWith(query))
        yield file
        
    def filesContaining(query: String) = 
      for (file <- filesHere; if file.getName.contains(query))
        yield file
        
    def filesRegex(query: String) = 
      for (file <- filesHere; if  file.getName.matches(query))
        yield file
        
    // 공통부분 함수로
    def filesMatching(query: String, matcher: (String, String) => Boolean) = {
        for (file <- filesHere; if matcher(file.getName, query))      // 함수내에서 인자가 순서(file.getName, query)대로 사용되면 위치지정자로 바꿀 수 있다.
          yield file
    }
    // filesEnding과 동일
    def fileEnding2(query: String) = filesMatching(query, _.endsWith(_))
    // filesContaining과 동일
    def filesContaining2(query: String) = filesMatching(query, _.contains(_))
    // filesRegex와 동일
    def filesRegex2(query: String) = filesMatching(query, _.matches(_))
    
    // 더 개선된 버전
    private def filesMatching2(matcher: String => Boolean) = 
      for (file <- filesHere; if matcher(file.getName))
        yield file
    def filesEnding3(query: String) = filesMatching2(_.endsWith(query))
    def filesContaining3(query: String) = filesMatching2(_.contains(query))
    def filesRegex3(query: String) = filesMatching2(_.matches(query))
  }
  
  // Currying
  //////////////////////////////////////////////////////////////////////////////////////////
  def plainOldSum(x: Int, y: Int) = x + y
  plainOldSum(1, 2)
  
  def curriedSum(x: Int)(y: Int) = x + y
  curriedSum(1)(2)    // 함수를 두번 연달아 호출한 것.
  
  val onePlus = curriedSum(1)_
  val twoPlus = curriedSum(2)_
  //////////////////////////////////////////////////////////////////////////////////////////
  
  // 9.4 새로운 제어 구조 작성
  //////////////////////////////////////////////////////////////////////////////////////////
  def twice(op: Double => Double, x: Double) = op(op(x))
  twice(_ + 1, 5) // 7
  
  // 자원을 열고 조작하고 닫아주는 구조. 빌려주기 패턴(Loan pattern)
  def withPrintWriter(file: File, op: PrintWriter => Unit) {
    val writer = new PrintWriter(file)
    try {
      op(writer)
    } finally {
      writer.close()
    }
  }
  withPrintWriter(new File("date.txt"), writer => writer.println(new java.util.Date))
  
  // 중괄호 사용을 위한 커링
  def withPrintWriter2(file: File)(op: PrintWriter => Unit) {
    val writer = new PrintWriter(file)
    try {
      op(writer)
    } finally {
      writer.close()
    }
  }
  val file = new File("date.txt")
  withPrintWriter2(file) {
    writer => writer.println(new java.util.Date)
  }
  //////////////////////////////////////////////////////////////////////////////////////////
  
  // 9.5 이름에 의한 호출 파라미터
  //////////////////////////////////////////////////////////////////////////////////////////
  var assertionsEnabled = true
  def myAssert(predicate: () => Boolean) =
    if (assertionsEnabled && !predicate())
      throw new AssertionError
  myAssert(() => 5 > 3)    // myAssert(5 > 3) is better.
  
  def byNameAssert(predicate: => Boolean) =
    if (assertionsEnabled && !predicate)
      throw new AssertionError
  byNameAssert(5 > 3)
  
  def boolAssert(predicate: Boolean) = 
    if (assertionsEnabled && !predicate)
      throw new AssertionError
  boolAssert( 5 > 3)    // 호출 시점에 이미 5 > 3가 계산되어 boolAssert(true)가 호출됨. byNameAssert는 그렇지 않음.
  //////////////////////////////////////////////////////////////////////////////////////////
}