package com.carparkspring.demo.service;

import com.carparkspring.demo.model.WorkerData;
import com.carparkspring.demo.model.ParkingSlot;
import com.carparkspring.demo.model.CarBookingData;
import com.carparkspring.demo.repository.WorkerDataRepository;
import com.carparkspring.demo.repository.ParkingSlotRepository;
import com.carparkspring.demo.repository.CarBookingDataRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class WorkerService {

    @Autowired
    private WorkerDataRepository workerDataRepository;

    @Autowired
    private ParkingSlotRepository parkingSlotRepository;

    @Autowired
    private CarBookingDataRepository carBookingDataRepository;

    private final Counter workerCounter;

    public WorkerService(MeterRegistry meterRegistry) {
        this.workerCounter = Counter.builder("workers.created")
                .description("Total number of workers created")
                .register(meterRegistry);
    }

    @Cacheable(value = "worker", key = "#id")
    public Optional<WorkerData> getWorkerById(Long id) {
        log.debug("Fetching worker by id: {}", id);
        return workerDataRepository.findById(id);
    }

    public List<WorkerData> getAllWorkers() {
        log.debug("Fetching all workers");
        return workerDataRepository.findAll();
    }

    public List<WorkerData> getWorkersByParkingId(Long parkingId) {
        log.debug("Fetching workers for parking: {}", parkingId);
        return workerDataRepository.findByParkingId(parkingId);
    }

    @Transactional
    @CacheEvict(value = "worker", allEntries = true)
    public WorkerData saveWorker(WorkerData workerData) {
        log.info("Saving worker: {}", workerData.getName());
        WorkerData saved = workerDataRepository.save(workerData);
        workerCounter.increment();
        return saved;
    }

    @Transactional
    @CacheEvict(value = "worker", allEntries = true)
    public void deleteWorker(Long id) {
        log.info("Deleting worker: {}", id);

        if (!workerDataRepository.existsById(id)) {
            throw new RuntimeException("Worker not found with id: " + id);
        }

        workerDataRepository.deleteById(id);
        log.info("Worker deleted successfully: {}", id);
    }

    @Transactional
    @CacheEvict(value = "worker", key = "#workerId")
    public void updateWorkerStatus(Long workerId, boolean onDuty) {
        log.info("Updating worker status - Worker: {}, OnDuty: {}", workerId, onDuty);

        WorkerData worker = workerDataRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        worker.setOnDuty(onDuty);
        workerDataRepository.save(worker);
    }

    @Cacheable(value = "workerDetails", key = "#workerId")
    public Map<String, Object> getWorkerDetails(Long workerId) {
        log.debug("Fetching worker details for worker: {}", workerId);

        Optional<WorkerData> workerDataOptional = workerDataRepository.findById(workerId);
        if (!workerDataOptional.isPresent()) {
            log.warn("Worker not found: {}", workerId);
            return null;
        }

        WorkerData workerData = workerDataOptional.get();
        Map<String, Object> workerDetails = new HashMap<>();
        workerDetails.put("id", workerData.getId());
        workerDetails.put("name", workerData.getName());
        workerDetails.put("position", workerData.getPosition());
        workerDetails.put("contactNumber", workerData.getContactNumber());
        workerDetails.put("onDuty", workerData.isOnDuty());

        if (workerData.getParking() != null) {
            workerDetails.put("parkingId", workerData.getParking().getId());
            workerDetails.put("parkingName", workerData.getParking().getName());
        }

        List<CarBookingData> assignedBookings = workerData.getCarBookings();
        workerDetails.put("totalAssignedBookings", assignedBookings.size());

        return workerDetails;
    }
}
