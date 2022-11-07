# OMG
## 说明
用于对象之间相互转换，类似MapStruct和Spring的BeanUtils.copyProperties().
## feature
- Spring的BeanUtils易用，直接调用静态方法。但是效率不高，且不支持类型不一致的转换，如Long<-\>String的转换。 
- MapStruct效率高，且支持类型不一致的转换。但是转换两个不同的对象，需要手动写一个接口和接口抽象方法。接口和方法多了会越来越混乱。 
- EasyMap参考了BeanUtils和MapStruct的优缺点，将两者的优点结合。
- EasyMap使用简单，根据不同的类型，直接调用静态方法，如EasyMap.mapSingleton(sourcePerson, TargetPerson.class);
- EasyMap效率高，采用在内存中生成转换的Java代码，编译并直接调用，除了第一次比较耗时，后面每次调用基本可以与MapStruct的效率持平。
## 使用方法  
### 单个对象
TargetPerson targetPeople = EasyMap.mapSingleton(sourcePerson, TargetPerson.class);  
### list
List<TargetPerson> targetPeoples = EasyMap.mapList(sourcePeoples, TargetPerson.class);  
### set  
Set<TargetPerson> targetPeoples = EasyMap.mapSet(sourcePeoples, TargetPerson.class);  
## 问题
- 第一次编译的时间总是很长，1.7s多，得想个办法在启动后主动触发一次编译  
## 需要支持吗？疑问  
- long to int?  
- double to float?  
## 局限性
- EasyMap.mapSingleton不支持泛型
- EasyMap.mapList，不支持多重泛型，即只支持List<SourcePerson\> <\-\> List\<TargetPerson\>，
  不支持List<List<SourcePerson\>\>和List<Map<String,SourcePerson\>\>  
- 不支持最外层的简单类型的转换即int<->String，int<->long
