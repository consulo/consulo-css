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

import consulo.language.Language;
import consulo.language.psi.PsiNamedElement;
import consulo.language.psi.stub.StubElement;
import consulo.language.psi.stub.StubInputStream;
import consulo.language.psi.stub.StubOutputStream;
import org.jspecify.annotations.Nullable;

import java.io.IOException;

/**
 * Named stub element type whose stub stores only the element name.
 *
 * @since 2026-10-25
 */
public abstract class CssSimpleNamedStubElementType<T extends PsiNamedElement> extends CssNamedStubElementType<T> {
    protected CssSimpleNamedStubElementType(String debugName, Language language) {
        super(debugName, language);
    }

    @Override
    public CssNamedStub<T> createStub(T psi, StubElement parentStub) {
        return new CssNamedStub<>(parentStub, this, psi.getName());
    }

    @Override
    public void serialize(CssNamedStub<T> stub, StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getName());
    }

    @Override
    public CssNamedStub<T> deserialize(StubInputStream dataStream, @Nullable StubElement parentStub) throws IOException {
        return new CssNamedStub<>(parentStub, this, dataStream.readNameString());
    }
}
