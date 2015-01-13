package edu.stanford.bmir.protege.web.client.ui.ontology.classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
import com.gwtext.client.data.*;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.EditorGridListener;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridRowListener;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.model.PropertyValueUtil;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.client.ui.ontology.properties.PropertiesTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.selection.Selectable;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

/**
 * A grid that shows all the properties of an entity. Can be used with classes,
 * properties, individuals. Works both for OWL and Frames ontologies.
 *
 * In current implementation supports basic editing of string values by
 * double-clicking on a cell.
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 *
 */
public class AllPropertiesGrid extends EditorGridPanel {

    private Project project;
    private EntityData _currentEntity;
    private RecordDef recordDef;
    private GroupingStore store;
    private GridRowListener gridRowListener;
    private EditorGridListener editorGridListener;
    private Collection<EntityData> currentSelection;
    private PropertyValueUtil propertyValueUtil;

    public AllPropertiesGrid(Project project) {
        this.project = project;
        this.propertyValueUtil = new PropertyValueUtil();

        createGrid();

        addGridRowListener(getRowListener());
        addEditorGridListener(getEditorGridListener());
    }

    public void setEntity(EntityData newEntity) {
        if (_currentEntity != null && _currentEntity.equals(newEntity)) {
            return;
        }
        _currentEntity = newEntity;
        refresh();
    }

    public void refresh() {
        store.removeAll();
        // store.clearGrouping();
        if (_currentEntity == null) {
            return;
        }
        reload();
    }

