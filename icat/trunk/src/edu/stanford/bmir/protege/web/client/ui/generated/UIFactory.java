package edu.stanford.bmir.protege.web.client.ui.generated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.ui.icd.ICDChangesPortlet;
import edu.stanford.bmir.protege.web.client.ui.icd.ICDClassTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.icd.ICDExportImportPortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.changeanalysis.ChangeAnalysisTab;
import edu.stanford.bmir.protege.web.client.ui.ontology.changeanalysis.ChangeTablePortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.changeanalysis.ChangeTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.changes.ChangeSummaryPortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.changes.ChangesPortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.changes.WatchedEntitiesPortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.classes.AllPropertiesPortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.classes.ClassTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.classes.ClassesTab;
import edu.stanford.bmir.protege.web.client.ui.ontology.classes.LabelingClassTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.classes.PropertiesViewPortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.classes.SuperclassesPortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.hierarchy.ManageHierarchyPortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.hierarchy.ManageHierarchyTab;
import edu.stanford.bmir.protege.web.client.ui.ontology.home.MyICDTab;
import edu.stanford.bmir.protege.web.client.ui.ontology.home.OntologiesPortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.individuals.IndividualsListPortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.individuals.IndividualsTab;
import edu.stanford.bmir.protege.web.client.ui.ontology.metadata.AnnotationsPortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.metadata.ImportsTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.metadata.MetadataTab;
import edu.stanford.bmir.protege.web.client.ui.ontology.metadata.MetricsPortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.notes.NotesPortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.notes.NotesTab;
import edu.stanford.bmir.protege.web.client.ui.ontology.notes.NotesTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.notes.OntologyNotesTab;
import edu.stanford.bmir.protege.web.client.ui.ontology.properties.PropertiesTab;
import edu.stanford.bmir.protege.web.client.ui.ontology.properties.PropertiesTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.restrictions.HTMLRestrictionsPortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.restrictions.RestrictionsPortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.reviews.ReviewsPortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.reviews.ReviewsTab;
import edu.stanford.bmir.protege.web.client.ui.ontology.search.BioPortalSearchPortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.search.OtherTerminologiesTab;
import edu.stanford.bmir.protege.web.client.ui.portlet.EntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.portlet.bioportal.BioPortalProposalsPortlet;
import edu.stanford.bmir.protege.web.client.ui.portlet.html.HtmlMessagePortlet;
import edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm.PropertyFieldPortlet;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;
import edu.stanford.bmir.protege.web.client.ui.tab.UserDefinedTab;

/**
 * This class is supposed to be automatically generated by the GWT generator at
 * compile time. In the current version it is not.
 *
 * This is a factory class that will provide create methods for all tabs and
 * portlets. The UI configurator will use this class to layout the UI for a
 * particular project and user.
 *
 * This factory tries to compensate for the lack of reflection support in GWT
 * and JS.
 *
 * @author Tania Tudorache <tudorache@stanford.edu>
 *
 */
public class UIFactory {

    /*
     * Tab factory
     */

    public static AbstractTab createTab(Project project, String tabJavaClassName) {
        if (tabJavaClassName.equals(ClassesTab.class.getName())) {
            return new ClassesTab(project);
        } else if (tabJavaClassName.equals(PropertiesTab.class.getName())) {
            return new PropertiesTab(project);
        } else if (tabJavaClassName.equals(IndividualsTab.class.getName())) {
            return new IndividualsTab(project);
        } else if (tabJavaClassName.equals(MetadataTab.class.getName())) {
            return new MetadataTab(project);
        } else if (tabJavaClassName.equals(NotesTab.class.getName())) {
            return new NotesTab(project);
        } else if (tabJavaClassName.equals(OtherTerminologiesTab.class.getName())) {
            return new OtherTerminologiesTab(project);
        } else if (tabJavaClassName.equals(UserDefinedTab.class.getName())) {
            return new UserDefinedTab(project);
        } else if (tabJavaClassName.equals(MyICDTab.class.getName())) {
            return new MyICDTab(project);
        } else if (tabJavaClassName.equals(ReviewsTab.class.getName())) {
            return new ReviewsTab(project);
        } else if (tabJavaClassName.equals(ManageHierarchyTab.class.getName())) {
            return new ManageHierarchyTab(project);
        } else if (tabJavaClassName.equals(OntologyNotesTab.class.getName())) {
            return new OntologyNotesTab(project);
        } else if (tabJavaClassName.equals(ChangeAnalysisTab.class.getName())) {
            return new ChangeAnalysisTab(project);
        }
        return null;
    }

