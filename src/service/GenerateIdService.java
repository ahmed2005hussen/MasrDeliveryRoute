package service;

import java.util.UUID;

public class GenerateIdService {
    public String generateId(String restaurantName) {

        String random = UUID.randomUUID().toString();

        return restaurantName + "-" + random;
    }
}
