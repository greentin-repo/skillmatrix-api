package com.greentin.enovation.dto;

public class DeleteWorkstationMappingDTO {
    private int branchId;
    private long parentWorkstationId;

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public long getParentWorkstationId() {
        return parentWorkstationId;
    }

    public void setParentWorkstationId(long parentWorkstationId) {
        this.parentWorkstationId = parentWorkstationId;
    }
}

