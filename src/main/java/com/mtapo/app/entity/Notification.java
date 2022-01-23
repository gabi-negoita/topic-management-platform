package com.mtapo.app.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "notification")
public class Notification {

    public static final String TYPE_TEACHER_RESPONSE = "teacher-topic-response";
    public static final String TYPE_STUDENT_REQUEST = "student-topic-request";
    public static final String TYPE_STUDENT_APPLICATION_CANCEL = "student-application-cancel";
    public static final String TYPE_TEACHER_APPLICATION_CANCEL = "teacher-application-cancel";
    public static final String TYPE_COMMENT = "comment";

    public static final String REQUEST_APPROVED_SUCCESS = "Request approved";
    public static final String REQUEST_APPROVED_FAILURE = "Request approval failed";
    public static final String REQUEST_DECLINED_SUCCESS = "Request declined";
    public static final String REQUEST_DECLINED_FAILURE = "Request declination failed";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Integer id;

    @Column(name = "description")
    private String description;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "date", nullable = false)
    private Timestamp date;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

    @Column(name = "is_seen", nullable = false)
    private Boolean isSeen;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_student_topic")
    private StudentTopic studentTopic;

    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_comment")
    private TopicComment comment;
}
