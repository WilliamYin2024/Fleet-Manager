package io.github.williamyin2024.fleetmanager.apigateway.externalservices;

import io.github.williamyin2024.fleetmanager.model.VehiclePosition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class PositionTrackingService {
	@Autowired
	private RestTemplate restTemplate;

	@Value("${position-tracker-url}")
	private String positionTrackerUrl;

	@Value("${staff-service-url}")
	private String staffServiceUrl;

	public Collection<VehiclePosition> getHistoryFor(String vehicleName) {
		ResponseEntity<VehiclePosition[]> response = restTemplate.getForEntity(
			positionTrackerUrl + "/history/" + vehicleName,
			VehiclePosition[].class);
		return List.of(response.getBody());
	}

	public VehiclePosition getLastReportFor(String vehicleName) {
		return restTemplate.getForObject(positionTrackerUrl + "/vehicles/" + vehicleName, VehiclePosition.class);
	}

	public String getDriverFor(String vehicleName) {
		return restTemplate.getForObject(staffServiceUrl + "/driver/" + vehicleName, String.class);
	}

	public Collection<VehiclePosition> getAllPositions() {
		ResponseEntity<VehiclePosition[]> response = restTemplate.getForEntity(
			positionTrackerUrl + "/vehicles/",
			VehiclePosition[].class);
		return List.of(response.getBody());
	}
}
