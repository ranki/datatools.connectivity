/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.datatools.connectivity.sqm.internal.core.util;

import java.io.StringWriter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.datatools.connectivity.sqm.internal.core.definition.DatabaseDefinition;

/**
 * @author ledunnel
 */
public class RTEHelper {

	/**
	 * @param ddlStatements
	 * @param databaseDefinition
	 * @param path
	 * @param statementTerminator - Use null if you want to use the default terminator.
	 * @return The generated file.
	 */
	public static IFile saveDDL(String[] ddlStatements,
			DatabaseDefinition databaseDefinition, IPath path, String statementTerminator) {
		IFile ddlFile = null;
		StringWriter writer = new StringWriter();
		String terminator = getDefaultStatementTerminator(databaseDefinition);
		if (statementTerminator != null){
			terminator = statementTerminator;
		}
		for (int i = 0; i < ddlStatements.length; i++) {
			writer.write(ddlStatements[i]
					+ terminator);
		}
		ddlFile = SaveDDLUtility.getInstance().saveDDLFileAsResource(writer,
				path.toString());
		return ddlFile;
	}

	private static String getDefaultStatementTerminator(DatabaseDefinition dbDef) {
		// Probably should retrieve the default terminator from database
		// definition
		return dbDef.getProduct().equals("SQL Server") ? "\r\nGo\r\n\r\n" //$NON-NLS-1$ //$NON-NLS-2$
				: ";\r\n\r\n"; //$NON-NLS-1$
	}
}
