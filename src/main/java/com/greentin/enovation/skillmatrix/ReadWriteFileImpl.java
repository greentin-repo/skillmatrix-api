package com.greentin.enovation.skillmatrix;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.dto.SkillMatrixRequest;
import com.greentin.enovation.model.BranchMaster;
import com.greentin.enovation.model.DepartmentMaster;
import com.greentin.enovation.model.dwm.Line;
import com.greentin.enovation.model.skillMatrix.SMAssessment;
import com.greentin.enovation.model.skillMatrix.SMAssessmentOptions;
import com.greentin.enovation.model.skillMatrix.SMAssessmentQues;
import com.greentin.enovation.model.skillMatrix.SMCategory;
import com.greentin.enovation.model.skillMatrix.SMQuestionType;
import com.greentin.enovation.model.skillMatrix.SMSkillLevel;
import com.greentin.enovation.model.skillMatrix.SMWorkstations;
import com.greentin.enovation.utils.CommonUtils;
import com.greentin.enovation.utils.EnovationConstants;
import com.greentin.enovation.utils.EnovationException;
import com.greentin.enovation.utils.WriteFilesUtils;

@Component
@Transactional
public class ReadWriteFileImpl {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReadWriteFileImpl.class);

	@Autowired
	SkillMatrixUtils smUtils;

	public SkillMatrixRequest uploadAssessmentDetails(Session session, SkillMatrixRequest request,
			String excelFilePath) {

		LOGGER.info("ReadWriteFileImpl | uploadAssessmentDetails");

		SkillMatrixRequest dto = new SkillMatrixRequest();

		/** Creating a Workbook from an Excel file (.xls or .xlsx) */
		Workbook workbook = getWorkbook(excelFilePath);
		if (workbook != null) {
			/** FormulaEvalutor object helps to read values computed by formula function. */

			final FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

			/**
			 * Dataformat object is used to read any type(such as text,number,date,formula)
			 * of data from excel.
			 */

			DataFormatter formatter = new DataFormatter();

			Sheet sheet = null;
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {

				LOGGER.info("Sheet name:{} ", workbook.getSheetName(i));
				if (workbook.getSheetName(i).toString() != null
						&& workbook.getSheetName(i).toString().trim().equals(SMConstant.ASSESSMENT_DETAILS)) {
					sheet = workbook.getSheetAt(i);

					preapareDataSaveAssessmentDetails(request, sheet, formatter, evaluator, session, dto);
					break;
				} else {
					LOGGER.info(" Sheet name not match");
					dto.setReason(SMConstant.EXCEL_NOT_IN_VALID_FORMAT);
					dto.setErrorInSheet(true);
				}
			}
		}
		return dto;
	}

	private void preapareDataSaveAssessmentDetails(SkillMatrixRequest request, Sheet sheet, DataFormatter formatter,
			FormulaEvaluator evaluator, Session session, SkillMatrixRequest dto) {

		LOGGER.info("In ReadWriteFileImpl | preapareDataSaveAssessmentDetails");
		int flag = validateAssessmenetExcel(sheet);
		SkillMatrixRequest assessDto = new SkillMatrixRequest();
		if (flag == 0) {
			readAssessmentExcelData(request, sheet, formatter, evaluator, session, dto, assessDto);

			if (!CollectionUtils.isEmpty(assessDto.getAssessmentList())) {

				validateAssessmentDetails(session, assessDto, request);

				if (!CollectionUtils.isEmpty(assessDto.getErrorList())) {
					LOGGER.info("ErrorList: {}", assessDto.getErrorList());
					dto.setErrorInSheet(true);
					dto.setSheetType(SMConstant.ASSESSMENT_DETAILS);
					dto.setErrorList(assessDto.getErrorList());
					dto.setStatusCode(EnovationConstants.Code100);
				} else {
					if (!CollectionUtils.isEmpty(assessDto.getAssessmentList())) {
						saveAssessmentDetails(session, request, assessDto);
					}
				}
			} else {
				LOGGER.info("Assessment List Is Empty");
				dto.setSheetType(SMConstant.ASSESSMENT_DETAILS);
				dto.setReason("Assessment List Is Empty");
				dto.setErrorInSheet(true);
			}
		} else {
			dto.setSheetType(SMConstant.ASSESSMENT_DETAILS);
			dto.setReason(SMConstant.EXCEL_NOT_IN_VALID_FORMAT);
			dto.setErrorInSheet(true);
		}
	}

	private void saveAssessmentDetails(Session session, SkillMatrixRequest request, SkillMatrixRequest assessDto) {
		LOGGER.info("ReadWriteFileImpl |saveAssessmentDetails");

//		Map<Object, List<SkillMatrixRequest>> map = assessDto.getAssessmentList().stream()
//				.collect(Collectors.groupingBy(p -> Pair.of(p.getAssessmentTitle(), p.getSkillLevelId())));
//		
		Map<Object, List<SkillMatrixRequest>> map = assessDto.getAssessmentList().stream()
				.collect(Collectors.groupingBy(p -> Pair.of(p.getAssessmentTitle(), p.getSkillLevelId()),
						LinkedHashMap::new, // Explicit type parameters
						Collectors.toList()));

		for (Entry<Object, List<SkillMatrixRequest>> x : map.entrySet()) {

			LOGGER.info("Assessment Title,MasterSkillId -{} ", x.getKey());
			SkillMatrixRequest obj = x.getValue().get(0);
			SMAssessment m = new SMAssessment();
			if (smUtils.isTitleNotExist(session, obj)) {
				m.setTitle(obj.getAssessmentTitle());
			} else {
				throw new EnovationException("Title is already used");
			}
			m.setTime(obj.getTime());
			if (obj.getBranchId() > 0) {
				m.setBranch(new BranchMaster(obj.getBranchId()));
			}
//			if (obj.getSkillLevelId() > 0) {
//				m.setSkillLevel(new SMSkillLevel(obj.getSkillLevelId()));
//			}
			m.setPassingMarks(obj.getPassingMark());
			m.setCreatedBy(request.getCreatedBy());
			m.setStatus(SMConstant.ASSESSMENT_CREATED);
			m.setIsActive(true);
			if (obj.getDeptId() > 0) {
				m.setDept(new DepartmentMaster(obj.getDeptId()));
			}
			if (obj.getLineId() > 0) {
				m.setLine(new Line(obj.getLineId()));
			}
			if (obj.getWorkstationId() > 0) {
				m.setWorkstation(new SMWorkstations(obj.getWorkstationId()));
			}
//			m.setAssessmentType(obj.getAssessmentType());

			String assessmentType = obj.getAssessmentType();
			if (assessmentType != null && (assessmentType.equals(EnovationConstants.SAFETY)
					|| assessmentType.equals(EnovationConstants.LEVEL))) {
				m.setAssessmentType(assessmentType);

				if (assessmentType.equals(EnovationConstants.LEVEL)) {
					if (obj.getSkillLevelId() > 0) {
						m.setSkillLevel(new SMSkillLevel(obj.getSkillLevelId()));
					} else {
						throw new EnovationException("SkillLevel is required for 'Level' assessment type.");
					}
				} else if (assessmentType.equals(EnovationConstants.SAFETY)) {
					if (obj.getSkillLevelId() > 0) {
						throw new EnovationException("SkillLevel is not required for 'Safety' assessment type.");
					}

				}
			} else {
				throw new EnovationException("Invalid assessment type, only 'Safety' and 'Level' are accepted.");
			}

			session.save(m);

			if (m != null && m.getId() > 0) {
				LOGGER.info("Assessment Id: " + m.getId());
//				Map<String, List<SkillMatrixRequest>> groupByCategory = x.getValue().stream()
//						.filter(y -> y.getCategoryName() != null)
//						.collect(Collectors.groupingBy(SkillMatrixRequest::getCategoryName));

				Map<String, List<SkillMatrixRequest>> groupByCategory = x.getValue().stream()
						.filter(y -> y.getCategoryName() != null)
						.collect(Collectors.groupingBy(SkillMatrixRequest::getCategoryName, LinkedHashMap::new, // Maintain
																												// insertion
																												// order
								Collectors.toList()));

				for (Entry<String, List<SkillMatrixRequest>> z : groupByCategory.entrySet()) {

					long catId = saveCategory(session, m.getId(), z.getKey());
					LOGGER.info("catId Id: " + catId);
					List<SkillMatrixRequest> assessList = z.getValue();
					for (SkillMatrixRequest o : assessList) {
						createAndSaveAssessmentQuestion(session, m, request, o, catId);
					}
				}
				List<SkillMatrixRequest> assessList = x.getValue();

				for (SkillMatrixRequest o : assessList) {
					LOGGER.info(" ");
					if (o.getCategoryName() == null ? true : o.getCategoryName().length() == 0) {
						LOGGER.info("create assessment without category: ");
						createAndSaveAssessmentQuestion(session, m, request, o, 0);
					}
				}
				smUtils.calculateTotalMark(session, m.getId());
			}
		}
	}

	private void createAndSaveAssessmentQuestion(Session session, SMAssessment m, SkillMatrixRequest request,
			SkillMatrixRequest o, long catId) {
		LOGGER.info("createAndSaveAssessmentQuestion ");
		SMAssessmentQues que = new SMAssessmentQues();
		que.setQestion(o.getQuestion());
		que.setQuesType(new SMQuestionType(SMConstant.ONE));
		que.setAssessment(new SMAssessment(m.getId()));
		que.setCreatedBy(request.getCreatedBy());
		que.setUpdatedBy(request.getUpdatedBy());
		que.setQueMarks(o.getQueMark());
		if (catId > 0) {
			que.setSmcategory(new SMCategory(catId));
		}
		que.setIsActive(true);
		session.save(que);
		saveOptions(que, o, session, request);
	}

	private long saveCategory(Session session, long assessmentId, String catName) {
		SMCategory cat = new SMCategory();
		LOGGER.info("saveCategory ");

		if (catName != null) {
			cat.setCategoryName(catName);
		}
		if (assessmentId > 0) {
			cat.setAssessment(new SMAssessment(assessmentId));
		}
		session.save(cat);
		return cat.getId();
	}

	private void saveOptions(SMAssessmentQues que, SkillMatrixRequest o, Session session, SkillMatrixRequest request) {
		LOGGER.info("ReadWriteFileImpl | saveOptions | que.getId():{} ", que.getId());
		List<SMAssessmentOptions> optList = new ArrayList<>();
		if (CommonUtils.isStringNotNull(o.getOptionOne())) {
			addOption(o.getOptionOne(), optList, o);
		}
		if (CommonUtils.isStringNotNull(o.getOptionTwo())) {
			addOption(o.getOptionTwo(), optList, o);
		}
		if (CommonUtils.isStringNotNull(o.getOptionThree())) {
			addOption(o.getOptionThree(), optList, o);
		}
		if (CommonUtils.isStringNotNull(o.getOptionFour())) {
			addOption(o.getOptionFour(), optList, o);
		}
		if (CommonUtils.isStringNotNull(o.getOptionFive())) {
			addOption(o.getOptionFive(), optList, o);
		}
		if (!CollectionUtils.isEmpty(optList)) {
			optList.stream().forEach(x -> {
				x.setQues(new SMAssessmentQues(que.getId()));
				x.setCreatedBy(request.getCreatedBy());
				x.setUpdatedBy(request.getUpdatedBy());
				session.save(x);
			});
		}
	}

	private void addOption(String option, List<SMAssessmentOptions> optList, SkillMatrixRequest o) {
		LOGGER.info("In ReadWriteFileImpl | addOption");
		SMAssessmentOptions opt = new SMAssessmentOptions();
		if (CommonUtils.isStringNotNull(o.getCorrectAnswer()) && o.getCorrectAnswer().equals(option)) {
			opt.setIsRightAns(true);
		} else {
			opt.setIsRightAns(false);
		}
		opt.setOption(option);
		optList.add(opt);
	}

	private void validateAssessmentDetails(Session session, SkillMatrixRequest assessDto, SkillMatrixRequest request) {
		LOGGER.info("In ReadWriteFileImpl | validateAssessmentDetails");
		List<SkillMatrixRequest> errorList = new ArrayList<>();
		List<BranchMaster> interList = smUtils.getBranchList(session);
		List<SkillMatrixRequest> skillList = smUtils.getSkillLevelList(session);

		for (SkillMatrixRequest x : assessDto.getAssessmentList()) {
			StringBuilder sb = new StringBuilder();
			boolean errorFlag = false;

			if (CommonUtils.isStringNotNull(x.getBranchName())) {
				LOGGER.info("Intevention Name Is Not Null");
				BranchMaster interObj = checkIfBranchExist(x.getBranchName(), interList);
				if (interObj != null && interObj.getBranchId() > 0) {
					x.setBranchId(interObj.getBranchId());

					errorFlag = setDeptIdByName(session, x, sb, errorFlag);
				} else {
					sb.append(SMConstant.BRANCHID_NOT_FOUND);
					errorFlag = true;
				}
			} else {
				sb.append(SMConstant.BRANCHID_NOT_FOUND);
				errorFlag = true;
			}

			if (CommonUtils.isStringNotNull(x.getSkillLevel())) {
				LOGGER.info("Skill Name Is Not Null");
				SkillMatrixRequest skillObj = checkIfSkillIsExist(skillList, x.getSkillLevel());
				if (skillObj != null && skillObj.getSkillLevelId() > 0) {
					x.setSkillLevelId(skillObj.getSkillLevelId());
				} else {
					sb.append(SMConstant.SKILL_LEVEL_NOT_EXIST);
					errorFlag = true;
				}
			}

			if (CommonUtils.isStringNotNull(x.getAssessmentTitle())) {
				LOGGER.info("Title Is Not Null");
			} else {
				sb.append(SMConstant.ASSESSMENT_TITLE_IS_MANDATORY);
				errorFlag = true;
			}

			if (x.getTime() > 0) {
				LOGGER.info("Assessment Duration Is :={} ", x.getTime());
			} else {
				sb.append(SMConstant.ASSESSMENT_DURATION_IS_MANDATORY);
				errorFlag = true;
			}
			if (CommonUtils.isStringNotNull(x.getQuestion())) {
				LOGGER.info("Question Is Not Null");
			} else {
				sb.append(SMConstant.QUESTION_IS_MANDATORY);
				errorFlag = true;
			}
			if (CommonUtils.isStringNotNull(x.getCorrectAnswer())) {
				LOGGER.info("Correct Option Is Not Null");
			} else {
				sb.append(SMConstant.CORRECT_OPTION_IS_MANDATORY);
				errorFlag = true;
			}
			if (x.getPassingMark() > 0) {
				LOGGER.info("Passing Mark Is Not Null");
			} else {
				sb.append(SMConstant.PASSING_MARK_IS_MANDATORY);
				errorFlag = true;
			}
			if (x.getQueMark() > 0) {
				LOGGER.info("Quetion Mark Is Not Null");
			} else {
				sb.append(SMConstant.QUESTION_MARK_IS_MANDATORY);
				errorFlag = true;
			}
			if (CommonUtils.isStringNotNull(x.getOptionOne())) {
				LOGGER.info("Option One Not Null");
			} else {
				sb.append(SMConstant.OPTION_IS_MANDATORY);
				errorFlag = true;
			}
			System.out.print("x.getAssessmentType()...." + x.getAssessmentType());
			if (CommonUtils.isStringNotNull(x.getAssessmentType())) {
				LOGGER.info("AssessmentType Is Not Null");

				// this line added because duplicate assessment adding for level and safety
				x.setSkillLvlId(x.getSkillLevelId());
				if (x.getAssessmentType().equals(EnovationConstants.LEVEL)
						&& !smUtils.assessmentAlreadyExist(session, x)) {
					throw new EnovationException(
							(CommonUtils.isStringNotNull(x.getSkillLevel()) ? x.getSkillLevel() + " " : "")
									+ "Level assessment already exists for this "
									+ (CommonUtils.isStringNotNull(x.getWorkstation()) ? x.getWorkstation()
											: "workstation"));
				} else if (x.getAssessmentType().equals(EnovationConstants.SAFETY)
						&& !smUtils.assessmentTypeAlreadyExist(session, x)) {
					throw new EnovationException("Safety assessment already exists for this "
							+ (CommonUtils.isStringNotNull(x.getWorkstation()) ? x.getWorkstation() : "workstation"));
				}

			} else {
				sb.append(SMConstant.ASSESSMENT_TYPE_IS_MANDATORY);
				errorFlag = true;
			}

			// if errorFlag value is true then particular records has some errors
			if (errorFlag) {
				sb.setLength((sb.length() - 1));
				x.setErrorMessage(sb.toString());
				errorList.add(x);
			}
		}
		assessDto.setErrorList(errorList);
	}

	private boolean setDeptIdByName(Session session, SkillMatrixRequest x, StringBuilder sb, boolean errorFlag) {
		List<SkillMatrixRequest> deptList = smUtils.getDeptList(session, x.getBranchId());
		if (CommonUtils.isStringNotNull(x.getDeptName())) {
			LOGGER.info("DEPT Name Is Not Null");
			SkillMatrixRequest deptObj = checkIfDeptIsExist(deptList, x.getDeptName());
			if (deptObj != null && deptObj.getDeptId() > 0) {
				x.setDeptId(deptObj.getDeptId());

				errorFlag = setLindIdByName(session, x, sb, errorFlag);
			} else {
				sb.append("Department Not found,");
				errorFlag = true;
			}

		} else {
			sb.append("Department Not found,");
			errorFlag = true;
		}
		return errorFlag;
	}

	private boolean setLindIdByName(Session session, SkillMatrixRequest x, StringBuilder sb, boolean errorFlag) {
		List<SkillMatrixRequest> lineList = smUtils.getLineList(session, x.getDeptId());
		if (CommonUtils.isStringNotNull(x.getLineName())) {
			LOGGER.info("Line Name Is Not Null");

			SkillMatrixRequest lineObj = checkIfLineIsExist(lineList, x.getLineName());
			if (lineObj != null && lineObj.getLineId() > 0) {
				x.setLineId(lineObj.getLineId());
				errorFlag = setWorkstationIdByName(session, x, sb, errorFlag);
			} else {
				sb.append("Line Not found,");
				errorFlag = true;
			}
		} else {
			sb.append("Line Not found,");
			errorFlag = true;
		}
		return errorFlag;
	}

	private boolean setWorkstationIdByName(Session session, SkillMatrixRequest x, StringBuilder sb, boolean errorFlag) {
		List<SkillMatrixRequest> workstationList = smUtils.getWorkstationList(session, x.getLineId());
		if (CommonUtils.isStringNotNull(x.getWorkstation())) {
			SkillMatrixRequest workObj = checkIfWorkstatiomIsExist(workstationList, x.getWorkstation());
			if (workObj != null && workObj.getWorkstationId() > 0) {
				x.setWorkstationId(workObj.getWorkstationId());

			} else {
				sb.append("Worksation Not found,");
				errorFlag = true;
			}
		} else {
			sb.append("Worksation Not found,");
			errorFlag = true;
		}
		return errorFlag;
	}

	private SkillMatrixRequest checkIfWorkstatiomIsExist(List<SkillMatrixRequest> workstationList, String workstation) {
		LOGGER.info("In ReadWriteFileImpl | checkIfWorkstatiomIsExist");
		return workstationList.stream()
				.filter(x -> x.getWorkstation().toUpperCase().trim().equals(workstation.toUpperCase().trim())).findAny()
				.orElse(null);
	}

	private SkillMatrixRequest checkIfLineIsExist(List<SkillMatrixRequest> lineList, String lineName) {
		LOGGER.info("In ReadWriteFileImpl | checkIfLineIsExist");
		return lineList.stream().filter(x -> x.getLineName().toUpperCase().trim().equals(lineName.toUpperCase().trim()))
				.findAny().orElse(null);
	}

	private SkillMatrixRequest checkIfDeptIsExist(List<SkillMatrixRequest> deptList, String deptName) {
		LOGGER.info("In ReadWriteFileImpl | SkillMatrixRequest");
		return deptList.stream().filter(x -> x.getDeptName().toUpperCase().trim().equals(deptName.toUpperCase().trim()))
				.findAny().orElse(null);
	}

	private SkillMatrixRequest checkIfSkillIsExist(List<SkillMatrixRequest> skillList, String skillLevel) {
		LOGGER.info("In ReadWriteFileImpl | SkillMatrixRequest");
		return skillList.stream()
				.filter(x -> x.getSkillLevel().toUpperCase().trim().equals(skillLevel.toUpperCase().trim())).findAny()
				.orElse(null);
	}

	private BranchMaster checkIfBranchExist(String branchName, List<BranchMaster> interList) {
		LOGGER.info("In ReadWriteFileImpl | checkIfBranchExist");
		return interList.stream().filter(x -> x.getName().toUpperCase().trim().equals(branchName.toUpperCase().trim()))
				.findAny().orElse(null);

	}

	// Added by Sonali L. Jan 15 2024 - added field assessmentType
	private void readAssessmentExcelData(SkillMatrixRequest request, Sheet sheet, DataFormatter formatter,
			FormulaEvaluator evaluator, Session session, SkillMatrixRequest dto, SkillMatrixRequest assessDto) {
		LOGGER.info("In ReadWriteFileImpl | readAssessmentExcelData");
		List<SkillMatrixRequest> assessmentList = new ArrayList<>();
		for (int j = 2; j < sheet.getLastRowNum() + 1; j++) {
			Row row = sheet.getRow(j);
			Row header = sheet.getRow(2); // Set First row to variable header for column names comparison
			if (row.getRowNum() > 2 && CommonUtils.excelRowChecker(formatter, row)) {

				SkillMatrixRequest obj = new SkillMatrixRequest();

				for (int k = header.getFirstCellNum(); k < header.getLastCellNum() + 1; k++) {

					Cell cellHeader = header.getCell(k);
					boolean cellHeaderFlag = CommonUtils.checkIfHeaderIsNotNull(cellHeader, formatter);
					if (cellHeaderFlag) {

						if (formatter.formatCellValue(cellHeader).trim().equals(SMConstant.PLANT_NAME)) {
							if (CommonUtils.validateCellData(formatter, row, k)) {
								obj.setBranchName(formatter.formatCellValue(row.getCell(k)));
							}
						}

						if (formatter.formatCellValue(cellHeader).trim().equals(SMConstant.DEPT_NAME)) {
							if (CommonUtils.validateCellData(formatter, row, k)) {
								obj.setDeptName(formatter.formatCellValue(row.getCell(k)));
							}
						}
						if (formatter.formatCellValue(cellHeader).trim().equals(SMConstant.LINE_NAME)) {
							if (CommonUtils.validateCellData(formatter, row, k)) {
								obj.setLineName(formatter.formatCellValue(row.getCell(k)));
							}
						}
						if (formatter.formatCellValue(cellHeader).trim().equals(SMConstant.WORKSTATION_NAME)) {
							if (CommonUtils.validateCellData(formatter, row, k)) {
								obj.setWorkstation(formatter.formatCellValue(row.getCell(k)));
							}
						}
						if (formatter.formatCellValue(cellHeader).trim().equals(SMConstant.CATEGORY_NAME)) {
							if (CommonUtils.validateCellData(formatter, row, k)) {
								obj.setCategoryName(formatter.formatCellValue(row.getCell(k)));
							}
						}

						if (formatter.formatCellValue(cellHeader).trim().equals(SMConstant.ASSESSMENT_LEVEL)) {
							if (CommonUtils.validateCellData(formatter, row, k)) {
								obj.setSkillLevel(formatter.formatCellValue(row.getCell(k)));
							}
						}
						if (formatter.formatCellValue(cellHeader).trim().equals(SMConstant.ASSESSMENT_TITLE)) {
							if (CommonUtils.validateCellData(formatter, row, k)) {
								obj.setAssessmentTitle(formatter.formatCellValue(row.getCell(k)));
							}
						}
						if (formatter.formatCellValue(cellHeader).trim().equals(SMConstant.QUE_MARK)) {
							if (CommonUtils.validateCellData(formatter, row, k)) {
								obj.setQueMark(
										Integer.parseInt(String.valueOf(formatter.formatCellValue(row.getCell(k)))));
							}
						}
						if (formatter.formatCellValue(cellHeader).trim().equals(SMConstant.PASSING_MARK)) {
							if (CommonUtils.validateCellData(formatter, row, k)) {
								obj.setPassingMark(
										Integer.parseInt(String.valueOf(formatter.formatCellValue(row.getCell(k)))));
							}
						}
						if (formatter.formatCellValue(cellHeader).trim().equals(SMConstant.LABEL_ASSESSMENT_DURATION)) {
							if (CommonUtils.validateCellData(formatter, row, k)) {
								obj.setTime(
										Integer.parseInt(String.valueOf(formatter.formatCellValue(row.getCell(k)))));
							}
						}
						if (formatter.formatCellValue(cellHeader).trim().equals(SMConstant.ASSESSMENT_TYPE)) {
							if (CommonUtils.validateCellData(formatter, row, k)) {
								obj.setAssessmentType(formatter.formatCellValue(row.getCell(k)));
							}
						}

						if (formatter.formatCellValue(cellHeader).trim().equals(SMConstant.LABEL_QUESTION)) {
							if (CommonUtils.validateCellData(formatter, row, k)) {
								obj.setQuestion(formatter.formatCellValue(row.getCell(k)));
							}
						}
						if (formatter.formatCellValue(cellHeader).trim().equals(SMConstant.LABEL_CORRECT_ANSWER)) {
							if (CommonUtils.validateCellData(formatter, row, k)) {
								obj.setCorrectAnswer(formatter.formatCellValue(row.getCell(k)));
							}
						} else if (formatter.formatCellValue(cellHeader).trim().equals(SMConstant.LABEL_OPTION_ONE)) {
							if (CommonUtils.validateCellData(formatter, row, k)) {
								obj.setOptionOne(formatter.formatCellValue(row.getCell(k)));
							}
						} else if (formatter.formatCellValue(cellHeader).trim().equals(SMConstant.LABEL_OPTION_TWO)) {
							if (CommonUtils.validateCellData(formatter, row, k)) {
								obj.setOptionTwo(formatter.formatCellValue(row.getCell(k)));
							}
						} else if (formatter.formatCellValue(cellHeader).trim().equals(SMConstant.LABEL_OPTION_THREE)) {
							if (CommonUtils.validateCellData(formatter, row, k)) {
								obj.setOptionThree(formatter.formatCellValue(row.getCell(k)));
							}
						} else if (formatter.formatCellValue(cellHeader).trim().equals(SMConstant.LABEL_OPTION_FOUR)) {
							if (CommonUtils.validateCellData(formatter, row, k)) {
								obj.setOptionFour(formatter.formatCellValue(row.getCell(k)));
							}
						} else if (formatter.formatCellValue(cellHeader).trim().equals(SMConstant.LABEL_OPTION_FIVE)) {
							if (CommonUtils.validateCellData(formatter, row, k)) {
								obj.setOptionFive(formatter.formatCellValue(row.getCell(k)));
							}
						}
					}
				}
				assessmentList.add(obj);
			}
		}
		assessDto.setAssessmentList(assessmentList);
	}

	private int validateAssessmenetExcel(Sheet sheet) {
		LOGGER.info("In ReadWriteFileImpl | validateAssessmenetExcel");
		int flag = 0;
		Row header = sheet.getRow(2);
		List<String> headersFromExcel = new ArrayList<>();
		List<String> mandatoryheaders = Arrays.asList(SMConstant.ASSESSMENT_UPLOAD_MANDATORY_HEADER);
		if (header != null) {
			getExcelHeaderList(header, headersFromExcel);
		} else {
			throw new EnovationException(SMConstant.EXCEL_NOT_IN_VALID_FORMAT);
		}
		if (!CollectionUtils.isEmpty(headersFromExcel)) {
			if (headersFromExcel.containsAll(mandatoryheaders)) {
				flag = 0;
			} else {
				flag = 2;
			}
		} else {
			flag = 2;
		}
		return flag;
	}

	public static void getExcelHeaderList(Row header, List<String> headersFromExcel) {
		LOGGER.info("ReadWriteFileImpl | getExcelHeaderList");
		for (int k = header.getFirstCellNum(); k < header.getLastCellNum() + 1; k++) {
			Cell cellHeader = header.getCell(k);
			if (cellHeader != null) {
				headersFromExcel.add(cellHeader.toString().trim());
			}
		}
	}

	private Workbook getWorkbook(String excelFilePath) {
		LOGGER.info("ReadWriteFileImpl | getWorkbook");
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(new File(excelFilePath));
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
		}
		return workbook;
	}

	public static String writeFileAndGetPath(SkillMatrixRequest request) {
		LOGGER.info("ReadWriteFileImpl | writeFileAndGetPath");
		String filePathToTrim = null;
		String picpathcut = null;
		LOGGER.info("Printing Branch ID ==>{} ", request.getOrgId());
		String docDirctory = EnovationConfig.buddyConfig.get(EnovationConstants.SugesstionImgPath) + "/"
				+ SMConstant.EXCEL_UPLOAD + "/" + request.getOrgId() + "/" + request.getTitle() + "/"
				+ SMConstant.EMPLOYEE + "/" + request.getCreatedBy() + "/" + SMConstant.UPLOADED_ON + "_"
				+ java.time.LocalDate.now();
		filePathToTrim = SMConstant.EXCEL_UPLOAD + "/" + request.getOrgId() + "/" + request.getTitle() + "/"
				+ SMConstant.EMPLOYEE + "/" + request.getCreatedBy() + "/" + SMConstant.UPLOADED_ON + "_"
				+ java.time.LocalDate.now();
		LOGGER.info("File Path To Trim ==>{} ", filePathToTrim);
		LOGGER.info("Document Directory ==>{} ", docDirctory);
		for (MultipartFile document : request.getExcel()) {
			String fileName = document.getOriginalFilename();
			request.setFileName(fileName);
			picpathcut = WriteFilesUtils.writeFileOnServer(document, docDirctory, filePathToTrim);
			LOGGER.info("File Name ==>{}  ", fileName);
			LOGGER.info("File path to upload {} ", picpathcut);
		}
		request.setDbUploadPath(picpathcut);
		LOGGER.info("PaceConfig.configurations.get(\"documentUploadPath\") + \"/\" {} ", picpathcut);
		return EnovationConfig.buddyConfig.get(EnovationConstants.SugesstionImgPath) + "/" + picpathcut;
	}

}
