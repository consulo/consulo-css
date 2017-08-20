/* The following code was generated by JFlex 1.4.4 on 8/20/17 7:09 PM */

package consulo.css.lang.lexer;

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import consulo.css.lang.CssTokens;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.4
 * on 8/20/17 7:09 PM from the specification file
 * <tt>W:/_github.com/consulo/consulo-css/src/consulo/css/lang/lexer/_CssLexer.flex</tt>
 */
public class _CssLexer extends LexerBase {
  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int BODY = 2;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1, 1
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\7\1\2\1\1\1\0\1\3\1\1\16\7\4\0\1\2\1\43"+
    "\1\10\1\25\1\7\1\24\1\0\1\11\1\40\1\41\1\6\1\37"+
    "\1\36\1\7\1\13\1\5\12\12\1\33\1\42\1\0\1\34\1\35"+
    "\2\0\6\26\24\7\1\31\1\4\1\32\1\0\1\26\1\0\1\46"+
    "\1\26\1\16\1\26\1\23\1\26\2\7\1\14\3\7\1\17\1\15"+
    "\1\44\1\20\1\7\1\45\1\7\1\21\3\7\1\22\2\7\1\27"+
    "\1\0\1\30\1\0\41\7\2\0\4\7\4\0\1\7\2\0\1\7"+
    "\7\0\1\7\4\0\1\7\5\0\27\7\1\0\37\7\1\0\u01ca\7"+
    "\4\0\14\7\16\0\5\7\7\0\1\7\1\0\1\7\21\0\160\7"+
    "\5\7\1\0\2\7\2\0\4\7\10\0\1\7\1\0\3\7\1\0"+
    "\1\7\1\0\24\7\1\0\123\7\1\0\213\7\1\0\5\7\2\0"+
    "\236\7\11\0\46\7\2\0\1\7\7\0\47\7\7\0\1\7\1\0"+
    "\55\7\1\0\1\7\1\0\2\7\1\0\2\7\1\0\1\7\10\0"+
    "\33\7\5\0\3\7\15\0\5\7\6\0\1\7\4\0\13\7\5\0"+
    "\53\7\37\7\4\0\2\7\1\7\143\7\1\0\1\7\10\7\1\0"+
    "\6\7\2\7\2\7\1\0\4\7\2\7\12\7\3\7\2\0\1\7"+
    "\17\0\1\7\1\7\1\7\36\7\33\7\2\0\131\7\13\7\1\7"+
    "\16\0\12\7\41\7\11\7\2\7\4\0\1\7\5\0\26\7\4\7"+
    "\1\7\11\7\1\7\3\7\1\7\5\7\22\0\31\7\3\7\104\0"+
    "\1\7\1\0\13\7\67\0\33\7\1\0\4\7\66\7\3\7\1\7"+
    "\22\7\1\7\7\7\12\7\2\7\2\0\12\7\1\0\7\7\1\0"+
    "\7\7\1\0\3\7\1\0\10\7\2\0\2\7\2\0\26\7\1\0"+
    "\7\7\1\0\1\7\3\0\4\7\2\0\1\7\1\7\7\7\2\0"+
    "\2\7\2\0\3\7\1\7\10\0\1\7\4\0\2\7\1\0\3\7"+
    "\2\7\2\0\12\7\4\7\7\0\1\7\5\0\3\7\1\0\6\7"+
    "\4\0\2\7\2\0\26\7\1\0\7\7\1\0\2\7\1\0\2\7"+
    "\1\0\2\7\2\0\1\7\1\0\5\7\4\0\2\7\2\0\3\7"+
    "\3\0\1\7\7\0\4\7\1\0\1\7\7\0\14\7\3\7\1\7"+
    "\13\0\3\7\1\0\11\7\1\0\3\7\1\0\26\7\1\0\7\7"+
    "\1\0\2\7\1\0\5\7\2\0\1\7\1\7\10\7\1\0\3\7"+
    "\1\0\3\7\2\0\1\7\17\0\2\7\2\7\2\0\12\7\1\0"+
    "\1\7\17\0\3\7\1\0\10\7\2\0\2\7\2\0\26\7\1\0"+
    "\7\7\1\0\2\7\1\0\5\7\2\0\1\7\1\7\7\7\2\0"+
    "\2\7\2\0\3\7\10\0\2\7\4\0\2\7\1\0\3\7\2\7"+
    "\2\0\12\7\1\0\1\7\20\0\1\7\1\7\1\0\6\7\3\0"+
    "\3\7\1\0\4\7\3\0\2\7\1\0\1\7\1\0\2\7\3\0"+
    "\2\7\3\0\3\7\3\0\14\7\4\0\5\7\3\0\3\7\1\0"+
    "\4\7\2\0\1\7\6\0\1\7\16\0\12\7\11\0\1\7\7\0"+
    "\3\7\1\0\10\7\1\0\3\7\1\0\27\7\1\0\12\7\1\0"+
    "\5\7\3\0\1\7\7\7\1\0\3\7\1\0\4\7\7\0\2\7"+
    "\1\0\2\7\6\0\2\7\2\7\2\0\12\7\22\0\2\7\1\0"+
    "\10\7\1\0\3\7\1\0\27\7\1\0\12\7\1\0\5\7\2\0"+
    "\1\7\1\7\7\7\1\0\3\7\1\0\4\7\7\0\2\7\7\0"+
    "\1\7\1\0\2\7\2\7\2\0\12\7\1\0\2\7\17\0\2\7"+
    "\1\0\10\7\1\0\3\7\1\0\51\7\2\0\1\7\7\7\1\0"+
    "\3\7\1\0\4\7\1\7\10\0\1\7\10\0\2\7\2\7\2\0"+
    "\12\7\12\0\6\7\2\0\2\7\1\0\22\7\3\0\30\7\1\0"+
    "\11\7\1\0\1\7\2\0\7\7\3\0\1\7\4\0\6\7\1\0"+
    "\1\7\1\0\10\7\22\0\2\7\15\0\60\7\1\7\2\7\7\7"+
    "\4\0\10\7\10\7\1\0\12\7\47\0\2\7\1\0\1\7\2\0"+
    "\2\7\1\0\1\7\2\0\1\7\6\0\4\7\1\0\7\7\1\0"+
    "\3\7\1\0\1\7\1\0\1\7\2\0\2\7\1\0\4\7\1\7"+
    "\2\7\6\7\1\0\2\7\1\7\2\0\5\7\1\0\1\7\1\0"+
    "\6\7\2\0\12\7\2\0\4\7\40\0\1\7\27\0\2\7\6\0"+
    "\12\7\13\0\1\7\1\0\1\7\1\0\1\7\4\0\2\7\10\7"+
    "\1\0\44\7\4\0\24\7\1\0\2\7\5\7\13\7\1\0\44\7"+
    "\11\0\1\7\71\0\53\7\24\7\1\7\12\7\6\0\6\7\4\7"+
    "\4\7\3\7\1\7\3\7\2\7\7\7\3\7\4\7\15\7\14\7"+
    "\1\7\17\7\2\0\46\7\1\0\1\7\5\0\1\7\2\0\53\7"+
    "\1\0\u014d\7\1\0\4\7\2\0\7\7\1\0\1\7\1\0\4\7"+
    "\2\0\51\7\1\0\4\7\2\0\41\7\1\0\4\7\2\0\7\7"+
    "\1\0\1\7\1\0\4\7\2\0\17\7\1\0\71\7\1\0\4\7"+
    "\2\0\103\7\2\0\3\7\40\0\20\7\20\0\125\7\14\0\u026c\7"+
    "\2\0\21\7\1\0\32\7\5\0\113\7\3\0\3\7\17\0\15\7"+
    "\1\0\4\7\3\7\13\0\22\7\3\7\13\0\22\7\2\7\14\0"+
    "\15\7\1\0\3\7\1\0\2\7\14\0\64\7\40\7\3\0\1\7"+
    "\3\0\2\7\1\7\2\0\12\7\41\0\3\7\2\0\12\7\6\0"+
    "\130\7\10\0\51\7\1\7\1\7\5\0\106\7\12\0\35\7\3\0"+
    "\14\7\4\0\14\7\12\0\12\7\36\7\2\0\5\7\13\0\54\7"+
    "\4\0\21\7\7\7\2\7\6\0\12\7\46\0\27\7\5\7\4\0"+
    "\65\7\12\7\1\0\35\7\2\0\13\7\6\0\12\7\15\0\1\7"+
    "\130\0\5\7\57\7\21\7\7\7\4\0\12\7\21\0\11\7\14\0"+
    "\3\7\36\7\15\7\2\7\12\7\54\7\16\7\14\0\44\7\24\7"+
    "\10\0\12\7\3\0\3\7\12\7\44\7\122\0\3\7\1\0\25\7"+
    "\4\7\1\7\4\7\3\7\2\7\11\0\300\7\47\7\25\0\4\7"+
    "\u0116\7\2\0\6\7\2\0\46\7\2\0\6\7\2\0\10\7\1\0"+
    "\1\7\1\0\1\7\1\0\1\7\1\0\37\7\2\0\65\7\1\0"+
    "\7\7\1\0\1\7\3\0\3\7\1\0\7\7\3\0\4\7\2\0"+
    "\6\7\4\0\15\7\5\0\3\7\1\0\7\7\16\0\5\7\32\0"+
    "\5\7\20\0\2\7\23\0\1\7\13\0\5\7\5\0\6\7\1\0"+
    "\1\7\15\0\1\7\20\0\15\7\3\0\33\7\25\0\15\7\4\0"+
    "\1\7\3\0\14\7\21\0\1\7\4\0\1\7\2\0\12\7\1\0"+
    "\1\7\3\0\5\7\6\0\1\7\1\0\1\7\1\0\1\7\1\0"+
    "\4\7\1\0\13\7\2\0\4\7\5\0\5\7\4\0\1\7\21\0"+
    "\51\7\u0a77\0\57\7\1\0\57\7\1\0\205\7\6\0\4\7\3\7"+
    "\2\7\14\0\46\7\1\0\1\7\5\0\1\7\2\0\70\7\7\0"+
    "\1\7\17\0\1\7\27\7\11\0\7\7\1\0\7\7\1\0\7\7"+
    "\1\0\7\7\1\0\7\7\1\0\7\7\1\0\7\7\1\0\7\7"+
    "\1\0\40\7\57\0\1\7\u01d5\0\3\7\31\0\11\7\6\7\1\0"+
    "\5\7\2\0\5\7\4\0\126\7\2\0\2\7\2\0\3\7\1\0"+
    "\132\7\1\0\4\7\5\0\51\7\3\0\136\7\21\0\33\7\65\0"+
    "\20\7\u0200\0\u19b6\7\112\0\u51cd\7\63\0\u048d\7\103\0\56\7\2\0"+
    "\u010d\7\3\0\20\7\12\7\2\7\24\0\57\7\1\7\4\0\12\7"+
    "\1\0\31\7\7\0\1\7\120\7\2\7\45\0\11\7\2\0\147\7"+
    "\2\0\4\7\1\0\4\7\14\0\13\7\115\0\12\7\1\7\3\7"+
    "\1\7\4\7\1\7\27\7\5\7\20\0\1\7\7\0\64\7\14\0"+
    "\2\7\62\7\21\7\13\0\12\7\6\0\22\7\6\7\3\0\1\7"+
    "\4\0\12\7\34\7\10\7\2\0\27\7\15\7\14\0\35\7\3\0"+
    "\4\7\57\7\16\7\16\0\1\7\12\7\46\0\51\7\16\7\11\0"+
    "\3\7\1\7\10\7\2\7\2\0\12\7\6\0\27\7\3\0\1\7"+
    "\1\7\4\0\60\7\1\7\1\7\3\7\2\7\2\7\5\7\2\7"+
    "\1\7\1\7\1\7\30\0\3\7\2\0\13\7\5\7\2\0\3\7"+
    "\2\7\12\0\6\7\2\0\6\7\2\0\6\7\11\0\7\7\1\0"+
    "\7\7\221\0\43\7\10\7\1\0\2\7\2\0\12\7\6\0\u2ba4\7"+
    "\14\0\27\7\4\0\61\7\u2104\0\u016e\7\2\0\152\7\46\0\7\7"+
    "\14\0\5\7\5\0\1\7\1\7\12\7\1\0\15\7\1\0\5\7"+
    "\1\0\1\7\1\0\2\7\1\0\2\7\1\0\154\7\41\0\u016b\7"+
    "\22\0\100\7\2\0\66\7\50\0\15\7\3\0\20\7\20\0\7\7"+
    "\14\0\2\7\30\0\3\7\31\0\1\7\6\0\5\7\1\0\207\7"+
    "\2\0\1\7\4\0\1\7\13\0\12\7\7\0\32\7\4\0\1\7"+
    "\1\0\32\7\13\0\131\7\3\0\6\7\2\0\6\7\2\0\6\7"+
    "\2\0\3\7\3\0\2\7\3\0\2\7\22\0\3\7\4\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\2\1\1\2\1\3\1\2\1\4\1\1\2\2\1\5"+
    "\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1\15"+
    "\1\16\1\17\1\20\1\5\1\21\1\6\1\22\1\23"+
    "\1\24\1\25\1\26\1\2\1\27\2\0\1\30\2\0"+
    "\1\31\1\0\4\1\3\20\3\0\1\27\2\30\1\20"+
    "\13\0\1\27\15\0\1\32\1\0\1\32";

