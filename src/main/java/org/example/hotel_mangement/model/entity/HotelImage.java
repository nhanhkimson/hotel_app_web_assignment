package org.example.hotel_mangement.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "hotel_image")
@Data
public class HotelImage {
    @Id
    @UuidGenerator
    @Column(name = "image_id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID imageId;

    @ManyToOne
    @JoinColumn(name = "hotel_code", nullable = false, columnDefinition = "uuid")
    @JsonIgnore
    private Hotel hotel;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "file_name", length = 255)
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(name = "upload_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;
}


