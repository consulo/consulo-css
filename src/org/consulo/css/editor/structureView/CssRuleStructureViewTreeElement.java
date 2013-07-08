package org.consulo.css.editor.structureView;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import org.consulo.css.lang.psi.CssRule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public class CssRuleStructureViewTreeElement extends PsiTreeElementBase<CssRule> {
  public CssRuleStructureViewTreeElement(CssRule psiElement) {
    super(psiElement);
  }

  @NotNull
  @Override
  public Collection<StructureViewTreeElement> getChildrenBase() {
    return Collections.emptyList();
  }

  @Nullable
  @Override
  public String getPresentableText() {
    return getElement().getSelectorReference().getSelectorPart().getOnlyName();
  }
}