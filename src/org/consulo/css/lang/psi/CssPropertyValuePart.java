package org.consulo.css.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.ArrayUtil;
import org.consulo.xstylesheet.definition.XStyleSheetProperty;
import org.consulo.xstylesheet.definition.XStyleSheetPropertyValueEntry;
import org.consulo.xstylesheet.definition.XStyleSheetPropertyValuePart;
import org.consulo.xstylesheet.psi.PsiXStyleSheetProperty;
import org.consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import org.jetbrains.annotations.NotNull;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssPropertyValuePart extends CssElement implements PsiXStyleSheetPropertyValuePart {
  public CssPropertyValuePart(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public Object getValue() {
    PsiXStyleSheetProperty parent = (PsiXStyleSheetProperty) getParent();

    XStyleSheetProperty xStyleSheetProperty = parent.getXStyleSheetProperty();
    if(xStyleSheetProperty == null) {
      return null;
    }
 
    XStyleSheetPropertyValueEntry[] validEntries = xStyleSheetProperty.getValidEntries();

    PsiXStyleSheetPropertyValuePart[] parts = parent.getParts();

    int i = ArrayUtil.indexOf(parts, this);

    String text = getText().trim();
    XStyleSheetPropertyValueEntry validEntry = validEntries[i];
    for (XStyleSheetPropertyValuePart valuePart : validEntry.getParts()) {
      Object o = valuePart.fromString(text);
      if(o != null) {
        return o;
      }
    }
    return null;
  }

  @Override
  public XStyleSheetPropertyValuePart getValuePart() {
    PsiXStyleSheetProperty parent = (PsiXStyleSheetProperty) getParent();

    XStyleSheetProperty xStyleSheetProperty = parent.getXStyleSheetProperty();
    if(xStyleSheetProperty == null) {
      return null;
    }

    XStyleSheetPropertyValueEntry[] validEntries = xStyleSheetProperty.getValidEntries();

    PsiXStyleSheetPropertyValuePart[] parts = parent.getParts();

    int i = ArrayUtil.indexOf(parts, this);

    String text = getText().trim();
    XStyleSheetPropertyValueEntry validEntry = validEntries[i];
    for (XStyleSheetPropertyValuePart valuePart : validEntry.getParts()) {
      Object o = valuePart.fromString(text);
      if(o != null) {
        return valuePart;
      }
    }
    return null;
  }

  @Override
  public XStyleSheetPropertyValuePart[] getValueParts() {
    PsiXStyleSheetProperty parent = (PsiXStyleSheetProperty) getParent();

    XStyleSheetProperty xStyleSheetProperty = parent.getXStyleSheetProperty();
    if(xStyleSheetProperty == null) {
      return null;
    }

    XStyleSheetPropertyValueEntry[] validEntries = xStyleSheetProperty.getValidEntries();

    PsiXStyleSheetPropertyValuePart[] parts = parent.getParts();

    int i = ArrayUtil.indexOf(parts, this);

    XStyleSheetPropertyValueEntry validEntry = validEntries[i];
    return validEntry.getParts();
  }
}
