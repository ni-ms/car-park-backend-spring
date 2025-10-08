package com.carparkspring.demo.repository;

import com.carparkspring.demo.model.WorkerTask;
import com.carparkspring.demo.model.WorkerTask.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerTaskRepository extends JpaRepository<WorkerTask, Long> {
    List<WorkerTask> findByWorkerId(Long workerId);

    List<WorkerTask> findByBookingId(Long bookingId);

    List<WorkerTask> findByTaskStatus(TaskStatus status);

    List<WorkerTask> findByWorkerIdAndTaskStatus(Long workerId, TaskStatus status);
}
