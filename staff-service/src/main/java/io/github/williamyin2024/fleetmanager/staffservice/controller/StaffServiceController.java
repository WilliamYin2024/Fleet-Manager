package io.github.williamyin2024.fleetmanager.staffservice.controller;

import io.github.williamyin2024.fleetmanager.model.StaffRecord;
import io.github.williamyin2024.fleetmanager.staffservice.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StaffServiceController {
	@Autowired
	private StaffService staffService;

	@RequestMapping(method= RequestMethod.GET, value="/driver/{vehicleName}", produces="application/json")
	public StaffRecord getDriverAssignedTo(@PathVariable String vehicleName) {
		return staffService.getDriverDetailsFor(vehicleName);
	}
}
