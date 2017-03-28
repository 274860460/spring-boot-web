package com.example.oauth2RestTemplet;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

/**
 * Created by iselin on 2017/3/16.
 */
public class OAuth2RestTemplateTest {

    public static void main(String[] args) {

        ResourceOwnerPasswordResourceDetails resources = new ResourceOwnerPasswordResourceDetails();
        resources.setClientId("loan-service");
        resources.setClientSecret("");
        resources.setUsername("admin");
        resources.setPassword("password");
        resources.setAccessTokenUri("http://localhost:9999/oauth/token");
        OAuth2RestTemplate template = new OAuth2RestTemplate(resources);

        System.out.println(template.getAccessToken());
    }
}
