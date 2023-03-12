import java.util.Scanner;

/**
 * ClassName: Lexer
 * Function: 词法解析器
 */
public class Lexer {
  public static final int EOI = 0; // end of file，表示文件的末尾
  public static final int SEMI = 1; // ;
  public static final int PLUS = 2; // +
  public static final int TIMES = 3; // *
  public static final int LP = 4;
  public static final int RP = 5;
  public static final int NUM_OR_ID = 6; // 如 1 2 3 4
  public static final int UNKNOWN_SYMBOL = 7;

  private int lookAhead = -1; // 表示当前指向的是哪个标签代表的数字

  public String yytext = ""; // 当前解析的子字符串，本例子当中每次截取一个
  public int yyleng = 0;
  public int yylineno = 0; // 算数表达式在第几行

  private String input_buffer = "";
  private String current = ""; // 当前正在处理的字符串 "2*3+4;"

  // 判断 lookAhead 是否到达了 结尾 EOI
  // 首次执行 lex函数，返回lex函数的结果赋值给 lookAhead
  public boolean match(int token) {
    if (lookAhead == -1) {
      lookAhead = lex();
    }
    return token == lookAhead;
  }

  public void advance() {
    lookAhead = lex();
  }

  public void runLexer() {
    while (!match(EOI)) {
      System.out.println("Token: " + token() + " ,Symbol: " + yytext);
      advance();
    }
  }

  /*
   * 判断是否是数字
   */
  private boolean isAlnum(char c) {
    if (Character.isAlphabetic(c) == true ||
        Character.isDigit(c) == true) {
      return true;
    }

    return false;
  }

  private int lex() {

    while (true) {

      // 第一次输入时会走到这里
      while (current == "") {
        Scanner s = new Scanner(System.in);
        while (true) {
          String line = s.nextLine();
          if (line.equals("end")) {
            break;
          }
          input_buffer += line;
        }
        s.close();

        if (input_buffer.length() == 0) {
          current = "";
          return EOI;
        }

        current = input_buffer;
        ++yylineno;
        current.trim();
      } // while (current == "")

      if (current.isEmpty()) {
        return EOI;
      }

      // 从循环中每次读入一个字符
      for (int i = 0; i < current.length(); i++) {

        yyleng = 0; // 每次进来都重置
        yytext = current.substring(0, 1);
        switch (current.charAt(i)) {
          case ';':
            current = current.substring(1);
            return SEMI;
          case '+':
            current = current.substring(1); // 每次都会截取 current
            return PLUS;
          case '*':
            current = current.substring(1);
            return TIMES;
          case '(':
            current = current.substring(1);
            return LP;
          case ')':
            current = current.substring(1);
            return RP;

          case '\n':
          case '\t':
          case ' ':
            current = current.substring(1);
            break;

          default:
            if (isAlnum(current.charAt(i)) == false) {
              return UNKNOWN_SYMBOL;
            } else {

              while (i < current.length() && isAlnum(current.charAt(i))) {
                i++;
                yyleng++;
              } // while (isAlnum(current.charAt(i)))

              yytext = current.substring(0, yyleng);
              current = current.substring(yyleng);
              return NUM_OR_ID;
            }
        } // switch (current.charAt(i))
      } // for (int i = 0; i < current.length(); i++)

    } // while (true)
  }// lex()

  private String token() {
    String token = "";
    switch (lookAhead) {
      case EOI:
        token = "EOI";
        break;
      case PLUS:
        token = "PLUS";
        break;
      case TIMES:
        token = "TIMES";
        break;
      case NUM_OR_ID:
        token = "NUM_OR_ID";
        break;
      case SEMI:
        token = "SEMI";
        break;
      case LP:
        token = "LP";
        break;
      case RP:
        token = "RP";
        break;
    }

    return token;
  }
}