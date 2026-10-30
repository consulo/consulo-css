package consulo.css.it.xml;

import consulo.application.ReadAction;
import consulo.it.AllowLogError;
import consulo.it.CodeInsightTestFixture;
import consulo.it.HeadlessProjectExtension;
import consulo.language.Language;
import consulo.language.psi.PsiDocumentManager;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.project.Project;
import consulo.ui.color.ColorValue;
import consulo.ui.color.RGBColor;
import consulo.undoRedo.CommandProcessor;
import consulo.xstylesheet.highlight.XStyleSheetElementColorProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * The color gutter of CSS values: reading and writing the color of one value part of a shorthand.
 */
@ExtendWith(HeadlessProjectExtension.class)
// consulo.xstylesheet (the property definitions) needs the XML plugin, which can't load completely headless
@AllowLogError({
    "consulo.language.impl.internal.psi.resolve.reference.ReferenceProvidersRegistryImpl",
    "consulo.xml.impl.internal.ExternalResource",
    "consulo.component.internal.NewExtensionPointImpl",
    // writes ask the scratch file service, which consulo-it doesn't bind
    "consulo.component.extension.ExtensionPoint"
})
public class CssColorValueTest {
    // as CssElementColorProvider
    private static final XStyleSheetElementColorProvider PROVIDER = new XStyleSheetElementColorProvider() {
        @Override
        public Language getLanguage() {
            return consulo.css.lang.CssLanguage.INSTANCE;
        }
    };

    @Test
    public void testShorthandColor(CodeInsightTestFixture fixture) throws Exception {
        PsiFile file = fixture.configureByText("a.css", "a { border: 1px solid #bbb; }");
        PsiElement length = leaf(file, "1px");
        PsiElement color = leaf(file, "#bbb");
        assertNull(ReadAction.compute(() -> PROVIDER.getColorFrom(length)));
        ColorValue value = ReadAction.compute(() -> PROVIDER.getColorFrom(color));
        assertNotNull(value);
        assertEquals(0xbb, value.toRGB().getRed());

        setColor(fixture, color, new RGBColor(255, 0, 0));
        assertEquals("a { border: 1px solid #f00; }", ReadAction.compute(file::getText));
    }

    @Test
    public void testKeywordAndAlpha(CodeInsightTestFixture fixture) throws Exception {
        PsiFile file = fixture.configureByText("b.css", "a { color: red; }");
        PsiElement color = leaf(file, "red");
        assertNotNull(ReadAction.compute(() -> PROVIDER.getColorFrom(color)));
        setColor(fixture, color, new RGBColor(0, 128, 255, 128));
        assertEquals("a { color: rgba(0, 128, 255, 50.2%); }", ReadAction.compute(file::getText));
    }

    @Test
    public void testRgbFunction(CodeInsightTestFixture fixture) throws Exception {
        PsiFile file = fixture.configureByText("c.css", "a { box-shadow: 0 0 2px rgb(255, 0, 0); }");
        PsiElement rgb = leaf(file, "rgb");
        PsiElement number = leaf(file, "255");
        ColorValue value = ReadAction.compute(() -> PROVIDER.getColorFrom(rgb));
        assertNotNull(value);
        assertEquals(255, value.toRGB().getRed());
        // one icon per value: on its first leaf only
        assertNull(ReadAction.compute(() -> PROVIDER.getColorFrom(number)));

        setColor(fixture, rgb, new RGBColor(0, 0, 255));
        assertEquals("a { box-shadow: 0 0 2px #00f; }", ReadAction.compute(file::getText));
    }

    private static PsiElement leaf(PsiFile file, String text) {
        return ReadAction.compute(() -> file.findElementAt(file.getText().indexOf(text)));
    }

    private static void setColor(CodeInsightTestFixture fixture, PsiElement element, ColorValue color) {
        Project project = fixture.getProject();
        project.getUIAccess().giveAndWait(() -> CommandProcessor.getInstance().newCommand().project(project).inWriteAction().run(() -> {
            PROVIDER.setColorTo(element, color);
            PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(fixture.getDocument());
            PsiDocumentManager.getInstance(project).commitAllDocuments();
        }));
    }
}
