package io.github.williamyin2024.fleetmanager.positiontracker.externalservices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.github.williamyin2024.fleetmanager.model.VehiclePosition;
import io.github.williamyin2024.fleetmanager.positiontracker.repository.PositionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

	@Autowired
	private PositionRepository positionRepository;

	@KafkaListener(
		topics={"${vehicle.positions.kafka.topic}","${actions.kafka.topic}"},
		groupId="${spring.kafka.consumer.group-id}"
	)
	public void listen(String message) throws JsonProcessingException {
		if (message.equals("reset")) {
			positionRepository.reset();
			log.info("Reset positions");
			return;
		}
		XmlMapper mapper = new XmlMapper();
		VehiclePosition position = mapper.readValue(message, VehiclePosition.class);
		positionRepository.updatePosition(position);
	}
}
