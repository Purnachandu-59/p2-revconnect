////package org.example.fullstackrevconnect.common.security;
////
////import jakarta.servlet.FilterChain;
////import jakarta.servlet.ServletException;
////import jakarta.servlet.http.HttpServletRequest;
////import jakarta.servlet.http.HttpServletResponse;
////import lombok.RequiredArgsConstructor;
////import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
////import org.springframework.security.core.context.SecurityContextHolder;
////import org.springframework.security.core.userdetails.UserDetails;
////import org.springframework.stereotype.Component;
////import org.springframework.web.filter.OncePerRequestFilter;
////
////import java.io.IOException;
////
////@Component
////@RequiredArgsConstructor
////public class JwtFilter extends OncePerRequestFilter {
////
////    private final JwtUtil jwtUtil;
////    private final CustomUserDetailsService userDetailsService;
////
////    @Override
////    protected void doFilterInternal(
////            HttpServletRequest request,
////            HttpServletResponse response,
////            FilterChain chain)
////            throws ServletException, IOException {
////
////        String path = request.getServletPath();
////
////        if (path.startsWith("/api/auth")) {
////            chain.doFilter(request, response);
////            return;
////        }
////
////        String authHeader = request.getHeader("Authorization");
////
////        if (authHeader != null && authHeader.startsWith("Bearer ")) {
////
////            String token = authHeader.substring(7);
////
////            try {
////
////                String username = jwtUtil.extractUsername(token);
////
////                if (username != null &&
////                        SecurityContextHolder.getContext().getAuthentication() == null) {
////
////                    UserDetails userDetails =
////                            userDetailsService.loadUserByUsername(username);
////
////                    if (jwtUtil.validateToken(token, userDetails)) {
////                        UsernamePasswordAuthenticationToken authToken =
////                                new UsernamePasswordAuthenticationToken(
////                                        userDetails,
////                                        null,
////                                        userDetails.getAuthorities());
////
////                        authToken.setDetails(
////                                new org.springframework.security.web.authentication.WebAuthenticationDetailsSource()
////                                        .buildDetails(request)
////                        );
////
////                        SecurityContextHolder.getContext().setAuthentication(authToken);
////                    }
////                }
////
////            } catch (Exception e) {
////                System.out.println("JWT Error: " + e.getMessage());
////            }
////        }
////
////        chain.doFilter(request, response);
////    }
////}
////
//package org.example.fullstackrevconnect.common.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class JwtFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//    private final CustomUserDetailsService userDetailsService;
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain chain)
//            throws ServletException, IOException {
//
//        String path = request.getServletPath();
//
//        if (path.startsWith("/api/auth")) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//
//            String token = authHeader.substring(7);
//
//            try {
//
//                String username = jwtUtil.extractUsername(token);
//
//                if (username != null &&
//                        SecurityContextHolder.getContext().getAuthentication() == null) {
//
//                    UserDetails userDetails =
//                            userDetailsService.loadUserByUsername(username);
//
//                    if (jwtUtil.validateToken(token, userDetails)) {
//
//                        // 🔥 EXTRACT ROLE FROM TOKEN
//                        String role = jwtUtil.extractRole(token);
//
//                        // 🔥 CONVERT ROLE TO SPRING AUTHORITY
//                        List<SimpleGrantedAuthority> authorities =
//                                List.of(new SimpleGrantedAuthority("ROLE_" + role));
//
//                        UsernamePasswordAuthenticationToken authToken =
//                                new UsernamePasswordAuthenticationToken(
//                                        userDetails,
//                                        null,
//                                        authorities   // 👈 IMPORTANT CHANGE
//                                );
//
//                        authToken.setDetails(
//                                new org.springframework.security.web.authentication.WebAuthenticationDetailsSource()
//                                        .buildDetails(request)
//                        );
//
//                        SecurityContextHolder.getContext().setAuthentication(authToken);
//                    }
//                }
//
//            } catch (Exception e) {
//                System.out.println("JWT Error: " + e.getMessage());
//            }
//        }
//
//        chain.doFilter(request, response);
//    }
//}
package org.example.fullstackrevconnect.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    // 🔥 Proper way to skip auth endpoints
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            try {

                String username = jwtUtil.extractUsername(token);

                if (username != null &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {

                    UserDetails userDetails =
                            userDetailsService.loadUserByUsername(username);

                    if (jwtUtil.validateToken(token, userDetails)) {


                        String role = jwtUtil.extractRole(token);


                        List<SimpleGrantedAuthority> authorities =
                                List.of(new SimpleGrantedAuthority("ROLE_" + role));

                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        authorities
                                );

                        authToken.setDetails(
                                new WebAuthenticationDetailsSource()
                                        .buildDetails(request)
                        );

                        SecurityContextHolder.getContext()
                                .setAuthentication(authToken);
                    }
                }

            } catch (Exception e) {
                System.out.println("JWT Error: " + e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }
}