package com.it_num.crud_api.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tags", indexes = {
        @Index(name = "idx_tags_public_id", columnList = "public_id", unique = true),
        @Index(name = "idx_tags_name", columnList = "name", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "publicId", "name"})
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", columnDefinition = "UUID",
            updatable = false, nullable = false, unique = true)
    private UUID publicId;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 7)
    private String color;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Task> tasks = new HashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        if (this.publicId == null) {
            this.publicId = UUID.randomUUID();
        }
    }
}