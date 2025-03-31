package com.greentin.enovation.accesscontrol;

import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.greentin.enovation.audit.AuditComponent;
import com.greentin.enovation.beans.EmployeeDetailsBean;
import com.greentin.enovation.beans.MenuData;
import com.greentin.enovation.bo.model.EmployeeDetailsBO;
import com.greentin.enovation.communication.ICommunication;
import com.greentin.enovation.config.EnovationConfig;
import com.greentin.enovation.employee.EmployeeDTO;
import com.greentin.enovation.master.IMasterDao;
import com.greentin.enovation.model.BranchAccessSetup;
import com.greentin.enovation.model.EmailTemplateMaster;
import com.greentin.enovation.model.EmployeeDetails;
import com.greentin.enovation.model.ModuleSubscription;
import com.greentin.enovation.model.RoleMenuAccess;
import com.greentin.enovation.model.StatusMaster;
import com.greentin.enovation.model.SubscriptionType;
import com.greentin.enovation.model.UserMenuAccess;
import com.greentin.enovation.security.JwtTokenProvider;
import com.greentin.enovation.security.UserRepository;
import com.greentin.enovation.utils.BuildResponse;
import com.greentin.enovation.utils.CommonUtils;
import com.greentin.enovation.utils.EnovationConstants;
import com.greentin.enovation.utils.MailUtil;

@Service
public class UserService implements UserServiceInterface {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Autowired
	AuditComponent audit;

	@Autowired
	EnovationConfig enoConfig;

	@Autowired
	UserDaoInterface userdaoinfc;

	@Autowired
	IMasterDao masterDao;

	@Autowired
	PasswordEncoder password;

	@Autowired
	@Qualifier("taskExecutor")
	private TaskExecutor taskExecutor;

	@Autowired
	JwtTokenProvider tokenProvider;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	private ICommunication communication;

	@Autowired
	CommonUtils commonUtils;

	@Value("${app.appversion}")
	private String appversion;

