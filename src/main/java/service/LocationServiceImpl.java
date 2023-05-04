package service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import domain.Location;

@Slf4j
public class LocationServiceImpl implements LocationService {

    private final Set<Location> locations = new HashSet<>();

    public LocationServiceImpl() {
        initLocations();
    }

    private void initLocations() {
        locations.add(new Location(50, 100, "A", null));
        locations.add(new Location(200, 300, "B", null));
        locations.add(new Location(50, 250, "C", null));
        locations.add(new Location(200, 280, "D", null));
    }

    @Override
    public Set<Location> findAll() {
        return locations;
    }

    @Override
    public void save(Location location) {
        log.info("Saving location: " + location);
        locations.add(location);
    }
}
