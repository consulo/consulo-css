package consulo.css.it.xml;

import consulo.application.ReadAction;
import consulo.application.dumb.IndexNotReadyException;
import consulo.it.AllowLogError;
import consulo.it.CodeInsightTestFixture;
import consulo.it.HeadlessProjectExtension;
import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiReference;
import consulo.language.psi.ResolveResult;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.xml.language.psi.XmlAttribute;
import consulo.xstylesheet.impl.html.psi.reference.classOrId.HtmlIdOrClassToCssFileReference;
import consulo.xstylesheet.psi.PsiXStyleSheetRule;
import consulo.xstylesheet.psi.XStyleSheetSimpleSelector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The references of the class and id attributes of HTML to the rules of the linked stylesheets: one for each class of a
 * class list, soft, as a class may come from elsewhere
 */
@ExtendWith(HeadlessProjectExtension.class)
@AllowLogError({
    "consulo.language.impl.internal.psi.resolve.reference.ReferenceProvidersRegistryImpl",
    "consulo.xml.impl.internal.ExternalResource",
    "consulo.component.internal.NewExtensionPointImpl"
})
public class HtmlClassReferenceTest {
    @Test
    public void aSoftReferenceForEachClass(CodeInsightTestFixture fixture) throws Exception {
        fixture.addFileToProject(CodeInsightTestFixture.SOURCE_ROOT + "/styles.css", ".card { color: red; }");
        PsiFile file = fixture.configureByText("index.html", """
            <html>
            <head><link rel="stylesheet" href="styles.css"></head>
            <body><div id="main" class="card  card-large"></div></body>
            </html>
            """);

        List<HtmlIdOrClassToCssFileReference> classes = references(file, "class");
        assertThat(ReadAction.compute(() -> classes.stream().map(PsiReference::getCanonicalText).toList()))
            .containsExactly("card", "card-large");
        assertThat(ReadAction.compute(() -> classes.stream().allMatch(PsiReference::isSoft))).isTrue();

        PsiElement card = ReadAction.compute(() -> classes.get(0).resolve());
        assertThat(card).isInstanceOf(PsiXStyleSheetRule.class);
        assertThat(ReadAction.compute(() -> classes.get(1).resolve())).isNull();

        List<HtmlIdOrClassToCssFileReference> ids = references(file, "id");
        assertThat(ReadAction.compute(() -> ids.stream().map(PsiReference::getCanonicalText).toList())).containsExactly("main");
    }

    /**
     * A page without a linked stylesheet, which a layout or a bundler gives its styles: its classes resolve to the class
     * selectors of the project stylesheets
     */
    @Test
    public void classesResolveToTheProjectStylesheets(CodeInsightTestFixture fixture) throws Exception {
        fixture.addFileToProject("resources/css/app.css", ".card, .badge { color: red; }\n.card:hover { color: blue; }");
        // the report of an installed package, with a class of the same name
        fixture.addFileToProject("vendor/phpunit/report/style.css", ".card { margin: 0; }");
        PsiFile file = fixture.configureByText("welcome.html", "<div class=\"card unknown\"></div>");

        List<HtmlIdOrClassToCssFileReference> classes = references(file, "class");
        List<PsiElement> card = waitForSelectors(classes.get(0), 2);
        assertThat(card).allMatch(XStyleSheetSimpleSelector.class::isInstance);
        assertThat(ReadAction.compute(() -> card.get(0).getContainingFile().getName())).isEqualTo("app.css");
        assertThat(ReadAction.compute(() -> classes.get(1).resolve())).isNull();

        List<String> variants = ReadAction.compute(() -> Arrays.stream(classes.get(0).getVariants())
            .map(variant -> ((LookupElement) variant).getLookupString())
            .toList());
        assertThat(variants).contains("card", "badge");
    }

    /**
     * Polls the resolve until the class index answers, which a reindex in dumb mode may delay
     */
    private static List<PsiElement> waitForSelectors(HtmlIdOrClassToCssFileReference reference, int count) throws Exception {
        long deadline = System.currentTimeMillis() + 60_000;
        List<PsiElement> targets = List.of();
        while (System.currentTimeMillis() < deadline) {
            targets = ReadAction.compute(() -> {
                try {
                    return Arrays.stream(reference.multiResolve(false)).map(ResolveResult::getElement).toList();
                }
                catch (IndexNotReadyException e) {
                    return List.<PsiElement>of();
                }
            });
            if (targets.size() == count) {
                return targets;
            }
            Thread.sleep(50);
        }
        throw new AssertionError("timed out waiting for the class index, last answer: " + targets);
    }

    private static List<HtmlIdOrClassToCssFileReference> references(PsiFile file, String attributeName) {
        return ReadAction.compute(() -> {
            XmlAttribute attribute = PsiTreeUtil.findChildrenOfType(file, XmlAttribute.class).stream()
                .filter(a -> a.getName().equals(attributeName))
                .findFirst()
                .orElseThrow();
            PsiElement value = Objects.requireNonNull(attribute.getValueElement());
            return Arrays.stream(value.getReferences())
                .filter(HtmlIdOrClassToCssFileReference.class::isInstance)
                .map(HtmlIdOrClassToCssFileReference.class::cast)
                .toList();
        });
    }
}
