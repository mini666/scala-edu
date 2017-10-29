# [흐름 제어 추상화](https://github.com/mini666/scala-edu/blob/master/src/main/scala/edu/scala/ch9/FlowControlAbstraction.scala)
## 코드 중복 줄이기
함수를 인자로 받는 함수를 고차함수라 하는데 이러한 고차 함수는 코드의 중복를 제거하고 간단하게 할 수있다.
## 클라이언트 코드 단순하게 만들기
스칼라 컬렉션 타입의 특별 루프 메서드(exists ...)는 루프 없이 클라이언트 코드를 간결하게 만들어 준다.
이런 특별 루프 메서드 정의는 Traversable이라는 트레이트에 있다. List, Set, Map은 Traversable 트레이트를 확장한다.
## 커링
본래 언어에서 지원하는 듯한 제어 추상화 구문을 만드는 방법을 이해하려면, 먼저 함수 언어에서 사용되는 기법 중 커링(Currying)을 이해해야 한다.
커링한 함수는 인자 목록이 하나가 아니고 여럿이다.

```
def curriedSum(x: Int)(y: Int) = x + y
curriedSum(1)(3)
```
curriedSum을 호출하는 것은 실제로는 2개의 전통적인 함수를 연달아 호출한 것이다. 첫번째 함수 호출은 Int 타입인 x를 인자로 받고, 호출 가능한 함수 값을 반환한다. 그 함수 값은 Int 타입의 인자 y를 취한다.
위치 표시자를 이용하면 다음과 같이 curriedSum을 부분 적용 함수로 사용할 수 있다.

```
val onePlus = curreidSum(1)_        // _ 전에 공백을 넣을 필요가 없다. println _ 과는 다르다.
onePlus(1)
```
## 새로운 제어 구조 작성
빌려주기 패턴(loan pattern)을 이용한 자바의 autoclosable 과 같은 제어 추상화를 만들 수 있다.  
좀 더 제어 구조처럼 보이게 하는 방법은 인자 목록을 감쌀 때 소괄호가 아닌 중괄호를 사용하는 것이다. 스칼라에서는 어떤 메서드를 호출하든 인자를 단 하나면 전달하는 경우 소괄호 대신 중괄호를 사용할 수 있다.

```
println("Hello, world!")
println { "Hello, world!" }
```
중괄호를 사용할 수 있게 한 이유는 클라이언트 프로그래머가 중괄호 내부에 함수 리터럴을 사용하도록 하기 위해서이다.
## 이름에 의한 호출 파라미터
if 및 while과 유사하게 중괄호 사이에 값을 전달하지 않는 형태로 구현하고 싶을때 이름에 의한 호출 파라미터를 사용할 수 있다.  
함수 인자 선언시 () => 가 아닌 =>을 사용하면 된다.  
빈 파라미터 목록인 ()를 생략할 수 있는 이름에 의한 호출 파라미터 타입은 파라미터에서만 사용할 수 있다.