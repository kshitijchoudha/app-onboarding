package com.mycompany.app;

import java.io.IOException;

import org.kohsuke.github.GHCreateRepositoryBuilder;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

public class GitHubClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Github Client...");

        GitHubClient ghClient = new GitHubClient();
        ghClient.createSpringBootHelmRepo("delete-this-test-1");
        
        GitHub github = new GitHubBuilder().withOAuthToken(System.getenv("GH_API_TOKEN")).build();

        System.out.println("Repo created..");
        
        GHRepository ghRepo =  github.getRepository("kshitijchoudha/delete-this-test");
        while(ghRepo ==null && ghRepo.getFullName() ==null)
        {
            System.out.println("Repo is not reade..sleeping for 10s");
            Thread.sleep(10000);
        }

        System.out.println("Repo ready"+ghRepo.getFullName());
        
    
    }

    public void createSpringBootHelmRepo(String applicationName) throws IOException
    {
        GitHub github = new GitHubBuilder().withOAuthToken(System.getenv("GH_API_TOKEN")).build();
        GHCreateRepositoryBuilder ghRepoBuilder = github.createRepository(applicationName);
        ghRepoBuilder.fromTemplateRepository("kshitijchoudha", "spring-boot-helm-template");
        ghRepoBuilder.create();
    }
}
