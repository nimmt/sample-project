---
marp: true
---

# タイトル：
# Scala による関数プログラミング

---

# 断り

### Scala 始めてまだ数ヶ月の初心者なので、暖かい目で見ていただけると嬉しいです。
### ＿○／|＿

---

# アジェンダ

- Scala とは何か？
- 関数言語 とは何か？
- その他 Scala で気になるやつ

---

# Scala とは何か？

## オブジェクト指向と関数型言語の特徴を統合したマルチパラダイムのプログラミング言語である

---

# なぜ Scala が作られることになったのか？

## 開発動機

> 公式サイトから - マーティン・オーダスキー

- 汎用言語はスケーラブルであり、同じ概念で、小さいプログラムも大きなプログラムも記述できるべきである
- スケーラビリティは関数型言語とオブジェクト指向言語のプログラミング概念を統合し、一般化することにより実現できる

<!-- スケーラビリティ（scalability）とは利用者や仕事の増大に適応できる能力・度合いのこと -->

---

# Scala の特徴

- オブジェクト指向言語
  - プリミティブな値は存在せず、継承やトレイトといった機能が使える
- 関数型プログラミングが可能
  - あとで色々話しますー(bow)
- 静的な型付き
  - 抽象化する仕組みや、型に起因するエラーをコンパイル時に見つけることができる
- Java との連携
  - Java VM で動くため、豊富な Java のライブラリを使用することが可能
- 並行処理
  - マルチコア CPU を意識した 「Actor」と呼ばれるライブラリをもっている
    ※ Scala 標準の Actor は非推奨となり、Akka の Actor を使用することを推奨

---

# オブジェクト指向言語 とは何か？

## データとコードのセットを基本要素にして物事を分析する考え方

# 関数型言語 とは何か？

## 副作用をもたない関数を使用してプログラムを構築する

---

### e.g.) 値が編集できない変数や不変コレクションが使える
```scala
// 編集できない変数
val msg: String = "Hello"
msg = "Workd" // error

// 配列型
val array = Array(1, 2, 3)
array :+ 4 // return Array(1, 2, 3, 4)
array :+= 4 // val のため再代入は error
array(1) = 20 // Array(1, 20, 3) に更新

// コレクション（immutable）
val list1 = List(1)
val list2 = 2 :: list1
// コレクション（mutable）
import scala.collection.mutable.ListBuffer
val list3 = ListBuffer(1)
list3 += 2 // ListBuffer(1, 2)
```

<!-- 配列とコレクションの違い
http://kechanzahorumon.hatenadiary.com/entry/2016/02/11/013338 -->

---

### e.g.) 関数自体を引数や戻り値にできる（高階関数）
```scala
// String 型引数を１つ取り、
// 「Int 型引数を２つ取り、String 型関数を返す関数」
// を返す関数
def getFunction(str: String): (Int, Int) => String
  = (x:Int, y:Int) => str + (x + y)

def function1 = getFunction("result1 is ")
function1(1, 2)
function1(4, 3)

def function2 = getFunction("result2 is ")
function2(1, 2)
```

関数は定義しているが、変数は存在しない。
オブジェクト指向で考えると、最初に指定される `result1 is ` を副作用として状態の保存が必要。

---

# 関数型言語の主な特徴

> 参考「ウィキペディア：関数型言語」

- 第一級関数と高階関数
- 純粋関数
- 再帰
- 厳格評価と遅延評価
- 型推論
- 参照透過性
- データ構造体
- ファンクタ、モナド、アプリカティブ

これら参考に scala ではどうなるか、少し調べてみました。

---

# 第一級関数と高階関数

### 関数を第一級オブジェクトとして扱える。

```scala
def getFunction(str: String): (Int, Int) => String
  = (x:Int, y:Int) => str + (x + y)
```

#### 補足
※ 第一級オブジェクトとは、生成、代入、演算、受け渡しといった基本的な操作が制限なくできること
※ 高階関数は第一級関数をサポートしてる前提で、関数を引数にしたり、戻り値とするような関数

<!-- https://syossan.hateblo.jp/entry/2016/07/16/211248 -->

---

# 純粋関数

### 同じ入力値を渡すたび、決まって同じ出力値が得られる

Scala は純粋関数ではないが、純粋関数にするための機能はそなわっている

---

# 再帰

### 繰り返す構文を使用せず、関数の中で自分自身を呼び出す

```scala
def sum(xs: List[Int]): Int = if (xs.isEmpty) 0 else xs.head + sum(xs.tail)

sum(List(1, 2, 3, 4, 5)) // 15
```

このプログラムがぱっと理解できれば再帰は完璧！

---

# 厳格評価と遅延評価

## 厳格評価

？？？

---

## 遅延評価

### 関数を引数に取り、必要となった時点で実行する

```scala
// not 遅延評価
def sum(l: Int, r: Int): Int = l + r

// 遅延評価
def sum(l: => Int, r: => Int): Int = l + r

// 遅延評価（l と r の余分な演算が省略できる）
def sum(l: => Int, r: => Int, t: Boolean): Int = if(t) l + r else 0
```

---

### val で宣言するときに lazy をつけると結果を遅延評価で結果をキャッシュしてくれる

