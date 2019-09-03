package com.drishika.gradzcircle.config;

import com.drishika.gradzcircle.security.*;
import com.drishika.gradzcircle.security.jwt.*;
import com.drishika.gradzcircle.web.rest.AccountResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import javax.annotation.PostConstruct;

@Configuration
@Import(SecurityProblemSupport.class)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UserDetailsService userDetailsService;

    private final TokenProvider tokenProvider;

    private final CorsFilter corsFilter;

    private final SecurityProblemSupport problemSupport;
    
    private final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    public SecurityConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder, UserDetailsService userDetailsService,TokenProvider tokenProvider,CorsFilter corsFilter, SecurityProblemSupport problemSupport) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;
    }

    @PostConstruct
    public void init() {
        try {
            authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        } catch (Exception e) {
            throw new BeanInitializationException("Security configuration failed", e);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/app/**/*.{js,html}")
            .antMatchers("/i18n/**")
            .antMatchers("/content/**")
            .antMatchers("/swagger-ui/index.html")
            .antMatchers("/test/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(problemSupport)
            .accessDeniedHandler(problemSupport)
        .and()
            .csrf()
            .disable()
            .headers()
            .frameOptions()
            .disable()
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/api/activate").permitAll()
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/account/reset-password/init").permitAll()
            .antMatchers("/api/account/reset-password/finish").permitAll()
            .antMatchers("/api/countries/enabled").permitAll()
            .antMatchers("/api/errMsgByComp/corporateRegister").permitAll()
            .antMatchers("/api/profile-info").permitAll()
            .antMatchers("/api/activeJobs").permitAll()
            .antMatchers("/api/countOfActiveJobs").permitAll()
            .antMatchers("/api/files/{id}").permitAll()
            .antMatchers("/api/activeJobsByEmploymentType/{employmentType}/{candidateId}/{matchScoreFrom}/{matchScoreTo}").permitAll()
            .antMatchers("/api/activeJobsByOneJobType/{jobType}/{candidateId}/{matchScoreFrom}/{matchScoreTo}").permitAll()
            .antMatchers("/api/activeJobsByTwoJobTypes/{jobType1}/{jobType2}/{candidateId}/{matchScoreFrom}/{matchScoreTo}").permitAll()
            .antMatchers("/api/activeJobsByThreeJobTypes/{jobType1}/{jobType2}/{jobType3}/{candidateId}/{matchScoreFrom}/{matchScoreTo}").permitAll()
            .antMatchers("/api/activeJobsByOneEmploymentTypeAndOneJobType/{employmentType}/{jobType}/{candidateId}/{matchScoreFrom}/{matchScoreTo}").permitAll()
            .antMatchers("/api/activeJobsByOneEmploymentTypeAndTwoJobType/{employmentType}/{jobType1}/{jobType2}/{candidateId}/{matchScoreFrom}/{matchScoreTo}").permitAll()
            .antMatchers("/api/activeJobsByOneEmploymentTypeAndThreeJobType/{employmentType}/{jobType1}/{jobType2}/{jobType3}/{candidateId}/{matchScoreFrom}/{matchScoreTo}").permitAll()
            .antMatchers("/api/candidatesPreview").permitAll()
            .antMatchers("/api/jobs/{id}").permitAll()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/websocket/tracker").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/websocket/**").permitAll()
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/v2/api-docs/**").permitAll()
            .antMatchers("/swagger-resources/configuration/ui").permitAll()
            .antMatchers("/swagger-ui/index.html").hasAuthority(AuthoritiesConstants.ADMIN)
        .and()
            .apply(securityConfigurerAdapter());

    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }

}
