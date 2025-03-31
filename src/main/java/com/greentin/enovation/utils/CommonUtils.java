package com.greentin.enovation.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import javax.imageio.ImageIO;
import javax.persistence.Tuple;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.oned.UPCAWriter;
import com.greentin.enovation.communication.ICommunication;

@Component
public class CommonUtils extends BaseRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

	@Autowired
	@Qualifier("taskExecutor")
	private TaskExecutor taskExecutor;

	@Autowired
	private ICommunication communication;

	public static char[] GenerateRandomPassword(int len) {
		// Using numeric values
		String numbers = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		// Using random method
		Random rndm_method = new Random();
		char[] otp = new char[len];
		for (int i = 0; i < len; i++) {
			otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
		}
		return otp;
	}

	public static char[] generateToken(int len) {
		// Using numeric values
		String numbers = "EFGHIJKLMNOPQRSTUVWXYZabcde0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzEFGHIJKLMNOPQRSTUVWXYZabcde";
		// Using random method
		Random rndm_method = new Random();
		char[] otp = new char[len];
		for (int i = 0; i < len; i++) {
			otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
		}
		return otp;
	}

	public static Timestamp currentDate() {
		Timestamp createdDate = new Timestamp(System.currentTimeMillis());
		return createdDate;
	}

	public static Timestamp deactivateDate(int days) {
		Timestamp createdDate = new Timestamp(System.currentTimeMillis());
		Calendar c = Calendar.getInstance();
		c.setTime(createdDate);
		c.add(Calendar.DATE, days);
		Date currentDatePlusOne = c.getTime();
		Timestamp create = new Timestamp(currentDatePlusOne.getTime());
		return create;
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllFCMKeys(List<Integer> empIds) {
		Session session = getCurrentSession();// em.unwrap(SessionFactory.class).openSession();
		List<String> fcmKeyList = null;
		try {
			fcmKeyList = session.createQuery("Select fcmKey from EmployeeDetails where empId IN (:ids)")
					.setParameter("ids", empIds).getResultList();
		} catch (Exception e) {
			LOGGER.info("Exception Occured in CommonUtils.getAllFCMKeys Method :  " + e);
		}
		return fcmKeyList;
	}

	public static int getDifferenceDays(Date d1, Date d2) {
		int daysdiff = 0;
		long diff = d2.getTime() - d1.getTime();
		long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
		daysdiff = (int) diffDays;
		return daysdiff;
	}

	public static int getCurrentDate() {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		Date date = ts;
		System.out.println(date);
		DateFormat formateYd = new SimpleDateFormat("dd");
		return Integer.valueOf(formateYd.format(date));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <TYPE> Comparator<TYPE> sort(Function<TYPE, ? extends Comparable> getterFunction,
			boolean descending) {
		if (descending) {
			return (o1, o2) -> getterFunction.apply(o2).compareTo(getterFunction.apply(o1));
		}
		return (o1, o2) -> getterFunction.apply(o1).compareTo(getterFunction.apply(o2));
	}

	public static Timestamp convertStringToTimestamp(String str_date) {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
			Date date = (Date) formatter.parse(str_date);
			java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());

			return timeStampDate;
		} catch (ParseException e) {
			System.out.println("Exception :" + e);
			return null;
		}
	}

	public static java.sql.Date convertStringToSqlDate(String str_date) {
		try {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = sdf1.parse(str_date);
			java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());
			return sqlStartDate;
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("EXCEPTION CONVERTSTRINGTOSQLDATE :" + e);
			return null;
		}
	}

	public static Date convertStringToUtilDate(String str_date) {
		try {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = sdf1.parse(str_date);
			return date;
		} catch (ParseException e) {
			System.out.println("EXCEPTION CONVERTSTRINGTOUTILDATE :" + e);
			return null;
		}
	}

	public static String getCurrentMonthFirstDate() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		return df.format(c.getTime());
	}

	public static Date convertTimestampToDate(Timestamp timestamp) {
		Instant ins = timestamp.toLocalDateTime().atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(ins);
	}

	public static String readingBarcode(String url) {
		InputStream barCodeInputStream;
		String decodeValue = null;
		try {
			barCodeInputStream = new FileInputStream(url);
			BufferedImage barCodeBufferedImage = null;
			try {
				barCodeBufferedImage = ImageIO.read(barCodeInputStream);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			LuminanceSource source = new BufferedImageLuminanceSource(barCodeBufferedImage);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			Reader reader = new MultiFormatReader();
			Result result = null;
			try {
				result = reader.decode(bitmap);
			} catch (NotFoundException | ChecksumException | FormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			decodeValue = result.getText();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Barcode text is " + decodeValue);
		return decodeValue;
	}

	public static String generatingBarcode(String url) {

		String text = "98376373783"; // this is the text that we want to encode

		int width = 400;
		int height = 300; // change the height and width as per your requirement

		// (ImageIO.getWriterFormatNames() returns a list of supported formats)
		String imageFormat = "png"; // could be "gif", "tiff", "jpeg" , "png"

		BitMatrix bitMatrix = null;
		try {
			// bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width,
			// height);
			bitMatrix = new UPCAWriter().encode(text, BarcodeFormat.UPC_A, width, height);
		} catch (WriterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			// MatrixToImageWriter.writeToStream(bitMatrix, imageFormat, new
			// FileOutputStream(new File("qrcode_97802017507991.png")));
			MatrixToImageWriter.writeToStream(bitMatrix, imageFormat,
					new FileOutputStream(new File("qrcode_97802017507991.png")));
			System.out.println("Success!");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return imageFormat;
	}

	public void sendFailedExceptionMail(String subject, String exceptionContent) {
		taskExecutor.execute(new MailUtil("anant@greentinsolutions.com", subject, exceptionContent, communication));
	}

	public void sendAPIFailedExceptionMail(String subject, String exceptionContent) {
		Arrays.asList(EnovationConstants.SUPPORT_TEAM_EMAILS).stream().forEach(x -> {
			taskExecutor.execute(new MailUtil(x, subject, exceptionContent, communication));
		});

	}

	public static String leadingWithZeros(int variabl, int length) {
		return StringUtils.leftPad(String.valueOf(variabl), length, EnovationConstants.ZERO_STRING);
	}

	public static String objectToString(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}

	public static int objectToInt(Object obj) {
		if (obj != null) {
			return Integer.valueOf(obj.toString());
		} else {
			return 0;
		}
	}

	public static long objectToLong(Object obj) {
		if (obj != null) {
			return Long.valueOf(obj.toString());
		} else {
			return 0;
		}
	}

	public static double objectToDouble(Object obj) {
		if (obj != null) {
			return Double.valueOf(obj.toString());
		} else {
			return 0.00;
		}
	}

	public static java.util.Date convertStringToDate(String str_date) {
		try {
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Date date = sdf1.parse(str_date);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("EXCEPTION CONVERTSTRINGTOSQLDATE :" + e);
			return null;
		}
	}

	/**
	 * @author rakesh 23 feb 2021
	 * @param format
	 * @param value
	 * @return
	 */
	public static boolean isDateValidFormat(String format, String value) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(value);
			if (!value.equals(sdf.format(date))) {
				date = null;
			}
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return date != null;
	}

	/**
	 * @author Vinay B. Jun 10, 2021 12:04:10 PM
	 * @param value void
	 */
	public static double convertValueWithTwoDecimalPlaces(double value) {
		DecimalFormat df2 = new DecimalFormat("#.##");
		df2.setRoundingMode(RoundingMode.UP);
		String convertedValue = df2.format(value);
		return Double.parseDouble(String.valueOf(convertedValue));

	}

	/**
	 * @author Aditi V. Oct 7, 2022 11:56:06 AM
	 * @param e
	 * @return boolean
	 */
	public static boolean isConstraintViolationException(Exception e) {
		Throwable t = e.getCause();
		while ((t != null) && !(t instanceof ConstraintViolationException)) {
			t = t.getCause();
		}
		return t instanceof ConstraintViolationException;
	}

	/**
	 * @author Anant K. August 08, 2023 11:56:06 AM
	 * @param tupleList
	 * @return list
	 */
	public static List<HashMap<String, Object>> parseTupleList(List<Tuple> tupleList) {
		LOGGER.info("# CommonUtils | parseTupleList");
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		if (!CollectionUtils.isEmpty(tupleList)) {
			tupleList.stream().forEach(x -> {
				HashMap<String, Object> map = new HashMap<String, Object>();
				x.getElements().forEach(te -> {
					map.put(te.getAlias(), x.get(te.getAlias()));
				});
				list.add(map);
			});
		}
		return list;
	}

	public static HashMap<String, Object> parseSingleTupleRecord(Tuple tuple) {
		LOGGER.info("# CommonUtils | parseSingleTupleRecord");
		HashMap<String, Object> map = new HashMap<String, Object>();
		tuple.getElements().forEach(te -> {
			map.put(te.getAlias(), tuple.get(te.getAlias()));
		});
		return map;
	}

	public static boolean validateCellData(DataFormatter formatter, Row row, int k) {
		boolean flag = false;
		if (String.valueOf(formatter.formatCellValue(row.getCell(k))) != null
				&& !formatter.formatCellValue(row.getCell(k)).equals("")
				&& formatter.formatCellValue(row.getCell(k)).trim().length() != 0) {
			flag = true;
		}
		return flag;
	}

	public static boolean excelRowChecker(DataFormatter formatter, Row row) {
		boolean flag = false;
		for (int k = row.getFirstCellNum(); (k >= 0 && (k < row.getLastCellNum() + 1)); k++) {
			boolean cellFlag = validateCellData(formatter, row, k);
			if (cellFlag) {
				String cellData = formatter.formatCellValue(row.getCell(k));
				if (cellData != null && !cellData.isEmpty()) {
					flag = true;
					break;
				}
			}
		}
		return flag;

	}

	public static boolean checkIfHeaderIsNotNull(Cell cellHeader, DataFormatter formatter) {
		if (cellHeader != null && !formatter.formatCellValue(cellHeader).equals("")
				&& formatter.formatCellValue(cellHeader).trim().length() != 0)
			return true;
		return false;
	}

	public static boolean isStringNotNull(String str) {
		return str != null && str.length() > 0;
	}
	
	// convertFileToBase64Str
		// To convert file to base64 string
		public static String convertFileToBase64Str(String filePath) throws IOException {
			LOGGER.info("In CommonUtils | convertFileToBase64Str");

			byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
			return Base64.getEncoder().encodeToString(fileContent);
		}
}
