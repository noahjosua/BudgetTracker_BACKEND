package com.example.budgettrackerv1;

/**
 * Income and Expense objects in json format - that is how we receive them from our frontend.
 */
public class MockData {
    public static final String json1 = "{\"dateCreated\": \"2023-05-15\",\n" +
            "    \"datePlanned\": \"2023-07-01\",\n" +
            "    \"category\": \"RENT\",\n" +
            "    \"description\": \"Stromrechnung für Mai\",\n" +
            "    \"amount\": 78.25}";

    public static final String json2 = "{\"dateCreated\": \"2023-04-20\",\n" +
            "    \"datePlanned\": \"2023-06-10\",\n" +
            "    \"category\": \"FREE_TIME\",\n" +
            "    \"description\": \"Kino Avengers Endgame\",\n" +
            "    \"amount\": 22.5}";

    public static final String json3 = "{\"dateCreated\": \"2023-03-05\",\n" +
            "    \"datePlanned\": \"2023-03-28\",\n" +
            "    \"category\": \"GROCERIES\",\n" +
            "    \"description\": \"Wocheneinkauf bei Aldi\",\n" +
            "    \"amount\": 65.75}";
}
