
# docker exec -it mysql_replica mysql -uroot -proot

use mysql;

GRANT SELECT ON *.* TO `dev`@`%`;

flush privileges;

CHANGE MASTER TO
    MASTER_HOST='mysql_primary',
    MASTER_USER='replica',
    MASTER_PASSWORD='P@ssW0rd!@#',
    MASTER_LOG_FILE='binary_log.000003',
    MASTER_LOG_POS=1069,
    GET_MASTER_PUBLIC_KEY=1;

start slave;

show slave status\G;

# check 1.Slave_IO_Running: Yes, 2.Slave_SQL_Running: Yes