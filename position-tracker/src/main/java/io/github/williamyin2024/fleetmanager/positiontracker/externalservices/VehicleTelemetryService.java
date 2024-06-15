package io.github.williamyin2024.fleetmanager.positiontracker.externalservices;

import io.github.williamyin2024.fleetmanager.model.VehiclePosition;
import io.github.williamyin2024.fleetmanager.positiontracker.exception.TelemetryServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
@Slf4j
public class VehicleTelemetryService {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${telemetry-url}")
	private String telemetryUrl;

	public void updateData(VehiclePosition vehiclePosition) {
		try {
			restTemplate.postForEntity(telemetryUrl + "/vehicles/", vehiclePosition, Void.class);
		} catch (RestClientException e) {
			log.info("Telemetry service unavailable. Unable to update data for {}", vehiclePosition.getName());
		}
	}

	public BigDecimal getSpeedFor(String vehicleName) throws TelemetryServiceUnavailableException {
		try {
			return restTemplate.getForObject(telemetryUrl + "/vehicles/" + vehicleName, BigDecimal.class);
		} catch (RestClientException e) {
			log.info("Telemetry service unavailable. Cannot get speed for vehicle {}", vehicleName);
			throw new TelemetryServiceUnavailableException();
		}
	}
}
