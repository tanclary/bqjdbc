/**
 * Copyright (c) 2015, STARSCHEMA LTD. All rights reserved.
 *
 * <p>Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * <p>1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. 2. Redistributions in binary form must reproduce the
 * above copyright notice, this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * <p>THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.starschema.clouddb.jdbc;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Junit test tests functions in BQResultset
 *
 * @author Horváth Attila
 * @author Gunics Balazs
 */
public class BQResultSetFunctionTest {

  private static java.sql.Connection con = null;
  private static java.sql.ResultSet Result = null;

  Logger logger = LoggerFactory.getLogger(BQResultSetFunctionTest.class);

  @Test
  public void ChainedCursorFunctionTest() {
    this.logger.info("ChainedFunctionTest");
    try {
      BQResultSetFunctionTest.Result.beforeFirst();
      Assert.assertTrue(BQResultSetFunctionTest.Result.next());
      Assert.assertEquals("you", BQResultSetFunctionTest.Result.getString(1));

    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail("SQLException" + e.toString());
    }
    try {
      Assert.assertTrue(BQResultSetFunctionTest.Result.absolute(10));
      Assert.assertEquals("whom", BQResultSetFunctionTest.Result.getString(1));
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail("SQLException" + e.toString());
    }

    try {
      Assert.assertFalse(BQResultSetFunctionTest.Result.next());
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail("SQLException" + e.toString());
    }

    try {
      Assert.assertEquals("", BQResultSetFunctionTest.Result.getString(1));
    } catch (SQLException e) {
      boolean ct = e.toString().contains("Cursor is not in a valid Position");
      if (ct == true) {
        Assert.assertTrue(ct);
      } else {
        this.logger.error("SQLexception" + e.toString());
        Assert.fail("SQLException" + e.toString());
      }
    }

    try {
      Assert.assertTrue(BQResultSetFunctionTest.Result.first());
      Assert.assertEquals("you", BQResultSetFunctionTest.Result.getString(1));
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail("SQLException" + e.toString());
    }

    try {
      Assert.assertTrue(BQResultSetFunctionTest.Result.isFirst());
      Assert.assertFalse(BQResultSetFunctionTest.Result.previous());
      BQResultSetFunctionTest.Result.afterLast();
      Assert.assertTrue(BQResultSetFunctionTest.Result.isAfterLast());
      Assert.assertTrue(BQResultSetFunctionTest.Result.absolute(-1));
      Assert.assertEquals("whom", BQResultSetFunctionTest.Result.getString(1));
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail("SQLException" + e.toString());
    }

    try {
      Assert.assertTrue(BQResultSetFunctionTest.Result.relative(-5));
      Assert.assertEquals("without", BQResultSetFunctionTest.Result.getString(1));
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail("SQLException" + e.toString());
    }

    try {
      Assert.assertFalse(BQResultSetFunctionTest.Result.relative(6));
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail("SQLException" + e.toString());
    }

    try {
      Assert.assertEquals("without", BQResultSetFunctionTest.Result.getString(1));
    } catch (SQLException e) {
      boolean ct = e.toString().contains("Cursor is not in a valid Position");
      if (ct == true) {
        Assert.assertTrue(ct);
      } else {
        this.logger.error("SQLexception" + e.toString());
        Assert.fail("SQLException" + e.toString());
      }
    }
    this.logger.info("chainedfunctiontest end");
  }

