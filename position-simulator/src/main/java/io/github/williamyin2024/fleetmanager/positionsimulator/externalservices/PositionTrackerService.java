package io.github.williamyin2024.fleetmanager.positionsimulator.externalservices;

import io.github.williamyin2024.fleetmanager.model.VehiclePosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PositionTrackerService {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${position-tracker-url}")
	private String positionTrackerUrl;

	public void sendReportToPositionTracker(VehiclePosition vehiclePosition) {
		restTemplate.postForEntity(positionTrackerUrl + "/vehicles/", vehiclePosition, Void.class);
	}

	public void clearHistories() {
		restTemplate.delete(positionTrackerUrl + "/vehicles/");
	}
}
