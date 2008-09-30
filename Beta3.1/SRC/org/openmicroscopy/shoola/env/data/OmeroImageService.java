/*
 * org.openmicroscopy.shoola.env.data.OmeroImageService
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

package org.openmicroscopy.shoola.env.data;


//Java imports
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Set;

//Third-party libraries

//Application-internal dependencies
import ome.api.IProjection;
import ome.model.core.Pixels;
import ome.model.core.PixelsDimensions;
import omeis.providers.re.data.PlaneDef;
import org.openmicroscopy.shoola.env.data.model.ProjectionParam;
import org.openmicroscopy.shoola.env.rnd.RenderingControl;
import org.openmicroscopy.shoola.env.rnd.RenderingServiceException;
import org.openmicroscopy.shoola.env.rnd.RndProxyDef;

import pojos.ImageData;

/** 
* List of methods to view images or thumbnails.
*
* @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
* 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
* @author	Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp;
* <a href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
* @version 3.0
* <small>
* (<b>Internal version:</b> $Revision: $ $Date: $)
* </small>
* @since OME2.2
*/
public interface OmeroImageService
{
  
	/** Identifies the <code>Maximum intensity</code> projection. */
	public static final int	   MAX_INTENSITY = IProjection.MAXIMUM_INTENSITY;
	
	/** Identifies the <code>Mean intensity</code> projection. */
	public static final int    MEAN_INTENSITY = IProjection.MEAN_INTENSITY;
	
	/** Identifies the <code>Sum intensity</code> projection. */
	public static final int    SUM_INTENSITY = IProjection.SUM_INTENSITY;
	
	/** Identifies the type used to store pixel values. */
	public static final String INT_8 = "int8";

	/** Identifies the type used to store pixel values. */
	public static final String UINT_8 = "uint8";

	/** Identifies the type used to store pixel values. */
	public static final String INT_16 = "int16";

	/** Identifies the type used to store pixel values. */
	public static final String UINT_16 = "uint16";

	/** Identifies the type used to store pixel values. */
	public static final String INT_32 = "int32";

	/** Identifies the type used to store pixel values. */
	public static final String UINT_32 = "uint32";

	/** Identifies the type used to store pixel values. */
	public static final String FLOAT = "float";

	/** Identifies the type used to store pixel values. */
	public static final String DOUBLE = "double";
	
	/**
	 * Initializes a {@link RenderingControl} proxy for the specified pixels
	 * set.
	 * 
	 * @param pixelsID The ID of the pixels set.
	 * @return See above.
	 * @throws DSOutOfServiceException  If the connection is broken, or logged
	 *                                  in.
	 * @throws DSAccessException        If an error occured while trying to 
	 *                                  retrieve data from OMEDS service.
	 */
	public RenderingControl loadRenderingControl(long pixelsID)
		throws DSOutOfServiceException, DSAccessException;

	/**
	 * Renders the specified 2D-plane. 
	 * 
	 * @param pixelsID  The ID of the pixels set.
	 * @param pd        The plane to render.
	 * @return          The buffered image representing the plane.
	 * @throws RenderingServiceException If the server cannot render the image.
	 */
	public BufferedImage renderImage(long pixelsID, PlaneDef pd)
		throws RenderingServiceException;    

	/**
	 * Shuts downs the rendering service attached to the specified 
	 * pixels set.
	 *
	 * @param pixelsID  The ID of the pixels set.
	 */
	public void shutDown(long pixelsID);
	
	/**
	 * Shuts downs the data sink attached to the specified pixels set.
	 *
	 * @param pixelsID  The ID of the pixels set.
	 */
	public void shutDownDataSink(long pixelsID);

	/**
	 * Returns a thumbnail of the currently selected 2D-plane for the
	 * passed pixels set.
	 * 
	 * @param pixelsID  The id of the pixels set.
	 * @param sizeX     The width of the thumbnail.
	 * @param sizeY     The height of the thumnail.
	 * @param userID	The id of the user the thumbnail is for.
	 * @return See above.
	 * @throws RenderingServiceException    If the server cannot retrieve the 
	 *                                      thumbnail.
	 */
	public BufferedImage getThumbnail(long pixelsID, int sizeX, int sizeY, long
										userID)
		throws RenderingServiceException;

	/**
	 * Retrieves the thumbnails corresponding to the passed collection of
	 * pixels set.
	 * 
	 * @param pixelsID	The collection of pixels set.
	 * @param maxLength The maximum length of a thumbnail.
	 * @return See above.
	 * @throws RenderingServiceException
	 */
	public Map<Long, BufferedImage> getThumbnailSet(List pixelsID, 
			                                        int maxLength)
		throws RenderingServiceException;
	
