package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

/**
 * This class defines the form constants that can be used to configure
 * the layout of forms in WebProtege.<br><br>
 * 
 * Modification to this class should be reflected in the user documentation,
 * available on the Protege Wiki through pages like 
 * <a href="http://protegewiki.stanford.edu/wiki/InstanceGridWidget">http://protegewiki.stanford.edu/wiki/InstanceGridWidget</a>
 * 
 * @author csnyulas
 *
 */
public class FormConstants {

    public static final String CLICKS_TO_EDIT = "clicks_to_edit";
    public static final int DEFAULT_CLICKS_TO_EDIT = 2;
    public static final String ONE_CLICK_COMBOBOX_EDITING = "one_click_combobox_editing";

	public static final String TABS = "tabs";
	public static final String TITLE = "title";
	public static final String HEADER_CSS_CLASS = "headerCssClass";	//we should not use camel case in property names

	//this applies to the PropertyFieldPortlet and controls if the tabs are shown in a row, or in multiple rows 
	public static final String MULTIROW_TABS = "multiRowTabs";  
	
	//portlet configuration constants
    public static final String ALLOWED_NEW_PROPERTIES = "allowed_new_properties";
    public static final String VISIBLE_PROPERTIES = "visible_properties";
	
	public static final String TYPES_ANY = "types_any";
	public static final String TYPES_ALL = "types_all";
	public static final String TYPES_NOT = "types_not";

	public static final String TEXTFIELD = "textfield";
	public static final String TEXTAREA = "textarea";
	public static final String CHECKBOX = "checkbox";
	public static final String COMBOBOX = "combobox";
	public static final String HTMLEDITOR = "htmleditor";
	public static final String EXTERNALREFERENCE = "externalreference";

	public static final String HTMLMESSAGE = "htmlMessage";

	public static final String MULTITEXTFIELD = "multitextfield";
	public static final String INSTANCETEXTFIELD = "instancetextfield";
	public static final String INSTANCEREFERENCE = "instancereference";
	public static final String INTERNALREFERENCE = "internalreference";

	public static final String CLASS_SELECTION_FIELD = "classselect";
	public static final String PROPERTY_SELECTION_FIELD = "propertyselect";

	public static final String FIELDSET = "fieldset";
	public static final String FIELDSET_COLLAPISBLE = "collapsible";
	public static final String FIELDSET_COLLAPSED = "collapsed";
	public static final String FIELDSET_CHECKBOX_TOGGLE = "checkboxToggle";

	public static final String INSTANCE_CHECKBOX = "instancecheckbox";
	public static final String INSTANCE_RADIOBUTTON = "instanceradiobutton";
	public static final String INSTANCE_COMBOBOX = "instancecombobox";

	public static final String NAME = "name";
	public static final String LABEL = "label";
    public static final String LABEL_WIDTH = "label_width";
	public static final String CLOSABLE = "closable";
	public static final String TOOLTIP = "tooltip";
	public static final String HELP = "help";
    public static final String LOAD_URL = "load_url";
	public static final String TEXT = "text";
	public static final String COMPONENT_TYPE = "component_type";
	public static final String ALLOWED_VALUES = "allowed_values";
	public static final String ALLOWED_VALUES_ONLY = "allowed_values_only";
	public static final String GROUP_SPECIFIC_ALLOWED_VALUES = "group_specific_allowed_values";
	public static final String READ_ONLY = "read_only";
	public static final String DISABLED = "disabled";
	public static final String PAGE_SIZE = "page_size";
	public static final String WRITE_ACCESS_GROUPS = "writeAccessGroups";	//we should not use camel case in property names
	public static final String SHOW_ONLY_FOR_GROUPS = "showOnlyForGroups";	//we should not use camel case in property names
	public static final String DO_NOT_SHOW_FOR_GROUPS = "doNotShowForGroups";	//we should not use camel case in property names
	public static final String SHOW_ONLY_FOR_TYPES = "showOnlyForTypes";	//we should not use camel case in property names
	public static final String DO_NOT_SHOW_FOR_TYPES = "doNotShowForTypes";	//we should not use camel case in property names

