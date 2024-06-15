package io.github.williamyin2024.fleetmanager.positiontracker.repository;

import io.github.williamyin2024.fleetmanager.model.VehiclePosition;
import io.github.williamyin2024.fleetmanager.positiontracker.exception.VehicleNotFoundException;

import java.util.Collection;

public interface PositionRepository {
	void updatePosition(VehiclePosition position);

	VehiclePosition getLatestPositionFor(String vehicleName) throws VehicleNotFoundException;

	void addAllReports(VehiclePosition[] allReports);

	Collection<VehiclePosition> getLatestPositionsOfAllVehicles();

	Collection<VehiclePosition> getHistoryFor(String vehicleName) throws VehicleNotFoundException;

	void reset();
}
