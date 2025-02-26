package com.finalProject.Back.dto.request;

import com.finalProject.Back.entity.Report;
import lombok.Data;

@Data
public class ReqReportDto {
    private Long contentId;
    private String content;
    private Long reportId;
    private String reportType;

    public Report toEntity() {
        return Report.builder()
                .contentId(contentId)
                .content(content)
                .user_id(reportId)
                .reportType(reportType)
                .build();
    }
}