	/**
	 * Reloads the rendering engine for the passed set of pixels.
	 * 
	 * @param pixelsID The id of the pixels set.
	 * @return See above.
	 * @throws RenderingServiceException If the rendering engine cannot be 
	 * 									 started.
	 * @throws DSAccessException        If an error occured while trying to 
	 *                                  retrieve data from OMEDS service.
	 */
	public RenderingControl reloadRenderingService(long pixelsID)
		throws DSAccessException, RenderingServiceException;
	
	/**
	 * Reloads the rendering engine for the passed set of pixels.
	 * 
	 * @param pixelsID The id of the pixels set.
	 * @return See above.
	 * @throws RenderingServiceException If the rendering engine cannot be 
	 * 									 started.
	 * @throws DSAccessException        If an error occured while trying to 
	 *                                  retrieve data from OMEDS service.
	 */
	public RenderingControl resetRenderingService(long pixelsID)
		throws DSAccessException, RenderingServiceException;

	/**
	 * Loads the dimensions in microns of the pixels set.
	 * 
	 * @param pixelsID The id of the pixels set.
	 * @return See above
	 * @throws DSOutOfServiceException  If the connection is broken, or logged
	 *                                  in.
	 * @throws DSAccessException        If an error occured while trying to 
	 *                                  retrieve data from OMEDS service.
	 */
	public PixelsDimensions loadPixelsDimensions(long pixelsID)
		throws DSOutOfServiceException, DSAccessException;

	/**
	 * Loads the pixels set.
	 * 
	 * @param pixelsID The id of the pixels set.
	 * @return See above.
	 * @throws DSOutOfServiceException  If the connection is broken, or logged
	 *                                  in.
	 * @throws DSAccessException        If an error occured while trying to 
	 *                                  retrieve data from OMEDS service.
	 */
	public Pixels loadPixels(long pixelsID)
		throws DSOutOfServiceException, DSAccessException;

	/**
	 * Returns the XY-plane identified by the passed z-section, timepoint 
	 * and wavelength.
	 * 
	 * @param pixelsID 	The id of pixels containing the requested plane.
	 * @param z			The selected z-section.
	 * @param t			The selected timepoint.
	 * @param c			The selected wavelength.
	 * @return See above.
	 * @throws DSOutOfServiceException  If the connection is broken, or logged
	 *                                  in.
	 * @throws DSAccessException        If an error occured while trying to 
	 *                                  retrieve data from OMEDS service.
	 */
	public byte[] getPlane(long pixelsID, int z, int t, int c)
		throws DSOutOfServiceException, DSAccessException;

	/**
	 * Applies the rendering settings associated to the passed pixels set 
	 * to the images contained in the specified datasets or categories
	 * if the rootType is <code>DatasetData</code> or <code>CategoryData</code>.
	 * Applies the settings to the passed images if the type is 
	 * <code>ImageData</code>.
	 * 
	 * @param pixelsID		The id of the pixels set of reference.
	 * @param rootNodeType	The type of nodes. Can either be 
	 * 						<code>ImageData</code>, <code>DatasetData</code> or 
	 * 						<code>CategoryData</code>.
	 * @param nodeIDs		The id of the nodes to apply settings to. 
	 * 						Mustn't be <code>null</code>.
	 * @return A map with two keys. A <code>True</code> key whose value 
	 * is a list of image's id, the settings have been applied to. 
	 * A <code>False</code> key whose value is a list
	 * of image's id, the settings couldn't be applied.
	 * @throws DSOutOfServiceException  If the connection is broken, or logged
	 *                                  in.
	 * @throws DSAccessException        If an error occured while trying to 
	 *                                  retrieve data from OMEDS service.
	 */
	public Map pasteRenderingSettings(long pixelsID, Class rootNodeType,
			Set<Long> nodeIDs)
		throws DSOutOfServiceException, DSAccessException;
	
