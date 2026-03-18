package consulo.scss.lang.lexer;

import consulo.css.lang.CssTokens;
import consulo.scss.lang.ScssTokens;
import consulo.language.ast.IElementType;
import consulo.language.lexer.LexerBase;

%%

%public
%class _ScssLexer
%extends LexerBase
%unicode
%function advanceImpl
%type IElementType

%state URL_BODY, URL_START

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace = [ \t]
AnySpace = {LineTerminator} | {WhiteSpace} | [\f]

EscapedCharacter = \\{InputCharacter}

TraditionalComment = ("/*"[^]{CommentContent})|"/*"
CommentContent = ([^"*"]*("*"+[^"*""/"])?)*("*"+"/")?

// Exclude $ from identifiers — in SCSS, $ is the variable prefix token
IdentifierPart = [[:jletter:]&&[^$]]? [[:jletterdigit:]&&[^$]]?
Identifier=("-"? {IdentifierPart}*)*

StringLiteral = \" ( \\\" | [^\"\n\r] )* \"
StringLiteral2 = \' ( \\\' | [^\'\n\r] )* \'
AnyStringLiteral = {StringLiteral} | {StringLiteral2}
NumberLiteral = [0-9]+ | [0-9]*\.[0-9]+
NumberLiteralWithSufixes = "-"? {NumberLiteral} ("in" | "cm" | "mm" | "pt" | "pc" | "px" | "em" | "ex" | "deg" | "%")?
HexNumberLiteral = "#" ([_0-9A-Fa-f])+

// test url part
nonascii      = [^\000-\177]
unicode       = \\{h}{1,6}(\r\n|[ \t\r\n\f])?
escape        = {unicode}|\\[^\r\n\f0-9a-fA-F]
h             = [0-9a-fA-F]

URL_PATTERN = ([!#$%&*-~]|{nonascii}|{escape})*

LineComment = "//" {InputCharacter}*
%%

// some idea from https://github.com/flyingsaucerproject/flyingsaucer/blob/master/flying-saucer-core/src/main/java/org/xhtmlrenderer/css/parser/Lexer.flex

<YYINITIAL>
{
    "{"                     { return CssTokens.LBRACE; }
    "}"                     { return CssTokens.RBRACE; }
    "["                     { return CssTokens.LBRACKET; }
    "]"                     { return CssTokens.RBRACKET; }
    "("                     { return CssTokens.LPAR; }
    ")"                     { return CssTokens.RPAR; }
    ":"                     { return CssTokens.COLON; }
    ";"                     { return CssTokens.SEMICOLON; }
    "::"                    { return CssTokens.COLONCOLON; }
    "="                     { return CssTokens.EQ; }
    ">"                     { return CssTokens.GT; }
    ","                     { return CssTokens.COMMA; }
    "*"                     { return CssTokens.ASTERISK; }
    "#"                     { return CssTokens.SHARP; }
    "~="                    { return CssTokens.TILDE_EQ; }
    "*="                    { return CssTokens.MUL_EQ; }
    "|="                    { return CssTokens.OR_EQ; }
    "^="                    { return CssTokens.XOR_EQ; }
    "$="                    { return CssTokens.DOL_EQ; }
    "."                     { return CssTokens.DOT; }
    "+"                     { return CssTokens.PLUS; }
    "~"                     { return CssTokens.TILDE; }
    "%"                     { return CssTokens.PERC; }

    // SCSS: line comment must be BEFORE slash
    {LineComment}           { return ScssTokens.LINE_COMMENT; }
    "/"                     { return CssTokens.SLASH; }

    // SCSS: variable prefix and parent selector
    "$"                     { return ScssTokens.DOLLAR; }
    "&"                     { return ScssTokens.AMPERSAND; }

    // SCSS: at-rules
    "@mixin"                { return ScssTokens.MIXIN_KEYWORD; }
    "@include"              { return ScssTokens.INCLUDE_KEYWORD; }
    "@use"                  { return ScssTokens.USE_KEYWORD; }
    "@forward"              { return ScssTokens.FORWARD_KEYWORD; }

    // CSS: at-rules
    "@import"               { return CssTokens.IMPORT_KEYWORD;}
    "@page"                 { return CssTokens.PAGE_KEYWORD;}
    "@media"                { return CssTokens.MEDIA_KEYWORD;}
    "@charset"              { return CssTokens.CHARSET_KEYWORD;}
    "@namespace"            { return CssTokens.NAMESPACE_KEYWORD;}
    "@font-face"            { return CssTokens.FONT_FACE_KEYWORD;}
    "@keyframes"            { return CssTokens.KEYFRAMES_KEYWORD;}
    "@-webkit-keyframes"    { return CssTokens.KEYFRAMES_KEYWORD;}
    "@-moz-keyframes"       { return CssTokens.KEYFRAMES_KEYWORD;}
    "@-o-keyframes"         { return CssTokens.KEYFRAMES_KEYWORD;}
    "!"({TraditionalComment}+|{WhiteSpace}+)?"important"   { return CssTokens.IMPORTANT_KEYWORD;  }

    "url("                  { yypushback(yylength()); yybegin(URL_START);}

    {NumberLiteralWithSufixes}  { return CssTokens.NUMBER; }

    {Identifier}            { return CssTokens.IDENTIFIER; }

    {AnyStringLiteral}      { return CssTokens.STRING; }

    {TraditionalComment}    { return CssTokens.BLOCK_COMMENT; }

    {AnySpace}+             { return CssTokens.WHITE_SPACE; }

    [^]                     { return CssTokens.BAD_CHARACTER; }
}

<URL_START>
{
    // it's 'url' always
    {Identifier}            { return CssTokens.IDENTIFIER; }

    "("                     { yybegin(URL_BODY); return CssTokens.LPAR; }

    [^]                     { return CssTokens.BAD_CHARACTER; }
}


<URL_BODY>
{
    {URL_PATTERN}           { return CssTokens.URL_TOKEN; }

    {AnyStringLiteral}      { return CssTokens.STRING; }

    ","                     { return CssTokens.COMMA; }

    ")"                     { yybegin(YYINITIAL); return CssTokens.RPAR; }

    {AnySpace}+             { return CssTokens.WHITE_SPACE; }

    [^]                     { return CssTokens.BAD_CHARACTER; }
}
