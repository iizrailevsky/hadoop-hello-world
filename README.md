# Hadoop Hello World Example

## Prerequisites

Confirm Java 1.6

```shell
$ java -version
java version "1.6.0_65"
Java(TM) SE Runtime Environment (build 1.6.0_65-b14-462-11M4609)
Java HotSpot(TM) 64-Bit Server VM (build 20.65-b04-462, mixed mode)
```

Install Homebrew, if needed:

```shell
$ ruby -e "$(curl -fsSL https://raw.github.com/mxcl/homebrew/go)"
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
$ cd /usr/local/Cellar/hadoop/2.3.0/libexec/etc/hadoop
```

Add to hadoop-env.sh:

```shell
export JAVA_HOME="/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Home"

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
PATH=$PATH:/usr/local/Cellar/hadoop/2.3.0/sbin
```

## Start Hadoop
```shell
$ hadoop namenode -format
$ start-all.sh
```

Make sure all Hadoop processes started correctly:

```shell
$ ps aux | grep hadoop | wc -l
     6
```

## Run the WordCount example

Build hadoop-hello-world project:

```shell
$ cd ~/hadoop-hello-world/
$ mvn clean install
```

Copy test input files to HDFS from local filesystem:

```shell
$ hadoop fs -copyFromLocal src/test/resources/input /.
$ hadoop fs -ls /input
Found 2 items
-rw-r--r--   1 iizrailevsky supergroup        520 2014-04-05 15:39 /input/test1
-rw-r--r--   1 iizrailevsky supergroup        590 2014-04-05 15:39 /input/test2
```

Run the WordCount example and check result output:

```shell
$ hadoop jar target/hadoop-hello-world.jar com.intuit.dataservices.hadoop.WordCount /input /output
$ hadoop fs -ls /output
$ hadoop fs -cat /output/part-r-00000
Although        1
Cymbeline       3
Denmark 2
```

Stop Hadoop:

```shell
$ stop-all.sh
```
