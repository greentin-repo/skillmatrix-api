package com.greentin.enovation.repository;

import com.greentin.enovation.model.skillMatrix.SMWorkstationMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query(value = "DELETE FROM sm_workstation_mapping WHERE parent_workstation_id = ?1", nativeQuery = true)
    void deleteByParentWorkstationId(long parentWorkstationId);

    void deleteByParentWorkstationIdAndBranchBranchId(long parentWorkstationId, int branchId);

    @Query("SELECT m FROM SMWorkstationMapping m WHERE m.branch.branchId = :branchId AND m.parentWorkstation.id = :parentWorkstationId AND m.childWorkstation.id = :childWorkstationId AND m.isActive = true")
    List<SMWorkstationMapping> findByBranchIdAndParentWorkstationIdAndChildWorkstationId(
            @Param("branchId") int branchId,
            @Param("parentWorkstationId") long parentWorkstationId,
            @Param("childWorkstationId") long childWorkstationId);
}