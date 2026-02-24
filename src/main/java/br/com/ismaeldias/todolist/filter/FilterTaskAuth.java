package br.com.ismaeldias.todolist.filter;


import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.ismaeldias.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(2) // Roda depois do CorsFilter (order 1 ou default)
public class FilterTaskAuth extends OncePerRequestFilter{

  private static final List<String> ALLOWED_ORIGINS = List.of(
      "https://ignite-todo-app-gold.vercel.app",
      "http://localhost:3000",
      "http://localhost:5173",
      "http://localhost:8100",
      "http://127.0.0.1:8100"
  );

  @Autowired
  private IUserRepository userRepository;

  /** Adiciona headers CORS na resposta para o browser não bloquear (mesmo em 401). */
  private void addCorsHeaders(HttpServletRequest request, HttpServletResponse response) {
    String origin = request.getHeader("Origin");
    if (origin != null && ALLOWED_ORIGINS.contains(origin)) {
      response.setHeader("Access-Control-Allow-Origin", origin);
    } else {
      response.setHeader("Access-Control-Allow-Origin", "http://localhost:8100");
    }
    response.setHeader("Access-Control-Allow-Credentials", "true");
    response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
    response.setHeader("Access-Control-Allow-Headers", "*");
  }

  private void sendErrorWithCors(HttpServletRequest request, HttpServletResponse response, int sc) throws IOException {
    addCorsHeaders(request, response);
    response.sendError(sc);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

      var serletPath = request.getServletPath();

      // Permite OPTIONS (preflight CORS) passar sem autenticação
      if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
        filterChain.doFilter(request, response);
        return;
      }

      if (serletPath.startsWith("/tasks/")) {

        String email = null;
        String password = null;

        // 1) Tenta Authorization: Basic base64(email:password)
        var authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Basic ")) {
          var authEncoded = authorization.substring("Basic ".length()).trim();
          byte[] authDecoded = Base64.getDecoder().decode(authEncoded);
          var authString = new String(authDecoded);
          int colon = authString.indexOf(':');
          if (colon >= 0) {
            email = authString.substring(0, colon).trim();
            password = authString.substring(colon + 1);
          }
        }

        // 2) Se não veio Basic Auth, tenta headers Username e Password (ex.: X-Username, X-Password)
        if (email == null || password == null) {
          String u = request.getHeader("X-Username");
          String p = request.getHeader("X-Password");
          if (u != null && !u.isBlank() && p != null) {
            email = u.trim();
            password = p;
          }
        }

        if (email == null || password == null || password.isBlank()) {
          sendErrorWithCors(request, response, 401);
          return;
        }

        var user = this.userRepository.findByEmail(email);
        if (user == null) {
          sendErrorWithCors(request, response, 401);
          return;
        }
        var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
        if (passwordVerify.verified) {
          request.setAttribute("idUser", user.getId());
          filterChain.doFilter(request, response);
        } else {
          sendErrorWithCors(request, response, 401);
        }
      } else {
        filterChain.doFilter(request, response);
      }
  }




  
}
