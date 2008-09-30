/*
 * org.openmicroscopy.shoola.env.data.views.calls.ContainerCounterLoader
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006 University of Dundee. All rights reserved.
 *
 *
 * 	This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *------------------------------------------------------------------------------
 */

package org.openmicroscopy.shoola.env.data.views.calls;

//Java imports
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.env.data.OmeroDataService;
import org.openmicroscopy.shoola.env.data.views.BatchCall;
import org.openmicroscopy.shoola.env.data.views.BatchCallTree;
import pojos.CategoryData;
import pojos.DataObject;
import pojos.DatasetData;
import pojos.TagAnnotationData;

/** 
 * Command to retrieve the number of items contained in a specified collection
 * of containers. 
 * Note that we don't retrieve the items cf. lazy loading rule.
 * 
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @version 2.2
 * <small>
 * (<b>Internal version:</b> $Revision$Date: )
 * </small>
 * @since OME2.2
 */
public class ContainerCounterLoader
	extends BatchCallTree
{

    /** The lastly retrieved map. */
    private Map		currentMap;
    
    /** The containers for which we need the value. */ 
    private Set		rootIDs;

    /** 
     * Creates a {@link BatchCall} to retrieve the number of items per 
     * container.
     * 
     * @return See above
     */
    private BatchCall makeBatchCall()
    {
        Iterator i = rootIDs.iterator();
        String description = "Counting items.";
        DataObject root;
        Long id = null;
        Class rootType = null;
        Set<Long> ids = new HashSet<Long>();
        while (i.hasNext()) {
            root = (DataObject) i.next();
            if (root instanceof DatasetData) {
                rootType = DatasetData.class;
                id = new Long(((DatasetData) root).getId());
            } else if (root instanceof CategoryData) {
                rootType = CategoryData.class;
                id = new Long(((CategoryData) root).getId());
            } else if (root instanceof TagAnnotationData) {
            	rootType = TagAnnotationData.class;
                id = new Long(((TagAnnotationData) root).getId());
            }
            if (id != null) ids.add(id);
        }
        final Class rootTypeFinal = rootType;
        final Set<Long> idFinal = ids;
        return new BatchCall(description) {
		    public void doCall() throws Exception
		    { 
		        OmeroDataService os = context.getDataService();
		        currentMap = os.getCollectionCount(rootTypeFinal, 
		                		OmeroDataService.IMAGES_PROPERTY, idFinal);
		    }
		};
    }
    
    /**
     * Adds a {@link BatchCall} to the tree for each value to retrieve.
     * @see BatchCallTree#buildTree()
     */
    protected void buildTree()
    {
    	add(makeBatchCall());
    	/*
        Iterator i = rootIDs.iterator();
        String description;
        DataObject root;
        Long id = null;
        Class rootType = null;
        while (i.hasNext()) {
            root = (DataObject) i.next();
            if (root instanceof DatasetData) {
                rootType = DatasetData.class;
                id = new Long(((DatasetData) root).getId());
            } else if (root instanceof CategoryData) {
                rootType = CategoryData.class;
                id = new Long(((CategoryData) root).getId());
            } else if (root instanceof TagAnnotationData) {
            	rootType = TagAnnotationData.class;
                id = new Long(((TagAnnotationData) root).getId());
            }
            if (id != null) {
                description = "Loading number of items for container: " +
							id.intValue();
                final Long idFinal = id;
                final Class rootTypeFinal = rootType;
				add(new BatchCall(description) {
				    public void doCall() throws Exception
				    { 
				        Set<Long> ids = new HashSet<Long>(1);
				        ids.add(idFinal);
				        OmeroDataService os = context.getDataService();
				        currentMap = os.getCollectionCount(rootTypeFinal, 
				                		OmeroDataService.IMAGES_PROPERTY, ids);
				    }
				});
            }
        }
        */
    }
    
    /**
     * Returns the lastly retrieved thumbnail.
     * This will be packed by the framework into a feedback event and
     * sent to the provided call observer, if any.
     * 
     * @return 	A Map whose key is the containerID and the value the number of 
     * 			items contained in the container.
     */
    //protected Object getPartialResult() { return currentMap; }

    /**
     * Returns <code>null</code> as there's no final result.
     * In fact, values are progressively delivered with feedback events.
     * @see BatchCallTree#getResult()
     */
    protected Object getResult() { return currentMap; }

    /**
     * Creates a new instance.
     * If bad arguments are passed, we throw a runtime
	 * exception so to fail early and in the caller's thread.
     * 
     * @param rootIDs	Collection of root ids. Mustn't be <code>null</code>.
     */
    public ContainerCounterLoader(Set rootIDs)
    {
        if (rootIDs == null) throw new NullPointerException("No root nodes.");
        try {
            rootIDs.toArray(new DataObject[] {});
        } catch (ArrayStoreException ase) {
            throw new IllegalArgumentException("rootIDs only contains " +
                                                "DataObject.");
        }
        this.rootIDs = rootIDs;
    }
    
}
