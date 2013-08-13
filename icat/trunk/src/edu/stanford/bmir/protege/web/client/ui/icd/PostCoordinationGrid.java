package edu.stanford.bmir.protege.web.client.ui.icd;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Anchor;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

import edu.stanford.bmir.icd.claml.ICDContentModelConstants;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.ICDServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityPropertyValues;
import edu.stanford.bmir.protege.web.client.rpc.data.PropertyEntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm.FormConstants;
import edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm.InstanceGridWidget;
import edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm.WidgetController;
import edu.stanford.bmir.protege.web.client.ui.util.UIConstants;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

/**
 *  ICD specific widget showing the post coordination axes. Makes a specific service call.
 *  Misuses the {@link InstanceGridWidget}, which needs to be configured in a specific way,
 *  otherwise this widget will not work.
 *  
 *  @author csnyulas
 *
 */
public class PostCoordinationGrid extends InstanceGridWidget {

    private static int OFFSET_DELETE_COLUMN = -1;
    private static int OFFSET_COMMENT_COLUMN = 1;
    private static int OFFSET_MAX_COLUMN = OFFSET_COMMENT_COLUMN;

    private WidgetController widgetController;
	
	public PostCoordinationGrid(Project project, WidgetController widgetController) {
		super(project);
		this.widgetController = widgetController;
	}

    @Override
	protected int getClicksToEdit() {
		return 1;
	}
	
    @Override
	protected boolean getEnableHeaderMenu() {
		return UIUtil.getBooleanConfigurationProperty(
                getWidgetConfiguration(), 		//ignore project configuration (unlike the general InstanceGridWidget)
                FormConstants.ENABLE_HEADER_MENU, getEnableHeaderMenuDefault());
	}

    @Override
    protected boolean getEnableHeaderMenuDefault() {
		return false;
	}

    @Override
    protected boolean getIsSortableDefault() {
		return false;
	}

    @Override
    protected Anchor createAddNewValueHyperlink() {
        return null;
    }

    @Override
    protected Anchor createAddExistingHyperlink() {
        return null;
    }

    @Override
    protected GridEditor createGridEditor(final String fieldType, final Map<String, Object> config) {
        return null;
    }
    
    @Override
    protected int getOffsetDeleteColumn() {
        return OFFSET_DELETE_COLUMN;
    }

    @Override
    protected int getOffsetCommentColumn() {
        return OFFSET_COMMENT_COLUMN;
    }

    @Override
    protected int getMaxColumnOffset() {
        return OFFSET_MAX_COLUMN;
    }

    @Override
    protected int getExtraColumnCount() {
        return OFFSET_MAX_COLUMN + 1;   //1 for the instance field
    }

    @Override
    protected void fillValues(List<String> subjects, List<String> props) {
        getStore().removeAll();
        EntityData currentSubject = getSubject();
		ICDServiceManager.getInstance().getEntityPropertyValuesForPostCoordinationAxes(getProject().getProjectName(), subjects, props, properties, 
                new GetTriplesHandler(currentSubject));
        ICDServiceManager.getInstance().getListOfSelectedPostCoordinationAxes(getProject().getProjectName(), currentSubject.getName(), properties, 
                new GetSelectedPostCoordinationAxesHandler(currentSubject));
    }

    @Override
    protected void onValueColumnClicked(GridPanel grid, int rowIndex,
    		int colIndex) {
        String fieldType = (String) getColumnConfiguration(colIndex, FormConstants.FIELD_TYPE);
        if (FormConstants.FIELD_TYPE_CHECKBOX_IMPORTANT.equals(fieldType)) {
	        GWT.log("Radio checked in col: " + colIndex);
	        Record record = getStore().getRecordAt(rowIndex);
	        String field = properties.get(colIndex);
	        int value = record.getAsInteger(field);
	        if ((value & 2) != 0) {
	        	onSetPostcoordinationAxisNotAllowed(record, rowIndex, colIndex);
	        }
	        else if ((value & 1) != 0) {
	        	onSetPostcoordinationAxisRequired(record, rowIndex, colIndex);
	        }
	        else {
	        	onSetPostcoordinationAxisAllowed(record, rowIndex, colIndex);
	        }
        }
    }
    
    @Override
    protected InstanceGridCellMouseListener getGridMouseListener() {
    	return new PostCoordinationGridMouseListener();
    }
    
    private class PostCoordinationGridMouseListener extends InstanceGridCellMouseListener {
    	
    	@Override
    	public void onCellClick(GridPanel grid, int rowIndex, int colIndex,
    			EventObject e) {
            String fieldType = (String) getColumnConfiguration(colIndex, FormConstants.FIELD_TYPE);
            if (FormConstants.FIELD_TYPE_CHECKBOX_IMPORTANT.equals(fieldType)) {
            	onValueColumnClicked(grid, rowIndex, colIndex);
            }
            else {
        		super.onCellClick(grid, rowIndex, colIndex, e);
            }
    	}
    	
