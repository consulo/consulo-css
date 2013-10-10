package org.consulo.css.lang.psi;

import org.consulo.xstylesheet.definition.XStyleSheetProperty;
import org.consulo.xstylesheet.definition.XStyleSheetPropertyValueEntry;
import org.consulo.xstylesheet.definition.XStyleSheetPropertyValuePart;
import org.consulo.xstylesheet.psi.PsiXStyleSheetProperty;
import org.consulo.xstylesheet.psi.PsiXStyleSheetPropertyValuePart;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import com.intellij.util.ArrayUtil;

/**
 * @author VISTALL
 * @since 03.07.13.
 */
public class CssPropertyValuePart extends CssElement implements PsiXStyleSheetPropertyValuePart
{
	public CssPropertyValuePart(@NotNull ASTNode node)
	{
		super(node);
	}

	@Override
	public Object getValue()
	{
		XStyleSheetPropertyValueEntry validEntry = findEntry();
		if(validEntry == null)
		{
			return null;
		}

		for(XStyleSheetPropertyValuePart valuePart : validEntry.getParts())
		{
			Object o = valuePart.getNativeValue(this);
			if(o != null)
			{
				return o;
			}
		}
		return null;
	}

	@Override
	public XStyleSheetPropertyValuePart getValuePart()
	{
		XStyleSheetPropertyValueEntry validEntry = findEntry();
		if(validEntry == null)
		{
			return null;
		}

		for(XStyleSheetPropertyValuePart valuePart : validEntry.getParts())
		{
			Object o = valuePart.getNativeValue(this);
			if(o != null)
			{
				return valuePart;
			}
		}
		return null;
	}

	@Override
	public XStyleSheetPropertyValuePart[] getValueParts()
	{
		XStyleSheetPropertyValueEntry validEntry = findEntry();
		if(validEntry == null)
		{
			return XStyleSheetPropertyValuePart.EMPTY_ARRAY;
		}
		return validEntry.getParts();
	}

	private XStyleSheetPropertyValueEntry findEntry()
	{
		PsiXStyleSheetProperty parent = (PsiXStyleSheetProperty) getParent();

		XStyleSheetProperty xStyleSheetProperty = parent.getXStyleSheetProperty();
		if(xStyleSheetProperty == null)
		{
			return null;
		}

		XStyleSheetPropertyValueEntry[] validEntries = xStyleSheetProperty.getValidEntries();

		PsiXStyleSheetPropertyValuePart[] parts = parent.getParts();
		if(parts.length == 0 || validEntries.length == 0)
		{
			return null;
		}

		int i = ArrayUtil.indexOf(parts, this);

		return i >= 0 && i < validEntries.length ? validEntries[i] : null;
	}
}
