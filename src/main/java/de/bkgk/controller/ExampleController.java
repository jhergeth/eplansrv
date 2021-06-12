package de.bkgk.controller;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.authentication.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;

@Controller("/api/test")
public class ExampleController {
    private static final Logger LOG = LoggerFactory.getLogger(ExampleController.class);

    @Get("/admin")
    @RolesAllowed({"EPlanAdmin"})
    public Map withroles(@Nullable Principal principal) {
        LOG.debug("Withroles");
        return returnUserMap(principal, "You have ROLE_ADMIN or ROLE_X roles");
    }

    @Get("/all")
    @PermitAll
    public Map anonymous(@Nullable Principal principal) {
        LOG.debug("Anonymous");
        return returnUserMap(principal,"You are anonymous");
    }

    @Get("/user")
    @RolesAllowed({"EPlan"})
    public Map user(@Nullable Principal principal, Authentication authentication) {
        LOG.debug("user authenticated: {}", authentication.getName());
        return returnUserMap(principal,authentication.getName() + " is authenticated");
    }

    @Get("/mod")
    @RolesAllowed({"EPlanManager"})
    public Map moderator(@Nullable Principal principal, Authentication authentication) {
        LOG.debug("moderator authenticated: {}", authentication.getName());
        return returnUserMap(principal,authentication.getName() + " is authenticated");
    }

    private Map returnUserMap(@Nullable Principal principal, String txt){
        if (principal == null) {
            return Collections.singletonMap("isLoggedIn", false);
        }
        return CollectionUtils.mapOf(
                "isLoggedIn", true,
                "username", principal.getName(),
                "message", txt
        );
    }
}