	/*
	 * Instance field widget
	 */
	public static final String GRID = "grid";
	public static final String HEADER = "header";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String PROPERTY = "property";
	public static final String INDEX = "index";
	public static final String SORTED = "sorted";
	public static final String WIDTH_ALL = "all";
	public static final String HIDDEN = "hidden";
	public static final String CLONE_OF = "clone_of";
	public static final String INDEX_OF_SUBJECT_COLUMN = "subject_column_index";
	public static final String COLUMN_PREFIX = "Column";
	public static final String ONT_TYPE = "ont_type";
	public static final String BP_SEARCH_PROPERTIES = "bp_search_properties";
    public static final String ENABLE_HEADER_MENU = "enable_header_menu";
    public static final String IS_SORTABLE = "sortable";
	public static final String FIELD_TYPE = "field_type";
	public static final String FIELD_VALUE_TYPE = "field_value_type";
	public static final String FIELD_EDITOR = "field_editor";
	public static final String FIELD_ALIGN = "field_align";
	public static final String FIELD_BG_COLOR = "field_bg_color";
	public static final String MULTIPLE_VALUES_ALLOWED = "multiple_values_allowed";
	public static final String TOP_CLASS = "top_class";
	public static final String EMPTY_TEXT = "empty_text";
	public static final String DEFAULT_COLUMN_TO_EDIT = "default_column_to_edit";
	public static final String COPY_IF_TEMPLATE = "copy_if_template";
	public static final String SHOW_TOOLBAR = "showToolbar";
	public static final String INSTANCE_DISPLAY_PROP = "instance_display_prop";
	public static final String CREATE_MISSING_SUBJECTS = "create_missing_subjects";
		//internal reference
	public static final String REFERENCED_VALUE_PROP="referenced_value_prop";

	public static final String FIELD_TYPE_LINK_ICON = "linkicon";
	public static final String FIELD_TYPE_NO_LINK = "nolink";
	public static final String FIELD_TYPE_MULTILINE_ICON = "multiline_icon";
	public static final String FIELD_TYPE_CHECKBOX = "checkbox";
	public static final String FIELD_TYPE_CHECKBOX_IMPORTANT = "checkbox_important";
	public static final String FIELD_TYPE_RADIO = "radio";
	public static final String FIELD_TYPE_COMBOBOX = "combobox";
	public static final String FIELD_TYPE_CLASS_BROWSER_TEXT = "class_browsertext";
	public static final String FIELD_TYPE_INSTANCE_BROWSER_TEXT = "instance_browsertext";
	public static final String FIELD_TYPE_INSTANCE_PROPERTY_VALUE = "instance_property_value";
	public static final String FIELD_TYPE_INSTANCE_PROPERTY_ICON = "instance_property_icon";

	public static final String CHECKBOX_DEFAULT_VALUE = "checkbox_default";
	
	//valid FIELD_VALUE_TYPE values are the string names of {@link edu.stanford.bmir.protege.web.client.rpc.data.ValueType} constants
	//such as String, Symbol, Integer, Boolean, Instance, Class, etc.
	
	public static final String FIELD_EDITOR_INLINE = "inline";
	public static final String FIELD_EDITOR_FLEXIBLE = "flexible";
	public static final String FIELD_EDITOR_MULTILINE = "multiline";
	public static final String FIELD_EDITOR_HTML = "html";
	public static final String FIELD_EDITOR_CLASS_SELECTOR = "class_selector";
	public static final String FIELD_EDITOR_INSTANCE_SELECTOR = "instance_selector";

	/*
	 * ICD specific
	 */
	public static final String ICDTITLE_TEXTFIELD = "icdtitle_textfield";
    public static final String ICDLINEARIZATION_GRID = "icdlinearization_grid";
    public static final String ICDINHERITEDTAG_GRID = "icdinheritedtag_grid";
    public static final String ICDINDEX_GRID = "icdindex_grid";
    public static final String ICDINCLUSION_GRID = "icdinclusion_grid";
    public static final String POSTCOORDINATION_GRID = "postcoordination_grid";
    public static final String SCALEEDITOR_GRID = "scaleeditor_grid";
    public static final String FIXEDSCALEVALUES_GRID = "fixedscalevalues_grid";
    public static final String PROPERTY_MAP = "property_map";
    public static final String PRECOORDINATION_COMP = "precoordination_comp";
    public static final String PRECOORDINATION_SUPERCLASS = "precoord_superclass";
    public static final String PRECOORDINATION_CUST_SCALE_VALUE_SELECTOR = "precoord_custom_scale_value_selector";
    public static final String PRECOORDINATION_FIX_SCALE_VALUE_SELECTOR = "precoord_fix_scale_value_selector";
    public static final String PRECOORDINATION_TREE_VALUE_SELECTOR = "precoord_tree_value_selector";
    public static final String NECESSARY_CONDITIONS_COMP = "necessary_cond_comp";
    public static final String LOGICAL_DEFINITIONS_COMP = "logical_definition_comp";
    
    
//    public static final String VALUE_SELECTOR_TYPE = "selector_type";
//    public static final String VALUE_SELECTOR_CUSTOM_SCALE = "custom_scale";
//    public static final String VALUE_SELECTOR_VALUE_TREE= "value_tree";
//    public static final String VALUE_SELECTOR_FIXED_SCALE = "fixed_scale";
    public static final String SUPERCLASS_SELECTOR = "superclass_selector";
    public static final String VALUE_SELECTORS = "value_selectors";
    public static final String SHOW_IS_DEFINED = "show_is_defined";
    public static final String SHOW_LOGICAL_NECESSARY_SWITCH = "show_logical_necessary_switch";

    public static final String LOGICAL_DEF_PROPERTIES_CONFIG = "properties_config";

}
