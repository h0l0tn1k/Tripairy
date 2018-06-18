package mentalrental;

import com.cloudant.client.api.Database;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/trips")
public class TripsServlet {

    public TripsServlet() {
    }

    @POST
    public Response create(@FormParam("title") String title,
                           @FormParam("description") String description)
            throws Exception {

        Database db = null;
        try {
            db = getDB();
        } catch (Exception re) {
            re.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        JsonObject resultObject = create(db, title, description);
        System.out.println("Create Successful.");
        return Response.ok(resultObject.toString()).build();
    }

    protected JsonObject create(Database db, String title, String description)
            throws IOException {

        String id = String.valueOf(System.currentTimeMillis());

        // create a new document
        System.out.println("Creating new document with id : " + id);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("title", title);
        data.put("_id", id);
        data.put("description", description);
        data.put("creation_date", new Date().toString());
        db.save(data);

        HashMap<String, Object> obj = db.find(HashMap.class, id);
        JsonObject resultObject = toJsonObject(obj);

        return resultObject;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("id") Long id, @QueryParam("cmd") String cmd) throws Exception {

        Database db = null;
        try {
            db = getDB();
        } catch (Exception re) {
            re.printStackTrace();
            throw re;
        }

        JsonObject resultObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();

        if (id == null) {
            try {
                // get all the document present in database
                List<HashMap> allDocs = db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse()
                        .getDocsAs(HashMap.class);

                if (allDocs.size() == 0) {
                    allDocs = initializeSampleData(db);
                }

                for (HashMap doc : allDocs) {
                    HashMap<String, Object> obj = db.find(HashMap.class, doc.get("_id") + "");
                    JsonObject jsonObject = new JsonObject();

                    jsonObject.addProperty("id", obj.get("_id") + "");
                    jsonObject.addProperty("title", obj.get("title") + "");
                    jsonObject.addProperty("description", obj.get("description") + "");

                    jsonArray.add(jsonObject);
                }
            } catch (Exception dnfe) {
                System.out.println("Exception thrown : " + dnfe.getMessage());
            }

            resultObject.addProperty("id", "all");
            resultObject.add("body", jsonArray);

            return Response.ok(resultObject.toString()).build();
        }

        // check if document exists
        HashMap<String, Object> obj = db.find(HashMap.class, id + "");
        if (obj != null) {
            JsonObject jsonObject = toJsonObject(obj);
            return Response.ok(jsonObject.toString()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") Long id) throws Exception {

        Database db = null;
        try {
            db = getDB();
        } catch (Exception re) {
            re.printStackTrace();
            throw re;
        }

        HashMap<String, Object> obj = db.find(HashMap.class, id.toString());
        if (obj == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(obj.get(id.toString())).build();
        }
    }

    @DELETE
    public Response delete(@QueryParam("id") long id) {

        Database db = null;
        try {
            db = getDB();
        } catch (Exception re) {
            re.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        // check if document exist
        HashMap<String, Object> obj = db.find(HashMap.class, id + "");

        if (obj == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            db.remove(obj);

            System.out.println("Delete Successful.");

            return Response.ok("OK").build();
        }
    }

    @PUT
    public Response update(@QueryParam("id") long id, @QueryParam("name") String name,
                           @QueryParam("value") String value) {

        Database db = null;
        try {
            db = getDB();
        } catch (Exception re) {
            re.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        // check if document exist
        HashMap<String, Object> obj = db.find(HashMap.class, id + "");

        if (obj == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            obj.put("name", name);
            obj.put("value", value);

            db.update(obj);

            System.out.println("Update Successful.");

            return Response.ok("OK").build();
        }
    }

      private JsonObject toJsonObject(HashMap<String, Object> obj) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", obj.get("_id") + "");
        jsonObject.addProperty("description", obj.get("description") + "");
        jsonObject.addProperty("title", obj.get("title") + "");
        return jsonObject;
    }

    private List<HashMap> initializeSampleData(Database db) throws Exception {

        long id = System.currentTimeMillis();
        String name = "Sample title 1";
        String value = "Sample description 2";

        // create a new document
        System.out.println("Creating new document with id : " + id);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("title", name);
        data.put("_id", id + "");
        data.put("description", value);
        db.save(data);

        return db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse().getDocsAs(HashMap.class);
    }

    private Database getDB() {
        return CloudantClientMgr.getDB();
    }

}
