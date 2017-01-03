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
* 트레이트.
  * 트레이트를 믹스인(Mix-in)할때는 extends(두번째부터는 with) 키워드를 사용하는데 이때 트레이트의 슈퍼클래스를 암시적으로 상속하고 트레이트를 믹스인한다.
  * 트레이트와 클래스의 다른점.
    * 클래스 파라미터를 가질 수 없다. 주 생성자에 전달할 파라미터를 트레이트 정의에 넣을 수 없다.
    * 클래스는 super호출을 정적으로 바인딩하고 트레이트는 동적으로 바인딩한다. 이로 인해 트레이트에서 Stackable Modification이 가능해진다.
  * 비교에 의해 순서를 매길 수 있는 클래스 구현시 Ordered 트레이트 믹스인. <, <=, >, >= 지원.
  * abstract override 메서드가 어떤 트레이트에  그 트레이트를 반드시 abstract override가 붙은 메서드에 대한 구체적 구현을 제공하는 클래스에 믹스인해야만 한다.
  * 믹스인 순서가 중요. 오른쪽부터 먼저 적용한다.
    * super를 호출하는 경우 선형화된 순서에서 오른쪽에 위치한 첫번째 구현부가 호출된다.
    ```
    class Animal
    trait Furry extends Animal
    trait HasLegs extends Animal
    trait FourLegged extends HasLegs
    class Cat extends Animal with Furry with FourLegged
    ```
    * Cat -> FourLegged -> HasLegs -> Furry -> Animal -> AnyRef -> Any
  * 트레이트와 추상 클래스 선택.
    * **어떤 행위를 재사용하지 않을 거라면**, 클래스.
    * **서로 관련이 없는 클래스에서 어떤 행위를 여러 번 재사용해야 한다면**, 트레이트.
    * **자바 코드에서 스칼라의 내용을 상속해야 한다면**, 추상 클래스. 자바에는 코드가 들어있는 트레이트와 같은 개변이 없어 트레이트를 상속하면 애매(자바8에서는?). 추상 메서드만 있는 트레이트는 자바의 인터페이스와 동일하므로 얼마든지 상속해도 좋음.
    * **컴파일한 바이트코드 형태로 배포할 예정이고** 배포한 내용을 누군가가 상속해 사용할 것 같다면 추상 클래스. *특정 트레이트에 멤버를 추가하거나 제거하면, 그 트레이트를 상속하는 모든 클래스는 그 자신의 변경 여부와 관계없이 다시 컴파일해야만 한다.* 만일 코드를 이용하는 클라이언트 측에서 트레이트를 상속하지 않고 호출만 한다면 트레이트를 이용해도 문제 없음.
    * **효율이 중요하다면** 클래스를 좀 더 사용. *자바 런타임은 클래스 멤버에 대한 가상 메서드 호출을 인터페이스의 메서드 호출보다 빠르게 수행.*
    * **여전히 판단이 서지 않는다면** 트레이트 사용. 언제든지 클래스로 바꿀 수 있다.
* 패키지와 임포트.
  * 스칼라 임포트와 자바 임포트의 차이.
    * 어느 곳에나 나타날 수 있다.
    * 패키지뿐만 아니라 (싱글톤 또는 일반) 객체도 참조할 수 있다.
    * 불러온 멤버 이름 중 일부를 숨기거나 다른 이름을 지정할 수 있다.
  * 중괄호로 둘러싼 임포트 셀렉터를 사용하여 이름을 감추거나 바꿀수 있다.
    * import Fruits.{Apple, Orange}
    * import Fruits.{Apple => McIntosh, Orange}
    * import java.{sql => s} : 패키지 이름 
    * import Fruits.{Applie => McIntosh, _}
    * import Fruits.{Pear => _, _} : Pear를 제외한 모든 이름 불러오기.
  * 암시적 임포트.
    * import java.lang._
    * import scala._
    * import Predef._
  * 뒤에서 임포트한 것이 앞에서 임포트한 것을 가린다. StringBuilder는 java.lang.StringBuilder가 아닌 scala.StringBuilder를 가리킨다.
  * 접근 수식자(제한자)
    * private 멤버는 그 정의를 포함한 클래스나 객체 내부에서만 접근 가능. Inner Class에도 동일하게 적용. 자바와 다르다?(코드 참고)
    * protected 멤버는 자바보다 더 제한적. 멤버를 정의한 클래스의 서브클래스에서만 접근 가능. 자바는 동일 패키지에서도 접근 가능.
    * 아무것도 없으면 public 이다.
    * 보호 접근.
    
    수식자 없음 | 전체 접근 가능(공개)
    private[P] | 바깥쪽 패키지(P) 내부에서 접근 가능
    private[P] | P가 자신의 패키지일때 자바의 패키지 접근과 동일
    private[C] | C가 클래스일때 자바의 protected와 같음.
    private[C] | C가 자기 자신일때 자바의 private과 같음.
    private[this] | 자기 자신 객체에서만 접근 가능.
    
  * 비공개 또는 보호 접근에 대해 동반 객체와 클래스에 동일한 권리를 준다. 클래스가 동반 객체의 비공개 멤버에 모두 접근할 수 있는 것처럼 객체도 동반 클래스의 모든 비공개 멤버에 접근할 수 있다.
  * 싱글톤 객체의 서브클래스를 만들 수 없으므로 동반 객체 안에서 protected 멤버를 선언하는 것은 말이 안된다.
  * 패키지 객체
    * 패키지 전체 스코프에 도우미 메서드를 두고 싶다면 패키지 최상위 수준에 넣으면 된다. (자바의 유틸 클래스 내의 public static 메서드들?)
    * 패키지 내에서 사용할 타입별명(type alias)과 암시적 변환을 넣기 위해 사용하는 경우가 많다.
