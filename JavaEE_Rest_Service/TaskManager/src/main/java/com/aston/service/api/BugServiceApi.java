package com.aston.service.api;

import com.aston.entities.Bug;

public interface BugServiceApi {
    Long createBug(Bug bug);
    Long deleteBug(Bug bug);
}
