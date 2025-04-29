package com.greentin.enovation.repository;

import com.greentin.enovation.model.skillMatrix.SMWorkstationMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SMWorkstationMappingRepository extends JpaRepository<SMWorkstationMapping, Long> {

    List<SMWorkstationMapping> findByParentWorkstationId(long parentWorkstationId);

    List<SMWorkstationMapping> findByChildWorkstationId(long parentWorkstationId);

    List<SMWorkstationMapping> findByBranchBranchId(int branchId);

    List<SMWorkstationMapping> findByDeptDeptId(int deptId);

    List<SMWorkstationMapping> findByLineId(long lineId);
}