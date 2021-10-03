### Блок 1. ###

[Скрин namenode](https://github.com/Brutalfacepalm/3_ml_bd/blob/master/hw_1/Screenshot%20from%202021-10-03%2022-18-06.png?raw=true) \
[Скрин resourcemanager](https://github.com/Brutalfacepalm/3_ml_bd/blob/master/hw_1/Screenshot%20from%202021-10-03%2022-17-41.png?raw=true)


### Блок 2. ###
###### Подключаемся к неймноде ######
    **sudo docker exec -it namenode /bin/bash**

###### Посмотрим на hdfs ######
    **root@cb9aa3ae4ea9:/# hdfs dfs -ls /**
    Found 1 items
    drwxr-xr-x   - root supergroup          0 2021-10-01 16:56 /rmstate

#### -mkdir, -touchz ####

###### (1) Создадим папку ######
    **root@cb9aa3ae4ea9:/# hdfs dfs -mkdir /block_2_1**

###### (2) Создадим папку внутри ######
    **root@cb9aa3ae4ea9:/# hdfs dfs -mkdir /block_2_1/block_2_2**

###### (3) Trash это типа корзина, чтоб можно было восстановить файл. Удалить мимо каризины - флаг -skipTrash ######

###### (4) Создадим пустой файл ######
    **root@cb9aa3ae4ea9:/# hdfs dfs -touchz /block_2_1/block_2_2/block_2_4_empty_file**

###### (5) Удалим файл ######
    **root@cb9aa3ae4ea9:/# hdfs dfs -rm -skipTrash /block_2_1/block_2_2/block_2_4_empty_file**
    Deleted /block_2_1/block_2_2/block_2_4_empty_file

###### (6) Удалим папки. Удалять можно поштучно обычным mkdir, а можно -rm -R, тогда удаление будет рекурсивно. ######
    **root@cb9aa3ae4ea9:/# hdfs dfs -rmdir /block_2_1/block_2_2**
    **root@cb9aa3ae4ea9:/# hdfs dfs -rmdir /block_2_1**


#### -put, -cat, -tail, -cp ####

###### (1) Скопируем файл в hdfs: Сначала создадим папку, куда скопируем. ######
    **sudo docker cp ../requirements.txt namenode:/**

    **root@cb9aa3ae4ea9:/# hdfs dfs -mkdir /block_2_1_put**
    **root@cb9aa3ae4ea9:/# hdfs dfs -put requirements.txt /block_2_1_put/requirements.txt**
    2021-10-01 17:53:11,102 INFO sasl.SaslDataTransferClient: SASL encryption trust check: localHostTrusted = false, remoteHostTrusted = false

###### (2) Содержимое файла ######
    **root@cb9aa3ae4ea9:/# hdfs dfs -cat /block_2_1_put/requirements.txt**
    2021-10-01 18:02:01,739 INFO sasl.SaslDataTransferClient: SASL encryption trust check: localHostTrusted = false, remoteHostTrusted = false
    Flask==1.1.2
    beautifulsoup4==4.9.1
    gunicorn==20.0.4
    jupyter==1.0.0
    lxml==4.5.2
    pylint==2.6.0
    pytest==6.0.2
    pytest-cov
    PyYAML==5.3.1
    requests==2.24.0

###### (3) Конец файла. Можно выводить через -cat и | tail -n,на выходе будет n строк с конца. А можно выводить через -tail, тогда выведится ровно 1 КБ данных с конца. ######
    **root@cb9aa3ae4ea9:/# hdfs dfs -cat /block_2_1_put/requirements.txt | tail -5**
    2021-10-01 18:02:22,675 INFO sasl.SaslDataTransferClient: SASL encryption trust check: localHostTrusted = false, remoteHostTrusted = false
    pylint==2.6.0
    pytest==6.0.2
    pytest-cov
    PyYAML==5.3.1
    requests==2.24.0

###### (4) Начало файла ######
    **root@cb9aa3ae4ea9:/# hdfs dfs -cat /block_2_1_put/requirements.txt | head -5**
    2021-10-01 18:02:01,739 INFO sasl.SaslDataTransferClient: SASL encryption trust check: localHostTrusted = false, remoteHostTrusted = false
    Flask==1.1.2
    beautifulsoup4==4.9.1
    gunicorn==20.0.4
    jupyter==1.0.0
    lxml==4.5.2
###### (5) Скопируем и перместим файл ######
    **root@cb9aa3ae4ea9:/# hdfs dfs -cp /block_2_1_put/requirements.txt /requirements.txt**
    2021-10-01 18:03:28,887 INFO sasl.SaslDataTransferClient: SASL encryption trust check: localHostTrusted = false, remoteHostTrusted = false
    2021-10-01 18:03:29,034 INFO sasl.SaslDataTransferClient: SASL encryption trust check: localHostTrusted = false, remoteHostTrusted = false

    **root@cb9aa3ae4ea9:/# hdfs dfs -ls /**
    Found 3 items
    drwxr-xr-x   - root supergroup          0 2021-10-01 17:59 /block_2_1_put
    -rw-r--r--   3 root supergroup       8289 2021-10-01 18:03 /requirements.txt
    drwxr-xr-x   - root supergroup          0 2021-10-01 16:56 /rmstate

#### fsck ####
###### (2) Изменить репликациии. В выводе -ls мы сразу видим фактор репликации для файла, сейчас он равен 3, вроде как по умолчанию. Изменим на 2. Изменение числа реплик длится около 5 секунд. ######

    **root@cb9aa3ae4ea9:/# hdfs dfs -ls /**
    Found 2 items
    -rw-r--r--   3 root supergroup       8289 2021-10-01 18:03 /requirements.txt
    drwxr-xr-x   - root supergroup          0 2021-10-01 16:56 /rmstate

    **root@cb9aa3ae4ea9:/# hdfs dfs -setrep -w 2 /requirements.txt**
    Replication 2 set: /requirements.txt
    Waiting for /requirements.txt ...
    WARNING: the waiting time may be long for DECREASING the number of replications.
    . done

    **root@cb9aa3ae4ea9:/# hdfs dfs -ls /**
    Found 2 items
    -rw-r--r--   2 root supergroup       8289 2021-10-01 18:03 /requirements.txt
    drwxr-xr-x   - root supergroup          0 2021-10-01 16:56 /rmstate

###### (3) Информация по файлу, блокам и расположениям ######
    **root@cb9aa3ae4ea9:/# hdfs fsck /requirements.txt -files -blocks -locations**
    Connecting to namenode via http://namenode:9870/fsck?ugi=root&files=1&blocks=1&locations=1&path=%2Frequirements.txt
    FSCK started by root (auth:SIMPLE) from /172.20.0.4 for path /requirements.txt at Fri Oct 01 18:08:44 UTC 2021
    /requirements.txt 8289 bytes, replicated: replication=2, 1 block(s):  OK
    0. BP-914163096-172.18.0.7-1633106653419:blk_1073741843_1019 len=8289 Live_repl=2  [DatanodeInfoWithStorage[172.20.0.6:9866,DS-449ba0cc-b6f6-44fb-b9dc-8537d8883eac,DISK], DatanodeInfoWithStorage[172.20.0.10:9866,DS-2f043176-3739-457e-84f9-8ed6b3fb4972,DISK]]


    Status: HEALTHY
     Number of data-nodes:	3
     Number of racks:		1
     Total dirs:			0
     Total symlinks:		0

    Replicated Blocks:
     Total size:	8289 B
     Total files:	1
     Total blocks (validated):	1 (avg. block size 8289 B)
     Minimally replicated blocks:	1 (100.0 %)
     Over-replicated blocks:	0 (0.0 %)
     Under-replicated blocks:	0 (0.0 %)
     Mis-replicated blocks:		0 (0.0 %)
     Default replication factor:	3
     Average block replication:	2.0
     Missing blocks:		0
     Corrupt blocks:		0
     Missing replicas:		0 (0.0 %)

    Erasure Coded Block Groups:
     Total size:	0 B
     Total files:	0
     Total block groups (validated):	0
     Minimally erasure-coded block groups:	0
     Over-erasure-coded block groups:	0
     Under-erasure-coded block groups:	0
     Unsatisfactory placement block groups:	0
     Average block group size:	0.0
     Missing block groups:		0
     Corrupt block groups:		0
     Missing internal blocks:	0
    FSCK ended at Fri Oct 01 18:08:44 UTC 2021 in 4 milliseconds


    The filesystem under path '/requirements.txt' is HEALTHY

###### (4) На предыдущем шаге ID блока начинается на blk. GS number - число в конце после. Запрос по блоку можно сдлеть двумя способами. ######

    **root@cb9aa3ae4ea9:/# hdfs fsck -blockId blk_1073741843**
    **root@cb9aa3ae4ea9:/# hdfs fsck -blockId blk_1073741843_1019.meta **
    Connecting to namenode via http://namenode:9870/fsck?ugi=root&blockId=blk_1073741843+&path=%2F
    FSCK started by root (auth:SIMPLE) from /172.20.0.4 at Fri Oct 01 18:14:12 UTC 2021

    Block Id: blk_1073741843
    Block belongs to: /requirements.txt
    No. of Expected Replica: 2
    No. of live Replica: 2
    No. of excess Replica: 0
    No. of stale Replica: 0
    No. of decommissioned Replica: 0
    No. of decommissioning Replica: 0
    No. of corrupted Replica: 0
    Block replica on datanode/rack: 4ebbb45b3923/default-rack is HEALTHY
    Block replica on datanode/rack: 435f62cabe7c/default-rack is HEALTHY

### Блок 3. ###
###### Mapper и Reducer могут запускаться как локальный код, так и MapReduce. ######
###### Для запуска: ######

    **bash run.sh AB_NYC_2019.csv result.txt**