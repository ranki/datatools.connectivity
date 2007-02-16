/*
 *************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.datatools.connectivity.oda.profile.provider;

import java.io.File;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.consumer.services.IPropertyProvider;
import org.eclipse.datatools.connectivity.oda.profile.OdaProfileExplorer;
import org.eclipse.datatools.connectivity.oda.util.manifest.ConnectionProfileProperty;

/**
 *  Provider of externalized properties defined in a linked connection profile.
 *  @since 3.0.4
 */
public class ProfilePropertyProviderImpl implements IPropertyProvider
{
    
    // logging variables
    private static final String sm_className = ProfilePropertyProviderImpl.class.getName();
    private static Logger sm_logger;

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.consumer.services.IPropertyProvider#getDataSourceProperties(java.util.Properties, java.lang.Object)
     */
    public Properties getDataSourceProperties( Properties candidateProperties,
            Object appContext ) throws OdaException
    {
        final String methodName = "getDataSourceProperties"; //$NON-NLS-1$
        
        IConnectionProfile connProfile = 
            getConnectionProfile( candidateProperties, appContext );
        if( connProfile == null )   // no linked profle
            return candidateProperties;
        
        // merges the 2 sets of properties; 
        // remove the entries of the linked profile instance, and
        // override candidate properties with those in profile instance
        
        Properties mergedProps = new Properties();
        mergedProps.putAll( candidateProperties );
        mergedProps.remove( ConnectionProfileProperty.PROFILE_NAME_PROP_KEY );
        mergedProps.remove( ConnectionProfileProperty.PROFILE_STORE_FILE_PATH_PROP_KEY );
                
        Properties profileProps = connProfile.getBaseProperties();
        if( profileProps != null )
            mergedProps.putAll( profileProps );
        
        // log count of merged properties
        if( getLogger().isLoggable( Level.FINER ) )
        {
            String logMsg = sm_className + "." + methodName + ": "; //$NON-NLS-1$ //$NON-NLS-2$
            logMsg += "Number of Candidate Properties = " + candidateProperties.size(); //$NON-NLS-1$
            logMsg += "; Number of Properties in profile = " + profileProps.size(); //$NON-NLS-1$
            logMsg += "; Number of Merged Effective Properties = " + mergedProps.size(); //$NON-NLS-1$
            getLogger().finer( logMsg );
        }
        
        return mergedProps;
    }

    /**
     * Find and return the connection profile store file of the profile
     * instance whose name is specified in the candidate connection properties.
     * The profile store file object may be provided in the connection property context,
     * or its file path is specified in the connection properties.
     * @param candidateProperties   local connection properties
     * @param connPropContext       connection property context object mapped to 
     *                        the IPropertyProvider.ODA_CONN_PROP_CONTEXT key
     *                        in an application context passed thru to a connection
     * @return
     * @throws OdaException
     */
    protected IConnectionProfile getConnectionProfile( Properties candidateProperties,
            Object connPropContext ) 
    {
        String profileName = getProfileName( candidateProperties );
        if( profileName == null )
            return null;    // no external profile is specified in properties
        
        // next determine which profile store file to use;
        // a profile store mapping specified in the connPropContext map 
        // takes precedence over the file path specified in the properties.
        File profileStore = getProfileStoreFile( connPropContext );
        if( profileStore == null )
            profileStore = getProfileStoreFile( candidateProperties );
        
        IConnectionProfile profile = null;
        try
        {
            // if null profile store file is specified, the default profile store path is used
            profile = OdaProfileExplorer.getInstance()
                        .getProfileByName( profileName, profileStore );
        }
        catch( OdaException ex )
        {
            getLogger().warning( getStackTraceStrings( ex ) );
        }
        if( profile == null )
        {
            // log warning that no profile is found
            getLogger().warning( "No connection profile is found by its specified name: " + profileName ); //$NON-NLS-1$
        }
        
        return profile;
    }
    
    protected String getProfileName( Properties candidateProperties )
    {
        String profileName =
            candidateProperties.getProperty( ConnectionProfileProperty.PROFILE_NAME_PROP_KEY );
        if( profileName == null || profileName.length() == 0 )
            return null;    // no profile name specified
        return profileName;
    }
    
    protected File getProfileStoreFile( Object connPropContext )
    {
        if( connPropContext == null || ! ( connPropContext instanceof Map ) )
            return null;
        
        Object propValue = 
            ( (Map) connPropContext ).get( ConnectionProfileProperty.PROFILE_STORE_FILE_PROP_KEY );
        if( propValue == null )
            return null;    // no non-null mapping for profile store file
        
        if( ( propValue instanceof File ) && ((File) propValue ).exists() )
            return (File) propValue;

        // mapping contains a value of unexpected type
        getLogger().warning( "getProfileStoreFile( Object ): Ignoring the PROFILE_STORE_FILE_PROP_KEY object in Connection Property Context.  The specified object must be an existing File."  );  //$NON-NLS-1$
        return null;        
    }
    
    /**
     * Find and return the connection profile store file whose
     * file path is specified in the connection properties.
     * Ensures the file path exists at runtime; otherwise returns null.
     * @param candidateProperties
     * @return
     */
    protected File getProfileStoreFile( Properties candidateProperties )
    {
        String profileStoreFilePath =
            candidateProperties.getProperty( ConnectionProfileProperty.PROFILE_STORE_FILE_PATH_PROP_KEY );
        if( profileStoreFilePath == null || profileStoreFilePath.length() == 0 )
            return null;    // no profile file path specified
        File profileStoreFile = new File( profileStoreFilePath );
        if( profileStoreFile.exists() )
            return profileStoreFile;
        
        // specified file path does not exist
        getLogger().warning( "getProfileStoreFile( Properties ): Ignoring the PROFILE_STORE_FILE_PATH_PROP_KEY value in connection properties. The specified path does not exist in file system."); //$NON-NLS-1$
        return null;
    }

    /**
     * Returns the class logger.
     */
    private static Logger getLogger()
    {
        if( sm_logger == null )
            sm_logger = Logger.getLogger( sm_className );
        return sm_logger;
    }
    
    /**
     * Formats and returns the exception's stack trace as a string.
     */
    private static String getStackTraceStrings( Throwable ex )
    {
        String logMsg = ex.toString() + "\n"; //$NON-NLS-1$
        Throwable cause = ( ex.getCause() != null ) ? ex.getCause() : ex;
        StackTraceElement[] stacks = cause.getStackTrace();
        for( int i = 0; i < stacks.length; i++ )
        {
            logMsg += stacks[i].toString() + "\n"; //$NON-NLS-1$
        }
        return logMsg;
    }
    
}
