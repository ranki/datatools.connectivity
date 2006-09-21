/*******************************************************************************
 * Copyright (c) 2006 Sybase, Inc.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: rcernich - initial API and implementation
 ******************************************************************************/
package org.eclipse.datatools.connectivity.sqm.loader;

import org.eclipse.datatools.connectivity.sqm.internal.core.connection.ConnectionFilter;
import org.eclipse.datatools.connectivity.sqm.internal.core.connection.ConnectionInfo;
import org.eclipse.datatools.connectivity.sqm.internal.core.rte.ICatalogObject;

public class CatalogFilterProvider extends ConnectionFilterProvider {

	public ConnectionFilter getConnectionFilter(ICatalogObject catalogObject) {
		ConnectionInfo ci = getConnectionInfo(catalogObject);
		return ci.getFilter(ConnectionFilter.CATALOG_FILTER);
	}

}