    	@Override
    	public void onCellDblClick(GridPanel grid, int rowIndex, int colIndex,
    			EventObject e) {
    		super.onCellDblClick(grid, rowIndex, colIndex, e);
    	}
    }
        
    @Override
    protected void onContextMenuCheckboxClicked(final int rowIndex, final int colIndex,  final EventObject e) {
        final Record record = getStore().getAt(rowIndex);
        if (record != null) {
            if (isWriteOperationAllowed()) {
                String field = record.getFields()[colIndex];
                String value = record.getAsString(field);
                if (value != null && !"".equals(value)) {
                    Menu contextMenu = new PostcoordinationAxisContextMenu(
                            record, rowIndex, colIndex);
                    contextMenu.showAt(e.getXY()[0] + 5, e.getXY()[1] + 5);
                }
            }
        }
    }

    final class PostcoordinationAxisContextMenu extends Menu{
        public PostcoordinationAxisContextMenu(final Record record, final int rowIndex, final int colIndex) {
            addNotAllowed("Not Allowed", UIConstants.ICON_CHECKBOX_UNCHECKED, record, rowIndex, colIndex);
            addAllowed("Allowed", UIConstants.ICON_CHECKBOX_CHECKED, record, rowIndex, colIndex);
            addRequired("Required", UIConstants.ICON_CHECKBOX_IMPORTANT, record, rowIndex, colIndex);
        }

		private void addAllowed(String menuText, String menuIcon,
				final Record record, final int rowIndex, final int colIndex) {
			MenuItem item = new MenuItem();
            item.setText(menuText);
            item.setIcon(menuIcon);
            item.addListener(new BaseItemListenerAdapter() {
                @Override
                public void onClick(BaseItem item, EventObject e) {
                    super.onClick(item, e);
                    onSetPostcoordinationAxisAllowed(record, rowIndex, colIndex);
                }
            });
            addItem(item);
		}
		
		private void addRequired(String menuText, String menuIcon,
				final Record record, final int rowIndex, final int colIndex) {
			MenuItem item = new MenuItem();
            item.setText(menuText);
            item.setIcon(menuIcon);
            item.addListener(new BaseItemListenerAdapter() {
                @Override
                public void onClick(BaseItem item, EventObject e) {
                    super.onClick(item, e);
                    onSetPostcoordinationAxisRequired(record, rowIndex, colIndex);
                }
            });
            item.setIconCls("menuitem_icon_32x16");
            addItem(item);
		}

		private void addNotAllowed(String menuText, String menuIcon,
				final Record record, final int rowIndex, final int colIndex) {
			MenuItem item = new MenuItem();
            item.setText(menuText);
            item.setIcon(menuIcon);
            item.addListener(new BaseItemListenerAdapter() {
                @Override
                public void onClick(BaseItem item, EventObject e) {
                    super.onClick(item, e);
                    onSetPostcoordinationAxisNotAllowed(record, rowIndex, colIndex);
                }
            });
            addItem(item);
		}
    }

	protected void onSetPostcoordinationAxisNotAllowed(Record record,
			int rowIndex, int colIndex) {
        String field = properties.get(colIndex);
        record.set(field, 0);

        String selSubject = record.getAsString(INSTANCE_FIELD_NAME);
        if (selSubject != null) {
        	ICDServiceManager.getInstance().removeAllowedPostCoordinationAxis(
        			getProject().getProjectName(), getSubject().getName(), 
        			selSubject, field, new RemovePostcoordinationAxisHandler(field));
        }
	}

	protected void onSetPostcoordinationAxisAllowed(Record record,
			int rowIndex, int colIndex) {
        String field = properties.get(colIndex);
        record.set(field, 1);

        String selSubject = record.getAsString(INSTANCE_FIELD_NAME);
        if (selSubject != null) {
        	ICDServiceManager.getInstance().addAllowedPostCoordinationAxis(
        			getProject().getProjectName(), getSubject().getName(), 
        			selSubject, field, false, new AddPostcoordinationAxisHandler(field));
        }
	}

	protected void onSetPostcoordinationAxisRequired(Record record,
			int rowIndex, int colIndex) {
        String field = properties.get(colIndex);
        record.set(field, 2);

        String selSubject = record.getAsString(INSTANCE_FIELD_NAME);
        if (selSubject != null) {
        	ICDServiceManager.getInstance().addAllowedPostCoordinationAxis(
        			getProject().getProjectName(), getSubject().getName(), 
        			selSubject, field, true, new AddPostcoordinationAxisHandler(field));
        }
	}

	
	
	class AddPostcoordinationAxisHandler extends AbstractAsyncHandler<Boolean> {
		private String pcAxisProperty;

		public AddPostcoordinationAxisHandler(String pcAxisProperty) {
			this.pcAxisProperty = pcAxisProperty;
		}

		@Override
		public void handleSuccess(Boolean first) {
			getStore().commitChanges();
			if (first)	{
            	activateValueSelectionWidget(pcAxisProperty);
			}
		}
		
