## `Lexer`词法解析函数

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
