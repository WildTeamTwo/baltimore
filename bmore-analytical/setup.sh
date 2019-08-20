#!/bin/bash

DB_BACKUP_PATH='../backups'
MYSQL_HOST='localhost'
MYSQL_PORT='3306'
MYSQL_USER='root'
MYSQL_PASSWORD='benutzenmaschine'
DATABASE_NAME='baltimore'

#################################################################################
# create mysql database
mysql -h ${MYSQL_HOST} -u ${MYSQL_USER} -p ${DATABASE_NAME} < "db/scripts/db.sql"

if [ $? -eq 0 ]; then
  echo "Database setup successfully completed"
else
  echo "Error found setup"
  exit 1
fi
