package edu.scala.ch17

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.ArrayBuffer
import scala.collection.immutable.TreeSet
import scala.collection.immutable.TreeMap

object ScalaCollection {
  
  def main(args: Array[String]): Unit = {
    // ListBuffer
    val buf = new ListBuffer[Int]
    buf += 1
    buf += 2
    println(s"$buf")
    3 +=: buf
    println(s"ListBuffer : $buf")
    println(s"list : ${buf.toList}")
    
    // ArrayBuffer
    val arrayBuffer = new ArrayBuffer[Int]()
    arrayBuffer += 12
    arrayBuffer += 15
    println(s"ArrayBuffer : $arrayBuffer")
    
    // Set
    val text = "See Spot run. Run, Spot. Run!"
    val wordArray = text.split("[ !,.]+")
    val words = scala.collection.mutable.Set.empty[String]
    for (word <- wordArray) words += word.toLowerCase
    println(s"Mutable Set : $words")
    
    // Map
    def countWords(text: String) = {
      val counts = scala.collection.mutable.Map.empty[String, Int]
      for (rawWord <- text.split("[ ,!.]+")) {
        val word = rawWord.toLowerCase
        val oldCount = counts.getOrElse(word, 0)
//        val oldCount = 
//          if (counts.contains(word)) {
//            counts(word)
//          } else {
//            0
//          }
        counts += (word -> (oldCount + 1))
      }
      
      counts
    }
    println(s"Map : ${countWords("See Spot run! run, Spot. Run!")}")
    
    // SortedSet, SortedMap
    val ts = TreeSet(9, 3, 1, 8, 0, 2, 7, 4, 6, 5)
    println(s"treeSet : $ts")
    val cs = TreeSet('f', 'u', 'n')
    println(s"treeSet : $cs")
    
    var tm = TreeMap(3 -> 'x', 1 -> 'x', 4 -> 'x')
    println(s"treeMap : $tm")
    tm += (2 -> 'x')
    println(s"treeMap : $tm")
  }
  
  // 튜플
  def longestWord(words: Array[String]) = {
    var word = words(0)
    var idx = 0
    for (i <- 1 until words.length) 
      if (words(i).length > word.length) {
        word = words(i)
        idx = i
      }
    (word, idx)
  }
  var longest = longestWord("The quick brown fox".split(" "))
  println(s"tuple : $longest")
  println(s"tuple(word, index) : $longest._1, $longest._2")
  val (word, idx) = longest
  println(s"word : $word, index : $idx")
}