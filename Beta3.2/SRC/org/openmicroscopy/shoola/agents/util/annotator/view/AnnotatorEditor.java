/*
 * org.openmicroscopy.shoola.agents.util.annotator.view.AnnotatorEditor 
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2007 University of Dundee. All rights reserved.
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
package org.openmicroscopy.shoola.agents.util.annotator.view;


//Java imports
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.util.DataHandler;
import pojos.DataObject;

/** 
 * Defines the interface provided by the annotator editor component.
 * The annotator provides a top-level window to host annotation 
 * and let the user interact with it.
 * <p>The typical life-cycle of an annotator editor is as follows.The object
 * is first created using the {@link AnnotatorFactory}. After
 * creation the object is in the {@link #NEW} state and is waiting for the
 * {@link #activate() activate} method to be called.
 * 
 * When the user quits the window, the {@link #discard() discard} method is
 * invoked and the object transitions to the {@link #DISCARDED} state.
 * At which point, all clients should de-reference the component to allow for
 * garbage collection.
 * 
 * </p>
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
public interface AnnotatorEditor 
	extends DataHandler
{

	/** Bounds property indicating to save the annotation. */
	public static final String		SAVE_PROPERTY = "Save";

	/** 
	 * Bounds property indicating that the annotations linked 
	 * to a given data object are loaded.
	 */
	public static final String		ANNOTATION_LOADED_PROPERTY = 
										"annotationLoaded";
	
	/** Indicates to layout the component horizontally. */
	public static final int	HORIZONTAL_LAYOUT = 0;
	
	/** Indicates to layout the component horizontally. */
	public static final int VERTICAL_LAYOUT = 1;
	
	/**
	 * Queries the current state.
	 * 
	 * @return One of the state flags defined by this interface.
	 */
	public int getState();
	
	/** Cancels any ongoing data loading. */
	public void cancel();
	
	/**
	 * Sets the annotations retrieved for the annotated 
	 * <code>DataObject</code>.
	 * 
	 * @param annotations The value to set.
	 */
	public void setAnnotations(Collection annotations);

	/** 
	 * Indicates that the annotation has been saved. 
	 * 
	 * @param result The updated <code>DataObject</code>s.
	 */
	public void setAnnotationSaved(List result);

	/** Creates or updates the annotation for the edited data object. */
	public void save();

	/**
	 * Saves the annotation for all displayed items if the passed 
	 * index if {@link #SELECT_ALL} or saves the annotation for the
	 * selected item if the passed index is {@link #SELECT_ONE}.
	 * 
	 * @param index One of the following constants: {@link #SELECT_ALL}
	 * 				or {@link #SELECT_ONE}.
	 */
	public void save(int index);
	
	/** 
	 * Creates a new textual annotation.
	 * 
	 * @param text The annotation to save.
	 */
	public void save(String text);
	
	/** 
	 * Removes the annotation. 
	 * 
	 * @param l The annotations to delete.
	 */
	public void delete(List l);
	
	/** 
	 * Returns the view
	 * 
	 * @return See above.
	 */
	public JComponent getUI();

	/**
	 * Returns <code>true</code> if the current user has an annotation 
	 * to delete, <code>false</code> otherwise.
	 * 
	 * @return See above.
	 */
	public boolean hasAnnotation();
	
	/** 
	 * Returns <code>true</code> if there is an annotation to save. 
	 * <code>false</code> otherwise.
	 * 
	 * @return See above.
	 */
	public boolean hasDataToSave();
	
	/** 
	 * Returns <code>true</code> if there is an annotation to save. 
	 * <code>false</code> otherwise.
	 * 
	 * @return See above.
	 */
	public boolean hasTextEntered();
	
	/**
	 * Retrieves the annotation for the passed object.
	 * 
	 * @param object The object to handle.
	 */
	public void retrieveAnnotations(DataObject object);

	/**
	 * Adds the selected nodes along the annotated one.
	 * 
	 * @param nodes The nodes to add.
	 */
	public void addSelectedNodes(List nodes);

}
