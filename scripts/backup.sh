#!/bin/bash

APP_NAME="relay-test-endpoint-1.0.0.jar"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
APP_PATH="${SCRIPT_DIR}/${APP_NAME}"
BACKUP_DIR="${SCRIPT_DIR}/backup"

if [ ! -f "$APP_PATH" ]; then
    echo "[ERROR] ${APP_NAME} not found: ${APP_PATH}"
    exit 1
fi

# Create backup directory
mkdir -p "${BACKUP_DIR}"

# Generate timestamp (YYYYMMDDHHmmss)
TIMESTAMP=$(date +"%Y%m%d%H%M%S")

# Backup filename: relay-test-endpoint-1.0.0_20260206143000.jar
BASENAME="${APP_NAME%.jar}"
BACKUP_FILE="${BASENAME}_${TIMESTAMP}.jar"

cp "${APP_PATH}" "${BACKUP_DIR}/${BACKUP_FILE}"

if [ $? -eq 0 ]; then
    echo "[INFO] Backup completed: ${BACKUP_DIR}/${BACKUP_FILE}"
else
    echo "[ERROR] Backup failed"
    exit 1
fi
