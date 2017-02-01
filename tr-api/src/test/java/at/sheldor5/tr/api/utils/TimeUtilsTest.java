package at.sheldor5.tr.api.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import at.sheldor5.tr.api.utils.TimeUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Michael Palata <a href="https://github.com/Sheldor5">@github.com/Sheldor5</a> on 21.01.2017.
 */
public class TimeUtilsTest {

  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
  private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

  @Test
  public void test_millis_to_readable() {
    String actual;

    actual = TimeUtils.getHumanReadableTime(1L * TimeUtils.HOUR_IN_MILLIS);
    Assert.assertEquals("Formatting milliseconds to human readable clock format failed", "01:00:00", actual);

    actual = TimeUtils.getHumanReadableTime(
            23L * TimeUtils.HOUR_IN_MILLIS
                    + 59L * TimeUtils.MINUTE_IN_MILLIS
                    + 59L * TimeUtils.SECOND_IN_MILLIS);
    Assert.assertEquals("Formatting milliseconds to human readable clock format failed", "23:59:59", actual);
  }

  @Test
  public void test_millis_to_summary() {
    String actual;

    actual = TimeUtils.getHumanReadableSummary(1L * 3600L);
    Assert.assertEquals("Formatting milliseconds to human readable summary format failed", "1:00:00", actual);

    actual = TimeUtils.getHumanReadableSummary(
            23L * 3600L
                    + 59L * 60L
                    + 59L);
    Assert.assertEquals("Formatting milliseconds to human readable summary format failed", "23:59:59", actual);

    actual = TimeUtils.getHumanReadableSummary(24L * 3600L);
    Assert.assertEquals("Formatting milliseconds to human readable summary format failed", "24:00:00", actual);

    actual = TimeUtils.getHumanReadableSummary(100L * 3600L);
    Assert.assertEquals("Formatting milliseconds to human readable summary format failed", "100:00:00", actual);
  }

  @Test
  public void test_string_to_time() {
    Assert.assertEquals("Conversion from time to millis failes", 1L * TimeUtils.HOUR_IN_MILLIS, TimeUtils.getMillis("1:00:00"));
    Assert.assertEquals("Conversion from time to millis failes",
            12L * TimeUtils.HOUR_IN_MILLIS
                    + 34L * TimeUtils.MINUTE_IN_MILLIS
                    + 56L * TimeUtils.SECOND_IN_MILLIS,
            TimeUtils.getMillis("12:34:56"));
    Assert.assertEquals("Conversion from time to millis failes", 100L * TimeUtils.HOUR_IN_MILLIS, TimeUtils.getMillis("100:00:00"));
  }

  @Test
  public void test_get_last_day_of_month() {
    Assert.assertEquals(31, TimeUtils.getLastDayOfMonth(2017, 1));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2017, 2));
    Assert.assertEquals(31, TimeUtils.getLastDayOfMonth(2017, 3));
    Assert.assertEquals(30, TimeUtils.getLastDayOfMonth(2017, 4));
    Assert.assertEquals(31, TimeUtils.getLastDayOfMonth(2017, 5));
    Assert.assertEquals(30, TimeUtils.getLastDayOfMonth(2017, 6));
    Assert.assertEquals(31, TimeUtils.getLastDayOfMonth(2017, 7));
    Assert.assertEquals(31, TimeUtils.getLastDayOfMonth(2017, 8));
    Assert.assertEquals(30, TimeUtils.getLastDayOfMonth(2017, 9));
    Assert.assertEquals(31, TimeUtils.getLastDayOfMonth(2017, 10));
    Assert.assertEquals(30, TimeUtils.getLastDayOfMonth(2017, 11));
    Assert.assertEquals(31, TimeUtils.getLastDayOfMonth(2017, 12));
  }

  @Test
  public void test_get_last_day_of_month_leap_year() {
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1904, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1908, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1912, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1916, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1920, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1924, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1928, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1932, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1932, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1936, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1940, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1944, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1948, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1952, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1956, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1960, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1964, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1968, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1972, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1976, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1980, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1984, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1988, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1992, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(1996, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2000, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2004, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2008, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2012, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2016, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2020, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2024, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2028, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2032, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2036, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2040, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2044, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2048, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2052, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2056, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2060, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2064, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2068, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2072, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2076, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2080, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2084, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2088, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2092, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2096, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2104, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2108, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2112, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2116, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2120, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2124, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2128, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2132, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2136, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2140, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2144, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2148, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2152, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2156, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2160, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2164, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2168, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2172, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2176, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2180, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2184, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2188, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2192, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2196, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2204, 2));
    Assert.assertEquals(29, TimeUtils.getLastDayOfMonth(2208, 2));
  }

  @Test
  public void test_get_last_day_of_month_no_leap_year() {
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1900, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1901, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1902, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1903, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1905, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1906, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1907, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1909, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1910, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1911, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1913, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1914, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1915, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1917, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1918, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1919, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1921, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1922, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1923, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1925, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1926, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1927, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1929, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1930, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1931, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1933, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1934, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1935, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1937, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1938, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1939, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1941, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1942, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1943, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1945, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1946, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1947, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1949, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1950, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1951, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1953, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1954, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1955, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1957, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1958, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1959, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1961, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1962, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1963, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1965, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1966, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1967, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1969, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1970, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1971, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1973, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1974, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1975, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1977, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1978, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1979, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1981, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1982, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1983, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1985, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1986, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1987, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1989, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1990, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1991, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1993, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1994, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1995, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1997, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1998, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(1999, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2001, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2002, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2003, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2005, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2006, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2007, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2009, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2010, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2011, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2013, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2014, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2015, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2017, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2018, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2019, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2021, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2022, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2023, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2025, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2026, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2027, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2029, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2030, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2031, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2033, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2034, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2035, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2037, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2038, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2039, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2041, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2042, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2043, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2045, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2046, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2047, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2049, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2050, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2051, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2053, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2054, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2055, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2057, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2058, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2059, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2061, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2062, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2063, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2065, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2066, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2067, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2069, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2070, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2071, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2073, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2074, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2075, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2077, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2078, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2079, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2081, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2082, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2083, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2085, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2086, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2087, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2089, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2090, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2091, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2093, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2094, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2095, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2097, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2098, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2099, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2100, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2101, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2102, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2103, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2105, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2106, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2107, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2109, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2110, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2111, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2113, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2114, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2115, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2117, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2118, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2119, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2121, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2122, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2123, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2125, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2126, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2127, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2129, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2130, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2131, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2133, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2134, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2135, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2137, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2138, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2139, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2141, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2142, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2143, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2145, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2146, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2147, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2149, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2150, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2151, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2153, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2154, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2155, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2157, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2158, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2159, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2161, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2162, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2163, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2165, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2166, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2167, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2169, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2170, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2171, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2173, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2174, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2175, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2177, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2178, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2179, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2181, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2182, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2183, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2185, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2186, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2187, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2189, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2190, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2191, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2193, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2194, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2195, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2197, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2198, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2199, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2200, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2201, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2202, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2203, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2205, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2206, 2));
    Assert.assertEquals(28, TimeUtils.getLastDayOfMonth(2207, 2));
  }
}