* 단언문과 단위테스트
  * assert/ensuring 과 같은 Predef에 정의된 메서드 사용.
  * JVM에 -ea나 -da 명령행 옵션을 사용하면 단언의 동작을 켜거나 끌 수 있다. => 역주: 실행해본 바로는(오라클 자바 1.8과 스칼라 2.11) -ea나 -da가 스칼라 단언문에는 영향을 끼치지 못한다. 다만 scalac에 -Xdisable-assertions를 명령행 인자로 넘기면 단언문을 아예 제거하고 컴파일한다.
  * 스칼라에서 단위 테스트
    * ScalaTest, Spec, ScalaCheck 등 다양한 도구 존재.
    * ScalaTest
      * scalatest.Suite를 확장하는 클래스를 만들어 테스트 메서드 정의.
      * FunSuite를 확장하면 좀 더 편하게 테스트케이스 작성할 수 있음.
      * '===' 연산자를 사용하면 '3 did not equal 2'와 같은 메시지를 볼 수 있다.
      * expect(2) { ele.width }와 같이 사용하면 'Expected 2, but got 3'와 같은 메시지를 볼 수 있다.
      * 발생되는 예외 체크에는 intercept[예외] { 코드 } 와 같이 사용한다.
      * JUnit에서 ScalaTest의 단언 문법을 사용하기 원한다면 JUnit3Suite의 서브클래스를 만들면 된다.
      * JUnitWrapperSuite를 사용하면 JUnit으로 작성한 자바 테스트를 ScalaTest를 통해 실행 가능하다.
      * Junit4와 TestNG도 지원.
    * 명세를 테스트로 사용하기
      * **동작주도개발(BDD Behavior-Driven-Development)** 테스트 스타일은 기대하는 코드 동작을 사람이 읽을 수 있는 명세로 작성.
      * ScalaTest는 Spec, WordSpec, FlatSpec, FeatureSpec등 여러 트레이트 제공.
    * 프라퍼티 기반 테스트 - ScalaCheck
      * 툴이 자동으로 프라퍼티에 대한 테스트 데이터를 생성하고 테스트 수행.
    * 테스트 조직과 실행
      * 수동으로 Suite를 처리하려면 nestedSuites 메서드를 오버라이드 하거나 포함시키고 싶은 Suite를 SuperSuite 클래스의 생성자에 전달.
      * 자동으로 포함시키기 위해서는 ScalaTest의 Runner에 패키지 이름을 제공한다.
