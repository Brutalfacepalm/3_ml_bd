set -x
HADOOP_STREAMING_JAR=/opt/hadoop-3.2.1/share/hadoop/tools/lib/hadoop-streaming-3.2.1.jar
INPUT_PATH=$1
OUT_PATH=$2

hdfs dfs -rm -R $OUT_PATH


yarn jar $HADOOP_STREAMING_JAR \
        -files mapper.py,reducer.py \
        -mapper "python ./mapper.py" \
        -reducer "python ./reducer.py" \
        -numReduceTasks 1 \
        -input $INPUT_PATH \
        -output $OUT_PATH

hdfs dfs -cat $(hdfs dfs -ls -R $OUT_PATH | grep "part"| awk '{print $8}') >> $OUT_PATH
