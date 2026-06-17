package domain;

import domain.base.AbstractLocation;

/**
 * Reprezentuje regał w bibliotece. Jest rozszerzeniem klasy {@code AbstractLocation}
 * i może zawierać dodatkowe informacje specyficzne dla regałów.
 */
public class Bookcase extends AbstractLocation {
	
	public Bookcase() {};
	public Bookcase(Integer id, String name) {
		super.init(id, name);
	}
}
