package com.kamar.issuemanagementsystem.global_configuration;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * The GlobalCspConfiguration class is a configuration class that configures the Content Security Policy (CSP) filter
 * for the application.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@Configuration
@RequiredArgsConstructor
public class GlobalCspConfiguration {

    private final CspFilter cspFilter;

    /**
     * Configures the Content Security Policy (CSP) filter for the application.
     *
     * @return The FilterRegistrationBean instance containing the configured CSP filter.
     * @see FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean<Filter> configureCsp(){

        /*create an instance of filter registration bean*/
        FilterRegistrationBean<Filter> filterRegistration = new FilterRegistrationBean<>();
        /*set the csp filter*/
        filterRegistration.setFilter(cspFilter);
        /*set the order*/
        filterRegistration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        /*return*/
        return filterRegistration;
    }
}



/**
 * The CspConfigurationFilter class is a filter that sets the content security policy headers for each HTTP request
 * and continues the filter chain.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@Component
class CspFilter extends OncePerRequestFilter {
    /**
     * Sets the content security policy headers and continues the filter chain.
     *
     * @param request the HTTP servlet request
     * @param response the HTTP servlet response
     * @param filterChain the filter chain for the request
     * @throws ServletException if the servlet encounters difficulty
     * @throws IOException if an I/O exception occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        /*set the CSP (content security policy headers)*/
        response.setHeader("Content-Security-Policy", "default-src 'self'; Script-src 'self'; Style-src 'self'; ");
        filterChain.doFilter(request, response);

    }
}
