package io.github.williamyin2024.fleetmanager.positiontracker.controller;

import io.github.williamyin2024.fleetmanager.model.VehiclePosition;
import io.github.williamyin2024.fleetmanager.positiontracker.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PositionUpdateController {
	@Autowired
	private PositionRepository positionRepository;

	@RequestMapping(method=RequestMethod.POST, value="/vehicles/")
	public void receiveUpdatedPostion(@RequestBody VehiclePosition newReport) {
		positionRepository.updatePosition(newReport);
	}

	@RequestMapping(method= RequestMethod.DELETE, value="/vehicles/")
	public void resetHistories() {
		positionRepository.reset();
	}
}
