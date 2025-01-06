package com.example.demo.services;

import com.example.demo.config.KeycloakConfig;
import com.example.demo.controllers.ListingController;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class KeycloakUserService {

    private final String groupId="ac89c612-4c0c-43e0-9eb9-421f0166861b";

    @Value ("${keycloak.realm}")
    private String realm;

    @Autowired
    private Keycloak keycloak;

    @Autowired
    private KeycloakConfig keycloakConfig;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(KeycloakUserService.class);


    //TODO VERIFY OBSOLETE
    public String createUser(String username, String email, String password) {
        // Create user representation
        UserRepresentation user = new UserRepresentation();

        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);

        // Set user password
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        user.setCredentials(Collections.singletonList(credential));

        // Create user
        Response response = keycloak.realm(keycloakConfig.getRealm())
                .users()
                .create(user);

        if (response.getStatus() == 201) {

            String userId = response.getLocation().getPath().split("/")[response.getLocation().getPath().split("/").length - 1];
            System.out.println(response + "   " + userId);
            //userService.registerUser(new User(username,email,password));
            GroupRepresentation group = null;
            try {

                keycloak.realm("SpringApp").users().get(userId).joinGroup(groupId);
                userDetailsService.createUserDetails(userId);

                return "User created successfully";
            } catch (NotFoundException e) {

                // Group does not exist, so create it
                group = new GroupRepresentation();
                group.setName("default");
                keycloak.realm("SpringApp").groups().add(group);
            }

        }
        return "Error: " + response.getStatusInfo().getReasonPhrase();

    }


    public UserRepresentation getUserById(String id){
        UsersResource usersResource= keycloak.realm("SpringApp").users();
        UserResource userResource =usersResource.get(id);
        logger.debug("KeycloakUserService.getUserById( {} ) result {}",id,userResource.toRepresentation());
        return userResource.toRepresentation();
    }




    public List<UserRepresentation> getUsers(){
        UsersResource usersResource=keycloak.realm("SpringApp").users();
        logger.debug("KeycloakUserService.getUsers() result {}",usersResource.list());
        return usersResource.list();
    }



    public UserRepresentation getUserByEmailOrUsername(String usernameOrEmail){
        logger.debug("KeycloakUserService.getUserByEmailOrUsername( {} )",usernameOrEmail);
        List<UserRepresentation> user= keycloak.realm(realm)
                .users()
                .searchByEmail(usernameOrEmail,true);
        if(user.size()==0){
            user=keycloak.realm(realm)
                    .users()
                    .searchByUsername(usernameOrEmail,true);
        }
        logger.debug("KeycloakUserService.getUserByEmailOrUsername result {})",user);
        if(user.size()==0){return null;}
        else{return user.getFirst();}

    }

}

