#!/bin/bash

# Set variables
DB_USER="root"
DB_NAME="deltasmart"
BACKUP_FILE="deltasmart_backup.sql"
MYSQL_CONTAINER_NAME="mysql_container_name" # Change this if you're using Docker

# Function to back up the database
backup_database() {
    echo "Backing up database $DB_NAME..."
    mysqldump -u $DB_USER -p $DB_NAME > $BACKUP_FILE
    if [ $? -eq 0 ]; then
        echo "Backup successful! Saved as $BACKUP_FILE."
    else
        echo "Backup failed!"
        exit 1
    fi
}

# Function to restore the database
restore_database() {
    echo "Restoring database $DB_NAME from $BACKUP_FILE..."
    mysql -u $DB_USER -p $DB_NAME < $BACKUP_FILE
    if [ $? -eq 0 ]; then
        echo "Restore successful!"
    else
        echo "Restore failed!"
        exit 1
    fi
}

# Main script logic
case "$1" in
    backup)
        backup_database
        ;;
    restore)
        restore_database
        ;;
    *)
        echo "Usage: $0 {backup|restore}"
        exit 1
esac

