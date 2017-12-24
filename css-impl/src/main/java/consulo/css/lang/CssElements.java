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

package consulo.css.lang;

import com.intellij.psi.tree.IElementType;
import consulo.css.lang.psi.CssRoot;
import consulo.css.lang.psi.*;
import consulo.psi.tree.ElementTypeAsPsiFactory;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public interface CssElements
{
	IElementType ROOT = new ElementTypeAsPsiFactory("ROOT", CssLanguage.INSTANCE, CssRoot.class);

	IElementType RULE = new ElementTypeAsPsiFactory("RULE", CssLanguage.INSTANCE, CssRule.class);

	IElementType BLOCK = new ElementTypeAsPsiFactory("BLOCK", CssLanguage.INSTANCE, CssBlock.class);

	IElementType PROPERTY = new ElementTypeAsPsiFactory("PROPERTY", CssLanguage.INSTANCE, CssProperty.class);

	IElementType PROPERTY_VALUE_PART = new ElementTypeAsPsiFactory("PROPERTY_VALUE_PART", CssLanguage.INSTANCE, CssPropertyValuePart.class);

	IElementType SELECTOR_SUFFIX_LIST = new ElementTypeAsPsiFactory("SELECTOR_SUFFIX_LIST", CssLanguage.INSTANCE, CssSelectorSuffixListImpl.class);

	IElementType SELECTOR_LIST = new ElementTypeAsPsiFactory("SELECTOR_LIST", CssLanguage.INSTANCE, CssSelectorListImpl.class);

	IElementType SELECTOR = new ElementTypeAsPsiFactory("SELECTOR", CssLanguage.INSTANCE, CssSelectorImpl.class);

	IElementType SIMPLE_SELECTOR = new ElementTypeAsPsiFactory("SIMPLE_SELECTOR", CssLanguage.INSTANCE, CssSimpleSelectorImpl.class);

	IElementType SELECTOR_PSEUDO_CLASS = new ElementTypeAsPsiFactory("SELECTOR_PSEUDO_CLASS", CssLanguage.INSTANCE, CssSelectorPseudoClass.class);

	IElementType SELECTOR_ATTRIBUTE = new ElementTypeAsPsiFactory("SELECTOR_ATTRIBUTE", CssLanguage.INSTANCE, CssSelectorAttribute.class);

	IElementType SELECTOR_ATTRIBUTE_LIST = new ElementTypeAsPsiFactory("SELECTOR_ATTRIBUTE_LIST", CssLanguage.INSTANCE, CssSelectorAttributeList.class);

	IElementType FUNCTION_CALL = new ElementTypeAsPsiFactory("FUNCTION_CALL", CssLanguage.INSTANCE, CssFunctionCall.class);

	IElementType FUNCTION_CALL_PARAMETER_LIST = new ElementTypeAsPsiFactory("FUNCTION_CALL_PARAMETER_LIST", CssLanguage.INSTANCE, CssFunctionCallParameterList.class);
}
