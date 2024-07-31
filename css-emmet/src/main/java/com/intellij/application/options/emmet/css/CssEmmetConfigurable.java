package com.intellij.application.options.emmet.css;

import com.intellij.codeInsight.template.emmet.options.EmmetOptions;
import com.intellij.xml.XmlBundle;
import consulo.annotation.component.ExtensionImpl;
import consulo.configurable.ApplicationConfigurable;
import consulo.configurable.Configurable;
import consulo.configurable.ConfigurationException;
import consulo.disposer.Disposable;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.awt.JBCheckBox;
import consulo.ui.ex.awt.VerticalFlowLayout;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.*;

/**
 * @author VISTALL
 * @since 2013-08-23
 */
@ExtensionImpl
public class CssEmmetConfigurable implements ApplicationConfigurable, Configurable.NoScroll {
    private JBCheckBox myAutoInsertCssVendorJBCheckBox;
    private JPanel myPrefixesPanel;
    private JBCheckBox myEnabledFuzzySearchJBCheckBox;
    private JPanel myPanel;

    private CssEditPrefixesListPanel myCssEditPrefixesListPanel;

    @Nonnull
    @Override
    public String getId() {
        return "editor.emmet.css";
    }

    @Nullable
    @Override
    public String getParentId() {
        return "editor.emmet";
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "CSS";
    }

    @RequiredUIAccess
    @Nullable
    @Override
    public JComponent createComponent(@Nonnull Disposable uiDisposable) {
        if (myPanel == null) {
            myPanel = new JPanel(new BorderLayout());

            JPanel topPanel = new JPanel(new VerticalFlowLayout());

            myEnabledFuzzySearchJBCheckBox = new JBCheckBox(XmlBundle.message("emmet.fuzzy.search"));
            topPanel.add(myEnabledFuzzySearchJBCheckBox);
            myAutoInsertCssVendorJBCheckBox = new JBCheckBox(XmlBundle.message("emmet.auto.insert.vendor.prefixes"));
            topPanel.add(myAutoInsertCssVendorJBCheckBox);

            myPanel.add(topPanel, BorderLayout.NORTH);

            myCssEditPrefixesListPanel = new CssEditPrefixesListPanel();
            myPrefixesPanel = myCssEditPrefixesListPanel.createMainComponent();
            myPrefixesPanel.setEnabled(true);

            myPanel.add(myPrefixesPanel, BorderLayout.CENTER);

            myAutoInsertCssVendorJBCheckBox.addActionListener(
                e -> myCssEditPrefixesListPanel.setEnabled(myAutoInsertCssVendorJBCheckBox.isSelected())
            );
        }
        return myPanel;
    }

    @RequiredUIAccess
    @Override
    public boolean isModified() {
        CssEmmetOptions emmetOptions = CssEmmetOptions.getInstance();

        return emmetOptions.isAutoInsertCssPrefixedEnabled() != myAutoInsertCssVendorJBCheckBox.isSelected() ||
            emmetOptions.isFuzzySearchEnabled() != myEnabledFuzzySearchJBCheckBox.isSelected() ||
            !emmetOptions.getAllPrefixInfo().equals(myCssEditPrefixesListPanel.getState());
    }

    @RequiredUIAccess
    @Override
    public void apply() throws ConfigurationException {
        CssEmmetOptions emmetOptions = CssEmmetOptions.getInstance();

        emmetOptions.setAutoInsertCssPrefixedEnabled(myAutoInsertCssVendorJBCheckBox.isSelected());
        emmetOptions.setFuzzySearchEnabled(myEnabledFuzzySearchJBCheckBox.isSelected());
        emmetOptions.setPrefixInfo(myCssEditPrefixesListPanel.getState());
    }

    @RequiredUIAccess
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

    @RequiredUIAccess
    @Override
    public void disposeUIResources() {
        myPanel = null;
        myCssEditPrefixesListPanel = null;
        myPrefixesPanel = null;
    }
}
