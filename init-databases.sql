CREATE DATABASE IF NOT EXISTS mehana;
CREATE DATABASE IF NOT EXISTS mehana_orders;

GRANT ALL PRIVILEGES ON mehana.* TO 'mehana_user'@'%';
GRANT ALL PRIVILEGES ON mehana_orders.* TO 'mehana_user'@'%';

FLUSH PRIVILEGES;
