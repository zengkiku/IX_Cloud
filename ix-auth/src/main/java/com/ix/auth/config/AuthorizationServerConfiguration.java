package com.ix.auth.config;

import com.ix.auth.support.CustomeOAuth2AccessTokenGenerator;
import com.ix.auth.support.core.CustomOAuth2TokenCustomizer;
import com.ix.auth.support.core.FormIdentityLoginConfigurer;
import com.ix.auth.support.core.IXDaoAuthenticationProvider;
import com.ix.auth.support.handler.IXAuthenticationFailureEventHandler;
import com.ix.auth.support.handler.IXAuthenticationSuccessEventHandler;
import com.ix.auth.support.password.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import com.ix.auth.support.password.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import com.ix.auth.support.sms.OAuth2ResourceOwnerSmsAuthenticationConverter;
import com.ix.auth.support.sms.OAuth2ResourceOwnerSmsAuthenticationProvider;
import com.ix.framework.core.constants.SecurityConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.util.Arrays;

/**
 * @author Zeng IX
 * @email zeng_kiku@qq.com
 * @Description: ????????????
 */
@Configuration
@RequiredArgsConstructor
public class AuthorizationServerConfiguration {

    private final OAuth2AuthorizationService oAuth2AuthorizationService;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        // ???????????????????????????
        http.apply(authorizationServerConfigurer.tokenEndpoint((tokenEndpoint) -> {
            // ??????????????????????????????Converter
            tokenEndpoint.accessTokenRequestConverter(accessTokenRequestConverter())
                    .accessTokenResponseHandler(new IXAuthenticationSuccessEventHandler()) // ?????????????????????
                    .errorResponseHandler(new IXAuthenticationFailureEventHandler()); // ?????????????????????
        }).clientAuthentication(oAuth2ClientAuthenticationConfigurer -> { // ????????????????????????
            oAuth2ClientAuthenticationConfigurer.errorResponseHandler(new IXAuthenticationFailureEventHandler());  // ???????????????????????????
        }).authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint.consentPage(SecurityConstants.CUSTOM_CONSENT_PAGE_URI))); // ????????????????????????confirm??????

        DefaultSecurityFilterChain securityFilterChain = http.authorizeHttpRequests(authorizeRequests -> {
                    // ??????????????????????????????
                    authorizeRequests.requestMatchers("/api/token/*", "/token/**", "/actuator/**", "/assets/**", "/error",
                            "/v3/api-docs").permitAll();
                    authorizeRequests.anyRequest().authenticated();
                })
                .apply(authorizationServerConfigurer.authorizationService(oAuth2AuthorizationService)
                        .authorizationServerSettings(AuthorizationServerSettings.builder()
                                .issuer(SecurityConstants.PROJECT_LICENSE).build()))
                .and()
                .apply(new FormIdentityLoginConfigurer()).and().build(); // ????????????????????????????????????

        // ?????????????????????????????????
        addCustomOAuth2GrantAuthenticationProvider(http);
        return securityFilterChain;
    }

    /**
     * ???????????????????????? </br>
     * client:username:uuid
     *
     * @return OAuth2TokenGenerator
     */
    @Bean
    public OAuth2TokenGenerator oAuth2TokenGenerator() {
        CustomeOAuth2AccessTokenGenerator accessTokenGenerator = new CustomeOAuth2AccessTokenGenerator();
        // ??????Token ????????????????????????
        accessTokenGenerator.setAccessTokenCustomizer(new CustomOAuth2TokenCustomizer());
        return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, new OAuth2RefreshTokenGenerator());
    }

    /**
     * request -> xToken ?????????????????????
     *
     * @return DelegatingAuthenticationConverter
     */
    private AuthenticationConverter accessTokenRequestConverter() {
        return new DelegatingAuthenticationConverter(Arrays.asList(
                // ????????????
                new OAuth2ResourceOwnerPasswordAuthenticationConverter(),
                // ????????????
                new OAuth2ResourceOwnerSmsAuthenticationConverter(),
                // ????????????
                new OAuth2RefreshTokenAuthenticationConverter(),
                // ???????????????
                new OAuth2AuthorizationCodeAuthenticationConverter(),
                // ???????????????
                new OAuth2ClientCredentialsAuthenticationConverter(),
                new OAuth2AuthorizationCodeRequestAuthenticationConverter())
        );
    }

    /**
     * ?????????????????????????????????
     * <p>
     * 1. ???????????? </br>
     * 2. ???????????? </br>
     */
    @SuppressWarnings("unchecked")
    private void addCustomOAuth2GrantAuthenticationProvider(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);

        OAuth2ResourceOwnerPasswordAuthenticationProvider resourceOwnerPasswordAuthenticationProvider =
                new OAuth2ResourceOwnerPasswordAuthenticationProvider(authenticationManager, authorizationService, oAuth2TokenGenerator());

        OAuth2ResourceOwnerSmsAuthenticationProvider resourceOwnerSmsAuthenticationProvider =
                new OAuth2ResourceOwnerSmsAuthenticationProvider(authenticationManager, authorizationService, oAuth2TokenGenerator());

        // ?????? UsernamePasswordAuthenticationToken
        http.authenticationProvider(new IXDaoAuthenticationProvider());
        // ?????? OAuth2ResourceOwnerPasswordAuthenticationToken
        http.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);
        // ?????? OAuth2ResourceOwnerSmsAuthenticationToken
        http.authenticationProvider(resourceOwnerSmsAuthenticationProvider);
    }

}