  @Test
  public void databaseMetaDataGetTables() {
    // clouddb,ARTICLE_LOOKUP,starschema.net,[Ljava.lang.String;@9e8424
    ResultSet result = null;
    try {
      // Function call getColumns
      // catalog:null,
      // schemaPattern: starschema_net__clouddb,
      // tableNamePattern:OUTLET_LOOKUP, columnNamePattern: null
      // result = con.getMetaData().getTables("OUTLET_LOOKUP", null, "starschema_net__clouddb", null
      // );
      result = con.getMetaData().getColumns(null, "starschema_net__clouddb", "OUTLET_LOOKUP", null);
      // Function call getTables(catalog: ARTICLE_COLOR_LOOKUP, schemaPattern: null,
      // tableNamePattern: starschema_net__clouddb, types: TABLE , VIEW , SYSTEM TABLE , SYNONYM ,
      // ALIAS , )
    } catch (SQLException e) {
      e.printStackTrace();
      Assert.fail();
    }
    try {
      Assert.assertTrue(result.first());
      while (!result.isAfterLast()) {
        String toprint = "";
        toprint += result.getString(1) + " , ";
        toprint += result.getString(2) + " , ";
        toprint += result.getString(3) + " , ";
        toprint += result.getString(4) + " , ";
        toprint += result.getString(5) + " , ";
        toprint += result.getString(6) + " , ";
        toprint += result.getString(7) + " , ";
        toprint += result.getString(8) + " , ";
        toprint += result.getString(9) + " , ";
        toprint += result.getString(10);
        System.err.println(toprint);
        result.next();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      Assert.fail();
    }
  }

  /**
   * Compares two String[][]
   *
   * @param expected
   * @param reality
   * @return true if they are equal false if not
   */
  private boolean comparer(String[][] expected, String[][] reality) {
    for (int i = 0; i < expected.length; i++) {
      for (int j = 0; j < expected[i].length; j++) {
        if (expected[i][j].toString().equals(reality[i][j]) == false) {
          return false;
        }
      }
    }

    return true;
  }

  /** For testing isValid() , Close() , isClosed() */
  @Test
  public void isClosedValidtest() {
    try {
      Assert.assertEquals(true, BQResultSetFunctionTest.con.isValid(0));
    } catch (SQLException e) {
      Assert.fail("Got an exception" + e.toString());
      e.printStackTrace();
    }
    try {
      Assert.assertEquals(true, BQResultSetFunctionTest.con.isValid(10));
    } catch (SQLException e) {
      Assert.fail("Got an exception" + e.toString());
      e.printStackTrace();
    }
    try {
      BQResultSetFunctionTest.con.isValid(-10);
    } catch (SQLException e) {
      Assert.assertTrue(true);
      // e.printStackTrace();
    }

    try {
      BQResultSetFunctionTest.con.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    try {
      Assert.assertTrue(BQResultSetFunctionTest.con.isClosed());
    } catch (SQLException e1) {
      e1.printStackTrace();
    }

    try {
      BQResultSetFunctionTest.con.isValid(0);
    } catch (SQLException e) {
      Assert.assertTrue(true);
      e.printStackTrace();
    }
  }

  /**
   * Makes a new Bigquery Connection to URL in file and gives back the Connection to static con
   * member.
   */
  @Before
  public void NewConnection() {

    try {
      if (BQResultSetFunctionTest.con == null || !BQResultSetFunctionTest.con.isValid(0)) {
        this.logger.info("Testing the JDBC driver");
        try {
          Class.forName("net.starschema.clouddb.jdbc.BQDriver");
          String jdbcUrl =
              BQSupportFuncts.constructUrlFromPropertiesFile(
                  BQSupportFuncts.readFromPropFile(
                      getClass().getResource("/installedaccount1.properties").getFile()));
          jdbcUrl += "&useLegacySql=true";
          BQResultSetFunctionTest.con =
              DriverManager.getConnection(
                  jdbcUrl,
                  BQSupportFuncts.readFromPropFile(
                      getClass().getResource("/installedaccount1.properties").getFile()));
        } catch (Exception e) {
          e.printStackTrace();
          this.logger.error("Error in connection" + e.toString());
          Assert.fail("General Exception:" + e.toString());
        }
        this.logger.info(((BQConnection) BQResultSetFunctionTest.con).getURLPART());
      }
    } catch (SQLException e) {
      logger.debug("Oops something went wrong", e);
    }
    this.QueryLoad();
  }

  // Comprehensive Tests:

  /**
   * Prints a String[][] QueryResult to Log
   *
   * @param input
   */
  private void printer(String[][] input) {
    for (int s = 0; s < input[0].length; s++) {
      String Output = "";
      for (int i = 0; i < input.length; i++) {
        if (i == input.length - 1) {
          Output += input[i][s];
        } else {
          Output += input[i][s] + "\t";
        }
      }
      this.logger.debug(Output);
    }
  }

  public void QueryLoad() {
    final String sql =
        "SELECT TOP(word,10) AS word, COUNT(*) as count FROM publicdata:samples.shakespeare";
    final String description = "The top 10 word from shakespeare #TOP #COUNT";
    String[][] expectation =
        new String[][] {
          {"you", "yet", "would", "world", "without", "with", "will", "why", "whose", "whom"},
          {"42", "42", "42", "42", "42", "42", "42", "42", "42", "42"}
        };

    this.logger.info("Test number: 01");
    this.logger.info("Running query:" + sql);

    try {
      Statement stmt =
          BQResultSetFunctionTest.con.createStatement(
              ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      stmt.setQueryTimeout(500);
      BQResultSetFunctionTest.Result = stmt.executeQuery(sql);
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail("SQLException" + e.toString());
    }
    Assert.assertNotNull(BQResultSetFunctionTest.Result);

    this.logger.debug(description);
    this.printer(expectation);

    try {
      Assert.assertTrue(
          "Comparing failed in the String[][] array",
          this.comparer(
              expectation, BQSupportMethods.GetQueryResult(BQResultSetFunctionTest.Result)));
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail(e.toString());
    }
  }

  @Test
  public void ResultSetMetadata() {
    try {
      this.logger.debug(BQResultSetFunctionTest.Result.getMetaData().getSchemaName(1));
      this.logger.debug("{}", BQResultSetFunctionTest.Result.getMetaData().getScale(1));
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
    }
    Assert.assertTrue(true);
  }

  @Test
  public void TestResultIndexOutofBound() {
    try {
      this.logger.debug("{}", BQResultSetFunctionTest.Result.getBoolean(99));
    } catch (SQLException e) {
      Assert.assertTrue(true);
      this.logger.error("SQLexception" + e.toString());
    }
  }

  @Test
  public void TestResultSetAbsolute() {
    try {
      Assert.assertTrue(BQResultSetFunctionTest.Result.absolute(1));
      Assert.assertEquals("you", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.absolute(2));
      Assert.assertEquals("yet", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.absolute(3));
      Assert.assertEquals("would", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.absolute(4));
      Assert.assertEquals("world", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.absolute(5));
      Assert.assertEquals("without", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.absolute(6));
      Assert.assertEquals("with", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.absolute(7));
      Assert.assertEquals("will", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.absolute(8));
      Assert.assertEquals("why", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.absolute(9));
      Assert.assertEquals("whose", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.absolute(10));
      Assert.assertEquals("whom", BQResultSetFunctionTest.Result.getString(1));
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail("SQLException" + e.toString());
    }

    try {
      Assert.assertFalse(BQResultSetFunctionTest.Result.absolute(0));
      Assert.assertEquals("", BQResultSetFunctionTest.Result.getString(1));
    } catch (SQLException e) {
      boolean ct = e.toString().contains("Cursor is not in a valid Position");
      if (ct == true) {
        Assert.assertTrue(ct);
      } else {
        this.logger.error("SQLexception" + e.toString());
        Assert.fail("SQLException" + e.toString());
      }
    }

    try {
      Assert.assertFalse(BQResultSetFunctionTest.Result.absolute(11));
      Assert.assertEquals("", BQResultSetFunctionTest.Result.getString(1));
    } catch (SQLException e) {
      boolean ct = e.toString().contains("Cursor is not in a valid Position");
      if (ct == true) {
        Assert.assertTrue(ct);
      } else {
        this.logger.error("SQLexception" + e.toString());
        Assert.fail("SQLException" + e.toString());
      }
    }
  }

  @Test
  public void TestResultSetAfterlast() {
    try {
      BQResultSetFunctionTest.Result.afterLast();
      Assert.assertTrue(BQResultSetFunctionTest.Result.previous());
      Assert.assertEquals("whom", BQResultSetFunctionTest.Result.getString(1));
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail("SQLException" + e.toString());
    }

    try {
      BQResultSetFunctionTest.Result.afterLast();
      Assert.assertEquals("", BQResultSetFunctionTest.Result.getString(1));
    } catch (SQLException e) {
      boolean ct = e.toString().contains("Cursor is not in a valid Position");
      if (ct == true) {
        Assert.assertTrue(ct);
      } else {
        this.logger.error("SQLexception" + e.toString());
        Assert.fail("SQLException" + e.toString());
      }
    }
  }

  @Test
  public void TestResultSetBeforeFirst() {
    try {
      BQResultSetFunctionTest.Result.beforeFirst();
      Assert.assertTrue(BQResultSetFunctionTest.Result.next());
      Assert.assertEquals("you", BQResultSetFunctionTest.Result.getString(1));
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail("SQLException" + e.toString());
    }

    try {
      BQResultSetFunctionTest.Result.beforeFirst();
      Assert.assertEquals("", BQResultSetFunctionTest.Result.getString(1));
    } catch (SQLException e) {
      boolean ct = e.toString().contains("Cursor is not in a valid Position");
      if (ct == true) {
        Assert.assertTrue(ct);
      } else {
        this.logger.error("SQLexception" + e.toString());
        Assert.fail("SQLException" + e.toString());
      }
    }
  }

  @Test
  public void TestResultSetFirst() {
    try {
      Assert.assertTrue(BQResultSetFunctionTest.Result.first());
      Assert.assertTrue(BQResultSetFunctionTest.Result.isFirst());
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail("SQLException" + e.toString());
    }
  }

  @Test
  public void TestResultSetgetBoolean() {
    try {
      Assert.assertTrue(BQResultSetFunctionTest.Result.absolute(1));
      Assert.assertEquals(Boolean.parseBoolean("42"), BQResultSetFunctionTest.Result.getBoolean(2));
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail("SQLException" + e.toString());
    }
  }

  @Test
  public void TestResultSetgetFloat() {
    try {
      Assert.assertTrue(BQResultSetFunctionTest.Result.absolute(1));
      Assert.assertEquals(new Float(42), BQResultSetFunctionTest.Result.getFloat(2));
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail("SQLException" + e.toString());
    }
  }

  @Test
  public void TestResultSetgetInteger() {
    try {
      Assert.assertTrue(BQResultSetFunctionTest.Result.absolute(1));
      Assert.assertEquals(42, BQResultSetFunctionTest.Result.getInt(2));
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail("SQLException" + e.toString());
    }
  }

  @Test
  public void TestResultSetgetRow() {

    try {
      Assert.assertTrue(BQResultSetFunctionTest.Result.absolute(1));
      Assert.assertEquals(1, BQResultSetFunctionTest.Result.getRow());
      Assert.assertTrue(BQResultSetFunctionTest.Result.absolute(10));
      Assert.assertEquals(10, BQResultSetFunctionTest.Result.getRow());
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail("SQLException" + e.toString());
    }
    try {
      BQResultSetFunctionTest.Result.beforeFirst();
      Assert.assertEquals(0, BQResultSetFunctionTest.Result.getRow());
      BQResultSetFunctionTest.Result.afterLast();
      Assert.assertEquals(0, BQResultSetFunctionTest.Result.getRow());
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail("SQLException" + e.toString());
    }
  }

  @Test
  public void TestResultSetgetString() {
    try {
      Assert.assertTrue(BQResultSetFunctionTest.Result.first());
      Assert.assertEquals("you", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.last());
      Assert.assertEquals("whom", BQResultSetFunctionTest.Result.getString(1));
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail("SQLException" + e.toString());
    }
  }

  @Test
  public void TestResultSetLast() {
    try {
      Assert.assertTrue(BQResultSetFunctionTest.Result.last());
      Assert.assertTrue(BQResultSetFunctionTest.Result.isLast());
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail("SQLException" + e.toString());
    }
  }

  @Test
  public void TestResultSetNext() {
    try {
      Assert.assertTrue(BQResultSetFunctionTest.Result.first());
      Assert.assertTrue(BQResultSetFunctionTest.Result.next());
      Assert.assertEquals("yet", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.next());
      Assert.assertEquals("would", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.next());
      Assert.assertEquals("world", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.next());
      Assert.assertEquals("without", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.next());
      Assert.assertEquals("with", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.next());
      Assert.assertEquals("will", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.next());
      Assert.assertEquals("why", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.next());
      Assert.assertEquals("whose", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.next());
      Assert.assertEquals("whom", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertFalse(BQResultSetFunctionTest.Result.next());
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail("SQLException" + e.toString());
    }

    try {
      Assert.assertEquals("", BQResultSetFunctionTest.Result.getString(1));
    } catch (SQLException e) {
      boolean ct = e.toString().contains("Cursor is not in a valid Position");
      if (ct == true) {
        Assert.assertTrue(ct);
      } else {
        this.logger.error("SQLexception" + e.toString());
        Assert.fail("SQLException" + e.toString());
      }
    }
  }

  @Test
  public void TestResultSetPrevious() {
    try {
      Assert.assertTrue(BQResultSetFunctionTest.Result.last());
      Assert.assertTrue(BQResultSetFunctionTest.Result.previous());
      Assert.assertEquals("whose", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.previous());
      Assert.assertEquals("why", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.previous());
      Assert.assertEquals("will", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.previous());
      Assert.assertEquals("with", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.previous());
      Assert.assertEquals("without", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.previous());
      Assert.assertEquals("world", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.previous());
      Assert.assertEquals("would", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.previous());
      Assert.assertEquals("yet", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.previous());
      Assert.assertEquals("you", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertFalse(BQResultSetFunctionTest.Result.previous());
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail("SQLException" + e.toString());
    }
    try {
      Assert.assertEquals("", BQResultSetFunctionTest.Result.getString(1));
    } catch (SQLException e) {
      boolean ct = e.toString().contains("Cursor is not in a valid Position");
      if (ct == true) {
        Assert.assertTrue(ct);
      } else {
        this.logger.error("SQLexception" + e.toString());
        Assert.fail("SQLException" + e.toString());
      }
    }
  }

  @Test
  public void TestResultSetRelative() {
    try {
      Assert.assertTrue(BQResultSetFunctionTest.Result.absolute(1));
      Assert.assertEquals("you", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.relative(1));
      Assert.assertEquals("yet", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.relative(2));
      Assert.assertEquals("world", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.relative(5));
      Assert.assertEquals("whose", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.relative(-5));
      Assert.assertEquals("world", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.relative(-2));
      Assert.assertEquals("yet", BQResultSetFunctionTest.Result.getString(1));
      Assert.assertTrue(BQResultSetFunctionTest.Result.relative(-1));
      Assert.assertEquals("you", BQResultSetFunctionTest.Result.getString(1));
    } catch (SQLException e) {
      this.logger.error("SQLexception" + e.toString());
      Assert.fail("SQLException" + e.toString());
    }
    try {
      Assert.assertTrue(BQResultSetFunctionTest.Result.first());
      Assert.assertFalse(BQResultSetFunctionTest.Result.relative(-1));
      Assert.assertEquals("", BQResultSetFunctionTest.Result.getString(1));
    } catch (SQLException e) {
      boolean ct = e.toString().contains("Cursor is not in a valid Position");
      if (ct == true) {
        Assert.assertTrue(ct);
      } else {
        this.logger.error("SQLexception" + e.toString());
        Assert.fail("SQLException" + e.toString());
      }
    }

    try {
      Assert.assertTrue(BQResultSetFunctionTest.Result.last());
      Assert.assertFalse(BQResultSetFunctionTest.Result.relative(1));
      Assert.assertEquals("", BQResultSetFunctionTest.Result.getString(1));
    } catch (SQLException e) {
      boolean ct = e.toString().contains("Cursor is not in a valid Position");
      if (ct == true) {
        Assert.assertTrue(ct);
      } else {
        this.logger.error("SQLexception" + e.toString());
        Assert.fail("SQLException" + e.toString());
      }
    }
  }
}
