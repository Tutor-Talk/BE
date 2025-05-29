package org.tutortalk.be.domain.contents.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Table(name = "content")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;    //인덱스 번호

    @Column(name = "classname", nullable = false )
    private String className; // 교육 이름(ex. wifi)

    @Column(name = "number", nullable = false )
    private int number;     // 식별값(ex. 3)

    @Column(name = "url", nullable = false )
    private String url;     // s3 이미지 주소
}
