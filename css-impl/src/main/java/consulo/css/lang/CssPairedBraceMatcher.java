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

import consulo.annotation.component.ExtensionImpl;
import consulo.language.BracePair;
import consulo.language.Language;
import consulo.language.PairedBraceMatcher;

import javax.annotation.Nonnull;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
@ExtensionImpl
public class CssPairedBraceMatcher implements PairedBraceMatcher {
    private BracePair[] myPairs = new BracePair[]{
        new BracePair(CssTokens.LPAR, CssTokens.RPAR, false),
        new BracePair(CssTokens.LBRACE, CssTokens.RBRACE, true),
        new BracePair(CssTokens.LBRACKET, CssTokens.RBRACKET, false),
    };

    @Override
    public BracePair[] getPairs() {
        return myPairs;
    }

    @Nonnull
    @Override
    public Language getLanguage() {
        return CssLanguage.INSTANCE;
    }
}
