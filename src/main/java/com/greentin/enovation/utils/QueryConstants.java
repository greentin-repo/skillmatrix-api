package com.greentin.enovation.utils;

public class QueryConstants {
	public static final String saveLoginFeedback_GET_FEEDBACK_DATA="SELECT f.feedback,f.created_date,f.ratings,f.feedback_by_id,f.feedback_type_id,ft.feedback_type,o.name,concat(e.first_name,' ',e.last_name) as 'Employee Name',e.email_id,f.source_type,o.portal_link,e.contact_no FROM feedback f INNER JOIN feedback_type ft ON ft.feedback_type_id=f.feedback_type_id INNER JOIN tbl_employee_details e ON (f.user_name=e.email_id OR f.user_name=e.cmpy_emp_id OR f.user_name=e.contact_no) INNER JOIN master_organization o ON o.org_id=e.org_id WHERE f.feedback_id=:feedId";
	public static final String saveLoginFeedback_GET_FEEDBACK_COUNT_DATA="SELECT COUNT(*) FROM feedback WHERE  user_name IS NOT NULL";
}
