/usr/bin/mysql -uroot -proot -e "drop database branchitup" branchitup


#untar environment
rm -R -f /branchitup-env/diskresources
sudo tar --force-local -zxf "diskresources-backup_${1}.tar.gz" -C /branchitup-env/


/usr/bin/mysql -uroot -proot < /backup/branchitup.sql
/usr/bin/mysql -uroot -proot branchitup < /backup/branchitup-backup_${1}.sql