    /*
     * Portlet factory
     */

    public static EntityPortlet createPortlet(Project project, String portletJavaClassName) {
        if (portletJavaClassName.equals(AllPropertiesPortlet.class.getName())) {
            return new AllPropertiesPortlet(project);
        } else if (portletJavaClassName.equals(AnnotationsPortlet.class.getName())) {
            return new AnnotationsPortlet(project);
        } else if (portletJavaClassName.equals(ClassTreePortlet.class.getName())) {
            return new ClassTreePortlet(project);
        } else if (portletJavaClassName.equals(LabelingClassTreePortlet.class.getName())) {
            return new LabelingClassTreePortlet(project);
        } else if (portletJavaClassName.equals(ImportsTreePortlet.class.getName())) {
            return new ImportsTreePortlet(project);
        } else if (portletJavaClassName.equals(IndividualsListPortlet.class.getName())) {
            return new IndividualsListPortlet(project);
        } else if (portletJavaClassName.equals(MetricsPortlet.class.getName())) {
            return new MetricsPortlet(project);
        } else if (portletJavaClassName.equals(NotesPortlet.class.getName())) {
            return new NotesPortlet(project);
        } else if (portletJavaClassName.equals(OntologiesPortlet.class.getName())) {
            return new OntologiesPortlet();
        } else if (portletJavaClassName.equals(PropertiesTreePortlet.class.getName())) {
            return new PropertiesTreePortlet(project);
        } else if (portletJavaClassName.equals(HTMLRestrictionsPortlet.class.getName())) {
            return new HTMLRestrictionsPortlet(project);
        } else if (portletJavaClassName.equals(PropertyFieldPortlet.class.getName())) {
            return new PropertyFieldPortlet(project);
        } else if (portletJavaClassName.equals(BioPortalSearchPortlet.class.getName())) {
            return new BioPortalSearchPortlet(project);
        } else if (portletJavaClassName.equals(ChangeSummaryPortlet.class.getName())) {
            return new ChangeSummaryPortlet(project);
        } else if (portletJavaClassName.equals(WatchedEntitiesPortlet.class.getName())) {
            return new WatchedEntitiesPortlet(project);
        } else if (portletJavaClassName.equals(ReviewsPortlet.class.getName())) {
            return new ReviewsPortlet(project);
        } else if (portletJavaClassName.equals(ManageHierarchyPortlet.class.getName())) {
            return new ManageHierarchyPortlet(project);
        } else if (portletJavaClassName.equals(HtmlMessagePortlet.class.getName())) {
            return new HtmlMessagePortlet(project);
        } else if (portletJavaClassName.equals(SuperclassesPortlet.class.getName())) {
            return new SuperclassesPortlet(project);
        } else if (portletJavaClassName.equals(ChangesPortlet.class.getName())) {
            return new ChangesPortlet(project);
        } else if (portletJavaClassName.equals(WatchedEntitiesPortlet.class.getName())) {
            return new WatchedEntitiesPortlet(project);
        } else if (portletJavaClassName.equals(PropertiesViewPortlet.class.getName())) {
            return new PropertiesViewPortlet(project);
        } else if (portletJavaClassName.equals(ICDClassTreePortlet.class.getName())) {
            return new ICDClassTreePortlet(project);
        } else if (portletJavaClassName.equals(ChangeTablePortlet.class.getName())) {
            return new ChangeTablePortlet(project);
        } else if (portletJavaClassName.equals(ChangeTreePortlet.class.getName())) {
            return new ChangeTreePortlet(project);
        } else if (portletJavaClassName.equals(NotesTreePortlet.class.getName())) {
            return new NotesTreePortlet(project);
        } else if (portletJavaClassName.equals(ICDExportImportPortlet.class.getName())) {
            return new ICDExportImportPortlet(project);
        } else if (portletJavaClassName.equals(BioPortalProposalsPortlet.class.getName())) {
            return new BioPortalProposalsPortlet(project);
        } else if (portletJavaClassName.equals(RestrictionsPortlet.class.getName())) {
            return new RestrictionsPortlet(project);
        } else if (portletJavaClassName.equals(ICDChangesPortlet.class.getName())) {
        	return new ICDChangesPortlet(project);
        }
        return null;
    }

