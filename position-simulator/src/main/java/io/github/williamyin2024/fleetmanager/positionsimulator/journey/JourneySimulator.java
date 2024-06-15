package io.github.williamyin2024.fleetmanager.positionsimulator.journey;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.github.williamyin2024.fleetmanager.model.Vehicle;
import io.github.williamyin2024.fleetmanager.model.VehiclePosition;
import io.github.williamyin2024.fleetmanager.positionsimulator.Main;
import io.github.williamyin2024.fleetmanager.positionsimulator.externalservices.PositionTrackerService;
import io.github.williamyin2024.fleetmanager.positionsimulator.utils.VehicleNameUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@Slf4j
public class JourneySimulator {
	@Autowired
	private PositionTrackerService positionTrackerService;

	private Map<String, Queue<String>> reports = new HashMap<>();
	private List<String> vehicleNames = new ArrayList<>();

	@Value("${bootstrap.servers}")
	private String bootstrapServers;
	@Value("${vehicle.positions.kafka.topic}")
	private String vehiclePositionsTopic;
	@Value("${actions.kafka.topic}")
	private String actionsTopic;

	private Properties properties;
	private KafkaProducer<String, String> producer;

	/**
	 * Read the data from the resources directory - should work for an executable Jar as
	 * well as through direct execution
	 */
	@PostConstruct
	private void setUpData() {
		PathMatchingResourcePatternResolver path = new PathMatchingResourcePatternResolver();
		try {
			for (Resource nextFile : path.getResources("tracks/*")) {
				URL resource = nextFile.getURL();
				File f = new File(resource.getFile());
				String vehicleName = VehicleNameUtils.prettifyName(f.getName());
				vehicleNames.add(vehicleName);
				populateReportQueueForVehicle(vehicleName);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
//		positionTrackerService.clearHistories();

		properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		producer = new KafkaProducer<>(properties);

		ProducerRecord<String, String> record = new ProducerRecord<>(
			actionsTopic,
			new Date().toString(),
			"reset"
		);
		producer.send(record);
		log.info("Message sent to Kafka: {}", record.topic());
		producer.flush();
	}

	private void populateReportQueueForVehicle(String vehicleName) {
		InputStream is = Main.class.getResourceAsStream("/tracks/" + VehicleNameUtils.uglifyName(vehicleName));
		try (Scanner sc = new Scanner(is)) {
			Queue<String> thisVehicleReports = new LinkedBlockingQueue<>();
			while (sc.hasNextLine()) {
				String nextReport = sc.nextLine();
				thisVehicleReports.add(nextReport);
			}
			reports.put(vehicleName, thisVehicleReports);
		}
	}


	@Scheduled(fixedDelay = 100)
	public void randomPositionUpdate() throws JsonProcessingException {
		// Random jitter. Sometimes we'll do nothing
		if (Math.random() < 0.9) return;

		// Choose random vehicle
		int position = (int) (Math.random() * vehicleNames.size());
		String chosenVehicleName = vehicleNames.get(position);

		// Grab next report for this vehicle
		// To smooth out fluctuations, we'll miss out lots of reports
		int randomReportDrop = (int) (Math.random() * 10);
		String nextReport = null;
		for (int i = 0; i <= randomReportDrop; i++) {
			nextReport = reports.get(chosenVehicleName).poll();
			if (nextReport == null) {
				System.out.println("Journey over for " + chosenVehicleName + ". Restarting route");

				ProducerRecord<String, String> record = new ProducerRecord<>(
					actionsTopic,
					new Date().toString(),
					"reset"
				);
				producer.send(record);
				log.info("Message sent to Kafka: {}", record.topic());
				producer.flush();

				populateReportQueueForVehicle(chosenVehicleName);
				nextReport = reports.get(chosenVehicleName).poll();
			}
		}

		VehiclePosition report = getVehicleDataFromRawString(chosenVehicleName, nextReport);

//		positionTrackerService.sendReportToPositionTracker(report);

		XmlMapper xmlMapper = new XmlMapper();
		String reportXmlString = xmlMapper.writeValueAsString(report);
		ProducerRecord<String, String> record = new ProducerRecord<>(
			vehiclePositionsTopic,
			report.getName() + " : " + report.getTimestamp().toString(),
			reportXmlString
		);
		producer.send(record);
		log.info("Message sent to Kafka: {}", record.topic());
		producer.flush();
	}

	private VehiclePosition getVehicleDataFromRawString(String chosenVehicleName, String nextReport) {
		String[] data = nextReport.split("\"");
		String lat = data[1];
		String lng = data[3];

		VehiclePosition report = new Vehicle.Builder()
			.withName(chosenVehicleName)
			.withLat(lat)
			.withLng(lng)
			.withTimestamp(new Date())
			.build();
		return report;
	}
}
