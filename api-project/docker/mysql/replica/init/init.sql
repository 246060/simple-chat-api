
# docker exec -it mysql_replica mysql -uroot -proot

use mysql;

GRANT SELECT ON *.* TO `dev`@`%`;

flush privileges;

CHANGE MASTER TO
    MASTER_HOST='mysql_main',
    MASTER_USER='replica',
    MASTER_PASSWORD='1234',
    MASTER_LOG_FILE='binary_log.000003',
    MASTER_LOG_POS=848,
    GET_MASTER_PUBLIC_KEY=1;

start slave;

show slave status\G;

# check 1.Slave_IO_Running: Yes, 2.Slave_SQL_Running: Yes