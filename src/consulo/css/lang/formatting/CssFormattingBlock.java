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

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import consulo.css.html.psi.CssHtmlTokens;
import consulo.css.lang.CssPsiTokens;
import consulo.css.lang.CssTokens;
import consulo.css.lang.parser.CssParserDefinition;
import consulo.css.lang.psi.CssBlock;
import consulo.css.lang.psi.CssProperty;
import consulo.css.lang.psi.CssRule;
import consulo.css.lang.psi.CssSelectorDeclarationList;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public class CssFormattingBlock extends AbstractBlock
{
	public CssFormattingBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment)
	{
		super(node, wrap, alignment);
	}

	@Override
	protected List<Block> buildChildren()
	{
		IElementType elementType = getNode().getElementType();
		List<Block> blocks = new ArrayList<>();
		if(elementType == CssParserDefinition.FILE_ELEMENT || elementType == CssHtmlTokens.HTML_CSS_ELEMENT)
		{
			ASTNode child = getNode().getFirstChildNode();
			while(child != null)
			{
				if(child.getElementType() == CssPsiTokens.RULE || child.getElementType() == CssTokens.BLOCK_COMMENT)
				{
					blocks.add(new CssFormattingBlock(child, null, null));
				}
				child = child.getTreeNext();
			}
		}
		else if(elementType == CssPsiTokens.RULE)
		{
			CssRule psi = getNode().getPsi(CssRule.class);
			assert psi != null;
			CssSelectorDeclarationList selectorReferenceList = psi.getSelectorDeclarationList();
			if(selectorReferenceList != null)
			{
				blocks.add(new CssFormattingBlock(selectorReferenceList.getNode(), null, null));
			}

			CssBlock block = psi.getBlock();
			if(block != null)
			{
				blocks.add(new CssFormattingBlock(block.getNode(), null, null));
			}
		}
		else if(elementType == CssPsiTokens.BLOCK)
		{
			CssBlock psi = getNode().getPsi(CssBlock.class);
			ASTNode temp = getNode().findChildByType(CssTokens.LBRACE);
			if(temp != null)
			{
				blocks.add(new CssFormattingBlock(temp, null, null));
			}

			for(CssProperty cssProperty : psi.getProperties())
			{
				blocks.add(new CssFormattingBlock(cssProperty.getNode(), null, null));
			}

			temp = getNode().findChildByType(CssTokens.RBRACE);
			if(temp != null)
			{
				blocks.add(new CssFormattingBlock(temp, null, null));
			}
		}
		return blocks;
	}

	@Nullable
	@Override
	public Spacing getSpacing(@Nullable Block block, @NotNull Block block2)
	{
		return null;
	}

	@Override
	public Indent getIndent()
	{
		IElementType elementType = getNode().getElementType();
		if(elementType == CssParserDefinition.FILE_ELEMENT)
		{
			return Indent.getAbsoluteNoneIndent();
		}/* else if (elementType == CssPsiTokens.RULE || elementType == CssPsiTokens.BLOCK || elementType == CssPsiTokens.SELECTOR_DECLARATION_LIST) {
			return Indent.getNoneIndent();
		} else if (elementType == CssTokens.LBRACE || elementType == CssTokens.RBRACE) {
			return Indent.getNoneIndent();
		}*/
		else if(elementType == CssPsiTokens.PROPERTY)
		{
			return Indent.getNormalIndent();
		}
		else if(elementType == CssTokens.LBRACE || elementType == CssTokens.RBRACE)
		{
			return Indent.getNoneIndent();
		}
		return Indent.getNoneIndent();
	}

	@Nullable
	@Override
	protected Indent getChildIndent()
	{
		IElementType elementType = getNode().getElementType();
		if(elementType == CssPsiTokens.BLOCK)
		{
			return Indent.getNormalIndent();
		}
		else if(elementType == CssParserDefinition.FILE_ELEMENT)
		{
			return Indent.getAbsoluteNoneIndent();
		}
		return null;
	}

	@Override
	public boolean isLeaf()
	{
		IElementType elementType = getNode().getElementType();
		return elementType == CssTokens.LBRACE || elementType == CssTokens.RBRACE;
	}
}