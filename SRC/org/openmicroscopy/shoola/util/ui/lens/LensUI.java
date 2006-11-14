/*
 * org.openmicroscopy.shoola.util.ui.lens.LensUI.java
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
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

//Third-party libraries

//Application-internal dependencies

/** 
 * Magnifing lens, will show a lens on the image and link into a second window
 * which will display the magnified image.
 * 
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
public class LensUI 
	extends JPanel
{
	
	/** Pick size of the border, this is a border running <code>BORDER_PICK_SIZE
	 * </code> units around the edge of the lens. 
	 */
	private int 				borderPickSize = 8;	
	
	/** Constants defining the border being picked. Where
	 *  +-----+=====+-----+
	 *  | ^^^   ^^^   ^^^ |
	 *  | <NW    N    NE  |
	 *  |                 |
	 *  |                 |
	 *  |                 |
	 *  +                 +
	 *  $				  $  
	 *  $				  $  
	 *  $<W	 		    E>$  
	 *  $				  $  
	 *  $				  $  
	 *  +                 +
	 *  |                 |
	 *  |                 |
	 *  |                 |
	 *  |                 |
	 *  |                 |
     *  +-----+=====+-----+
     *    ^^^   ^^^   ^^^
     *     SW    S     SE
     *  are passed back by {@link #getPickDir(int, int)} return the border area
     *  picked by the user.
     *  
     *  Constant for North border Pick.
	 */ 
	final static int			NORTH = 0;

    /**  Constant for North west border Pick. */
    final static int			NORTH_WEST = 1;

    /**  Constant for North east border Pick. */
	final static int			NORTH_EAST = 2;
	
    /**  Constant for South border Pick. */
	final static int			SOUTH = 3;
	
    /**  Constant for South west border Pick. */
	final static int			SOUTH_WEST = 4;
	
    /**  Constant for South east border Pick. */
	final static int			SOUTH_EAST = 5;
	
    /**  Constant for East border Pick. */
	final static int			EAST = 6;
	
    /**  Constant for West border Pick. */
	final static int			WEST = 7;
	
	/** Minimum height of the lens. This will constrain the size of the lens 
	 * when the user resizes it using the border picking. */
	final static int 			MINIMUM_HEIGHT = 20;
	
	/** Maximum height of the lens. This will constrain the size of the lens 
	 * when the user resizes it using the border picking. */
	final static int			MAXIMUM_HEIGHT = 150;
	
	/** Minimum width of the lens. This will constrain the size of the lens 
	 * when the user resizes it using the border picking. */
	final static int			MINIMUM_WIDTH = 20;
	
	/** Maximum width of the lens. This will constrain the size of the lens 
	 * when the user resizes it using the border picking. */
	final static int			MAXIMUM_WIDTH = 150;
	
	/** When the width or height of the lens is this size, snap the cross hair 
	 * to SNAPPED_CROSSHAIR length; 
	 */
	final static int			CROSSHAIR_SNAP = 30;
	
	/** size of the crosshair when snapped. */
	final static int			SNAPPED_CROSSHAIR = 6;

	/** size of the crosshair when nto snapped. */
	final static int			UNSNAPPED_CROSSHAIR = 8;
	
	/** size of the crosshair, running from centre of lens. */
	private int 				crosshairLength = UNSNAPPED_CROSSHAIR;
	
	/** size of the visble crosshair. */
	private int 				crosshairTick = crosshairLength/2-1;
	
	/** Colour of the frame of the lens */
	private Color 				lensBorderColour = 
								new Color(128, 128, 128, 240);
	
	/** Colour of the lens cross hair. */
	private Color 				lensCrossHairColour = 
								new Color(194, 194, 194, 220);

	/** LensUI Controller.	 */
	private LensController		lensController;
	
	/** Show the cross hair on the lens */
	private boolean 			showCrossHair;
	
	/** Mouselistener for the lens. */
	private LensMouseListener	mouseListener;
	
	
	/** Parent component of the lens and zoomWindowUI. */
	private LensComponent		lensComponent;
	
	/** Lens options popup menu. */
	private LensMenu			lensMenu;
	
	/** lens popupMenu. */
	private JPopupMenu			menu;

	/** The amount of zooming in the original image. */
	private float imageZoomFactor;
	
	/** 
	 * Constructor for the lens control. Will set initial width and height of
	 * the LensUI. 
	 * 
	 * @param lensComponent Parent component of the lens.  
	 * @param w Width of the LensUI. 
	 * @param h Height of the lens.
	 */
	LensUI(LensComponent lensComponent, int w, int h)
	{
		this.lensComponent = lensComponent;
		setSize(new Dimension(w,h));
		setOpaque(false);
		setShowCrossHair(true);
		mouseListener = new LensMouseListener(this);
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
		addMouseWheelListener(mouseListener);
		lensMenu =  LensMenu.getMenu(lensComponent);
		menu = lensMenu.getPopupMenu();
	}
	
	
	/**
	 * Overridden,
	 * Show the lens frame and depending on settings the crosshairs.
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics og)
	{
		super.paintComponent(og);
		Graphics2D g = (Graphics2D) og;
		g.setStroke(new BasicStroke(2.0f));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
								RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(lensBorderColour);
		
		g.drawRect(1, 1, getWidth()-3, getHeight()-3);
		if( showCrossHair)
		{
			g.setStroke(new BasicStroke(1.0f));
			g.setColor(lensCrossHairColour);
			g.drawLine((getWidth()-crosshairTick+1)/2, 
					(getHeight()-crosshairTick+1)/2-crosshairLength, 
					(getWidth()-crosshairTick+1)/2, 
					(getHeight()-crosshairTick+1)/2-crosshairTick);
			g.drawLine((getWidth()-crosshairTick+1)/2, 
					(getHeight()-crosshairTick+1)/2+crosshairTick, 
					(getWidth()-crosshairTick+1)/2, 
					(getHeight()-crosshairTick+1)/2+crosshairLength);
			g.drawLine((getWidth()-crosshairTick+1)/2-crosshairLength, 
					(getHeight()-crosshairTick+1)/2, 
					(getWidth()-crosshairTick+1)/2-crosshairTick, 
					(getHeight()-crosshairTick+1)/2);
			g.drawLine((getWidth()-crosshairTick+1)/2+crosshairTick, 
					(getHeight()-crosshairTick+1)/2, 
					(getWidth()-crosshairTick+1)/2+crosshairLength, 
					(getHeight()-crosshairTick+1)/2);
		}
	}
	
	/**
	 * Set the location of the lens to the coordinates (x,y)
	 * 
	 * @param x see above. 
	 * @param y see above.
	 */
	public void setLocation(int x, int y)
	{
		this.setBounds(x, y, getWidth(), getHeight());
	}
	
	/**
	 * Set the bounds of the lens; x,y position and width and height. 
	 * If the lens is too small shrink crosshairs. 
	 * 
	 * @param x x-coordinate.
	 * @param y y-coordinate.
	 * @param w width of lens.
	 * @param h height of lens.
	 */
	public void setBounds(int x, int y, int w, int h) 
	{
		super.setBounds(x, y, w, h);
		setPickParam(w, h);
	}
	
	/**
	 * Overridden, 
	 * Allows the panel to change size and the lens to resize with it
	 * @see java.awt.Component#setSize(int, int)
	 */
	public void setSize(int w, int h)
	{
		super.setSize(w, h);
		setPickParam(w, h);
	}
	
	/**
	 * Overridden, 
	 * Allows the panel to change size and the lens to resize with it
	 * @see java.awt.Component#setSize(Dimension)
	 */
	public void setSize(Dimension d)
	{
		super.setSize(d);
		setPickParam(d.width, d.height);
	}

	/**
	 * Set the prefered size of the lens to (w, h) and if needed shrink the 
	 * lens crosshairs. 
	 * 
	 * @param w see above. 
	 * @param h see above/ 
	 */
	void setPreferredSize(int w, int h)
	{
		super.setPreferredSize(new Dimension(w, h));
		setPickParam(w, h);
	}
	
	/**
	 * Returns <code>true</code> if the lens has been picked, this is the area
	 * inside the border. 
	 * 
	 * +--------+
	 * | Border |
	 * | +----+ |
	 * | |Lens| |
	 * | +----+ |
	 * |        |
	 * +--------+
	 * 
	 * @param x mouse X position inside the lens.
	 * @param y mouse Y position inside the lens.
	 * 
	 * @return see above.
	 */
	boolean lensPicked(int x, int y)
	{
		Rectangle rect = new Rectangle(borderPickSize, borderPickSize, 
				this.getWidth()-borderPickSize*2, 
				this.getHeight()-borderPickSize*2);
		return rect.contains(x, y);
	}

	/**
	 * Set the image zoom factor. The image in the viewer has been zoomed by
	 * this number.
	 * 
	 * @param imageZoomFactor the amount of zooming that has occurred on the 
	 * image. 
	 */
	public void setImageZoomFactor(float imageZoomFactor)
	{
		this.imageZoomFactor = imageZoomFactor;
		this.setSize(lensComponent.getLensScaledSize());
		this.setLocation(lensComponent.getLensScaledLocation().x, 
				lensComponent.getLensScaledLocation().y);
	}
	
	/**
	 * Returns <code>true</code> if the mouse event at x,y is inside the border.
	 *  
	 * +--------+
	 * | Border |
	 * | +----+ |
	 * | |Lens| |
	 * | +----+ |
	 * |        |
	 * +--------+
	 * 
	 * @param x mouse X position inside the lens.
	 * @param y mouse Y position inside the lens.
	 * 
	 * @return see above.
	 */
	boolean lensBorderPicked(int x, int y)
	{
		Rectangle rect = new Rectangle(borderPickSize, borderPickSize, 
				this.getWidth()-borderPickSize*2, 
				this.getHeight()-borderPickSize*2);
		Rectangle rectBorder = new Rectangle(0, 0, 
				this.getWidth(), this.getHeight());
		return (!rect.contains(x, y) && rectBorder.contains(x, y)); 
	}
	
	/**
	 * Show or hide the cross hairs in the lens. 
	 * 
	 * @param show see above.
	 */
	void setShowCrossHair(boolean show)
	{
		showCrossHair = show;
	}
	
	/**
	 * Mouse down event triggered from lens mouse listener.
	 * 
	 * @param x x mouse position.
	 * @param y y mouse position.
	 */
	void mouseMoved(int x, int y)
	{
		lensController.lensMouseMoved(x, y);
	}
	
	/**
	 * Mouse down event triggered from lens mouse listener.
	 * 
	 * @param x x mouse position.
	 * @param y y mouse position.
	 */
	void mouseDown(int x, int y)
	{
		lensController.lensMouseDown(x, y);
	}

	/**
	 * Mouse drag event triggered from lens, if the shift key is pressed 
	 * dX and dY will be the same; equal to which ever one is greater, this 
	 * is used to keep the lens square when shift pressed. 
	 * 
	 * @param x x mouse position.
	 * @param y y mouse position.
	 * @param shift true if the shift key is pressed. 
	 */
	void mouseDrag(int x, int y, boolean shift)
	{
		lensController.lensMouseDrag(x, y, shift);
	}
	
	/**
	 * Mouse down event triggered from lens mouse listener.
	 * 
	 * @param x x mouse position.
	 * @param y y mouse position.
	 */
	void mouseUp(int x, int y)
	{
		lensController.lensMouseUp(x, y);
	}
	
	/**
	 * MouseWheel moved by tick units. 
	 * 
	 * @param tick
	 */
	void mouseWheelMoved(int tick)
	{
		lensController.lensMouseWheelMoved(tick);
	}
	
	/** Show popup menu. At (x,y).
	 * @param x see above. 
	 * @param y see above.
	 * */
	void showMenu(int x, int y)
	{
		menu.show(this, x, y);
	}
	
	/**
	 * Attach lens to controller.
	 *  
	 * @param lensController
	 */
	void addController(LensController lensController)
	{
		this.lensController = lensController;
	}
	
	/** 
	 * Returns the constant defining where in the border the user cliked.
	 *  +-----+=====+-----+
	 *  | ^^^   ^^^   ^^^ |
	 *  | <NW    N    NE  |
	 *  |                 |
	 *  |                 |
	 *  |                 |
	 *  +                 +
	 *  $				  $  
	 *  $				  $  
	 *  $<W	 		    E>$  
	 *  $				  $  
	 *  $				  $  
	 *  +                 +
	 *  |                 |
	 *  |                 |
	 *  |                 |
	 *  |                 |
	 *  |                 |
     *  +-----+=====+-----+
     *    ^^^   ^^^   ^^^
     *     SW    S     SE
     *  
     *  @param x mouse x position in the border.
     *  @param y mouse y position in the border.
     *  
     *  @return see above.
	 */ 
	int getPickDir(int x, int y)
	{
		int resizeDir=-1;
		int resizeCornerSize = 0;
		
		  if (x <= borderPickSize) {
              if (y < resizeCornerSize + borderPickSize) 
            	  resizeDir = NORTH_WEST;
              else if (y > this.getHeight()-resizeCornerSize-borderPickSize)
                  resizeDir = SOUTH_WEST;
              else resizeDir = WEST;
          } else if (x >= this.getWidth()-borderPickSize) {
              if (y < resizeCornerSize+borderPickSize) resizeDir = NORTH_EAST;
              else if (y > this.getHeight()-resizeCornerSize-borderPickSize)
                  resizeDir = SOUTH_EAST;
              else resizeDir = EAST;
          } else if (y <= borderPickSize) {
              if (x < resizeCornerSize+borderPickSize) resizeDir = NORTH_WEST;
              else if (x > this.getWidth()-resizeCornerSize-borderPickSize)
                  resizeDir = NORTH_EAST;
              else resizeDir = NORTH;
          } else if (y >= this.getHeight()-borderPickSize) {
              if (x < resizeCornerSize+borderPickSize) resizeDir = SOUTH_WEST;
              else if (x > this.getWidth()-resizeCornerSize-borderPickSize)
                  resizeDir = SOUTH_EAST;
              else resizeDir = SOUTH;	
          }
		  return resizeDir;
	}
	
	private void setPickParam(int w, int h)
	{
		if (Math.min(w, h) < CROSSHAIR_SNAP)
		{
			crosshairLength = 6;
			borderPickSize = 4;
		}
		else
		{
			crosshairLength = 8;
			borderPickSize = 8;
		}
		crosshairTick = crosshairLength/2-1;
	}
	
}
