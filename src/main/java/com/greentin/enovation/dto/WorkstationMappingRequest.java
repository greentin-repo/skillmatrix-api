package com.greentin.enovation.dto;

import java.util.List;

public class WorkstationMappingRequest {
    private int orgId;
    private int branchId;
    private long parentWorkstationId;
    private List<Long> childWorkstationId;
    private Boolean isActive;
    private int deptId;
    private long lineId;

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

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

    public List<Long> getChildWorkstationId() {
        return childWorkstationId;
    }

    public void setChildWorkstationId(List<Long> childWorkstationId) {
        this.childWorkstationId = childWorkstationId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public long getLineId() {
        return lineId;
    }

    public void setLineId(long lineId) {
        this.lineId = lineId;
    }
}