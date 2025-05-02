package com.greentin.enovation.service;

import com.greentin.enovation.dto.DeleteWorkstationMappingDTO;
import com.greentin.enovation.dto.SkillMatrixRequest;
import com.greentin.enovation.model.skillMatrix.SMWorkstationMapping;
import com.greentin.enovation.dto.WorkstationMappingRequest;
import com.greentin.enovation.response.SkillMatrixResponse;

import java.util.List;

public interface SMWorkstationMappingService {
    List<SMWorkstationMapping> saveMappings(WorkstationMappingRequest request);
    SMWorkstationMapping saveMapping(SMWorkstationMapping mapping);
    SMWorkstationMapping updateMapping(SMWorkstationMapping mapping);
    List<SMWorkstationMapping> updateMappings(WorkstationMappingRequest request);
    void deleteMapping(long id);
    void deleteMappingsByParentWorkstationId(DeleteWorkstationMappingDTO deleteWorkstationMappingDTO);
    SMWorkstationMapping getMappingById(long id);
    List<SMWorkstationMapping> getAllMappings();
    List<SMWorkstationMapping> getMappingsByParentWorkstation(long parentWorkstationId);
    List<SMWorkstationMapping> getMappingsByChildWorkstation(long childWorkstationId);
    List<SMWorkstationMapping> getMappingsByBranch(int branchId);
    List<SMWorkstationMapping> getMappingsByDepartment(int deptId);
    List<SMWorkstationMapping> getMappingsByLine(long lineId);
    List<SMWorkstationMapping> findByBranchIdAndParentWorkstationIdAndChildWorkstationId(int branchId, long parentWorkstationId, long childWorkstationId);
}