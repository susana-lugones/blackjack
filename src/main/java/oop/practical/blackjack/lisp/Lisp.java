package oop.practical.blackjack.lisp;

public final class Lisp {

    public static Ast parse(String input) throws ParseException {
        return new Parser(new Lexer(input).lex()).parse();
    }

}
