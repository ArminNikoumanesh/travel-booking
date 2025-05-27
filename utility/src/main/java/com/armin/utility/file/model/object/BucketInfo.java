package com.armin.utility.file.model.object;

import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;

public class BucketInfo {
    @NotNull
    private String name;
    private Timestamp created;
    private long size;

    public BucketInfo(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
