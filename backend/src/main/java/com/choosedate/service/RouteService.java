package com.choosedate.service;

import com.choosedate.domain.dto.RouteSummaryRequestDto;
import com.choosedate.domain.dto.RouteSummaryResponseDto;

public interface RouteService {
    public RouteSummaryResponseDto summarize(RouteSummaryRequestDto req);
}
