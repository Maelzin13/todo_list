package br.com.ismaeldias.todolist.filter;


import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.ismaeldias.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter{

  @Autowired
  private IUserRepository userRepository;


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

        var authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Basic ")) {
          response.sendError(401);
          return;
        }

        var authEncoded = authorization.substring("Basic ".length()).trim();
        byte[] authDecoded = Base64.getDecoder().decode(authEncoded);
        var authString = new String(authDecoded);
        // Basic Auth: primeira parte = email, segunda = senha (split no primeiro ":" para senhas com ":")
        int colon = authString.indexOf(':');
        if (colon < 0) {
          response.sendError(401);
          return;
        }
        String email = authString.substring(0, colon).trim();
        String password = authString.substring(colon + 1);

        var user = this.userRepository.findByEmail(email);
        if (user == null) {
          response.sendError(401);
          return;
        }
        var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
        if (passwordVerify.verified) {
          request.setAttribute("idUser", user.getId());
          filterChain.doFilter(request, response);
        } else {
          response.sendError(401);
        }
      } else { 
        filterChain.doFilter(request, response);
      }
        

  }




  
}