    //TODO: taking out ManageHierarchyTab.class.getName() - must be de-icd-ezed
    public static List<String> getAvailableTabNames() {
        /*
         * Removed tabs:
         * MyICDTab.class.getName()
         * OtherTerminologiesTab.class.getName(),
         *
         */
        String[] tabs = { ClassesTab.class.getName(), PropertiesTab.class.getName(), IndividualsTab.class.getName(),
                MetadataTab.class.getName(), NotesTab.class.getName(),
                ChangeAnalysisTab.class.getName(), OntologyNotesTab.class.getName() };
        return Arrays.asList(tabs);
    }

    public static List<String> getAvailablePortletNames() {
        /*
         * Removed portlets:
         * PropertyFieldPortlet.class.getName()
         * ChangeTreePortlet.class.getName(), ChangeSummaryPortlet.class.getName(), ChangeTablePortlet.class.getName(),
         * NotesPortlet.class.getName(),
         * ICDClassTreePortlet.class.getName(), ICDExportImportPortlet.class.getName(),
         */
        String[] portlets = { AllPropertiesPortlet.class.getName(), PropertiesViewPortlet.class.getName(), ClassTreePortlet.class.getName(),
                ImportsTreePortlet.class.getName(), IndividualsListPortlet.class.getName(),
                MetricsPortlet.class.getName(),  OntologiesPortlet.class.getName(),
                PropertiesTreePortlet.class.getName(), HTMLRestrictionsPortlet.class.getName(), RestrictionsPortlet.class.getName(),
                BioPortalSearchPortlet.class.getName(),
                ManageHierarchyPortlet.class.getName(), SuperclassesPortlet.class.getName(), ChangesPortlet.class.getName(),
                WatchedEntitiesPortlet.class.getName(), NotesTreePortlet.class.getName(),
                NotesTreePortlet.class.getName(),
                BioPortalProposalsPortlet.class.getName(),
                ICDChangesPortlet.class.getName()};

        List<String> portletsList = Arrays.asList(portlets);
        Collections.sort(portletsList, new Comparator<String>() {
            public int compare(String p1, String p2) {
                String n1 = p1.substring(p1.lastIndexOf(".") + 1);
                String n2 = p2.substring(p2.lastIndexOf(".") + 1);
                return n1.compareTo(n2);
            }
        });
        return portletsList;
    }

    /*
     * Generic utils
     */

    public static List<String> getAvailableTabShortNames() {
        List<String> tabs = getAvailableTabNames();
        List<String> shortNames = new ArrayList<String>();
        for (String tab : tabs) {
            shortNames.add(tab.substring(tab.lastIndexOf(".") + 1));
        }
        return shortNames;
    }

    /**
     * @return A map from the short names of the portlets to the full java class
     *         name of the portlets.
     */
    public static Map<String, String> getAvailablePortletNameMap() {
        List<String> portlets = getAvailablePortletNames();
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (String portlet : portlets) {
            map.put(portlet.substring(portlet.lastIndexOf(".") + 1), portlet);
        }
        return map;
    }

    /**
     * @return A map from the short names of tabs (label) to the full java class
     *         name of the tabs.
     */
    public static Map<String, String> getAvailableTabNameMap() {
        List<String> tabs = getAvailableTabNames();
        Collections.sort(tabs);
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (String tab : tabs) {
            map.put(tab.substring(tab.lastIndexOf(".") + 1), tab);
        }
        return map;
    }

}
