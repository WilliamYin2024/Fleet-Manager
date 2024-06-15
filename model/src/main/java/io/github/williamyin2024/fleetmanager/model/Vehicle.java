package io.github.williamyin2024.fleetmanager.model;

import java.math.BigDecimal;
import java.util.Date;

public class Vehicle {
	private String name;
	private BigDecimal lat;
	private BigDecimal lng;
	private Date timestamp;
	private BigDecimal speed;

	private Vehicle(Builder builder) {
		this.name = builder.name;
		this.lat = builder.lat;
		this.lng = builder.lng;
		this.timestamp = builder.timestamp;
		this.speed = builder.speed;
	}

	public static class Builder {
		private String name;
		private BigDecimal lat;
		private BigDecimal lng;
		private Date timestamp;
		private BigDecimal speed;

		public Builder withTimestamp(Date date) {
			this.timestamp = date;
			return this;
		}

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withLat(BigDecimal lat) {
			this.lat = lat;
			return this;
		}

		public Builder withLng(BigDecimal lng) {
			this.lng = lng;
			return this;
		}

		public Builder withSpeed(BigDecimal speed) {
			this.speed = speed;
			return this;
		}

		public VehiclePosition build() {
			return new VehiclePosition(name, lat, lng, timestamp, speed);
		}

		public Builder withLat(String lat) {
			return this.withLat(new BigDecimal(lat));
		}

		public Builder withLng(String lng) {
			return this.withLng(new BigDecimal(lng));
		}

		public Builder withVehiclePostion(VehiclePosition data) {
			this.name = data.getName();
			this.lat = data.getLat();
			this.lng = data.getLng();
			this.timestamp = data.getTimestamp();
			this.speed = data.getSpeed();

			return this;
		}
	}
}
