
---
title: "Modularity in Scala"
draft: true
publishdate: 2019-12-30T18:46:05+01:00
---

# What's in a paradigm?

**Scala** is a great multi-paradigm programming language, but what's in a *paradigm*?

> **Paradigm**: *a world view underlying the theories and methodology of a particular scientific subject.*


If a paradigm is a **world view**, it should encompass everything about a specific programming language. Programming languages that hail from academia tend to be very true to the underlying model of computation, casting all language constructs in the light of that model.

{{<giphy Ec3BFLEq2M5gI>}}

<!--more-->

# Haskell


**Haskell** is a good example of a language where both syntax and language constructs are clearly based on the *lambda calculus*. *Function application* is denoted simply by arranging the function and arguments next to each other:


```haskell
f x
```
where `f` is a function, and `x` is an argument to which `f` is applied. 
In the lambda calculus, function application uses the exact same notation:


> $ f x $

In Haskell, function literals are defined by backslashes:

```haskell
(\x -> (+) x x) 21
-- 42
```

the reasoning behind the backslash is that it *"kinda looks like half-a-lambda symbol"*. (The $\lambda$ symbol is also nowhere to be found on most keyboards). Consider the corresponding notation in lambda calculus:

> $ \lambda x . + x x $

> **NOTE**: `+` is not a part of the lambda calculus, we just use it as a placeholder for a function that implements addition. **The same goes for integer literals.**

## Currying 

This leads us to another feature of Haskell, namely **currying** by default. In the lambda calculus, there are only functions of 0 arguments (constants) and functions of 1 argument. How does addition work, then? It clearly needs 2 operands. The `(+)` function takes a single number and yields a function which "stores" this first argument. The new function can only add to this number, i.e., the first argument is considered a constant at this point. 

> $$ (+) = \lambda x . \lambda y . \mathtt{builtin (x,y)} $$

> \begin{align} 
> &(+) \\;  39 \\;  3 
> \\\  =\\; &(\lambda x . \lambda y . \mathtt{builtin (x,y)} ) \\; 39 \\; 3 \\\
> \\\  =\\; &(\lambda y . \mathtt{builtin (39,y)} )  \\; 3 
> \\\  =\\; & \mathtt{builtin (39,3)} 
> \end{align}

`builtin` can then be calculated using the instructions and integers of the CPU on the current platform. It *is* possible to encode integers and addition in the pure lambda calculus, but it gets hairy very quickly, and it's a good place to *draw the line between theory and pragmatism*.

## Functions

A function definition in **Haskell** is a bit of syntax sugar for the lambda calculus:

```haskell
addmul x y = (+) x ((*) x y)
```

we can desugar that as:

>  $$ addmul = \lambda x . \lambda y .  + x  \\; (* \\; x \\; y) $$

Since the lambdacalculus doesn't include the (=) equality term, we can bind our function as an argument to the rest of the program like so:

> $$ (\lambda addmul . \\; \mathbb{P})  (\lambda x . \\; \lambda y .  + \\; x \\; (* \\; x \\; y)) $$

where $\mathbb{P}$ is "the rest of our program", now with the definition of `addmul` in scope.

The point here is that, with relatively few (and easy) transformations, we have desugared the haskell code into a program in the lambda calculus. You could say that the paradigm of `functional programming` is based on lambda calculus. 

# Logic Programming

**Prolog** is another example of a language where the syntax is heavily influenced by the underlying paradigm: **logic programming**. It consists of rules ("entailments"), and facts.

Rules use the *entailment* **( $\vdash$ )** symbol written in ASCII as: 

```prolog
disney :- dog(goofy), mouse(mickey). 
```

meaning that `disney` entails that `goofy` is a dog **and** `mickey` is a mouse. If you squint, `:-` kinda looks like the turnstile **( $\vdash$ )** symbol. Note also that commas denote **conjunction** like they often do in mathematical notation.

Goofy and Mickey can be replaced by variables as such:

```prolog
disney(X, Y) :- dog(X), mouse(Y).
disney(goofy, mickey). 
```

