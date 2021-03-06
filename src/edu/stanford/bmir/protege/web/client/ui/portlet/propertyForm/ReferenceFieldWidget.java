package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.HTMLPanel;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.ChAOServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.NotesData;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectConfiguration;
import edu.stanford.bmir.protege.web.client.ui.ontology.notes.NoteInputPanel;
import edu.stanford.bmir.protege.web.client.ui.ontology.search.BioPortalConstants;
import edu.stanford.bmir.protege.web.client.ui.ontology.search.BioPortalSearchComponent;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

public class ReferenceFieldWidget extends MultilevelInstanceGridWidget {

    private BioPortalSearchComponent bpSearchComponent = null;
    protected Map<String, Object> bpSearchProperties;

    public ReferenceFieldWidget(Project project) {
        super(project);
    }

    @Override
    public void setup(Map<String, Object> widgetConfiguration, PropertyEntityData propertyEntityData) {
        super.setup(widgetConfiguration, propertyEntityData);

        //since we combine 2 property maps we can't directly reuse them but have to make a copy of their content
        //(otherwise the merging operation would alter the first (i.e. the global) property map)
        bpSearchProperties = new HashMap<String, Object>();

        //read global properties
        Map<String, Object> projectConfiguration = getProject().getProjectConfiguration().getProperties();
        if (projectConfiguration != null) {
            Map<String, Object> globalBpSearchProperties = (Map<String, Object>) projectConfiguration.get(FormConstants.BP_SEARCH_PROPERTIES);
            if(globalBpSearchProperties != null) {
                bpSearchProperties.putAll(globalBpSearchProperties);
            }
        }

        //read local properties
        Map<String, Object> widgetConfig = getWidgetConfiguration();
        Map<String, Object> localBpSearchProperties = (Map<String, Object>) widgetConfig.get(FormConstants.BP_SEARCH_PROPERTIES);
        if (localBpSearchProperties != null) {
            bpSearchProperties.putAll(localBpSearchProperties);
        }
    }