	/**
	 * Resets the rendering settings for the images contained in the 
	 * specified datasets or categories
	 * if the rootType is <code>DatasetData</code> or <code>CategoryData</code>.
	 * Resets the settings to the passed images if the type is 
	 * <code>ImageData</code>.
	 * 
	 * @param rootNodeType	The type of nodes. Can either be 
	 * 						<code>ImageData</code>, <code>DatasetData</code> or 
	 * 						<code>CategoryData</code>.
	 * @param nodeIDs		The id of the nodes to apply settings to. 
	 * 						Mustn't be <code>null</code>.
	 * @return A map with two keys. A <code>True</code> key whose value 
	 * is a list of image's id, the settings have been applied to. 
	 * A <code>False</code> key whose value is a list
	 * of image's id, the settings couldn't be applied.
	 * @throws DSOutOfServiceException  If the connection is broken, or logged
	 *                                  in.
	 * @throws DSAccessException        If an error occured while trying to 
	 *                                  retrieve data from OMEDS service.
	 */
	public Map resetRenderingSettings(Class rootNodeType, Set<Long> nodeIDs)
		throws DSOutOfServiceException, DSAccessException;
	
	/**
	 * Sets the rendering settings for the images contained in the 
	 * specified datasets or categories
	 * if the rootType is <code>DatasetData</code> or <code>CategoryData</code>.
	 * Resets the settings to the passed images if the type is 
	 * <code>ImageData</code>.
	 * 
	 * @param rootNodeType	The type of nodes. Can either be 
	 * 						<code>ImageData</code>, <code>DatasetData</code> or 
	 * 						<code>CategoryData</code>.
	 * @param nodeIDs		The id of the nodes to apply settings to. 
	 * 						Mustn't be <code>null</code>.
	 * @return A map with two keys. A <code>True</code> key whose value 
	 * is a list of image's id, the settings have been applied to. 
	 * A <code>False</code> key whose value is a list
	 * of image's id, the settings couldn't be applied.
	 * @throws DSOutOfServiceException  If the connection is broken, or logged
	 *                                  in.
	 * @throws DSAccessException        If an error occured while trying to 
	 *                                  retrieve data from OMEDS service.
	 */
	public Map setOriginalRenderingSettings(Class rootNodeType, 
											Set<Long> nodeIDs)
		throws DSOutOfServiceException, DSAccessException;
	
	/**
	 * Retrieves the rendering setting related to a given set of pixels.
	 * Returns a Map whose keys are the experimenter who created the rnd 
	 * settings and the value the settings itself.
	 * 
	 * @param pixelsID The id of the pixels set.
	 * @return See above.
	 * @throws DSOutOfServiceException  If the connection is broken, or logged
	 *                                  in.
	 * @throws DSAccessException        If an error occured while trying to 
	 *                                  retrieve data from OMEDS service.
	 */
	public Map getRenderingSettings(long pixelsID)
		throws DSOutOfServiceException, DSAccessException;
	
	/**
	 * Creates a preview projected image 
	 * 
	 * @param pixelsID  The ID of the pixels set.
	 * @param startZ    The first optical section.
	 * @param endZ      The last optical section.
	 * @param stepping  The stepping used during the projection.
	 * @param type      The type of projection.
     * @param channels The collection of channels to project.
	 * @return The buffered image representing the projected image.
	 * @throws RenderingServiceException If the server cannot render the image.
	 * @throws DSOutOfServiceException  If the connection is broken, or logged
	 *                                  in.
	 */
	public BufferedImage renderProjected(long pixelsID, int startZ, int endZ, 
			 int stepping, int type, List<Integer> channels)
		throws RenderingServiceException, DSOutOfServiceException; 
	
	/**
	 * Projects the specified set of pixels according to the projection's 
	 * parameters. Adds the created image to the passed dataset.
	 * 
	 * @param ref The object hosting the projection's parameters.
	 * @return The newly created image.
	 * @throws DSOutOfServiceException  If the connection is broken, or logged
	 *                                  in.
	 * @throws DSAccessException        If an error occured while trying to 
	 *                                  retrieve data from OMEDS service.
	 */
	public ImageData projectImage(ProjectionParam ref)
		throws DSOutOfServiceException, DSAccessException;

	/**
	 * Creates rendering setting for the specified pixels set and
     * copies the settings from the passed rendering setting object if
     * not <code>null</code>. Returns <code>true</code> if the rendering
     * settings have been successfully created and updated, <code>false</code>
     * otherwise.
     * 
     * @param pixelsID	The id of the pixels set to handle.
     * @param rndToCopy The rendering settings to copy to the newly created one.
     * @param indexes	Collection of channel's indexes. 
     * 					Mustn't be <code>null</code>.
	 * @return See above.
	 * @throws DSOutOfServiceException  If the connection is broken, or logged
	 *                                  in.
	 * @throws DSAccessException        If an error occured while trying to 
	 *                                  retrieve data from OMEDS service.
	 */
	public Boolean createRenderingSettings(long pixelsID, RndProxyDef rndToCopy,
			List<Integer> indexes)
		throws DSOutOfServiceException, DSAccessException;
}
