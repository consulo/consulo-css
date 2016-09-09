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

import com.intellij.lang.ASTNode;
import com.intellij.util.ReflectionUtil;
import consulo.css.lang.psi.CssElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssPsiElementType extends CssElementType {
    private final Constructor<? extends CssElement> myConstructor;

    public CssPsiElementType(@NotNull @NonNls String debugName, @NotNull Class<? extends CssElement> clazz) {
        super(debugName);
        try {
            myConstructor = clazz.getConstructor(ASTNode.class);
        } catch (NoSuchMethodException e) {
            throw new Error(e);
        }
    }

    @NotNull
    public CssElement createPsi(@NotNull ASTNode astNode) {
        return ReflectionUtil.createInstance(myConstructor, astNode);
    }
}
