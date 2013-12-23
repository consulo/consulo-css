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

package org.consulo.css.lang.formatting;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import org.consulo.css.lang.CssPsiTokens;
import org.consulo.css.lang.CssTokens;
import org.consulo.css.lang.parser.CssParserDefinition;
import org.consulo.css.lang.psi.CssBlock;
import org.consulo.css.lang.psi.CssProperty;
import org.consulo.css.lang.psi.CssRule;
import org.consulo.css.lang.psi.CssSelectorDeclarationList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author VISTALL
 * @since 08.07.13.
 */
public class CssFormattingBlock extends AbstractBlock {

	public CssFormattingBlock(@NotNull ASTNode node, @Nullable Wrap wrap, @Nullable Alignment alignment) {
		super(node, wrap, alignment);
	}

	@Override
	protected List<Block> buildChildren() {
		IElementType elementType = getNode().getElementType();
		List<Block> blocks = new ArrayList<Block>();
		if (elementType == CssParserDefinition.FILE_ELEMENT) {
			ASTNode child = getNode().getFirstChildNode();
			while (child != null) {
				if(child.getElementType() == CssPsiTokens.RULE || child.getElementType() == CssTokens.BLOCK_COMMENT) {
					blocks.add(new CssFormattingBlock(child, null, null));
				}
				child = child.getTreeNext();
			}
		} else if (elementType == CssPsiTokens.RULE) {
			CssRule psi = getNode().getPsi(CssRule.class);
			assert psi != null;
			CssSelectorDeclarationList selectorReferenceList = psi.getSelectorDeclarationList();
			if (selectorReferenceList != null) {
				blocks.add(new CssFormattingBlock(selectorReferenceList.getNode(), null, null));
			}

			CssBlock block = psi.getBlock();
			if (block != null) {
				blocks.add(new CssFormattingBlock(block.getNode(), null, null));
			}
		} else if (elementType == CssPsiTokens.BLOCK) {
			CssBlock psi = getNode().getPsi(CssBlock.class);
			ASTNode temp = getNode().findChildByType(CssTokens.LBRACE);
			if (temp != null) {
				blocks.add(new CssFormattingBlock(temp, null, null));
			}

			for (CssProperty cssProperty : psi.getProperties()) {
				blocks.add(new CssFormattingBlock(cssProperty.getNode(), null, null));
			}

			temp = getNode().findChildByType(CssTokens.RBRACE);
			if (temp != null) {
				blocks.add(new CssFormattingBlock(temp, null, null));
			}
		}
		return blocks;
	}

	@Nullable
	@Override
	public Spacing getSpacing(@Nullable Block block, @NotNull Block block2) {
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
		}*/ else if (elementType == CssPsiTokens.PROPERTY) {
			return Indent.getNormalIndent();
		}
		else if (elementType == CssTokens.LBRACE || elementType == CssTokens.RBRACE) {
			return Indent.getNoneIndent();
		}
		return Indent.getNoneIndent();
	}

	@Nullable
	@Override
	protected Indent getChildIndent() {
		IElementType elementType = getNode().getElementType();
		if (elementType == CssPsiTokens.BLOCK) {
			return Indent.getNormalIndent();
		}
		else if(elementType == CssParserDefinition.FILE_ELEMENT) {
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