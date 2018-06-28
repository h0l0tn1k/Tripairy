package mentalrental.dao;

import mentalrental.entity.Trip;

import java.io.IOException;
import java.util.List;

public interface TripDao {
    String create(Trip trip);
    void update(Trip trip);
    void delete(Trip trip);
    List<Trip> findAll();
    Trip findById(String id);
    Trip findByTitle(String name);
}
