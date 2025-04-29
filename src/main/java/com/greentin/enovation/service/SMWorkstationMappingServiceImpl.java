package com.greentin.enovation.service;

import com.google.gson.Gson;
import com.greentin.enovation.dto.SkillMatrixRequest;
import com.greentin.enovation.model.skillMatrix.SMWorkstationMapping;
import com.greentin.enovation.model.skillMatrix.SMWorkstations;
import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.DepartmentMaster;
import com.greentin.enovation.model.dwm.Line;
import com.greentin.enovation.dto.WorkstationMappingRequest;
import com.greentin.enovation.repository.SMWorkstationMappingRepository;
import com.greentin.enovation.response.SkillMatrixResponse;
import com.greentin.enovation.utils.BuildResponse;
import com.greentin.enovation.utils.EnovationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SMWorkstationMappingServiceImpl implements SMWorkstationMappingService {

    @Autowired
    private SMWorkstationMappingRepository mappingRepository;

    @Override
    public List<SMWorkstationMapping> saveMappings(WorkstationMappingRequest request) {
        // Validate request
        if (request == null || request.getChildWorkstationId() == null || request.getChildWorkstationId().isEmpty()) {
            return new ArrayList<>();
        }

        // Create parent workstation with full details
        SMWorkstations parentWorkstation = new SMWorkstations();
        parentWorkstation.setId(request.getParentWorkstationId());
        
        // Create branch, dept, and line objects
        BranchMaster branch = new BranchMaster(request.getBranchId());
        DepartmentMaster dept = new DepartmentMaster(request.getDeptId());
        Line line = new Line(request.getLineId());

        // Create separate mappings for each child workstation ID
        return request.getChildWorkstationId().stream()
                .map(childId -> {
                    SMWorkstationMapping mapping = new SMWorkstationMapping();
                    mapping.setParentWorkstation(parentWorkstation);
                    
                    // Create child workstation
                    SMWorkstations childWorkstation = new SMWorkstations();
                    childWorkstation.setId(childId);
                    mapping.setChildWorkstation(childWorkstation);
                    
                    mapping.setBranch(branch);
                    mapping.setDept(dept);
                    mapping.setLine(line);
                    mapping.setIsActive(true); // Set default active status
                    mapping.setCreatedBy(0); // Set default created by
                    mapping.setUpdatedBy(0); // Set default updated by
                    return mappingRepository.save(mapping);
                })
                .collect(Collectors.toList());
    }

    @Override
    public SMWorkstationMapping saveMapping(SMWorkstationMapping mapping) {
        if (mapping == null) {
            throw new IllegalArgumentException("Mapping cannot be null");
        }
        return mappingRepository.save(mapping);
    }

    @Override
    public SMWorkstationMapping updateMapping(SMWorkstationMapping mapping) {
        if (mapping == null) {
            throw new IllegalArgumentException("Mapping cannot be null");
        }
        return mappingRepository.save(mapping);
    }

    @Override
    public void deleteMapping(long id) {
        mappingRepository.deleteById(id);
    }

    @Override
    public SMWorkstationMapping getMappingById(long id) {
        return mappingRepository.findById(id).orElse(null);
    }

    @Override
    public List<SMWorkstationMapping> getAllMappings() {
        return mappingRepository.findAll();
    }

    @Override
    public List<SMWorkstationMapping> getMappingsByParentWorkstation(long parentWorkstationId) {
        return mappingRepository.findByParentWorkstationId(parentWorkstationId);
    }

    @Override
    public List<SMWorkstationMapping> getMappingsByChildWorkstation(long childWorkstationId) {
        return mappingRepository.findByChildWorkstationId(childWorkstationId);
    }

    @Override
    public List<SMWorkstationMapping> getMappingsByBranch(int branchId) {
        return mappingRepository.findByBranchBranchId(branchId);
    }

    @Override
    public List<SMWorkstationMapping> getMappingsByDepartment(int deptId) {
        return mappingRepository.findByDeptDeptId(deptId);
    }

    @Override
    public List<SMWorkstationMapping> getMappingsByLine(long lineId) {
        return mappingRepository.findByLineId(lineId);
    }
}