    protected GridRowListener getRowListener() {
        if (gridRowListener == null) {
            gridRowListener = new GridRowListenerAdapter() {
                @Override
                public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
                    String property = store.getAt(rowIndex).getAsString("property");
                    currentSelection = new ArrayList<EntityData>();
                    currentSelection.add(new EntityData(property));

                    super.onRowClick(grid, rowIndex, e);
                }
            };
        }
        return gridRowListener;
    }

    protected EditorGridListener getEditorGridListener() {
        if (editorGridListener == null) {
            editorGridListener = new EditorGridListenerAdapter() {
                @Override
                public boolean doBeforeEdit(GridPanel grid, Record record, String field, Object value, int rowIndex,
                        int colIndex) {
                    String valueType = record.getAsString("valueType");
                    if (!project.hasWritePermission(GlobalSettings.getGlobalSettings().getUserName())) {
                        return false;
                    } // TODO: allow only editing of string values for now
                    return valueType == null || valueType.equalsIgnoreCase("string")
                            || valueType.equalsIgnoreCase("any");
                }

                @Override
                public void onAfterEdit(GridPanel grid, Record record, String field, Object newValue, Object oldValue,
                        int rowIndex, int colIndex) {
                    // special handling rdfs:Literal
//                    ValueType valueType = (ValueType) record.getAsObject("valueType");
                    ValueType valueType = ValueType.valueOf((String)record.getAsObject("valueType"));
                    String lang = record.getAsString("language");
                    if (lang != null && lang.length() > 0) {
                        newValue = "~#" + lang + " " + newValue.toString();
                        oldValue = "~#" + lang + " " + oldValue.toString();
                        valueType = ValueType.Literal;
                    }
                    final PropertyType propertyType = (PropertyType) record.getAsObject("propertyType");

                    replacePropertyValue(_currentEntity.getName(), record.getAsString("propertyName"), valueType,
                            oldValue.toString(), newValue.toString(), null, propertyType); //TODO: how to handle operationDescription
                }
            };
        }
        return editorGridListener;
    }

    // TODO: assume value value type is the same as the property value type, fix
    // later
    protected void replacePropertyValue(String entityName, String propName, ValueType propValueType, String oldValue,
                                        String newValue, String operationDescription, PropertyType propertyValue) {
        propertyValueUtil.replacePropertyValue(project.getProjectName(), entityName, propName, propValueType, propertyValue, oldValue,
                newValue, GlobalSettings.getGlobalSettings().getUserName(), operationDescription,
                new ReplacePropertyValueHandler());
    }

    protected void deletePropertyValue(String entityName, String propName, ValueType propValueType, PropertyType propertyType, String value,
                                       String operationDescription) {
        propertyValueUtil.deletePropertyValue(project.getProjectName(), entityName, propName, propValueType, propertyType, value,
                GlobalSettings.getGlobalSettings().getUserName(), operationDescription,
                new RemovePropertyValueHandler());

    }

    protected void createGrid() {
        createColumns();

        recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("propertyName"), new StringFieldDef("property"),
                new StringFieldDef("value"), new StringFieldDef("language"), new StringFieldDef("valueType"), new ObjectFieldDef("propertyType")  });

        ArrayReader reader = new ArrayReader(recordDef);
        MemoryProxy dataProxy = new MemoryProxy(new Object[][] {});
        store = new GroupingStore(dataProxy, reader);
        store.setSortInfo(new SortState("property", SortDir.ASC));
        setStore(store);


        createButton = new ToolbarButton("Add property value");
        createButton.setCls("toolbar-button");
        createButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                onAddPropertyValue();
            }
        });
        if (!project.hasWritePermission(GlobalSettings.getGlobalSettings().getUserName())) {
            createButton.disable();
        }

        deleteButton = new ToolbarButton("Delete property value");
        deleteButton.setCls("toolbar-button");
        if (!project.hasWritePermission(GlobalSettings.getGlobalSettings().getUserName())) {
            deleteButton.disable();
        }
        deleteButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                int selectedRow = getCellSelectionModel().getSelectedCell()[0];
                if (selectedRow < 0) {
                    return;
                }
                onDelete(store.getAt(selectedRow));
            }
        });
        setTopToolbar(new Button[] { createButton, deleteButton });


        /*
         * GroupingView gridView = new GroupingView();
         * gridView.setForceFit(true);gridView.setGroupTextTpl(
         * "{text} ({[values.rs.length]} {[values.rs.length > 1 ? \"Values\" : \"Value\"]})"
         * );
         */

        GridView gridView = new GridView();
        gridView.setAutoFill(true);
        //gridView.setScrollOffset(0);

        setHeight(200);
        setAutoWidth(true);
        setLoadMask("Loading properties");

        setStripeRows(true);
        setAutoExpandColumn("value");
        setAnimCollapse(true);
        setClicksToEdit(2);
        setView(gridView);

        store.load();

    }

    protected void createColumns() {
        ColumnConfig propCol = new ColumnConfig();
        propCol.setHeader("Property");
        propCol.setId("property");
        propCol.setDataIndex("property");
        propCol.setResizable(true);
        propCol.setSortable(true);
        //propCol.setWidth(180);
        // propCol.setHidden(true);

        ColumnConfig valueCol = new ColumnConfig();
        valueCol.setHeader("Value");
        valueCol.setId("value");
        valueCol.setDataIndex("value");
        valueCol.setResizable(true);
        valueCol.setSortable(true);
        valueCol.setCss("word-wrap: break-word ;");
        valueCol.setRenderer(renderLast);
        valueCol.setEditor(new GridEditor(new TextField()));

        ColumnConfig languageCol = new ColumnConfig();
        languageCol.setHeader("Lang");
        languageCol.setId("language");
        languageCol.setDataIndex("language");
        languageCol.setResizable(true);
        languageCol.setSortable(true);
        languageCol.setWidth(30);

        ColumnConfig[] columns = new ColumnConfig[] { propCol, valueCol, languageCol };

        ColumnModel columnModel = new ColumnModel(columns);
        setColumnModel(columnModel);

    }

    Renderer renderLast = new Renderer() {
        public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
                Store store) {
            return Format
                    .format(
                            "<style type=\"text/css\">.x-grid3-cell-inner, .x-grid3-hd-inner { white-space:normal !important; }</style> {0}",
                            new String[] { record.getAsString("value") });
        }
    };


    private ToolbarButton createButton;
    private ToolbarButton deleteButton;

    public void updateButtonStates() {
        if (project.hasWritePermission(GlobalSettings.getGlobalSettings().getUserName())) {
            createButton.enable();
            deleteButton.enable();
        } else {
            createButton.disable();
            deleteButton.disable();
        }
    }


    public void reload() {
        store.removeAll();
        OntologyServiceManager.getInstance().getEntityTriples(project.getProjectName(), _currentEntity.getName(),
                new GetTriplesHandler());
    }

    public Collection<EntityData> getSelection() {
        return currentSelection;
    }

    protected void onDelete(Record record) {
        ValueType valueType = ValueType.valueOf(record.getAsString("valueType"));
        PropertyType propertyType = (PropertyType) record.getAsObject("propertyType");
        String lang = record.getAsString("language");
        String value = record.getAsString("value");
        if (lang != null && lang.length() > 0) {
            value = "~#" + lang + " " + value;
            valueType = ValueType.Literal;
        }
        deletePropertyValue(_currentEntity.getName(), record.getAsString("propertyName"), valueType, propertyType, value, null); //TODO: how to handle operationDescription
    }

    protected void onAddPropertyValue() {
        final com.gwtext.client.widgets.Window window = new com.gwtext.client.widgets.Window();
        window.setTitle("Select value");
        window.setClosable(true);
        window.setPaddings(7);
        window.setWidth(250);
        window.setHeight(350);
        window.setLayout(new FitLayout());
        window.setCloseAction(com.gwtext.client.widgets.Window.HIDE);
        window.add(new SelectionDialog(window, createSelectable()));
        window.show();
    }

    public Selectable createSelectable() {
        PropertiesTreePortlet propertiesTreePortlet = new PropertiesTreePortlet(project);
        return propertiesTreePortlet;
    }

    protected void addEmptyPropertyRow(PropertyEntityData propEntityData) {
        Triple triple = new Triple(_currentEntity, propEntityData, new EntityData(null));
        ValueType valueType = triple.getProperty().getValueType();
        Record record = recordDef.createRecord(new Object[] { triple.getProperty().getName(),
                triple.getProperty().getBrowserText(), "", null, valueType == null ? null : valueType.toString() , propEntityData.getPropertyType()});
        GWT.log("After created record", null);
        stopEditing();
        store.insert(0, record);
        startEditing(0, 1);

    }

    /*
     * Remote calls
     */

    class GetTriplesHandler extends AbstractAsyncHandler<List<Triple>> {

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at getting triples for " + _currentEntity, caught);
        }

        @Override
        public void handleSuccess(List<Triple> triples) {
            store.removeAll();
            if (triples == null) {return;}
            for (Triple triple : triples) {
                String str = triple.getValue().getName();

                String lan = triple.getValue().getLanguage();
                if (isLiteralWithLang(str)) {
                    lan = getLang(str);
                    str = getText(str);
                } else {
                    str = UIUtil.getDisplayText(triple.getValue());
                }
                str = UIUtil.replaceEOLWithBR(str);

                Record record = recordDef.createRecord(getRow(triple.getProperty().getName(), triple.getProperty().getBrowserText(), str, lan,
                        triple.getProperty().getValueType().toString(), triple.getProperty().getPropertyType()));
                store.add(record);
            }
            // store.groupBy("property");
        }

        private boolean isLiteralWithLang(String str) {
            int indexOfSpace = str.indexOf(" ");
            return str.indexOf("~#") == 0 && indexOfSpace > 0;
        }

        private String getLang(String str) {
            int indexOfSpace = str.indexOf(" ");
            return str.substring(2, indexOfSpace);
        }

        private String getText(String str) {
            int indexOfSpace = str.indexOf(" ");
            return str.substring(indexOfSpace + 1);
        }

        private Object[] getRow(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
            return new Object[] { o1, o2, o3, o4, o5, o6 };
        }

    }

    class ReplacePropertyValueHandler extends AbstractAsyncHandler<Void> {

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at replace property value for " + _currentEntity, caught);
            Window.alert("There was an error at setting the property value for " + _currentEntity.getBrowserText()
                    + ".<br>Please try again later.");
        }

        @Override
        public void handleSuccess(Void result) {
            GWT.log("Success at setting property value for " + _currentEntity.getBrowserText(), null);
            refresh();
        }
    }

    class RemovePropertyValueHandler extends AbstractAsyncHandler<Void> {

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Error at removing property value for " + _currentEntity, caught);
            Window.alert("There was an error at removing the property value for " + _currentEntity.getBrowserText()
                    + ".<br>Please try again later.");
        }

        @Override
        public void handleSuccess(Void result) {
            GWT.log("Success at removing property value for " + _currentEntity.getBrowserText(), null);
            refresh();
        }

    }

    /*
     * Internal class - to be refactored
     */

    class SelectionDialog extends Panel {
        private com.gwtext.client.widgets.Window parent;
        private Selectable selectable;

        public SelectionDialog(com.gwtext.client.widgets.Window parent, Selectable selectable) {
            super();
            this.parent = parent;
            this.selectable = selectable;

            Button selectButton = new Button("Select", new ButtonListenerAdapter() {
                @Override
                public void onClick(Button button, EventObject e) {
                    Collection<EntityData> selection = SelectionDialog.this.selectable.getSelection();
                    if (selection != null && selection.size() > 0) {
                        EntityData singleSelection = selection.iterator().next();
                        SelectionDialog.this.parent.close();
                        addEmptyPropertyRow((PropertyEntityData) singleSelection);
                    }
                }
            });

            setLayout(new FitLayout());
            add((Widget) selectable);
            addButton(selectButton);
        }
    }

}