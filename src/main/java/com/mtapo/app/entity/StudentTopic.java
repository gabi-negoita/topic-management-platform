package com.mtapo.app.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "student_topic")
public class StudentTopic {

    public static final String STATUS_WAITING = "waiting";
    public static final String STATUS_APPROVED = "approved";
    public static final String STATUS_DECLINED = "declined";
    public static final String STATUS_CANCELED = "canceled";

    public static final String REASON_REQUEST_DECLINED = "The teacher declined the application request";
    public static final String REASON_STUDENT_CANCELED_APPROVED_TOPIC = "The student cancelled an approved topic";
    public static final String REASON_TEACHER_CANCELED_APPROVED_TOPIC = "The teacher cancelled an approved topic";
    public static final String REASON_ALREADY_APPROVED_TOPIC = "Another topic has already been approved for this student";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_topic")
    private Topic topic;

    @Value("#{T(System).currentTimeMillis()}")
    @Column(name = "created_on", nullable = false)
    private Timestamp date;

    @Value(STATUS_WAITING)
    @Column(name = "status")
    private String status;

    @Column(name = "reason")
    private String reason;
}
