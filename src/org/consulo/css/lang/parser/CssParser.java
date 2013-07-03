package org.consulo.css.lang.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageVersion;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.consulo.css.lang.CssPsiTokens;
import org.consulo.css.lang.CssTokens;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssParser implements PsiParser, CssTokens, CssPsiTokens {
  @NotNull
  @Override
  public ASTNode parse(@NotNull IElementType iElementType, @NotNull PsiBuilder builder, @NotNull LanguageVersion languageVersion) {
    PsiBuilder.Marker mark = builder.mark();
    while (!builder.eof()) {
      if(builder.getTokenType() == IDENTIFIER) {
        PsiBuilder.Marker marker = builder.mark();

        parsAnySelector(builder);

        if(builder.getTokenType() == LBRACE) {
          PsiBuilder.Marker bodyMarker = builder.mark();
          builder.advanceLexer();

          while (!builder.eof()) {
            if(builder.getTokenType() == IDENTIFIER) {
              PsiBuilder.Marker propertyMarker = builder.mark();

              builder.advanceLexer();

              if(expect(builder, COLON, "':' expected")) {
                PsiBuilder.Marker valueMarker = null;

                while (!builder.eof()) {
                  if(builder.getTokenType() == COMMA) {
                    if(valueMarker != null) {
                      valueMarker.done(PROPERTY_VALUE_PART);
                      valueMarker = null;
                    }

                    builder.advanceLexer();
                  }
                  else if(builder.getTokenType() == SEMICOLON) {
                    break;
                  }
                  else {
                    if(valueMarker == null) {
                      valueMarker = builder.mark();
                    }
                    builder.advanceLexer();
                  }
                }

                if(valueMarker != null) {
                  valueMarker.done(PROPERTY_VALUE_PART);
                }
              }

              expect(builder, SEMICOLON, "';' expected");

              propertyMarker.done(PROPERTY);
            }
            else {
              break;
            }
          }

          expect(builder, RBRACE, "'}' expected");

          bodyMarker.done(BLOCK);
        }
        else {
          builder.error("'{' expected");
        }

        marker.done(RULE);
      }
      else {
        builder.advanceLexer();
      }
    }
    mark.done(iElementType);
    return builder.getTreeBuilt();
  }

  private static boolean expect(PsiBuilder builder, IElementType elementType, String message) {
    if(builder.getTokenType() != elementType) {
      builder.error(message);
      return false;
    }
    else {
      builder.advanceLexer();
      return true;
    }
  }

  private void parsAnySelector(PsiBuilder builder) {
    PsiBuilder.Marker refMarker = builder.mark();

    if(builder.getTokenType() == IDENTIFIER) {
      PsiBuilder.Marker marker = builder.mark();
      builder.advanceLexer();
      marker.done(SELECTOR_ELEMENT);
    }

    refMarker.done(SELECTOR_REFERENCE);
  }
}
