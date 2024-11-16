package com.example.tp_retrofit;
import fi.iki.elonen.NanoHTTPD;

import java.util.HashMap;
import java.util.Map;

public class MyHttpServer extends NanoHTTPD {

    private final Map<Integer, Map<String, String>> items = new HashMap<>();
    private int idCounter = 1;

    public MyHttpServer(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Method method = session.getMethod();

        try {
            if ("/items".equals(uri)) {
                switch (method) {
                    case GET:
                        return getAllItems(session);
                    case POST:
                        return createItem(session);
                    case PUT:
                        return updateItem(session);
                    case DELETE:
                        return deleteItem(session);
                }
            }
        } catch (Exception e) {
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "Error: " + e.getMessage());
        }

        return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "Not Found");
    }

    private Response getAllItems(IHTTPSession session) {
        StringBuilder response = new StringBuilder("[");
        for (Map.Entry<Integer, Map<String, String>> entry : items.entrySet()) {
            response.append("{\"id\":").append(entry.getKey())
                    .append(",\"name\":\"").append(entry.getValue().get("name"))
                    .append("\",\"description\":\"").append(entry.getValue().get("description"))
                    .append("\"},");
        }
        if (response.length() > 1) {
            response.setLength(response.length() - 1); // Remove last comma
        }
        response.append("]");
        return newFixedLengthResponse(Response.Status.OK, "application/json", response.toString());
    }

    private Response createItem(IHTTPSession session) throws Exception {
        Map<String, String> postData = new HashMap<>();
        session.parseBody(postData);
        String name = session.getParms().get("name");
        String description = session.getParms().get("description");

        Map<String, String> newItem = new HashMap<>();
        newItem.put("name", name);
        newItem.put("description", description);

        items.put(idCounter++, newItem);
        return newFixedLengthResponse(Response.Status.CREATED, "application/json", "{\"message\":\"Item created successfully\"}");
    }

    private Response updateItem(IHTTPSession session) throws Exception {
        String idParam = session.getParms().get("id");
        if (idParam == null) {
            return newFixedLengthResponse(Response.Status.BAD_REQUEST, "application/json", "{\"error\":\"ID required\"}");
        }

        int id = Integer.parseInt(idParam);
        if (!items.containsKey(id)) {
            return newFixedLengthResponse(Response.Status.NOT_FOUND, "application/json", "{\"error\":\"Item not found\"}");
        }

        Map<String, String> postData = new HashMap<>();
        session.parseBody(postData);
        String name = session.getParms().get("name");
        String description = session.getParms().get("description");

        Map<String, String> updatedItem = items.get(id);
        if (name != null) updatedItem.put("name", name);
        if (description != null) updatedItem.put("description", description);

        return newFixedLengthResponse(Response.Status.OK, "application/json", "{\"message\":\"Item updated successfully\"}");
    }

    private Response deleteItem(IHTTPSession session) {
        String idParam = session.getParms().get("id");
        if (idParam == null) {
            return newFixedLengthResponse(Response.Status.BAD_REQUEST, "application/json", "{\"error\":\"ID required\"}");
        }

        int id = Integer.parseInt(idParam);
        if (!items.containsKey(id)) {
            return newFixedLengthResponse(Response.Status.NOT_FOUND, "application/json", "{\"error\":\"Item not found\"}");
        }

        items.remove(id);
        return newFixedLengthResponse(Response.Status.OK, "application/json", "{\"message\":\"Item deleted successfully\"}");
    }
}

