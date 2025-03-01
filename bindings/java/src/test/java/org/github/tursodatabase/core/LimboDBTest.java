package org.github.tursodatabase.core;

import org.github.tursodatabase.LimboErrorCode;
import org.github.tursodatabase.TestUtils;
import org.github.tursodatabase.exceptions.LimboException;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LimboDBTest {

    @Test
    void db_should_open_normally() throws Exception {
        String dbPath = TestUtils.createTempFile();
        LimboDB db = LimboDB.create("jdbc:sqlite" + dbPath, dbPath);
        db.load();
        db.open(0);
    }

    @Test
    void should_throw_exception_when_opened_twice() throws Exception {
        String dbPath = TestUtils.createTempFile();
        LimboDB db = LimboDB.create("jdbc:sqlite:" + dbPath, dbPath);
        db.load();
        db.open(0);

        assertThatThrownBy(() -> db.open(0)).isInstanceOf(SQLException.class);
    }

    @Test
    void throwJavaException_should_throw_appropriate_java_exception() throws Exception {
        String dbPath = TestUtils.createTempFile();
        LimboDB db = LimboDB.create("jdbc:sqlite:" + dbPath, dbPath);
        db.load();

        final int limboExceptionCode = LimboErrorCode.ETC.code;
        try {
            db.throwJavaException(limboExceptionCode);
        } catch (Exception e)  {
            assertThat(e).isInstanceOf(LimboException.class);
            LimboException limboException = (LimboException) e;
            assertThat(limboException.getResultCode().code).isEqualTo(limboExceptionCode);
        }
    }
}
