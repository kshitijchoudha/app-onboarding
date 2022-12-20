package com.mycompany.app;

import java.io.IOException;
import java.util.logging.*;

/**
 * Hello world!
 *
 */
public class App {

   
    public static void main(String[] args) throws Exception {
        System.out.println("Application onboarding on Harness and Github....");

        String serviceName;

        if (args == null || args.length == 0)
            serviceName = "delete-me-service";
        else
            serviceName = args[0];
        String envName = serviceName + "-env-1";
        String infraDefName = envName + "-infra-def-1";
        String pipelineName = serviceName + "-pipeline-1";
        HarnessClient harnessClient = new HarnessClient();
        harnessClient.createHarnessEnvironment(envName);

        harnessClient.createHarnessInfraDefinition(infraDefName,
                envName.replace("-", ""), "NativeHelm", "default");
        // harnessClient.createHarnessK8SConnector("auto-k8s-connector-kind","k8s-kind-mac-delegate");

        GitHubClient ghClient = new GitHubClient();
        System.out.println("Creating Github Repo....");
        ghClient.createSpringBootHelmRepo(serviceName);

        //TODO: find a better way to identify when new repo is ready for new commit
        Thread.sleep(20000);

        harnessClient.createHarnessService(serviceName, serviceName);

        harnessClient.createHarnessPipeline(pipelineName,
                serviceName.replace("-", ""),
                envName.replace("-", ""),
                infraDefName.replace("-", ""),
                serviceName);
    }
}
