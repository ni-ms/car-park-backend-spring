package com.carparkspring.demo.controller;

import com.carparkspring.demo.model.WorkerData;
import com.carparkspring.demo.service.WorkerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/worker")
@Tag(name = "Worker", description = "Worker management APIs")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORKER')")
    @Operation(summary = "Get worker by ID", description = "Retrieve a specific worker by ID")
    public Optional<WorkerData> getWorkerById(@PathVariable Long id) {
        return workerService.getWorkerById(id);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get all workers", description = "Retrieve all workers")
    public List<WorkerData> getAllWorkers() {
        return workerService.getAllWorkers();
    }

    @GetMapping("/parking/{parkingId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORKER')")
    @Operation(summary = "Get workers by parking", description = "Get all workers assigned to a specific parking facility")
    public List<WorkerData> getWorkersByParkingId(@PathVariable Long parkingId) {
        return workerService.getWorkersByParkingId(parkingId);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Create worker", description = "Create a new worker")
    public WorkerData createWorker(@RequestBody WorkerData worker) {
        return workerService.saveWorker(worker);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Update worker", description = "Update an existing worker")
    public WorkerData updateWorker(@RequestBody WorkerData worker) {
        return workerService.saveWorker(worker);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORKER')")
    @Operation(summary = "Update worker duty status", description = "Update worker on-duty status")
    public String updateWorkerStatus(@PathVariable Long id, @RequestParam boolean onDuty) {
        workerService.updateWorkerStatus(id, onDuty);
        return "Worker status updated successfully";
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Delete worker", description = "Delete a worker")
    public String deleteWorker(@PathVariable Long id) {
        workerService.deleteWorker(id);
        return "Worker deleted successfully";
    }

    @GetMapping("/{id}/details")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORKER')")
    @Operation(summary = "Get worker details", description = "Get detailed information about a worker")
    public Map<String, Object> getWorkerDetails(@PathVariable Long id) {
        return workerService.getWorkerDetails(id);
    }
}
