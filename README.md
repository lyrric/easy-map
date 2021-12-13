# OMG
## 问题
- 第一次编译的时间总是很长，1.7s多，得想个办法在启动后主动触发一次编译  
## 局限性
- List <-> List时，不支持多重泛型，即只支持List<SourcePerson> <-> List<TargetPerson>，
  不支持List<List<SourcePerson>>和List<Map<String，SourcePerson>>  
- 不支持最外层的简单类型的转换即int<->String，int<->long
