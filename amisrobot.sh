#!/bin/sh

## 启动测试脚本
        echo "-------------开始启动--------------"
                ## 启动路径
                path=./amisbook002.sh
                ## html路径
                ## myrepotpath=/amisbook/amisbook002/amisrobot/linuxreport/
                echo "启动测试脚本"
                ./amisbook002.sh
				echo "-------------睡眠60秒--------------"
				cp ../amisrobot/amisbook/linuxreport/report.html 
               