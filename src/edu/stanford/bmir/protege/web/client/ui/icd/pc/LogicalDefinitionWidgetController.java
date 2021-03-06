package edu.stanford.bmir.protege.web.client.ui.icd.pc;

import java.util.Collection;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.gwtext.client.widgets.Panel;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.PropertyWidget;
import edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm.FormGenerator;

public class LogicalDefinitionWidgetController<ControllingWidget extends LogicalDefinitionWidget> extends PreCoordinationWidgetController<ControllingWidget> {

	private LogicalDefinitionWidget logicalDefinitionWidget;
	private NecessaryConditionsWidget necessaryConditionsWidget;
	
	public LogicalDefinitionWidgetController(Project project, Panel tabPanel, FormGenerator formGenerator) {
		super(project, tabPanel, formGenerator);
		initWidgets();//TODO check if this is necessary, or is there  abetter place to call this
	}


	@Override
	public void setControllingWidget(ControllingWidget widget) {
		super.setControllingWidget(widget);
		if (widget instanceof LogicalDefinitionWidget) {
			logicalDefinitionWidget = (LogicalDefinitionWidget) widget;
		}
		else {
			GWT.log("ERROR: The controlling widget of a LogicalDefinitionWidgetController should be of type LogicalDefinitionWidget!");
		}
	}
	
	public LogicalDefinitionWidget getLogicalDefinitionWidget() {
		return logicalDefinitionWidget;
	}

//	public void setLogicalDefinitionWidget(LogicalDefinitionWidget logicalDefinitionWidget) {
//		this.logicalDefinitionWidget = logicalDefinitionWidget;
//	}

	public NecessaryConditionsWidget getNecessaryCondittionsWidget() {
		return necessaryConditionsWidget;
	}

	public void setNecessaryConditionsWidget(NecessaryConditionsWidget necessaryConditionsWidget) {
		this.necessaryConditionsWidget = necessaryConditionsWidget;
	}

	@Override
	protected List<String> getAllProperties() {
		//TODO: finish this
		//return super.getAllProperties();
		return logicalDefinitionWidget.getValidProperties();
	}


	@Override
	protected PropertyWidget createWidgetForProperty(String propertyName, boolean isDefinitional) {
		if (isDefinitional) {
			logicalDefinitionWidget.addAxisToForm(propertyName);
			return logicalDefinitionWidget.getWidgetForProperty(propertyName);
		}
		else {
			necessaryConditionsWidget.addAxisToForm(propertyName);
			return necessaryConditionsWidget.getWidgetForProperty(propertyName);
		}
	}

	
	@Override
	protected void removeWidgetForProperty(String propertyName) {
		hideWidgetForProperty(propertyName);
		
	}
	
	@Override
	public void afterWidgetVisibilityChanged(PropertyWidget widget, boolean isVisible) {
		logicalDefinitionWidget.afterWidgetVisibilityChanged(widget, isVisible);
		//TODO check if this makes sense like this
		necessaryConditionsWidget.afterWidgetVisibilityChanged(widget, true);
	}

	
	@Override
	protected PropertyWidget getWidgetForProperty(String propertyName) {
		PropertyWidget widget = super.getWidgetForProperty(propertyName);
		if ( widget == null ) {
			widget = logicalDefinitionWidget.getWidgetForProperty(propertyName);

			if ( widget == null ) {
				widget = necessaryConditionsWidget.getWidgetForProperty(propertyName);
			}
		}
		return widget;
	}
	
	@Override
	public void hideAllWidgets() {
		// TODO hide widgets
		//super.hideAllWidgets();
		logicalDefinitionWidget.removeAllAxis();
		necessaryConditionsWidget.removeAllAxis();
		
		//update list of properties in properties selector
		logicalDefinitionWidget.clearPropertiesList();
		//TODO see if we need to call this, or something else
		//necessaryConditionsWidget.clearPropertiesList();
	}
	
	@Override
	protected void updatePropertyList(List<String> result) {
		GWT.log("updatePropertyList");
		if (logicalDefinitionWidget != null) {
			logicalDefinitionWidget.setValidProperties(result);
		}
		else {
			GWT.log("logicalDefinitionWidget id not set for " + this);
		}
	}


	public void showHideWidgetsDueToTypeChange(PostCoordinationAxesForm form, List<String> toShow, List<String> toHide) {
		GWT.log("showHideWidgetsDueToTypeChange: " + form + "\ntoShow: " + toShow + "\ntoHide: " + toHide);
		// TODO Check this
    	for (String property : toShow) {
        	showWidgetForProperty(property);
		}
    	for (String property : toHide) {
        	hideWidgetForProperty(property);
		}
	}

	@Override
	public void showWidget(PropertyWidget widget) {
		if (widget != null ) {
			super.showWidget(widget);
			afterWidgetVisibilityChanged(widget, true);
		}
	}

//	@Override
//	protected void setWidgetValue(PropertyWidget widget,
//			EntityData entityData, boolean isDefinitional) {
//		super.setWidgetValue(widget, entityData, isDefinitional);
//		if ( entityData == null ) {
//			hide
//		}
//	}

	public void updateIsDefinitionalOfPropertyWidget(String property, PropertyValueSelectorWidget widget, boolean isDefinitional) {
		PropertyWidget logDefWidgetForProperty = logicalDefinitionWidget.getWidgetForProperty(property);
		PropertyWidget necCondWidgetForProperty = necessaryConditionsWidget.getWidgetForProperty(property);
		Collection<EntityData> values = widget.getValues();
		EntityData currValue = null;
		if (values != null &&  ! values.isEmpty()) {
			currValue = values.iterator().next();
		}
//		else {
//			//TODO This is a hack. We should not need this if widget.getValues() would work properly. Make sure to fix  getValues, and remove this.
//			currValue = new EntityData(widget.getSelectedValue());
//		}
		
		if ( isDefinitional ) {
			if ( widget == necCondWidgetForProperty ) {
				necessaryConditionsWidget.removeAxisFromForm(property);
				logicalDefinitionWidget.addAxisToForm(property);
				setWidgetValue( logicalDefinitionWidget.getWidgetForProperty(property), currValue, isDefinitional );
			}
			else if ( widget == logDefWidgetForProperty ) {
				setWidgetValue( logicalDefinitionWidget.getWidgetForProperty(property), currValue, isDefinitional );
			}
		}
		else {
			if ( widget == logDefWidgetForProperty ) {
				logicalDefinitionWidget.removeAxisFromForm(property);
				necessaryConditionsWidget.addAxisToForm(property);
				setWidgetValue( necessaryConditionsWidget.getWidgetForProperty(property), currValue, isDefinitional );
			}
		}
	}
	
}
