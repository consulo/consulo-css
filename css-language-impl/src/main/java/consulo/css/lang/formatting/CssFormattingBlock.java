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

package consulo.css.lang.formatting;

import consulo.css.lang.CssElements;
import consulo.css.lang.CssTokens;
import consulo.css.lang.parser.CssParserDefinition;
import consulo.language.ast.ASTNode;
import consulo.xstylesheet.psi.PsiXStyleSheetBlock;
import consulo.language.ast.IElementType;
import consulo.language.codeStyle.*;

import org.jspecify.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 2013-07-08
 */
public class CssFormattingBlock extends AbstractBlock {
    public CssFormattingBlock(ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment) {
        super(node, wrap, alignment);
    }

    @Override
    protected List<Block> buildChildren() {
        List<Block> blocks = new ArrayList<>();
        ASTNode child = getNode().getFirstChildNode();
        while (child != null) {
            if (!FormatterUtil.containsWhiteSpacesOnly(child)) {
                blocks.add(createChildBlock(child));
            }
            child = child.getTreeNext();
        }
        return blocks;
    }

    /**
     * Dialects return their own block subclass here.
     */
    protected Block createChildBlock(ASTNode child) {
        return new CssFormattingBlock(child, null, null);
    }

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable Block block, Block block2) {
        return null;
    }

    @Override
    public Indent getIndent() {
        IElementType elementType = getNode().getElementType();
        if (elementType == CssParserDefinition.FILE_ELEMENT) {
            return Indent.getAbsoluteNoneIndent();
        }/* else if (elementType == CssPsiTokens.RULE || elementType == CssPsiTokens.BLOCK || elementType == CssPsiTokens.SELECTOR_DECLARATION_LIST) {
            return Indent.getNoneIndent();
		} else if (elementType == CssTokens.LBRACE || elementType == CssTokens.RBRACE) {
			return Indent.getNoneIndent();
		}*/
        else if (elementType == CssElements.PROPERTY) {
            return Indent.getNormalIndent();
        }
        else if (elementType == CssTokens.LBRACE || elementType == CssTokens.RBRACE) {
            return Indent.getNoneIndent();
        }

        // everything inside a block: declarations, nested rules, nested at-rules (CSS Nesting), comments
        ASTNode parent = getNode().getTreeParent();
        if (parent != null && parent.getPsi() instanceof PsiXStyleSheetBlock) {
            return Indent.getNormalIndent();
        }
        return Indent.getNoneIndent();
    }

    @Nullable
    @Override
    protected Indent getChildIndent() {
        IElementType elementType = getNode().getElementType();
        if (elementType == CssElements.BLOCK || getNode().getPsi() instanceof PsiXStyleSheetBlock) {
            return Indent.getNormalIndent();
        }
        else if (elementType == CssElements.ROOT || elementType == CssParserDefinition.FILE_ELEMENT) {
            return Indent.getAbsoluteNoneIndent();
        }
        return null;
    }

    @Override
    public boolean isLeaf() {
        IElementType elementType = getNode().getElementType();
        return elementType == CssTokens.LBRACE || elementType == CssTokens.RBRACE;
    }
}