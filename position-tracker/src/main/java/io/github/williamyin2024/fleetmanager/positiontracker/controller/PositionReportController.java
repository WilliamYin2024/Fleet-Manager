package io.github.williamyin2024.fleetmanager.positiontracker.controller;

import io.github.williamyin2024.fleetmanager.model.VehiclePosition;
import io.github.williamyin2024.fleetmanager.positiontracker.exception.VehicleNotFoundException;
import io.github.williamyin2024.fleetmanager.positiontracker.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
public class PositionReportController {
	@Autowired
	private PositionRepository positionRepository;

	@RequestMapping(method= RequestMethod.GET,value="/vehicles/{vehicleName}")
	public VehiclePosition getLatestReportForVehicle(@PathVariable String vehicleName) throws VehicleNotFoundException {
		VehiclePosition position = positionRepository.getLatestPositionFor(vehicleName);
		return position;
	}

	@RequestMapping(method=RequestMethod.GET, value="/history/{vehicleName}")
	public Collection<VehiclePosition> getEntireHistoryForVehicle(@PathVariable("vehicleName") String vehicleName) throws VehicleNotFoundException {
		return this.positionRepository.getHistoryFor(vehicleName);
	}

	@RequestMapping(method=RequestMethod.GET, value="/vehicles/")
	public Collection<VehiclePosition> getUpdatedPositions(@RequestHeader Map<String, String> headers) {
		Collection<VehiclePosition> results = positionRepository.getLatestPositionsOfAllVehicles();
		return results;
	}

}
