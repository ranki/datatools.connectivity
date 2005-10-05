/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.datatools.connectivity.oda.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Default implementation of IBlob for use by ODA runtime drivers.
 * <code>Blob</code> handles common types of raw data that represent
 * a BLOB value.
 */
public class Blob implements IBlob
{

    private byte[] m_byteArray = null;   
    
    private InputStream m_stream = null;
    private long m_length = Long.MIN_VALUE;
    
    private Blob()
    {
        
    }
    
    /**
     * Create a new Blob from a byte array.
     * @param blob		byte array representing the blob.
     */
    public Blob( byte[] byteArray )
    {
        m_byteArray = byteArray;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IBlob#getBinaryStream()
     */
    public InputStream getBinaryStream() throws OdaException
    {
        if ( m_stream == null )
            m_stream = new ByteArrayInputStream( m_byteArray );
        
        return m_stream;
    }

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IBlob#getBytes(long, int)
     */
    public byte[] getBytes( long position, int length ) throws OdaException
    {
        // let the oda.consumer.helper provides default implementation
	    throw new UnsupportedOperationException();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.IBlob#length()
     */
    public long length() throws OdaException
    {
        if ( m_length == Long.MIN_VALUE )
            m_length = m_byteArray.length;
        
        return m_length;
    }

}
