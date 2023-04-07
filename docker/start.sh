#!/bin/sh
export AUTH_APP=ix-auth.jar
export SYSTEM_APP=ix-server-system.jar
export GATEWAY_APP=ix-gateway.jar
export DFS_APP=ix-server-dfs.jar

export AUTH_APP_port=8889
export SYSTEM_APP_port=8082
export GATEWAY_APP_port=8080
export DFS_APP_port=8084

case "$1" in

start)

        ## 启动system
        echo "--------开始启动SYSTEM_APP---------------"
        nohup java -jar $SYSTEM_APP &
        SYSTEM_APP_pid=`lsof -i:$SYSTEM_APP_port|grep "LISTEN"|awk '{print $2}'`
        until [ -n "$SYSTEM_APP_pid" ]
            do
              SYSTEM_APP_pid=`lsof -i:$SYSTEM_APP_port|grep "LISTEN"|awk '{print $2}'`
            done
        echo "SYSTEM_APP pid is $SYSTEM_APP_pid"
        echo "---------SYSTEM_APP 启动成功-----------"

        ## 启动gateway
        echo "--------开始启动GATEWAY_APP---------------"
        nohup java -jar $GATEWAY_APP &
        GATEWAY_APP_pid=`lsof -i:$GATEWAY_APP_port|grep "LISTEN"|awk '{print $2}'`
        until [ -n "$GATEWAY_APP_pid" ]
            do
              GATEWAY_APP_pid=`lsof -i:$GATEWAY_APP_port|grep "LISTEN"|awk '{print $2}'`
            done
        echo "GATEWAY_APP pid is $GATEWAY_APP_pid"
        echo "---------GATEWAY_APP 启动成功-----------"

        ## 启动auth
        echo "--------AUTH_APP 开始启动--------------"
        nohup java -jar $AUTH_APP &
        AUTH_APP_pid=`lsof -i:$AUTH_APP_port|grep "LISTEN"|awk '{print $2}'`
        until [ -n "$AUTH_APP_pid" ]
            do
              AUTH_APP_pid=`lsof -i:$AUTH_APP_port|grep "LISTEN"|awk '{print $2}'`
            done
        echo "AUTH_APP pid is $AUTH_APP_pid"
        echo "--------AUTH_APP 启动成功--------------"

        ## 启动dfs
        echo "--------开始启动DFS_APP---------------"
        nohup java -jar $DFS_APP &
        DFS_APP_pid=`lsof -i:$DFS_APP_port|grep "LISTEN"|awk '{print $2}'`
        until [ -n "$DFS_APP_pid" ]
            do
              AUTH_APP_pid=`lsof -i:$DFS_APP_port|grep "LISTEN"|awk '{print $2}'`
            done
        echo "DFS_APP pid is $DFS_APP_pid"
        echo "---------DFS_APP 启动成功-----------"

        echo "===startAll success==="
        ;;

 stop)
        P_ID=`ps -ef | grep -w $AUTH_APP | grep -v "grep" | awk '{print $2}'`
        if [ "$P_ID" == "" ]; then
            echo "===AUTH_APP process not exists or stop success"
        else
            kill -9 $P_ID
            echo "AUTH_APP killed success"
        fi
		P_ID=`ps -ef | grep -w $SYSTEM_APP | grep -v "grep" | awk '{print $2}'`
        if [ "$P_ID" == "" ]; then
            echo "===SYSTEM_APP process not exists or stop success"
        else
            kill -9 $P_ID
            echo "SYSTEM_APP killed success"
        fi
		 P_ID=`ps -ef | grep -w $GATEWAY_APP | grep -v "grep" | awk '{print $2}'`
        if [ "$P_ID" == "" ]; then
            echo "===GATEWAY_APP process not exists or stop success"
        else
            kill -9 $P_ID
            echo "GATEWAY_APP killed success"
        fi
		 P_ID=`ps -ef | grep -w $DFS_APP | grep -v "grep" | awk '{print $2}'`
        if [ "$P_ID" == "" ]; then
            echo "===DFS_APP process not exists or stop success"
        else
            kill -9 $P_ID
            echo "DFS_APP killed success"
        fi

        echo "===stop success==="
        ;;

restart)
        $0 stop
        sleep 2
        $0 start
        echo "===restart success==="
        ;;
esac
exit 0