```scala
def sum = { println("run"); 1 + 1 }
sum + sum // run と２回出る

lazy val sum = { println("run"); 1 + 1 }
sum + sum // run が１回しかでない

def sum(l: => Int): Int = l + l
lazy val add: Int = 10 + 20 // コストが高い処理だとする
sum(add) // add は１回しか実行されない
def add: Int = 10 + 20 // コストが高い処理だとする
sum(add) // add が２回実行される
```

<!-- https://qiita.com/BooookStore/items/2627f33764dbabbcf55f -->

---

# 型推論

### 静的型付き言語の言語機能で、明示的に型を記述しなくてもコンパイラが自動的に型を決定してくれる

```scala
val x = 100
x.getClass // Class[Int] = int

val y = "string"
y.getClass // Class[_ <: String] = class java.lang.String
```

<!-- https://qiita.com/t-yng/items/1fe622911bdd379d37ea -->

---

# 参照透過性

### 参照のことを意識せずに扱える

```scala
val x = 1

...

val add = (i: Int) => (j: Int) => i + j

> add(x)(2) // 3
> add(x)(2) // 3
```

関数が同じ引数で２回呼ばれたら同じ値を返す。このような性質を参照透明性という。  
純粋関数であれば、変数の普遍性が担保できるため、参照透明性が保証できる。

---

# データ構造体

何を指しているのかわからなかった。。。

### 配列

```scala
val list = List("first", "second", "third")
list(0) // "first"
```

### タプル

```scala
def calc(list: List[Int]): (String, Int) = ("合計", list.sum)

val ret = calc(List(1,2,3,4,5)) // (String, Int) = (合計,15)
ret._1 // String = 合計
ret._2 // Int = 15
```

---

### 列挙体

scala に列挙体はないので擬似列挙体

```scala
// 同一ファイル内からのみ継承可能なクラス
sealed trait Language

// シングルトンクラス
object Language {
  case object Scala extends Language
  case object Ruby extends Language
  case object PHP extends Language
}

val lang: Language = Language.Scala

lang match {
  case Language.Scala => println("Scala !!")
  case Language.Ruby => println("Ruby !!")
}
// PHP がないよ！って Warning もでる
```

---

# ファンクタ、モナド、アプリカティブ

## 情報工学の博士号を取る！

---

# ファンクタ、モナド、アプリカティブ

## ファンクタ

```haskell
fmap (+2) -- 箱の中の値に関数が適用できる
```

## アプリカティブ

```haskell
(*) <*> (+2) -- 箱の中の関数を箱の中の値に適用できる
```

## モナド

```haskell
getLine >>= readFile >>= putStrLn -- 箱に入った値を返す関数を箱の中の値に適用できる
```

---

# ファンクタ、モナド、アプリカティブ

不純関数から不純物を取り除き、純粋関数とし扱える仕組みらしいです。
データベースやファイル IO みたいな副作用を追い出すために Haskell では使用されています。

![笑う画像](https://camo.qiitausercontent.com/f9c69f7c159540b16e9415442cf71341e9755eed/68747470733a2f2f71696974612d696d6167652d73746f72652e73332e616d617a6f6e6177732e636f6d2f302f3838392f31383864346265332d333666302d663939302d643837632d6566376234303435353663652e676966)

これ以上の難しい理解ができませんでした。。。。すみません(^_^;)

---

# その他 Scala で気になるやつ

覚えておいた方がよさそうなものをピックアップしてみました。

- クラスの種類
- Trait
- Future

---

# クラスの種類

## クラス

```scala
class Programmer(val language: String) {
  println("language = " + language)

  def coding() = println(language + "を使ってコーディングします")
}

val pg = new Programmer("scala")
pg.coding // scalaを使ってコーディングします
```

---

# クラスの種類

## オブジェクト（シングルトンクラス）
補助コンストラクタは使用できない
```scala
object Programmer {
  def coding(language: String) = println(language + "を使ってコーディングします")
}

Programmer.coding("scala") // scalaを使ってコーディングします
```

---

# クラスの種類

## コンパニオンオブジェクト
ファクトリメソッドとかで使える
```scala
class SampleCompanion private (num: Int)

object SampleCompanion {
  def apply(num: Int) = new SampleCompanion(num)
}

println(SampleCompanion(10))
```

---

# クラスの種類

## ケースクラス
```scala
case class Apple(name: String)

val fuji = Apple("fuji")
val jona = Apple("jona")

fuji match {
  case Apple("fuji") => println("Apple name is fuji !!")
  case Apple(name) => println("Apple name is " + name)
  case _ => println("other")
} // Apple name is fuji !!
```

---

# Trait

```scala
trait Programmer {
  def coding = println("コーディングします")
}

// 継承して使用する
class Person extends Programmer
val person = new Person
person.coding // コーディングします
```

---

# Trait

```scala
trait Programmer {
  def coding = println("コーディングします")
}

// 継承クラスがある場合はクラス定義時にミックスイン
abstract class Person
class Inoue extends Person with Programmer
val inoue = new Inoue
inoue.coding // コーディングします

// インスタンス作成時ににミックスイン
class Hiroyuki
val hiroyuki = new Hiroyuki with Programmer
hiroyuki.coding // コーディングします
```

---

# Future

非同期処理

```scala
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

val f: Future[String] = Future {
  Thread.sleep(10000)

  "finish!!"
}

f.isCompleted // false

// f が終わるのを待つ
val result: String = Await.result(f, Duration.Inf)
println(result) // finish!!
```
