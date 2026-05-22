package com.it_num.crud_api.domain;

import com.it_num.crud_api.domain.enums.Priority;
import com.it_num.crud_api.domain.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tasks", indexes = {
        @Index(name = "idx_tasks_public_id", columnList = "public_id", unique = true),
        @Index(name = "idx_tasks_project_id", columnList = "project_id"),
        @Index(name = "idx_tasks_status", columnList = "status"),
        @Index(name = "idx_tasks_priority", columnList = "priority")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "publicId", "title", "status", "priority"})
public class Task {

    // === Identifiants ===

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", columnDefinition = "UUID",
            updatable = false, nullable = false, unique = true)
    private UUID publicId;

    // === Champs obligatoires ===

    @Column(nullable = false, length = 200)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    // === Champs optionnels ===

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TaskStatus status = TaskStatus.TODO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Priority priority = Priority.MEDIUM;

    @Column(name = "due_date")
    private LocalDate dueDate;

    // === Relation N-N avec Tag ===

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "task_tags",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"),
            indexes = {
                    @Index(name = "idx_task_tags_task_id", columnList = "task_id"),
                    @Index(name = "idx_task_tags_tag_id", columnList = "tag_id")
            }
    )
    private Set<Tag> tags = new HashSet<>();

    // === Timestamps ===

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    // === Lifecycle callbacks ===

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        if (this.publicId == null) {
            this.publicId = UUID.randomUUID();
        }
        if (this.status == null) {
            this.status = TaskStatus.TODO;
        }
        if (this.priority == null) {
            this.priority = Priority.MEDIUM;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // === Helper methods pour gérer les tags ===

    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getTasks().add(this);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getTasks().remove(this);
    }
}