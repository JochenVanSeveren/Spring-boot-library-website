package domain;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import model.Location;

@Slf4j
public class LocationServiceImpl implements LocationService {

    private final List<Location> locations = new ArrayList<>();

    public LocationServiceImpl() {
        initLocations();
    }

    private void initLocations() {
        locations.add(new Location(12345, 67890, "New York", null));
        locations.add(new Location(23456, 78901, "Los Angeles", null));
        locations.add(new Location(34567, 89012, "Chicago", null));
        locations.add(new Location(45678, 90123, "San Francisco", null));
    }

    @Override
    public List<Location> findAll() {
        return locations;
    }

    @Override
    public void save(Location location) {
        log.info("Saving location: " + location);
        locations.add(location);
    }
}
