package io.github.williamyin2024.fleetmanager.positiontracker.repository;

import io.github.williamyin2024.fleetmanager.model.Vehicle;
import io.github.williamyin2024.fleetmanager.model.VehiclePosition;
import io.github.williamyin2024.fleetmanager.positiontracker.exception.TelemetryServiceUnavailableException;
import io.github.williamyin2024.fleetmanager.positiontracker.exception.VehicleNotFoundException;
import io.github.williamyin2024.fleetmanager.positiontracker.externalservices.VehicleTelemetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

@Repository
public class PositionRepositoryInMemoryImpl implements PositionRepository {
	private Map<String, TreeSet<VehiclePosition>> positionDatabase;

	@Autowired
	private VehicleTelemetryService telemetryService;

	public PositionRepositoryInMemoryImpl() {
		positionDatabase = new HashMap<>();
	}

	@Override
	public void updatePosition(VehiclePosition data) {
		String vehicleName = data.getName();
		TreeSet<VehiclePosition> positions = positionDatabase.computeIfAbsent(vehicleName, k -> new TreeSet<>());

		VehiclePosition vehicleWithSpeed;
		try {
			BigDecimal speed = telemetryService.getSpeedFor(vehicleName);
			vehicleWithSpeed = new Vehicle.Builder().withVehiclePostion(data).withSpeed(speed).withTimestamp(new Date()).build();
		} catch (TelemetryServiceUnavailableException e) {
			vehicleWithSpeed = new Vehicle.Builder().withVehiclePostion(data).withTimestamp(new Date()).build();
		}
		positions.add(vehicleWithSpeed);
		telemetryService.updateData(data);
	}

	@Override
	public VehiclePosition getLatestPositionFor(String vehicleName) throws VehicleNotFoundException {
		TreeSet<VehiclePosition> reportsForThisVehicle = positionDatabase.get(vehicleName);
		if (reportsForThisVehicle == null) throw new VehicleNotFoundException();
		return reportsForThisVehicle.first();
	}

	@Override
	public void addAllReports(VehiclePosition[] allReports) {
		for (VehiclePosition next: allReports) {
			this.updatePosition(next);
		}
	}

	@Override
	public Set<VehiclePosition> getLatestPositionsOfAllVehicles() {
		Set<VehiclePosition> results = new HashSet<>();

		for (TreeSet<VehiclePosition> reports: this.positionDatabase.values()) {
			if (!reports.isEmpty()) results.add(reports.first());
		}
		return results;
	}

	public Collection<VehiclePosition> getHistoryFor(String vehicleName) throws VehicleNotFoundException {
		return this.positionDatabase.get(vehicleName);
	}

	public void reset() {
		positionDatabase.clear();
	}
}