* 케이스 클래스와 패턴 매치
  * 케이스 클래스
    * 컴파일러는 클래스 이름과 같은 이름의 팩토리 메서드를 추가한다. new Var("x") 대신 Var("X") 사용 가능.
    * 케이스 클래스의 파라미터 목록에 이쓴 모든 클래스 파라미터에 암시적으로 val 접두어를 붙인다. 각 파라미터가 클래스의 필드도 된다.
    * 컴파일러는 케이스 클래스에 toString, hashCode, equals 메서드의 구현을 추가한다.
    * 컴파일러는 케이스 클래스에서 일부를 변경한 복사본을 생성하는 copy 메서드를 추가. 기존의 인스턴스에서 하나 이상의 속성을 바꾼 새로운 인스턴스를 생성할 때 유용. copy 메서드는 디폴트 파라미터와 이름 있는 파라미터를 제공.
  * 패턴 매치
    * 상수 패턴은 == 연산자로 매치.
      * 리터럴, val이나 싱글톤 객체도 상수로 사용.
    * 변수만을 사용한 패턴은 모든 값과 매치. 와일드카드 패턴(_)도 모든 값과 매치.
      * 소문자로 시작하는 이름은 패턴변수로 취급하고 나머지는 상수로 간주.
      * 소문자 이름을 상수 패턴으로 사용하고자 한다면
        * 어떤 객체의 필드인 경우 짧은 이름이 아니라 긴 이름을 사용. pi는 변수패턴이지만 this.pi, obj.pi는 상수 취급.
        * 역따옴표로 변수 이름을 감싸면 상수 패턴이다. `pi`
    * 생성자 패턴(Class(...))
    * 시퀀스 패턴
      * 배열이나 리스트 같은 시퀀스 타입에 대한 매치
      * 생상자 패턴과 동일한 문법 지원하고 패턴 내부에 원하는 개수만큼 원소를 명시할 수도 있음. 길이를 한정하지 않고 싶을때는 _* 사용.
    * 튜플 패턴
    * 타입지정 패턴
      * isInstanceOf[T], asInstanceOf[T]를 사용하는 것보다 타입지정 패턴을 사용하는 것이 바람직.
      * Map[_. _]에서 _ 대신 소문자로 시작하는 타입 변수를 쓸 수도 있다.
      * Collection류에서는 타입 이레이저로 인해 타입이 소거되어 타입을 명시한 컬렉션을 패턴 패치할 수 없으나, 배열은 타입이 소거되지 않아 패턴 매치 가능.
    * 변수 바인딩
      * 변수 이름 다음에 @ 기호를 넣은 것. 패턴 매치에 성공하면 매치된 객체(@ 뒤에 있는)를 변수에 저장.
    * 패턴 가드 - 패턴 뒤에 if문.
    * 패턴 겹침 - 구체적인 매칭이 앞에 일반적인 매칭이 뒤에 위치해야 한다.
  * 봉인한 클래스
    * 클래스와 같은 파일이 아닌 다른 곳에서 새로운 서브클래스를 만들 수 없다. 그렇기 때문에 빠진 패턴 매치에 대한 내용을 컴파일러가 경고 표시해 준다.
    * 최상위 클래스에만 sealed 키워드를 넣으면 된다.
  * Option 타입 - None, Some(x)로 표현. Map.get 과 같은 컬렉션의 몇몇 표준 연산들의 결과값.
    * 옵션값을 분리해 내는 가장 일반적인 방법이 패턴 매치. 
  * 기타
    * 변수 정의에서 패턴 사용하기. 
    * case 시퀀스로 부분 함수 만들기
    * for 표현식에서 패턴 사용.
