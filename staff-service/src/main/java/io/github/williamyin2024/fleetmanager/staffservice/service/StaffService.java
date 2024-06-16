package io.github.williamyin2024.fleetmanager.staffservice.service;


import io.github.williamyin2024.fleetmanager.model.StaffRecord;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StaffService {
	private Map<String, String> drivers = Stream.of(new String[][] {
		{"Queensway Truck", "Karen Cassidy"},
		{"Islington Truck", "Timothy Smith"},
		{"Finch Truck", "Duncan Gonzalez"},
		{"Danforth Truck", "Darren Koch"},
		{"East Queens Truck", "Jeremy Martin"}}).collect(Collectors.toMap(data -> data[0], data -> data[1]));

	private Map<String, String> photos = Stream.of(new String[][] {
		{"Karen Cassidy","placeholder.jpg"},
		{"Timothy Smith","placeholder.jpg"},
		{"Duncan Gonzalez","placeholder.jpg"},
		{"Darren Koch","placeholder.jpg"},
		{"Jeremy Martin","placeholder.jpg"}}).collect(Collectors.toMap(data -> data[0], data -> data[1]));


	public StaffRecord getDriverDetailsFor(String vehicleName)
	{
		String driverName = drivers.get(vehicleName);
		String staffPhoto = photos.get(driverName);
		return new StaffRecord(driverName, staffPhoto);
	}
}
