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

import consulo.language.ast.IElementType;

/**
 * SCSS-specific tokens beyond what CssTokens provides.
 *
 * @author VISTALL
 * @since 2026-03-16
 */
public interface ScssTokens {
    IElementType LINE_COMMENT = new ScssElementType("LINE_COMMENT");
    IElementType DOLLAR = new ScssElementType("DOLLAR");
    IElementType AMPERSAND = new ScssElementType("AMPERSAND");
    IElementType MIXIN_KEYWORD = new ScssElementType("MIXIN_KEYWORD");
    IElementType INCLUDE_KEYWORD = new ScssElementType("INCLUDE_KEYWORD");
    IElementType USE_KEYWORD = new ScssElementType("USE_KEYWORD");
    IElementType FORWARD_KEYWORD = new ScssElementType("FORWARD_KEYWORD");
}
