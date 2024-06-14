package com.example.easypan.entity.dto;

import java.io.Serializable;

public class UserSpaceDto implements Serializable {
    private Long userSpace;
    private Long totalUserSpace;

    public Long getUserSpace() {
        return userSpace;
    }

    public void setUserSpace(Long userSpace) {
        this.userSpace = userSpace;
    }

    public Long getTotalUserSpace() {
        return totalUserSpace;
    }

    public void setTotalUserSpace(Long totalUserSpace) {
        this.totalUserSpace = totalUserSpace;
    }
}
