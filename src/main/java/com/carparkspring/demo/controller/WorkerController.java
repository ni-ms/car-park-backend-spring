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

@RestController
@RequestMapping("/worker")
@Tag(name = "Worker Operations", description = "Worker scheduling and assignment (NOT registration)")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    // ===========================================
    // GET ALL WORKERS (Admin Only)
    // ===========================================
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get all workers", description = "Get all workers in system")
    public List<WorkerData> getAllWorkers() {
        return workerService.getAllWorkers();
    }

    // ===========================================
    // GET WORKER BY ID
    // ===========================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORKER')")
    @Operation(summary = "Get worker by ID")
    public WorkerData getWorkerById(@PathVariable Long id) {
        return workerService.getWorkerById(id)
                .orElseThrow(() -> new RuntimeException("Worker not found"));
    }

    // ===========================================
    // GET WORKERS BY PARKING
    // ===========================================
    @GetMapping("/parking/{parkingId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORKER')")
    @Operation(summary = "Get workers by parking", description = "Get all workers assigned to a parking facility")
    public List<WorkerData> getWorkersByParking(@PathVariable Long parkingId) {
        return workerService.getWorkersByParkingId(parkingId);
    }

    // ===========================================
    // ASSIGN WORKER TO PARKING (Admin Only)
    // ===========================================
    @PutMapping("/{workerId}/assign-parking/{parkingId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Assign worker to parking", description = "Assign a worker to a parking facility")
    public WorkerData assignWorkerToParking(@PathVariable Long workerId, @PathVariable Long parkingId) {
        return workerService.assignToParking(workerId, parkingId);
    }

    // ===========================================
    // UPDATE DUTY STATUS
    // ===========================================
    @PutMapping("/{workerId}/duty-status")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORKER')")
    @Operation(summary = "Update duty status", description = "Mark worker as on-duty or off-duty")
    public WorkerData updateDutyStatus(@PathVariable Long workerId, @RequestParam boolean onDuty) {
        workerService.updateWorkerStatus(workerId, onDuty);
        return workerService.getWorkerById(workerId)
                .orElseThrow(() -> new RuntimeException("Worker not found"));
    }

    // ===========================================
    // UPDATE WORKER SHIFT (Admin Only)
    // ===========================================
    @PutMapping("/{workerId}/shift")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Update worker shift", description = "Update worker's shift schedule")
    public WorkerData updateShift(@PathVariable Long workerId, @RequestParam String shift) {
        return workerService.updateShift(workerId, shift);
    }

    // ===========================================
    // UPDATE WORKER POSITION (Admin Only)
    // ===========================================
    @PutMapping("/{workerId}/position")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Update worker position", description = "Update worker's job position")
    public WorkerData updatePosition(@PathVariable Long workerId, @RequestParam String position) {
        return workerService.updatePosition(workerId, position);
    }

    // ===========================================
    // GET WORKER DETAILS
    // ===========================================
    @GetMapping("/{workerId}/details")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORKER')")
    @Operation(summary = "Get worker details", description = "Get detailed worker information")
    public Map<String, Object> getWorkerDetails(@PathVariable Long workerId) {
        return workerService.getWorkerDetails(workerId);
    }

    // ===========================================
    // GET ON-DUTY WORKERS (Admin/Worker)
    // ===========================================
    @GetMapping("/on-duty")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORKER')")
    @Operation(summary = "Get on-duty workers", description = "Get all currently on-duty workers")
    public List<WorkerData> getOnDutyWorkers() {
        return workerService.getOnDutyWorkers();
    }
}
