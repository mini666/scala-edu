# scala-edu
study scala
----------
* fsc(Fast Scala Compiler) 사용하면 더 빨리 컴파일 가능.
  * fsc <source files...> 
  * fsc -shutdown
* println("""|Welcome to Ultamix 3000.
             |Type "HELP" for help.""".stripMargin)
* 관례상 부수효과가 있다면 메서드 호출시 괄호, 그렇지 않으면 괄호를 사용하지 않는다.
* 클래스 
  * 필드나 메서드 정의에 들어가지 않는 코드는 생성자 코드임.
  * 보조 생성자는 `def this(...)`로 시작하고 첫구문은 `this(...)`로 다른 생성자를 호출해야함.
  * super는 주생성자에서만 호출할 수 있다.
  * 스칼라에서 상수표현은 첫글자만 대문자로 하는 클래스명과 같이 표현한다.
  * 연산자 식별자는 하나 이상의 연산자 문자로 이루어 지고 연산자 문자는 +,:,?,~,# 등 출력 가능한 아스키 문자.
  * :-> 는 스칼라 컴파일러가 내부에서 $colon$minus$greater 로 바꾸는데 자바에서 이를 이용하여 접근할 수 있다.
  * 혼합 식별자는 영숫자 뒤 밑줄이 오고 그 다음 연산자 식별자가 온다. `unary_+`, `myvar_=`
  * 리터럴 식별자 `...` 역따옴표로 둘러싼 문자열.
  * yield는 스칼라의 식별자이므로 Thread.yield()가 아닌 Thread.`yield`()로 해야 함.

* 내장 제어 
  * try/catch 절도 표현식이다. finally에서는 값을 변경하지 말고 자원의 release만을 담당. `def f(): Int = try { return 1 } finally { return 2 }`의 결과는 2이고 `def g(): Int = try { 1 } finally { 2 }`의 결과는 1이다. 
  * while문은 표현식이 아니라 루프문
  * 할당의 결과는 Unit이므로 아래 코드는 ()과 ""를 비교하는 것이므로 항상 false.
```
var line = ""
while ((line = readLine()) != "") // 작동하지 않음.
  println("Read : " + line)
```

* 함수/클로저.
  * 함수 리터럴로부터 실행 시점에 만들어낸 객체인 함수 값(객체)을 `클로저`라 한다. 자유 변수에 대한 바인딩을 Capturing하여 자유변수가 없게 닫는(Closing) 행위에서 따온 말.
  * Tail Recursive
    * `-g:notailcalls` 옵션을 컴파일 옵션에 추가하여 꼬리 최적화 수행 안할 수도 있음.
    * 재귀가 간접적으로 일어날때, 마지막 호출이 함수 값을 호출하는 경우에는 최적화가 일어나지 않는다.
    * 꼬리 재귀 최적화는 오직 마지막 연산으로 자기 자신을 호출하는 경우에만 이뤄진다.

* 흐름 제어 추상화.
  * 함수를 인자로 받는 함수를 고차함수라 한다.
  * 모든 함수는 호출에 따라 달라지는 비공통 부분과 호출과 상관없이 일정한 공통부분으로 나눌 있는데 공통부분은 함수 본문이며 비공통부분은 반드시 인자로 주어져야 한다.
  * 인자를 하나만 전달하는 경우 소괄호 대신 중괄호를 사용할 수 있다. 여러개인 경우에도 커링을 사용하면 가능하다.
  
* 상속과 구성.
  * 단일 접근 원칙 : 필드나 메서드 중 어떤 방식으로 속성를 정의하더라도 클라이언트 코드에 영향을 끼치지 않아야 한다. Side-effect가 있으면 괄호 사용, 그렇지 않으면 괄호 사용 안 함.
  * 필드가 파라미터 없는 메서드를 오버라이드할 수 있다.
  * 일반적으로 자바에는 4개의 네임스페이스(필드, 메서드, 타입, 패키지)가 있는 반면 스칼라는 2개(값:(필드,메서드,패키지,싱글톤객체), 타입(클래스,트레이트))만 있다.
  * 깨지기 쉬운 기반클래스 문제 - 상위 클래스에서 메서드를 추가할때 해당 메서드가 이미 하위 클래스에 존재하는 경우.

* 스칼라의 계층 구조.
  * 모든 클래스는 Any의 서브 클래스, Nothing은 모든 다른 클래스의 서브클래스.
  * Any의 하위 클래스로 AnyVal, AnyRef가 있으며 AnyVal의 하위 클래스(Int, Long, Float, Double, Short, Byte, Boolean, Char, Unit)를 제외한 나머지 클래스는 모두 AnyRef를 상속한다. 
  * 스칼라 클래스들은 ScalaObject(ScalaObject도 AnyRef의 하위클래스)를 상속하고 
  * Null은 AnyRef를 상속하는 모든 클래스의 서브클래스이다.
  * Nothing은 AnyRef, AnyVal을 포함한 모든 클래스의 서브클래스이다.
  * Any에 있는 메서드.
    * final def ==(that: Any): Boolean
    * final def !=(that: Any): Boolean
    * def equals(that: Any): Boolean
    * def ##: Int
    * def hashCode: Int
    * def toString: String
  * ==와 equals는 동일. ##와 hashCode는 동일. ==와 !=는 final이므로 override할 수 없고 equals를 override해야 함.
  * 참조비교를 위해서는 AnyRef에 있는 eq, ne를 이용.
  * Nothing의 쓸모중 하나는 비정상종료를 표시하는 것. 모든 클래스의 공통클래스이므로 어느 함수에서나 사용 가능하다.
