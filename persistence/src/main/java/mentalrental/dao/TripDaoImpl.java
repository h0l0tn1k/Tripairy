package mentalrental.dao;

import com.cloudant.client.api.Database;
import com.cloudant.client.org.lightcouch.NoDocumentException;
import mentalrental.CloudantClientMgr;
import mentalrental.entity.Trip;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mentalrental.exceptions.TripDbException;

import java.io.IOException;
import java.util.List;

public class TripDaoImpl implements TripDao {

    public String create(Trip trip) {
        Database db = null;
        try {
            db = CloudantClientMgr.getDB();
        } catch (Exception re) {
            re.printStackTrace();
            throw new TripDbException("Creation of trip failed.", re);
        }

        trip.setId(String.valueOf(System.currentTimeMillis()));
        db.save(trip);
        return trip.getId();
    }

    public void update(Trip trip) {
        if (trip == null)
            return;

        Database db = null;
        try {
            db = CloudantClientMgr.getDB();
        } catch (Exception re) {
            re.printStackTrace();
            throw new TripDbException("Update of trip failed.", re);
        }

        db.update(trip);
    }

    public void delete(Trip trip) {
        if (trip == null)
            return;

        Database db = null;
        try {
            db = CloudantClientMgr.getDB();
        } catch (Exception re) {
            re.printStackTrace();
            throw new TripDbException("Removing of trip failed.", re);
        }

        db.remove(trip);
    }

    public List<Trip> findAll() {
        Database db = null;
        try {
            db = CloudantClientMgr.getDB();
            return db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse().getDocsAs(Trip.class);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new TripDbException("Getting all trip failed.", ioe);
        }
        catch (Exception re) {
            re.printStackTrace();
            throw new TripDbException("Getting all trip failed.", re);
        }
    }

    public Trip findById(String id) {
        Database db = null;
        try {
            db = CloudantClientMgr.getDB();
            return db.find(Trip.class, id);
        }
        catch (NoDocumentException nde) {
            return null;
        }
        catch (Exception re) {
            re.printStackTrace();
            throw new TripDbException("Finding by id of trip failed.", re);
        }
    }

    public Trip findByTitle(String name) {
        Database db = null;
        try {
            db = CloudantClientMgr.getDB();
        } catch (Exception re) {
            re.printStackTrace();
            throw new TripDbException("Removing of trip failed.", re);
        }

        throw new UnsupportedOperationException();
    }
}
