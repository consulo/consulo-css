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

import consulo.css.lang.psi.*;
import consulo.language.ast.ElementTypeAsPsiFactory;
import consulo.language.ast.IElementType;

/**
 * @author VISTALL
 * @since 2013-07-03
 */
public interface CssElements {
    IElementType ROOT = new ElementTypeAsPsiFactory("ROOT", CssLanguage.INSTANCE, CssRoot::new);

    IElementType RULE = new ElementTypeAsPsiFactory("RULE", CssLanguage.INSTANCE, CssRule::new);

    IElementType BLOCK = new ElementTypeAsPsiFactory("BLOCK", CssLanguage.INSTANCE, CssBlock::new);

    IElementType PROPERTY = new ElementTypeAsPsiFactory("PROPERTY", CssLanguage.INSTANCE, CssProperty::new);

    IElementType VARIABLE = new ElementTypeAsPsiFactory("VARIABLE", CssLanguage.INSTANCE, CssVariable::new);

    IElementType VARIABLE_REFERENCE =
        new ElementTypeAsPsiFactory("VARIABLE_REFERENCE", CssLanguage.INSTANCE, CssVariableReference::new);

    IElementType PROPERTY_VALUE_PART =
        new ElementTypeAsPsiFactory("PROPERTY_VALUE_PART", CssLanguage.INSTANCE, CssPropertyValuePart::new);

    IElementType SELECTOR_SUFFIX_LIST =
        new ElementTypeAsPsiFactory("SELECTOR_SUFFIX_LIST", CssLanguage.INSTANCE, CssSelectorSuffixListImpl::new);

    IElementType SELECTOR_LIST = new ElementTypeAsPsiFactory("SELECTOR_LIST", CssLanguage.INSTANCE, CssSelectorListImpl::new);

    IElementType SELECTOR = new ElementTypeAsPsiFactory("SELECTOR", CssLanguage.INSTANCE, CssSelectorImpl::new);

    IElementType SIMPLE_SELECTOR =
        new ElementTypeAsPsiFactory("SIMPLE_SELECTOR", CssLanguage.INSTANCE, CssSimpleSelectorImpl::new);

    IElementType SELECTOR_PSEUDO_CLASS =
        new ElementTypeAsPsiFactory("SELECTOR_PSEUDO_CLASS", CssLanguage.INSTANCE, CssSelectorPseudoClass::new);

    IElementType SELECTOR_PSEUDO_CLASS_ARGUMENT_LIST = new ElementTypeAsPsiFactory(
        "SELECTOR_PSEUDO_CLASS_ARGUMENT_LIST",
        CssLanguage.INSTANCE,
        CssSelectorPseudoClassArgumentList::new
    );

    IElementType SELECTOR_ATTRIBUTE =
        new ElementTypeAsPsiFactory("SELECTOR_ATTRIBUTE", CssLanguage.INSTANCE, CssSelectorAttribute::new);

    IElementType SELECTOR_ATTRIBUTE_LIST =
        new ElementTypeAsPsiFactory("SELECTOR_ATTRIBUTE_LIST", CssLanguage.INSTANCE, CssSelectorAttributeList::new);

    IElementType FUNCTION_CALL = new ElementTypeAsPsiFactory("FUNCTION_CALL", CssLanguage.INSTANCE, CssFunctionCall::new);

    IElementType FUNCTION_CALL_PARAMETER_LIST =
        new ElementTypeAsPsiFactory("FUNCTION_CALL_PARAMETER_LIST", CssLanguage.INSTANCE, CssFunctionCallParameterList::new);

    IElementType CHARSET = new ElementTypeAsPsiFactory("CHARSET", CssLanguage.INSTANCE, CssCharset::new);

    IElementType IMPORT = new ElementTypeAsPsiFactory("IMPORT", CssLanguage.INSTANCE, CssImport::new);
}
