package pl.kat.ue.id3.table;

import java.util.Collection;
import java.util.HashSet;

public class AttributeMetadata extends HashSet<ValueMetadata> {

    public AttributeMetadata(Collection<? extends ValueMetadata> c) {
        super(c);
    }
}