package crypto_analytics.controller.bitfinex;


import crypto_analytics.client.bitfinex.AccountPermissionsClient;
import crypto_analytics.domain.bitfinex.permisions.PermisionsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/crypto")
public class AccountPermissionsController {

    @Autowired
    private AccountPermissionsClient accountPermissionsClient;

    @RequestMapping(method = RequestMethod.GET, value = "/permissions")
    private List<PermisionsDto> getUserPermissions() throws Exception {
        return accountPermissionsClient.getPermisionsList();
    }
}
