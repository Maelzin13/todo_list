package br.com.ismaeldias.todolist.task;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(schema = "financas", name = "tasks")
public class TaskModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 50)
  private String title;

  @Column(length = 255)
  private String description;

  @Column(name = "start_at")
  private LocalDateTime startAt;

  @Column(name = "end_at")
  private LocalDateTime endAt;

  @Column(length = 20)
  private String priority;

  @Column(name = "id_usuario", nullable = false)
  private Long idUser;

  @Column(name = "is_complete", nullable = false)
  private Boolean isComplete = false;

  @CreationTimestamp
  @Column(name = "data_cadastro")
  private LocalDateTime createdAt;

  public void setTitle(String title) throws Exception {
    if (title != null && title.length() > 50) {
      throw new Exception("O campo title deve conter no máximo 50 caracteres.");
    }
    this.title = title;
  }
}
