package com.greentin.enovation.skillmatrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.greentin.enovation.model.skillMatrix.SMWorkstationMapping;
import com.greentin.enovation.dto.WorkstationMappingRequest;
import com.greentin.enovation.response.SkillMatrixResponse;
import com.greentin.enovation.service.SMWorkstationMappingService;
import com.greentin.enovation.dto.SkillMatrixRequest;

import java.util.HashMap;
import java.util.List;

/**
 * Controller for managing workstation mappings
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/apis/sm/workstation-mapping")
public class SMWorkstationMappingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMWorkstationMappingController.class);

    @Autowired
    private SMWorkstationMappingService mappingService;

    @Autowired
    private SettingDaoImpl settingDao;

    /**
     * Save multiple workstation mappings
     */
    @PostMapping(value = "/save")
    public SkillMatrixResponse saveMappings(@RequestBody WorkstationMappingRequest request) {
        LOGGER.info("# SMWorkstationMappingController || saveMappings");
        SkillMatrixResponse response = new SkillMatrixResponse();
        try {
            List<SMWorkstationMapping> savedMappings = mappingService.saveMappings(request);
            HashMap<String, Object> data = new HashMap<>();
            data.put("mappings", savedMappings);
            response.setStatus("success");
            response.setStatusCode(200);
            response.setResult(true);
            response.setData(data);
        } catch (Exception e) {
            LOGGER.error("Error saving workstation mappings: {}", e.getMessage());
            response.setStatus("error");
            response.setStatusCode(500);
            response.setResult(false);
            response.setReason("Error saving workstation mappings: " + e.getMessage());
        }
        return response;
    }

    /**
     * Update an existing workstation mapping
     */
    @PostMapping(value = "/update")
    public SkillMatrixResponse updateMapping(@RequestBody WorkstationMappingRequest request) {
        LOGGER.info("# SMWorkstationMappingController || updateMapping");
        SkillMatrixResponse response = new SkillMatrixResponse();
        try {
            mappingService.updateMappings(request);
            response.setStatus("success");
            response.setStatusCode(200);
            response.setResult(true);
            response.setReason("Workstation Mapping updated successfully");
        } catch (Exception e) {
            LOGGER.error("Error updating workstation mappings: {}", e.getMessage());
            response.setStatus("error");
            response.setStatusCode(500);
            response.setResult(false);
            response.setReason("Error updating workstation mappings: " + e.getMessage());
        }
        return response;
    }

    @DeleteMapping(value = "/delete-by-parent/{parentWorkstationId}")
    public SkillMatrixResponse deleteMappingsByParentWorkstation(@PathVariable("parentWorkstationId") long parentWorkstationId) {
        LOGGER.info("# SMWorkstationMappingController || deleteMappingsByParentWorkstation");
        SkillMatrixResponse response = new SkillMatrixResponse();
        try {
            mappingService.deleteMappingsByParentWorkstationId(parentWorkstationId);
            response.setStatus("success");
            response.setStatusCode(200);
            response.setResult(true);
        } catch (Exception e) {
            LOGGER.error("Error deleting workstation mappings by parent: {}", e.getMessage());
            response.setStatus("error");
            response.setStatusCode(500);
            response.setResult(false);
            response.setReason("Error deleting workstation mappings by parent: " + e.getMessage());
        }
        return response;
    }

    /**
     * Get a workstation mapping by ID
     */
    @GetMapping(value = "/get/{id}")
    public SkillMatrixResponse getMappingById(@PathVariable("id") Long id) {
        LOGGER.info("# SMWorkstationMappingController || getMappingById");
        SkillMatrixResponse response = new SkillMatrixResponse();
        try {
            SMWorkstationMapping mapping = mappingService.getMappingById(id);
            HashMap<String, Object> data = new HashMap<>();
            data.put("mapping", mapping);
            response.setStatus("success");
            response.setStatusCode(200);
            response.setResult(true);
            response.setData(data);
        } catch (Exception e) {
            LOGGER.error("Error getting workstation mapping: {}", e.getMessage());
            response.setStatus("error");
            response.setStatusCode(500);
            response.setResult(false);
            response.setReason("Error getting workstation mapping: " + e.getMessage());
        }
        return response;
    }

    /**
     * Get all workstation mappings
     */
    @GetMapping(value = "/get-all")
    public SkillMatrixResponse getAllMappings() {
        LOGGER.info("# SMWorkstationMappingController || getAllMappings");
        SkillMatrixResponse response = new SkillMatrixResponse();
        try {
            List<SMWorkstationMapping> mappings = mappingService.getAllMappings();
            HashMap<String, Object> data = new HashMap<>();
            data.put("mappings", mappings);
            response.setStatus("success");
            response.setStatusCode(200);
            response.setResult(true);
            response.setData(data);
        } catch (Exception e) {
            LOGGER.error("Error getting all workstation mappings: {}", e.getMessage());
            response.setStatus("error");
            response.setStatusCode(500);
            response.setResult(false);
            response.setReason("Error getting all workstation mappings: " + e.getMessage());
        }
        return response;
    }

    /**
     * Get mappings by child workstation
     */
    @GetMapping(value = "/get-by-child/{childWorkstationId}")
    public SkillMatrixResponse getMappingsByChildWorkstation(@PathVariable("childWorkstationId") Long childWorkstationId) {
        LOGGER.info("# SMWorkstationMappingController || getMappingsByChildWorkstation");
        SkillMatrixResponse response = new SkillMatrixResponse();
        try {
            List<SMWorkstationMapping> mappings = mappingService.getMappingsByChildWorkstation(childWorkstationId);
            HashMap<String, Object> data = new HashMap<>();
            data.put("mappings", mappings);
            response.setStatus("success");
            response.setStatusCode(200);
            response.setResult(true);
            response.setData(data);
        } catch (Exception e) {
            LOGGER.error("Error getting mappings by child workstation: {}", e.getMessage());
            response.setStatus("error");
            response.setStatusCode(500);
            response.setResult(false);
            response.setReason("Error getting mappings by child workstation: " + e.getMessage());
        }
        return response;
    }

    /**
     * Get mappings by branch
     */
    @GetMapping(value = "/get-by-branch/{branchId}")
    public SkillMatrixResponse getMappingsByBranch(@PathVariable("branchId") int branchId) {
        LOGGER.info("# SMWorkstationMappingController || getMappingsByBranch");
        SkillMatrixResponse response = new SkillMatrixResponse();
        try {
            List<SMWorkstationMapping> mappings = mappingService.getMappingsByBranch(branchId);
            HashMap<String, Object> data = new HashMap<>();
            data.put("mappings", mappings);
            response.setStatus("success");
            response.setStatusCode(200);
            response.setResult(true);
            response.setData(data);
        } catch (Exception e) {
            LOGGER.error("Error getting mappings by branch: {}", e.getMessage());
            response.setStatus("error");
            response.setStatusCode(500);
            response.setResult(false);
            response.setReason("Error getting mappings by branch: " + e.getMessage());
        }
        return response;
    }

    /**
     * Get mappings by department
     */
    @GetMapping(value = "/get-by-department/{deptId}")
    public SkillMatrixResponse getMappingsByDepartment(@PathVariable("deptId") int deptId) {
        LOGGER.info("# SMWorkstationMappingController || getMappingsByDepartment");
        SkillMatrixResponse response = new SkillMatrixResponse();
        try {
            List<SMWorkstationMapping> mappings = mappingService.getMappingsByDepartment(deptId);
            HashMap<String, Object> data = new HashMap<>();
            data.put("mappings", mappings);
            response.setStatus("success");
            response.setStatusCode(200);
            response.setResult(true);
            response.setData(data);
        } catch (Exception e) {
            LOGGER.error("Error getting mappings by department: {}", e.getMessage());
            response.setStatus("error");
            response.setStatusCode(500);
            response.setResult(false);
            response.setReason("Error getting mappings by department: " + e.getMessage());
        }
        return response;
    }

    /**
     * Get mappings by line
     */
    @GetMapping(value = "/get-by-line/{lineId}")
    public SkillMatrixResponse getMappingsByLine(@PathVariable("lineId") int lineId) {
        LOGGER.info("# SMWorkstationMappingController || getMappingsByLine");
        SkillMatrixResponse response = new SkillMatrixResponse();
        try {
            List<SMWorkstationMapping> mappings = mappingService.getMappingsByLine(lineId);
            HashMap<String, Object> data = new HashMap<>();
            data.put("mappings", mappings);
            response.setStatus("success");
            response.setStatusCode(200);
            response.setResult(true);
            response.setData(data);
        } catch (Exception e) {
            LOGGER.error("Error getting mappings by line: {}", e.getMessage());
            response.setStatus("error");
            response.setStatusCode(500);
            response.setResult(false);
            response.setReason("Error getting mappings by line: " + e.getMessage());
        }
        return response;
    }
}