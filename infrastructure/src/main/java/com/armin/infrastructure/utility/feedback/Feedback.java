package com.armin.infrastructure.utility.feedback;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 14.06.22
 */
@Getter
@Setter
public class Feedback {
    private Double lat;
    private Double lng;
    private String wayId;
    private String textComment;
    private String voiceComment;
    private FeedbackVoteType voteType;
    private FeedbackType type;
    private String images;
    private Integer userId;
    private JsonNode data;
}
