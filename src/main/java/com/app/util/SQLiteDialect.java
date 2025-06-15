//package com.app.util;
//
//import org.hibernate.dialect.Dialect;
//import org.hibernate.dialect.identity.IdentityColumnSupport;
//import org.hibernate.dialect.identity.SQLiteIdentityColumnSupport;
//
//import java.sql.Types;
//
//public class SQLiteDialect extends Dialect {
//
//    public SQLiteDialect() {
//        super();
//        // Map Java types to SQLite types
//        
//    }
//
//    @Override
//    public IdentityColumnSupport getIdentityColumnSupport() {
//        return new SQLiteIdentityColumnSupport();
//    }
//
//    @Override
//    public boolean hasAlterTable() {
//        return true;
//    }
//
//    @Override
//    public boolean dropConstraints() {
//        return false;
//    }
//
//    @Override
//    public String getAddColumnString() {
//        return "add column";
//    }
//
//    @Override
//    public String getForUpdateString() {
//        return "";
//    }
//
//    @Override
//    public boolean supportsOuterJoinForUpdate() {
//        return false;
//    }
//
//    @Override
//    public boolean supportsIfExistsBeforeTableName() {
//        return true;
//    }
//
//    @Override
//    public boolean supportsIfExistsAfterTableName() {
//        return false;
//    }
//}