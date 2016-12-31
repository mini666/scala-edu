package edu.scala.ch13

class Fruits(n: String, c: String) {
  val name = n
  val color = c
}

object Fruits {
  val menu = List(new Fruits("pear", "yellow"), new Fruits("apple", "red")) 
}