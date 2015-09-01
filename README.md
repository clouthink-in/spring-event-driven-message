# Introduction

The lightweight event driven message framework based on reactor.

# Dependencies

* Reactor 2.0.4.RELEASE

# Usage

So far `FIXME` is available 

## Maven

    <dependency>
        <groupId>in.clouthink.daas</groupId>
        <artifactId>daas-edm</artifactId>
        <version>${daas.edm.version}</version>
    </dependency>

## Gradle

    compile "in.clouthink.daas:daas-edm:${daas_edm_version}"


## Spring Configuration

Use `@EnableEdm` to get started 

    @Configuration
    @EnableEdm
    public class Application {}
