#!/bin/bash

cd src/main/java

rm -f "Application.java"

cd entity
find . ! -name 'BaseEntity.java' -type f -exec rm -f {} +

cd ../dao
find . ! -name 'BaseDAO.java' -type f -exec rm -f {} +

cd ../example
find . ! -name 'BaseExample.java' -type f -exec rm -f {} +
