# Hadoop Mac Install and Map Reduce Examples

## Prerequisites

Confirm Java 1.6.* or higher for Hadoop

```shell
$ java -version
java version "1.7.0_79"
Java(TM) SE Runtime Environment (build 1.7.0_79-b15)
Java HotSpot(TM) 64-Bit Server VM (build 24.79-b02, mixed mode)
```

Install Homebrew, Git, Maven if needed:

```shell
$ ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"

% brew install git
% brew install git-flow-avh

% brew install maven
```

Ensure you can login to your localhost machine:

```shell
$ ssh-keygen -t rsa -P ""
$ cat $HOME/.ssh/id_rsa.pub >> $HOME/.ssh/authorized_keys
$ ssh localhost
```

## Installation

Install Hadoop and Hive:

```shell
$ brew install hadoop
$ brew install hive
```

## Configuration

Every component of Hadoop is configured using Shell and XML files.

```shell
$ cd /usr/local/Cellar/hadoop/2.7.1/libexec/etc/hadoop
```

Add to hadoop-env.sh your JAVA_HOME path, e.g.:

```shell
export JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home"

...

export HADOOP_OPTS="$HADOOP_OPTS -Djava.security.krb5.realm= -Djava.security.krb5.kdc="
```

Add to core-site.xml:

```xml
<configuration>
 <property>
   <name>hadoop.tmp.dir</name>
   <value>/tmp/hadoop-${user.name}</value>
   <description>A base for other temporary directories.</description>
 </property>
 <property>
   <name>fs.default.name</name>
   <value>hdfs://localhost:9000</value>
 </property>
</configuration>
```

Add to hdfs-site.xml:

```xml
<configuration>
  <property>
    <name>dfs.replication</name>
    <value>1</value>
  </property>
</configuration>
```

Add to mapred-site.xml:

```xml
<configuration>
  <property>
    <name>mapred.job.tracker</name>
    <value>localhost:9001</value>
  </property>
</configuration>
```

Update your ~/.bash_profile PATH:

```shell
PATH=$PATH:/usr/local/Cellar/hadoop/2.7.1/sbin
```

Source your ~/.bash_profile

```shell
$ source ~/.bash_profile
```

## Start / Stop Hadoop
```shell
# cleanup HDFS directory
$ rm -rf /tmp/hadoop-*
# format HDFS
$ hadoop namenode -format
# unleash the Hadoop daemons
$ start-all.sh
```

Make sure all Hadoop processes started correctly:

```shell
$ ps aux | grep hadoop | wc -l
     6
```

Stop Hadoop:

```shell
$ stop-all.sh
```

## Hadoop Examples Setup

Build hadoop-hello-world project:

```shell
$ cd ~/hadoop-hello-world/
$ mvn clean install
```

## Run the VoteTally Example

Copy test input files to HDFS from local filesystem:

```shell
$ hadoop fs -mkdir /input
$ hadoop fs -copyFromLocal src/test/resources/input/votetally /input/.
$ hadoop fs -ls /input
$ hadoop fs -ls /input/votetally
Found 5 items
-rw-r--r--   1 iizrailevsky supergroup      12637 2015-09-26 17:27 /input/votetally/ballotsDistrict0
-rw-r--r--   1 iizrailevsky supergroup      12566 2015-09-26 17:27 /input/votetally/ballotsDistrict1
-rw-r--r--   1 iizrailevsky supergroup      12500 2015-09-26 17:27 /input/votetally/ballotsDistrict2
-rw-r--r--   1 iizrailevsky supergroup      12557 2015-09-26 17:27 /input/votetally/ballotsDistrict3
-rw-r--r--   1 iizrailevsky supergroup      12637 2015-09-26 17:27 /input/votetally/ballotsDistrict4
```

Run the VoteTally example and check result output:

```shell
$ hadoop jar target/hadoop-hello-world.jar com.iizrailevsky.hadoop.example.votetally.VoteTally /input/votetally /output/votetally
$ hadoop fs -ls /output/votetally
$ hadoop fs -cat /output/votetally/part-r-00000
Bush	354
Carson	324
Christie	317
```

## Run the WordCount Example

Copy test input files to HDFS from local filesystem:

```shell
$ hadoop fs -mkdir /input
$ hadoop fs -copyFromLocal src/test/resources/input/wordcount /input/.
$ hadoop fs -ls /input/wordcount
Found 2 items
-rw-r--r--   1 iizrailevsky supergroup        520 2014-04-05 15:39 /input/test1
-rw-r--r--   1 iizrailevsky supergroup        590 2014-04-05 15:39 /input/test2
```

Run the WordCount example and check result output:

```shell
$ hadoop jar target/hadoop-hello-world.jar com.iizrailevsky.hadoop.example.wordcount.WordCount /input/wordcount /output/wordcount
$ hadoop fs -ls /output/wordcount
$ hadoop fs -cat /output/wordcount/part-r-00000
Although        1
Cymbeline       3
Denmark 2
```


