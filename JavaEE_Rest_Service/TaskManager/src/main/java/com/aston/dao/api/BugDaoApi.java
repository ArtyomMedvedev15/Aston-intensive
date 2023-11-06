package com.aston.dao.api;

import com.aston.entities.Bug;

public interface BugDaoApi {
    Long createBug(Bug bug);
    Long deleteBug(Bug bug);
}
