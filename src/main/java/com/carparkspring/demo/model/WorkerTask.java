package com.carparkspring.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "worker_tasks")
public class WorkerTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    @JsonIgnore
    private WorkerData worker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    @JsonIgnore
    private CarBookingData booking;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskType taskType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus taskStatus = TaskStatus.PENDING;

    private BigDecimal taskPrice;

    private String notes; 

    private LocalDateTime assignedAt;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    
    public enum TaskType {
        CAR_WASH("Car Wash", 15.0),
        INTERIOR_CLEANING("Interior Cleaning", 20.0),
        FLUID_CHECK("Fluid Check", 10.0),
        TIRE_PRESSURE("Tire Pressure Check", 5.0),
        BATTERY_CHECK("Battery Check", 10.0),
        WIPER_REPLACEMENT("Wiper Replacement", 25.0),
        AIR_FRESHENER("Air Freshener", 5.0),
        VACUUM_CLEANING("Vacuum Cleaning", 12.0),
        POLISH_WAX("Polish & Wax", 50.0),
        ENGINE_WASH("Engine Wash", 30.0);

        private final String displayName;
        private final Double defaultPrice;

        TaskType(String displayName, Double defaultPrice) {
            this.displayName = displayName;
            this.defaultPrice = defaultPrice;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Double getDefaultPrice() {
            return defaultPrice;
        }
    }

    
    public enum TaskStatus {
        PENDING,
        ASSIGNED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }
}
