/*
 * Copyright 2013-2026 consulo.io
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

package consulo.scss.lang;

import consulo.css.lang.psi.*;
import consulo.language.ast.ElementTypeAsPsiFactory;
import consulo.language.ast.IElementType;
import consulo.scss.lang.psi.ScssInclude;
import consulo.scss.lang.psi.ScssMixin;
import consulo.scss.lang.psi.ScssVariableDeclaration;
import consulo.scss.lang.psi.ScssVariableReference;

/**
 * PSI element types mirroring CssElements but bound to ScssLanguage.
 * Reuses CSS PSI classes for standard constructs; adds SCSS-specific elements.
 *
 * @author VISTALL
 * @since 2026-03-16
 */
public interface ScssElements {
    IElementType ROOT = new ElementTypeAsPsiFactory("ROOT", ScssLanguage.INSTANCE, CssRoot::new);

    IElementType RULE = new ElementTypeAsPsiFactory("RULE", ScssLanguage.INSTANCE, CssRule::new);

    IElementType BLOCK = new ElementTypeAsPsiFactory("BLOCK", ScssLanguage.INSTANCE, CssBlock::new);

    IElementType PROPERTY = new ElementTypeAsPsiFactory("PROPERTY", ScssLanguage.INSTANCE, CssProperty::new);

    IElementType VARIABLE = new ElementTypeAsPsiFactory("VARIABLE", ScssLanguage.INSTANCE, CssVariable::new);

    IElementType VARIABLE_REFERENCE =
        new ElementTypeAsPsiFactory("VARIABLE_REFERENCE", ScssLanguage.INSTANCE, CssVariableReference::new);

    IElementType PROPERTY_VALUE_PART =
        new ElementTypeAsPsiFactory("PROPERTY_VALUE_PART", ScssLanguage.INSTANCE, CssPropertyValuePart::new);

    IElementType SELECTOR_SUFFIX_LIST =
        new ElementTypeAsPsiFactory("SELECTOR_SUFFIX_LIST", ScssLanguage.INSTANCE, CssSelectorSuffixListImpl::new);

    IElementType SELECTOR_LIST = new ElementTypeAsPsiFactory("SELECTOR_LIST", ScssLanguage.INSTANCE, CssSelectorListImpl::new);

    IElementType SELECTOR = new ElementTypeAsPsiFactory("SELECTOR", ScssLanguage.INSTANCE, CssSelectorImpl::new);

    IElementType SIMPLE_SELECTOR =
        new ElementTypeAsPsiFactory("SIMPLE_SELECTOR", ScssLanguage.INSTANCE, CssSimpleSelectorImpl::new);

    IElementType SELECTOR_PSEUDO_CLASS =
        new ElementTypeAsPsiFactory("SELECTOR_PSEUDO_CLASS", ScssLanguage.INSTANCE, CssSelectorPseudoClass::new);

    IElementType SELECTOR_PSEUDO_CLASS_ARGUMENT_LIST = new ElementTypeAsPsiFactory(
        "SELECTOR_PSEUDO_CLASS_ARGUMENT_LIST",
        ScssLanguage.INSTANCE,
        CssSelectorPseudoClassArgumentList::new
    );

    IElementType SELECTOR_ATTRIBUTE =
        new ElementTypeAsPsiFactory("SELECTOR_ATTRIBUTE", ScssLanguage.INSTANCE, CssSelectorAttribute::new);

    IElementType SELECTOR_ATTRIBUTE_LIST =
        new ElementTypeAsPsiFactory("SELECTOR_ATTRIBUTE_LIST", ScssLanguage.INSTANCE, CssSelectorAttributeList::new);

    IElementType FUNCTION_CALL = new ElementTypeAsPsiFactory("FUNCTION_CALL", ScssLanguage.INSTANCE, CssFunctionCall::new);

    IElementType FUNCTION_CALL_PARAMETER_LIST =
        new ElementTypeAsPsiFactory("FUNCTION_CALL_PARAMETER_LIST", ScssLanguage.INSTANCE, CssFunctionCallParameterList::new);

    IElementType CHARSET = new ElementTypeAsPsiFactory("CHARSET", ScssLanguage.INSTANCE, CssCharset::new);

    IElementType IMPORT = new ElementTypeAsPsiFactory("IMPORT", ScssLanguage.INSTANCE, CssImport::new);

    IElementType MEDIA = new ElementTypeAsPsiFactory("MEDIA", ScssLanguage.INSTANCE, CssMedia::new);

    IElementType MEDIA_QUERY_LIST =
        new ElementTypeAsPsiFactory("MEDIA_QUERY_LIST", ScssLanguage.INSTANCE, CssMediaQueryList::new);

    IElementType FONT_FACE = new ElementTypeAsPsiFactory("FONT_FACE", ScssLanguage.INSTANCE, CssFontFace::new);

    IElementType KEYFRAMES = new ElementTypeAsPsiFactory("KEYFRAMES", ScssLanguage.INSTANCE, CssKeyframes::new);

    IElementType KEYFRAME_SELECTOR =
        new ElementTypeAsPsiFactory("KEYFRAME_SELECTOR", ScssLanguage.INSTANCE, CssKeyframeSelector::new);

    // SCSS-specific elements
    IElementType SCSS_VARIABLE = new ElementTypeAsPsiFactory("SCSS_VARIABLE", ScssLanguage.INSTANCE, ScssVariableDeclaration::new);

    IElementType SCSS_VARIABLE_REFERENCE =
        new ElementTypeAsPsiFactory("SCSS_VARIABLE_REFERENCE", ScssLanguage.INSTANCE, ScssVariableReference::new);

    IElementType MIXIN = new ElementTypeAsPsiFactory("MIXIN", ScssLanguage.INSTANCE, ScssMixin::new);

    IElementType INCLUDE = new ElementTypeAsPsiFactory("INCLUDE", ScssLanguage.INSTANCE, ScssInclude::new);
}
