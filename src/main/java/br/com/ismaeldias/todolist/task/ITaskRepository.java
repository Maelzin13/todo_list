package br.com.ismaeldias.todolist.task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ITaskRepository extends JpaRepository<TaskModel, Long> {

  List<TaskModel> findByIdUser(Long idUser);
}
