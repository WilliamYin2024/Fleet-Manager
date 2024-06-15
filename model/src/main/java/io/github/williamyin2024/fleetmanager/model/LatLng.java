package io.github.williamyin2024.fleetmanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LatLng {
	private BigDecimal lat;
	private BigDecimal lng;
}
