package src;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JTIMain {

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Work\\JTI\\src\\main\\resources\\TestScenario.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));

        String line, title = "", description = "";
        String key = "IB-3";
        String mainLabel = "New_Profile_Password_Change";

        boolean startedGiven = false;

        while ((line = br.readLine()) != null) {

            if (line.startsWith("IB")) {
                title = line;
                continue;
            }
            if(startedGiven && line.equals("")){
                startedGiven = false;
                sendRequest(title, description, key, mainLabel);
                description = new String();
            }

            if (startedGiven || line.startsWith("GIVEN") || line.startsWith("*GIVEN*") ) {
                startedGiven = true;
                description = description + line + "\n";
            }
        }
    }
    private static void sendRequest(String title, String description, String key, String mainLabel) throws IOException {
        title = StringEscapeUtils.escapeJava(title);
        description = StringEscapeUtils.escapeJava(description);
        mainLabel = StringEscapeUtils.escapeJava(mainLabel);

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n" +
                                                         "    \"fields\": {\n" +
                                                         "        \"project\": {\n" +
                                                         "            \"key\": \"IB\"\n" +
                                                         "        },\n" +
                                                         "        \"summary\": \""+ title +"\",\n" +
                                                         "        \"description\": \"" + description + "\",\n" +
                                                         "        \"issuetype\": {\n" +
                                                         "            \"name\": \"Test\"\n" +
                                                         "        },\n" +
                                                         "        \"labels\": [\n" +
                                                         "            \""+ mainLabel + "\"\n"+
                                                         "        ]\n" +
                                                         "    },\n" +
                                                         "    \"update\": {\n" +
                                                         "        \"issuelinks\": [\n" +
                                                         "            {\n" +
                                                         "                \"add\": {\n" +
                                                         "                    \"type\": {\n" +
                                                         "                        \"name\": \"Test\",\n" +
                                                         "                        \"inward\": \"Tested by\",\n" +
                                                         "                        \"outward\": \"Tests\"\n" +
                                                         "                    },\n" +
                                                         "                    \"outwardIssue\": {\n" +
                                                         "                        \"key\": \"" + key + "\"\n" +
                                                         "                    }\n" +
                                                         "                }\n" +
                                                         "            }\n" +
                                                         "        ]\n" +
                                                         "    }\n" +
                                                         "}");
        Request request = new Request.Builder()
            .url("https://icbpijira.atlassian.net/rest/api/latest/issue/")
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Basic YW5kcmVpLmRvYnJvdkBlbmRhdmEuY29tOlpLeWJJN0hEV2JGaGExeVpsbVpKMTA2QQ==")
            .addHeader("User-Agent", "PostmanRuntime/7.13.0")
            .addHeader("Accept", "*/*")
            .addHeader("Cache-Control", "no-cache")
            .addHeader("Postman-Token", "8cdbd8b6-dbfe-4795-88d1-897de74c7103,9a627600-75f3-4980-89cd-4776c464b66b")
            .addHeader("Host", "icbpijira.atlassian.net")
            .addHeader("cookie", "atlassian.xsrf.token=BKOT-MDEH-3XFL-U81T_d6545139aa63aceb8088dca8da17bab890839a0f_lin")
            .addHeader("accept-encoding", "gzip, deflate")
            .addHeader("content-length", "784")
            .addHeader("Connection", "keep-alive")
            .addHeader("cache-control", "no-cache")
            .build();

        Response response = client.newCall(request).execute();

        System.out.println(response);
    }
}