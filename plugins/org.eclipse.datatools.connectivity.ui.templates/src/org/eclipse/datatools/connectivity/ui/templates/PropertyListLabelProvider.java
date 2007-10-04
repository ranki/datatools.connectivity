/*******************************************************************************
 * Copyright (c) 2007 Sybase, Inc.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: brianf - initial API and implementation
 ******************************************************************************/
package org.eclipse.datatools.connectivity.ui.templates;

import org.eclipse.jface.viewers.LabelProvider;

/**
 * Label provider for selected driver properties
 * 
 * @author brianf
 *
 */
public class PropertyListLabelProvider extends LabelProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object element) {
		if (element instanceof PropertyObject) {
			return ((PropertyObject)element).getPropertyName();
		}
		return super.getText(element);
	}

}
