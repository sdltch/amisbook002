
#!/bin/sh

## 启动测试脚本
        echo "-------------开始启动--------------"
        echo "启动测试脚本"
        mvn clean compile install package
        dates=`date "+%Y-%m-%d_%H:%M:%S"`linuxreport.html
        myFile="/root/.jenkins/workspace/amisbook002/amisrobot/linuxreport/linuxreport.html"
        # 这里的-f参数判断$myFile是否存在
        if [ -f "$myFile" ]; then
          mv amisrobot/linuxreport/linuxreport.html amisrobot/linuxreport/reporthistory/$dates
        fi
        mvn exec:java -Dexec.mainClass="com.example.amisbook002.MainStart"



