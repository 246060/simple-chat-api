
# -- docker exec -it mysql_main mysql -uroot -proot

use mysql;

CREATE USER 'replica'@'%' IDENTIFIED BY 'P@ssW0rd!@#';
# -- CREATE USER 'replica'@'%' IDENTIFIED WITH 'mysql_native_password' BY 'P@ssW0rd!@#';

GRANT REPLICATION SLAVE ON *.* TO 'replica'@'%';

GRANT ALL PRIVILEGES ON *.* TO `dev`@`%`;

flush privileges;

show master status\G;

# check binary_log and cursor position
