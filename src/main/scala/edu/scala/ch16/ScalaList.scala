package edu.scala.ch16

class ScalaList {

  def append[T](xs: List[T], ys: List[T]): List[T] = xs match {
    case List() => ys
    case x :: xs1 => x :: append(xs1, ys)
  }

  // 첫번째 인자의 크기만큼 시간이 걸리고 반복적으로 호출되므로 성능이 좋지 않다.
  def rev[T](xs: List[T]): List[T] = xs match {
    case List() => xs
    case x :: xs1 => rev(xs1) ::: List(x) // rev(xs1) :: x와  무슨 차이인가? 아마도 :: 가 맞는듯.
  }

  // 개선된 rev
  def reverseLeft[T](xs: List[T]) = (List[T]() /: xs){(ys, y) => y :: ys}
  
  // Merge Sort
  def msort[T](less: (T, T) => Boolean)(xs: List[T]): List[T] = {
    def merge(xs: List[T], ys: List[T]): List[T] = {
      (xs, ys) match {
        case (Nil, _) => ys
        case (_, Nil) => xs
        case (x :: xs1, y :: ys1) =>
          if (less(x, y)) x :: merge(xs1, ys)
          else y :: merge(xs, ys1)
      }
    }

    val n = xs.length / 2
    if (n == 0) xs
    else {
      val (ys, zs) = xs splitAt n
      merge(msort(less)(ys), msort(less)(zs))
    }
  }
}

object ScalaList {
  def main(args: Array[String]): Unit = {
    val test = new ScalaList

    println("------------------- merge sort test ----------------------")
    var sourceList = List(5, 7, 1, 3)
    println(s"source : $sourceList")
    println(s"result : ${test.msort((x: Int, y: Int) => x < y)(sourceList)}")

    val intSort = test.msort((x: Int, y: Int) => x < y) _ // Currying
    val reverseIntSort = test.msort((x: Int, y: Int) => x > y) _ // 내림차순 정렬

    val mixedInts = List(4, 1, 9, 0, 5, 8, 3, 6, 2, 7)
    println(s"source : $mixedInts")
    println(s"result(asc) : ${intSort(mixedInts)}")
    println(s"result(desc) : ${reverseIntSort(mixedInts)}")

    println("\n------------------- range ----------------------")
    println(List.range(1, 5)) // 1은 포함하고 5는 포함하지 않는다.

    println("\n------------------- flatMap ----------------------")
    // 1 <= j < i < 5인 모든 (i, j) 순서쌍의 리스트
    val orderPairList = List.range(1, 5) flatMap (
      i => List.range(1, i) map (j => (i, j)))
    println(orderPairList)
    for (i <- List.range(1, 5); j <- List.range(1, i)) yield (i, j)

    println("\n------------------- foreach --------------------")
    var sum = 0
    println(s"summation of 1 to 10 : ${List.range(1, 11) foreach (sum += _)}")

    // filter
    println("\n------------------- filter --------------------")
    val result = List.range(1, 5).filter { _ % 2 == 0 } // 짝수만 고르기.
    println(s"even number = $result")

    // partition
    println("\n------------------- partition --------------------")
    val result2 = List.range(1, 5).partition(_ % 2 == 0) // 짝수/홀수 분할.
    println(s"(even, odd) = $result2")

    // find
    println("\n------------------- find --------------------")
    var result3 = List.range(1, 5).find(_ % 2 == 0) // 짝수/홀수 분할.
    println(s"first even number = $result3")
    result3 = List.range(1, 5).find(_ <= 0)
    println(s"none result = $result3")

    // takeWhile
    println("\n------------------- takeWhile --------------------")
    sourceList = List(1, 2, 3, -4, 5)
    println(s"source : $sourceList")
    println(s"result : ${sourceList takeWhile (_ > 0)}")

    // dropWhile
    println("\n------------------- dropWhile --------------------")
    println(s"source : $sourceList")
    println(s"result : ${sourceList dropWhile (_ > 0)}")

    // forall, exists
    println("\n------------------- forall/exists --------------------")
    def hasZeroRow(m: List[List[Int]]) = {
      m exists (row => row forall (_ == 0))
    }
    //    hasZeroRow(diag3)

    // /:, :\
    println("""\n------------------- /:,:\--------------------""")
    def total(xs: List[Int]): Int = (0 /: xs)(_ + _)
    def product(xs: List[Int]): Int = (1 /: xs)(_ * _)
    val words = List("the", "red", "forx", "jumped", "the", "lazy", "dog")
    val result4 = ("" /: words)(_ + " " + _)    // 각 단어들 사이에 공백 추가. 가장 처음에도 공백존재
    println(s"left fold result(/:) : $result4")
    val result5 = (words.head /: words.tail)(_ + " " + _)  // 가장 처음 공백도 제거.
    println(s"left fold result(/:) : $result5")
    
    // flatten의 경우 오른쪽 폴드가 더 효율적이다. 
    // xs ::: ys 연산자는 첫번째 인자 xs에 비례해 시간이 걸린다.
    def flattenLeft[T](xss: List[List[T]]) = (List[T]() /: xss)(_ ::: _)      // xss.head를 n-1번 복사. n은 xss의 크기. 
    def flattenReight[T](xss: List[List[T]]) = (xss :\ List[T]())(_ ::: _)
    
    // sortWith
    println("\n------------------- sortWith --------------------")
    println(s"source : $sourceList")
    println(s"result : ${sourceList sortWith (_ < _)}")
    println(s"source : $words")
    println(s"result : ${words sortWith (_.length < _.length)}")
    
    // 타입 추론.
    println("\n------------------- 타입추론 --------------------")
    val charList = List('a', 'b', 'c', 'd', 'e')
    val func = test.msort((x: Char, y: Char) => x > y) _
    println(s"msort result : ${func(charList)}")
    
    println(s"sortWith result : ${charList sortWith (_ > _)}")      // 타입 추론 성공 : charList를 통해 (Char, Char) => Boolean 함수임을 추론한다.
//    test.msort(_ > _)(charList)                                   // 타입 추론 실패 : 정보가 없으므로 _ > _의 타입을 추론할 수 없다.
    println(s"msort result : ${test.msort[Char](_ > _)(charList)}") // 명확하게 타입 인자를 전달하여 해결.

    // 인자의 순서를 바꾸면 타입 추론이 가능. 리스트가 먼저 오고 함수가 나중에.
    def msortSwapped[T](xs: List[T])(less: (T, T) => Boolean): List[T] = {
      def merge(xs: List[T], ys: List[T]): List[T] = {
        (xs, ys) match {
          case (Nil, _) => ys
          case (_, Nil) => xs
          case (x :: xs1, y :: ys1) =>
            if (less(x, y)) x :: merge(xs1, ys)
            else y :: merge(xs, ys1)
        }
      }

      val n = xs.length / 2
      if (n == 0) xs
      else {
        val (ys, zs) = xs splitAt n
        merge(msortSwapped(ys)(less), msortSwapped(zs)(less))
      }
    }
    
    println(s"msortSwapped result : ${msortSwapped(charList)(_ > _)}")    // 타입추론 성공.
    
  }
}