/*
 * org.openmicroscopy.shoola.agents.treeviewer.ProjectsLoader 
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2008 University of Dundee. All rights reserved.
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
package org.openmicroscopy.shoola.agents.treeviewer;



//Java imports
import java.util.HashSet;
import java.util.Set;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.treeviewer.browser.Browser;
import org.openmicroscopy.shoola.agents.treeviewer.browser.TreeImageDisplay;
import org.openmicroscopy.shoola.env.data.views.CallHandle;
import pojos.ProjectData;

/** 
 * Loads the datasets/images contained in the project hosted by the passed 
 * node. This class calls the <code>loadHierarchy</code> method in the
 * <code>HierarchyBrowsingView</code>. 
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $Date: $)
 * </small>
 * @since OME3.0
 */
public class ProjectsLoader 
	extends DataBrowserLoader
{

	/** Handle to the async call so that we can cancel it. */
    private CallHandle  		handle;
    
    /** Reference to the node hosting the project to browse. */
    private TreeImageDisplay 		node;
    
    /**
     * Creates a new instance.
     * 
     * @param viewer The viewer this loader is for. 
     *               Mustn't be <code>null</code>.
     * @param node   The node hosting the project to browse.
     *               Mustn't be <code>null</code>.
     */
    public ProjectsLoader(Browser viewer, TreeImageDisplay node)
	{
		super(viewer);
		if (node == null)
			throw new IllegalArgumentException("No node of reference.");
		this.node = node;
	}
	
	 /**
     * Retrieves the data.
     * @see DataBrowserLoader#load()
     */
    public void load()
    {
    	long userID = TreeViewerAgent.getUserDetails().getId();
    	long id = node.getUserObjectId();
    	Set<Long> ids = new HashSet<Long>();
    	ids.add(id);
    	handle = hiBrwView.loadHierarchy(ProjectData.class, ids, userID, this);
    }

    /**
     * Cancels the data loading.
     * @see DataBrowserLoader#cancel()
     */
    public void cancel() { handle.cancel(); }

    /**
     * Feeds the result back to the viewer.
     * @see DataBrowserLoader#handleResult(Object)
     */
    public void handleResult(Object result)
    {
        if (viewer.getState() == Browser.DISCARDED) return;  //Async cancel.
       // viewer.setLeaves((Set) result, timeNode, expNode); 
        viewer.setHierarchyRoots((Set) result, node, false);
    }
    
}
