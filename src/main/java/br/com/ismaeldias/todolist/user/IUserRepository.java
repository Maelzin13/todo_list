package br.com.ismaeldias.todolist.user;

import org.springframework.data.jpa.repository.JpaRepository;



public interface IUserRepository extends JpaRepository<UserModel, Long> {

  /** Login: Basic Auth envia email como primeiro campo; tabela financas.usuario tem coluna email. */
  UserModel findByEmail(String email);
}
