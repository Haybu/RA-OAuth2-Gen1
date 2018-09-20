package io.agilehandy.agency;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@Slf4j
public class HomeController {

    @GetMapping(path="/home", produces="application/json")
    @SuppressWarnings("unchecked")
    public Map<String, Object> home(Principal principal) {
        log.info("Getting through gateway home controller");
        OAuth2Authentication authentication = (OAuth2Authentication) principal;
        Map<String, Object> user =
                (Map<String, Object>) authentication.getUserAuthentication().getDetails();
        return user;
    }

}
