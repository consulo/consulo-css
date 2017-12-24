package consulo.css.lang.lexer;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import consulo.css.lang.CssTokens;

%%

%public
%class _CssLexer
%extends LexerBase
%unicode
%function advanceImpl
%type IElementType
%eof{ return;
%eof}

%state BODY

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace = [ \t]
AnySpace = {LineTerminator} | {WhiteSpace} | [\f]

EscapedCharacter = \\{InputCharacter}

TraditionalComment = ("/*"[^]{CommentContent})|"/*"
CommentContent = ([^"*"]*("*"+[^"*""/"])?)*("*"+"/")?

IdentifierPart = [:jletter:]? [:jletterdigit:]?
Identifier=("-"? {IdentifierPart}*)*

StringLiteral = \" ( \\\" | [^\"\n\r] )* \"
StringLiteral2 = \' ( \\\' | [^\'\n\r] )* \'
NumberLiteral = [0-9]+ | [0-9]*\.[0-9]+
NumberLiteralWithSufixes = {NumberLiteral} ("in" | "cm" | "mm" | "pt" | "pc" | "px" | "em" | "ex" | "%")?
HexNumberLiteral = "#" ([_0-9A-Fa-f])+

UriTextPart={AnySpace} | [:jletter:] | [:jletterdigit:]
%%

<YYINITIAL> {
    "#"{Identifier}         { return CssTokens.SELECTOR_ID; }
    "."{Identifier}         { return CssTokens.SELECTOR_CLASS; }
    "{"                     { yybegin(BODY); return CssTokens.LBRACE; }
    "}"                     { return CssTokens.RBRACE; }
    "["                     { return CssTokens.LBRACKET; }
    "]"                     { return CssTokens.RBRACKET; }
    ":"                     { return CssTokens.COLON; }
    "::"                    { return CssTokens.COLONCOLON; }
    "="                     { return CssTokens.EQ; }
    ">"                     { return CssTokens.GT; }
    ","                     { return CssTokens.COMMA; }
    "*"                     { return CssTokens.ASTERISK; }
    "."                     { return CssTokens.DOT; }
    "+"                     { return CssTokens.PLUS; }
    "~"                     { return CssTokens.TILDE; }
    {Identifier}            { return CssTokens.IDENTIFIER; }
    {StringLiteral}         { return CssTokens.STRING; }
    {StringLiteral2}         { return CssTokens.STRING; }
    {TraditionalComment}    { return CssTokens.BLOCK_COMMENT; }
    {AnySpace}+             { return CssTokens.WHITE_SPACE; }
    .                       { return CssTokens.BAD_CHARACTER; }
}

<BODY> {
    "#"                     { return CssTokens.SHARP; }
    "."                     { return CssTokens.DOT; }
    "{"                     { return CssTokens.LBRACE; }
    "}"                     { yybegin(YYINITIAL); return CssTokens.RBRACE; }
    "["                     { return CssTokens.LBRACKET; }
    "]"                     { return CssTokens.RBRACKET; }
    "("                     { return CssTokens.LPAR; }
    ")"                     { return CssTokens.RPAR; }
    ":"                     { return CssTokens.COLON; }
    "::"                    { return CssTokens.COLONCOLON; }
    "="                     { return CssTokens.EQ; }
    ";"                     { return CssTokens.SEMICOLON; }
    ","                     { return CssTokens.COMMA; }
    "*"                     { return CssTokens.ASTERISK; }
    "."                     { return CssTokens.DOT; }
    "+"                     { return CssTokens.PLUS; }
    "%"                     { return CssTokens.PERC; }
    "!"({TraditionalComment}+|{WhiteSpace}+)?"important"   { return CssTokens.IMPORTANT;  }
    {NumberLiteralWithSufixes} { return CssTokens.NUMBER; }
    {HexNumberLiteral}      { return CssTokens.NUMBER; }
    {Identifier}            { return CssTokens.IDENTIFIER; }
	{StringLiteral}         { return CssTokens.STRING; }
	{StringLiteral2}        { return CssTokens.STRING; }
    {TraditionalComment}    { return CssTokens.BLOCK_COMMENT; }
    {AnySpace}+             { return CssTokens.WHITE_SPACE; }
    .                       { return CssTokens.BAD_CHARACTER; }
}