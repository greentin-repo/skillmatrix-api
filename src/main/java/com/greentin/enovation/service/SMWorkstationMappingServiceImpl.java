package com.greentin.enovation.service;

import com.greentin.enovation.model.skillMatrix.SMWorkstationMapping;
import com.greentin.enovation.repository.SMWorkstationMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SMWorkstationMappingServiceImpl implements SMWorkstationMappingService {

    @Autowired
    private SMWorkstationMappingRepository mappingRepository;

    @Override
    public SMWorkstationMapping saveMapping(SMWorkstationMapping mapping) {
        return mappingRepository.save(mapping);
    }

    @Override
    public SMWorkstationMapping updateMapping(SMWorkstationMapping mapping) {
        return mappingRepository.save(mapping);
    }

    @Override
    public void deleteMapping(Long id) {
        mappingRepository.deleteById(id);
    }

    @Override
    public SMWorkstationMapping getMappingById(Long id) {
        return mappingRepository.findById(id).orElse(null);
    }

    @Override
    public List<SMWorkstationMapping> getAllMappings() {
        return mappingRepository.findAll();
    }

    @Override
    public List<SMWorkstationMapping> getMappingsByParentWorkstation(Long parentWorkstationId) {
        return mappingRepository.findByParentWorkstationId(parentWorkstationId);
    }

    @Override
    public List<SMWorkstationMapping> getMappingsByChildWorkstation(Long childWorkstationId) {
        return mappingRepository.findByChildWorkstationId(childWorkstationId);
    }

    @Override
    public List<SMWorkstationMapping> getMappingsByBranch(Long branchId) {
        return mappingRepository.findByBranchBranchId(branchId);
    }

    @Override
    public List<SMWorkstationMapping> getMappingsByDepartment(Long deptId) {
        return mappingRepository.findByDeptDeptId(deptId);
    }

    @Override
    public List<SMWorkstationMapping> getMappingsByLine(Long lineId) {
        return mappingRepository.findByLineId(lineId);
    }
}