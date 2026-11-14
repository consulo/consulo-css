package consulo.css.it.xml;

import consulo.application.ReadAction;
import consulo.it.AllowLogError;
import consulo.it.CodeInsightTestFixture;
import consulo.it.HeadlessProjectExtension;
import consulo.language.psi.PsiFile;
import consulo.xstylesheet.definition.XStyleSheetAtRule;
import consulo.xstylesheet.definition.XStyleSheetTable;
import consulo.xstylesheet.psi.XStyleSheetFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The at-rules of the stylesheet table of a CSS file, loaded from the MDN data, as the completion after {@code @} offers
 * them
 */
@ExtendWith(HeadlessProjectExtension.class)
// consulo.xstylesheet (the property definitions) needs the XML plugin, which can't load completely headless
@AllowLogError({
    "consulo.language.impl.internal.psi.resolve.reference.ReferenceProvidersRegistryImpl",
    "consulo.xml.impl.internal.ExternalResource",
    "consulo.component.internal.NewExtensionPointImpl"
})
public class CssAtRuleTableTest {
    @Test
    public void atRulesOfCss(CodeInsightTestFixture fixture) throws Exception {
        PsiFile file = fixture.configureByText("a.css", "a { color: red; }");
        XStyleSheetTable table = ReadAction.compute(() -> XStyleSheetFile.getXStyleSheetTable(file));

        assertThat(table.getAtRules()).extracting(XStyleSheetAtRule::name).contains("media", "import", "font-face", "supports", "layer");
        XStyleSheetAtRule media = table.findAtRule("media");
        assertThat(media).isNotNull();
        assertThat(media.syntax()).startsWith("@media ");
        assertThat(media.url()).endsWith("/@media");
        assertThat(table.findAtRule("apply")).isNull();
    }
}
