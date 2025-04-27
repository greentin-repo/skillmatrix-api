package com.greentin.enovation.model.skillMatrix;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.DepartmentMaster;
import com.greentin.enovation.model.dwm.Line;

@Entity
@Table(name = "sm_workstation_mapping")
public class SMWorkstationMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "parent_workstation_id")
    private SMWorkstations parentWorkstation;

    @ManyToOne
    @JoinColumn(name = "child_workstation_id")
    private SMWorkstations childWorkstation;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private BranchMaster branch;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private DepartmentMaster dept;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    @Column(name = "mapping_type", length = 50)
    private String mappingType; // e.g., "SIMILAR", "EQUIVALENT"

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_by", columnDefinition = "int default 0", updatable = false)
    private int createdBy;

    @Column(name = "updated_by", columnDefinition = "int default 0")
    private int updatedBy;

    @CreationTimestamp
    @Column(name = "created_date", columnDefinition = "datetime default now()", updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private Timestamp updatedDate;

    public SMWorkstationMapping() {
        super();
    }

    public SMWorkstationMapping(long id) {
        super();
        this.id = id;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SMWorkstations getParentWorkstation() {
        return parentWorkstation;
    }

    public void setParentWorkstation(SMWorkstations parentWorkstation) {
        this.parentWorkstation = parentWorkstation;
    }

    public SMWorkstations getChildWorkstation() {
        return childWorkstation;
    }

    public void setChildWorkstation(SMWorkstations childWorkstation) {
        this.childWorkstation = childWorkstation;
    }

    public BranchMaster getBranch() {
        return branch;
    }

    public void setBranch(BranchMaster branch) {
        this.branch = branch;
    }

    public DepartmentMaster getDept() {
        return dept;
    }

    public void setDept(DepartmentMaster dept) {
        this.dept = dept;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public String getMappingType() {
        return mappingType;
    }

    public void setMappingType(String mappingType) {
        this.mappingType = mappingType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}