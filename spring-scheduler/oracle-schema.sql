
DROP TABLE ST_CLUSTER_CONTROL;

CREATE TABLE ST_CLUSTER_CONTROL(
  TASK_NAME VARCHAR2(50) UNIQUE,
  TASK_STATUS VARCHAR2(20) DEFAULT 'WAIT',
  TASK_LOCK INT DEFAULT 1 ,
  TASK_GROUP VARCHAR2(50) DEFAULT NULL,
  TASK_CLASS_NAME VARCHAR2(100) DEFAULT NULL,
  SCHEDULE_NAME VARCHAR2(50) DEFAULT NULL,
  LAST_START_TIME TIMESTAMP,
  LAST_END_TIME TIMESTAMP
);