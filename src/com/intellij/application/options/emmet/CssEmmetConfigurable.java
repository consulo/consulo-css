package com.intellij.application.options.emmet;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.components.JBCheckBox;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author VISTALL
 * @since 23.08.13.
 */
public class CssEmmetConfigurable implements EmmetOptionsProvider{
	private JBCheckBox myAutoInsertCssVendorJBCheckBox;
	private JPanel myPrefixesPanel;
	private JBCheckBox myEnabledFuzzySearchJBCheckBox;
	private JPanel myPanel;

	private CssEditPrefixesListPanel myCssEditPrefixesListPanel;

	public CssEmmetConfigurable() {
		myAutoInsertCssVendorJBCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				myCssEditPrefixesListPanel.setEnabled(myAutoInsertCssVendorJBCheckBox.isSelected());
			}
		});
	}

	private void createUIComponents() {
		myCssEditPrefixesListPanel = new CssEditPrefixesListPanel();
		myPrefixesPanel = myCssEditPrefixesListPanel.createMainComponent();
		myPrefixesPanel.setEnabled(true);
	}

	@NotNull
	@Override
	public String getId() {
		return "css.emmet.application.settings";
	}

	@Nullable
	@Override
	public Runnable enableSearch(String s) {
		return null;
	}

	@Nls
	@Override
	public String getDisplayName() {
		return "CSS";
	}

	@Nullable
	@Override
	public String getHelpTopic() {
		return null;
	}

	@Nullable
	@Override
	public JComponent createComponent() {
		return myPanel;
	}

	@Override
	public boolean isModified() {
		CssEmmetOptions emmetOptions = CssEmmetOptions.getInstance();

		return emmetOptions.isAutoInsertCssPrefixedEnabled() != myAutoInsertCssVendorJBCheckBox.isSelected() ||
				emmetOptions.isFuzzySearchEnabled() != myEnabledFuzzySearchJBCheckBox.isSelected() ||
				!emmetOptions.getAllPrefixInfo().equals(myCssEditPrefixesListPanel.getState());
	}

	@Override
	public void apply() throws ConfigurationException {
		CssEmmetOptions emmetOptions = CssEmmetOptions.getInstance();

		emmetOptions.setAutoInsertCssPrefixedEnabled(myAutoInsertCssVendorJBCheckBox.isSelected());
		emmetOptions.setFuzzySearchEnabled(myEnabledFuzzySearchJBCheckBox.isSelected());
		emmetOptions.setPrefixInfo(myCssEditPrefixesListPanel.getState());
	}

	@Override
	public void reset() {
		CssEmmetOptions cssEmmetOptions = CssEmmetOptions.getInstance();
		EmmetOptions emmetOptions = EmmetOptions.getInstance();

		myAutoInsertCssVendorJBCheckBox.setEnabled(emmetOptions.isEmmetEnabled());
		myAutoInsertCssVendorJBCheckBox.setSelected(cssEmmetOptions.isAutoInsertCssPrefixedEnabled());
		myEnabledFuzzySearchJBCheckBox.setEnabled(emmetOptions.isEmmetEnabled());
		myEnabledFuzzySearchJBCheckBox.setSelected(cssEmmetOptions.isFuzzySearchEnabled());

		myCssEditPrefixesListPanel.setEnabled(emmetOptions.isEmmetEnabled() && cssEmmetOptions.isAutoInsertCssPrefixedEnabled());
		myCssEditPrefixesListPanel.setState(cssEmmetOptions.getAllPrefixInfo());
	}

	@Override
	public void disposeUIResources() {
		myCssEditPrefixesListPanel = null;
		myPrefixesPanel = null;
	}
}
