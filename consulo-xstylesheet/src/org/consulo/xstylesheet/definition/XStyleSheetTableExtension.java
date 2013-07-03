package org.consulo.xstylesheet.definition;

import com.intellij.openapi.extensions.AbstractExtensionPointBean;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.psi.PsiFile;
import com.intellij.util.xmlb.annotations.Attribute;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class XStyleSheetTableExtension extends AbstractExtensionPointBean {
    public static final ExtensionPointName<XStyleSheetTableExtension> EP_NAME = ExtensionPointName.create("org.consulo.xstylesheet.table");

    @Attribute("file")
    public String file;

    @Attribute("condition")
    public Condition<PsiFile> condition;

    private NotNullLazyValue<XStyleSheetTable> myLazyTableValue = new NotNullLazyValue<XStyleSheetTable>() {
        @NotNull
        @Override
        protected XStyleSheetTable compute() {
            InputStream resourceAsStream = getLoaderForClass().getResourceAsStream(file);
            if(resourceAsStream == null) {

            }
            return null;
        }
    };

    public XStyleSheetTable getTable() {
        return myLazyTableValue.getValue();
    }
}
