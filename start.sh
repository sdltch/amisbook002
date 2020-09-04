
#!/bin/sh

## 启动测试脚本
        echo "-------------开始启动--------------"
        echo "启动测试脚本"
        mvn clean compile install package
        dates=`date "+%Y-%m-%d_%H:%M:%S"`linuxreport.html
        mv amisrobot/linuxreport/linuxreport.html amisrobot/linuxreport/reporthistory/$dates
        mvn exec:java -Dexec.mainClass="com.example.amisbook002.MainStart"



