package br.com.ismaeldias.todolist.user;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Mapeamento da tabela existente financas.usuario (banco já existente).
 * Login por email (Basic Auth envia email:senha).
 */
@Data
@Entity
@Table(schema = "financas", name = "usuario")
public class UserModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nome", length = 150)
  private String name;

  @Column(length = 100)
  private String email;

  @Column(name = "senha", length = 100)
  private String password;

  @Column(name = "data_cadastro")
  private LocalDate dataCadastro;

  /** API e Basic Auth usam "username"; na tabela financas.usuario o campo é email. */
  public String getUsername() {
    return email;
  }

  public void setUsername(String username) {
    this.email = username;
  }
}
