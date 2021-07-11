package com.sillock.domain.sillog.model.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDate;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Document(collection = "qna")
public class Qna {
    @Id
    private String id;
    private String question;
    private String answer;
    private List<String> tags;
    private LocalDate regDate;

    @Builder.Default
    private LocalDate modDate = LocalDate.now();
}
