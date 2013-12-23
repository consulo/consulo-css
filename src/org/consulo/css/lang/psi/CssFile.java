/*
 * Copyright 2013 must-be.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.consulo.css.lang.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.util.SmartList;
import org.consulo.css.lang.CssFileType;
import org.consulo.css.lang.CssLanguage;
import org.consulo.xstylesheet.definition.XStyleSheetTable;
import org.consulo.xstylesheet.definition.XStyleSheetTableExtension;
import org.consulo.xstylesheet.definition.impl.EmptyXStyleSheetTable;
import org.consulo.xstylesheet.definition.impl.MergedXStyleSheetTable;
import org.consulo.xstylesheet.psi.reference.nameResolving.XStyleRuleCondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 02.07.13.
 */
public class CssFile extends PsiFileBase {
  public CssFile(@NotNull FileViewProvider provider) {
    super(provider, CssLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public FileType getFileType() {
    return CssFileType.INSTANCE;
  }

  @Override
  public void accept(@NotNull PsiElementVisitor psiElementVisitor) {
    psiElementVisitor.visitFile(this);
  }

  @Nullable
  public CssRule findRule(@NotNull XStyleRuleCondition condition) {
    for (CssRule o : getRules()) {
      for (CssSelectorDeclaration reference : o.getSelectorDeclarations()) {
        if (condition.isAccepted(reference)) {
          return o;
        }
      }
    }
    return null;
  }

  @NotNull
  public List<CssRule> findRules(@NotNull XStyleRuleCondition condition) {
    List<CssRule> list = new ArrayList<CssRule>();
    for (CssRule o : getRules()) {
      for (CssSelectorDeclaration reference : o.getSelectorDeclarations()) {
        if (condition.isAccepted(reference)) {
          list.add(o);
        }
      }
    }
    return list;
  }

  @NotNull
  public CssRule[] getRules() {
    return findChildrenByClass(CssRule.class);
  }

  @NotNull
  public XStyleSheetTable getXStyleSheetTable() {
    SmartList<XStyleSheetTable> list = new SmartList<XStyleSheetTable>();
    for (XStyleSheetTableExtension extension : XStyleSheetTableExtension.EP_NAME.getExtensions()) {
      /*if (extension.condition == null || extension.condition.value(this)) */
      {
        list.add(extension.getTable());
      }
    }
    return list.isEmpty() ? EmptyXStyleSheetTable.INSTANCE : new MergedXStyleSheetTable(list.toArray(new XStyleSheetTable[list.size()]));
  }
}
