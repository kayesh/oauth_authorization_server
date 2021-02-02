package com.example.ssc26as.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableAuthorizationServer
public class AuthConfig extends AuthorizationServerConfigurerAdapter {

  @Autowired
  public AuthenticationManager authenticationManager;

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.inMemory()
            .withClient("client1")
            .secret("secret1")
            .authorizedGrantTypes("password")
            .scopes("read");
  }


  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints.authenticationManager(authenticationManager)
             .tokenStore(tokenStore())
             .accessTokenConverter(converter());

//    // enable cors for "/oauth/token"
//    Map<String, CorsConfiguration> corsConfigMap = new HashMap<>();
//    CorsConfiguration config = new CorsConfiguration();
//    config.setAllowCredentials(true);
//
//    config.setAllowedOrigins(Collections.singletonList("*"));
//    config.setAllowedMethods(Collections.singletonList("*"));
//    config.setAllowedHeaders(Collections.singletonList("*"));
//    corsConfigMap.put("/oauth/token", config);
//    endpoints.getFrameworkEndpointHandlerMapping()
//            .setCorsConfigurations(corsConfigMap);

  }

  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(converter());
  }

  @Bean
  public JwtAccessTokenConverter converter() {
    var c = new JwtAccessTokenConverter();

    c.setSigningKey("ymLTU8rq83j4fmJZj60wh4OrMNuntIj4fmJ");

    return c;
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()");

  }

//  @Bean
//  public CorsConfigurationSource corsConfigurationSource() {
//    CorsConfiguration configuration = new CorsConfiguration();
//    configuration.setAllowedOrigins(List.of("*"));
//    configuration.addAllowedHeader("*");
//    configuration.addAllowedMethod("*");
//    configuration.setAllowCredentials(true);
//    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//    source.registerCorsConfiguration("/**", configuration);
//    return source;
//  }
}