package org.consulo.css.lang.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.consulo.css.lang.CssTokens;

%%

%class _CssLexer
%implements FlexLexer
%final
%unicode
%function advance
%type IElementType
%eof{ return;
%eof}

%state BODY
%state URI
%state URI_BODY

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace = [ \t]
AnySpace = {LineTerminator} | {WhiteSpace} | [\f]

EscapedCharacter = \\{InputCharacter}

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
CommentContent = ( [^*] | \*+ [^/*] )*

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
    "#"                     { return CssTokens.SHARP; }
    "."                     { return CssTokens.DOT; }
    "{"                     { yybegin(BODY); return CssTokens.LBRACE; }
    "}"                     { return CssTokens.RBRACE; }
    "["                     { return CssTokens.LBRACKET; }
    "]"                     { return CssTokens.RBRACKET; }
    ":"                     { return CssTokens.COLON; }
    "::"                    { return CssTokens.COLONCOLON; }
    "="                     { return CssTokens.EQ; }
    ">"                     { return CssTokens.GE; }
    //";"                     { return CssTokens.SEMICOLON; }
    ","                     { return CssTokens.COMMA; }
    "*"                     { return CssTokens.ASTERISK; }
    "."                     { return CssTokens.DOT; }
    "+"                     { return CssTokens.PLUS; }
    //"%"                     { return CssTokens.PERC; }
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
    ":"                     { return CssTokens.COLON; }
    "::"                    { return CssTokens.COLONCOLON; }
    "="                     { return CssTokens.EQ; }
    ";"                     { return CssTokens.SEMICOLON; }
    ","                     { return CssTokens.COMMA; }
    "*"                     { return CssTokens.ASTERISK; }
    "."                     { return CssTokens.DOT; }
    "+"                     { return CssTokens.PLUS; }
    "%"                     { return CssTokens.PERC; }
    "url"                   { /*yybegin(URI); */return CssTokens.URI; }
    {NumberLiteralWithSufixes} { return CssTokens.NUMBER; }
    {HexNumberLiteral}      { return CssTokens.NUMBER; }
    {Identifier}            { return CssTokens.IDENTIFIER; }
	{StringLiteral}         { return CssTokens.STRING; }
	{StringLiteral2}        { return CssTokens.STRING; }
    {TraditionalComment}    { return CssTokens.BLOCK_COMMENT; }
    {AnySpace}+             { return CssTokens.WHITE_SPACE; }
    .                       { return CssTokens.BAD_CHARACTER; }
}

<URI> {
    "("                     { yybegin(URI_BODY); return CssTokens.LPAR; }
  //  {AnySpace}+             { return CssTokens.WHITE_SPACE; }
    .                       { yybegin(BODY); return CssTokens.BAD_CHARACTER; }
}

<URI_BODY> {
    ")"                     { yybegin(BODY); return CssTokens.RPAR; }
    .                       { return CssTokens.URI_TEXT; }
}