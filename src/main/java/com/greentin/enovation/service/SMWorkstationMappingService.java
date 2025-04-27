package com.greentin.enovation.service;

import com.greentin.enovation.model.skillMatrix.SMWorkstationMapping;
import java.util.List;

public interface SMWorkstationMappingService {
    SMWorkstationMapping saveMapping(SMWorkstationMapping mapping);
    SMWorkstationMapping updateMapping(SMWorkstationMapping mapping);
    void deleteMapping(Long id);
    SMWorkstationMapping getMappingById(Long id);
    List<SMWorkstationMapping> getAllMappings();
    List<SMWorkstationMapping> getMappingsByParentWorkstation(Long parentWorkstationId);
    List<SMWorkstationMapping> getMappingsByChildWorkstation(Long childWorkstationId);
    List<SMWorkstationMapping> getMappingsByBranch(Long branchId);
    List<SMWorkstationMapping> getMappingsByDepartment(Long deptId);
    List<SMWorkstationMapping> getMappingsByLine(Long lineId);
}