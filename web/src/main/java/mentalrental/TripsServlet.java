package mentalrental;

import com.google.gson.Gson;
import mentalrental.dao.TripDao;
import mentalrental.dao.TripDaoImpl;
import mentalrental.entity.Trip;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Currency;

@Path("/trips")
public class TripsServlet {

    private final Gson gson = new Gson();
    private final TripDao tripDao = new TripDaoImpl();

    public TripsServlet() {
    }

    @POST
    public Response create(@FormParam("title") String title,
                           @FormParam("description") String description,
                           @FormParam("imageUrl") String imageUrl,
                           @FormParam("price") BigDecimal price,
                           @FormParam("currency") String currency,
                           @FormParam("rating") Integer rating) {
        //todo
        Trip trip = new Trip();
        trip.setTitle(title);
        trip.setDescription(description);
        trip.setImageUrl(imageUrl);
        trip.setCurrency(Currency.getInstance(currency.toUpperCase()));
        trip.setPrice(price);
        trip.setRating(rating);
        tripDao.create(trip);

        return Response.ok(gson.toJson(tripDao.findById(trip.getId()))).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        return Response.ok(gson.toJson(tripDao.findAll())).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") String id) {

        Trip trip = tripDao.findById(id);
        if (trip != null) {
            return Response.ok(gson.toJson(trip)).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") String id) {
        Trip trip = tripDao.findById(id);
        if (trip == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        tripDao.delete(trip);
        return Response.ok("OK").build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") String id,
                           @FormParam("title") String title,
                           @FormParam("description") String description,
                           @FormParam("imageUrl") String imageUrl,
                           @FormParam("price") BigDecimal price,
                           @FormParam("currency") String currency,
                           @FormParam("rating") Integer rating) {

        //todo
        Trip trip = tripDao.findById(id);
        if (trip == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        trip.setTitle(title);
        trip.setDescription(description);
        trip.setImageUrl(imageUrl);
        trip.setCurrency(Currency.getInstance(currency.toUpperCase()));
        trip.setPrice(price);
        trip.setRating(rating);

        tripDao.update(trip);
        return Response.ok(gson.toJson(tripDao.findById(trip.getId()))).build();
    }
}
