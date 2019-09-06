/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package org.matsim.vehicles;


import org.matsim.utils.objectattributes.attributable.Attributable;
import org.matsim.utils.objectattributes.attributable.Attributes;

/**
 * @author dgrether
 */
public final class VehicleCapacity implements Attributable {
	// maybe at least these subtypes should be immutable?
	// --> no, we make them fully settable, "data base in memory".  This has been the matsim design from the beginning, and all attempts to deviate from it
	// always seem to lead to awkward code.  If parallel computing pieces need immutable data structures, they first need to copy them from the in-memory
	// data base.  kai, sep'19

	private Integer seats = 1; // one seat for the driver
	private Integer standingRoom = 0 ;
	private Double volumeInCubicMeters = Double.MAX_VALUE ; // not an active constraint; infty not possible by xsd
	private Double weightInTons = Double.MAX_VALUE ; // not an active constraint; infty not possible by xsd
//	private FreightCapacity freightCap = null;
	private Attributes attributes = new Attributes() ;

	public VehicleCapacity(){
		// yyyy leaving this public for the time being because otherwise the carrier vehicle type reading logic needs to change completely.  kai, sep'19
	}

	public Integer getSeats() {
		return seats;
	}

	public Integer getStandingRoom() {
		return standingRoom;
	}

	public void setSeats(Integer seats) {
		this.seats = seats;
	}

	public void setStandingRoom(Integer standingRoom) {
		this.standingRoom = standingRoom;
	}

	public Double getVolumeInCubicMeters() {
		return volumeInCubicMeters;
	}

	public void setVolumeInCubicMeters(double volumeInCubicMeters) {
		this.volumeInCubicMeters = volumeInCubicMeters;
	}

	public Double getWeightInTons() {
		return weightInTons;
	}

	public void setWeightInTons(double weightInTons) {
		this.weightInTons = weightInTons;
	}

	public Attributes getAttributes(){
		return this.attributes ;
	}

//	public void setFreightCapacity(FreightCapacity freightCapacity) {
//		this.freightCap = freightCapacity;
//	}
//	public FreightCapacity getFreightCapacity() {
//		return freightCap;
//	}
	// (these are no longer there; use capacity.get/setVolume/Weight directly, or use getAttributes. kai/kai, aug'19)

}
