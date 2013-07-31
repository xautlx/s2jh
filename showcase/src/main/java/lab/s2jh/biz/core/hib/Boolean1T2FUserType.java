package lab.s2jh.biz.core.hib;

import java.io.Serializable;

import org.hibernate.dialect.Dialect;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.DiscriminatorType;
import org.hibernate.type.PrimitiveType;
import org.hibernate.type.StringType;
import org.hibernate.type.descriptor.java.BooleanTypeDescriptor;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

public class Boolean1T2FUserType extends AbstractSingleColumnStandardBasicType<Boolean> implements
        PrimitiveType<Boolean>, DiscriminatorType<Boolean> {

    public static final Boolean1T2FUserType INSTANCE = new Boolean1T2FUserType();

    public Boolean1T2FUserType() {
        super(VarcharTypeDescriptor.INSTANCE, new BooleanTypeDescriptor( '1', '2' ));
    }

    public String getName() {
        return "boolean_1t2f_char_number";
    }

    public Class getPrimitiveClass() {
        return boolean.class;
    }

    public Boolean stringToObject(String xml) throws Exception {
        return fromString(xml);
    }

    public Serializable getDefaultValue() {
        return Boolean.FALSE;
    }

    public String objectToSQLString(Boolean value, Dialect dialect) throws Exception {
        return StringType.INSTANCE.objectToSQLString(value.booleanValue() ? "1" : "2", dialect);
    }
}
