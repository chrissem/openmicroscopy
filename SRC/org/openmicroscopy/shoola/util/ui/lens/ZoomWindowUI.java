
/*
 * org.openmicroscopy.shoola.util.ui.lens.ZoomWindowUI.java
 *
 *------------------------------------------------------------------------------
 *
 *  Copyright (C) 2004 Open Microscopy Environment
 *      Massachusetts Institute of Technology,
 *      National Institutes of Health,
 *      University of Dundee
 *
 *
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
 *
 *------------------------------------------------------------------------------
 */
package org.openmicroscopy.shoola.util.ui.lens;

//Java imports
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

//Third-party libraries

//Application-internal dependencies

/** 
 * The ZoomWindowUI is the dialog box used to display the zoomed image. It 
 * contains the zoomPanel which displays the zoomed image and the coordinates 
 * of the lens.  
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 	<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author	Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp;
 * 	<a href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $Date: $)
 * </small>
 * @since OME2.2
 */
public class ZoomWindowUI 
	extends	JFrame 
{

	/** Panel holding the zoomed Image */
	private ZoomPanel		zoomPanel;
	
	/** ScrollPane which will contain the zoomPanel which in turn holds the 
	 * zoomed image.
	 */
	private JScrollPane		scrollPanel;
	
	/** The statusPanel shows the position, size and zoomFactor of the lens. */
	private StatusPanel		statusPanel;
	
	/** Parent component of the lens and zoomWindowUI. */
	private LensComponent		lensComponent;
	
	/** Lens menu bar. */
	private JMenuBar			menu;
	
	/** Lens options popup menu. */
	private LensMenu			lensMenu;
	
	/** Constructor of the ZoomWindowUI 
	 * @param lensComponent the parent component of the control.
	 */
	ZoomWindowUI(LensComponent lensComponent)
	{
		this.lensComponent = lensComponent;
		setTitle("Zoom Window");
		setSize(300,300);
		setLocation(900,200);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener( new WindowAdapter() 
		{
			public void windowClosing( WindowEvent e )
			{
				close();
			}
		});
		
		zoomPanel = new ZoomPanel();
		scrollPanel = new JScrollPane(zoomPanel);
		statusPanel = new StatusPanel();
		this.setLayout(new BorderLayout());
		this.add(scrollPanel, BorderLayout.CENTER);
		this.add(statusPanel, BorderLayout.SOUTH);
		lensMenu =  LensMenu.getMenu(lensComponent);
		menu = lensMenu.getMenubar();
		this.setJMenuBar(menu);
	}
	
	private void close()
	{
		lensComponent.zoomWindowClosed();
	}
	
	/** 
	 * Sets the size of the zoomedImage panel, called after the zoomfactor
	 * or lens has chaned. 
	 * 
	 * @param w new width
	 * @param h new height
	 */
	void setZoomedImageSize(int w, int h)
	{
		zoomPanel.setSize(new Dimension(w, h));
		zoomPanel.setPreferredSize(new Dimension(w, h));
		zoomPanel.setMinimumSize(new Dimension(w, h));
		zoomPanel.setMaximumSize(new Dimension(w, h));
		JViewport currentView = scrollPanel.getViewport();
        int viewportW = currentView.getWidth();
	    int viewportH = currentView.getHeight();
	    int x = w/2-viewportW/2;
	    if( x<0 )
	       	x = 0;
	    int y = h/2-viewportH/2;
	    if( y<0 )
	       	y = 0;
	        
	    currentView.setViewPosition(new Point(x, y));
	}
	
	/**
	 * Display in pixels if <code>true</code> or in microns otherwise.
	 * 
	 * @param b see above.
	 */
	void setDisplayInPixels(boolean b)
	{
		statusPanel.setDisplayInPixels(b);
		statusPanel.repaint();
	}

	/**
	 * Set the mapping from pixel size to microns along the x and y axis. 
	 * @param x mapping in x axis.
	 * @param y mapping in y axis.
	 */
	public void setXYPixelMicron(float x, float y)
	{
		statusPanel.setXYPixelMicron(x, y);
		statusPanel.repaint();
	}
	
	/** Set the XY values of the lens position text.
	 * 
	 * @param x See above.
	 * @param y See above.
	 */
	void setLensXY(int x,int y)
	{
		statusPanel.setLensXY(x, y);
	}
	
	/** Set the W, H values of the lens width, Height text.
	 * 
	 * @param w See above.
	 * @param h See above.
	 */
	void setLensWidthHeight(int w, int h)
	{
		statusPanel.setLensWidthHeight(w, h);
	}
		
	/** Set the zoomFactor of the lens.
	 * 
	 * @param zoomFactor See above.
	 */
	void setLensZoomFactor(float zoomFactor)
	{
		statusPanel.setLensZoomFactor(zoomFactor);
	}
	
	/**
	 * Set the image shown on the zoomWindow.
	 * 
	 * @param zoomImage See above.
	 */
	void setZoomImage(BufferedImage zoomImage)
	{
		zoomPanel.setZoomImage(zoomImage);
	}
	
}
