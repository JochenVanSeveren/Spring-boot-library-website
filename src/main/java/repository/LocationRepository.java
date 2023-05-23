package repository;

import domain.Location;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {

    Set<Location> findAll();

}
