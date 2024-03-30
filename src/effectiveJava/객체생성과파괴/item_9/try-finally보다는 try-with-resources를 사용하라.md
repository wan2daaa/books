# try-finally보다는 try-with-resources를 사용하라

- 자바 라이브러리에는 close 메서드를 호출해 직접 닫아줘야 하는 자원이 많다.
  - e.x. `InputStream, java.sql.Connection` ...
- 자원 닫기는 클라이언트가 놓치기 쉬워서 예측할 수 없는 성능 문제로 이어지기도함
  - 이런 자원들 상당수가 finalizer를 사용하지만 finalizer는 그렇게 믿을만 하지 않다

---

- 전통적으로 **자원이 제대로 닫힘을 보장하는 수단**으로 `try-finally` 가 쓰였다.
  - **예외가 발생하거나**, 
  - **메서드에서 반환되는 경우**를 포함해서

```java
  static String firstLineOfFile(String path) throws IOException {
  BufferedReader br = new BufferedReader(new FileReader(path));
  try {
    return br.readLine();
  }finally {
    br.close();
  }
}
```
- 나쁘지 않지만, 자원을 하나 더 사용한다면 ? 
```java
  private static final int BUFFER_SIZE = 10000;
  static void copy(String src, String dst) throws IOException {
    InputStream in = new FileInputStream(src);
    try {
      OutputStream out =new FileOutputStream(dst);
      try {
        byte[] buf = new byte[BUFFER_SIZE];
        int n ;
        while ((n = in.read(buf)) >= 0) 
          out.write(buf, 0 , n);
      }finally {
        out.close();
      }
    }finally {
      in.close();
    }
  }
```
- try-finally 문을 제대로 사용한 앞의 두 코드 예제에조차 미묘한 결점이 있다.
- 예외는 try 블록과 finally 블록 모두에서 발생할 수 있는데,
  - 첫번째 예제 코드에서 만약 물리적 오류가 발생하여
    - readLine 메서드가 예외를 던지고,
      - 마찬가지로 close 메서드도 실패하면
  - 결국 두번째 예외가 첫번째 예외를 집어 삼켜 버린다.
    - 그렇게 되면 디버깅을 매우 어렵게 한다.


---


- 이러한 문제들은 자바 7 에서 추가된 `try-with-resources` 덕에 모두 해결됨 
- 이 구조를 사용하려면 **해당자원이 AutoClosable 인터페이스를 구현해야한다.**
  - AutoClosable은 단순히 void를 반환하는 close 메서드 하나만 덩그러니 정의한 인터페이스이다.
- 우리도 **닫아햐 하는 자원을 뜻하는 클래스를 작성**한다면, **Autoclosable을 반드시 구현**해야한다.

- try-finally 로 작성한 코드를 try-with-resources로 바꿔 작성한 코드이다.
```java
  static String firstLineOfFile(String path) throws IOException {
    try (BufferedReader br = new BufferedReader(
        new FileReader(path))) {
      return br.readLine();
    }
  }
  
  static void copy(String src, String dst) throws IOException {
    try (InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst)) {
      byte[] buf = new byte[1000];
      int n;
      while ((n = in.read(buf)) >= 0) {
        out.write(buf, 0, n);
      }
    }
  }
```

- `try-with-resources` **버전이 짧고 읽기 수월**할 뿐 아니라 **문제를 진단하기도 훨씬 좋다**.
  - firstLineOfFile을 생각해 보자
    - readLine 과 (코드에는 없는) close 호출 양쪽에서 예외가 발생하면,
      - -> close에서 발생한 예외는 숨겨지고,
      - readLine에서 발생한 예외가 기록된다.
    - 이처럼 실전에서는 프로그래머에게 보여줄 예외 하나만 보존되고 여러 개의 다른 예외가 숨겨질 수도 있다.
      - 숨겨진 예외들도 그냥 버려지지않고, `suppressed`라는 꼬리표를 달고 출력된다.
      - 또, 자바 7에서 Throwable에 추가된 `getSuppressed` 메서드를 이용하면 프로그램 코드에서 가져올 수 있다.

---


- 보통의 try-finally 절 처럼 **try-with-resources 에서도 catch 절을 쓸 수 있다.**
- catch 절 덕분에 try 문 중복을 피하고 다수의 예외를 처리할 수 있다.

```java
  static String firstLineOfFile(String path, String defaultVal) {
    try (BufferedReader br = new BufferedReader(
        new FileReader(path))) {
      return br.readLine();
    } catch (IOException e) {
      return defaultVal;
    }
  }
```
## 핵심정리
> 꼭 회수해야 하는 자원을 다룰 때는 try-finally 말고, try-with-resources를 사용하자.
> 예외는 없다. 코드는 더 짧고 분명해지고, 만들어지는 예외정보도 훨씬 유용하다.
> try-finally로 작성하면 실용적이지 못할 만큼 코드가 지저분해지는 경우라도, 
> try-with-resources로는 정확하고 쉽게 자원을 회수할 수 있다.