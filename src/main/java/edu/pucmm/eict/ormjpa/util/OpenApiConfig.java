package edu.pucmm.eict.ormjpa.util;

import com.fasterxml.jackson.databind.node.TextNode;
import edu.pucmm.eict.ormjpa.Main;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.OpenApiPluginConfiguration;
import io.javalin.security.RouteRole;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class OpenApiConfig extends OpenApiPlugin {

    enum Rules implements RouteRole {
        ANONYMOUS,
        USER,
    }

    public OpenApiConfig(@NotNull Consumer<OpenApiPluginConfiguration> openApiPluginConfigurationConsumer) {
        super(openApiPluginConfigurationConsumer);
        openApiPluginConfigurationConsumer.andThen(openApiConf -> {
            openApiConf
                    .withRoles(Rules.ANONYMOUS)
                    .withDefinitionConfiguration((version, openApiDefinition) -> {
                        openApiDefinition
                                .withInfo(openApiInfo ->
                                        openApiInfo
                                                .description("Proyecto demostración sobre ORM con Javalin")
                                                .termsOfService("https://example.com/tos")
                                                .contact("API Support", "https://www.example.com/support", "support@example.com")
                                                .license("Apache 2.0", "https://www.apache.org/licenses/", "Apache-2.0")
                                )
                                .withServer(openApiServer ->
                                                openApiServer
                                                        .description("Server description goes here")
                                                        .url("http://localhost:{port}/")
                                                        //.url("http://localhost:{port}{basePath}/" + version + "/")
                                                        .variable("port", "Server's port", "7000", "8080", "7000")
                                        //.variable("basePath", "Base path of the server", "", "", "v1")
                                )
                                .withSecurity(openApiSecurity ->
                                        openApiSecurity
                                                .withBasicAuth()
                                                .withBearerAuth()
                                                .withApiKeyAuth("ApiKeyAuth", "X-Api-Key")
                                                .withCookieAuth("CookieAuth", "JSESSIONID")
                                                .withOpenID("OpenID", "https://example.com/.well-known/openid-configuration")
                                                .withOAuth2("OAuth2", "This API uses OAuth 2 with the implicit grant flow.", oauth2 ->
                                                        oauth2
                                                                .withClientCredentials("https://api.example.com/credentials/authorize")
                                                                .withImplicitFlow("https://api.example.com/oauth2/authorize", flow ->
                                                                        flow
                                                                                .withScope("read_pets", "read your pets")
                                                                                .withScope("write_pets", "modify pets in your account")
                                                                )
                                                )
                                                .withGlobalSecurity("OAuth2", globalSecurity ->
                                                        globalSecurity
                                                                .withScope("write_pets")
                                                                .withScope("read_pets")
                                                )
                                )
                                .withDefinitionProcessor(content -> { // you can add whatever you want to this document using your favourite json api
                                    content.set("test", new TextNode("Value"));
                                    return content.toPrettyString();
                                });
                    });
        });

    }
}
