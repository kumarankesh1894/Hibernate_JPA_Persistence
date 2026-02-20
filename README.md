# Hibernate JPA Configuration -- XML vs Class-Based Configuration with HikariCP

## Overview

This document explains:

-   Why we use `persistence.xml`
-   Why we created `CustomClassConfiguration`
-   What a Connection Pool is
-   Why we use HikariCP
-   When to use each approach

------------------------------------------------------------------------

## 1️⃣ Using `persistence.xml` (XML-Based Configuration)

In a normal Hibernate JPA Maven project, configuration is done using:

`src/main/resources/META-INF/persistence.xml`

Example:

``` xml
<persistence-unit name="my-persistence-unit" transaction-type="RESOURCE_LOCAL">
    <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>

    <class>org.hibernatejpa.entity.Student</class>

    <properties>
        <property name="jakarta.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
        <property name="jakarta.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/HJPA_DB"/>
        <property name="jakarta.persistence.jdbc.user" value="userName"/>
        <property name="jakarta.persistence.jdbc.password" value="password"/>
        <property name="hibernate.hbm2ddl.auto" value="create"/>
    </properties>
</persistence-unit>
```

### What This Does

-   Registers entity classes\
-   Configures database connection\
-   Sets Hibernate properties\
-   Bootstraps `EntityManagerFactory`

This is called **XML-based configuration**.

------------------------------------------------------------------------

## 2️⃣ What Is Class-Based Configuration?

Instead of XML, we configure Hibernate using Java classes.

Example:

``` java
public class CustomClassConfiguration implements PersistenceUnitInfo {
    @Override
    public List<String> getManagedClassNames() {
        return List.of("org.hibernatejpa.entity.Student");
    }

    @Override
    public Properties getProperties() {
        Properties props = new Properties();
        props.put("hibernate.hbm2ddl.auto", "create");
        return props;
    }
}
```

This replaces `persistence.xml` with Java configuration.

This is called **class-based configuration**.

------------------------------------------------------------------------

## 3️⃣ Why Use CustomClassConfiguration?

We use class-based configuration when:

-   We want dynamic configuration
-   We need multiple databases
-   We need multi-tenant support
-   We want zero XML
-   We are building frameworks
-   We need full control over DataSource

### Example Use Case

Multi-tenant SaaS system:

User A → Connect to DB A\
User B → Connect to DB B

This cannot be handled using static XML.\
It requires programmatic configuration.

------------------------------------------------------------------------

## 4️⃣ What Is a Connection Pool?

### Problem Without Pool

Each time we create a connection:

``` java
DriverManager.getConnection(...);
```

The database must:

-   Open network connection\
-   Authenticate user\
-   Allocate session\
-   Allocate memory

This is expensive and slow.

If 100 users hit the system:

-   100 connections created\
-   100 connections destroyed

This causes performance problems and database overload.

------------------------------------------------------------------------

### What Is a Connection Pool?

A connection pool:

-   Creates connections once
-   Keeps them ready
-   Reuses them
-   Limits maximum connections

Instead of:

Create → Use → Destroy

We do:

Create once → Reuse many times

This improves performance and stability.

------------------------------------------------------------------------

## 5️⃣ Why Use HikariCP?

There are many connection pools:

-   C3P0\
-   Apache DBCP\
-   HikariCP

We use **HikariCP** because:

### 🔥 High Performance

Very fast and lightweight.

### 💾 Low Memory Usage

Consumes fewer resources.

### 🏢 Industry Standard

Spring Boot uses HikariCP by default.

### ⚡ Advanced Timeout Handling

Supports connection timeout, idle timeout, max lifetime, etc.

Example:

``` java
HikariDataSource ds = new HikariDataSource();
ds.setMaximumPoolSize(10);
ds.setConnectionTimeout(30000);
```

### 🛡 Production Ready

Hibernate's built-in pool is NOT recommended for production.

Log message:

Using built-in connection pool (not intended for production use)

------------------------------------------------------------------------

## 6️⃣ Why Combine CustomClassConfiguration + HikariCP?

When using:

``` java
getNonJtaDataSource()
```

We provide Hikari as the DataSource.

Flow:

CustomClassConfiguration\
↓\
Provides Hikari DataSource\
↓\
Hibernate uses that pool\
↓\
EntityManager uses pooled connections

This ensures high performance and production readiness.

------------------------------------------------------------------------

## 7️⃣ When Should We Use Each?

  Scenario                XML   Class Config      Hikari
  ----------------------- ----- ----------------- ----------
  Learning project        ✅    ❌                Optional
  Production REST API     ✅    ❌                ✅
  Spring Boot app         ❌    Auto-configured   ✅
  Multi-tenant SaaS       ❌    ✅                ✅
  Framework development   ❌    ✅                ✅

------------------------------------------------------------------------

## 8️⃣ Interview-Level Summary

**What is Connection Pool?**\
A mechanism that reuses database connections instead of creating new
ones for every request.

**Why HikariCP?**\
It is a high-performance, lightweight, production-ready connection pool
used by Spring Boot.

**What is Class-Based Configuration?**\
Configuring Hibernate using Java classes instead of XML for flexibility
and dynamic behavior.

**Why CustomClassConfiguration?**\
To replace persistence.xml and provide programmatic configuration,
including custom DataSource and connection pooling.

------------------------------------------------------------------------

## 9️⃣ Final Understanding

JDBC → Connects to DB\
Connection Pool → Manages DB connections\
HikariCP → High-performance connection pool\
Hibernate → ORM layer\
persistence.xml → XML configuration\
CustomClassConfiguration → Java-based configuration

------------------------------------------------------------------------

## 🔥 Key Takeaway

-   Use XML for simple projects.\
-   Use HikariCP for production.\
-   Use class-based configuration for advanced, dynamic setups.\
-   Most enterprise applications use HikariCP.
