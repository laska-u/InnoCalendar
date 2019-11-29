import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Parser {

    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final String SPREADSHEETID = "11YgZf49a5olHPqKJTjJODlwiIIDBw9eHg98rfnqu1GE";
    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = Parser.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public List<String> getCourses() {
        try {
            return getFirstRow();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<List<Object>> VALUES = new ArrayList<>();

    public static void  initTableLoad() throws GeneralSecurityException, IOException {
        final String range = "E2:AO721";
        Sheets service = GetSheets(range);
        ValueRange response = service.spreadsheets().values()
                .get(SPREADSHEETID, range)
                .execute();
        VALUES = response.getValues();
        if (VALUES == null || VALUES.isEmpty()) {
            System.out.println("No data found.");
        }
    }

    // use case: Modify schedule 
    // link to the description: https://bit.ly/2OVct7M
    public static List<Integer> getChangesCoursesIds() throws IOException, GeneralSecurityException {

        List<Integer> changesIds = new ArrayList<>();

        List<List<Object>> values = GetMainListData();
        for (int i = 0; i < values.size(); i++) {
            for (int j = 0; j < values.get(i).size(); j++) {
                if (((values.get(i).get(j))).equals((VALUES.get(i).get(j)))) {
                } else {
                    System.out.println("diffrent i:" + i + "j: " + j);
                    changesIds.add(j);
                    VALUES.get(i).set(j, (values.get(i).get(j)));
                }
            }
        }
        return changesIds;
    }


    public static List<String> getFirstRow() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        List<String> courses = new ArrayList<>();
        final String range = "E1:AO1";
        Sheets service = GetSheets(range);
        ValueRange response = service.spreadsheets().values()
                .get(SPREADSHEETID, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            for (int i = 0; i < values.get(0).size(); i++) {
                courses.add((String) values.get(0).get(i));
            }
        }
        return courses;
    }

    // Send notification about class
    // Step: scanning the schedule document 
    // link to the description: https://bit.ly/34xV7Eh
    public static HashMap<Integer, String> getCoursesByDatetime() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now().plus(Duration.ofHours(1));
        int rowIndex = 1;
        Sheets service = GetSheets("A1:A800");
        ValueRange response = service.spreadsheets().values()
                .get(SPREADSHEETID, "A1:A800")
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            System.out.println(localDate);
            System.out.println(values);
            for (int i = 0; i < values.size(); i++) {

                if (!values.get(i).isEmpty() && localDate.toString().equals(values.get(i).get(0).toString())) {
                    rowIndex = i + 1;
                    System.out.println(rowIndex);
                }
            }
        }

        final String timeRange = "D" + rowIndex + ":D" + (rowIndex + 7);
        System.out.println(timeRange);
        response = service.spreadsheets().values()
                .get(SPREADSHEETID, timeRange)
                .execute();
        values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            for (int i = 0; i < values.size(); i++) {
                LocalTime endTime = LocalTime.parse(values.get(i).get(0).toString().split("-")[0]);
                LocalTime startTime = endTime.minus(Duration.ofMinutes(5));
                if (startTime.isBefore(localTime) && endTime.isAfter(localTime)) {
                    rowIndex = rowIndex + i;
                }
            }
        }

        HashMap<Integer, String> currentCourses = new HashMap<Integer, String>();

        final String courseRange = "E" + rowIndex + ":AO" + rowIndex;
        response = service.spreadsheets().values()
                .get(SPREADSHEETID, courseRange)
                .execute();
        values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            for (int i = 0; i < values.get(0).size(); i++) {
                if (values.get(0).get(i) != "") {
                    currentCourses.put(i, values.get(0).get(i).toString());
                }
            }
        }
        return currentCourses;
    }


    private static Sheets GetSheets(String range) throws IOException, GeneralSecurityException
    {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        return service;
    }
    private static  List<List<Object>> GetMainListData() throws IOException, GeneralSecurityException
    {
        final String range = "E2:AO721";
        Sheets service = GetSheets(range);
        ValueRange response = service.spreadsheets().values()
                .get(SPREADSHEETID, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.err.println("No data found.");
        }
        return values;
    }
}
