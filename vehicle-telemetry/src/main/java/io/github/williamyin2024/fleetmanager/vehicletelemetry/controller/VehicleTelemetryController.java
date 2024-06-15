package io.github.williamyin2024.fleetmanager.vehicletelemetry.controller;

import io.github.williamyin2024.fleetmanager.model.VehiclePosition;
import io.github.williamyin2024.fleetmanager.vehicletelemetry.service.VehicleTelemetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class VehicleTelemetryController {
	@Autowired
	private VehicleTelemetryService vehicleTelemetryService;

	@RequestMapping(method=RequestMethod.POST, value="/vehicles/")
	public void updateData(@RequestBody VehiclePosition data) {
		this.vehicleTelemetryService.updateData(data);
	}

	@RequestMapping(method=RequestMethod.GET, value="/vehicles/{vehicleName}")
	public BigDecimal getSpeedFor(@PathVariable("vehicleName") String vehicleName) {
		return this.vehicleTelemetryService.getSpeedFor(vehicleName);
	}
}
