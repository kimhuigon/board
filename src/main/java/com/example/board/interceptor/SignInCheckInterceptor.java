package com.example.board.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.board.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SignInCheckInterceptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response,
      Object handler) throws Exception {
    HttpSession session = request.getSession();
    User user = (User) session.getAttribute("user_info");
    if (user == null) {
      response.sendRedirect("/signin");
      return false;
    }
    return true;
  }
}