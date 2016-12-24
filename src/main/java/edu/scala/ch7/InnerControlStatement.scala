package edu.scala.ch7

class InnerControlStatement {

  // for 표현식
  def forExpression(): Unit = {
    val filesHere = (new java.io.File(".")).listFiles

    for (file <- filesHere) {
      println(file)
    }

    // Range를 이용한 순회
    for (i <- 1 to 4)
      println("Iteration " + i)
    // 최대값 제외
    for (i <- 1 until 4) {
      println("Iteration " + i)
    }

    // 필터링
    for (file <- filesHere if file.getName.endsWith(".scala")) {
      println(file)
    }
    for (
      file <- filesHere if file.isFile if file.getName.endsWith(".scala")
    ) println(file)

    // 중첩 순회
    def fileLines(file: java.io.File) = scala.io.Source.fromFile(file).getLines().toList
    def grep(pattern: String) = {
      for (
        file <- filesHere if file.getName.endsWith(".scala");
        line <- fileLines(file) if line.trim.matches(pattern)
      ) println(file + ": " + line.trim)
    }
    grep(".*gcd.*")

    // for 중에 변수 바인딩하기
    def grep2(pattern: String): Unit = {
      for {
        file <- filesHere
        if file.getName.endsWith(".scala")
        line <- fileLines(file)
        trimmed = line.trim
        if (trimmed.matches(pattern))
      } println(file + ": " + trimmed)
    }
    grep2(".*gcd.*")

    // 새로운 컬렉션 만들어내기
    def scalaFiles = for {
      file <- filesHere
      if file.getName.endsWith(".scala")
    } yield file

    val forLineLengths = for {
      file <- filesHere
      if file.getName.endsWith(".scala")
      line <- fileLines(file)
      trimmed = line.trim
      if trimmed.matches(".*for.*")
    } yield trimmed.length
  }

  // try/catch 표현식.
  def tryExpression(): Unit = {
    // try 표현식
    var n = 10
    val half = if (n % 2 == 0) n / 2 else throw new RuntimeException("n numst be even")

    // 발생한 예외 잡기
    import java.io.FileReader
    import java.io.FileNotFoundException
    import java.io.IOException

    val f = new FileReader("input.txt")
    try {
      // use file
    } catch {
      case ex: FileNotFoundException =>
      case ex: IOException =>
    } finally {
      f.close()
    }
  }

  // Match 표현
  def matchExpression(args: Array[String]): Unit = {
    val firstArg = if (args.length > 0) args(0) else ""
    firstArg match {
      case "salt" => println("pepper")
      case "chips" => println("slasa")
      case "eggs" => println("bacon")
      case _ => println("hub?")
    }

    val friend =
      firstArg match {
        case "salt" => "pepper"
        case "chips" => "salsa"
        case "eggs" => "bacon"
        case _ => "huh?"
      }
    println(friend)
  }

  // continue/break 문은 스칼라에 없음.
  var args: Array[String] = Array("a", "b")
  def changeToScalaStyle(): Unit = {
    // Java
    //   int i = 0;
    //   boolean foundI = false;
    //   while (i < args.length) {
    //     if (args[i].sartsWith("-")) {
    //       i = i + 1;
    //       continue;
    //     }
    //     if (args[i].endsWith(".scala")) {
    //       foundI= true;
    //       break;
    //     }
    //     
    //     i = i + 1;
    //   }

    // Java => Scala
    var i = 0
    var foundIt = false
    while (i < args.length && !foundIt) {
      if (!args(i).startsWith("-")) {
        if (args(i).endsWith(".scala")) {
          foundIt = true
        }
      }
      i = i + 1
    }

    // apply recursive
    def searchFrom(i: Int): Int = {
      if (i >= args.length) -1
      else if (args(i).startsWith("-")) searchFrom(i + 1)
      else if (args(i).endsWith(".scala")) i
      else searchFrom(i + 1)
    }
    i = searchFrom(0)

    // Scala에서 Break 사용 - break에서 예외를 던지고 breakable에서 예외를 잡는 방법이로 구현. break호출 메서드와 breakable 호출 메서드가 같지 않아도 된다.
    import scala.util.control.Breaks._
    import java.io._
    val in = new BufferedReader(new InputStreamReader(System.in))
    breakable {
      while (true) {
        println("? ")
        if (in.readLine() == "") break
      }
    }
  }

  // Sample - 곱셈표
  def productTable() : Unit = {
    def makeRowSeq(row: Int) = {
      for (col <- 1 to 10) yield {
        val prod = (row * col).toString()
        val padding = " " * (4 - prod.length)
        padding + prod
      }
    }
    
    def makeRow(row: Int) = makeRowSeq(row)
    def multiTable() = {
      val tableSeq = // 한 열에 해당하는 문자열의 시퀀스
      for (row <- 1 to 10)
        yield makeRow(row)
      
      tableSeq.mkString("\n")
    }
  }
}