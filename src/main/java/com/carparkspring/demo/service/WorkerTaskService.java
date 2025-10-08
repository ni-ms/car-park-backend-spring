package com.carparkspring.demo.service;

import com.carparkspring.demo.model.CarBookingData;
import com.carparkspring.demo.model.WorkerData;
import com.carparkspring.demo.model.WorkerTask;
import com.carparkspring.demo.repository.CarBookingDataRepository;
import com.carparkspring.demo.repository.WorkerDataRepository;
import com.carparkspring.demo.repository.WorkerTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class WorkerTaskService {

    @Autowired
    private WorkerTaskRepository taskRepository;

    @Autowired
    private WorkerDataRepository workerRepository;

    @Autowired
    private CarBookingDataRepository bookingRepository;

    @Transactional
    public WorkerTask assignTask(Long workerId, Long bookingId, WorkerTask.TaskType taskType, String notes) {
        log.info("Assigning task {} to worker {} for booking {}", taskType, workerId, bookingId);

        WorkerData worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        CarBookingData booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        WorkerTask task = new WorkerTask();
        task.setWorker(worker);
        task.setBooking(booking);
        task.setTaskType(taskType);
        task.setTaskStatus(WorkerTask.TaskStatus.ASSIGNED);
        task.setTaskPrice(BigDecimal.valueOf(taskType.getDefaultPrice()));
        task.setNotes(notes);
        task.setAssignedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    @Transactional
    public WorkerTask startTask(Long taskId) {
        WorkerTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTaskStatus(WorkerTask.TaskStatus.IN_PROGRESS);
        task.setStartedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    @Transactional
    public WorkerTask completeTask(Long taskId) {
        WorkerTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTaskStatus(WorkerTask.TaskStatus.COMPLETED);
        task.setCompletedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    public List<WorkerTask> getWorkerTasks(Long workerId) {
        return taskRepository.findByWorkerId(workerId);
    }

    public List<WorkerTask> getBookingTasks(Long bookingId) {
        return taskRepository.findByBookingId(bookingId);
    }

    public List<WorkerTask> getPendingTasks() {
        return taskRepository.findByTaskStatus(WorkerTask.TaskStatus.PENDING);
    }

    public BigDecimal calculateTotalTaskCost(Long bookingId) {
        List<WorkerTask> tasks = taskRepository.findByBookingId(bookingId);
        return tasks.stream()
                .map(WorkerTask::getTaskPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
