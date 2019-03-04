package com.hbase.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Date;

/**
 * @author Cobub
 */
public class Constants {

  public static final String ERROR_BY_CAUGHT = "An error was caught";
  public static final String EXCEPTION_WAS_CAUGHT = "Exception was caught";
  public static final String AN_ERROR_WAS_CAUGHT = "An error was caught";
  public static final String YYYY_MM_DD_HH_59_59 = "yyyy-MM-dd HH:59:59";
  public static final String YYYY_MM_DD_HH_00_00 = "yyyy-MM-dd HH:00:00";
  public static final String YYYY_M_MDD_HH = "yyyyMMddHH";
  public static final String  YYYY_MM_DD = "yyyy-MM-dd";
  public static final String PLATFORM = "platform";

  private Constants() {}

  public static final String UNKNOWN = "Unknown";
  public static final String ENCODING = "UTF-8";
  public static final String SPLIT_CHARACTERS_1 = "\001";
  public static final String SPLIT_CHARACTERS_2 = "\002";
  public static final String ACT_SPLIT_CHARACTER = "||";
  public static final String PATH_SPLIT = "/";
  public static final int SCAN_CACHING = 20000;
  public static final int ACCESS_LEVEL_LIMIT = 6;
  private static String INITIAL_DATE ="2014-01-01";
  protected static final long ONE_DAY_MILLIS = 24 * 3600 * 1000L;
  protected static final long ONE_HOUR_MILLIS = 3600 * 1000L;
  public static final int JOB_SUCC = 0;
  public static final int JOB_FAIL = 1;
  public static final String ACT_EXIT = "Exit";
  public static final int TOP_N_APP = 100;
  private static boolean JOB_FORCE_RUN = false;
  private static Logger logger = LoggerFactory.getLogger(Constants.class);

  public static void setJobForceRun(boolean jobForceRun) {
    JOB_FORCE_RUN = jobForceRun;
  }

  public static boolean getJobForceRun() {
    return JOB_FORCE_RUN ;
  }


}