    @Override
    protected Anchor createReplaceNewValueHyperlink() {
        final Map<String, Object> widgetConfiguration = getWidgetConfiguration();
        final ProjectConfiguration projectConfiguration = getProject().getProjectConfiguration();
        Anchor addNewLink = new Anchor(
                InstanceGridWidgetConstants.getIconLink(
                        InstanceGridWidgetConstants.getReplaceNewValueActionDesc(widgetConfiguration, projectConfiguration, "Replace term"),
                        InstanceGridWidgetConstants.getReplaceIcon(widgetConfiguration, projectConfiguration)), true);
        addNewLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (isWriteOperationAllowed()) {
                    onCreateNewReference();
                }
            }
        });
        return addNewLink;
    }

    @Override
    protected Anchor createReplaceExistingHyperlink() {
        final Map<String, Object> widgetConfiguration = getWidgetConfiguration();
        final ProjectConfiguration projectConfiguration = getProject().getProjectConfiguration();
        Anchor addNewLink = new Anchor(
                InstanceGridWidgetConstants.getIconLink(
                        InstanceGridWidgetConstants.getReplaceExistingValueActionDesc(widgetConfiguration, projectConfiguration, "Find & Replace <br/>term"),
                        InstanceGridWidgetConstants.getReplaceIcon(widgetConfiguration, projectConfiguration)), true);
        addNewLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (isWriteOperationAllowed()){
                    //TODO fix this
                    //onAddNewReference((String) ReferenceFieldWidget.this.getWidgetConfiguration().get(FormConstants.LABEL));
                    onAddNewReference( true);
                }
            }
        });

        return addNewLink;
    }

    public String getReferenceProperty() {
        return (String) bpSearchProperties.get(BioPortalConstants.CONFIG_PROPERTY_REFERENCE_PROPERTY);
    }

    @Override
    protected GridEditor createGridEditor(final String fieldType, final Map<String, Object> config) {
        return null;
    }

    @Override
    public String preRenderColumnContent(String content, String fieldType, String emptyText) {
        if (fieldType != null && fieldType.equals(FormConstants.FIELD_TYPE_NO_LINK)) {
            content = getContentOrEmptyText(content, emptyText);
        } else {
            if (content.startsWith("http://")) {
                content = "<a href= \"" + content + "\" target=\"_blank\">" + content + "</a>";
            }
            else {
            	content = getContentOrEmptyText(content, emptyText);
            }
        }
        return content;
    }

    @Override
    protected Anchor createAddExistingHyperlink() {
        final Map<String, Object> widgetConfiguration = getWidgetConfiguration();
        final ProjectConfiguration projectConfiguration = getProject().getProjectConfiguration();
        Anchor addNewLink = new Anchor(
                InstanceGridWidgetConstants.getIconLink(
                        InstanceGridWidgetConstants.getAddExistingValueActionDesc(widgetConfiguration, projectConfiguration, "Find term"),
                        InstanceGridWidgetConstants.getAddIcon(widgetConfiguration, projectConfiguration)), true);
        addNewLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                //TODO fix this
                //onAddNewReference((String) ReferenceFieldWidget.this.getWidgetConfiguration().get(FormConstants.LABEL));
                onAddNewReference(false);
            }
        });

        return addNewLink;
    }

    @Override
    public Anchor createAddNewValueHyperlink() {
        final Map<String, Object> widgetConfiguration = getWidgetConfiguration();
        final ProjectConfiguration projectConfiguration = getProject().getProjectConfiguration();
        Anchor addNewLink = new Anchor(
                InstanceGridWidgetConstants.getIconLink(
                        InstanceGridWidgetConstants.getAddNewValueActionDesc(widgetConfiguration, projectConfiguration, "Add term"),
                        InstanceGridWidgetConstants.getAddIcon(widgetConfiguration, projectConfiguration)), true);
        addNewLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (isWriteOperationAllowed()) {
                    onCreateNewReference();
                }
            }
        });

        return addNewLink;
    }

    private void onAddNewReference(boolean replaceExisting) {
        final Window window = new Window();

        window.setTitle("BioPortal Search  for property " + UIUtil.getDisplayText(getProperty()));
        window.setWidth(800);
        window.setHeight(400);
        window.setLayout(new FitLayout());

        bpSearchComponent = new BioPortalSearchComponent(getProject(), !isMultiValue()) {
            @Override
            protected AbstractAsyncHandler<EntityData> getImportBioPortalConceptHandler() {
                return new ImportBioPortalConceptHandler(this);
            }
        };

        bpSearchComponent.setProperty(getProperty());

        Store store = getStore();
        if (store.getRecords() != null && store.getRecords().length > 0){
            bpSearchComponent.setReplaceExisting(replaceExisting);
            bpSearchComponent.setCurrentValue(store.getAt(0).getAsString(INSTANCE_FIELD_NAME));
        }

        window.add(bpSearchComponent);
        window.show();

        bpSearchComponent.setConfigProperties(bpSearchProperties);
        bpSearchComponent.setEntity(getSubject());
    }

    private void onCreateNewReference() {
        final Window window = new Window();
        window.setTitle("Create new manual reference");
        window.setMinWidth(560);
        window.setWidth(560);
        window.setMinHeight(505);
        window.setHeight(505);

        Panel panel = new Panel();
        panel.setWidth(548);
        panel.setHeight(465);

        Panel wrappingPanel = new Panel();
        wrappingPanel.setHeight(270);
        wrappingPanel.setLayout(new FitLayout());

        final FormPanel formPanel = new FormPanel();
        formPanel.setPaddings(7);

        final TextField txtLabel = new TextField("Label");
        final TextField txtCode = new TextField("Code");
        final TextField txtSource = new TextField("Terminology");
        final TextField txtURL = new TextField("URL");
        HTMLPanel verticalSpacer = new HTMLPanel("<BR /><BR />");
        final NoteInputPanel nip = new NoteInputPanel(getProject(), "Enter a comment about this reference (optional):",
                false, "", "", null, window);
        nip.showButtons(false);

        Button btnCreate = new Button("Create reference", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                String label = txtLabel.getText();
                String code = txtCode.getText();
                String source = txtSource.getText();
                String url = txtURL.getText();
                if (label.length() > 0 || code.length() > 0 || source.length() > 0 || url.length() > 0) {
                    createNewReference(label, code, source, url, nip);
                }
                window.close();
            }
        });
        Button btnCancel = new Button("Cancel", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                window.close();
            }
        });

        formPanel.add(txtLabel, new AnchorLayoutData("100%"));
        formPanel.add(txtCode, new AnchorLayoutData("100%"));
        formPanel.add(txtSource, new AnchorLayoutData("100%"));
        formPanel.add(txtURL, new AnchorLayoutData("100%"));
        formPanel.add(verticalSpacer);

        wrappingPanel.add(nip, new AnchorLayoutData("100% 170"));

        FormPanel formPanelBottom = new FormPanel();
        TextField dummyField = new TextField(); //we need to add this to the bottom formPanel
        //because otherwise we are not allowed to add buttons to it
        dummyField.setVisible(false);
        formPanelBottom.add(dummyField);
        formPanelBottom.addButton(btnCreate);
        formPanelBottom.addButton(btnCancel);

        panel.add(formPanel, new AnchorLayoutData("100% 150"));
        panel.add(wrappingPanel, new AnchorLayoutData("100% 170"));
        panel.add(formPanelBottom, new AnchorLayoutData("100% 20"));
        window.add(panel);
        window.show();
    }

    private void createNewReference(String label, String termId, String ontologyId, String url,
            final NoteInputPanel noteInputPanel) {
        bpSearchComponent = new BioPortalSearchComponent(getProject(), !isMultiValue()) {
            @Override
            protected AbstractAsyncHandler<EntityData> getCreateManualreferenceHandler() {
                return new CreateManualReferenceHandler(noteInputPanel);
            }
        };
        bpSearchComponent.setConfigProperties(bpSearchProperties);
        bpSearchComponent.setEntity(getSubject(), false);//do not reload because bpSearchProperties is not visible
        if (!isReplace()){
            bpSearchComponent.createReference(ontologyId, termId, termId, label, url);
        } else {
            bpSearchComponent.replaceReference(ontologyId, termId, termId, label, url, getStore().getAt(0).getAsString(INSTANCE_FIELD_NAME));
        }
    }

    public void addUserCommentOnReference(final Project project, EntityData refInstance) {

        final Window window = new Window();
        window.setTitle("Comment on reference");
        window.setWidth(600);
        window.setHeight(350);
        window.setMinWidth(300);
        window.setMinHeight(250);
        window.setLayout(new FitLayout());
        window.setPaddings(5);
        window.setButtonAlign(Position.CENTER);

        //window.setCloseAction(Window.HIDE);
        window.setPlain(true);

        final NoteInputPanel nip = new NoteInputPanel(project, "Enter a comment about this reference (optional):",
                false, refInstance, window, new AsyncCallback<NotesData>() {
            public void onFailure(Throwable caught) {
                if (caught != null) {
                    MessageBox.alert(caught.getMessage());
                }
                window.close();
            }

            public void onSuccess(NotesData note) {
                addUserComment(project, note);
                window.close();
            }
        });
        window.add(nip);

        window.show();
        nip.getMainComponentForFocus().focus();
    }

    public static void addUserComment(final Project project, NotesData note) {
        if (note != null
                && ((note.getSubject() != null && note.getSubject().length() > 0) || (note.getBody() != null && note
                        .getBody().length() > 0))) {
            ChAOServiceManager.getInstance().createNote(project.getProjectName(), note, false,
                    new AbstractAsyncHandler<NotesData>() {
                @Override
                public void handleFailure(Throwable caught) {
                    MessageBox.alert("Creating note failed: " + caught.getMessage());
                }

                @Override
                public void handleSuccess(NotesData result) {
                    GWT.log("Note added successfully: " + result.toString(), null);
                }
            });
        }
    }

    /*
     * Remote calls
     */

    class ImportBioPortalConceptHandler extends AbstractAsyncHandler<EntityData> {
        private BioPortalSearchComponent bpSearchComponent;

        public ImportBioPortalConceptHandler(BioPortalSearchComponent bioPortalSearchComponent) {
            this.bpSearchComponent = bioPortalSearchComponent;
        }

        @Override
        public void handleFailure(Throwable caught) {
            bpSearchComponent.getEl().unmask();
            GWT.log("Could not import BioPortal concept ", null);
            MessageBox.alert("Import operation failed!");
        }

        @Override
        public void handleSuccess(EntityData refInstance) {
            bpSearchComponent.getEl().unmask();
            if (refInstance != null) {
                //activate this code if we need it in the future
                //addUserCommentOnReference(getProject(), refInstance);
                refresh();
            } else {
                MessageBox.alert("Import operation DID NOT SUCCED!");
            }
        }
    }


    class CreateManualReferenceHandler extends AbstractAsyncHandler<EntityData> {
        private NoteInputPanel noteInputPanel;

        public CreateManualReferenceHandler(NoteInputPanel noteInputPanel) {
            this.noteInputPanel = noteInputPanel;
        }

        @Override
        public void handleFailure(Throwable caught) {
            GWT.log("Could not create manual reference for " + getSubject(), null);
            MessageBox.alert("Reference creation failed!");
        }

        @Override
        public void handleSuccess(EntityData refInstance) {
            refresh();
            if (refInstance != null) {
                //addUserCommentOnReference(getProject(), refInstance);
                noteInputPanel.setAnnotatedEntity(refInstance);
                noteInputPanel.doSendNote();
            } else {
                MessageBox.alert("Reference creation DID NOT SUCCEDED!");
            }
        }
    }

}
