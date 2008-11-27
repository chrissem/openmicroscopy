/*
 * org.openmicroscopy.shoola.util.roi.model.util.Coord3D 
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
package org.openmicroscopy.shoola.util.roi.model.util;

//Java imports
import java.util.Comparator;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.util.roi.model.util.Coord3D;

/** 
 * Defines a plane. Rename??
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 	<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author	Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp;
 * 	<a href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $Date: $)
 * </small>
 * @since OME3.0
 */
public class Coord3D 
	implements Comparator
{
	/** Offset for z in calculating hashcode. This currently splits z and t
	 * range into 16384 zsections, and 262144 t sections.*/
	final static int ZTBITSPLIT = 18;
	
	/** The timepoint. */
	private int t;
	
	/** The z-section. */
	private int z;
	
	/** Creates a default point. */
	public Coord3D()
	{
		this(0, 0);
	}
	
	/**
	 * Creates a new instance.
	 * 
	 * @param zsec	The z-section.
	 * @param time	The timepoint..
	 */
	public Coord3D(int zsec, int time)
	{
		if (zsec < 0 || time < 0)
			throw new IllegalArgumentException("Coordinates not valid.");
		t = time;
		z = zsec;
	}
	
	/**
	 * Returns the timepoint.
	 * 
	 * @return See above.
	 */
	public int getTimePoint() { return t; }
	
	/**
	 * Returns the z-section.
	 * 
	 * @return See above.
	 */
	public int getZSection() { return z; }
	
	/**
	 * Overridden to control if the passed object equals the current one.
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object obj)
	{
		if (!(obj instanceof Coord3D)) return false;
		Coord3D comp = (Coord3D) obj;
		return (comp.t == this.t && comp.z == this.z);
	}

	/**
	 * Overridden to compare the passed objects.
	 * @see Comparator#compare(Object, Object)
	 */
	public int compare(Object o1, Object o2) 
	{
		if (!(o1 instanceof Coord3D) || !(o2 instanceof Coord3D))
			return -1;
		Coord3D a = (Coord3D) o1;
		Coord3D b = (Coord3D) o2;
		if (a.t < b.t) return -1;
		else if (a.t > b.t) return 1;
		else if (a.z < b.z) return -1;
		else if (a.z > b.z) return 1;
		else return 0;
	}

	/**
	 * Calculate the hashCode for the data, The hashcode is generated by bitshifting
	 * z by ZTBITSPLIT bits and adding t. 
	 * @return see above.
	 */
	public int hashCode()
	{
		int value = z<<ZTBITSPLIT+t;
		return value;
	}
	
	/**
	 * Clones the object.
	 * @see java.lang.Object#clone()
	 */
	public Coord3D clone() { return new Coord3D(this.t, this.z); }

	/**
	 * Returns the string of the coord.
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "T: " + t + " Z: " + z;
	}
	
}


