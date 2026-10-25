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

package consulo.css.lang.psi.stub;

import consulo.language.psi.PsiNamedElement;
import consulo.language.psi.stub.IStubElementType;
import consulo.language.psi.stub.NamedStubBase;
import consulo.language.psi.stub.StubElement;
import org.jspecify.annotations.Nullable;

/**
 * Stub of a named CSS element: keeps only the name.
 *
 * @since 2026-10-25
 */
public class CssNamedStub<T extends PsiNamedElement> extends NamedStubBase<T> {
    public CssNamedStub(@Nullable StubElement parent, IStubElementType elementType, @Nullable String name) {
        super(parent, elementType, name);
    }

    @Override
    public String getName() {
        String name = super.getName();
        return name == null ? "" : name;
    }
}
