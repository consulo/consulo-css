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

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace = [ \t]
AnySpace = {LineTerminator} | {WhiteSpace} | [\f]

EscapedCharacter = \\{InputCharacter}

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
CommentContent = ( [^*] | \*+ [^/*] )*

IdentifierPart = [:jletter:] [:jletterdigit:]*
Identifier="-"? {IdentifierPart}* "-"? {IdentifierPart}*

StringLiteral = \" ( \\\" | [^\"\n\r] )* \"
NumberLiteral = [0-9]+ | [0-9]*\.[0-9]+

%%

<YYINITIAL> {
    "#"                     { return CssTokens.SHARP; }
    "."                     { return CssTokens.DOT; }
    "{"                     { return CssTokens.LBRACE; }
    "}"                     { return CssTokens.RBRACE; }
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
    {NumberLiteral}         { return CssTokens.NUMBER; }
    {StringLiteral}         { return CssTokens.STRING; }
    {Identifier}            { return CssTokens.IDENTIFIER; }
    {TraditionalComment}    { return CssTokens.BLOCK_COMMENT; }
    {AnySpace}+             { return CssTokens.WHITE_SPACE; }
    .                       { return CssTokens.BAD_CHARACTER; }
}