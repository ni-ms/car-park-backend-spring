package com.carparkspring.demo.controller;

import com.carparkspring.demo.model.WorkerTask;
import com.carparkspring.demo.service.WorkerTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/worker-task")
@Tag(name = "Worker Tasks", description = "Manage worker tasks and services")
public class WorkerTaskController {

    @Autowired
    private WorkerTaskService taskService;

    @PostMapping("/assign")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORKER')")
    @Operation(summary = "Assign task to worker", description = "Assign a service task to a worker for a booking")
    public WorkerTask assignTask(@RequestBody TaskAssignmentRequest request) {
        return taskService.assignTask(
                request.getWorkerId(),
                request.getBookingId(),
                request.getTaskType(),
                request.getNotes()
        );
    }

    @PutMapping("/{taskId}/start")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORKER')")
    @Operation(summary = "Start task", description = "Mark task as in progress")
    public WorkerTask startTask(@PathVariable Long taskId) {
        return taskService.startTask(taskId);
    }

    @PutMapping("/{taskId}/complete")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORKER')")
    @Operation(summary = "Complete task", description = "Mark task as completed")
    public WorkerTask completeTask(@PathVariable Long taskId) {
        return taskService.completeTask(taskId);
    }

    @GetMapping("/worker/{workerId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORKER')")
    @Operation(summary = "Get worker tasks", description = "Get all tasks assigned to a worker")
    public List<WorkerTask> getWorkerTasks(@PathVariable Long workerId) {
        return taskService.getWorkerTasks(workerId);
    }

    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Get booking tasks", description = "Get all tasks for a booking")
    public List<WorkerTask> getBookingTasks(@PathVariable Long bookingId) {
        return taskService.getBookingTasks(bookingId);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORKER')")
    @Operation(summary = "Get pending tasks", description = "Get all pending tasks")
    public List<WorkerTask> getPendingTasks() {
        return taskService.getPendingTasks();
    }

    @GetMapping("/booking/{bookingId}/total-cost")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Calculate task costs", description = "Calculate total cost of all tasks for a booking")
    public BigDecimal calculateTotalCost(@PathVariable Long bookingId) {
        return taskService.calculateTotalTaskCost(bookingId);
    }

    @Data
    public static class TaskAssignmentRequest {
        private Long workerId;
        private Long bookingId;
        private WorkerTask.TaskType taskType;
        private String notes;
    }
}
