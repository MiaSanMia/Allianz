##############################################################################
## comment-async-service 启动脚本
##
##
##############################################################################
#!bin/bash

JVM_ARGS=" -server -Xms5G -Xmx5G -XX:ThreadStackSize=256k -XX:MaxPermSize=256M -verbose:gc -XX:+PrintGCDetails -Xloggc:logs/gc.log -XX:+UseParallelGC -XX:+PrintGCTimeStamps "
JMX_ARGS=" -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=7075 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false "

JAVA_OPTS="$JVM_ARGS $JMX_ARGS"
#debug="-DEBUG_ENV -Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=9999";
#JAVA_OPTS="$JAVA_OPTS $debug";

nohup /data/web/jdk/bin/java $JAVA_OPTS -jar ./target/comment-async-service-2.0.jar &

exit 0 

