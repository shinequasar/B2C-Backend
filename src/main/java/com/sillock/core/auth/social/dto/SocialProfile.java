package com.sillock.core.auth.social.dto;

import lombok.*;

/**
 * created by soyeon 21/06/23
 */
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocialProfile {
    private String email;
    private String gender;
    private String age_range;
}