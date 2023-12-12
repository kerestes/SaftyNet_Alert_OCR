package fr.saftynet.alerts.utilities;

import java.util.HashMap;

public class JsonResponse {

    public static HashMap<String, String> errorResponse(final String errorMessage){
        HashMap<String, String> errorResponse = new HashMap<>();
        errorResponse.put("Error", errorMessage);
        return errorResponse;
    }

    public static HashMap<String, String> deleteResponse(final Long id){
        HashMap<String, String> deleteResponse = new HashMap<>();
        deleteResponse.put("Delete", "if id (" + id + ") exists, it was deleted");
        return deleteResponse;
    }
}
