package service;

import domain.Location;

import java.util.List;
import java.util.Set;

public interface LocationService
{
    Set<Location> findAll();

    void save(Location location);

}