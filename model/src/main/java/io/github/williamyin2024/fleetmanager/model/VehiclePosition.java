package io.github.williamyin2024.fleetmanager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehiclePosition implements Comparable<VehiclePosition> {
	private String name;
	private BigDecimal lat;
	private BigDecimal lng;

	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS", timezone="UTC")
	private Date timestamp;
	private BigDecimal speed;

	@Override
	public int compareTo(VehiclePosition o) {
		return o.timestamp.compareTo(this.timestamp);
	}
}
