package src.main.java.com;
import java.io.IOException;
import java.net.*;
import java.net.http.*;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class HarnessClient {

  private static final String accountId = "9S4tqOcyQkeFXybZvJt6fA";
  private static final String orgId = "default";
  private static final String projectId = "Default_Project_1671068007356";
  public static void main(String[] args) throws Exception {

    HarnessClient harnessClient = new HarnessClient();
    //harnessClient.createHarnessEnvironment("auto-env-1");
    //harnessClient.createHarnessInfraDefinition("auto-infra-def-env-1", "autoenv1");
    harnessClient.createHarnessK8SConnector("auto-k8s-connector-kind", "k8s-kind-mac-delegate");
    
  }

  public String createHarnessEnvironment(String envName) throws IOException, InterruptedException{

    var httpClient = HttpClient.newBuilder().build();

    var payload = String.join("\n"
      , "{"
      , " \"orgIdentifier\": \""+HarnessClient.orgId+"\","
      , " \"projectIdentifier\": \""+HarnessClient.projectId+"\","
      , " \"identifier\": \""+envName.replace("-", "")+"\","
      , " \"name\": \""+envName+"\","
      , " \"type\": \"PreProduction\""
      , "}"
    );

    System.out.println(payload);

    HashMap<String, String> params = new HashMap<>();
    params.put("accountIdentifier", HarnessClient.accountId);

    var query = params.keySet().stream()
      .map(key -> key + "=" + URLEncoder.encode(params.get(key), StandardCharsets.UTF_8))
      .collect(Collectors.joining("&"));

    var host = "https://app.harness.io";
    var pathname = "/gateway/ng/api/environmentsV2";
    var request = HttpRequest.newBuilder()
      .POST(HttpRequest.BodyPublishers.ofString(payload))
      .uri(URI.create(host + pathname + '?' + query))
      .header("Content-Type", "application/json")
      .header("x-api-key", System.getenv("HARNESS_API_TOKEN"))
      .build();

    var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    System.out.println(response.body());

    return response.body();
  }

  public String createHarnessInfraDefinition(String infraDefName, String envName) throws IOException, InterruptedException{

    var httpClient = HttpClient.newBuilder().build();

    var payload = String.join("\n"
      , "{"
      , " \"identifier\": \""+infraDefName.replace("-", "")+"\","
      , " \"orgIdentifier\": \""+HarnessClient.orgId+"\","
      , " \"projectIdentifier\": \""+HarnessClient.projectId+"\","
      , " \"environmentRef\": \""+envName+"\","
      , " \"name\": \""+infraDefName+"\","
      , " \"type\": \"KubernetesDirect\","
      , " \"yaml\": \"{deploymentType: NativeHelm, spec: {connectorRef: k8skindconnector, namespace: default, releaseName: release-<+INFRA_KEY>}}\""
      , "}"
    );
    System.out.println(payload);

    HashMap<String, String> params = new HashMap<>();
    params.put("accountIdentifier", HarnessClient.accountId);

    var query = params.keySet().stream()
      .map(key -> key + "=" + URLEncoder.encode(params.get(key), StandardCharsets.UTF_8))
      .collect(Collectors.joining("&"));

    var host = "https://app.harness.io";
    var pathname = "/gateway/ng/api/infrastructures";
    var request = HttpRequest.newBuilder()
      .POST(HttpRequest.BodyPublishers.ofString(payload))
      .uri(URI.create(host + pathname + '?' + query))
      .header("Content-Type", "application/json")
      .header("x-api-key", System.getenv("HARNESS_API_TOKEN"))
      .build();

    var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    System.out.println(response.body());
    return response.body();
  }

  public String createHarnessK8SConnector(String connectorName, String delegateSelector) throws IOException, InterruptedException{
    var httpClient = HttpClient.newBuilder().build();

    var payload = String.join("\n"
      , "{"
      , " \"connector\": {"
      , "  \"name\": \""+connectorName+"\","
      , "  \"identifier\": \""+connectorName.replace("-", "")+"\","
      , "  \"description\": \"k8s-connctor\","
      , "  \"orgIdentifier\": \""+HarnessClient.orgId+"\","
      , "  \"projectIdentifier\": \""+HarnessClient.projectId+"\","
      , "  \"tags\": {"
      , "   \"property1\": \"string\","
      , "   \"property2\": \"string\""
      , "  },"
      , "  \"type\": \"K8sCluster\","
      , "  \"spec\": {"
      , "   \"connectorType\": {\"type\": \"KubernetesClusterConfig\"},"
      , "   \"credential\": {\"type\": \"InheritFromDelegate\"},"
      , "   \"delegateSelectors\": [\""+delegateSelector+"\"]"
      , "  }"
      , " }"
      , "}"
    );

    System.out.println(payload);
    HashMap<String, String> params = new HashMap<>();
    params.put("accountIdentifier", HarnessClient.accountId);
    

    var query = params.keySet().stream()
      .map(key -> key + "=" + URLEncoder.encode(params.get(key), StandardCharsets.UTF_8))
      .collect(Collectors.joining("&"));

    var host = "https://app.harness.io";
    var pathname = "/gateway/ng/api/connectors";
    var request = HttpRequest.newBuilder()
      .POST(HttpRequest.BodyPublishers.ofString(payload))
      .uri(URI.create(host + pathname + '?' + query))
      .header("Content-Type", "application/json")
      .header("x-api-key", System.getenv("HARNESS_API_TOKEN"))
      .build();

    var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    System.out.println(response.body());

    return response.body();

  }
}