package org.consulo.css.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ArrayUtil;
import com.intellij.util.IncorrectOperationException;
import org.consulo.css.lang.CssTokens;
import org.consulo.xstylesheet.definition.XStyleSheetProperty;
import org.consulo.xstylesheet.definition.XStyleSheetPropertyValueEntry;
import org.consulo.xstylesheet.definition.XStyleSheetPropertyValuePart;
import org.consulo.xstylesheet.definition.XStyleSheetTable;
import org.consulo.xstylesheet.psi.PsiXStyleSheetProperty;
import org.consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssProperty extends CssElement implements PsiNameIdentifierOwner, PsiXStyleSheetProperty {
  public CssProperty(@NotNull ASTNode node) {
    super(node);
  }

  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    PsiElement firstChild = getFirstChild();
    if (firstChild.getNode().getElementType() == CssTokens.IDENTIFIER) {
      return firstChild;
    }
    return null;
  }

  @Nullable
  @Override
  public XStyleSheetProperty getXStyleSheetProperty() {
    String name = getName();
    if (name == null) {
      return null;
    }
    CssFile containingFile = getContainingFile();
    XStyleSheetTable xStyleSheetTable = containingFile.getXStyleSheetTable();
    return xStyleSheetTable.findProperty(name);
  }

  @NotNull
  @Override
  public PsiXStyleSheetPropertyValuePart[] getParts() {
    return findChildrenByClass(PsiXStyleSheetPropertyValuePart.class);
  }

  @Override
  public String getName() {
    PsiElement nameIdentifier = getNameIdentifier();
    return nameIdentifier == null ? null : nameIdentifier.getText();
  }

  @Override
  public PsiElement setName(@NonNls @NotNull String s) throws IncorrectOperationException {
    return null;
  }
}
