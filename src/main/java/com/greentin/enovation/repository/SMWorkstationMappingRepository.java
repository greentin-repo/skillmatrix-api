package com.greentin.enovation.repository;

import com.greentin.enovation.model.skillMatrix.SMWorkstationMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SMWorkstationMappingRepository extends JpaRepository<SMWorkstationMapping, Long> {

    List<SMWorkstationMapping> findByParentWorkstationId(Long parentWorkstationId);

    List<SMWorkstationMapping> findByChildWorkstationId(Long childWorkstationId);

    List<SMWorkstationMapping> findByBranchBranchId(Long branchId);

    List<SMWorkstationMapping> findByDeptDeptId(Long deptId);

    List<SMWorkstationMapping> findByLineId(Long lineId);
}