* 리스트
  * 스칼라 리스트 타입은 공변적(Covariant)이다. 이는 S 타입이 T 타입의 서브타입이면 List[S]도 List[T]의 서브타입.
  * 빈 리스트는 List[Nothing].
  * 리스트는 Nil과 ::(콘즈)로 생성 가능. 콘즈는 중위 연산자이다.
  * 리스트의 기본 연산은 head/tail/isEmpty. head/tail은 비어있지 않은 리스트에서만 가능.
  * List 클래스의 1차 메서드 - 어떤 메서드가 함수를 인자로 받지 않는다면 그 메서드를 1차 메서드라 부른다.
    * 두 리스트 연결하기
      ::: - 두 리스트를 더한다. 자바의 addAll.
    * 분할 정복 원칙.
    * 리스트 길이 구하기 - length는 비교적 비싼 연산. 리스트 원소 개수만큼 시간이 걸린다. isEmpty가 더 효율적.
    * 리스트 양 끝에 접근하기 - init와 last. last는 head와 유사하게 가장 마지막 원소. init은 tail과 유사하게 마지막 원소를 제외한 나머지 원소들.
    * 리스트 뒤집기 - reverse
    * 접두사와 접미사 : drop, task, splitAt
      * drop : xs drop n - 첫번째에서 n번째까지 원소를 ㅔㅈ외한 xs 리스트의 모든 원소 반환
      * task : xs take n - xs 리스트의 처음부터 n번째가지 원소를 반환
      * splitAt : 주어진 인덱스 위치에서 리스트를 분할해서 두 리스트가 들어 있는 순서쌍을 반환. xs splitAt n = xs take n, xs drop n
    * 원소 선택: apply와 indices
      * List apply n = List(n). n값에 비례하여 시간이 걸리기 때문에 자주 사용되지 않는다.
      * indicess는 유효한 모든 인덱스 리스트를 반환.
    * flatten - 리스트의 리스트를 한 리스트로 만들기
    * zip과 unzip - 두 리스트를 순서쌍으로 묶기. 길이가 다른 경우 남는 것은 버린다.
      * zipWithIndex - 인덱스와 함께 순서쌍으로 만듦.
    * toString, mkString
    * iterator, toArray, copyToArray - 리스트 변환.
      * copyToArray는 리스트 원소를 어떤 배열의 특정 지짐부터 연속적으로 복사 - xs copyToArray (arr, start)
  * List 클래스의 고차 메서드.
    * map, flatMap, foreach
      * map, flatMap은 T => U 인 함수를 인자로 받고, foreach는 결과가 Unit 인 함수를 받는다.
    * filter, partition, find, takeWhile, dropWhile, span
      * filter : T => Boolean 타입의 함수를 받는다.
      * partition : filter와 같지만, 리스트의 순서쌍을 반환한다. 순서쌍에서 한 리스트는 술어(Predicate)가 true인 원소를 포함하며, 나머지 하나는 술어가 falsedls 원소를 포함한다.
        * xs partition p = (xs filter p, xs filter (!p()))
      * find : filter와 비슷하지만 주어진 술어 함수를 만족하는 모든 원소를 반환하지 않고 만족하는 첫번째 원소만 반환한다. Option[T](Some(x), None)를 반환한다.
      * takeWhile, dropWhile도 술어 함수를 받는다.
        * takeWhile은 리스트에서 술어를 만족하는 가장 긴 접두사를 반환하고 dropWhile은 제거한다.
    * forall, exists : 리스트 전체에 대한 술어
      forall은 술어함수를 받아 리스트의 모든 원소가 술어함수를 만족할때 결과가 true이다. exists는 하나라도 만족하면 true이다.
    * /:(왼쪽폴드), :/(오른쪽폴드) : 리스트 폴드.
      * 리스트의 원소들을 어떤 연산자를 가지고 결합히는 것. 결합법칙이 성립하는 경우 왼쪽/오른쪽 폴드는 동일한 결과가 나오나 효율은 다를 수 있다.
      * 슬래시의 방향이 각각 오른쪽 또는 왼쪽으로 치우친 트리의 그림과 비슷. 콜론의 위치에 따라서 결합 방향이 정해짐. 시작값을 콜론이 있는 쪽에 넣으면 된다.
      * foldLeft, foldRight 메서드가 동일한 기능을 수행.
    * sortWith : 리스트 정렬.
      * xs sortWith before 연산에서 before는 비교함수.
      * merge sort를 수행한다.
  * List 객체의 메서드.
    * List.apply : List(1, 2, 3)
    * List.range : List.range(from, until [, gap]) until 은 포함되지 않는다. 세번째 인자는 증감치.
    * List.fill : 같은 원소의 복사본을 0번 이상 반복한 리스트를 만듦.
      * List.fill(생성할 리스트의 길이)(반복할 원소)
      * List.fill(2, 3)('b') : 다차원 리스트 생성. List(List(b, b, b), List(b, b, b))
    * List.tabulate : 제공된 함수로 계산한 원소의 리스트를 생성. fill과 동일하나 원소가 아닌 함수로 작업.
    * List.concat : 여러 리스트를 연결한다.
  * 여러 리스트를 함께 처리
    * 튜플에 있는 zipped 메서드는 단일 리스트가 아닌 여러 리스트에서 동작. (List(10, 20), List(3, 4, 5)).zippped.map(_ * _) => List(30, 80)
    * zipped 한 함수는 map, exists, forall 등.
  * 스칼라의 타입 추론 알고리즘 이해
    * 스칼라의 타입 추론은 흐름기반(Flow Based)으로 동작한다. 메서드 적용인 m(args)에서, 타입 추론 로직은 메서드 m의 타입이 알려져 있는지를 먼저 검사. 만약 m에 타입이 있다면 그타입으로 부터 메서드에 적용할 인자의 예상 타입을 추론.
    * 함수 파라미터와 일반 파라미터가 있는 다형성 메서드 설계시 함수 인자를 별도의 커링한 파라미터 목록으로 맨 마지막에 위치 시킨다. <= 타입 추론 가능.