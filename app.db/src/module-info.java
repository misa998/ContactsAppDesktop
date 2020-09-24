module app.db {
    requires java.sql;
    requires sqlite.jdbc;
    requires transitive app.common;

    opens app.db to java.sql;
    exports app.db;
}