These programs are equivalent, i.e., they yield the same facts. If you squint again, the rule now kinda looks like a function with the left hand side being the function header and the righthand side being the body. *This is not really a coincidence*.


## Curry-Howard-Lambek correspondence

It turns out that logic, lambda calculus, and turing machines are equivalent in terms of *what they can do*. This is interesting in many ways. With this in mind, you could think of the different paradigms as simply *your choice of programming style*. 

{{< giphy SuBYa2XO3aVH8Qt8IK >}}

In the programming language [**Idris**](https://www.idris-lang.org/), the same program can be interpreted both as a term in the lambda calculus and as a logical proposition. Since **type systems are based on logic**, this means that the same language can be used to talk about types and runtime behaviour. This allows for very advanced types to be defined, giving detailed guarantees about a programs *runtime behaviour* based on the programs type. **Idris** places a lot of emphasis on using **total functions**, since allowing anything else would lead to **unsound** behaviour when viewing terms as logic propositions.

There is much beauty to be found in the Curry-Howard-Lambek correspondence and its many implications, but the software industry doesn't care about beauty in general. It is notoriously difficult to quantify productivity gains from even the most basic type system, and this leads to an ongoing discussion on whether or not dynamic or static typing is "*better*". **Google** [released an article](https://days2011.scala-lang.org/sites/days2011/files/ws3-1-Hundt.pdf) comparing the productivity and performance of `C++`, `Java`, `Go`, and `Scala`, however, making conclusions based on different people's solutions with different programming languages always seems to border on the philosophical. This is mainly true because it's not possible to remove the human factor -- once I have experience with a specific problem, solving it again in another language would give that language an unfair advantage. In other words: 

> there is no way to separate the **programming discipline** from the **problem solving discipline**.



# A few words about pragmatism

There are many languages that are less interested in having a close connection to a mathematical foundation, but instead adhere to the paradigm of the machines on which it will be run. Assembly languages are extremely pragmatic programming languages that are created with the purpose of controlling the CPU with a list of instructions. The vocabulary here is closely related to the CPU instructions and architecture on which it will be used. While this closely resembles `imperative programming`, there is little to no effort made to resemble the `Turing Machine`. While processors are indeed equivalent to the Turing Machine in terms of what it can calculate, there are a plethora of concepts invented to *speed up the processor* that bleed into the syntax and semantics of assembly languages.

This approach is much more pragmatic, and it has historically enabled extremely efficient use of the computing hardware. However, programs written in this style are inherently of a low level of abstraction. This means that programs can be difficult to understand, since the intention of the author is completely lost between a list of register-manipulating instructions.
Because of this, a number of *"high level languages"* were invented, allowing the use of more human-readable language, named variables, methods, and later - even recursion.

As programs grew in scope and complexity, maintaining and developing software became increasingly difficult. A need for `modularisation` slowly appeared. How can we make sure that changing our program doesn't incur *unwanted side effects*? How do make clear borders between *"sections of our programs"*, such that the interaction with software doesn't entail a complete understanding of the inner workings of that software?

One answer to this problem was "objects". As I am writing this, I realize that giving a complete history of object oriented programming is out of scope, so I will cut it down to simply: 

> Object oriented programming is one of the most successful approaches to modularity in the software industry to this date.

## Are modules part of the paradigm?

I would separate the *paradigm of computation* from the *paradigm of modularization*. The distinction of different "categories" of paradigms is often left to interpretation. I would suggest a few different categories like:

* Computational (what's the underlying model for computation, e.g. functional, logic, imperative,...)
* Organizational (how are modules organized, e.g. files, packages, classes, objects, structs, ...)
* Methodology (abstract vs. non abstract, e.g. declarative vs imperative)

You could add an arbitrary amount of categories to this list, but I feel like at least these 3 are useful. In the next few blog posts, I will concentrate on how the different *organizational* constructs in *Scala* solve programming challenges.


## SOLID principle

> Note: I am not a fan of Robert C. Martin, and he is one of the few people I have blocked on Twitter. However, I realize that his work has inspired many and that SOLID is accepted as gospel to many, hence this section.

The main goals of object oriented programming are:

1. Single responsibility
1. Open-closed
1. Liskov substitution
1. Interface segregation
1. Dependency inversion

I want to mainly discuss the 4th point. **Interface segregation** is closely related to **single responsibility** in trying to increase the focus of modules in your software. The less information and surface your interfaces have, the less likely it is that changing a module results in needing to update unrelated code. As a software engineer, I find that writing modules to a pragmatic size while still having small focused modules is difficult.

In a mixed paradigm language like Scala, we have many different ways to express modules:

1. packages
2. mixin-traits
3. classes
4. case classes
5. sealed trait ADTs
6. objects (singletons)
7. function literals
8. typeclasses

How on earth do we choose the right language feature for the right problem? What even constitutes a "right" choice? 

# Testing

{{<giphy tK5JkmMAPveNO>}}

Most often, I find that writing tests for my code is a great way to quantify the modularity of the code. How easy is it to isolate the functions that I want to test? How much boiler plate do I need to set up my test cases? Can I run my tests in parallel? Do I need "**hacks**" to inspect the internals of the modules that I'm testing?

## Testing functional programs

Supposedly, functional programming **is the best thing ever** because it makes testing embarrassingly easy -- this is not true in the general case! It's easy to write a purely functional program with a completely tangled up structure, where testing a single function effectively means testing the entire program. Heck, nothing stops you from writing your entire program **as a single function**.

Some problems also lend themselves beautifully to a functional programming solution, while others require ingenuity to be testable in a functional setting.

### Testing inherently immutable code

Say we need to test a function that sums up a list of integers. We could implement it like so:

```scala
def sum(xs: List[Int]): Int = xs match {
  case head :: tail => head + sum(tail)
  case Nil          => 0
}
```

testing it would amount to something like:

```scala
assert(sum(List(1,2,3)) == 6)
```

This is nice. This is simple. But why is it simple? If we see `sum` as a module, it deals only with the argument of interest (`xs`), and it produces only 1 result, which is the interesting result for summing up a list. There is no **overfetching** of arguments, and there is no **overproducing** of results or side effects. But the real life happens, and you need to do much much more than adding numbers:

```scala
case class S3Conf(user: String, secret: String)

case class AppConf(baseUrl: String, databaseUrl: String, port: Int, s3Conf: S3Conf)

class BusinessDatabase(appConf: AppConf, db: DatabaseApi, logger: Logger) {

  def insertBusinessObject(obj:Object) = db.insert(obj)

  def removeObject(id:ObjectId) = db.remove(id)

  def listObjects() = {
    logger.debug(s"Listing all objects for database url: ${appConf.databaseUrl}")
    db.listAll()
  }

  ....
}
```

Now, our main module here is `BusinessDatabase`. It has some dependencies on `AppConf`, `DatabaseApi`, and `Logger`. We see that it implements 3 functions, each doing *something* with the database API. In that sense, it makes much sense that they exist within the same class. `listObjects`, however, is the only method that makes use of `appConf` and `logger`. Testing `insertBusinessObject` now entails the construction of both an `AppConf` and a `Logger`, even though we don't really need them. How do we solve this? We could extract the "culprit", `listObjects`, such that we could get rid of the 2 arguments for this class, but there's a good chance that we would add a new debug-log in another function down the line. Naming would also be an issue, as there's no real (=business) reason for this extraction other than testing/modularity. So we just leave this imperfection in our code 😯?

{{< giphy 9J92ARAauOQfdDoKlC >}}

It doesn't really leave us with much in terms of professional satisfaction.

# The blog

In the next few posts, I want to investigate how modularity is solved with different approaches like `classes and interfaces`, `typeclasses`, `final tagless`, and `free monads`. 

Does one approach lead to smaller modules? What's the weight in terms of boiler plate? What kind of background and understanding is needed for each approach? How do our tests change based on the different approaches? I don't have all the answers yet, but I'm looking forward to aligning my thoughts on modularity and functional programming. 

See you in the next post 👋🏻

{{<giphy l1J3CbFgn5o7DGRuE>}}