  private static int [] zzUnpackAction() {
    int [] result = new int[80];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\47\0\116\0\165\0\234\0\116\0\303\0\352"+
    "\0\u0111\0\116\0\116\0\116\0\116\0\116\0\116\0\u0138"+
    "\0\116\0\116\0\116\0\116\0\u015f\0\u0186\0\116\0\u01ad"+
    "\0\116\0\116\0\116\0\116\0\116\0\u01d4\0\u01fb\0\352"+
    "\0\u0222\0\116\0\u0111\0\u0249\0\116\0\u0186\0\u0270\0\u0297"+
    "\0\u02be\0\u02e5\0\116\0\u030c\0\u01ad\0\u0333\0\u035a\0\u0381"+
    "\0\u03a8\0\352\0\u0111\0\303\0\u03cf\0\u03f6\0\u041d\0\u0444"+
    "\0\u046b\0\u0492\0\u04b9\0\u04e0\0\u0507\0\u052e\0\u0555\0\116"+
    "\0\u057c\0\u05a3\0\u05ca\0\u05f1\0\u0618\0\u063f\0\u0666\0\u068d"+
    "\0\u06b4\0\u06db\0\u0702\0\u0729\0\u0750\0\116\0\u0777\0\u04e0";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[80];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\3\3\4\1\3\1\5\1\6\1\7\1\10\1\11"+
    "\1\7\1\12\10\7\1\3\1\13\1\7\1\14\1\15"+
    "\1\16\1\17\1\20\1\21\1\22\1\23\1\24\4\3"+
    "\3\7\1\3\3\4\1\3\1\5\1\6\1\7\1\10"+
    "\1\11\1\25\1\26\10\7\1\27\1\30\1\7\1\31"+
    "\1\32\1\16\1\17\1\20\1\21\1\3\1\23\1\24"+
    "\1\33\1\34\1\35\1\36\3\7\50\0\3\4\51\0"+
    "\1\37\47\0\1\7\2\0\1\7\1\0\10\7\2\0"+
    "\1\7\15\0\3\7\1\40\1\0\2\40\1\41\3\40"+
    "\1\42\36\40\1\43\1\0\2\43\1\44\4\43\1\42"+
    "\35\43\33\0\1\45\22\0\1\7\2\0\1\25\1\46"+
    "\1\47\1\7\2\50\1\51\2\7\1\52\1\53\1\0"+
    "\1\7\15\0\3\7\12\0\1\54\46\0\1\55\3\0"+
    "\1\55\4\0\1\55\2\0\1\55\17\0\1\55\2\0"+
    "\1\56\2\0\1\57\6\0\1\60\32\0\47\61\1\40"+
    "\1\0\2\40\1\41\3\40\1\62\36\40\1\43\1\0"+
    "\2\43\1\44\4\43\1\63\35\43\7\0\1\7\2\0"+
    "\1\7\1\0\1\7\1\64\6\7\2\0\1\7\15\0"+
    "\3\7\7\0\1\7\2\0\1\7\1\0\3\7\1\64"+
    "\4\7\2\0\1\7\15\0\3\7\7\0\1\7\2\0"+
    "\1\7\1\0\2\7\1\64\2\7\2\64\1\7\2\0"+
    "\1\7\15\0\3\7\7\0\1\7\2\0\1\7\1\0"+
    "\3\7\1\64\2\7\1\64\1\7\2\0\1\7\15\0"+
    "\3\7\12\0\1\54\1\0\1\65\1\0\2\66\1\67"+
    "\2\0\1\70\1\53\24\0\1\56\11\0\1\60\40\0"+
    "\1\71\57\0\1\72\27\0\6\61\1\73\40\61\15\0"+
    "\1\53\50\0\1\53\45\0\1\53\2\0\2\53\43\0"+
    "\1\53\2\0\1\53\24\0\5\74\1\75\6\74\1\76"+
    "\32\74\20\0\1\77\26\0\5\61\1\100\1\73\40\61"+
    "\5\74\1\75\1\101\5\74\1\76\37\74\1\75\1\71"+
    "\5\74\1\76\37\74\1\75\1\101\5\74\1\76\2\74"+
    "\1\102\27\74\44\0\1\103\2\0\5\74\1\104\1\101"+
    "\45\74\1\75\1\101\5\74\1\76\3\74\1\105\26\74"+
    "\45\0\1\106\6\0\1\57\6\0\1\60\32\0\5\74"+
    "\1\75\1\101\5\74\1\76\27\74\1\107\2\74\21\0"+
    "\1\110\25\0\5\74\1\75\1\101\5\74\1\76\30\74"+
    "\1\111\1\74\46\0\1\112\5\74\1\75\1\101\5\74"+
    "\1\76\4\74\1\113\25\74\15\0\1\114\31\0\5\74"+
    "\1\75\1\101\5\74\1\76\31\74\1\115\21\0\1\116"+
    "\25\0\5\74\1\75\1\101\5\74\1\76\1\117\36\74"+
    "\1\75\1\101\5\74\1\76\4\74\1\120\25\74";

  private static int [] zzUnpackTrans() {
    int [] result = new int[1950];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;
  private static final char[] EMPTY_BUFFER = new char[0];
  private static final int YYEOF = -1;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\2\1\1\11\2\1\1\11\3\1\6\11\1\1\4\11"+
    "\2\1\1\11\1\1\5\11\2\1\2\0\1\11\2\0"+
    "\1\11\1\0\4\1\1\11\2\1\3\0\4\1\13\0"+
    "\1\11\15\0\1\11\1\0\1\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[80];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private CharSequence zzBuffer = "";

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the textposition at the last state to be included in yytext */
  private int zzPushbackPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /**
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  private IElementType myTokenType;
  private int myState;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;



  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 2238) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }

  @Override
  public IElementType getTokenType() {
    if (myTokenType == null) locateToken();
    return myTokenType;
  }

  @Override
  public final int getTokenStart(){
    if (myTokenType == null) locateToken();
    return zzStartRead;
  }

  @Override
  public final int getTokenEnd(){
    if (myTokenType == null) locateToken();
    return getTokenStart() + yylength();
  }

  @Override
  public void advance() {
    if (myTokenType == null) locateToken();
    myTokenType = null;
  }

  @Override
  public int getState() {
    if (myTokenType == null) locateToken();
    return myState;
  }

  @Override
  public void start(final CharSequence buffer, int startOffset, int endOffset, final int initialState) {
    reset(buffer, startOffset, endOffset, initialState);
    myTokenType = null;
  }

   @Override
   public CharSequence getBufferSequence() {
     return zzBuffer;
   }

   @Override
   public int getBufferEnd() {
     return zzEndRead;
   }

  public void reset(CharSequence buffer, int start, int end,int initialState){
    zzBuffer = buffer;
    zzCurrentPos = zzMarkedPos = zzStartRead = start;
    zzPushbackPos = 0;
    zzAtEOF  = false;
    zzAtBOL = true;
    zzEndRead = end;
    yybegin(initialState);
    myTokenType = null;
  }

   protected void locateToken() {
     if (myTokenType != null) return;
     try {
       myState = yystate();
       myTokenType = advanceImpl();
     }
     catch (java.io.IOException e) { /*Can't happen*/ }
     catch (Error e) {
       // add lexer class name to the error
       final Error error = new Error(getClass().getName() + ": " + e.getMessage());
       error.setStackTrace(e.getStackTrace());
       throw error;
     }
   }

