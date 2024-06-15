package io.github.williamyin2024.fleetmanager.apigateway.controller;

import io.github.williamyin2024.fleetmanager.apigateway.externalservices.PositionTrackingService;
import io.github.williamyin2024.fleetmanager.model.LatLng;
import io.github.williamyin2024.fleetmanager.model.VehiclePosition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
public class VehicleController  {
	@Autowired
	private PositionTrackingService positionTrackingService;

	@GetMapping("/history/{vehicleName}")
	@ResponseBody
	@CrossOrigin(origins = "*")
	public Collection<LatLng> getHistoryFor(@PathVariable("vehicleName") String vehicleName) {
		Collection<LatLng> results = new ArrayList<>();
		Collection<VehiclePosition> vehicles = positionTrackingService.getHistoryFor(vehicleName);
		for (VehiclePosition next : vehicles) {
			LatLng position = new LatLng(next.getLat(), next.getLng());
			results.add(position);
		}
		Collections.reverse((List<?>) results);
		return results;
	}

	@GetMapping("/vehicles/{vehicleName}")
	@ResponseBody
	@CrossOrigin(origins = "*")
	public VehiclePosition getLastReportFor(@PathVariable("vehicleName") String vehicleName) {
		return positionTrackingService.getLastReportFor(vehicleName);
	}

	@GetMapping("/vehicles/driver/{vehicleName}")
	@ResponseBody
	@CrossOrigin(origins = "*")
	public String getDriverFor(@PathVariable("vehicleName") String vehicleName) {
		return positionTrackingService.getDriverFor(vehicleName);
	}

	@GetMapping("/vehicles/")
	@ResponseBody
	@CrossOrigin(origins = "*")
	public Collection<VehiclePosition> getAllVehiclePositions() {
		Collection<VehiclePosition> results = positionTrackingService.getAllPositions();
		return results;
	}
}