	@Override
	public UserResponse saveProduct(Product product) {
		LOGGER.info("# INSIDE IN SAVEPRODUCT ");
		UserResponse userDTO = new UserResponse();
		try {
			boolean isProductExists = userdaoinfc.isProductExists(product);
			if (!isProductExists) {
				boolean saveProduct = userdaoinfc.saveProduct(product);
				if (saveProduct) {
					userDTO.setStatus(EnovationConstants.statusSuccess);
					userDTO.setStatusCode(EnovationConstants.Code200);
					userDTO.setResult(EnovationConstants.ResultTrue);
					userDTO.setReason("SAVEPRODUCT");
					return userDTO;
				} else {
					userDTO.setStatus(EnovationConstants.statusFail);
					userDTO.setStatusCode(EnovationConstants.Code200);
					userDTO.setResult(EnovationConstants.ResultFalse);
					userDTO.setReason("NOT SAVEPRODUCT");
					return userDTO;
				}
			} else {
				userDTO.setStatus(EnovationConstants.statusFail);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultFalse);
				userDTO.setReason(EnovationConstants.ALREADY_EXITS);
				return userDTO;
			}

		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			userDTO.setReason("EXCEPTION OCCURED IN SAVEPRODUCT API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN SAVEPRODUCT  " + e.getMessage());
			return userDTO;
		}
	}

	@Override
	public UserResponse updateProduct(Product product) {
		LOGGER.info("# INSIDE IN UPDATEPRODUCT ");
		UserResponse userDTO = new UserResponse();
		try {
			boolean updateProduct = userdaoinfc.updateProduct(product);
			if (updateProduct) {
				userDTO.setStatus(EnovationConstants.statusSuccess);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultTrue);
				userDTO.setReason(" updateProduct");
				return userDTO;
			} else {
				userDTO.setStatus(EnovationConstants.statusFail);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultFalse);
				userDTO.setReason("NOT updateProduct");
				return userDTO;
			}

		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			userDTO.setReason("EXCEPTION OCCURED IN updateProduct API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN updateProduct  " + e.getMessage());
			return userDTO;
		}
	}

	@Override
	public UserResponse getProductList() {
		UserResponse userList = null;
		try {
			List<Product> productList = userdaoinfc.getProductList();
			if (productList != null && productList.size() > 0) {
				userList = new UserResponse();
				userList.setStatus(EnovationConstants.statusSuccess);
				userList.setStatusCode(EnovationConstants.Code200);
				userList.setResult(EnovationConstants.ResultTrue);
				userList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				userList.setProductList(productList);
				return userList;
			} else {
				userList = new UserResponse();
				userList.setStatus(EnovationConstants.statusSuccess);
				userList.setStatusCode(EnovationConstants.Code200);
				userList.setResult(EnovationConstants.ResultFalse);
				userList.setReason(EnovationConstants.RecordsDoesNotExist);
				return userList;
			}

		} catch (Exception e) {
			userList = new UserResponse();
			userList.setStatus(EnovationConstants.statusFail);
			userList.setStatusCode(EnovationConstants.Code500);
			userList.setResult(EnovationConstants.ResultFalse);
			userList.setReason("Exception Occured in getProductList Api :" + e.getMessage());
			return userList;
		}
	}

	@Override
	public UserResponse getActiveProductList() {
		UserResponse userList = null;
		try {
			List<Product> productList = userdaoinfc.getActiveProductList();
			if (productList != null && productList.size() > 0) {
				userList = new UserResponse();
				userList.setStatus(EnovationConstants.statusSuccess);
				userList.setStatusCode(EnovationConstants.Code200);
				userList.setResult(EnovationConstants.ResultTrue);
				userList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				userList.setProductList(productList);
				return userList;
			} else {
				userList = new UserResponse();
				userList.setStatus(EnovationConstants.statusSuccess);
				userList.setStatusCode(EnovationConstants.Code200);
				userList.setResult(EnovationConstants.ResultFalse);
				userList.setReason(EnovationConstants.RecordsDoesNotExist);
				return userList;
			}

		} catch (Exception e) {
			userList = new UserResponse();
			userList.setStatus(EnovationConstants.statusFail);
			userList.setStatusCode(EnovationConstants.Code500);
			userList.setResult(EnovationConstants.ResultFalse);
			userList.setReason("Exception Occured in getActiveProductList Api :" + e.getMessage());
			return userList;
		}
	}

	@Override
	public UserResponse getMenuListByProductId(Integer productId) {
		UserResponse userList = null;
		try {
			List<Menu> menuList = userdaoinfc.getMenuListByProductId(productId);
			if (menuList != null && menuList.size() > 0) {
				userList = new UserResponse();
				userList.setStatus(EnovationConstants.statusSuccess);
				userList.setStatusCode(EnovationConstants.Code200);
				userList.setResult(EnovationConstants.ResultTrue);
				userList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				userList.setMenuList(menuList);
				return userList;
			} else {
				userList = new UserResponse();
				userList.setStatus(EnovationConstants.statusSuccess);
				userList.setStatusCode(EnovationConstants.Code200);
				userList.setResult(EnovationConstants.ResultFalse);
				userList.setReason(EnovationConstants.RecordsDoesNotExist);
				return userList;
			}

		} catch (Exception e) {
			userList = new UserResponse();
			userList.setStatus(EnovationConstants.statusFail);
			userList.setStatusCode(EnovationConstants.Code500);
			userList.setResult(EnovationConstants.ResultFalse);
			userList.setReason("EXCEPTION OCCURED IN GETMENULISTBYPRODUCTID API :" + e.getMessage());
			return userList;
		}
	}

	@Override
	public UserResponse saveMenu(Menu menu) {
		LOGGER.info("# INSIDE IN SAVEMENU ");
		UserResponse userDTO = new UserResponse();
		try {
			boolean isProductExists = userdaoinfc.isMenuExists(menu);
			if (!isProductExists) {
				boolean saveMenu = userdaoinfc.saveMenu(menu);
				if (saveMenu) {
					userDTO.setStatus(EnovationConstants.statusSuccess);
					userDTO.setStatusCode(EnovationConstants.Code200);
					userDTO.setResult(EnovationConstants.ResultTrue);
					userDTO.setReason("SAVEMENU");
					return userDTO;
				} else {
					userDTO.setStatus(EnovationConstants.statusFail);
					userDTO.setStatusCode(EnovationConstants.Code200);
					userDTO.setResult(EnovationConstants.ResultFalse);
					userDTO.setReason("NOT SAVEMENU");
					return userDTO;
				}
			} else {
				userDTO.setStatus(EnovationConstants.statusFail);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultFalse);
				userDTO.setReason(EnovationConstants.ALREADY_EXITS);
				return userDTO;
			}

		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			userDTO.setReason("EXCEPTION OCCURED IN SAVEMENU API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN SAVEMENU  " + e.getMessage());
			return userDTO;
		}
	}

	@Override
	public UserResponse updateMenu(Menu menu) {
		LOGGER.info("# INSIDE IN UPDATEPRODUCT ");
		UserResponse userDTO = new UserResponse();
		try {
			boolean updateMenu = userdaoinfc.updateMenu(menu);
			if (updateMenu) {
				userDTO.setStatus(EnovationConstants.statusSuccess);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultTrue);
				userDTO.setReason(" UPDATEMENU");
				return userDTO;
			} else {
				userDTO.setStatus(EnovationConstants.statusFail);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultFalse);
				userDTO.setReason("NOT UPDATEMENU");
				return userDTO;
			}

		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			userDTO.setReason("EXCEPTION OCCURED IN UPDATEMENU API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN UPDATEMENU  " + e.getMessage());
			return userDTO;
		}
	}

	@Override
	public UserResponse saveSubMenu(SubMenu submenu) {
		LOGGER.info("# INSIDE IN SAVESUBMENU ");
		UserResponse userDTO = new UserResponse();
		try {
			boolean isSubMenuExists = userdaoinfc.isSubMenuExists(submenu);
			if (!isSubMenuExists) {
				boolean saveSubMenu = userdaoinfc.saveSubMenu(submenu);
				if (saveSubMenu) {
					userDTO.setStatus(EnovationConstants.statusSuccess);
					userDTO.setStatusCode(EnovationConstants.Code200);
					userDTO.setResult(EnovationConstants.ResultTrue);
					userDTO.setReason("SAVESUBMENU");
					return userDTO;
				} else {
					userDTO.setStatus(EnovationConstants.statusFail);
					userDTO.setStatusCode(EnovationConstants.Code200);
					userDTO.setResult(EnovationConstants.ResultFalse);
					userDTO.setReason("NOT SAVESUBMENU");
					return userDTO;
				}
			} else {
				userDTO.setStatus(EnovationConstants.statusFail);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultFalse);
				userDTO.setReason(EnovationConstants.ALREADY_EXITS);
				return userDTO;
			}

		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			userDTO.setReason("EXCEPTION OCCURED IN SAVESUBMENU API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN SAVESUBMENU  " + e.getMessage());
			return userDTO;
		}
	}

	@Override
	public UserResponse updateSubMenu(SubMenu submenu) {
		LOGGER.info("# INSIDE IN UPDATESUBMENU ");
		UserResponse userDTO = new UserResponse();
		try {
			boolean updateSubMenu = userdaoinfc.updateSubMenu(submenu);
			if (updateSubMenu) {
				userDTO.setStatus(EnovationConstants.statusSuccess);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultTrue);
				userDTO.setReason(" UPDATESUBMENU");
				return userDTO;
			} else {
				userDTO.setStatus(EnovationConstants.statusFail);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultFalse);
				userDTO.setReason("NOT UPDATESUBMENU");
				return userDTO;
			}

		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			userDTO.setReason("EXCEPTION OCCURED IN UPDATESUBMENU API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN UPDATESUBMENU  " + e.getMessage());
			return userDTO;
		}
	}

	@Override
	public UserResponse disableSubMenu(SubMenu submenu) {
		LOGGER.info("# INSIDE IN DISABLESUBMENU ");
		UserResponse userDTO = new UserResponse();
		try {
			boolean disableSubMenu = userdaoinfc.disableSubMenu(submenu);
			if (disableSubMenu) {
				userDTO.setStatus(EnovationConstants.statusSuccess);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultTrue);
				if (submenu.getStatus() == EnovationConstants.DESABLE_STATUS) {
					userDTO.setReason("DISABLE SUBMENU");
				} else {
					userDTO.setReason("Enable SUBMENU");
				}
				return userDTO;
			} else {
				userDTO.setStatus(EnovationConstants.statusFail);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultFalse);
				userDTO.setReason("NOT DISABLESUBMENU");
				return userDTO;
			}

		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			userDTO.setReason("EXCEPTION OCCURED IN DISABLESUBMENU API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN DISABLESUBMENU  " + e.getMessage());
			return userDTO;
		}
	}

	@Override
	public UserResponse disableProduct(Product product) {
		LOGGER.info("# INSIDE IN DISABLEPRODUCT ");
		UserResponse userDTO = new UserResponse();
		try {
			boolean disableProduct = userdaoinfc.disableProduct(product);
			if (disableProduct) {
				userDTO.setStatus(EnovationConstants.statusSuccess);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultTrue);
				if (product.getStatus() == EnovationConstants.DESABLE_STATUS) {
					userDTO.setReason("DISABLE PRODUCT");
				} else {
					userDTO.setReason("ENABLE PRODUCT");
				}
				userDTO.setReason("DISABLE PRODUCT");
				return userDTO;
			} else {
				userDTO.setStatus(EnovationConstants.statusFail);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultFalse);
				userDTO.setReason("NOT DISABLEPRODUCT");
				return userDTO;
			}
		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			userDTO.setReason("EXCEPTION OCCURED IN DISABLEPRODUCT API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN DISABLEPRODUCT  " + e.getMessage());
			return userDTO;
		}
	}

	@Override
	public UserResponse registration(Registration user) {
		LOGGER.info("# INSIDE IN SAVE USER ");
		UserResponse userDTO = new UserResponse();
		try {
			// set orgdamin and alies
			splitDomainName(user);// split domain and alies
			// check orgDain is exists or not in organization table
			boolean isOrgDomaimExists = userdaoinfc.isOrgDomaimExists(user);
			if (!isOrgDomaimExists) {
				// if orgDomain is not exists then =>
				boolean saveUser = userdaoinfc.registration(user);
				if (saveUser) {
					userDTO.setStatus(EnovationConstants.statusSuccess);
					userDTO.setStatusCode(EnovationConstants.Code200);
					userDTO.setResult(EnovationConstants.ResultTrue);
					userDTO.setReason("SAVE USER");
					return userDTO;
				} else {
					userDTO.setStatus(EnovationConstants.statusFail);
					userDTO.setStatusCode(EnovationConstants.Code200);
					userDTO.setResult(EnovationConstants.ResultFalse);
					userDTO.setReason("NOT SAVE USER");
					return userDTO;
				}
			} else {
				// check email is exists or not in employee table
				List<EmployeeDetails> list = userdaoinfc.isEmployeeEmailExists(user.getEmail());

				if (list != null && list.size() > 0) {
					// --yes email already exist please check your mails or login with your cred.
					userDTO.setStatus(EnovationConstants.statusFail);
					userDTO.setStatusCode(EnovationConstants.Code200);
					userDTO.setResult(EnovationConstants.ResultFalse);
					userDTO.setReason(EnovationConstants.ALREADY_EXITS);
					return userDTO;
				} else {
					// get org id and branch in employee table for this perticuler org.
					List<EmployeeDetails> empList = userdaoinfc.getEmpListByOrg(user.getOrgDomain());
					if (empList != null && empList.size() > 0) {
						// if org exists and email not exist condition true =>
						// save user as a role is superadmin
						boolean saveRegUser = userdaoinfc.saveRegUser(empList.get(0), user);
						if (saveRegUser) {

							userDTO.setStatus(EnovationConstants.statusSuccess);
							userDTO.setStatusCode(EnovationConstants.Code200);
							userDTO.setResult(EnovationConstants.ResultTrue);
							userDTO.setReason(EnovationConstants.DATA_SAVE);
							return userDTO;
						}
					}
					// email exists --yes / no
					// --no route to user to register company and give him super admin

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			userDTO.setReason("EXCEPTION OCCURED IN SAVE USER API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN SAVE USER  " + e.getMessage());
			return userDTO;
		}
		return userDTO;
	}

	private void splitDomainName(Registration user) {
		String mail = user.getEmail();
		String comapanyDoaminName = mail.split("@")[1];
		System.out.println("Company Domain Name :" + comapanyDoaminName);
		user.setOrgDomain(comapanyDoaminName);
		String aliseName = comapanyDoaminName.split("\\.")[0];
		System.out.println("Alise Name : " + aliseName);
		user.setAlies(aliseName);
	}

	@Override
	public UserResponse verifyEmail(Registration user) {
		LOGGER.info("#INSIDE IN VERIFYEMAIL ");
		UserResponse userDTO = new UserResponse();
		try {
			boolean checkEmailVerified = userdaoinfc.checkEmailVerified(user);
			if (checkEmailVerified) {
				Registration registrationOBJ = userdaoinfc.verifyEmail(user);
				if (registrationOBJ != null) {
					userDTO.setStatus(EnovationConstants.statusSuccess);
					userDTO.setStatusCode(EnovationConstants.Code200);
					userDTO.setResult(EnovationConstants.ResultTrue);
					userDTO.setEmail(registrationOBJ.getEmail());
					userDTO.setPortalLink(registrationOBJ.getPortalLink());
					userDTO.setReason(EnovationConstants.Email_VERIFIED);
					return userDTO;
				} else {
					userDTO.setStatus(EnovationConstants.statusFail);
					userDTO.setStatusCode(EnovationConstants.Code200);
					userDTO.setResult(EnovationConstants.ResultFalse);
					userDTO.setReason(EnovationConstants.ACCOUNT_ALREADY_VERIFIED);
					return userDTO;
				}
			} else {
				userDTO.setStatus(EnovationConstants.statusFail);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultFalse);
				userDTO.setReason(EnovationConstants.RecordsDoesNotExist);
				return userDTO;
			}

		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setReason(EnovationConstants.EMAIL_ALREADY_EXITS);
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN VERIFYEMAIL  " + e.getMessage());
			return userDTO;
		}
	}

	@Override
	public UserResponse saveUser(Users user) {
		LOGGER.info("# INSIDE IN SAVE USER ");
		UserResponse userDTO = new UserResponse();
		try {
			/*
			 * boolean isUsernameExists=userdaoinfc.isUsernameExists(user);
			 * if(!isUsernameExists){
			 */
			/*
			 * boolean isuseremailExists=userdaoinfc.isUseremailExists(user.getEmail());
			 * if(!isuseremailExists){
			 */boolean saveUser = userdaoinfc.saveUser(user);
			if (saveUser) {
				userDTO.setStatus(EnovationConstants.statusSuccess);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultTrue);
				return userDTO;
			} else {
				userDTO.setStatus(EnovationConstants.statusFail);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultFalse);
				return userDTO;
			}
			/*
			 * }else { userDTO.setStatus(EnovationConstants.statusFail);
			 * userDTO.setStatusCode(EnovationConstants.Code200);
			 * userDTO.setResult(EnovationConstants.ResultFalse);
			 * userDTO.setReason("email already exists"); return userDTO; }
			 */
			/*
			 * }else { userDTO.setStatus(EnovationConstants.statusFail);
			 * userDTO.setStatusCode(EnovationConstants.Code200);
			 * userDTO.setResult(EnovationConstants.ResultFalse);
			 * userDTO.setReason("username already exists"); return userDTO; }
			 */

		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			userDTO.setReason("EXCEPTION OCCURED IN SAVE USER API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN SAVE USER  " + e.getMessage());
			return userDTO;
		}
	}

	@Override
	public UserResponse getRegisterUserList() {
		UserResponse userList = null;
		try {
			List<Users> registerList = userdaoinfc.getRegisterUserList();
			if (registerList != null && registerList.size() > 0) {
				userList = new UserResponse();
				userList.setStatus(EnovationConstants.statusSuccess);
				userList.setStatusCode(EnovationConstants.Code200);
				userList.setResult(EnovationConstants.ResultTrue);
				userList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				userList.setUsersList(registerList);
				return userList;
			} else {
				userList = new UserResponse();
				userList.setStatus(EnovationConstants.statusSuccess);
				userList.setStatusCode(EnovationConstants.Code200);
				userList.setResult(EnovationConstants.ResultFalse);
				userList.setReason(EnovationConstants.RecordsDoesNotExist);
				return userList;
			}

		} catch (Exception e) {
			userList = new UserResponse();
			userList.setStatus(EnovationConstants.statusFail);
			userList.setStatusCode(EnovationConstants.Code500);
			userList.setResult(EnovationConstants.ResultFalse);
			userList.setReason("EXCEPTION OCCURED IN GETMENULISTBYPRODUCTID API :" + e.getMessage());
			return userList;
		}
	}

	@Override
	public UserResponse getGreentinUserList() {
		UserResponse userList = null;
		try {
			List<Users> GreentinUserList = userdaoinfc.getGreentinUserList();
			if (GreentinUserList != null && GreentinUserList.size() > 0) {
				userList = new UserResponse();
				userList.setStatus(EnovationConstants.statusSuccess);
				userList.setStatusCode(EnovationConstants.Code200);
				userList.setResult(EnovationConstants.ResultTrue);
				userList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				userList.setUsersList(GreentinUserList);
				return userList;
			} else {
				userList = new UserResponse();
				userList.setStatus(EnovationConstants.statusSuccess);
				userList.setStatusCode(EnovationConstants.Code200);
				userList.setResult(EnovationConstants.ResultFalse);
				userList.setReason(EnovationConstants.RecordsDoesNotExist);
				return userList;
			}

		} catch (Exception e) {
			userList = new UserResponse();
			userList.setStatus(EnovationConstants.statusFail);
			userList.setStatusCode(EnovationConstants.Code500);
			userList.setResult(EnovationConstants.ResultFalse);
			userList.setReason("EXCEPTION OCCURED IN getGreentinUserList API :" + e.getMessage());
			return userList;
		}
	}

	@SuppressWarnings("unused")
	@Override
	public UserResponse login(EmployeeDetails employee/* ,HttpServletRequest request */) {
		UserResponse response = null;
		try {

			/*
			 * CHECK RECORDS IN REGISTRATION TABLE
			 */
			Registration loginReg = userdaoinfc.checkLoginRegisdtration(employee);
			/*
			 * (REGISTRATION TABLE) IF RECORDS EXISTS=>
			 */
			if (loginReg != null) {
				/*
				 * CHECK EMAIL VERIFIED OR NOT IF YES=>
				 */
				if (loginReg.getIsEmailVerified() == EnovationConstants.ONE) {

					response = authLogin(employee);
					return response;

				} else {
					/*
					 * CHECK EMAIL VERIFIED OR NOT IF NOT=>
					 */
					response = new UserResponse();
					response.setStatus(EnovationConstants.statusSuccess);
					response.setStatusCode(EnovationConstants.Code200);
					response.setResult(EnovationConstants.ResultFalse);
					response.setReason(EnovationConstants.EMAIL_NOT_VERIFIED);
					return response;
				}

			} else {
				/*
				 * (REGISTRATION TABLE) IF RECORDS EXISTS=>
				 */
				/*
				 * (EMPLOYEE TABLE) CHECK RECORDS EXISTS=>
				 */
				EmployeeDetails checkEmpLogin = userdaoinfc.checkLoginEmployee(employee);
				/*
				 * (EMPLOYEE TABLE) CHECK RECORDS EXISTS IF EXISTS=>
				 */
				if (checkEmpLogin != null) {
					// Check If Role Employee or Not
					/*
					 * Set<Role> roles = checkEmpLogin.getRoles(); if(roles!=null &&
					 * roles.size()==1) { Iterator< Role> itrRole = roles.iterator();
					 * if(itrRole.hasNext()) { Role role = itrRole.next();
					 * if(role.getName()==RoleName.EMPLOYEE) { response=new UserResponse();
					 * response.setStatus(EnovationConstants.statusSuccess);
					 * response.setStatusCode(EnovationConstants.Code200);
					 * response.setResult(EnovationConstants.ResultFalse);
					 * response.setReason(EnovationConstants.EMPLOYEE_LOGIN_ACCESS); return
					 * response; } } }
					 */
					/*
					 * (EMPLOYEE TABLE) CHECK EMAIL VERIFIED IF VERIFIED=>
					 */
					if (checkEmpLogin.getIsEmailVerified() == EnovationConstants.ONE) {

						response = authLogin(employee);
						return response;
						// }else {
						// response=new UserResponse();
						// response.setStatus(EnovationConstants.statusSuccess);
						// response.setStatusCode(EnovationConstants.Code200);
						// response.setResult(EnovationConstants.ResultFalse);
						// response.setReason(EnovationConstants.INCORRECT_PASSWORD);
						// return response;
					} else {
						/*
						 * (EMPLOYEE TABLE) CHECK EMAIL VERIFIED IF NOT VERIFIED=>
						 */
						response = new UserResponse();
						response.setStatus(EnovationConstants.statusSuccess);
						response.setStatusCode(EnovationConstants.Code200);
						response.setResult(EnovationConstants.ResultFalse);
						response.setReason(EnovationConstants.EMAIL_NOT_VERIFIED);
						return response;
					}
				} else {
					/*
					 * (EMPLOYEE TABLE) CHECK RECORDS EXISTS IF NOT EXISTS=>
					 */
					response = new UserResponse();
					response.setStatus(EnovationConstants.statusSuccess);
					response.setStatusCode(EnovationConstants.Code200);
					response.setResult(EnovationConstants.ResultFalse);
					response.setReason(EnovationConstants.RecordsDoesNotExist);
					return response;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			response = new UserResponse();
			response.setStatus(EnovationConstants.statusFail);
			response.setStatusCode(EnovationConstants.CODE401);
			response.setResult(EnovationConstants.ResultFalse);

			EmployeeDetails checkEmpLogin = userdaoinfc.checkLoginEmployee(employee);

			System.out.println("=============== Employee Id ==> " + checkEmpLogin.getEmailId());
			System.out.println("=============== Employee FCM Key ==> " + checkEmpLogin.getFcmKey());

			int trackerId = audit.insertLoginAudit(0, checkEmpLogin.getEmpId(), checkEmpLogin.getFcmKey(),
					checkEmpLogin.getEmailId(), EnovationConstants.LOGIN_FAILED, "NA",
					checkEmpLogin.getBranch().getBranchId(), checkEmpLogin.getOrganization().getOrgId(), "");
			response.setTransactionId(trackerId);

			if (trackerId > 0) {
				System.out.println("====================== Record Inserted ======================");
			} else {
				System.out.println("====================== Failed To Insert Record ======================");
			}
			response.setReason("Invalid Password, Please Enter Correct Password");

			return response;
		}

	}

	@SuppressWarnings("unused")
	private UserResponse authLogin(EmployeeDetails employee) throws JsonProcessingException {
		UserResponse response = null;
		/*
		 * AUTHINTICATE EMAIL AND PASSWORD
		 */
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(employee.getEmailId(), employee.getPassword()));

		StringBuffer authToken = new StringBuffer("Bearer ");
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String auth = tokenProvider.generateToken(authentication);
		if (auth != null) {
			authToken.append(auth);
		}
		EmployeeDetails login = userRepository.findByContactNoOrEmailIdOrCmpyEmpId(employee.getEmailId());

		if (login != null) {
			ProductOrgConfig pconfig = userdaoinfc.getProductOrgConfigDet(login);
			long noOfDaysBetween = ChronoUnit.DAYS.between(pconfig.getSubscriptionDate().toInstant(),
					CommonUtils.currentDate().toInstant());
			System.out.println("Number of Days  : " + noOfDaysBetween);
			System.out.println("Subscription Number of Days : " + pconfig.getSubscriptionPlan().getNoOfDays());

			if (noOfDaysBetween > pconfig.getSubscriptionPlan().getNoOfDays()) {
				response = new UserResponse();
				response.setStatus(EnovationConstants.statusFail);
				response.setStatusCode(EnovationConstants.Code100);
				response.setResult(EnovationConstants.ResultFalse);
				response.setReason(EnovationConstants.TRIAL_EXPIRED);

			} else {
				/*
				 * SEND VALID RESPONSE AFTER RECORDS FETCH
				 */
				response = new UserResponse();
				response.setStatus(EnovationConstants.statusSuccess);
				response.setStatusCode(EnovationConstants.Code200);
				response.setResult(EnovationConstants.ResultTrue);
				response.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				/*
				 * COPY EMPLOYEE DETAILS RESPONSE IN EMPLOYEE DETAILS BEAN
				 */
				EmployeeDetailsBean empDetails = new EmployeeDetailsBean();
				// Set Emp details if not Null
				if (login != null) {
					System.out.println(login.getCreatedDate());
					System.out.println("Trial Days Period : " + noOfDaysBetween);
					/*
					 * UPDATE FCMKEY AND LOGGEDIN STATUS
					 */
					userdaoinfc.updateFCMKey(login.getEmpId(), employee.getFcmKey());

					if (noOfDaysBetween <= EnovationConstants.TRIAL_PERIOD) {
						login.setNoOfDaysLeft((int) (EnovationConstants.TRIAL_PERIOD - noOfDaysBetween));
					}
					BeanUtils.copyProperties(login, empDetails);
					response.setEmployeeDetails(empDetails);
				}
				/*
				 * ADDED MENU ACCESS LIST IN LOGIN RESPONSE
				 */
				// String email = employee.getEmailId();
				// System.out.println("Printing email ID : " +email);
				//
				// List<MultiBranchAccess> orgExeList = userdaoinfc.getOrgExeBranchList(email);
				// if(!orgExeList.isEmpty()) {
				// response.setMultiBranchList(orgExeList);
				// }

				List<MenuData> menuList = getMenuList(login);
				if (pconfig != null) {
					empDetails.setProductOrgConfigDet(pconfig);
				}
				if (menuList != null)
					response.setMenuDataList(menuList);
				response.setAuthToken(authToken.toString());

				/*
				 * * ADDED STATUS LIST IN LOGIN RESPONSE
				 * 
				 */
				List<StatusMaster> statusList = masterDao.getListofStatus();
				JSONObject status = new JSONObject();
				for (StatusMaster statusMaster : statusList) {
					Gson gson = new Gson();
					String json = gson.toJson(statusMaster);
					JSONObject obj;
					try {
						obj = new JSONObject(json);
						status.put(statusMaster.getStatus(), obj);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				response.setMasterStatus(status.toString());
				String username = null;
				if (login.getEmailId() != null) {
					username = login.getEmailId();
				} else {
					username = login.getContactNo();
				}
				String responseString = "";
				try {
					// responseString=(new Gson()).toJson(response);
				} catch (Exception e) {

					LOGGER.info(ExceptionUtils.getStackTrace(e));
				}

				System.out.println("============== Reading Response =============");

				ObjectMapper mapper = new ObjectMapper();

				String jsonInString = mapper.writeValueAsString(response);
				System.out.println(jsonInString);

				System.out.println("============= JSON Data in String format ==============");
				System.out.println(jsonInString);

				int trackerId = audit.insertLoginAudit(0, login.getEmpId(), employee.getFcmKey(), username,
						EnovationConstants.LOGGEDIN_SUCCESS, "NA", empDetails.getBranch().getBranchId(),
						empDetails.getOrganization().getOrgId(), jsonInString);
				response.setTransactionId(trackerId);

				if (!CollectionUtils.isEmpty(login.getRoles())) {
					if (isRoleSuperAdmin(login)) {
						HashMap<String, Object> data = new HashMap<>();
						long planDay = pconfig.getSubscriptionPlan().getNoOfDays();
						long dayLeft = planDay - noOfDaysBetween;
						if (noOfDaysBetween > planDay) {
							data.put("isSubscriptionExpired", "Y");
						} else {
							data.put("isSubscriptionExpired", "N");
						}
						if (dayLeft <= 15) {
							data.put("isShowPopup", "Y");
						} else {
							data.put("isShowPopup", "N");
						}
						data.put("dayLeft", noOfDaysBetween);
						data.put("subscriptionDate", pconfig.getSubscriptionDate());
						data.put("planDay", planDay);
						data.put("dayLeft", dayLeft);
						response.setData(data);
					}
				}

				return response;
			}

		}

		return response;

	}

	private boolean isRoleSuperAdmin(EmployeeDetails login) {
		for (Role r : login.getRoles()) {
			if (r.getId() == EnovationConstants.SUPER_ADMIN) {
				return true;
			}
		}
		return false;
	}

	@Override
	public UserResponse getListofRole() {
		UserResponse userList = null;
		try {
			List<Role> roleList = userdaoinfc.getListofRole();
			if (roleList != null && roleList.size() > 0) {
				userList = new UserResponse();
				userList.setStatus(EnovationConstants.statusSuccess);
				userList.setStatusCode(EnovationConstants.Code200);
				userList.setResult(EnovationConstants.ResultTrue);
				userList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				userList.setRoleList(roleList);
				return userList;
			} else {
				userList = new UserResponse();
				userList.setStatus(EnovationConstants.statusSuccess);
				userList.setStatusCode(EnovationConstants.Code200);
				userList.setResult(EnovationConstants.ResultFalse);
				userList.setReason(EnovationConstants.RecordsDoesNotExist);
				return userList;
			}

		} catch (Exception e) {
			userList = new UserResponse();
			userList.setStatus(EnovationConstants.statusFail);
			userList.setStatusCode(EnovationConstants.Code500);
			userList.setResult(EnovationConstants.ResultFalse);
			userList.setReason("EXCEPTION OCCURED IN GETLISTOFROLE API :" + e.getMessage());
			return userList;
		}
	}

	@Override
	public UserResponse saveRoleMenuAccess(RoleMenuAccess role) {
		LOGGER.info("# INSIDE IN SAVEROLEMENUACCESS ");
		UserResponse userDTO = new UserResponse();
		try {
			boolean saveRoleMenuAccess = userdaoinfc.saveRoleMenuAccess(role);
			if (saveRoleMenuAccess) {
				userDTO.setStatus(EnovationConstants.statusSuccess);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultTrue);
				userDTO.setReason(" SAVEROLEMENUACCESS");
				return userDTO;
			} else {
				userDTO.setStatus(EnovationConstants.statusFail);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultFalse);
				userDTO.setReason("NOT SAVEROLEMENUACCESS");
				return userDTO;
			}

		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			userDTO.setReason("EXCEPTION OCCURED IN SAVEROLEMENUACCESS API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN SAVEROLEMENUACCESS  " + e.getMessage());
			return userDTO;
		}
	}

	@Override
	public UserResponse updateRoleMenuAccess(RoleMenuAccess role) {
		LOGGER.info("# INSIDE IN UPDATEROLEMENUACCESS ");
		UserResponse userDTO = new UserResponse();
		try {
			boolean updateRoleMenuAccess = userdaoinfc.updateRoleMenuAccess(role);
			if (updateRoleMenuAccess) {
				userDTO.setStatus(EnovationConstants.statusSuccess);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultTrue);
				userDTO.setReason(" UPDATEROLEMENUACCESS");
				return userDTO;
			} else {
				userDTO.setStatus(EnovationConstants.statusFail);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultFalse);
				userDTO.setReason("NOT UPDATEROLEMENUACCESS");
				return userDTO;
			}

		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			userDTO.setReason("EXCEPTION OCCURED IN UPDATEROLEMENUACCESS API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN UPDATEROLEMENUACCESS  " + e.getMessage());
			return userDTO;
		}
	}

	@Override
	public UserResponse getListofRoleAccess(Integer orgId, Integer branchId, Integer roleId) {
		UserResponse userList = null;
		try {
			if (orgId != 0 && branchId != 0) {
				List<MenuData> list = userdaoinfc.getMenuListByRoleId(orgId, branchId, roleId);
				if (list != null && list.size() > 0) {
					userList = new UserResponse();
					userList.setStatus(EnovationConstants.statusSuccess);
					userList.setStatusCode(EnovationConstants.Code200);
					userList.setResult(EnovationConstants.ResultTrue);
					userList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
					userList.setMenuDataList(list);
					return userList;
				} else {
					userList = new UserResponse();
					userList.setStatus(EnovationConstants.statusSuccess);
					userList.setStatusCode(EnovationConstants.Code200);
					userList.setResult(EnovationConstants.ResultFalse);
					userList.setReason(EnovationConstants.RecordsDoesNotExist);
					return userList;
				}
			} else {
				userList = new UserResponse();
				userList.setStatus(EnovationConstants.statusSuccess);
				userList.setStatusCode(EnovationConstants.CODE400);
				userList.setReason(EnovationConstants.BAD_REQUEST);
				return userList;
			}
		} catch (Exception e) {
			userList = new UserResponse();
			userList.setStatus(EnovationConstants.statusFail);
			userList.setStatusCode(EnovationConstants.Code500);
			userList.setResult(EnovationConstants.ResultFalse);
			userList.setReason("EXCEPTION OCCURED IN getGreentinUserList API :" + e.getMessage());
			return userList;
		}
	}

	@Override
	public UserResponse getListofRoleAccessNew(Integer orgId, Integer branchId, Integer roleId, Integer productId) {
		UserResponse userList = null;
		try {
			if (orgId != 0 && branchId != 0) {
				List<MenuData> list = userdaoinfc.getMenuListByRoleIdNew(orgId, branchId, roleId, productId);
				if (list != null && list.size() > 0) {
					userList = new UserResponse();
					userList.setStatus(EnovationConstants.statusSuccess);
					userList.setStatusCode(EnovationConstants.Code200);
					userList.setResult(EnovationConstants.ResultTrue);
					userList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
					userList.setMenuDataList(list);
					return userList;
				} else {
					userList = new UserResponse();
					userList.setStatus(EnovationConstants.statusSuccess);
					userList.setStatusCode(EnovationConstants.Code200);
					userList.setResult(EnovationConstants.ResultFalse);
					userList.setReason(EnovationConstants.RecordsDoesNotExist);
					return userList;
				}
			} else {
				userList = new UserResponse();
				userList.setStatus(EnovationConstants.statusSuccess);
				userList.setStatusCode(EnovationConstants.CODE400);
				userList.setReason(EnovationConstants.BAD_REQUEST);
				return userList;
			}
		} catch (Exception e) {
			userList = new UserResponse();
			userList.setStatus(EnovationConstants.statusFail);
			userList.setStatusCode(EnovationConstants.Code500);
			userList.setResult(EnovationConstants.ResultFalse);
			userList.setReason("EXCEPTION OCCURED IN getGreentinUserList API :" + e.getMessage());
			return userList;
		}
	}

	@Override
	public UserResponse saveRoleAccessList(Set<RoleMenuAccess> role) {
		LOGGER.info("# INSIDE IN SAVEROLEACCESSLIST ");
		UserResponse userDTO = new UserResponse();
		try {
			boolean saveRoleMenuAccess = userdaoinfc.saveRoleAccessList(role);
			if (saveRoleMenuAccess) {
				userDTO.setStatus(EnovationConstants.statusSuccess);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultTrue);
				userDTO.setReason(" SAVEROLEACCESSLIST");
				return userDTO;
			} else {
				userDTO.setStatus(EnovationConstants.statusFail);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultFalse);
				userDTO.setReason("NOT SAVEROLEACCESSLIST");
				return userDTO;
			}

		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			userDTO.setReason("EXCEPTION OCCURED IN SAVEROLEACCESSLIST API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN SAVEROLEACCESSLIST  " + e.getMessage());
			return userDTO;
		}
	}

	private List<MenuData> getMenuList(EmployeeDetails emp) {
		@SuppressWarnings("unused")
		List<MenuData> menuList = null;
		List<MenuData> menuListByModuleSubscription = null;
		try {
			int branchId = emp.getBranch().getBranchId();
			int orgId = emp.getBranch().getOrg().getOrgId();
			Set<Role> roles = emp.getRoles();
			System.out.println(" Org Id " + orgId);
			System.out.println(" branchId Id " + branchId);
			System.out.println(" roleId " + roles);
			// menuList = userdaoinfc.getMenuListByMultipleRoleId(orgId, branchId,
			// roles);/*Deprecated, Now we r check module with subscription*/
			menuListByModuleSubscription = userdaoinfc.getMenuListByModuleSubscription(orgId, branchId, roles, emp);
		} catch (Exception e) {
			LOGGER.info("# INSIDE IN getMenuList Custom Method ");
		}
		// return menuList;
		return menuListByModuleSubscription;
	}

	@Override
	public UserResponse getListofSubscriptionType() {
		LOGGER.info("# INSIDE IN GETLISTOFSUBSCRIPTIONTYPE ");
		UserResponse userDTO = new UserResponse();
		try {
			List<SubscriptionType> ListofSubscriptionType = userdaoinfc.getListofSubscriptionType();
			if (ListofSubscriptionType != null && ListofSubscriptionType.size() > 0) {
				userDTO.setStatus(EnovationConstants.statusSuccess);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultTrue);
				userDTO.setReason(" GETLISTOFSUBSCRIPTIONTYPE");
				userDTO.setListofSubscriptionType(ListofSubscriptionType);
				return userDTO;
			} else {
				userDTO.setStatus(EnovationConstants.statusFail);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultFalse);
				userDTO.setReason("! GETLISTOFSUBSCRIPTIONTYPE");
				return userDTO;
			}

		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			userDTO.setReason("EXCEPTION OCCURED IN GETLISTOFSUBSCRIPTIONTYPE API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN GETLISTOFSUBSCRIPTIONTYPE  " + e.getMessage());
			return userDTO;
		}
	}

	@Override
	public UserResponse updateSetupComleted(ProductOrgConfig request) {
		LOGGER.info("# INSIDE IN UPDATESETUPCOMLETED ");
		UserResponse userDTO = new UserResponse();
		try {
			boolean updateSetupComleted = userdaoinfc.updateSetupComleted(request);
			if (updateSetupComleted) {
				userDTO.setStatus(EnovationConstants.statusSuccess);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultTrue);
				userDTO.setReason(" UPDATESETUPCOMLETED");
				return userDTO;
			} else {
				userDTO.setStatus(EnovationConstants.statusFail);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultFalse);
				userDTO.setReason("! UPDATESETUPCOMLETED");
				return userDTO;
			}

		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			userDTO.setReason("EXCEPTION OCCURED IN UPDATESETUPCOMLETED API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN UPDATESETUPCOMLETED  " + e.getMessage());
			return userDTO;
		}
	}

	@Override
	public UserResponse isEmailVerify(String email) {
		LOGGER.info("# INSIDE IN ISEMAILVERIFY");
		UserResponse userDTO = new UserResponse();
		try {
			List<EmployeeDetails> list = userdaoinfc.isUseremailExists(email);
			if (list != null && list.size() > 0) {
				userDTO.setStatus(EnovationConstants.statusSuccess);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultTrue);
				// userDTO.setReason(EnovationConstants.ALREADY_EXITS);
				return userDTO;
			} else {
				userDTO.setStatus(EnovationConstants.statusFail);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultFalse);
				userDTO.setReason(EnovationConstants.ACCOUNT_ALREADY_VERIFIED);
				return userDTO;
			}
		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			userDTO.setReason(EnovationConstants.ACCOUNT_ALREADY_VERIFIED);
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN SAVE USER  " + e.getMessage());
			return userDTO;
		}
	}

	@Override
	public UserResponse forgotPassword(EmployeeDetails request) {
		LOGGER.info("# INSIDE IN forgotPassword");
		UserResponse userDTO = new UserResponse();
		try {
			String forgotPassword = userdaoinfc.forgotPassword(request);
			if (forgotPassword != null) {
				userDTO.setStatus(EnovationConstants.statusSuccess);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultTrue);
				userDTO.setReason("password updated successfully");
				userDTO.setPortalLink(forgotPassword);
				return userDTO;
			} else {
				userDTO.setStatus(EnovationConstants.statusFail);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultFalse);
				userDTO.setReason("Email ID  Does Not Exists");
				return userDTO;
			}
		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			userDTO.setReason("EXCEPTION OCCURED IN SAVE USER API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN SAVE USER  " + e.getMessage());
			return userDTO;
		}
	}

	@Override
	public UserResponse saveDemoRequest(DemoRequest request) {
		LOGGER.info("# INSIDE IN saveDemoRequest");
		UserResponse userDTO = new UserResponse();
		int count = 1;
		try {
			boolean isDemoRequestEmailExists = userdaoinfc.isDemoRequestEmailExists(request.getEmail());
			System.out.println("isDemoRequestEmailExists val is (t/f) :" + isDemoRequestEmailExists);
			// if(!isDemoRequestEmailExists){
			boolean requst = userdaoinfc.saveDemoRequest(request);
			if (requst) {
				List<DemoRequest> list = userdaoinfc.demoRequestList();
				if (list != null && list.size() > 0) {
					count = list.size();
					count++;
				}
				EmailTemplateMaster messageContent = enoConfig.getMessageContent("DemoEscalation");
				String mailContent = "";
				if (messageContent.getBody() != null && messageContent.getBody().contains("{name}")) {

					mailContent = messageContent.getBody()
							.replaceAll(Pattern.quote("{name}"), request.getFirstName() + " " + request.getLastName())
							.replaceAll(Pattern.quote("{contactNo}"), request.getContact())
							.replaceAll(Pattern.quote("{country}"), request.getCountry())
							.replaceAll(Pattern.quote("{email}"), request.getEmail())
							.replaceAll(Pattern.quote("{details}"), "New Demo Request")
							.replaceAll(Pattern.quote("{domain}"), request.getCompany())
							.replaceAll(Pattern.quote("{regDate}"),
									String.valueOf(new Timestamp(System.currentTimeMillis())));
				}
				if (request.getEmail() != null) {
					taskExecutor.execute(new MailUtil(EnovationConstants.RAJ_DUBAL,
							count + EnovationConstants.DEMO_REQUEST, mailContent, communication));
					taskExecutor.execute(new MailUtil(EnovationConstants.SACHIN_SHINDE,
							count + EnovationConstants.DEMO_REQUEST, mailContent, communication));
					taskExecutor.execute(new MailUtil(EnovationConstants.VINAY_B,
							count + EnovationConstants.DEMO_REQUEST, mailContent, communication));
				}
				userDTO.setStatus(EnovationConstants.statusSuccess);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultTrue);
				userDTO.setReason("save request successfully");
				return userDTO;
			} else {
				userDTO.setStatus(EnovationConstants.statusFail);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultFalse);
				userDTO.setReason("email-id does not exists");
				return userDTO;
			}
			/*
			 * }else { userDTO.setStatus(EnovationConstants.statusFail);
			 * userDTO.setStatusCode(EnovationConstants.Code200);
			 * userDTO.setResult(EnovationConstants.ResultFalse);
			 * userDTO.setReason(EnovationConstants.ALREADY_EXITS); return userDTO; }
			 */
		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			userDTO.setReason("EXCEPTION OCCURED IN SAVE DEMO REQUEST API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN SAVE DEMO REQUEST  " + e.getMessage());
			return userDTO;
		}
	}

	@Override
	public UserResponse demoRequestList() {
		LOGGER.info("# INSIDE IN saveDemoRequest");
		UserResponse userDTO = new UserResponse();
		try {
			List<DemoRequest> list = userdaoinfc.demoRequestList();
			if (list != null && list.size() > 0) {
				userDTO.setStatus(EnovationConstants.statusSuccess);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultTrue);
				userDTO.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				userDTO.setDemoRequestList(list);
				return userDTO;
			} else {
				userDTO.setStatus(EnovationConstants.statusSuccess);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultFalse);
				userDTO.setReason("email-id does not exists");
				return userDTO;
			}
		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			userDTO.setReason("EXCEPTION OCCURED IN SAVE DEMO REQUEST API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN SAVE DEMO REQUEST  " + e.getMessage());
			return userDTO;
		}
	}

	@Override
	public UserResponse saveRegistration(Registration request) {
		LOGGER.info("# INSIDE IN SAVE USER ");
		UserResponse userDTO = new UserResponse();
		EmployeeDetails emp = null;
		try {
			// set orgdamin and alies
			splitDomainName(request);// split domain and alies
			// Need to Check Email Already Exist or Not
			boolean isRegistrationemailExists = userdaoinfc.isRegistrationemailExists(request.getEmail());
			// Need to Check Domain And EmailId is Existing or Not
			List<EmployeeDetails> empDetails = userdaoinfc.getEmpListByOrg(request.getOrgDomain(), request.getEmail());
			if (empDetails != null && empDetails.size() > 0) {
				emp = empDetails.get(0);
				if (emp.getIsEmailVerified() == EnovationConstants.ENABLE_STATUS) {
					userDTO.setStatus(EnovationConstants.statusSuccess);
					userDTO.setStatusCode(EnovationConstants.Code200);
					userDTO.setResult(EnovationConstants.ResultFalse);
					userDTO.setResendEmail(EnovationConstants.resultFalse);
					userDTO.setReason("Your Email Id already Register with US, Please login to Portal");
					return userDTO;
				} else {
					userDTO.setStatus(EnovationConstants.statusSuccess);
					userDTO.setStatusCode(EnovationConstants.Code200);
					userDTO.setResult(EnovationConstants.resultTrue);
					userDTO.setResendEmail(EnovationConstants.resultTrue);
					userDTO.setReason(
							"Your Email Id already Register with US, But not verified, Do want to resend verification mail?");
					return userDTO;
				}
			} else {
				if (!isRegistrationemailExists) {
					boolean saveUser = userdaoinfc.saveRegistration(request);
					if (saveUser) {
						userDTO.setStatus(EnovationConstants.statusSuccess);
						userDTO.setStatusCode(EnovationConstants.Code200);
						userDTO.setResult(EnovationConstants.ResultTrue);
						userDTO.setReason("SAVE USER");
						return userDTO;
					} else {
						userDTO.setStatus(EnovationConstants.statusFail);
						userDTO.setStatusCode(EnovationConstants.Code200);
						userDTO.setResult(EnovationConstants.ResultFalse);
						userDTO.setReason("NOT SAVE USER");
						return userDTO;
					}
				} else {
					userDTO.setStatus(EnovationConstants.statusFail);
					userDTO.setStatusCode(EnovationConstants.Code200);
					userDTO.setResult(EnovationConstants.ResultFalse);
					userDTO.setReason("email already exists");
					return userDTO;
				}
			}
		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			userDTO.setReason("EXCEPTION OCCURED IN  SAVE Registration  API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN SAVE Registration  " + e.getMessage());
			return userDTO;
		}
	}

	@Override
	public UserResponse resendEmail(Registration request) {
		LOGGER.info("# INSIDE IN resendEmail USER ");
		UserResponse userDTO = new UserResponse();
		List<Registration> registerData = userdaoinfc.isRegisterEmail(request.getEmail());
		List<EmployeeDetails> empDetailsList = null;

		// Check if Email Id Present in Reg and is verified or Not
		if (registerData != null && registerData.size() > 0) {
			if (registerData.get(0).getIsEmailVerified() == EnovationConstants.DEACTIVESTATUS) {
				System.out.println("Data Of registerData : " + registerData.get(0).getIsEmailVerified());
				// RESEND EMAIL For Verification
				userdaoinfc.reSendMail(registerData.get(0));
				userDTO.setStatus(EnovationConstants.statusSuccess);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultTrue);
				userDTO.setReason("Please check Mail For Email Verification");
			} else {
				userDTO.setStatus(EnovationConstants.statusSuccess);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultTrue);
				userDTO.setReason("Please login to portal, Email is already verified or reset password");
			}
		}
		if (empDetailsList == null) {
			empDetailsList = userdaoinfc.isEmployeeEmailExists(request.getEmail());
			if (empDetailsList != null && empDetailsList.size() > 0) {
				System.out.println("Data Of Emp : " + empDetailsList.get(0).getIsEmailVerified());
				if (empDetailsList.get(0).getIsEmailVerified() == EnovationConstants.ENABLE_STATUS) {
					userDTO.setStatus(EnovationConstants.statusSuccess);
					userDTO.setStatusCode(EnovationConstants.Code200);
					userDTO.setResult(EnovationConstants.ResultTrue);
					userDTO.setReason("Please login to portal, Email is already verified or reset password");
				} else {
					// RESEND EMAIL For Verification
					userdaoinfc.reSendMailToEmployee(empDetailsList.get(0));
					userDTO.setStatus(EnovationConstants.statusSuccess);
					userDTO.setStatusCode(EnovationConstants.Code200);
					userDTO.setResult(EnovationConstants.ResultTrue);
					userDTO.setReason("Please check Mail For Email Verification");
				}
			} else {
				userDTO.setStatus(EnovationConstants.statusFail);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.resultFalse);
				userDTO.setReason("Email Id Not Exist");
			}
		}
		return userDTO;
	}

	@Override
	public UserResponse appVersion() {
		UserResponse userDTO = new UserResponse();
		userDTO.setStatus(EnovationConstants.statusSuccess);
		userDTO.setAppVersion(appversion);
		userDTO.setStatusCode(EnovationConstants.Code200);
		userDTO.setResult(EnovationConstants.ResultTrue);
		return userDTO;
	}

	@Override
	public UserResponse logout(EmployeeDetails emp/* ,HttpServletRequest request */) {
		LOGGER.info("# INSIDE IN LOGOUT ");
		UserResponse userDTO = new UserResponse();
		try {
			boolean logout = userdaoinfc.logout(emp);
			if (logout) {
				userDTO.setStatus(EnovationConstants.statusSuccess);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultTrue);
				userDTO.setReason("logged out");
				return userDTO;
			} else {
				userDTO.setStatus(EnovationConstants.statusFail);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultFalse);
				userDTO.setReason("logging out failed");
				return userDTO;
			}

		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			userDTO.setReason("EXCEPTION OCCURED IN LOGOUT API : " + e.getMessage());
			LOGGER.error("# INSIDE EXCEPTION OCCERED IN LOGOUT API  " + e.getMessage());
			return userDTO;
		}
	}

	@Override
	public UserResponse getMenuListByBranchIdOrOrgId(Integer branchId, Integer orgId) {
		UserResponse userList = null;
		try {
			List<ModuleSubscription> menuList = userdaoinfc.getMenuListByBranchIdOrOrgId(branchId, orgId);
			if (menuList != null && menuList.size() > 0) {
				userList = new UserResponse();
				userList.setStatus(EnovationConstants.statusSuccess);
				userList.setStatusCode(EnovationConstants.Code200);
				userList.setResult(EnovationConstants.ResultTrue);
				userList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				userList.setModuleList(menuList);
				return userList;
			} else {
				userList = new UserResponse();
				userList.setStatus(EnovationConstants.statusSuccess);
				userList.setStatusCode(EnovationConstants.Code200);
				userList.setResult(EnovationConstants.ResultFalse);
				userList.setReason(EnovationConstants.RecordsDoesNotExist);
				return userList;
			}

		} catch (Exception e) {
			userList = new UserResponse();
			userList.setStatus(EnovationConstants.statusFail);
			userList.setStatusCode(EnovationConstants.Code500);
			userList.setResult(EnovationConstants.ResultFalse);
			userList.setReason("EXCEPTION OCCURED IN GET MENU LIST BY SUBSCRIPTION  API :" + e.getMessage());
			return userList;
		}
	}

	@Override
	public UserResponse saveBranchAccessSetup(BranchAccessSetup request) {
		LOGGER.info("# INSIDE IN SAVE BRANCH ACCESS SETUP ");
		UserResponse userDTO = new UserResponse();
		try {
			boolean isSave = userdaoinfc.saveBranchAccessSetup(request);
			if (isSave) {
				userDTO.setStatus(EnovationConstants.statusSuccess);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultTrue);
				return userDTO;
			} else {
				userDTO.setStatus(EnovationConstants.statusFail);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultFalse);
				return userDTO;
			}

		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			LOGGER.error("# INSIDE EXCEPTION OCCERED SAVE BRANCH ACCESS SETUP API  " + e.getMessage());
			return userDTO;
		}
	}

	@Override
	public UserResponse getBranchAccessSetup(int orgId) {
		UserResponse userList = null;
		try {
			List<BranchAccessSetup> menuList = userdaoinfc.getBranchAccessSetup(orgId);
			if (menuList != null && menuList.size() > 0) {
				userList = new UserResponse();
				userList.setStatus(EnovationConstants.statusSuccess);
				userList.setStatusCode(EnovationConstants.Code200);
				userList.setResult(EnovationConstants.ResultTrue);
				userList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				userList.setBranchAccessList(menuList);
				return userList;
			} else {
				userList = new UserResponse();
				userList.setStatus(EnovationConstants.statusSuccess);
				userList.setStatusCode(EnovationConstants.Code200);
				userList.setResult(EnovationConstants.ResultFalse);
				userList.setReason(EnovationConstants.RecordsDoesNotExist);
				return userList;
			}

		} catch (Exception e) {
			userList = new UserResponse();
			userList.setStatus(EnovationConstants.statusFail);
			userList.setStatusCode(EnovationConstants.Code500);
			userList.setResult(EnovationConstants.ResultFalse);
			userList.setReason("EXCEPTION OCCURED IN GET MENU LIST BY SUBSCRIPTION  API :" + e.getMessage());
			return userList;
		}
	}

	@Override
	public UserResponse deleteBranchAccessSetup(int id) {
		LOGGER.info("# INSIDE IN DELETE BRANCH ACCESS SETUP ");
		UserResponse userDTO = new UserResponse();
		try {
			boolean isDelete = userdaoinfc.deleteBranchAccessSetup(id);
			if (isDelete) {
				userDTO.setStatus(EnovationConstants.statusSuccess);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultTrue);
				return userDTO;
			} else {
				userDTO.setStatus(EnovationConstants.statusFail);
				userDTO.setStatusCode(EnovationConstants.Code200);
				userDTO.setResult(EnovationConstants.ResultFalse);
				return userDTO;
			}

		} catch (Exception e) {
			userDTO.setStatus(EnovationConstants.statusFail);
			userDTO.setStatusCode(EnovationConstants.Code500);
			userDTO.setResult(EnovationConstants.ResultFalse);
			LOGGER.error("# INSIDE EXCEPTION OCCERED DELETE BRANCH ACCESS SETUP API  " + e.getMessage());
			return userDTO;
		}
	}

	@Override
	public UserResponse getBranchAccessSetupByEmpId(int orgId, int empId) {
		UserResponse userList = null;
		try {
			List<BranchAccessSetup> menuList = userdaoinfc.getBranchAccessSetupByEmpId(orgId, empId);
			if (menuList != null && menuList.size() > 0) {
				userList = new UserResponse();
				userList.setStatus(EnovationConstants.statusSuccess);
				userList.setStatusCode(EnovationConstants.Code200);
				userList.setResult(EnovationConstants.ResultTrue);
				userList.setReason(EnovationConstants.RECORDSFETCHSUCCESS);
				userList.setBranchAccessList(menuList);
				return userList;
			} else {
				userList = new UserResponse();
				userList.setStatus(EnovationConstants.statusSuccess);
				userList.setStatusCode(EnovationConstants.Code200);
				userList.setResult(EnovationConstants.ResultFalse);
				userList.setReason(EnovationConstants.RecordsDoesNotExist);
				return userList;
			}

		} catch (Exception e) {
			userList = new UserResponse();
			userList.setStatus(EnovationConstants.statusFail);
			userList.setStatusCode(EnovationConstants.Code500);
			userList.setResult(EnovationConstants.ResultFalse);
			userList.setReason("EXCEPTION OCCURED IN GET MENU LIST BY SUBSCRIPTION  API :" + e.getMessage());
			return userList;
		}
	}

	/**
	 * @author rakesh 12 june 2020
	 */
	@Override
	public UserResponse getUserAccessList(UserMenuAccess request) {
		LOGGER.info(" IN UserService | getUserAccessList");
		UserResponse response = new UserResponse();
		try {
			List<EmployeeDetailsBO> empList = userdaoinfc.getUserAccessList(request);
			if (empList != null && !empList.isEmpty()) {
				response.setEmpList(empList);
				BuildResponse.success(response);
			} else {
				BuildResponse.fail(response);
			}
		} catch (Exception e) {
			LOGGER.info(" IN getUserAccessList |  Error occured " + e.getMessage());
			Gson gson = new Gson();
			String json = gson.toJson(request);
			commonUtils.sendAPIFailedExceptionMail("Exception Occured At - " + this.getClass().getSimpleName(),
					" <p><b> Method Name - </b>" + new Throwable().getStackTrace()[0].getMethodName() + " <br></p> "
							+ "<p><b> Request Body - </b>" + json + "<p><b> Exception - </b>"
							+ ExceptionUtils.getStackTrace(e));
			BuildResponse.internalServerError(response);
		}
		return response;
	}

	/**
	 * @author rakesh 12 june 2020
	 */
	@Override
	public UserResponse addOrUpdateUserAccess(MenuDTO request) {
		LOGGER.info(" IN UserService | addOrUpdateUserAccess");
		UserResponse response = new UserResponse();
		try {
			boolean flag = userdaoinfc.addOrUpdateUserAccess(request);
			if (flag) {
				BuildResponse.success(response);
			} else {
				BuildResponse.fail(response);
			}
		} catch (Exception e) {
			LOGGER.info(" IN addOrUpdateUserAccess |  Error occured " + e.getMessage());
			Gson gson = new Gson();
			String json = gson.toJson(request);
			commonUtils.sendAPIFailedExceptionMail("Exception Occured At - " + this.getClass().getSimpleName(),
					" <p><b> Method Name - </b>" + new Throwable().getStackTrace()[0].getMethodName() + " <br></p> "
							+ "<p><b> Request Body - </b>" + json + "<p><b> Exception - </b>"
							+ ExceptionUtils.getStackTrace(e));
			BuildResponse.internalServerError(response);
		}
		return response;
	}

}
