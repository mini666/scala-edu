package edu.scala

package object ch13 {
  
  def showFruit(fruit: Fruits) {
    import fruit._
    println(name + "s are " + color)
  }
}

package printmenu {
  import ch13.Fruits
  import ch13.showFruit
  
  object PrintMenu {
    def main(args: Array[String]) {
      for (fruit <- Fruits.menu) {
        showFruit(fruit)
      }
    }
  }
}