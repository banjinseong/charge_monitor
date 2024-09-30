package charge.station.monitor.config.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    // 허용할 IP 주소 목록
    private final List<String> allowedIps = List.of(
            "192.168.1.1",
            "221.153.222.124",
            "127.0.0.1" // 로컬호스트 등
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 요청 IP 가져오기
        String clientIp = request.getRemoteAddr();
        // 요청 URL 가져오기
        String requestUrl = request.getRequestURL().toString();

        // 로그 출력 (IP, 요청 URL 등)
        log.info("Request from IP: {}, URL: {}", clientIp, requestUrl);

        // 요청 헤더 출력
        log.info("Request Headers:");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            log.info("{}: {}", headerName, headerValue);
        }

        // IP가 허용된 목록에 포함되어 있는지 확인
        if (!allowedIps.contains(clientIp)) {
            // IP가 허용된 목록에 없으면 접근 차단
            log.warn("Access denied for IP: {}", clientIp);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
            response.getWriter().write("Access Denied");
            return; // 요청 처리 중단
        }

        // 다음 필터로 요청을 넘김 (허용된 IP만)
        filterChain.doFilter(request, response);
    }
}