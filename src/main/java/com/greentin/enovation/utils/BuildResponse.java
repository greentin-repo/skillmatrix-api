package com.greentin.enovation.utils;

import com.greentin.enovation.accesscontrol.UserResponse;
import com.greentin.enovation.employee.EmployeeDTO;
import com.greentin.enovation.response.MasterResponse;
import com.greentin.enovation.response.SkillMatrixResponse;

public class BuildResponse {

	/**
	 * @author Vinay B Dec 31, 2019 12:35:18 PM
	 * @param mstrList
	 * @return
	 */
	public static MasterResponse success(MasterResponse mstrList) {
		mstrList.setStatus(EnovationConstants.statusSuccess);
		mstrList.setStatusCode(EnovationConstants.Code200);
		mstrList.setResult(EnovationConstants.resultTrue);
		return mstrList;
	}

	/**
	 * @author Vinay B Dec 31, 2019 12:36:01 PM
	 * @param mstrList
	 * @return
	 */
	public static MasterResponse fail(MasterResponse mstrList) {
		mstrList.setStatus(EnovationConstants.statusFail);
		mstrList.setStatusCode(EnovationConstants.Code200);
		mstrList.setResult(EnovationConstants.resultFalse);
		return mstrList;
	}

	/**
	 * @author Vinay B Dec 31, 2019 12:36:01 PM
	 * @param mstrList
	 * @return
	 */
	public static MasterResponse fail100(MasterResponse mstrList) {
		mstrList.setStatus(EnovationConstants.statusFail);
		mstrList.setStatusCode(EnovationConstants.Code100);
		mstrList.setResult(EnovationConstants.resultFalse);
		return mstrList;
	}

	/**
	 * @author Vinay B Dec 31, 2019 12:37:03 PM
	 * @param mstrList
	 * @return
	 */
	public static MasterResponse badRequest(MasterResponse mstrList) {
		mstrList.setStatus(EnovationConstants.statusFail);
		mstrList.setStatusCode(EnovationConstants.CODE400);
		mstrList.setResult(EnovationConstants.resultFalse);
		mstrList.setReason(EnovationConstants.BAD_REQUEST);
		return mstrList;
	}

	/**
	 * @author Vinay B Dec 31, 2019 12:37:45 PM
	 * @param mstrList
	 * @return
	 */
	public static MasterResponse internalServerError(MasterResponse mstrList) {
		mstrList.setStatus(EnovationConstants.statusFail);
		mstrList.setStatusCode(EnovationConstants.Code500);
		mstrList.setResult(EnovationConstants.ResultFalse);
		mstrList.setReason(EnovationConstants.SERVER_EORROR);
		return mstrList;
	}

	public static MasterResponse failWithCode100(MasterResponse mstrList) {
		mstrList.setStatus(EnovationConstants.statusFail);
		mstrList.setStatusCode(EnovationConstants.Code100);
		mstrList.setResult(EnovationConstants.ResultFalse);
		return mstrList;
	}

	public static MasterResponse failWithCode100PendingActions(MasterResponse mstrList) {
		mstrList.setStatus(EnovationConstants.statusFail);
		mstrList.setStatusCode(EnovationConstants.Code100);
		mstrList.setResult(EnovationConstants.ResultFalse);
		mstrList.setReason("Actions pending.User to login & close actions first.");
		return mstrList;
	}
	
	/**
	 * @author rakesh 12 june 2020
	 * @param response
	 * @return
	 */
	public static UserResponse success(UserResponse response) {
		response.setStatus(EnovationConstants.statusSuccess);
		response.setStatusCode(EnovationConstants.Code200);
		response.setResult(EnovationConstants.resultTrue);
		return response;
	}

	/**
	 * @author rakesh 12 june 2020
	 * @param response
	 * @return
	 */
	public static UserResponse fail(UserResponse response) {
		response.setStatus(EnovationConstants.statusFail);
		response.setStatusCode(EnovationConstants.Code200);
		response.setResult(EnovationConstants.resultFalse);
		return response;
	}

