package io.github.lyrric.generated;
public class TargetPerson$Conversion1 {

    public io.github.lyrric.test.model.TargetPerson convert(io.github.lyrric.test.model.SourcePerson source){
        io.github.lyrric.test.model.TargetPerson target = new io.github.lyrric.test.model.TargetPerson();
        if(source.getId() != null){
            target.setId(source.getId());
        }
        return target;
    }
}

