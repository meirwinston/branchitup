/usr/bin/mysql -uroot -proot -e "drop database branchitup" branchitup
/usr/bin/mysql -uroot -proot < /backup/branchitup.sql
rm -R -f /branchitup-env/diskresources
mkdir /branchitup-env/diskresources
cd /branchitup-env/diskresources
mkdir images
mkdir pdf