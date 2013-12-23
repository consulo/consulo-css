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

package org.consulo.css.lang;

import org.consulo.css.lang.psi.*;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public interface CssPsiTokens {
	IElementType RULE = new CssPsiElementType("RULE", CssRule.class);

	IElementType BLOCK = new CssPsiElementType("BLOCK", CssBlock.class);

	IElementType PROPERTY = new CssPsiElementType("PROPERTY", CssProperty.class);

	IElementType PROPERTY_VALUE_PART = new CssPsiElementType("PROPERTY_VALUE_PART", CssPropertyValuePart.class);

	IElementType SELECTOR_DECLARATION_LIST = new CssPsiElementType("SELECTOR_DECLARATION_LIST", CssSelectorDeclarationList.class);

	IElementType SELECTOR_DECLARATION = new CssPsiElementType("SELECTOR_DECLARATION", CssSelectorDeclaration.class);

	IElementType SELECTOR_REFERENCE = new CssPsiElementType("SELECTOR_REFERENCE", CssSelectorReference.class);

	IElementType SELECTOR_PSEUDO_CLASS = new CssPsiElementType("SELECTOR_PSEUDO_CLASS", CssSelectorPseudoClass.class);

	IElementType SELECTOR_ATTRIBUTE = new CssPsiElementType("SELECTOR_ATTRIBUTE", CssSelectorAttribute.class);

	IElementType SELECTOR_ATTRIBUTE_LIST = new CssPsiElementType("SELECTOR_ATTRIBUTE_LIST", CssSelectorAttributeList.class);

	IElementType FUNCTION_CALL = new CssPsiElementType("FUNCTION_CALL", CssFunctionCall.class);

	IElementType FUNCTION_CALL_PARAMETER_LIST = new CssPsiElementType("FUNCTION_CALL_PARAMETER_LIST", CssFunctionCallParameterList.class);
}
