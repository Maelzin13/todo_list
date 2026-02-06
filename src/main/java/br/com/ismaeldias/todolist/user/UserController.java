package br.com.ismaeldias.todolist.user;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private IUserRepository userRepository;
 
  /**
   * Cadastro de usuário. JSON deve conter: name, email, password.
   * Login (Basic Auth) usa sempre email + senha.
   */
  @PostMapping("/")
  public ResponseEntity<?> create(@RequestBody UserModel userModel) {
    String email = userModel.getEmail() != null ? userModel.getEmail().trim() : null;
    if (email == null || email.isBlank()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campo email é obrigatório.");
    }
    userModel.setEmail(email);

    var user = this.userRepository.findByEmail(email);
    if (user != null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe com este email.");
    }

    if (userModel.getPassword() == null || userModel.getPassword().isBlank()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campo senha é obrigatório.");
    }

    var passwordHashed = BCrypt.withDefaults()
        .hashToString(12, userModel.getPassword().toCharArray());
    userModel.setPassword(passwordHashed);
    userModel.setDataCadastro(LocalDate.now());

    var userCreated = this.userRepository.save(userModel);
    return ResponseEntity.status(HttpStatus.OK).body(userCreated);
  }
}
