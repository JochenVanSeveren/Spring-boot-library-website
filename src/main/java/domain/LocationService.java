package domain;

import model.Location;

import java.util.List;

public interface LocationService
{
    List<Location> findAll();

    void save(Location location);

}
