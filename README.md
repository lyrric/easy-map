# OMG
## 局限性
- List <-> List时，不支持多重泛型，即只支持List<SourcePerson> <-> List<TargetPerson>，
  不支持List<List<SourcePerson>>和List<Map<String，SourcePerson>>  
- 不支持最外层的简单类型的转换即int<->String，int<->long
