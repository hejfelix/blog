---
title: "Modularity in Scala"
draft: true
publishdate: 2019-12-30T18:46:05+01:00
---

**Scala** is a great multi-paradigm programming language, but what's in a *paradigm*?


> **Paradigm**: *a world view underlying the theories and methodology of a particular scientific subject.*

If a paradigm is a **world view**, it should encompass everything about a specific programming language. Programming languages that hail from academia tend to be very true to the underlying model of computation, casting all language constructs in the light of that model.

**Haskell** is a good example of a language where both syntax and language constructs are clearly based on the *lambda calculus*. *Function application* is denoted simply by arranging the function and arguments next to each other:

```haskell
f x
```
where `f` is a function, and `x` is an argument to which `f` is applied. 
In the lambda calculus, function application uses the exact same notation:

> \\[ f x \\]

In Haskell, function literals are defined by backslashes:

```haskell
(\x -> (+) x x) 21
-- 42
```

the reasoning behind the backslash is that it *"kinda looks like half-a-lambda symbol"*. (The $\lambda$ symbol is also nowhere to be found on most keyboards). Consider the corresponding notation in lambda calculus:

> \\[ \lambda x . + x x \\]

> **NOTE**: `+` is not a part of the lambda calculus, we just use it as a placeholder for a function that implements addition. **The same goes for integer literals.**

## Currying 

This leads us to another feature of Haskell, namely **currying** by default. In the lambda calculus, there are only functions of 0 arguments (constants) and functions of 1 argument. How does addition work, then? It clearly needs 2 operands. The `(+)` function takes a single number and yields a function which "stores" this first argument. The new function can only add to this number, i.e., the first argument is considered a constant at this point. 

> \\[ (+) = \lambda x . \lambda y . \mathtt{builtin (x,y)} \\]

> \begin{align} 
> &(+) \\;  39 \\;  3 
> \\\  =\\; &(\lambda x . \lambda y . \mathtt{builtin (x,y)} ) \\; 39 \\; 3 \\\
> \\\  =\\; &(\lambda y . \mathtt{builtin (39,y)} )  \\; 3 
> \\\  =\\; & \mathtt{builtin (39,3)} 
> \end{align}

`builtin` can then be calculated using the instructions and integers of the CPU on the current platform. It *is* possible to encode integers and addition in the pure lambda calculus, but it gets hairy very quickly, and it's a good place to *draw the line between theory and pragmatism*.

## Function Literals

A function definition in **Haskell** is a bit of syntax sugar for the lambda calculus:

```haskell
addmul x y = (+) x ((*) x y)
```

we can desugar that as:

>  \\[ addmul = \lambda x . \lambda y .  + x  \\; (* \\; x \\; y) \\]

Since the lambdacalculus doesn't include the (=) equality term, we can bind our function as an argument to the rest of the program like so:

> \\[ (\lambda addmul . \\; \mathbb{P})  (\lambda x . \\; \lambda y .  + \\; x \\; (* \\; x \\; y)) \\]

where $\mathbb{P}$ is "the rest of our program", now with the definition of `addmul` in scope.

The point here is that, with relatively few (and easy) transformations, we have desugared the haskell code into a program in the lambda calculus. You could say that the paradigm of `functional programming` is based on lambda calculus. 

# Logic Programming

**Prolog** is another example of a language where the syntax is heavily influenced by the underlying paradigm: **logic programming**. It consists of rules ("entailments"), and facts.

Rules use the *entailment* **( $\vdash$ )** symbol written in ASCII as: 

```prolog
disney :- dog(goofy), mouse(mickey). 
```

meaning that `disney` entails that `goofy` is a dog and `mickey` is a mouse. If you squint, `:-` kinda looks like the turnstile **( $\vdash$ )** symbol.

Goofy and Mickey can be replaced by variables as such:

```prolog
disney(X, Y) :- dog(X), mouse(Y).
disney(goofy, mickey). 
```

These programs are equivalent, i.e., they yield the same facts. If you squint again, the rule now kinda looks like a function with the left hand side being the function header and the righthand side being the body. *This is not really a coincidence*.


## Curry-Howard-Lambek correspondence

It turns out that logic, lambda calculus, and turing machines are equivalent in terms of *what they can do*. This is interesting in many ways. With this in mind, you could think of the different paradigms as simply *your choice of programming style*. Furthermore, in a programming language like [**Idris**](https://www.idris-lang.org/), the same program can be interpreted both as a term in the lambda calculus and as a logical proposition. Since **type systems are based on logic**, this means that the same language can be used to talk about types and runtime behaviour. This allows for very advanced types to be defined, giving detailed guarantees about a programs *runtime behaviour* based on the programs type. **Idris** places a lot of emphasis on using **total functions**, since allowing anything else would lead to **unsound** behaviour when viewing terms as logic propositions.

There is much beauty to be found in the Curry-Howard-Lambek correspondence and its many implications, but the software industry doesn't care about beauty in general. It is notoriously difficult to quantify productivity gains from even the most basic type system, and this leads to an ongoing discussion on whether or not dynamic or static typing is "*better*". **Google** [released an article](https://days2011.scala-lang.org/sites/days2011/files/ws3-1-Hundt.pdf) comparing the productivity and performance of `C++`, `Java`, `Go`, and `Scala`, however, making conclusions based on different people's solutions with different programming languages always seems to border on the philosophical. This is mainly true because it's not possible to remove the human factor -- once I have experience with a specific problem, solving it again in another language would give that language an unfair advantage. In other words: 

> there is no way to separate the **programming discipline** from the **problem solving discipline**.



# A few words about pragmatism

There are many languages that are less interested in having a close connection to a mathematical foundation, but instead adhere to the paradigm of the machines on which it will be run. Assembly languages are extremely pragmatic programming languages that are created with the purpose of controlling the CPU with a list of instructions. The vocabulary here is closely related to the CPU instructions and architecture on which it will be used. While this closely resembles `imperative programming`, there is little to no effort made to resemble the `Turing Machine`. While processors are indeed equivalent to the Turing Machine in terms of what it can calculate, there are a plethora of concepts invented to *speed up the processor* that bleed into the syntax and semantics of assembly languages.

This approach is much more pragmatic, and it has historically enabled extremely efficient use of the computing hardware. However, programs written in this style are inherently of a low level of abstraction. This means that programs can be difficult to understand, since the intention of the author is completely lost between a list of register-manipulating instructions.
Because of this, a number of *"high level languages"* were invented, allowing the use of more human-readable language, named variables, methods, and later - even recursion.

As programs grew in scope and complexity, maintaining and developing software became increasingly difficult. A need for `modularisation` slowly appeared. How can we make sure that changing our program doesn't incur *unwanted side effects*? How do make clear borders between *"sections of our programs"*, such that the interaction with software doesn't entail a complete understanding of the inner workings of that software?

One answer to this problem was "objects".