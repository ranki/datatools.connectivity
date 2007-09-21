/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.datatools.connectivity.sample.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class SelectExistingConnectionWizardPage extends WizardPage {

	protected SelectExistingConnectionWizardPage(String pageName) {
		super(pageName);
	}

	public void createControl(Composite parent) {
        setTitle("Sample Page"); //$NON-NLS-1$
        setMessage("Select an existing connection.");
        
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout gridLayout = new GridLayout();
        gridLayout.horizontalSpacing = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        container.setLayout(gridLayout);
        
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        container.setLayoutData(gridData);

        SelectExistingConnectionProfileDialogPage composite = new SelectExistingConnectionProfileDialogPage ();
        composite.createControl(container); 
        setControl(container);
	}

}