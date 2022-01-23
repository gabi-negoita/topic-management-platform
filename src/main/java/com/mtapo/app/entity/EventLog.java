package com.mtapo.app.entity;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "event_log")
public class EventLog {

    public static String OPERATION_INSERT = "insert";
    public static String OPERATION_UPDATE = "update";
    public static String OPERATION_DELETE = "delete";

    public static String STATUS_SUCCESS = "success";
    public static String STATUS_FAILED = "failed";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "operation", nullable = false)
    private String operation;

    @Column(name = "operation_status", nullable = false)
    private String status;

    @Lob
    @Column(name = "details", nullable = false)
    private String details;

    @Value("#{T(System).currentTimeMillis()}")
    @Column(name = "created_on", nullable = false)
    private Timestamp date;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
}
