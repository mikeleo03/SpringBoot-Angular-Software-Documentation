package com.example.lecture_13.config.interceptor;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.example.lecture_13.data.model.APIKey;
import com.example.lecture_13.data.repository.APIKeyRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class APIKeyInterceptor implements HandlerInterceptor {

    private final APIKeyRepository apiKeyRepository;
    private final static Logger logger = LoggerFactory.getLogger(APIKeyInterceptor.class);

    @Autowired
    public APIKeyInterceptor(APIKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
        logger.info("APIKeyRepository injected: {}", (this.apiKeyRepository != null));
    }

    // Request is intercepted by this method before reaching the Controller
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Pre-processing logic here
        // Apply timestamp
        response.addHeader("timestamp", LocalDateTime.now().toString());
        logger.info("[PreHandle] Timestamp applied.");
        logger.info("[PreHandle][" + request + "]" + "[" + request.getMethod()+ "] " + request.getRequestURI());
        return true; // Continue with the request
    }

    // Response is intercepted by this method before reaching the client
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Post-handle logic here
        logger.info("[PostHandle][" + request + "]" + "[" + request.getMethod()+ "] " + request.getRequestURI());
    }

    // This method is called after request & response HTTP communication is done.
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("[AfterCompletion][" + request + "]" + "[" + request.getMethod()+ "] " + request.getRequestURI());

        String username = request.getHeader("api-key-username");
        if (username != null) {
            response.addHeader("username", username);
            logger.info("[AfterCompletion] Username detected : {}", username);
        }

        // Update lastUsedAt in the database
        String requestApiKey = request.getHeader("api-key");
        logger.info("[AfterCompletion] API Key : {}", requestApiKey);
        Optional<APIKey> apiKeyOpt = apiKeyRepository.findFirstByApiKeyAndActiveTrue(requestApiKey);
        if (apiKeyOpt.isPresent()) {
            APIKey apiKey = apiKeyOpt.get();
            apiKey.setUsername(username);
            apiKey.setLastUsedAt(LocalDateTime.now());
            apiKeyRepository.save(apiKey);
            logger.info("[AfterCompletion] API Key data updated : {}", apiKey);
        }

        logger.info("[AfterCompletion] Execution done.");
    }
}
