package de.theo.json.schema.codegen.model;

public abstract class NumberType extends BaseType {

    private Integer multipleOf;
    private Integer minimum;
    private Integer exclusiveMinimum;
    private Integer maximum;
    private Integer exclusiveMaximum;

    public NumberType(String name, Integer multipleOf, Integer minimum, Integer exclusiveMinimum, Integer maximum, Integer exclusiveMaximum) {
        super(name);
        this.multipleOf = multipleOf;
        this.minimum = minimum;
        this.exclusiveMinimum = exclusiveMinimum;
        this.maximum = maximum;
        this.exclusiveMaximum = exclusiveMaximum;
    }

    @Override
    public String toString() {
        return "NumberType{" +
                "multipleOf=" + multipleOf +
                ", minimum=" + minimum +
                ", exclusiveMinimum=" + exclusiveMinimum +
                ", maximum=" + maximum +
                ", exclusiveMaximum=" + exclusiveMaximum +
                "} " + super.toString();
    }
}
