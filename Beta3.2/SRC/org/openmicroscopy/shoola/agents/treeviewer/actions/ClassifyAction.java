/*
 * org.openmicroscopy.shoola.agents.treeviewer.actions.ClassifyAction
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

package org.openmicroscopy.shoola.agents.treeviewer.actions;


//Java imports
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.Icon;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.treeviewer.IconManager;
import org.openmicroscopy.shoola.agents.treeviewer.browser.Browser;
import org.openmicroscopy.shoola.agents.treeviewer.browser.TreeImageDisplay;
import org.openmicroscopy.shoola.agents.treeviewer.cmd.ClassifyCmd;
import org.openmicroscopy.shoola.agents.treeviewer.view.TreeViewer;
import org.openmicroscopy.shoola.util.ui.UIUtilities;
import pojos.ImageData;

/** 
 * Classifies the currently selected image.
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @version 2.2
 * <small>
 * (<b>Internal version:</b> $Revision$ $Date$)
 * </small>
 * @since OME2.2
 */
public class ClassifyAction
    extends TreeViewerAction
{

    /** Identifies the classify action. */
    public static final int     CLASSIFY = ClassifyCmd.CLASSIFY;
    
    /** Identifies the declassify action. */
    public static final int     DECLASSIFY = ClassifyCmd.DECLASSIFY;
    
    /** The name of the action. */
    private static final String NAME_CLASSIFY = "Add Tag";
    
    /** The description of the action. */
    private static final String DESCRIPTION_CLASSIFY = "Add Tag to the " +
    		"selected image.";
    
    /** The name of the action. */
    private static final String NAME_DECLASSIFY = "Remove Tag";
    
    /** The description of the action. */
    private static final String DESCRIPTION_DECLASSIFY = "Remove the tag " +
    		"from the selected image.";
    
    /** One of the constants defined by this class. */
    private final int index;
    
    /**
     * Sets the action enabled depending on the selected type.
     * @see TreeViewerAction#onDisplayChange(TreeImageDisplay)
     */
    protected void onDisplayChange(TreeImageDisplay selectedDisplay)
    {
        if (selectedDisplay == null) {
            setEnabled(false);
            return;
        }
        /*
        Browser browser = model.getSelectedBrowser();
        if (browser != null) {
            if (browser.getBrowserType() == Browser.CATEGORY_EXPLORER && 
                index == CLASSIFY) {
                setEnabled(false);
                return;
            }    
        }
        setEnabled(selectedDisplay.getUserObject() instanceof ImageData);
        */
        Object o = selectedDisplay.getUserObject();
        if (!(o instanceof ImageData)) {
        	setEnabled(false);
        	return;
        }
        Browser browser = model.getSelectedBrowser();
        if (browser != null) {
        	int n = browser.getSelectedDisplays().length;
        	if (n > 1) {
        		 setEnabled(!(index == DECLASSIFY));
        	} else {
        		if (index == DECLASSIFY) {
        			ImageData img = (ImageData) o;
            		Long v = null;//img.getClassificationCount();
            		setEnabled(v != null && v.longValue() > 0);
        		} else
        		
        		setEnabled(true);
        	}
        }
    }
    
    /**
     * Creates a new instance.
     * 
     * @param model Reference to the Model. Mustn't be <code>null</code>.
     * @param index One of the constants defined by this class.
     */
    public ClassifyAction(TreeViewer model, int index)
    {
        super(model);
        IconManager im = IconManager.getInstance();
        String d = "";
        Icon icon = null;
        switch (index) {
            case CLASSIFY:
                name = NAME_CLASSIFY;
                d = DESCRIPTION_CLASSIFY;
                icon = im.getIcon(IconManager.CLASSIFY);
                this.index = index;
                break;
            case DECLASSIFY:
                name = NAME_DECLASSIFY;
                d = DESCRIPTION_DECLASSIFY;
                icon = im.getIcon(IconManager.DECLASSIFY);
                this.index = index;
                break;
            default:
                throw new IllegalArgumentException("Index not supported.");
        }
        putValue(Action.NAME, name);
        putValue(Action.SHORT_DESCRIPTION, UIUtilities.formatToolTipText(d));
        putValue(Action.SMALL_ICON, icon);
    }
    
    /**
     * Creates a {@link ClassifyCmd} command to execute the action. 
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
        ClassifyCmd cmd = new ClassifyCmd(model, index);
        cmd.execute();
    }

}
