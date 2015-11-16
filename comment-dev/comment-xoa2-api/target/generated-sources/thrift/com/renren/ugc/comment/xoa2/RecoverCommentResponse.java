/**
 * Autogenerated by Thrift Compiler (0.8.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.renren.ugc.comment.xoa2;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecoverCommentResponse implements org.apache.thrift.TBase<RecoverCommentResponse, RecoverCommentResponse._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("RecoverCommentResponse");

  private static final org.apache.thrift.protocol.TField RECOVERED_FIELD_DESC = new org.apache.thrift.protocol.TField("recovered", org.apache.thrift.protocol.TType.BOOL, (short)1);
  private static final org.apache.thrift.protocol.TField BASE_REP_FIELD_DESC = new org.apache.thrift.protocol.TField("baseRep", org.apache.thrift.protocol.TType.STRUCT, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new RecoverCommentResponseStandardSchemeFactory());
    schemes.put(TupleScheme.class, new RecoverCommentResponseTupleSchemeFactory());
  }

  /**
   * 是否已经恢复
   */
  public boolean recovered; // optional
  /**
   * 响应信息
   */
  public com.renren.xoa2.BaseResponse baseRep; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * 是否已经恢复
     */
    RECOVERED((short)1, "recovered"),
    /**
     * 响应信息
     */
    BASE_REP((short)2, "baseRep");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // RECOVERED
          return RECOVERED;
        case 2: // BASE_REP
          return BASE_REP;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __RECOVERED_ISSET_ID = 0;
  private BitSet __isset_bit_vector = new BitSet(1);
  private _Fields optionals[] = {_Fields.RECOVERED,_Fields.BASE_REP};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.RECOVERED, new org.apache.thrift.meta_data.FieldMetaData("recovered", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    tmpMap.put(_Fields.BASE_REP, new org.apache.thrift.meta_data.FieldMetaData("baseRep", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.renren.xoa2.BaseResponse.class)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(RecoverCommentResponse.class, metaDataMap);
  }

  public RecoverCommentResponse() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public RecoverCommentResponse(RecoverCommentResponse other) {
    __isset_bit_vector.clear();
    __isset_bit_vector.or(other.__isset_bit_vector);
    this.recovered = other.recovered;
    if (other.isSetBaseRep()) {
      this.baseRep = new com.renren.xoa2.BaseResponse(other.baseRep);
    }
  }

  public RecoverCommentResponse deepCopy() {
    return new RecoverCommentResponse(this);
  }

  @Override
  public void clear() {
    setRecoveredIsSet(false);
    this.recovered = false;
    this.baseRep = null;
  }

  /**
   * 是否已经恢复
   */
  public boolean isRecovered() {
    return this.recovered;
  }

  /**
   * 是否已经恢复
   */
  public RecoverCommentResponse setRecovered(boolean recovered) {
    this.recovered = recovered;
    setRecoveredIsSet(true);
    return this;
  }

  public void unsetRecovered() {
    __isset_bit_vector.clear(__RECOVERED_ISSET_ID);
  }

  /** Returns true if field recovered is set (has been assigned a value) and false otherwise */
  public boolean isSetRecovered() {
    return __isset_bit_vector.get(__RECOVERED_ISSET_ID);
  }

  public void setRecoveredIsSet(boolean value) {
    __isset_bit_vector.set(__RECOVERED_ISSET_ID, value);
  }

  /**
   * 响应信息
   */
  public com.renren.xoa2.BaseResponse getBaseRep() {
    return this.baseRep;
  }

  /**
   * 响应信息
   */
  public RecoverCommentResponse setBaseRep(com.renren.xoa2.BaseResponse baseRep) {
    this.baseRep = baseRep;
    return this;
  }

  public void unsetBaseRep() {
    this.baseRep = null;
  }

  /** Returns true if field baseRep is set (has been assigned a value) and false otherwise */
  public boolean isSetBaseRep() {
    return this.baseRep != null;
  }

  public void setBaseRepIsSet(boolean value) {
    if (!value) {
      this.baseRep = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case RECOVERED:
      if (value == null) {
        unsetRecovered();
      } else {
        setRecovered((Boolean)value);
      }
      break;

    case BASE_REP:
      if (value == null) {
        unsetBaseRep();
      } else {
        setBaseRep((com.renren.xoa2.BaseResponse)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case RECOVERED:
      return Boolean.valueOf(isRecovered());

    case BASE_REP:
      return getBaseRep();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case RECOVERED:
      return isSetRecovered();
    case BASE_REP:
      return isSetBaseRep();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof RecoverCommentResponse)
      return this.equals((RecoverCommentResponse)that);
    return false;
  }

  public boolean equals(RecoverCommentResponse that) {
    if (that == null)
      return false;

    boolean this_present_recovered = true && this.isSetRecovered();
    boolean that_present_recovered = true && that.isSetRecovered();
    if (this_present_recovered || that_present_recovered) {
      if (!(this_present_recovered && that_present_recovered))
        return false;
      if (this.recovered != that.recovered)
        return false;
    }

    boolean this_present_baseRep = true && this.isSetBaseRep();
    boolean that_present_baseRep = true && that.isSetBaseRep();
    if (this_present_baseRep || that_present_baseRep) {
      if (!(this_present_baseRep && that_present_baseRep))
        return false;
      if (!this.baseRep.equals(that.baseRep))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();

    boolean present_recovered = true && (isSetRecovered());
    builder.append(present_recovered);
    if (present_recovered)
      builder.append(recovered);

    boolean present_baseRep = true && (isSetBaseRep());
    builder.append(present_baseRep);
    if (present_baseRep)
      builder.append(baseRep);

    return builder.toHashCode();
  }

  public int compareTo(RecoverCommentResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    RecoverCommentResponse typedOther = (RecoverCommentResponse)other;

    lastComparison = Boolean.valueOf(isSetRecovered()).compareTo(typedOther.isSetRecovered());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetRecovered()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.recovered, typedOther.recovered);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetBaseRep()).compareTo(typedOther.isSetBaseRep());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetBaseRep()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.baseRep, typedOther.baseRep);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("RecoverCommentResponse(");
    boolean first = true;

    if (isSetRecovered()) {
      sb.append("recovered:");
      sb.append(this.recovered);
      first = false;
    }
    if (isSetBaseRep()) {
      if (!first) sb.append(", ");
      sb.append("baseRep:");
      if (this.baseRep == null) {
        sb.append("null");
      } else {
        sb.append(this.baseRep);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bit_vector = new BitSet(1);
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class RecoverCommentResponseStandardSchemeFactory implements SchemeFactory {
    public RecoverCommentResponseStandardScheme getScheme() {
      return new RecoverCommentResponseStandardScheme();
    }
  }

  private static class RecoverCommentResponseStandardScheme extends StandardScheme<RecoverCommentResponse> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, RecoverCommentResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // RECOVERED
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.recovered = iprot.readBool();
              struct.setRecoveredIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // BASE_REP
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.baseRep = new com.renren.xoa2.BaseResponse();
              struct.baseRep.read(iprot);
              struct.setBaseRepIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, RecoverCommentResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.isSetRecovered()) {
        oprot.writeFieldBegin(RECOVERED_FIELD_DESC);
        oprot.writeBool(struct.recovered);
        oprot.writeFieldEnd();
      }
      if (struct.baseRep != null) {
        if (struct.isSetBaseRep()) {
          oprot.writeFieldBegin(BASE_REP_FIELD_DESC);
          struct.baseRep.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class RecoverCommentResponseTupleSchemeFactory implements SchemeFactory {
    public RecoverCommentResponseTupleScheme getScheme() {
      return new RecoverCommentResponseTupleScheme();
    }
  }

  private static class RecoverCommentResponseTupleScheme extends TupleScheme<RecoverCommentResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, RecoverCommentResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetRecovered()) {
        optionals.set(0);
      }
      if (struct.isSetBaseRep()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetRecovered()) {
        oprot.writeBool(struct.recovered);
      }
      if (struct.isSetBaseRep()) {
        struct.baseRep.write(oprot);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, RecoverCommentResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.recovered = iprot.readBool();
        struct.setRecoveredIsSet(true);
      }
      if (incoming.get(1)) {
        struct.baseRep = new com.renren.xoa2.BaseResponse();
        struct.baseRep.read(iprot);
        struct.setBaseRepIsSet(true);
      }
    }
  }

}
