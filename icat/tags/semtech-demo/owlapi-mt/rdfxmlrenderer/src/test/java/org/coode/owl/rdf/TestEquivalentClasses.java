package org.coode.owl.rdf;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
/*
 * Copyright (C) 2007, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 09-May-2007<br><br>
 */
public class TestEquivalentClasses extends AbstractRendererAndParserTestCase {

    protected String getClassExpression() {
        return "Equivalent classes axioms test case";
    }


    protected Set<OWLAxiom> getAxioms() {
        OWLClass clsA = getManager().getOWLDataFactory().getOWLClass(TestUtils.createIRI());
        OWLObjectProperty prop = getManager().getOWLDataFactory().getOWLObjectProperty(TestUtils.createIRI());
        OWLClassExpression descA = getManager().getOWLDataFactory().getOWLObjectSomeValuesFrom(prop,
                getManager().getOWLDataFactory().getOWLThing());
        Set<OWLClassExpression> classExpressions = new HashSet<OWLClassExpression>();
        classExpressions.add(clsA);
        classExpressions.add(descA);
        OWLAxiom ax = getManager().getOWLDataFactory().getOWLEquivalentClassesAxiom(classExpressions);
        return Collections.singleton(ax);
    }
}