package com.sillock.event.domain;

import com.sillock.tag.domain.Tag;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OpenEventTag {
    @EmbeddedId
    private OpenEventTagPK id;

    @MapsId("tagId")
    @OneToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @MapsId("openEventId")
    @OneToOne
    @JoinColumn(name = "open_event_id")
    private OpenEvent openEvent;
}