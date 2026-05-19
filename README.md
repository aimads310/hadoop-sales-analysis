# Hadoop Sales Analysis - AWS EC2

تحليل بيانات المبيعات باستخدام Apache Hadoop MapReduce على AWS EC2

## بيئة التشغيل
- AWS EC2 - Ubuntu
- Apache Hadoop 3.3.6
- Java OpenJDK 11

## هيكل المشروع
- `code/` — ملفات Java (SalesAnalysis + RegionalSalesAnalysis)
- `data/` — بيانات المبيعات (100,000 سجل)

## نتائج البنشمارك
| عدد السجلات | وقت التنفيذ |
|-------------|-------------|
| 25,000 | 4.16 ثانية |
| 50,000 | 5.11 ثانية |
| 75,000 | 5.13 ثانية |
| 100,000 | 5.21 ثانية |

## تشغيل المشروع
```bash
javac --release 11 \
  -classpath "$HADOOP_HOME/share/hadoop/common/*:$HADOOP_HOME/share/hadoop/mapreduce/*" \
  -d build code/SalesAnalysis.java code/RegionalSalesAnalysis.java
jar -cf sales_analysis.jar -C build .
hadoop jar sales_analysis.jar SalesAnalysis /input /output
```
