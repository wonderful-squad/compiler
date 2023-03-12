## `Lexer`词法解析

`Lexer.java 类`

输入:

```
1+2*3+4;
end
```

输出

```
Token: NUM_OR_ID ,Symbol: 1
Token: PLUS ,Symbol: +
Token: NUM_OR_ID ,Symbol: 2
Token: TIMES ,Symbol: *
Token: NUM_OR_ID ,Symbol: 3
Token: PLUS ,Symbol: +
Token: NUM_OR_ID ,Symbol: 4
Token: SEMI ,Symbol: ;
```

## 1. 首先判断 match

- 判断 lookAhead 是否到达了 结尾 EOI
- lookAhead 默认值为 -1，则只在首次执行首次执行 lex 函数
- 返回 lex 函数的结果赋值给 lookAhead

```java
  public boolean match(int token) {
    if (lookAhead == -1) {
      lookAhead = lex();  // 1.1 这里仅会在第一次 lookAhead 默认值为 -1 执行
    }
    return token == lookAhead;  // 1.2 返回是否 lookAhead走到了结尾
  }

  public void advance() {
    lookAhead = lex();
  }

  public void runLexer() {
    // 1 这里会去执行match
    // | 3之后下一次再走到match 就只会判断 当前 lookAhead是否走到了结尾
    while (!match(EOI)) {
      System.out.println("Token: " + token() + " ,Symbol: " + yytext);  // 2 打印输出语句
      advance();  // 3 这里会去继续执行下一个字符串，打标签
    }
  }
```

## `BasicParser` 语法解析

当前用 java 实现的简易编译器目的是 `将一条或一组含有加号 乘号 的算数表达式编译成类似汇编语言的伪代码`，因此必须给算数表达式设立一组语法规则， 程序才能对输入的表达式进行分析，把一组带有分号的算数表达式称为 `statements`, 例如:

```
1+2*3+4；
2+3*4+5；
3+4*5+6；
```

将一组表达式中的某一条带有分号的表达式称为 `expression` 因此 `statements`的语法规则可以写成：

```
statements -> expression; | expression; statements
```

1. 在 `->` 右边有两组解析规则，他们用符号 `|` 分隔开
2. `->` 左边的被解析的对象居然在右边的解析规则中出现，形成了一种循环，也就是用自己来解释自己，这种被称为 `左循环`

- 右边有两组解析规则，用右边替换左边时，到底选择哪一组？
- 左边的符号(statements)出现在右边的规则中，替换的话就会出现死循环

```
statements(buffer) {
  expression(buffer);
  statements(buffer);
}
```

## 初级的语法规则

```
Expression -> expression + term | term;  // 有加号就左边，没有加号就右边，例如 1 + 2; 或者 2;
term -> term * factor | factor  // 里面有乘号 2 * 3 就是左边，如果没有 乘号就是右边
factor -> NUMBER | (expression)  // factor可以是 一个数字 或者一个带括号的 表达式
```

## 升级后的语法规则

1. `statements -> '空' | expression; statements`
2. `expression -> term expression'`
3. `expression' -> +term expression' | '空'`
4. `term -> factor term'`
5. `term' -> * factor term' | '空'`
6. `factor -> number | (expression)`
