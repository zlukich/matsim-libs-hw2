package org.matsim.contrib.freight.carrier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Route;


public class Tour {

	public static abstract class TourElement {
		
//		public abstract String getActivityType();
//
//		public abstract Id getLocation();
//
//		public abstract double getDuration();
//		
//		@Deprecated
//		public abstract CarrierShipment getShipment();
//		
//		public abstract TimeWindow getTimeWindow();
		
	};
	
	public static abstract class TourActivity extends TourElement{
		
		public abstract String getActivityType();

		public abstract Id getLocation();

		public abstract double getDuration();

		public abstract TimeWindow getTimeWindow();
		
		public abstract void setExpectedActStart(double startTime);
		
		public abstract double getExpectedActStart();
		
		public abstract void setExpectedArrival(double arrivalTime);
		
		public abstract double getExpectedArrival();

		public abstract void setExpectedActEnd(double currTime);
		
		public abstract double getExpectedActEnd();
	}
	
	public static abstract class ShipmentBasedActivity extends TourActivity {
		public abstract CarrierShipment getShipment();
	}
	
	public static class Leg extends TourElement {
		
		private Route route;
		
		private double expTransportTime;

		private double departureTime;
		
		public Route getRoute(){
			return route;
		}
		
		public void setRoute(Route route){
			this.route = route;
		}

		public double getExpectedTransportTime() {
			return expTransportTime;
		}
		
		public void setExpectedTransportTime(double transportTime){
			this.expTransportTime = transportTime;
		}

		public void setDepartureTime(double currTime) {
			this.departureTime = currTime;
		}
		
		public double getDepartureTime(){
			return departureTime;
		}
	}
	
	public static class Pickup extends ShipmentBasedActivity {

		private CarrierShipment shipment;
		
		private double expActStartTime;
		
		private double expActArrTime;
		
		private double expActEndTime;

		public Pickup(CarrierShipment shipment) {
			this.shipment = shipment;
		}

		@Override
		public String getActivityType() {
			return FreightConstants.PICKUP;
		}

		@Override
		public TimeWindow getTimeWindow() {
			return shipment.getPickupTimeWindow();
		}

		@Override
		public Id getLocation() {
			return shipment.getFrom();
		}

		@Override
		public double getDuration() {
			return shipment.getPickupServiceTime();
		}

		@Override
		public CarrierShipment getShipment() {
			return shipment;
		}

		@Override
		public void setExpectedActStart(double startTime) {
			expActStartTime = startTime;
		}

		@Override
		public double getExpectedActStart() {
			return expActStartTime;
		}

		@Override
		public void setExpectedArrival(double arrivalTime) {
			expActArrTime = arrivalTime;
			
		}

		@Override
		public double getExpectedArrival() {
			return expActArrTime;
		}

		@Override
		public void setExpectedActEnd(double currTime) {
			this.expActEndTime = currTime;
		}

		@Override
		public double getExpectedActEnd() {
			return this.expActEndTime;
			
		}
		
		

	};
	
	public static class Delivery extends ShipmentBasedActivity {

		private CarrierShipment shipment;
		
		private double expActStartTime;
		
		private double expArrTime;

		private double expActEndTime;

		public Delivery(CarrierShipment shipment) {
			this.shipment = shipment;
		}

		@Override
		public TimeWindow getTimeWindow() {
			return shipment.getDeliveryTimeWindow();
		}

		@Override
		public String getActivityType() {
			return FreightConstants.DELIVERY;
		}

		@Override
		public Id getLocation() {
			return shipment.getTo();
		}

		@Override
		public double getDuration() {
			return shipment.getDeliveryServiceTime();
		}

		@Override
		public CarrierShipment getShipment() {
			return shipment;
		}

		@Override
		public void setExpectedActStart(double startTime) {
			expActStartTime = startTime;
		}

		@Override
		public double getExpectedActStart() {
			return expActStartTime;
		}

		@Override
		public void setExpectedArrival(double arrivalTime) {
			expArrTime = arrivalTime;
		}

		@Override
		public double getExpectedArrival() {
			return expArrTime;
		}

		@Override
		public void setExpectedActEnd(double currTime) {
			this.expActEndTime = currTime;
			
		}

		@Override
		public double getExpectedActEnd() {
			return this.expActEndTime;
		}
		
	};
	
	public static class GeneralActivity extends TourActivity {

		private String type;
		
		private Id location;
		
		private Double duration;
		
		private Double earliestStart;
		
		private Double latestStart;
		
		private double expActStartTime;
		
		private double expArrTime;

		private double expActEndTime;
		
		public GeneralActivity(String type, Id location, Double earliestStart, Double latestStart, Double duration) {
			super();
			this.type = type;
			this.location = location;
			this.duration = duration;
			this.earliestStart = earliestStart;
			this.latestStart = latestStart;
		}

		@Override
		public String getActivityType() {
			return type;
		}

		@Override
		public Id getLocation() {
			return location;
		}

		@Override
		public double getDuration() {
			return this.duration;
		}

		@Override
		public TimeWindow getTimeWindow() {
			return new TimeWindow(earliestStart, latestStart);
		}

		@Override
		public void setExpectedActStart(double startTime) {
			expActStartTime = startTime;
		}

		@Override
		public double getExpectedActStart() {
			return expActStartTime;
		}

		@Override
		public void setExpectedArrival(double arrivalTime) {
			expArrTime = arrivalTime;
		}

		@Override
		public double getExpectedArrival() {
			return expArrTime;
		}

		@Override
		public void setExpectedActEnd(double currTime) {
			this.expActEndTime = currTime;
		}

		@Override
		public double getExpectedActEnd() {
			return this.expActEndTime;
		}
		
	}

	private List<TourElement> tourElements;
	
	private Id startLinkId;
	
	private Id endLinkId;
	
	private double earliestDeparture;
	
	private double latestDeparture;

	Tour(Id startLinkId, List<TourElement> tourElements, Id endLinkId) {
		this.startLinkId = startLinkId;
		this.tourElements = Collections.unmodifiableList(tourElements);
		this.endLinkId = endLinkId;
		this.earliestDeparture = 0.0;
		this.latestDeparture = 0.0;
	}

	public List<TourElement> getTourElements() {
		return Collections.unmodifiableList(tourElements);
	}

	public List<CarrierShipment> getShipments() {
		List<CarrierShipment> shipments = new ArrayList<CarrierShipment>();
		for (TourElement tourElement : tourElements) {
			if (tourElement instanceof Pickup) {
				Pickup pickup = (Pickup) tourElement;
				shipments.add(pickup.shipment);
			}
		}
		return shipments;
	}

	public Id getStartLinkId() {
		return startLinkId;
	}

	public Id getEndLinkId() {
		return endLinkId;
	}

	public double getEarliestDeparture() {
		return earliestDeparture;
	}

	public void setEarliestDeparture(double earliestDeparture) {
		this.earliestDeparture = earliestDeparture;
	}

	public double getLatestDeparture() {
		return latestDeparture;
	}

	public void setLatestDeparture(double latestDeparture) {
		this.latestDeparture = latestDeparture;
	}

}
