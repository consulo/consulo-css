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

Identifier = [:jletter:] [:jletterdigit:]*

StringLiteral = \" ( \\\" | [^\"\n\r] )* \"

%%

<YYINITIAL> {
    "#"                     { return CssTokens.SHARP; }
    "."                     { return CssTokens.DOT; }
    "{"                     { return CssTokens.LBRACE; }
    "}"                     { return CssTokens.RBRACE; }
    {Identifier}            { return CssTokens.IDENTIFIER; }
    {TraditionalComment}    { return CssTokens.BLOCK_COMMENT; }
    {AnySpace}+             { return CssTokens.WHITE_SPACE; }
    .                       { return CssTokens.BAD_CHARACTER; }
}