   /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   *
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {
    return true;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final CharSequence yytext() {
    return zzBuffer.subSequence(zzStartRead, zzMarkedPos);
  }


  /**
   * Returns the character at position <tt>pos</tt> from the
   * matched text.
   *
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch.
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer.charAt(zzStartRead+pos);
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of
   * yypushback(int) and a match-all fallback rule) this method
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() {
    if (!zzEOFDone) {
      zzEOFDone = true;
    
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public IElementType advanceImpl() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    CharSequence zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = ZZ_LEXSTATE[zzLexicalState];


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL.charAt(zzCurrentPosL++);
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL.charAt(zzCurrentPosL++);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 5: 
          { return CssTokens.DOT;
          }
        case 27: break;
        case 21: 
          { return CssTokens.RPAR;
          }
        case 28: break;
        case 3: 
          { return CssTokens.WHITE_SPACE;
          }
        case 29: break;
        case 4: 
          { return CssTokens.ASTERISK;
          }
        case 30: break;
        case 7: 
          { yybegin(BODY); return CssTokens.LBRACE;
          }
        case 31: break;
        case 25: 
          { return CssTokens.COLONCOLON;
          }
        case 32: break;
        case 24: 
          { return CssTokens.STRING;
          }
        case 33: break;
        case 10: 
          { return CssTokens.RBRACKET;
          }
        case 34: break;
        case 12: 
          { return CssTokens.EQ;
          }
        case 35: break;
        case 6: 
          { return CssTokens.SHARP;
          }
        case 36: break;
        case 16: 
          { return CssTokens.NUMBER;
          }
        case 37: break;
        case 13: 
          { return CssTokens.GE;
          }
        case 38: break;
        case 20: 
          { return CssTokens.LPAR;
          }
        case 39: break;
        case 18: 
          { return CssTokens.LBRACE;
          }
        case 40: break;
        case 17: 
          { return CssTokens.PERC;
          }
        case 41: break;
        case 14: 
          { return CssTokens.COMMA;
          }
        case 42: break;
        case 26: 
          { return CssTokens.IMPORTANT;
          }
        case 43: break;
        case 8: 
          { return CssTokens.RBRACE;
          }
        case 44: break;
        case 2: 
          { return CssTokens.BAD_CHARACTER;
          }
        case 45: break;
        case 15: 
          { return CssTokens.PLUS;
          }
        case 46: break;
        case 1: 
          { return CssTokens.IDENTIFIER;
          }
        case 47: break;
        case 22: 
          { return CssTokens.SEMICOLON;
          }
        case 48: break;
        case 19: 
          { yybegin(YYINITIAL); return CssTokens.RBRACE;
          }
        case 49: break;
        case 9: 
          { return CssTokens.LBRACKET;
          }
        case 50: break;
        case 23: 
          { return CssTokens.BLOCK_COMMENT;
          }
        case 51: break;
        case 11: 
          { return CssTokens.COLON;
          }
        case 52: break;
        default:
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            zzDoEOF();
            return null;
          }
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