		@Override
		public void handleFailure(Throwable caught) {
			getStore().rejectChanges();
		}
	}

    class RemovePostcoordinationAxisHandler extends AbstractAsyncHandler<Boolean> {
		private String pcAxisProperty;

		public RemovePostcoordinationAxisHandler(String pcAxisProperty) {
			this.pcAxisProperty = pcAxisProperty;
		}
    	
        @Override
        public void handleSuccess(Boolean last) {
            getStore().commitChanges();
            if (last)	{
            	deactivateValueSelectionWidget(pcAxisProperty);
            }
        }

		@Override
		public void handleFailure(Throwable caught) {
			getStore().rejectChanges();
		}
    }


    public void activateValueSelectionWidget(String pcAxisProperty) {
    	widgetController.showWidgetForProperty(pcAxisProperty);
	}

    public void deactivateValueSelectionWidget(String pcAxisProperty) {
    	widgetController.hideWidgetForProperty(pcAxisProperty);
	}

    
	
    @Override
    protected Renderer createColumnRenderer(final String fieldType, Map<String, Object> config) {
        if (FormConstants.FIELD_TYPE_COMBOBOX.equals(fieldType)) {
            return super.createColumnRenderer(fieldType, config);
        }

        InstanceGridColumnRenderer renderer = new PostCoordinationGridColumnRenderer(fieldType, null);
        return renderer;
    }

    class PostCoordinationGridColumnRenderer extends InstanceGridColumnRenderer {
    	private String type;
    	
        public PostCoordinationGridColumnRenderer(final String fieldType) {
            this(fieldType, null);
        }

        public PostCoordinationGridColumnRenderer(final String fieldType, Map<String, String> valueToDisplayTextMap) {
			super(fieldType, valueToDisplayTextMap);
            this.type = fieldType;
        }

		@Override
		public String render(Object value, CellMetadata cellMetadata,
				Record record, int rowIndex, int colNum, Store store) {
			
			if (FormConstants.FIELD_TYPE_CHECKBOX_IMPORTANT.equals(type)) {
				return renderThreeWayCheckBox(value, cellMetadata, record, rowIndex, colNum, store);
			}
			
			return super.render(value, cellMetadata, record, rowIndex, colNum, store);
		}
		
		protected String renderThreeWayCheckBox(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
                Store store) {
			if (value instanceof String) {
				String strValue = (String)value;
				try {
					int intValue = Integer.parseInt(strValue);
					value = new Integer(intValue);
				}
				catch (NumberFormatException nfe) {
				}
			}
			if (value instanceof Integer) {
				int intValue = ((Integer)value).intValue();
				switch (intValue) {
				case 0: 
					return "<img class=\"checkbox\" src=\"" + UIConstants.ICON_CHECKBOX_UNCHECKED + "\"/>";//<div style=\"text-align: center;\"> IMG_TAG </div>;
				case 1: 
					return "<img class=\"checkbox\" src=\"" + UIConstants.ICON_CHECKBOX_CHECKED + "\"/>";//<div style=\"text-align: center;\"> IMG_TAG </div>;
				case 2: 
				case 3: 
					return "<img class=\"checkbox\" src=\"" + UIConstants.ICON_CHECKBOX_IMPORTANT + "\"/>";//<div style=\"text-align: center;\"> IMG_TAG </div>;
				default: 
					return "<img class=\"checkbox\" src=\"" + UIConstants.ICON_CHECKBOX_UNKNOWN + "\"/>";//<div style=\"text-align: center;\"> IMG_TAG </div>;
				}
			}
			else {
				if (value == null) {
					return "<img class=\"checkbox\" src=\"" + UIConstants.ICON_CHECKBOX_UNKNOWN + "\"/>";//<div style=\"text-align: center;\"> IMG_TAG </div>;
				}
				else {
					return value.toString();
				}
			}
		}
    }

    protected class GetSelectedPostCoordinationAxesHandler extends AbstractAsyncHandler<List<String>> {
    	private EntityData subject;
    	
        public GetSelectedPostCoordinationAxesHandler(EntityData subject) {
			this.subject = subject;
		}

		@Override
		public void handleFailure(Throwable caught) {
            GWT.log("Post-Coordination Grid Widget: Error at getting list if selected post-cooordination axes properties for " + subject, caught);
        	for (String propertyName : properties) {
       			widgetController.showWidgetForProperty(propertyName);
        	}
		}

		@Override
        public void handleSuccess(List<String> selectedProperties) {
			//in case the selection did not change since the remote method was called
			if (subject != null && subject.equals(getSubject())) {
	        	for (String propertyName : properties) {
	        		if (selectedProperties.contains(propertyName)) {
	        			widgetController.showWidgetForProperty(propertyName);
	        		}
	        		else {
	        			widgetController.hideWidgetForProperty(propertyName);
	        		}
	        	}
			}
        }
    }

}