	/**
	 * @author rakesh 12 june 2020
	 * @param response
	 * @return
	 */
	public static UserResponse fail100(UserResponse response) {
		response.setStatus(EnovationConstants.statusFail);
		response.setStatusCode(EnovationConstants.Code100);
		response.setResult(EnovationConstants.resultFalse);
		return response;
	}

	/**
	 * @author rakesh 12 june 2020
	 * @param response
	 * @return
	 */
	public static UserResponse badRequest(UserResponse response) {
		response.setStatus(EnovationConstants.statusFail);
		response.setStatusCode(EnovationConstants.CODE400);
		response.setResult(EnovationConstants.resultFalse);
		response.setReason(EnovationConstants.BAD_REQUEST);
		return response;
	}

	/**
	 * @author rakesh 12 june 2020
	 * @param response
	 * @return
	 */
	public static UserResponse internalServerError(UserResponse response) {
		response.setStatus(EnovationConstants.statusFail);
		response.setStatusCode(EnovationConstants.Code500);
		response.setResult(EnovationConstants.ResultFalse);
		response.setReason(EnovationConstants.SERVER_EORROR);
		return response;
	}

	/**
	 * @author Vinay B. Dec 27, 2021 3:04:16 PM
	 */
	public static UserResponse failWithCode100(UserResponse response) {
		response.setStatus(EnovationConstants.statusFail);
		response.setStatusCode(EnovationConstants.Code100);
		response.setResult(EnovationConstants.ResultFalse);
		return response;
	}

	/**
	 * @author Anant K. August 08, 2023 3:04:16 PM
	 */
	public static SkillMatrixResponse success(SkillMatrixResponse res) {
		res.setStatus(EnovationConstants.statusSuccess);
		res.setStatusCode(EnovationConstants.Code200);
		res.setResult(EnovationConstants.resultTrue);
		return res;

	}

	public static SkillMatrixResponse fail(SkillMatrixResponse res) {
		res.setStatus(EnovationConstants.statusFail);
		res.setStatusCode(EnovationConstants.Code200);
		res.setResult(EnovationConstants.resultFalse);
		return res;
	}

	public static SkillMatrixResponse fail100(SkillMatrixResponse res) {
		res.setStatus(EnovationConstants.statusFail);
		res.setStatusCode(EnovationConstants.Code100);
		res.setResult(EnovationConstants.resultFalse);
		return res;
	}

	public static SkillMatrixResponse internalServerError(SkillMatrixResponse res) {
		res.setStatus(EnovationConstants.statusFail);
		res.setStatusCode(EnovationConstants.Code500);
		res.setResult(EnovationConstants.ResultFalse);
		return res;
	}

	public static SkillMatrixResponse badRequest(SkillMatrixResponse res) {
		res.setStatus(EnovationConstants.statusFail);
		res.setStatusCode(EnovationConstants.CODE400);
		res.setResult(EnovationConstants.ResultFalse);
		res.setReason(EnovationConstants.BAD_REQUEST);
		return res;
	}
	public static void success(EmployeeDTO response) {
		response.setStatus(EnovationConstants.statusSuccess);
		response.setStatusCode(EnovationConstants.Code200);
		response.setResult(EnovationConstants.ResultTrue);
	}

	public static void failWithCode100(EmployeeDTO response) {
		response.setStatus(EnovationConstants.statusFail);
		response.setStatusCode(EnovationConstants.Code100);
		response.setResult(EnovationConstants.ResultFalse);
	}

	public static void fail(EmployeeDTO response) {
		response.setStatus(EnovationConstants.statusFail);
		response.setStatusCode(EnovationConstants.Code200);
		response.setResult(EnovationConstants.ResultFalse);
	}
	
	public static void internalServerError(EmployeeDTO response) {
		response.setStatus(EnovationConstants.statusFail);
		response.setStatusCode(EnovationConstants.Code500);
		response.setResult(EnovationConstants.ResultFalse);
		response.setReason(EnovationConstants.SERVER_EORROR);
	}
}
