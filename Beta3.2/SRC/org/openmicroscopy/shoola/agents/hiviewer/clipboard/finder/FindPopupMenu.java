/*
 * org.openmicroscopy.shoola.agents.hiviewer.clipboard.finder.FindPopupMenu
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

package org.openmicroscopy.shoola.agents.hiviewer.clipboard.finder;



//Java imports
import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//Third-party libraries

//Application-internal dependencies

/** 
 * Pop-up menu for finder tabbed pane.
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * after code by
 *          Barry Anderson &nbsp;&nbsp;&nbsp;&nbsp;
 *              <a href="mailto:banderson@computing.dundee.ac.uk">
 *              banderson@computing.dundee.ac.uk</a>
 * @version 2.2
 * <small>
 * (<b>Internal version:</b> $Revision: $ $Date: $)
 * </small>
 * @since OME2.2
 */
class FindPopupMenu
    extends JPopupMenu
{
    
    /** The text displayed by the {@link #inNameItem}. */
    private static final String NAME_IN_NAME = "Find in name";
    
    /** The description of the {@link #inNameItem}. */
    private static final String DESCRIPTION_IN_NAME = "Finds the occurence of" +
                                                " the phrase in the name.";
    
    /** The text displayed by the {@link #inDescriptionItem}. */
    private static final String NAME_IN_DESCRIPTION = "Find in description";
    
    /** The description of the {@link #inDescriptionItem}. */
    private static final String DESCRIPTION_IN_DESCRIPTION = 
                    "Finds the occurence of the phrase in the description.";
    
    /** Reference to the {@link FindPane}. */
    private FindPane model;
    
    /** Item to find the phrase in the name. */
    private JCheckBoxMenuItem   inNameItem; 
    
    /** Item to find the phrase in the description. */
    private JCheckBoxMenuItem   inDescriptionItem; 
    
    /** Helper method to create the items. */
    private void createMenuItems()
    {
        inNameItem = new JCheckBoxMenuItem(NAME_IN_NAME);
        inNameItem.setToolTipText(DESCRIPTION_IN_NAME);
        inNameItem.setSelected(model.isNameSelected());
        inNameItem.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
                model.setNameSelected(item.isSelected());
            }
        });
        inDescriptionItem = new JCheckBoxMenuItem(NAME_IN_DESCRIPTION);
        inDescriptionItem.setToolTipText(DESCRIPTION_IN_DESCRIPTION);
        inDescriptionItem.setSelected(model.isDescriptionSelected());
        inDescriptionItem.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
                model.setDescriptionSelected(item.isSelected());
            }
        });
    }
    
    /** Builds and lays out the GUI. */
    private void buildGUI()
    {
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        add(inNameItem);
        add(inDescriptionItem);
    }
    
    /**
     * Creates a new instance.
     * 
     * @param model Reference to the {@link FindPane}.
     */
    FindPopupMenu(FindPane model)
    {
        if (model == null) throw new NullPointerException("No model.");
        this.model = model;
        createMenuItems();
        buildGUI();
    }
    
}
