package com.armin.utility.file.model.object;

import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

public class ContainerInfo {
    @NotNull
    private String name;
    private Timestamp created;
    @NotNull
    private Collection<BucketInfo> buckets;
    private long size;

    public ContainerInfo(@NotNull String name) {
        this.name = name;
        this.buckets = new ArrayList<>();
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

    @NotNull
    public Collection<BucketInfo> getBuckets() {
        return buckets;
    }

    public void setBuckets(@NotNull Collection<BucketInfo> buckets) {
        this.buckets = buckets;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
