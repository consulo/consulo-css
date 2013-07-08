package org.consulo.css.editor.structureView;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import org.consulo.css.lang.psi.CssFile;
import org.consulo.css.lang.psi.CssRule;
import org.consulo.css.lang.psi.CssSelectorReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public class CssStructureViewTreeElement extends PsiTreeElementBase<CssFile> {

  public CssStructureViewTreeElement(CssFile psiElement) {
    super(psiElement);
  }

  @NotNull
  @Override
  public Collection<StructureViewTreeElement> getChildrenBase() {
    List<StructureViewTreeElement> list = new ArrayList<StructureViewTreeElement>();
    for (CssRule cssRule : getElement().getRules()) {
      for (CssSelectorReference cssSelectorReference : cssRule.getSelectorReferences()) {
        list.add(new CssSelectorReferenceStructureViewTreeElement(cssSelectorReference));
      }
    }
    return list;
  }

  @Nullable
  @Override
  public String getPresentableText() {
    return getElement().getName();
  }
}
