#. 함수와 클로저
## 특별한 형태의 함수 호출
### 반복 파라미터
함수의 마지막 파라미터 반복 가능.  
별표(\*)를 인자의 타입 타입에 추가.  

```
def echo(args: String*) = for (arg <- args) println(arg)
```

반복 파라미터의 타입은 배열이다. String*로 선언한 파라미터의 타입은 실제로 Array[String]이다. 하지만 적절한 타입의 배열을 반복 파라미터로 직접 전달하려고 하면 컴파일 오류가 발생한다.
배열을 반복 인자로 전달하기 위해서는 콜론에 _* 기호를 추가해야 한다.

```
scala> echo(arr: _*)
```

### 이름 붙인 인자
일반적인함수 호출ㄹ에서 인자는 함수 정의의 파라미터 순서와 맞아야 한다. 이름 붙인 인자는 파라미터 목록에 정해진 순서와 다른 순서로 함수에 인자를 전달하게 해준다.

```
def speed(distance: Float, time: Float): Float = 
	distnace / time

speed(100, 10)
speed(time = 10, speed = 100)
```

위치 기반 인자와 이름 붙은 인자를 혼용할 수도 있는데 이와 같은 경우 위치 기반 인자를 먼저 쓴다.이름 붙인 인자는 보통 디폴트 인자 값과 함께 사용하는 경우가 많다.
### 디폴트 인자값
파라미터의 디폴트 값을 지정할 수 있고 디폴트 파라미터는 함수 호출시 인자를 생략할 수 있다. 
```
def printTime(out: java.io.PrintStream = Console.out) = 
	out.println("time = " + System.currentTimeMillis())

def printTime2(out: java.io.PrintStream = Console.out, divisor: Int = 1) = 
	out.println("time = " + System.currentTimeMillis() / divisor)

printTime2(out = Console.err)
printTime2(divisor = 1000)
```

## 꼬리 재귀
```
def approxiamte(guess: Double): Double = 
	if (isGoodEnough(guess) guess
	else approxiamte(improve(guess))

def approximateLoot(initailGuess: Double): Double = {
	var guess = initialGuess
	while (!isGoodEnough(guess))
		guess = improve(guess)
	guess
```

위 두 함수는 동일한 성능을 보임. 꼬리 최적화에 의해 컴파일된 코드가 동일하다.
### 꼬리 재귀 함수 추적
꼬리 최적화가 일어난 경우와 그렇지않은 경우의 에러 스택이 다르다. 꼬리 최적화가 일어난 경우 재귀함수 호출 스택이 보이지 않는다. *-g:notailcalls* 컴파일 옵션을 추가하여 강제적으로 꼬리 최적화를 하지 않을 수도 있다.
### 꼬리 재귀의 한계
꼬리 최적화는 함수의 마지막 연산으로 자기 자신을 호출하는 경우에만 일어나며 그 이외의 경우에는 일어나지 않는다. 자신을 가리키는 함수값의 경우에도 마찬가지이다.
```
val funValue = nestedFun _
def nestedFun(x: Int) {
	if (x != 0) { println(x); funValue(x - 1) }		// 꼬리 최적화가 일어나지 않는다.
}
```

