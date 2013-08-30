current_time="$(date '+%Y-%m-%d-%T')";
/usr/bin/mysqldump -uroot -proot --skip-add-drop-table --no-create-db --no-create-info --skip-add-locks --skip-comments branchitup > /backup/branchitup-backup_$current_time.sql;

#tar environment
cd /branchitup-env;
tar -zcf /backup/diskresources-backup_$current_time.tar.gz diskresources/
chmod 755 /backup/diskresources-backup_$current_time.tar.gz;
