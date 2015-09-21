#!/bin/sh
mvn -f module/core/pom.xml clean install
mvn -f module/email/pom.xml clean install
mvn -f module/sms/pom.xml clean install
mvn -f module/push/pom.xml clean install
