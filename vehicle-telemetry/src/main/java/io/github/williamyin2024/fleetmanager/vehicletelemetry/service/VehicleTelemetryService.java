package io.github.williamyin2024.fleetmanager.vehicletelemetry.service;

import io.github.williamyin2024.fleetmanager.model.VehiclePosition;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalPosition;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

@Service
public class VehicleTelemetryService {

	private final GeodeticCalculator geoCalc = new GeodeticCalculator();
	private static final BigDecimal MPS_TO_MPH_FACTOR = new BigDecimal("2.236936");
	private static final int REPORTS_TO_SMOOTH = 10;
	private final Map<String, Deque<VehiclePosition>> vehicleCache;

	public VehicleTelemetryService() {
		this.vehicleCache = new HashMap<>();
	}

	private BigDecimal calculateSpeedInMph(String vehicleName) {
		Deque<VehiclePosition> positions = vehicleCache.get(vehicleName);
		if (positions == null || positions.size() < 2) return null;

		VehiclePosition posA = positions.getFirst();
		VehiclePosition posB = positions.getLast();

		long timeAinMillis = posA.getTimestamp().getTime();
		long timeBinMillis = posB.getTimestamp().getTime();
		long timeInMillis = timeBinMillis - timeAinMillis;
		if (timeInMillis == 0) return new BigDecimal("0");

		BigDecimal timeInSeconds = new BigDecimal(timeInMillis / 1000.0);

		GlobalPosition pointA = new GlobalPosition(posA.getLat().doubleValue(), posA.getLng().doubleValue(), 0.0);
		GlobalPosition pointB = new GlobalPosition(posB.getLat().doubleValue(), posB.getLng().doubleValue(), 0.0);

		double distance = geoCalc.calculateGeodeticCurve(Ellipsoid.WGS84, pointA, pointB).getEllipsoidalDistance(); // Distance between Point A and Point B
		BigDecimal distanceInMetres = new BigDecimal (""+ distance);

		BigDecimal speedInMps = distanceInMetres.divide(timeInSeconds, RoundingMode.HALF_UP);
		BigDecimal milesPerHour = speedInMps.multiply(MPS_TO_MPH_FACTOR);

		if (milesPerHour.doubleValue() > 70) {
			milesPerHour = BigDecimal.valueOf((Math.random() * 80) + 30);
		}

		return milesPerHour;
	}

	public void updateData(VehiclePosition update) {
		Deque<VehiclePosition> vehicleReports = this.vehicleCache.get(update.getName());
		if (vehicleReports == null) {
			vehicleReports = new LinkedBlockingDeque<>(REPORTS_TO_SMOOTH);
			this.vehicleCache.put(update.getName(), vehicleReports);
		}
		try {
			vehicleReports.add(update);
		}
		catch (IllegalStateException e) {
			vehicleReports.removeFirst();
			vehicleReports.add(update);
		}
	}

	public BigDecimal getSpeedFor(String vehicleName) {
		return this.calculateSpeedInMph(vehicleName);
	}
}
