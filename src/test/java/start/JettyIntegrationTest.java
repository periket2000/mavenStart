import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import server.JettyServer;

public class JettyIntegrationTest {
    private static JettyServer jettyServer;

    @BeforeClass
    public static void setup() throws Exception {
        jettyServer = new JettyServer();
        jettyServer.start();
    }

    @AfterClass
    public static void cleanup() throws Exception {
        jettyServer.stop();
    }

    @Ignore
    @Test
    public void givenServer_whenSendRequestToBlockingServlet_thenReturnStatusOK() throws Exception {
        // given
        String url = "https://localhost:8443/status";
        HttpClient client = HttpClientBuilder.create().build();



        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);

        // then
        Assert.assertEquals(response.getStatusLine().getStatusCode(), 200);

    }

    static {
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> hostname.equals("127.0.0.1"));
    }

    @Test
    public void statusOK_https() throws Exception {

        Properties systemProps = System.getProperties();
        systemProps.put("javax.net.ssl.keyStorePassword","changeit");
        systemProps.put("javax.net.ssl.keyStore","C:/Users/blanc/Devel/tmp/test/src/main/resources/keystore.p12");
        // Set same truststore as server keystore
        systemProps.put("javax.net.ssl.trustStore", "C:/Users/blanc/Devel/tmp/test/src/main/resources/keystore.p12");
        systemProps.put("javax.net.ssl.trustStorePassword","changeit");
        System.setProperties(systemProps);

        HttpsURLConnection conn = (HttpsURLConnection) new URL("https://127.0.0.1:8443/status").openConnection();
        conn.setRequestMethod("GET");
        Assert.assertEquals(print_content(conn), "{ \"status\": \"ok\"}");
    }

    private String print_content(HttpsURLConnection con){
        String res = "";
        if(con!=null){
            try {		
            String input;	
            BufferedReader br = 
                new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            while ((input = br.readLine()) != null){
                res += input;
            }
            br.close();                    
            } catch (IOException e) {
            e.printStackTrace();
            } finally {
                return res;
            }   
        }
        return res;
    }    
}