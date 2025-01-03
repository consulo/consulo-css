/*
 * Copyright 2000-2013 JetBrains s.r.o.
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
package com.intellij.application.options.emmet.css;

import consulo.css.emmet.localize.CssEmmetLocalize;
import consulo.ui.ex.awt.*;
import consulo.ui.ex.awt.speedSearch.TableViewSpeedSearch;
import consulo.ui.ex.awt.table.ListTableModel;
import consulo.ui.ex.awt.table.TableView;
import consulo.ui.ex.awt.util.TableUtil;

import jakarta.annotation.Nullable;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zolotov
 * @since 2013-02-20
 */
public class CssEditPrefixesListPanel {
    private final ListTableModel<CssPrefixInfo> myPrefixesModel;
    private final TableView<CssPrefixInfo> myPrefixesTableView;

    public CssEditPrefixesListPanel() {
        ColumnInfo[] columns = new ColumnInfo[CssPrefix.values().length + 1];
        columns[0] = new PropertyColumn();
        CssPrefix[] values = CssPrefix.values();
        for (int i = 0; i < values.length; i++) {
            columns[i + 1] = new PrefixColumnInfo(values[i]);
        }

        myPrefixesModel = new ListTableModel<>(columns, new ArrayList<CssPrefixInfo>(), 0);
        myPrefixesModel.setSortable(true);
        myPrefixesTableView = new TableView<>(myPrefixesModel);
        myPrefixesTableView.setAutoCreateRowSorter(true);
        myPrefixesTableView.setBorder(null);
        myPrefixesTableView.setStriped(true);
        myPrefixesTableView.getRowSorter().toggleSortOrder(0);

        TableViewSpeedSearch.register(myPrefixesTableView, CssPrefixInfo::getPropertyName);
    }

    public void setEnabled(boolean value) {
        myPrefixesTableView.setEnabled(value);
    }

    public void setState(Set<CssPrefixInfo> prefixInfos) {
        myPrefixesModel.setItems(new ArrayList<>(prefixInfos));
    }

    public Set<CssPrefixInfo> getState() {
        return new HashSet<>(myPrefixesModel.getItems());
    }

    public JPanel createMainComponent() {
        final ToolbarDecorator decorator = ToolbarDecorator.createDecorator(myPrefixesTableView);
        decorator.disableUpDownActions();
        final JPanel panel = decorator.createPanel();
        decorator.setAddAction(e -> {
            TableUtil.stopEditing(myPrefixesTableView);
            String propertyName = Messages.showInputDialog(
                myPrefixesTableView,
                CssEmmetLocalize.editPrefixesPanelNewPropertyMessage().get(),
                CssEmmetLocalize.editPrefixesPanelNewPropertyText().get(),
                null
            );
            if (propertyName != null && !propertyName.isEmpty()) {
                List<CssPrefixInfo> items = myPrefixesModel.getItems();
                for (CssPrefixInfo state : items) {
                    if (propertyName.equals(state.getPropertyName())) {
                        myPrefixesTableView.clearSelection();
                        myPrefixesTableView.addSelection(state);
                        scrollToSelection();
                        return;
                    }
                }
                CssPrefixInfo newPrefixInfo = new CssPrefixInfo(propertyName);
                myPrefixesModel.addRow(newPrefixInfo);
                myPrefixesTableView.clearSelection();
                myPrefixesTableView.addSelection(newPrefixInfo);
                scrollToSelection();
            }
            myPrefixesTableView.requestFocus();
        }).setRemoveAction(e -> {
            TableUtil.stopEditing(myPrefixesTableView);
            CssPrefixInfo selectedObject = myPrefixesTableView.getSelectedObject();
            int selectedRow = myPrefixesTableView.getSelectedRow();
            int index = myPrefixesModel.indexOf(selectedObject);
            if (0 <= index && index < myPrefixesModel.getRowCount()) {
                myPrefixesModel.removeRow(index);
                if (selectedRow < myPrefixesTableView.getRowCount()) {
                    myPrefixesTableView.setRowSelectionInterval(selectedRow, selectedRow);
                }
                else {
                    if (selectedRow > 0) {
                        myPrefixesTableView.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
                    }
                }
            }
            myPrefixesTableView.getParent().repaint();
            myPrefixesTableView.requestFocus();
        });
        return panel;
    }

    private void scrollToSelection() {
        int selectedRow = myPrefixesTableView.getSelectedRow();
        if (selectedRow >= 0) {
            myPrefixesTableView.scrollRectToVisible(myPrefixesTableView.getCellRect(selectedRow, 0, true));
        }
    }

    private static class PropertyColumn extends ColumnInfo<CssPrefixInfo, String> {
        public PropertyColumn() {
            super("Property");
        }

        @Nullable
        @Override
        public String valueOf(CssPrefixInfo prefixInfo) {
            return prefixInfo.getPropertyName();
        }
    }

    private class PrefixColumnInfo extends ColumnInfo<CssPrefixInfo, Boolean> {
        private final CssPrefix myPrefix;
        private final BooleanTableCellRenderer myCellRenderer = new BooleanTableCellRenderer();

        @Nullable
        @Override
        public TableCellRenderer getRenderer(CssPrefixInfo state) {
            return myCellRenderer;
        }

        public PrefixColumnInfo(CssPrefix prefix) {
            super(prefix.getText());
            myPrefix = prefix;
        }

        @Nullable
        @Override
        public TableCellEditor getEditor(CssPrefixInfo prefixInfo) {
            JBCheckBox box = new JBCheckBox();
            box.setHorizontalAlignment(SwingConstants.CENTER);
            return new DefaultCellEditor(box);
        }

        @Override
        public boolean isCellEditable(CssPrefixInfo prefixInfo) {
            return myPrefixesTableView.isEnabled();
        }

        @Override
        public final Class<Boolean> getColumnClass() {
            return Boolean.class;
        }

        @Override
        public final void setValue(final CssPrefixInfo prefixInfo, final Boolean aValue) {
            prefixInfo.setValue(myPrefix, aValue);
        }

        @Nullable
        @Override
        public final Boolean valueOf(CssPrefixInfo state) {
            return state.getValue(myPrefix);
        }
    }
}
